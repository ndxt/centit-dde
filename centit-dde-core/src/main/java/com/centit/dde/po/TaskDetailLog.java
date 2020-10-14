package com.centit.dde.po;

import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @author zhf
 */
@Data
@Entity
@Table(name="D_TASK_DETAIL_LOG")
public class TaskDetailLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="LOG_DETAIL_ID")
    @ApiModelProperty(value = "日志明细编号", hidden = true)
    @ValueGenerator(strategy = GeneratorType.UUID)
    @NotBlank
    private String logDetailId;

    @Column(name = "TASK_ID")
    @ApiModelProperty(value = "任务ID", required = true)
    private String taskId;

    @Column(name="LOG_ID")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @Column(name="LOG_TYPE")
    @ApiModelProperty(value = "日志类别", required = true)
    private String logType;

    @Column(name="RUN_BEGIN_TIME")
    @ApiModelProperty(value = "执行开始时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.IFNULL, value = "today()")
    private Date runBeginTime;

    @Column(name="RUN_END_TIME")
    @ApiModelProperty(value = "执行结束时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.IFNULL, value = "today()")
    private Date runEndTime;

    @Column(name="LOG_INFO")
    @ApiModelProperty(value = "任务明细描述")
    private String logInfo;

    @Column(name="SUCCESS_PIECES")
    @ApiModelProperty(value = "成功条数")
    private Integer successPieces;

    @Column(name="ERROR_PIECES")
    @ApiModelProperty(value = "失败条数")
    private Integer errorPieces;


}
