package com.centit.product.dataopt.datarule;

import lombok.Data;

@Data
public class CheckRule {
    String checkType;
    String [] checkParams;
    String errorMsg;
}
