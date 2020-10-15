package com.centit.dde.datarule;

import lombok.Data;

/**
 * @author zhf
 */
@Data
public class CheckRule {
    String type;
    String field;
    String expression;
    String min;
    String max;
    String info;
}
