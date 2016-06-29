package com.centit.sys.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;

public class SysRollingFileAppender extends DailyRollingFileAppender {

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        String directory = SysParametersUtils.getLogHome();
        File f = new File(directory);
        if (!f.isDirectory() || !f.canRead()) {
            f.mkdirs();
        }

        super.setFile(fileName.startsWith(directory) ? fileName : directory + fileName, append, bufferedIO, bufferSize);
    }

}
