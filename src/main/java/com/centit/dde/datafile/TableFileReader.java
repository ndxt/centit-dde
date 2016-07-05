package com.centit.dde.datafile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.centit.dde.util.ItemValue;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringRegularOpt;

public class TableFileReader {

    public static final Log LOGGER = LogFactory.getLog(TableFileReader.class);

    /**
     * 临时文件夹路径
     */
    private String filePath;

    private String sorceOsId;

    private String sourceDBName;

    private String exportName;// table name

    private String dataOptId;

    private String exportDesc;

    public String getSorceOsId() {
        return sorceOsId;
    }

    public String getSourceDBName() {
        return sourceDBName;
    }

    public String getExportName() {
        return exportName;
    }

    public String getDataOptId() {
        return dataOptId;
    }

    public String getExportDesc() {
        return exportDesc;
    }

    private Element tableElement;

    public Element getTableElement() {
        return tableElement;
    }

    private static Element loadTalbeElement(String filePath) {
        Element tempElement = null;
        try {
            SAXReader saxReader = new SAXReader();
            Document tableDom = saxReader.read(new File(filePath));
            tempElement = tableDom.getRootElement();

            if (!"table".equals(tempElement.getName()))
                tempElement = null;

            // xmlDoc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            LOGGER.error("读取导入文件" + filePath + " 错误:" + e.getMessage(), e.getCause());
        }

        return tempElement;
    }

    public boolean readTableInfoFromXML(String xmlData) {
        Element tempElement = null;
        try {
            Document tableDom = DocumentHelper.parseText(xmlData);
            tempElement = tableDom.getRootElement();

            if (LOGGER.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("XML 根目录中参数为 ");

                @SuppressWarnings("unchecked")
                List<Attribute> attributes = (List<Attribute>)tempElement.attributes();
                for (Attribute attribute : attributes) {
                    sb.append("name = " + attribute.getName() + " value = " + attribute.getValue());
                }

                LOGGER.debug(sb);
            }

            if (!"table".equals(tempElement.getName())) {
                tempElement = null;
            }
        } catch (DocumentException e) {
            LOGGER.error("解析XML字符串 错误:" + e.getMessage(), e.getCause());
        }
        if (tempElement == null) {
            return false;
        }

        return readTableInfo(tempElement);
    }

    public boolean readTableInfo(InputStream xmlData) {
        Element tempElement = null;
        try {
            SAXReader saxReader = new SAXReader();
            Document tableDom = saxReader.read(xmlData);
            tempElement = tableDom.getRootElement();

            if (!"table".equals(tempElement.getName()))
                tempElement = null;
        } catch (DocumentException e) {
            LOGGER.error("解析XML字符串 错误:" + e.getMessage(), e.getCause());
        }
        if (tempElement == null)
            return false;

        return readTableInfo(tempElement);
    }

    public boolean readTableInfo(String xmlFilePath) {
        Element tempElement = loadTalbeElement(xmlFilePath);
        if (tempElement == null)
            return false;

        return readTableInfo(tempElement);
    }

    public boolean readTableInfo(Element tableElement) {
        this.tableElement = tableElement;

        if (!"table".equals(tableElement.getName()))
            return false;
        /**
         * <table mapinfoId="opt-01" sourcedatabase="epowerdb" * *
         * sorceosid="epower" name="table01">
         */
        sorceOsId = tableElement.attributeValue("sorceosid");
        sourceDBName = tableElement.attributeValue("sourcedatabase");
        exportName = tableElement.attributeValue("name");
        dataOptId = tableElement.attributeValue("dataoptid");
        exportDesc = tableElement.attributeValue("exportdesc");

        return true;
    }

    public String getXML() {
        return tableElement.asXML();
    }

    public int getRowSum() {
        return tableElement.elements().size();
    }

    public Element getRowElement(int index) {
        return (Element) tableElement.elements().get(index);
    }

