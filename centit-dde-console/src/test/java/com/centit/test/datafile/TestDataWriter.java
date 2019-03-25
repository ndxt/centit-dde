package com.centit.test.datafile;

/*import com.centit.dde.datafile.ExchangeFileReader;
import com.centit.dde.datafile.ExchangeFileWriter;
import com.centit.dde.datafile.TableFileReader;
import com.centit.dde.datafile.TableFileWriter;*/
import com.centit.dde.po.TaskLog;
import com.centit.framework.common.SysParametersUtils;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.ZipCompressor;
import com.centit.support.file.FileSystemOpt;

import java.io.File;
import java.io.Writer;
import java.util.Map;

public class TestDataWriter {

    private static String getAppPath(){
        return SysParametersUtils.getAppHome();
    }

    /*private static void testExchange(){
        String appPath = getAppPath();
        ExchangeFileWriter ef = new ExchangeFileWriter();
        ef.setFilePath(appPath+"/temp");
        FileSystemOpt.createDirect(appPath+"/temp");
        ef.setDdeID("dde-01");
        ef.setExchangeName("export-01");
        ef.setTaskID("0001");
        ef.setOperator("admin");
        ef.setExportTime(DatetimeOpt.currentUtilDate());

        ef.prepareWriter();
        ef.writeExchangeBegin();
        ef.writeDataBegin();

        TableFileWriter tf = new TableFileWriter();

        tf.setFilePath(ef.getExchangeFilePath());
        tf.setExportName("table01");
        tf.setDataOptId("opt-01");
        tf.setSourceOsId("epower");
        tf.setSourceDBName("epowerdb");
        tf.setTableWriter(ef.getExchangeWriter());
        tf.writeTableBegin();
        for(int i=0;i<5;i++){
            tf.writeRowBegin();

            tf.writeIntField( "key", i);
            tf.writeField("name", "value"+i);
            tf.writeDateField("time",DatetimeOpt.currentUtilDate());

            tf.writeRowEnd();
        }
        tf.writeTableEnd();

        ef.writeDataEnd();
        ef.writeExchangeEnd();
        ef.closeWriter();
    }*/

   /* private static void testExchange2(){
        String appPath = getAppPath();
        ExchangeFileWriter ef = new ExchangeFileWriter();
        ef.setFilePath(appPath+"/temp");
        FileSystemOpt.createDirect(appPath+"/temp");
        ef.setDdeID("dde-01");
        ef.setExchangeName("export-01");
        ef.setTaskID("0001");
        ef.setOperator("admin");
        ef.setExportTime(DatetimeOpt.currentUtilDate());

        Writer fw = ef.prepareWriter();
        ef.writeExchangeBegin();
        ef.writeDataBegin();

        ef.writeTableInfo( "table01");

        TableFileWriter tf = new TableFileWriter();


        tf.setFilePath(ef.getExchangeFilePath());
        tf.setExportName("table01");
        tf.setDataOptId("opt-01");
        tf.setSourceOsId("epower");
        tf.setSourceDBName("epowerdb");

         tf.prepareWriter();

        tf.writeTableBegin();
        for(int i=0;i<5;i++){
            tf.writeRowBegin();

            tf.writeIntField( "key", i);
            tf.writeField( "name", "value"+i);
            tf.writeDateField("time",DatetimeOpt.currentUtilDate());

            tf.writeRowEnd();
        }
        tf.writeTableEnd();
        tf.closeWriter();

        ef.writeDataEnd();
        ef.writeExchangeEnd();
        ef.closeWriter();

        ef.compressExchangeFile();
    }*/


   /* private static void testExchange3(){
        String appPath = getAppPath();
        ExchangeFileWriter ef = new ExchangeFileWriter();
        ef.setFilePath(appPath+"/temp");
        //FileSystemOpt.createDirect(appPath+"/temp");
        ef.setDdeID("dde-01");
        ef.setExchangeName("export-01");
        ef.setTaskID("0001");
        ef.setOperator("admin");
        ef.setExportTime(DatetimeOpt.currentUtilDate());

        ef.prepareMemoryWriter();
        ef.writeExchangeBegin();
        ef.writeDataBegin();

        TableFileWriter tf = new TableFileWriter();

        tf.setFilePath(ef.getExchangeFilePath());
        tf.setExportName("table01");
        tf.setDataOptId("opt-01");
        tf.setSourceOsId("epower");
        tf.setSourceDBName("epowerdb");

        tf.setTableWriter(ef.getExchangeWriter());
        tf.writeTableBegin();
        for(int i=0;i<5;i++){
            tf.writeRowBegin();

            tf.writeIntField( "key", i);
            tf.writeField( "name", "value"+i);
            tf.writeDateField("time",DatetimeOpt.currentUtilDate());

            tf.writeRowEnd();
        }
        tf.writeTableEnd();

        ef.writeDataEnd();
        ef.writeExchangeEnd();

        System.out.println(ef.getMemoryDataXML());

        ef.closeWriter();

        System.out.println(ef.getMemoryDataXML());

    }*/



    private static void testZip(){
        //ZipCompressor zip = new ZipCompressor();
        String appPath = getAppPath();
        //zip.compress(appPath+"/temp/export-01.zip" , appPath+"/temp/export-01");

        ZipCompressor.release(appPath+"/temp/export-01/export-010001.zip",
                appPath+"/temp/export-02");

        ZipCompressor.release(appPath+"/temp/export-01/export-010002.zip",
                appPath+"/temp/export-03");


    }

    private static void testFile(){
        //ZipCompressor zip = new ZipCompressor();
        String appPath = getAppPath();
        //zip.compress(appPath+"/temp/export-01.zip" , appPath+"/temp/export-01");
        File f = new File(appPath+"/temp/export-01/export-010002.zip");

        System.out.println(f.getPath());
        System.out.println(f.getName());
        //System.out.println(f.

    }

   /* private static void testReadExchange(){
        //ZipCompressor zip = new ZipCompressor();
        ExchangeFileReader reader = new ExchangeFileReader();
        String appPath = getAppPath();
        reader.setDataDirPath(appPath+"/temp/export-01/export-010001");
        reader.readExchangeInfo();
        for(int i=0;i<reader.getTableSum();i++){
            TableFileReader tr = reader.getTableFileReader(i);
            System.out.println(tr.getExportName());
            for(int j=0;j<tr.getRowSum();j++){
                Map<String,ItemValue> row = tr.readRowData(tr.getRowElement(j));
                for(Map.Entry<String,ItemValue> item:row.entrySet())
                    System.out.println(item.getKey()+" : "+item.getValue().toString());
            }
        }

    }*/

    /*private static void testReadExchange2(){
        //ZipCompressor zip = new ZipCompressor();
        ExchangeFileReader reader = new ExchangeFileReader();
        String appPath = getAppPath();
        reader.setDataDirPath(appPath+"/temp/export-01/export-010001");
        reader.readExchangeInfo();
        for(int i=0;i<reader.getTableSum();i++){
            TableFileReader tr = reader.getTableFileReader(i);
            System.out.println(tr.getXML());
        }
    }*/


    public static void main(String arg[]){
        //testReadExchange2();
        //testExchange2();
        //testZip();
        //testFile();
        //testExchange3();
        //System.out.println(getAppPath());
    }
}
