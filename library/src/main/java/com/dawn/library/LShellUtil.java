package com.dawn.library;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Shell命令执行工具类
 */
@SuppressWarnings("unused")
public class LShellUtil {

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private LShellUtil() {
        throw new AssertionError();
    }

    /**
     * 杀死指定进程
     * @param pid 进程ID
     */
    public static void killProcess(int pid) {
        execCommand("kill -9 " + pid, true);
    }

    /**
     * 检查是否拥有root权限
     * @return 是否有root权限
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * 执行shell命令（默认返回结果）
     * @param command 命令
     * @param isRoot 是否需要root
     * @return 命令执行结果
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    /**
     * 执行shell命令列表（默认返回结果）
     * @param commands 命令列表
     * @param isRoot 是否需要root
     * @return 命令执行结果
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[0]), isRoot, true);
    }

    /**
     * 执行shell命令数组（默认返回结果）
     * @param commands 命令数组
     * @param isRoot 是否需要root
     * @return 命令执行结果
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * 执行shell命令
     * @param command 命令
     * @param isRoot 是否需要root
     * @param isNeedResultMsg 是否需要返回结果信息
     * @return 命令执行结果
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * 执行shell命令列表
     * @param commands 命令列表
     * @param isRoot 是否需要root
     * @param isNeedResultMsg 是否需要返回结果信息
     * @return 命令执行结果
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[0]), isRoot, isNeedResultMsg);
    }

    /**
     * 执行shell命令数组
     * @param commands 命令数组
     * @param isRoot 是否需要root
     * @param isNeedResultMsg 是否需要返回结果信息
     * @return 命令执行结果
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
                if (successResult != null) successResult.close();
                if (errorResult != null) errorResult.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) process.destroy();
        }
        return new CommandResult(result,
                successMsg == null ? null : successMsg.toString(),
                errorMsg == null ? null : errorMsg.toString());
    }

    /**
     * 命令执行结果
     */
    public static class CommandResult {
        /** 命令执行结果码 */
        public int result;
        /** 成功信息 */
        public String successMsg;
        /** 错误信息 */
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
