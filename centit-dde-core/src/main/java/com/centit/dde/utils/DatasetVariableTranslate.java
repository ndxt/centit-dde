package com.centit.dde.utils;

import com.centit.dde.core.DataSet;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.compiler.VariableTranslate;
import com.centit.support.json.JSONTransformDataSupport;

/**
 * @author zhf
 */
public class DatasetVariableTranslate
    implements VariableTranslate, JSONTransformDataSupport {

    private DataSet dataSet;

    public DatasetVariableTranslate(DataSet dataSet) {
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
        if(dataSet ==null) {
            return "";
        }
        return ReflectionOpt.attainExpressionValue(dataSet.getData(), varName);
    }

    @Override
    public Object attainExpressionValue(String expression) {
        if(".".equals(expression)){
            return this.dataSet.getData();
        }
        VariableFormula variableFormula = new VariableFormula();
        variableFormula.setExtendFuncMap(DataSetOptUtil.extendFuncs);
        variableFormula.setTrans(this);
        variableFormula.setFormula(expression);
        return variableFormula.calcFormula();
    }

    @Override
    public String mapTemplateString(String templateString) {
        return Pretreatment.mapTemplateStringAsFormula(templateString, this, "", true);
    }
}

