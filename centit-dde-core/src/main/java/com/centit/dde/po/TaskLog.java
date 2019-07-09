package com.centit.dde.po;

import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @ClassName TaskExchange
 * @Date 2019/3/20 9:21
 * @Version 1.0
 */
@Data
@Entity
@ApiModel
@Table(name="D_TASK_LOG")
public class TaskLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="LOG_ID")
    @ValueGenerator(strategy = GeneratorType.UUID)
    @ApiModelProperty(value = "日志ID", hidden = true)
    @NotBlank
    private String logId;

    @Column(name="Task_ID")
    @ApiModelProperty(value = "任务ID", hidden = true)
    private String taskId;

    @Column(name="RUN_BEGIN_TIME")
    @ApiModelProperty(value = "执行开始时间")
    //@ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date runBeginTime;

    @Column(name="RUN_END_TIME")
    @ApiModelProperty(value = "执行结束时间")
    //@ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date runEndTime;

    @Column(name="RUN_TYPE")
    @ApiModelProperty(value = "执行方式", required = true)
    private String runType;

    @Column(name="RUNNER")
    @ApiModelProperty(value = "执行人员", hidden = true)
    private String runner;

    @Column(name="OTHER_MESSAGE")
    @ApiModelProperty(value = "其他提示信息", required = true)
    private String otherMessage;

    @Column(name="ERROR_PIECES")
    @ApiModelProperty(value = "失败条数")
    private String errorPieces;

    @Column(name="SUCCESS_PIECES")
    @ApiModelProperty(value = "成功条数")
    private String successPieces;

   /* @Transient
    private Set<TaskDetailLog> taskDetailLogs;

    public Set<TaskDetailLog> getTaskDetailLogs() {
        if (this.taskDetailLogs == null)
            this.taskDetailLogs = new HashSet<TaskDetailLog>();
        return this.taskDetailLogs;
    }

    public void setTaskDetailLogs(Set<TaskDetailLog> taskDetailLogs) {
        this.taskDetailLogs = taskDetailLogs;
    }*/

}
