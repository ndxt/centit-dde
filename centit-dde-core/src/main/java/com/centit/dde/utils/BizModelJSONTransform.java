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

    static class StackData{
        Object data;
        int  index;
        int  count;
        public StackData(Object data, int index, int count) {
            this.data = data;
            this.index = index;
            this.count = count;
        }
    }

    private final BizModel bizModel;
    private int stackLength;
    private final List<StackData> stack;

    public BizModelJSONTransform(BizModel obj) {
        this.bizModel = obj;
        this.stackLength = 0;
        this.stack = new ArrayList<>(10);
    }

    public BizModelJSONTransform(BizModel obj, Object stackValue) {
        this(obj);
        if(stackValue!=null) {
            pushStackValue(stackValue,0,1);
        }
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

    /**
     * 清理栈中的值
     */
    public void clearStackValue() {
        this.stackLength = 0;
    }

    @Override
    public void pushStackValue(Object value, int index, int count) {
        if (stack.size() > stackLength) {
            stack.set(stackLength, new StackData(value, index, count));
        } else {
            stack.add(new StackData(value, index, count));
        }
        stackLength++;
    }

    @Override
    public Object popStackValue() {
        Object obj = stackLength > 0 ? stack.get(stackLength - 1).data : null;
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
            if (stackLength > 1) {
                if ("..__row_index".equals(labelName)) {
                    return stack.get(stackLength - 2).index;
                }
                if ("..__row_count".equals(labelName)) {
                    return stack.get(stackLength - 2).count;
                }
                return ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength - 2).data,
                    labelName.substring(2));
            }
            return null;
        } else if (labelName.startsWith(ConstantValue.SPOT)) {
            if (stackLength > 0) {
                if (".__row_index".equals(labelName)) {
                    return stack.get(stackLength - 1).index;
                }
                if (".__row_count".equals(labelName)) {
                    return stack.get(stackLength - 1).count;
                }
                return ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength - 1).data,
                    labelName.substring(1));
            } else {
                return fetchRootData(labelName.substring(1));
            }
        } else {
            if (stackLength > 0) { // 优先从栈中获取变量
                if ("__row_index".equals(labelName)) {
                    return stack.get(stackLength - 1).index;
                }
                if ("__row_count".equals(labelName)) {
                    return stack.get(stackLength - 1).count;
                }
                Object obj = ReflectionOpt.attainExpressionValue(
                    stack.get(stackLength - 1).data,
                    labelName);
                if(obj!=null){
                    return obj;
                }
            }
            return fetchRootData(labelName);
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
