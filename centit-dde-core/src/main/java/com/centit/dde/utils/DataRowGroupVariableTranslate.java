package com.centit.dde.utils;

import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.compiler.VariableTranslate;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
public class DataRowGroupVariableTranslate
    implements VariableTranslate {

    public static final String ROW_INDEX_LABEL = DataRowVariableTranslate.ROW_INDEX_LABEL;
    public static final String GROUP_INDEX_LABEL = "__group_index";
    public static final String ROW_COUNT_LABEL = DataRowVariableTranslate.ROW_COUNT_LABEL;
    public static final String GROUP_COUNT_LABEL = "__group_count";
    private List<Map<String, Object>> dataSet;
    private int offset;
    private int length;
    private int currentPos;

    public DataRowGroupVariableTranslate(List<Map<String, Object>> dataSet) {
        this.dataSet = dataSet;
    }

     /**
      * 默认返回业务模型对象的属性值 , request 队形的参数
      *变量名 -》变量值的转变
      *变量 是用 ${变量名}
      *如果这个变量不存在，返回空字符串 "''"
      * @param varName 变量
      * @return 值
      */
    @Override
    public Object getVarValue(String varName) {

        if(ROW_INDEX_LABEL.equals(varName)){
            return this.currentPos;
        }

        if(GROUP_INDEX_LABEL.equals(varName)){
            return this.currentPos - this.offset;
        }

        if(ROW_COUNT_LABEL.equals(varName)){
            return this.dataSet.size();
        }

        if(GROUP_COUNT_LABEL.equals(varName)){
            return this.length;
        }

        if(dataSet ==null) {
            return null;
        }

        int n = varName.lastIndexOf('.');
        if(n>0) {
            String indexStr = varName.substring(n + 1);
            if (indexStr.charAt(0) == '_' && StringRegularOpt.isDigit(indexStr.substring(1))) {
                String fieldName = varName.substring(0, n);
                int pos = NumberBaseOpt.castObjectToInteger(indexStr.substring(1), 0);
                if (currentPos + pos < offset + length) {
                    return ReflectionOpt.attainExpressionValue(dataSet.get(currentPos + pos), fieldName);
                } else {
                    return null;
                }
            } else if (StringRegularOpt.isDigit(indexStr)) {
                String fieldName = varName.substring(0, n);
                int pos = NumberBaseOpt.castObjectToInteger(indexStr, 0);
                if (currentPos - pos >= offset) {
                    return ReflectionOpt.attainExpressionValue(dataSet.get(currentPos - pos), fieldName);
                } else {
                    return null;
                }
            }
        }
        return ReflectionOpt.attainExpressionValue(dataSet.get(currentPos), varName);
    }

    public void setDataSet(List<Map<String, Object>> varObj) {
        this.dataSet = varObj;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }
}

