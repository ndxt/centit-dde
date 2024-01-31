package com.centit.dde.utils;

import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.compiler.VariableTranslate;
import com.centit.support.json.JSONTransformDataSupport;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhf
 */
public class BizModelJSONTransform
    implements VariableTranslate, JSONTransformDataSupport {
    private BizModel bizModel;
    private int stackLength;
    private List<Object> stack;

    public BizModelJSONTransform(BizModel obj) {
        this.bizModel = obj;
        this.stackLength = 0;
        this.stack = new ArrayList<>(10);
    }

    public BizModelJSONTransform(BizModel obj, Object stackValue) {
        this(obj);
        pushStackValue(stackValue);
    }

    @Override
    public Object attainExpressionValue(String expression) {
        if (expression == null) {
            return null;
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

    private Object peekStackValue() {
        return stackLength > 1 ? stack.get(stackLength - 2) : null;
    }

    @Override
    public void pushStackValue(Object value) {
        if (stack.size() > stackLength) {
            stack.set(stackLength, value);
        } else {
            stack.add(value);
        }
        stackLength++;
    }

    @Override
    public Object popStackValue() {
        Object obj = stackLength > 0 ? stack.get(stackLength - 1) : null;
        if (stackLength > 0) {
            stackLength--;
        }
        return obj;
    }

    @Override
    public Object getVarValue(String labelName) {
        if (labelName.startsWith(ConstantValue.ROOT_NODE_TAG)) {
            return fetchRootData(labelName.substring(1));
        } else if (labelName.startsWith(ConstantValue.DOUBLE_SPOT)) {
            return ReflectionOpt.attainExpressionValue(
                peekStackValue(),
                labelName.substring(2));
        } else if (labelName.startsWith(ConstantValue.SPOT)) {
            if (stackLength > 0) {
                return ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength - 1),
                    labelName.substring(1));
            } else {
                return fetchRootData(labelName.substring(1));
            }
        } else {
            if (stackLength > 0) {
                return ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength - 1),
                    labelName);
            } else {
                return fetchRootData(labelName);
            }
        }
    }

    protected Object fetchRootData(String labelName) {
        if (StringUtils.isBlank(labelName)) {
            return this.bizModel;
        }

        String dataSetName, valuePath;
        int pointIndex = labelName.indexOf('.');
        int pointIndex2 = labelName.indexOf('[');
        if (pointIndex2 > 0 && (pointIndex<0 || pointIndex2 < pointIndex)) { // [
            dataSetName = labelName.substring(0, pointIndex2);
            valuePath = labelName.substring(pointIndex2);
        } else if (pointIndex > 0) { // .
            dataSetName = labelName.substring(0, pointIndex);
            valuePath = labelName.substring(pointIndex + 1);
        } else {
            dataSetName = labelName;
            valuePath = null;
        }

        DataSet currentDateSet = this.bizModel.getDataSet(dataSetName);
        if (currentDateSet != null) {
            if (StringUtils.isBlank(valuePath)) {
                return currentDateSet.getData();
            }
            if (ConstantValue.DATASET_SIZE.equals(valuePath)) {
                return currentDateSet.getSize();
            }
            return ReflectionOpt.attainExpressionValue(
                currentDateSet.getData(),
                valuePath);
        }

        if (ConstantValue.MODEL_NAME.equals(dataSetName)) {
            return this.bizModel.getModelName();
        }
        return null;
    }

}
