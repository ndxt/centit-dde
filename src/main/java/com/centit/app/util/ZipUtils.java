package com.centit.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.centit.app.po.Fileinfo;
import com.centit.app.po.Publicinfo;
import com.centit.sys.util.CommonCodeUtil;
import com.centit.sys.util.SysParametersUtils;

/**
 * TODO 压缩包工具类
 *
 * @author zk
 * @create 2013-3-11
 */
public class ZipUtils {


    /**
     * 压缩文件
     *
     * @param toZipFile
     * @param zipFile
     * @return
     * @throws IOException
     */
    public static File zip(File toZipFile, File zipFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zos.setEncoding("gbk");

        zip(toZipFile, zos, "");

        zos.flush();
        zos.close();

        return zipFile;
    }

    /**
     * 批量压缩文件
     *
     * @param toZipFile
     * @param zipFile
     * @return
     * @throws IOException
     */
    public static File zip(Collection<File> toZipFile, File zipFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zos.setEncoding("gbk");

        for (File file : toZipFile) {
            zip(file, zos, "");

            zos.flush();
        }

        zos.close();

        return zipFile;
    }

    private static void zip(File toZipFile, ZipOutputStream zos, String dir) throws IOException {
        String name = toZipFile.getName();

        if (!StringUtils.isBlank(dir)) {
            name = dir + File.separator + name;
        }

        // 文件夹
        if (toZipFile.isDirectory()) {
            File[] files = toZipFile.listFiles();

            // 需要判断空文件夹
            if (files.length == 0) {
                ZipEntry zipEntry = new ZipEntry(name + "/");
                zos.putNextEntry(zipEntry);
                zos.closeEntry();
            } else {
                for (File file : files) {
                    zip(file, zos, name);
                }
            }

        }
        // 文件
        else {

            zos.putNextEntry(new ZipEntry(name));

            InputStream bis = new FileInputStream(toZipFile);

            byte[] buf = new byte[4096];
            int len;
            while ((len = bis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            bis.close();
        }
    }

    public static void zipPublicinfo(List<Publicinfo> infos, File zipFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zos.setEncoding("gbk");

        try {
            for (Publicinfo info : infos) {
                String path = info.getZipPath();
                ZipEntry zipEntry = null;

                // 文件夹
                if (StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_FOLDER, info.getIsfolder())) {
                    zipEntry = new ZipEntry(path + "/");
                    zos.putNextEntry(zipEntry);
                }
                // 文件
                else {
                    zipEntry = new ZipEntry(path);
                    zos.putNextEntry(zipEntry);

                    Fileinfo fileinfo = info.getFileinfo();
                    File toZipFile = new File(SysParametersUtils.getUploadHome() + fileinfo.getPath());
                    InputStream bis = new FileInputStream(toZipFile);

                    byte[] buf = new byte[4096];
                    int len;
                    while ((len = bis.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                    bis.close();
                }

                zos.closeEntry();
            }
        } catch (Exception e) {
            zos.close();
        } finally {
            zos.close();
        }

    }
}
