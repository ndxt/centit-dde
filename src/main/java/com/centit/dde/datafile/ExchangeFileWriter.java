package com.centit.dde.datafile;

import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.support.utils.DatetimeOpt;
import com.centit.support.utils.FileSystemOpt;
import com.centit.support.utils.ZipCompressor;

public class ExchangeFileWriter {
    public static final Log loger = LogFactory.getLog(ExchangeFileWriter.class);
    private static final int BUFSIZE = 64 * 1024;

    private CharArrayWriter memoryWriter = null;
    //metadata
    /**
     * 文件夹路径
     */
    private String filePath;
    /**
     * 导出任务名称
     */
    private String exchangeName;
    /**
     * 操作人员
     */
    private String operator;
    /**
     * 导出时间
     */
    private Date exportTime;
    /**
     * 数据交换平台ID
     */
    private String ddeID;
    /**
     * 任务序列号
     */
    private String taskID;


    private BufferedWriter exchangeWriter;


    public BufferedWriter getExchangeWriter() {
        return exchangeWriter;
    }

    public void setExchangeWriter(BufferedWriter exchangeWriter) {
        this.exchangeWriter = exchangeWriter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public String getDdeID() {
        return ddeID;
    }

    public void setDdeID(String ddeID) {
        this.ddeID = ddeID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    /*//data
    private List<TableFileWriter> tables;

    public List<TableFileWriter> getTables() {
        if(tables==null)
            tables = new ArrayList<TableFileWriter>();
        return tables;
    }

    public void setTables(List<TableFileWriter> tables) {
        this.tables = tables;
    }
    
    public void addTable(TableFileWriter table) {
        getTables().add(table);
    }
    */
    public String getExchangeFilePath() {
        return filePath + "/" + exchangeName + "/" + exchangeName + taskID;
    }

    public BufferedWriter prepareWriter() {
        BufferedWriter fw = null;
        FileSystemOpt.createDirect(filePath + "/" + exchangeName + "/" + exchangeName + taskID);
        try {
            fw = new BufferedWriter(new FileWriter(
                    filePath + "/" + exchangeName + "/" + exchangeName + taskID + "/exchange.xml", false));
            fw.write("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
        } catch (IOException e) {
            loger.error("创建并打开输出文件:" + filePath + "/" + exchangeName + "/" + exchangeName + taskID
                    + "/" + exchangeName + ".xml  错误：" + e.getMessage());
            //e.printStackTrace();
        }
        closeWriter();
        exchangeWriter = fw;
        return fw;
    }

    public BufferedWriter prepareMemoryWriter() {
        memoryWriter = new CharArrayWriter(BUFSIZE);
        BufferedWriter fw = new BufferedWriter(memoryWriter);
        try {
            fw.write("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
        } catch (IOException e) {
            loger.error("创建CharArrayWriter:" + String.valueOf(BUFSIZE) + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
        closeWriter();
        exchangeWriter = fw;
        return fw;
    }

    public String getMemoryDataXML() {
        memoryWriter.flush();
        return memoryWriter.toString();
    }

    public void closeWriter() {
        try {
            if (exchangeWriter != null)
                exchangeWriter.close();
        } catch (IOException e) {
            loger.error("关闭文件:" + filePath + "/" + exchangeName + "/" + exchangeName + taskID
                    + "/" + exchangeName + ".xml  错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void closeWriter(Writer fw) {
        try {
            if (fw != null)
                fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeExchangeBegin() {

        try {
            exchangeWriter.write("<exchange id=\"" + exchangeName + "\" taskid=\"" + taskID
                    + "\" ddeid=\"" + ddeID
                    + "\" operator=\"" + operator
                    + "\" exporttime=\"" + DatetimeOpt.convertDatetimeToString(exportTime) + "\">\r\n");
        } catch (IOException e) {
            loger.error("写入exchange信息:" + exchangeName + "-" + taskID
                    + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }

    }

    public void writeDataBegin() {
        try {
            exchangeWriter.write("<data>\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTableInfo(String tableName) {
        try {
            exchangeWriter.write("<table name=\"" + tableName +
                    "\" store=\"infile\" >" +
                    tableName + ".xml</table>\r\n");
        } catch (IOException e) {
            loger.error("写入数据文件信息:" + tableName + "  错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeDataEnd() {
        try {
            exchangeWriter.write("</data>\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writerFlush() {
        try {
            exchangeWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeExchangeEnd() {
        try {
            exchangeWriter.write("</exchange>\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressExchangeFile() {
        String exchangePath = this.getExchangeFilePath();
        //zip.compress(exchangePath+".zip", exchangePath);
        ZipCompressor.compressFileInDirectory(exchangePath + ".zip", exchangePath);
        //删除导出的临时文件，只要保留压缩后zip文件
        FileSystemOpt.deleteDirect(new File(exchangePath));
        
    }

}
