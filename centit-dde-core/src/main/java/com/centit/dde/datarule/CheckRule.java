package com.centit.dde.datarule;

import lombok.Data;

/**
 * @author zhf
 */
@Data
public class CheckRule {
    String types;
    String columnname;
    String Dformulainfo;
    String Fmin;
    String Fmax;
    String errormsg;
    String min;
    String max;
}
