package com.centit.dde.utils;

import com.centit.dde.core.BizModel;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.compiler.VariableTranslate;

import java.util.ArrayList;

/**
 * @author codefan@sina.com
 */
public class DataRowVariableTranslate
    extends BizModelJSONTransform {

    public static final String ROW_INDEX_LABEL = "__row_index";
    public static final String ROW_COUNT_LABEL = "__row_count";

    private final int rowInd;
    private final int rowCount;


    public DataRowVariableTranslate(BizModel bizModel, Object data, int rowInd, int rowCount) {
        super(bizModel);
        this.rowInd = rowInd;
        this.rowCount = rowCount;
        pushStackValue(data);
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
            return this.rowInd;
        }
        if(ROW_COUNT_LABEL.equals(varName)){
            return this.rowCount;
        }
        return super.getVarValue(varName);
    }
}

