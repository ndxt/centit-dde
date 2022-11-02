package com.centit.dde.qrcode.utils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtil {
    /**
     * 递归生成目录, file 为根据相对路径创建时, 会抛npe
     * @param file
     * @throws FileNotFoundException
     */
    public static void mkDir(File file) throws FileNotFoundException {
        if (file.getParentFile() == null) {
            file = file.getAbsoluteFile();
        }

        if (file.getParentFile().exists()) {
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        } else {
            mkDir(file.getParentFile());
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        }
    }


    /**
     * 创建文件
     *
     * @param filename
     * @return
     */
    public static File createFile(String filename) throws FileNotFoundException {
        if (filename == null || "".equals(filename)) {
            return null;
        }

        int index = filename.lastIndexOf('/');
        if (index <= 0) {
            return new File(filename);
        }

        String path = filename.substring(0, index);
        mkDir(new File(path));
        return new File(filename);
    }
}
