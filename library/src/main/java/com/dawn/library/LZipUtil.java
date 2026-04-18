package com.dawn.library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 压缩解压工具类
 */
@SuppressWarnings("unused")
public class LZipUtil {

    /**
     * 解压ZIP文件
     * @param zipFilePath ZIP文件路径
     * @param destDir 目标目录
     * @throws IOException IO异常
     * @throws SecurityException 安全异常（Zip Slip攻击检测）
     */
    public static void unzip(String zipFilePath, String destDir) throws IOException {
        if (zipFilePath == null || destDir == null) {
            throw new IllegalArgumentException("zipFilePath and destDir cannot be null");
        }

        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String canonicalDestDir = dir.getCanonicalPath();
        if (!canonicalDestDir.endsWith(File.separator)) {
            canonicalDestDir += File.separator;
        }

        try (ZipInputStream zipIn = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFilePath)))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                String entryPath = destDir + File.separator + entry.getName();
                File entryFile = new File(entryPath);

                // 防止 Zip Slip 漏洞攻击
                String canonicalPath = entryFile.getCanonicalPath();
                if (!canonicalPath.startsWith(canonicalDestDir)) {
                    throw new SecurityException("Zip Slip detected: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    File parent = entryFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    try (BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(entryFile))) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = zipIn.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zipIn.closeEntry();
            }
        }
    }

    /**
     * 安全解压ZIP文件（不抛出异常）
     * @param zipFilePath ZIP文件路径
     * @param destDir 目标目录
     * @return 是否解压成功
     */
    public static boolean unzipSafe(String zipFilePath, String destDir) {
        try {
            unzip(zipFilePath, destDir);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
