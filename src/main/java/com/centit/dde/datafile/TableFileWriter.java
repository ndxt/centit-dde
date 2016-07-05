package com.centit.dde.datafile;

import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.file.FileSystemOpt;
import com.centit.support.xml.XmlUtils;

public class TableFileWriter {
    public static final Log loger = LogFactory.getLog(TableFileWriter.class);
    private static final int BUFSIZE = 64 * 1024;
    private CharArrayWriter memoryWriter = null;

    private String sorceOsId;
    private String sourceDBName;
    private String exportName;//table name
    private String dataOptId;
    private String exportDesc;

    private String filePath;

    private BufferedWriter tableWriter;
    
/*    private String transaction;
    private String conflict;*/

    public String getSorceOsId() {
        return sorceOsId;
    }

    public void setSorceOsId(String sorceOsId) {
        this.sorceOsId = sorceOsId;
    }

    public String getSourceDBName() {
        return sourceDBName;
    }

    public void setSourceDBName(String sourceDBName) {
        this.sourceDBName = sourceDBName;
    }

    public String getExportName() {
        return exportName;
    }

    public void setExportName(String exportName) {
        this.exportName = exportName;
    }

    public String getDataOptId() {
        return dataOptId;
    }

    public void setDataOptId(String mapinfoId) {
        this.dataOptId = mapinfoId;
    }

    public String getExportDesc() {
        return exportDesc;
    }

    public void setExportDesc(String exportDesc) {
        this.exportDesc = exportDesc;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public BufferedWriter getTableWriter() {
        return tableWriter;
    }

    public void setTableWriter(BufferedWriter tableWriter) {
        this.tableWriter = tableWriter;
    }

    public BufferedWriter prepareWriter() {
        //java.io.CharArrayWriter
        BufferedWriter fw = null;
        try {
            fw = new BufferedWriter(new FileWriter(
                    filePath + "/" + exportName + ".xml", false));

            fw.write("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
        } catch (IOException e) {
            loger.error("创建并打开输出文件:" + filePath + "/" + exportName + ".xml  错误：" + e.getMessage());
            //e.printStackTrace();
        }
        closeWriter();
        tableWriter = fw;
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
        tableWriter = fw;
        return fw;
    }

    public String getMemoryDataXML() {
        return memoryWriter.toString();
    }

    public void closeWriter() {
        try {
            if (tableWriter != null)
                tableWriter.close();
        } catch (IOException e) {
            loger.error("关闭文件:" + filePath + "/" + exportName + ".xml 错误：" + e.getMessage());
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

    public void writeTableBegin() {
        try {
            tableWriter.write("<table name=\"" + exportName +
                    "\" sorceosid=\"" + sorceOsId +
                    "\" sourcedatabase=\"" + sourceDBName +
                    "\" dataoptid=\"" + dataOptId +
                    "\" >\r\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRowBegin() {
        try {
            tableWriter.write("<row>\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeNullField(String name) {
        try {
            tableWriter.write("<item name=\"" + name + "\"/>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:null 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeField(String name, String value) {
        try {
            tableWriter.write("<item name=\"" + name + "\">" +
                    XmlUtils.encodeString(value) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + value + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeCDataField(String name, String value) {
        try {
            tableWriter.write("<item name=\"" + name + "\"><![CDATA[" +
                    value + "]]></item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + value + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeIntField(String name, int iValue) {
        try {
            tableWriter.write("<item name=\"" + name + "\" type=\"number\">" +
                    String.valueOf(iValue) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + String.valueOf(iValue) + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeLongField(String name, long lValue) {
        try {
            tableWriter.write("<item name=\"" + name + "\" type=\"number\">" +
                    String.valueOf(lValue) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + String.valueOf(lValue) + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeDoubleField(String name, double dbValue) {
        try {
            tableWriter.write("<item name=\"" + name + "\" type=\"number\">" +
                    String.valueOf(dbValue) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + String.valueOf(dbValue) + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeDateField(String name, Date dateValue) {
        try {
            tableWriter.write("<item name=\"" + name + "\" type=\"date\" >" +
                    DatetimeOpt.convertDateToString(dateValue) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + dateValue.toString() + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }


    public void writeDateField(String name, Date dateValue, String format) {
        try {
            tableWriter.write("<item name=\"" + name + "\" type=\"date\" format=\"" + format + "\">" +
                    DatetimeOpt.convertDateToString(dateValue, format) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + dateValue.toString() + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }


    public void writeDatetimeField(String name, Date dateValue) {
        try {
            tableWriter.write("<item name=\"" + name + "\" type=\"datetime\" >" +
                    DatetimeOpt.convertDatetimeToString(dateValue) + "</item>\r\n");
        } catch (IOException e) {
            loger.error("写入item:" + name + " value:" + dateValue.toString() + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeClobField(String name, String clobValue, boolean infile, String sKey) {
        try {
            if (infile) {
                tableWriter.write("<item name=\"" + name + "\" type=\"clob\"  store=\"infile\">" +
                        exportName + "/" + name + "/" + sKey + ".dat" + "</item>\r\n");

                FileSystemOpt.createDirect(filePath + "/" + exportName + "/" + name);
                try {
                    //Writer lobw = new FileWriter(filePath+"/"+ exportName+"/"+ name +"/"+ sKey +".dat");
                    Writer lobw = new OutputStreamWriter(
                            new FileOutputStream(
                                    filePath + "/" + exportName + "/" + name + "/" + sKey + ".dat"), "GBK");
                    lobw.write(clobValue);
                    lobw.close();
                } catch (IOException e) {
                    loger.error("写入clob文件:" + filePath + "/" + exportName + "/" + name + "/" + sKey + ".dat 错误：" + e.getMessage());
                    //e.printStackTrace();
                }

            } else
                tableWriter.write("<item name=\"" + name + "\" type=\"clob\"  format=\"plain\"><![CDATA[" +
                        clobValue + "]]></item>\r\n");

        } catch (IOException e) {
            loger.error("写入clobitem:" + name + " length:" + clobValue.length() + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeBlobField(String name, byte[] blobValue, boolean infile, String sKey) {
        try {
            if (infile) {
                tableWriter.write("<item name=\"" + name + "\" type=\"blob\"  store=\"infile\">" +
                        exportName + "/" + name + "/" + sKey + ".dat" + "</item>\r\n");
                FileSystemOpt.createDirect(filePath + "/" + exportName + "/" + name);
                try {
                    FileOutputStream dos = new FileOutputStream(
                            new File(filePath + "/" + exportName + "/" + name + "/" + sKey + ".dat"));
                    dos.write(blobValue);
                    dos.close();

                } catch (IOException e) {
                    loger.error("写入blob文件:" + filePath + "/" + exportName + "/" + name + "/" + sKey + ".dat 错误：" + e.getMessage());
                    //e.printStackTrace();
                }
            } else
                tableWriter.write("<item name=\"" + name + "\" type=\"blob\" format=\"base64\"><![CDATA[ " +
                        new String(Base64.encodeBase64(blobValue)) + "]]></item>\r\n");
        } catch (IOException e) {
            loger.error("写入blobitem:" + name + " length:" + blobValue.length + " 错误：" + e.getMessage());
            //e.printStackTrace();
        }

    }

    public void writerFlush() {
        try {
            tableWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRowEnd() {
        try {
            tableWriter.write("</row>\r\n");
            tableWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeTableEnd() {
        try {
            tableWriter.write("</table>\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
