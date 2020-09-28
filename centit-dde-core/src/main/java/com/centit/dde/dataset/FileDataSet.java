package com.centit.dde.dataset;

import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.DataSetWriter;

/**
 * 需要设置一个文件路径
 */
public abstract class FileDataSet implements DataSetReader, DataSetWriter {

    protected String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
