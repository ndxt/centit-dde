package com.centit.product.dataopt.utils;

import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.support.algorithm.ReflectionOpt;

import java.util.ArrayList;
import java.util.List;

import com.centit.support.compiler.VariableFormula;
import com.centit.support.compiler.VariableTranslate;
import com.centit.support.json.JSONTransformDataSupport;
import org.apache.commons.lang3.StringUtils;

public class BizModelJSONTransform
    implements VariableTranslate, JSONTransformDataSupport{
    private BizModel data;
    private int stackLength;
    private List<Object> stack;

    public BizModelJSONTransform(BizModel obj){
        this.data = obj;
        this.stackLength = 0;
        this.stack = new ArrayList<>(10);
    }

    @Override
    public Object attainExpressionValue(String expression){
        return VariableFormula.calculate(expression, this);
    }

    private Object peekStackValue(){
        return stackLength>0? stack.get(stackLength-1): null;
    }
    // n>0
    private Object peekStackValue(int n){
        return stackLength>n? stack.get(stackLength-n-1): null;
    }

    @Override
    public void pushStackValue(Object value){
        if(stack.size()>stackLength){
            stack.set(stackLength, value);
        } else {
            stack.add(value);
        }
        stackLength++;
    }

    @Override
    public Object popStackValue(){
        Object obj = stackLength>0? stack.get(stackLength-1): null;
        if(stackLength>0){
            stackLength-- ;
        }
        return obj;
    }

    @Override
    public Object getVarValue(String labelName) {
        if(labelName.startsWith("/")){
            return fetchRootData(labelName.substring(1));
        } else if(labelName.startsWith("..")){
            return ReflectionOpt.attainExpressionValue(
                peekStackValue(1),
                labelName.substring(2));
        } else if(labelName.startsWith(".")){//
            if(stackLength>0) {
                return ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength-1),
                    labelName.substring(1));
            } else {
                return fetchRootData(labelName.substring(1));
            }
        } else {//
            if(stackLength>0) {
                return ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength-1),
                    labelName);
            } else {
                return fetchRootData(labelName);
            }
        }
    }

    private Object fetchRootData(String labelName){
        if(StringUtils.isBlank(labelName)){
            return null;
        }

        String dataSetName , valuePath;
        int pointIndex = labelName.indexOf('.');
        int pointIndex2 = labelName.indexOf('[');
        if(pointIndex2>0 && pointIndex2<pointIndex){
            pointIndex = pointIndex2;
        }
        if(pointIndex>0){
            dataSetName = labelName.substring(0, pointIndex);
            valuePath = labelName.substring(pointIndex+1);
        } else {
            dataSetName = labelName;
            valuePath = null;
        }

        DataSet currentDateSet = this.data.fetchDataSetByName(dataSetName);
        if(currentDateSet!=null){
            if(StringUtils.isBlank(valuePath)){
                return currentDateSet.getData();
            }
            return ReflectionOpt.attainExpressionValue(
                currentDateSet.getData(),
                valuePath);
        }

        if("modelTag".equals(dataSetName)){
            if(StringUtils.isBlank(valuePath)) {
                return this.data.getModelTag();
            } else {
                this.data.getModelTag()
                    .get(valuePath);
            }
        }
        if("modelName".equals(dataSetName)){
            return this.data.getModelName();
        }
        return null;
    }

}
