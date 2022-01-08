package com.centit.dde.vo;

import lombok.Data;

@Data
public class UpdateOptIdParamVo {
    private String optId;
    private String[] optCodes;
    private String[] apiIds;
}
