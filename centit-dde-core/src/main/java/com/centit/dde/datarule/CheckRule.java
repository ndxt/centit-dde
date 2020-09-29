package com.centit.dde.datarule;

import lombok.Data;

/**
 * @author zhf
 */
@Data
public class CheckRule {
    String checkType;
    String [] checkParams;
    String errorMsg;
}
