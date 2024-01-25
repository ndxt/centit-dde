package com.centit.dde.utils;

import com.centit.dde.core.BizModel;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.compiler.VariableTranslate;

/**
 * @author codefan@sina.com
 */
public class DataRowVariableTranslate
    implements VariableTranslate {

    public static final String ROW_INDEX_LABEL = "__row_index";
    public static final String ROW_COUNT_LABEL = "__row_count";

    private final int rowInd;
    private final int rowCount;
    private Object rowData;
    private BizModelJSONTransform bizModelJSONTransform;

    public DataRowVariableTranslate(BizModel bizModel, Object data, int rowInd, int rowCount) {
        this.rowInd = rowInd;
        this.rowCount = rowCount;
        this.rowData = data;
        this.bizModelJSONTransform = new BizModelJSONTransform(bizModel);
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

        if (varName.startsWith(ConstantValue.ROOT_NODE_TAG)) {
            return this.bizModelJSONTransform.fetchRootData(varName.substring(1));
        }

        if (this.rowData == null)
            return null;
        return ReflectionOpt.attainExpressionValue(this.rowData, varName);
    }
}

