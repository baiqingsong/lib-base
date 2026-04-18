# lib-base

Android 实用工具类库

## 引用

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.baiqingsong:lib-base:Tag'
}
```

## 权限

根据实际使用的工具类，在 `AndroidManifest.xml` 中添加对应权限：

```xml
<!-- 存储相关 (LCrashHandlerUtil) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- 安装应用 (LAppUtil, LSystemUtil) -->
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />

<!-- 电话/短信 (LSystemUtil) -->
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.SEND_SMS" />
```

## 类说明

### `com.dawn.library.LAppUtil` 应用工具类

| 方法 | 说明 |
|------|------|
| `getVersionName(Context)` | 获取版本名称 |
| `getVersionCode(Context)` | 获取版本号 |
| `getAppSize(Context, String)` | 获取应用大小（字节） |
| `installApk(Context, String, String)` | 安装 APK（支持 FileProvider） |
| `uninstallApk(Context, String)` | 卸载应用 |
| `runApp(Context, String)` | 启动指定应用 |
| `isAppInstalled(Context, String)` | 判断应用是否已安装 |
| `getAppName(Context)` | 获取应用名称 |
| `cleanCache(Context)` | 清除应用内部缓存 |
| `cleanDatabases(Context)` | 清除应用内部数据库 |
| `cleanSharedPreference(Context)` | 清除应用 SharedPreferences |
| `isDebug(Context)` | 判断是否为 Debug 模式 |

### `com.dawn.library.LCipherUtil` 加密工具类

| 方法 | 说明 |
|------|------|
| `encryptMD5(String)` | MD5 加密（字符串） |
| `encryptMD5(InputStream)` | MD5 加密（输入流） |
| `base64Encode(String)` | Base64 编码 |
| `base64Decode(String)` | Base64 解码 |
| `encryptSHA1(String)` | SHA1 加密（字符串） |
| `encryptSHA1(File)` | SHA1 加密（文件） |
| `xorEncode(String, String)` | 异或加密 |
| `xorDecode(String, String)` | 异或解密 |
| `encryptAES(String, String)` | AES 加密 |
| `decryptAES(String, String)` | AES 解密 |

### `com.dawn.library.LColorUtil` 颜色工具类

| 方法 | 说明 |
|------|------|
| `colorToHex(int)` | 颜色值转十六进制字符串 |
| `hexToColor(String)` | 十六进制字符串转颜色值 |
| `rgbToColor(int, int, int)` | RGB 生成颜色值 |
| `argbToColor(int, int, int, int)` | ARGB 生成颜色值 |
| `getRed(int)` | 获取红色分量 |
| `getGreen(int)` | 获取绿色分量 |
| `getBlue(int)` | 获取蓝色分量 |
| `getAlpha(int)` | 获取透明度分量 |
| `setAlpha(int, int)` | 设置透明度 |
| `blendColors(int, int, float)` | 颜色混合 |
| `darkenColor(int, float)` | 加深颜色 |
| `lightenColor(int, float)` | 变亮颜色 |
| `isDarkColor(int)` | 是否为深色 |
| `isLightColor(int)` | 是否为浅色 |
| `getComplementaryColor(int)` | 获取互补色 |
| `randomColor()` | 随机颜色 |
| `randomColor(int)` | 指定透明度的随机颜色 |

### `com.dawn.library.LConvertUtil` 类型转换工具类

| 方法 | 说明 |
|------|------|
| `toInt(String, int)` | 字符串转 int（异常返回默认值） |
| `toLong(String, long)` | 字符串转 long |
| `toFloat(String, float)` | 字符串转 float |
| `toDouble(String, double)` | 字符串转 double |
| `toBoolean(String, boolean)` | 字符串转 boolean |
| `intToBytes(int)` | int 转 byte 数组 |
| `bytesToInt(byte[])` | byte 数组转 int |
| `longToBytes(long)` | long 转 byte 数组 |
| `bytesToLong(byte[])` | byte 数组转 long |
| `fenToYuan(long)` | 分转元 |
| `yuanToFen(String)` | 元转分 |
| `celsiusToFahrenheit(double)` | 摄氏度转华氏度 |
| `fahrenheitToCelsius(double)` | 华氏度转摄氏度 |

### `com.dawn.library.LCrashHandlerUtil` 崩溃处理工具类

| 方法 | 说明 |
|------|------|
| `getInstance()` | 获取单例实例 |
| `setListener(OnSetCrashHandlerListener)` | 设置崩溃回调监听 |
| `init(Context)` | 初始化（注册为默认异常处理器） |
| `uncaughtException(Thread, Throwable)` | 异常捕获回调（系统自动调用） |
| `getFilePath(Context)` | 获取日志文件存储路径 |
| `getFileName(Date)` | 获取日志文件名 |

### `com.dawn.library.LDateUtil` 日期工具类

| 方法 | 说明 |
|------|------|
| `longToDateTime(long)` | 时间戳转日期时间字符串 |
| `longToDate(long)` | 时间戳转日期字符串 |
| `longToTime(long)` | 时间戳转时间字符串 |
| `getTime()` | 获取当前时间 |
| `getDate()` | 获取当前日期 |
| `getDateTime()` | 获取当前日期时间 |
| `getDateTime(String)` | 自定义格式获取日期时间 |
| `dateTimeToLong(String)` | 日期时间字符串转时间戳 |
| `dateToLong(String)` | 日期字符串转时间戳 |
| `getCurrentTimeMillis()` | 获取当前时间戳（毫秒） |
| `getDaysBetween(long, long)` | 计算两个日期之间的天数差 |
| `isToday(long)` | 判断是否为今天 |
| `getDayOfWeek(long)` | 获取星期几 |
| `getDayOfWeekName(long)` | 获取星期几的中文名称 |
| `addDays(long, int)` | 增加天数 |
| `addHours(long, int)` | 增加小时 |
| `addMinutes(long, int)` | 增加分钟 |
| `formatDuration(long)` | 格式化时长 |
| `isLeapYear(int)` | 判断是否为闰年 |
| `getAge(long)` | 根据生日计算年龄 |
| `getDaysInMonth(int, int)` | 获取指定月份天数 |
| `getFriendlyTime(long)` | 友好时间描述（如 "3分钟前"） |
| `getStartOfDay()` | 获取今天 00:00 时间戳 |
| `getStartOfDay(long)` | 获取指定日期 00:00 时间戳 |
| `getEndOfDay(long)` | 获取指定日期 23:59:59 时间戳 |

### `com.dawn.library.LDBOperationUtil` 数据库操作工具类

| 方法 | 说明 |
|------|------|
| `newSingleInstance(Context, String, boolean)` | 初始化数据库 |
| `getLiteOrm()` | 获取 LiteOrm 实例 |
| `insert(T)` | 插入单条数据 |
| `insertAll(List)` | 批量插入 |
| `queryAll(Class)` | 查询全部 |
| `queryByWhere(Class, String, String)` | 单条件查询 |
| `queryByWhere(Class, String, String[])` | 多值条件查询 |
| `queryByWhereTwo(Class, ...)` | 双条件查询 |
| `queryByWhereLength(Class, String, String[], int, int)` | 分页 + 多值查询 |
| `queryByWhereLength(Class, String, String, int, int)` | 分页 + 单值查询 |
| `queryByWhereOrder(Class, String, String[], String)` | 条件查询 + 排序 |
| `queryByWhereLike(Class, String, String)` | 模糊查询 |
| `deleteWhere(Class, String, String[])` | 多值条件删除 |
| `deleteWhere(Class, String, String)` | 单值条件删除 |
| `delete(T)` | 删除单条数据 |
| `deleteAll(Class)` | 清空表数据 |
| `deleteDatabase()` | 删除数据库文件 |
| `reCreateDatabase()` | 重建数据库 |
| `deleteSection(Class, long, long, String)` | 删除指定区间数据 |
| `deleteList(List)` | 批量删除 |
| `update(T)` | 更新单条数据 |
| `updateAll(List)` | 批量更新 |
| `queryCount(Class)` | 查询表中数据条数 |

### `com.dawn.library.LDeviceUtil` 设备工具类

| 方法 | 说明 |
|------|------|
| `getDeviceId()` | 获取主板序列号 |
| `loadFileAsString(String)` | 读取系统文件内容 |
| `getCpuSerial()` | 获取 CPU 序列号 |
| `getMemInfo()` | 获取内存信息 |
| `getSystemVersion()` | 获取系统版本信息 |
| `getWifiMac()` | 获取 WiFi MAC 地址 |
| `getManufacturer()` | 获取设备制造商 |
| `getModel()` | 获取设备型号 |
| `getBrand()` | 获取设备品牌 |
| `getAndroidVersion()` | 获取 Android 版本号 |
| `getSDKVersion()` | 获取 SDK 版本号 |
| `getFingerprint()` | 获取设备指纹 |
| `getHardware()` | 获取硬件名称 |
| `getDisplay()` | 获取显示名称 |
| `getLanguage()` | 获取系统语言 |
| `getCountry()` | 获取国家/地区代码 |
| `getAvailableMemory(Context)` | 获取可用内存大小 |
| `getTotalMemory(Context)` | 获取总内存大小 |
| `isLowMemory(Context)` | 判断是否低内存 |
| `getInternalStorageAvailable()` | 获取可用存储空间 |
| `getInternalStorageTotal()` | 获取总存储空间 |
| `getDeviceInfo()` | 获取完整设备信息字符串 |
| `isEmulator()` | 判断是否为模拟器 |

### `com.dawn.library.LJsonUtil` JSON工具类

| 方法 | 说明 |
|------|------|
| `objToJson(Object)` | 对象转 JSON 字符串 |
| `jsonToObj(String, Class)` | JSON 字符串转对象 |
| `listToJson(List)` | 集合转 JSON 字符串 |
| `jsonToList(String, Class)` | JSON 字符串转集合 |

### `com.dawn.library.LNumeralUtil` 数字工具类

| 方法 | 说明 |
|------|------|
| `isValidNumber(String)` | 判断是否为有效数字 |
| `isValidAmount(String)` | 判断是否为有效金额 |
| `formatAmount(double)` | 格式化金额 |
| `formatNumber(double, int)` | 格式化数字（指定小数位） |

### `com.dawn.library.LShellUtil` Shell工具类

| 方法 | 说明 |
|------|------|
| `execCommand(String, boolean)` | 执行 Shell 命令 |
| `execCommand(String[], boolean)` | 批量执行 Shell 命令 |
| `isRooted()` | 判断设备是否已 root |

### `com.dawn.library.LSPUtil` SharedPreferences工具类

| 方法 | 说明 |
|------|------|
| `setSP(Context, String, Object)` | 存储值（默认 SP 文件） |
| `getSP(Context, String, Object)` | 读取值（默认 SP 文件） |
| `cleanAllSP(Context)` | 清除所有 SP 数据 |
| `removeSP(Context, String)` | 移除指定 key |
| `containsKey(Context, String)` | 判断是否包含指定 key |
| `getAllKeys(Context)` | 获取所有 key |
| `setSP(Context, String, String, Object)` | 存储值（指定 SP 文件名） |
| `getSP(Context, String, String, Object)` | 读取值（指定 SP 文件名） |

### `com.dawn.library.LStringUtil` 字符串工具类

| 方法 | 说明 |
|------|------|
| `parseEmpty(String)` | 空字符串处理（null → ""） |
| `isEmpty(String)` | 判断是否为空字符串 |
| `strLength(String)` | 获取字符串长度 |
| `isChinese(String)` | 判断是否全是中文 |
| `isContainChinese(String)` | 判断是否包含中文 |
| `decimalFormat(double, String)` | 数字格式化 |
| `toHexString(byte[])` | 字节数组转十六进制字符串 |
| `toByteArray(String)` | 十六进制字符串转字节数组 |
| `format(int)` | int 转十六进制字符串 |
| `parseHex2Opposite(String)` | 十六进制取反 |
| `getXor(String)` | 异或结果 |
| `capitalizeFirst(String)` | 首字母大写 |
| `lowercaseFirst(String)` | 首字母小写 |
| `camelToUnderline(String)` | 驼峰转下划线 |
| `underlineToCamel(String)` | 下划线转驼峰 |
| `isNumeric(String)` | 判断是否为数字 |
| `isInteger(String)` | 判断是否为整数 |
| `removeSpaces(String)` | 移除所有空格 |
| `reverse(String)` | 反转字符串 |
| `equals(String, String)` | 判断是否相等（null 安全） |
| `equalsIgnoreCase(String, String)` | 忽略大小写判断是否相等 |
| `defaultIfEmpty(String, String)` | 为空时返回默认值 |
| `join(String, String...)` | 字符串拼接（数组） |
| `join(String, List)` | 字符串拼接（集合） |
| `repeat(String, int)` | 重复字符串 |
| `abbreviate(String, int)` | 截断并添加省略号 |
| `containsIgnoreCase(String, String)` | 忽略大小写包含判断 |
| `safeSubstring(String, int, int)` | 安全截取子串 |
| `safeTrim(String)` | 安全 trim |
| `countMatches(String, String)` | 统计子串出现次数 |
| `padLeft(String, int, char)` | 左填充 |
| `padRight(String, int, char)` | 右填充 |

### `com.dawn.library.LSystemUtil` 系统工具类

| 方法 | 说明 |
|------|------|
| `reboot()` | 重启手机 |
| `openSettings(Context)` | 打开系统设置 |
| `installApk(Context, File, String)` | 安装 APK |
| `showSoftInput(Context)` | 显示软键盘 |
| `hideSoftInput(Context)` | 隐藏软键盘 |
| `getScreenWidth(Context)` | 获取屏幕宽度（px） |
| `getScreenHeight(Context)` | 获取屏幕高度（px） |
| `getScreenDensity(Context)` | 获取屏幕密度 |
| `dp2px(Context, float)` | dp 转 px |
| `px2dp(Context, float)` | px 转 dp |
| `sp2px(Context, float)` | sp 转 px |
| `px2sp(Context, float)` | px 转 sp |
| `getStatusBarHeight(Context)` | 获取状态栏高度 |
| `getNavigationBarHeight(Context)` | 获取导航栏高度 |
| `copyToClipboard(Context, String)` | 复制文本到剪贴板 |
| `getFromClipboard(Context)` | 从剪贴板获取文本 |
| `openDialer(Context, String)` | 打开拨号界面 |
| `sendSMS(Context, String, String)` | 发送短信 |

### `com.dawn.library.LValidateUtil` 验证工具类

| 方法 | 说明 |
|------|------|
| `isValidEmail(String)` | 验证邮箱 |
| `isValidPhone(String)` | 验证手机号 |
| `isValidIdCard(String)` | 验证身份证号（含校验位） |
| `isValidUrl(String)` | 验证 URL |
| `isValidIP(String)` | 验证 IP 地址 |
| `isStrongPassword(String)` | 验证强密码 |
| `isValidBankCard(String)` | 验证银行卡号（Luhn 算法） |
| `isValidPlateNumber(String)` | 验证车牌号 |
| `isValidQQ(String)` | 验证 QQ 号 |
| `isValidWechat(String)` | 验证微信号 |
| `isValidZipCode(String)` | 验证邮政编码 |
| `formatPhone(String)` | 手机号脱敏 |
| `formatIdCard(String)` | 身份证脱敏 |
| `formatBankCard(String)` | 银行卡号脱敏 |
| `formatEmail(String)` | 邮箱脱敏 |

### `com.dawn.library.LZipUtil` 压缩解压工具类

| 方法 | 说明 |
|------|------|
| `unzip(String, String)` | 解压 zip 文件到指定目录 |


