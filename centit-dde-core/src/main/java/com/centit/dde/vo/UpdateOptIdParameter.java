package com.centit.dde.vo;

import lombok.Data;

@Data
public class UpdateOptIdParameter {
    private String optId;
    private String[] optCodes;
    private String[] apiIds;
}
