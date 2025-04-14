package com.centit.dde.adapter.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.OrderBy;
import java.util.Date;


/**
 * @author zhf
 */
@Data
public class CallApiLogDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @OrderBy
    @ApiModelProperty(value = "执行步骤序号")
    private Integer stepNo;

    @ApiModelProperty(value = "操作节点id", required = true)
    private String optNodeId;

    @ApiModelProperty(value = "日志类别", required = true)
    private String logType;

    @ApiModelProperty(value = "执行开始时间")
    private Date runBeginTime;

    @ApiModelProperty(value = "执行结束时间")
    private Date runEndTime;

    @ApiModelProperty(value = "任务明细描述")
    // @Basic(fetch = FetchType.LAZY)
    private String logInfo;

    @ApiModelProperty(value = "成功条数")
    private Integer successPieces;

    @ApiModelProperty(value = "失败条数")
    private Integer errorPieces;

    public CallApiLogDetail(){
        successPieces = 0;
        errorPieces = 0;
    }
}
