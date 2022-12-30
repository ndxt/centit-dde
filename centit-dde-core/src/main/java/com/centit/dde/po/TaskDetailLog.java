package com.centit.dde.po;

import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @author zhf
 */
@Data
@Entity
@Table(name="d_task_detail_log")
public class TaskDetailLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="log_detail_id")
    @ApiModelProperty(value = "日志明细编号", hidden = true)
    @ValueGenerator(strategy = GeneratorType.UUID)
    @NotBlank
    private String logDetailId;

    @Column(name = "task_id")
    @ApiModelProperty(value = "任务ID", required = true)
    private String taskId;

    @Column(name="log_id")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @Column(name="log_type")
    @ApiModelProperty(value = "日志类别", required = true)
    private String logType;

    @Column(name="run_begin_time")
    @ApiModelProperty(value = "执行开始时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.IFNULL, value = "today()")
    private Date runBeginTime;

    @Column(name="run_end_time")
    @ApiModelProperty(value = "执行结束时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.IFNULL, value = "today()")
    private Date runEndTime;

    @OrderBy
    @Column(name="step_no")
    @ApiModelProperty(value = "执行步骤序号")
    private Integer stepNo;

    @Column(name="log_info")
    @ApiModelProperty(value = "任务明细描述")
    private String logInfo;

    @Column(name="success_pieces")
    @ApiModelProperty(value = "成功条数")
    private Integer successPieces;

    @Column(name="error_pieces")
    @ApiModelProperty(value = "失败条数")
    private Integer errorPieces;

    public TaskDetailLog (){
        successPieces = 0;
        errorPieces = 0;
    }
}
