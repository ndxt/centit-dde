package com.centit.dde.datarule;

import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhf
 */
public abstract class CheckRuleUtils {
    public static String CHECK_RULE_RESULT_TAG = "__check_rule_result";


    private static Object getObjectProperty(Map<String, Object> object, CheckRule rule) {
        return object.get(rule.getColumnname());
    }


    private static boolean checkDataLength(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        int dataLen = StringBaseOpt.castObjectToString(data).length();
        return dataLen >= NumberBaseOpt.castObjectToInteger(rule.getFmin(), 0)
            && dataLen <= NumberBaseOpt.castObjectToInteger(rule.getFmax(), 0);
    }

    private static boolean checkMinLength(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        int datalen = StringBaseOpt.castObjectToString(data).length();
        return datalen >= NumberBaseOpt.castObjectToInteger(rule.getMin(), 0);
    }

    private static boolean checkMaxLength(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        int datalen = StringBaseOpt.castObjectToString(data).length();
        return datalen <= NumberBaseOpt.castObjectToInteger(rule.getMax(), 0);
    }

    private static boolean checkDataValue(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        return GeneralAlgorithm.compareTwoObject(
            data, rule.getMin()) >= 0
            && GeneralAlgorithm.compareTwoObject(
            data, rule.getMax()) <= 0;
    }

    private static boolean checkMinValue(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        return GeneralAlgorithm.compareTwoObject(
            data, rule.getMin()) >= 0;
    }

    private static boolean checkMaxValue(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        return GeneralAlgorithm.compareTwoObject(
            data, rule.getMax()) <= 0;
    }

    /**
     * 十七位数字本体码权重
     */
    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * mod11,对应校验码字符值
     */
    private static final char[] VALIDATE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    private static boolean checkIdCardNo(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        String idCardNo = idCard.trim();
        if (idCardNo.length() == 15) {
            return true;
        }
        if (idCardNo.length() != 18) {
            return false;
        }
        // wi*Ai和
        int sum = 0;
        // 进行模11运算
        int mod;

        for (int i = 0; i < 17; i++) {
            char curChar = idCardNo.charAt(i);
            if (curChar < '0' || curChar > '9') {
                return false;
            }
            sum += (idCardNo.charAt(i) - '0') * WEIGHT[i];
        }
        // 进行模11运算
        mod = sum % 11;
        return VALIDATE[mod] == idCardNo.charAt(17);
    }

    private static boolean checkIdCardNo(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data != null && !checkIdCardNo(StringBaseOpt.castObjectToString(data))) {
            return false;
        }
        return true;
    }

    private static boolean checkRegex(Map<String, Object> object, CheckRule rule) {
        Object data = getObjectProperty(object, rule);
        if (data == null) {
            return true;
        }
        return Pattern.matches(rule.getDformulainfo(),
            StringBaseOpt.castObjectToString(data));
    }

    private static boolean checkFormula(Map<String, Object> object, CheckRule rule) {
        return BooleanBaseOpt.castObjectToBoolean(
            VariableFormula.calculate(rule.getDformulainfo(), object), false);
    }

    public static boolean checkData(Map<String, Object> object, CheckRule rule) {
        if (StringUtils.isBlank(rule.getTypes())) {
            return true;
        }
        switch (rule.getTypes().toLowerCase()) {
            case "length":
                return checkDataLength(object, rule);
            case "minlength":
                return checkMinLength(object, rule);
            case "maxlength":
                return checkMaxLength(object, rule);
            case "value":
                return checkDataValue(object, rule);
            case "minvalue":
                return checkMinValue(object, rule);
            case "maxvalue":
                return checkMaxValue(object, rule);
            case "idcard":
                return checkIdCardNo(object, rule);
            case "regex":
                return checkRegex(object, rule);
            case "formula":
                return checkFormula(object, rule);
            default:
                return true;
        }
        //return true;
    }
}
