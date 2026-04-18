package com.dawn.library;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * RxJava3 线程调度封装工具
 * <p>
 * 注意事项：
 * 1. countdown 返回的 Disposable 必须在页面销毁时调用 dispose()，否则会内存泄漏
 * 2. 多个倒计时互相独立，各自持有独立的 Disposable
 * 3. 推荐使用 CompositeDisposable 统一管理多个任务的生命周期
 */
@SuppressWarnings("unused")
public class RxTask {
    private static final String TAG = "RxTask";
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 执行有返回值的异步任务
     * @param backgroundTask 后台任务（在IO线程执行）
     * @param uiTask 结果处理（在主线程执行）
     * @param <T> 返回值类型
     * @return 可取消的Disposable
     */
    public static <T> Disposable runAsync(Callable<T> backgroundTask, UiTask<T> uiTask) {
        return Single.fromCallable(backgroundTask)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> { if (uiTask != null) uiTask.onSuccess(result); },
                        error -> {
                            Log.e(TAG, "RxTask runAsync error", error);
                            if (uiTask != null) uiTask.onError(error);
                        }
                );
    }

    /**
     * 执行无返回值的异步任务
     * @param runnable 后台任务（在IO线程执行）
     * @param onComplete 完成回调（在主线程执行）
     * @return 可取消的Disposable
     */
    public static Disposable runAsync(Runnable runnable, Runnable onComplete) {
        return Completable.fromRunnable(runnable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> { if (onComplete != null) onComplete.run(); },
                        error -> Log.e(TAG, "RxTask error", error)
                );
    }

    /**
     * 执行无返回值的异步任务（带错误回调）
     * @param runnable 后台任务
     * @param onComplete 完成回调
     * @param errorTask 错误回调
     * @return 可取消的Disposable
     */
    public static Disposable runAsync(Runnable runnable, Runnable onComplete, ErrorTask errorTask) {
        return Completable.fromRunnable(runnable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> { if (onComplete != null) onComplete.run(); },
                        error -> {
                            Log.e(TAG, "RxTask error", error);
                            if (errorTask != null) errorTask.onError(error);
                        }
                );
    }

    /**
     * 执行无返回值的异步任务（无回调）
     * @param runnable 后台任务
     * @return 可取消的Disposable
     */
    public static Disposable runAsync(Runnable runnable) {
        return Completable.fromRunnable(runnable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        error -> Log.e(TAG, "RxTask error", error)
                );
    }

    /**
     * 倒计时功能（间隔1秒）
     * @param totalSeconds 总秒数（必须 >= 0）
     * @param countdownTask 倒计时回调接口
     * @return 可取消的Disposable
     */
    public static Disposable countdown(int totalSeconds, CountdownTask countdownTask) {
        return countdown(totalSeconds, 1, countdownTask);
    }

    /**
     * 倒计时功能
     * <p>
     * 修复说明：
     * 1. 使用 totalSeconds/interval 精确计算 tick 数，避免多余/缺失的回调
     * 2. 移除不必要的 subscribeOn（interval 自带 computation 调度器）
     * 3. doOnDispose 通过 MAIN_HANDLER 保证回调在主线程
     * 4. 使用 AtomicBoolean 防止 dispose 后仍回调
     *
     * @param totalSeconds 总秒数（必须 >= 0）
     * @param interval 间隔时间（秒，必须 > 0）
     * @param countdownTask 倒计时回调接口
     * @return 可取消的Disposable
     */
    public static Disposable countdown(int totalSeconds, int interval, CountdownTask countdownTask) {
        if (totalSeconds < 0) {
            if (countdownTask != null) countdownTask.onError(new IllegalArgumentException("totalSeconds must be >= 0"));
            return Disposable.disposed();
        }
        if (interval <= 0) {
            if (countdownTask != null) countdownTask.onError(new IllegalArgumentException("interval must be > 0"));
            return Disposable.disposed();
        }

        // 精确计算需要发射的tick总数（含初始的第0次）
        final int tickCount = totalSeconds / interval + 1;
        final AtomicBoolean disposed = new AtomicBoolean(false);

        return Observable.interval(0, interval, TimeUnit.SECONDS)
                .take(tickCount)
                .map(tick -> {
                    int remaining = totalSeconds - (int) (tick * interval);
                    return Math.max(0, remaining);
                })
                // interval 默认在 computation 调度器，无需 subscribeOn
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(() -> {
                    if (disposed.compareAndSet(false, true)) {
                        if (countdownTask != null) {
                            // 确保 onDisposed 在主线程回调
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                countdownTask.onDisposed();
                            } else {
                                MAIN_HANDLER.post(countdownTask::onDisposed);
                            }
                        }
                    }
                })
                .subscribe(
                        remainingSeconds -> {
                            if (!disposed.get() && countdownTask != null) {
                                countdownTask.onNext(remainingSeconds);
                            }
                        },
                        error -> {
                            if (!disposed.get() && countdownTask != null) {
                                countdownTask.onError(error);
                            }
                        },
                        () -> {
                            if (!disposed.get() && countdownTask != null) {
                                countdownTask.onComplete();
                            }
                        }
                );
    }

    /**
     * 在主线程执行任务
     * @param runnable 要在主线程执行的任务
     */
    public static void post(Runnable runnable) {
        if (runnable == null) return;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            MAIN_HANDLER.post(runnable);
        }
    }

    /**
     * 延迟在主线程执行任务
     * @param runnable 要在主线程执行的任务
     * @param delayMillis 延迟时间（毫秒，必须 >= 0）
     * @return 可取消的Disposable
     */
    public static Disposable postDelayed(Runnable runnable, long delayMillis) {
        if (runnable == null) return Disposable.disposed();
        if (delayMillis < 0) delayMillis = 0;
        return Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tick -> runnable.run(),
                        error -> Log.e(TAG, "RxTask postDelayed error", error)
                );
    }

    /**
     * 创建一个 CompositeDisposable 用于统一管理多个任务的生命周期
     * @return CompositeDisposable
     */
    public static CompositeDisposable createComposite() {
        return new CompositeDisposable();
    }

    /**
     * 安全地取消 Disposable
     * @param disposable 待取消的 Disposable
     */
    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 安全地取消 CompositeDisposable 中的所有任务
     * @param compositeDisposable 待清理的 CompositeDisposable
     */
    public static void disposeAll(CompositeDisposable compositeDisposable) {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    /** 倒计时回调接口 */
    public interface CountdownTask {
        /** 每个间隔回调剩余秒数 */
        void onNext(int remainingSeconds);
        /** 倒计时正常结束 */
        void onComplete();
        /** 倒计时异常 */
        void onError(Throwable throwable);
        /** 倒计时被外部取消（dispose） */
        default void onDisposed() {}
    }

    /** UI线程回调接口 */
    public interface UiTask<T> {
        void onSuccess(T result);
        void onError(Throwable throwable);
    }

    /** 错误回调接口 */
    public interface ErrorTask {
        void onError(Throwable throwable);
    }
}
