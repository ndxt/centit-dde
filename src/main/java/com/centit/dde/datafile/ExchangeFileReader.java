package com.centit.dde.datafile;

import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.ZipCompressor;
import com.centit.support.file.FileSystemOpt;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ExchangeFileReader {
    public static final Logger logger = LoggerFactory.getLogger(ExchangeFileReader.class);

    private String dataDirPath;

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

    public String getDataDirPath() {
        return dataDirPath;
    }

    public void setDataDirPath(String dataDirPath) {
        this.dataDirPath = dataDirPath;
    }

    public void releaseExchangeFile(String zipDataFile) {
        //zip.compress(exchangePath+".zip", exchangePath);
        dataDirPath = filePath + "/" + DatetimeOpt.convertDateToString(
                DatetimeOpt.currentUtilDate(), "YYYY_MM_DD") + "/" + FileSystemOpt.extractFileName(zipDataFile);
        ZipCompressor.release(zipDataFile,
                dataDirPath);
    }

    private Document exchangeDom;
    private List<Element> tableElements;
    private int tableSum;

    /**
     * @return the tableElements
     */
    public List<Element> getTableElements() {
        return tableElements;
    }


    /**
     * @return the tableSum
     */
    public int getTableSum() {
        return tableSum;
    }

    private boolean loadExchangeDOM() {
        boolean readOK = false;
        
        try {
            //InputStream  fis= this.getClass().getResourceAsStream(dataDirPath + "/exchange.xml");
            SAXReader saxReader = new SAXReader();
            saxReader.setValidation(false);
            exchangeDom = saxReader.read(new File(dataDirPath + "/exchange.xml"));
            
            readOK = true;
//            xmlDoc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            logger.error("读取导入文件" + dataDirPath + "/exchange.xml 错误:" + e.getMessage(), e.getCause());
        }

        return readOK;
    }

    @SuppressWarnings("unchecked")
    public boolean readExchangeInfo() {
       
        if (!loadExchangeDOM()) return false;
        Element exchangeE = exchangeDom.getRootElement();

        if (!"exchange".equals(exchangeE.getName()))
            return false;
/*        <exchange id="exp-001" operator="admin" 
 *          taskid="0001" ddeid="dde-01" exporttime="2014-06-17 14:40:11">
*/
        exchangeName = exchangeE.attributeValue("id");
        operator = exchangeE.attributeValue("operator");
        ddeID = exchangeE.attributeValue("ddeid");
        taskID = exchangeE.attributeValue("taskid");
        String sTime = exchangeE.attributeValue("exporttime");
        exportTime = DatetimeOpt.convertStringToDate(sTime, "yyyy-MM-dd HH:mm:ss");

        //exchangeE.selectNodes("data/table");
        Element dataE = exchangeE.element("data");
        if (dataE == null)
            return false;
        tableElements = (List<Element>) dataE.elements();
        if (tableElements == null)
            return false;

        tableSum = tableElements.size();

        return true;
    }


    public TableFileReader getTableFileReader(int index) {
        if (index < 0 || index >= tableSum) return null;
        TableFileReader tableReader = new TableFileReader();
        tableReader.setFilePath(dataDirPath);
        Element tableElement = tableElements.get(index);
        String sStore = tableElement.attributeValue("store");
        if ("infile".equals(sStore)) {
            String tableFilePath = dataDirPath + "/" + tableElement.getTextTrim();
            logger.info(tableFilePath);

            tableReader.readTableInfo(tableFilePath);
        } else {
            tableReader.readTableInfo(tableElement);
        }

        return tableReader;
    }


}
