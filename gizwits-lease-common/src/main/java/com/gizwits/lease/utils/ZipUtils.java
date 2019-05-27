package com.gizwits.lease.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author lilh
 * @date 2017/8/31 12:14
 */
public class ZipUtils {

    private ZipUtils() {
    }

    public static void doCompress(String srcFile, String zipFile) throws IOException {
        doCompress(new File(srcFile), new File(zipFile));
    }

    /**
     * 文件压缩
     *
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    public static void doCompress(File srcFile, File zipFile) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            doCompress(srcFile, out);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);//记得关闭资源
        }
    }

    public static void doCompress(String fileName, ZipOutputStream out) throws IOException {
        doCompress(new File(fileName), out);
    }

    public static void doCompress(String fileName, OutputStream out) throws IOException {
        ZipOutputStream zipOutputStream;
        try {
            zipOutputStream = new ZipOutputStream(out);
            doCompress(fileName, zipOutputStream);
        } finally {
            IOUtils.closeQuietly(out);//记得关闭资源
        }
    }

    public static void doCompress(Set<String> fileNames, OutputStream out) throws IOException {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(out);
            for (String fileName : fileNames) {
                doCompress(fileName, zipOutputStream);
            }
        } finally {
            IOUtils.closeQuietly(zipOutputStream);//记得关闭资源
        }
    }

    public static void doCompress(Set<String> fileNames, HttpServletResponse response) throws IOException {
        doCompress(fileNames, response, null);
    }

    public static void doCompress(Set<String> fileNames, HttpServletResponse response, String zipFileName) throws IOException {
        if (StringUtils.isBlank(zipFileName)) {
            zipFileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        }
        if (!zipFileName.endsWith(".zip")) {
            zipFileName += ".zip";
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipFileName, "utf-8"));
        //OutputStream fout = new FileOutputStream("e:/" + zipFileName);
        try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
            for (String fileName : fileNames) {
                doCompress(fileName, out);
            }
        }

    }

    public static void doCompress(File file, ZipOutputStream out) throws IOException {
        doCompress(file, out, "");
    }

    public static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    ZipUtils.doCompress(file, out, name);
                }
            }
        } else {
            ZipUtils.doZip(inFile, out, dir);
        }
    }

    public static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }
        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);

        out.write(FileUtils.readFileToByteArray(inFile));
        out.flush();
        out.closeEntry();
    }

    public static void main(String[] args) throws IOException {
        Set<String> set = new HashSet<>();
        set.add("/data/lease/files/qrcode/device_template.xls");
        OutputStream out = new FileOutputStream("/data/lease/files/qrcode/device_template.zip");
        doCompress(set, out);
    }
}
