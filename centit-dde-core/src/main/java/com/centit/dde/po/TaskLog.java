package com.centit.dde.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
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
@ApiModel
@Table(name="d_task_log")
public class TaskLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="log_id")
    @ValueGenerator(strategy = GeneratorType.UUID)
    @ApiModelProperty(value = "日志ID", hidden = true)
    @NotBlank
    private String logId;

    @Column(name="task_id")
    @ApiModelProperty(value = "API网关ID", hidden = true)
    private String taskId;

    @Column(name="opt_id")
    @ApiModelProperty(value = "菜单ID", hidden = true)
    private String optId;

    @Column(name="application_id")
    @ApiModelProperty(value = "项目id", hidden = true)
    private String applicationId;

    @Column(name="run_begin_time")
    @ApiModelProperty(value = "执行开始时间")
    private Date runBeginTime;

    @Column(name="run_end_time")
    @ApiModelProperty(value = "执行结束时间")
    private Date runEndTime;

    @Column(name="run_type")
    @ApiModelProperty(value = "执行方式", required = true)
    private String runType;

    @Column(name="runner")
    @ApiModelProperty(value = "执行人员", hidden = true)
    private String runner;

    @Column(name="other_message")
    @ApiModelProperty(value = "其他提示信息", required = true)
    private String otherMessage;

    @Column(name="error_pieces")
    @ApiModelProperty(value = "失败条数")
    private String errorPieces;

    @Column(name="success_pieces")
    @ApiModelProperty(value = "成功条数")
    private String successPieces;

    @Column(name="api_type")
    @ApiModelProperty(value = "API类别，是草稿还是正式运行的日志，0草稿，1正式")
    private Integer apiType;

    /**
     * 临时记录 执行步骤
     */
    private int stepNo;

    public TaskLog(){
        this.stepNo = 0;
    }

    public int getStepNo(){
        return ++ stepNo;
    }
}