    @SuppressWarnings("unchecked")
    public Map<String, ItemValue> readRowData(Element row) {
        Map<String, ItemValue> rowData = new HashMap<String, ItemValue>();
        for (Element e : (List<Element>) row.elements()) {
            if (!"item".equals(e.getName()))
                continue;
            String sName = e.attributeValue("name");
            String sType = e.attributeValue("type");
            ItemValue item = new ItemValue(sName);
            if ("number".equals(sType)) {
                item.setColType(ItemValue.number);
                item.setStrValue(e.getTextTrim());

            } else if ("date".equals(sType)) {
                item.setColType(ItemValue.datetime);
                String sFormat = e.attributeValue("format");
                if (StringRegularOpt.isNvl(sFormat))
                    sFormat = "yyyy-MM-dd";
                String sValue = e.getTextTrim();
                item.setDateValue(DatetimeOpt.convertStringToDate(sValue, sFormat));
            } else if ("datetime".equals(sType)) {
                item.setColType(ItemValue.datetime);
                String sFormat = e.attributeValue("format");
                if (StringRegularOpt.isNvl(sFormat))
                    sFormat = "yyyy-MM-dd HH:mm:ss";
                String sValue = e.getTextTrim();
                item.setDateValue(DatetimeOpt.convertStringToDate(sValue, sFormat));
            } else if ("clob".equals(sType)) {
                item.setColType(ItemValue.clob);
                String sStore = e.attributeValue("store");

                String sValue = e.getTextTrim();
                if ("infile".equals(sStore)) {
                    try {
                        // StringBuilder sb = new StringBuilder();
                        // char[] tempchars = new char[1024];
                        // //int charread;
                        // InputStreamReader reader = new InputStreamReader(new
                        // FileInputStream(filePath+"/"+ sValue));
                        // // 读入多个字符到字符数组中，charread为一次读取字符数
                        // while (reader.read(tempchars) > 0 ) {
                        // sb.append(tempchars);
                        // }
                        // reader.close();
                        // item.setStrValue(sb.toString());

                        item.setStrValue(FileUtils.readFileToString(new File(filePath + "/" + sValue)));

                    } catch (IOException err) {
                        LOGGER.error("读取clob文件:" + filePath + "/" + sValue + " 错误：" + err.getMessage());
                        // e.printStackTrace();
                    }
                } else {
                    String sFormat = e.attributeValue("format");
                    if ("base64".equals(sFormat))
                        item.setStrValue(new String(Base64.decode(sValue)));
                    else
                        item.setStrValue(sValue);
                }

            } else if ("blob".equals(sType)) {
                item.setColType(ItemValue.blob);
                String sStore = e.attributeValue("store");
                String sValue = e.getTextTrim();
                if ("infile".equals(sStore)) {
                    try {

                        // FileInputStream in = new
                        // FileInputStream(filePath+"/"+ sValue);
                        // int byteread = in.available();
                        // byte[] tempbytes = new byte[byteread+1];
                        // in.read(tempbytes);
                        // in.close();
                        // item.setBlobValue(tempbytes);

                        item.setBlobValue(FileUtils.readFileToByteArray(new File(filePath + "/" + sValue)));

                    } catch (IOException err) {
                        LOGGER.error("读取clob文件:" + filePath + "/" + sValue + " 错误：" + err.getMessage());
                        // e.printStackTrace();
                    }
                } else {
                    item.setBlobValue(Base64.decode(sValue));
                }

            } else {
                item.setColType(ItemValue.varchar);
                item.setStrValue(e.getTextTrim());
            }
            rowData.put(sName, item);
        }
        return rowData;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "TableFileReader [filePath=" + filePath + ", sorceOsId=" + sorceOsId + ", sourceDBName=" + sourceDBName
                + ", exportName=" + exportName + ", dataOptId=" + dataOptId + ", exportDesc=" + exportDesc
                + ", tableElement=" + tableElement + "]";
    }

}
