package com.centit.dde.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Data
@Entity
@Table(name="D_TASK_DETAIL_LOG")
public class TaskDetailLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="LOG_DETAIL_ID")
    @ApiModelProperty(value = "日志明细编号", hidden = true)
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String logDetailId;

    @Column(name = "TASK_ID")
    @ApiModelProperty(value = "任务ID", required = true)
    @NotBlank
    private String taskId;

    @Column(name="LOG_ID")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @Column(name="LOG_TYPE")
    @ApiModelProperty(value = "日志类别", required = true)
    @NotBlank
    private String logType;
    
    @Column(name="RUN_BEGIN_TIME")
    @ApiModelProperty(value = "执行开始时间")
    private Date runBeginTime;
    
    @Column(name="RUN_END_TIME")
    @ApiModelProperty(value = "执行结束时间")
    private Date runEndTime;
    
    @Column(name="LOG_INFO")
    @ApiModelProperty(value = "任务明细描述")
    private String logInfo;
    
    @Column(name="SUCCESS_PIECES")
    @ApiModelProperty(value = "成功条数")
    private Long successPieces;
    
    @Column(name="ERROR_PIECES")
    @ApiModelProperty(value = "失败条数")
    private Long errorPieces;


}
