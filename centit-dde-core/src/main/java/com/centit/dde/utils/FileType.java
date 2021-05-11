package com.centit.dde.utils;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;

public class FileType {

    public static String checkFileExcelType(InputStream inputStream) throws IOException {
        byte[] b = new byte[28];
        inputStream.read(b, 0, 28);
        String fileHead =  String.valueOf(Hex.encodeHex(b,false));
        if(fileHead.startsWith("D0CF11E0"))
        {
            return "xls";
        }
        if(fileHead.startsWith("504B0304"))
        {
            return "xlsx";
        }
        if(fileHead.startsWith("6D6F7669"))
        {
            return "csv";
        }
        if(fileHead.startsWith("7B226461"))
        {
            return "json";
        }
        return "";
    }

}
