package com.centit.dde.adapter.po;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.centit.search.annotation.ESField;
import com.centit.search.annotation.ESType;
import com.centit.search.document.ESDocument;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhf
 */
@Data
@ApiModel
@ESType(indexName = "callapilog", shards = 5)
public class CallApiLog implements ESDocument, Serializable {

    private static final long serialVersionUID =  1L;

    @ESField(type = "keyword") //(name="log_id")
    @ValueGenerator(strategy = GeneratorType.UUID)
    @ApiModelProperty(value = "日志ID", hidden = true)
    @NotBlank
    private String logId;

    @ESField(type = "keyword") //(name="task_id")
    @ApiModelProperty(value = "API网关ID", hidden = true)
    private String taskId;

    @ESField(type = "keyword") //(name="opt_id")
    @ApiModelProperty(value = "菜单ID", hidden = true)
    private String optId;

    @ESField(type = "keyword") //(name="application_id")
    @ApiModelProperty(value = "项目id", hidden = true)
    private String applicationId;

    @ESField(type = "keyword") //(name="opt_id")
    @ApiModelProperty(value = "菜单ID", hidden = true)
    private String topUnit;

    @ApiModelProperty(value = "执行开始时间")
    @ESField(type = "Date") //
    private Date runBeginTime;

    @ApiModelProperty(value = "执行结束时间")
    @ESField(type = "Date") //
    private Date runEndTime;

    @ESField(type = "keyword") //(name="run_type")
    @ApiModelProperty(value = "执行方式", required = true)
    private String runType;

    @ESField(type = "keyword") //(name="runner")
    @ApiModelProperty(value = "执行人员", hidden = true)
    private String runner;

    @ESField(type = "keyword") //(name="other_message")
    @ApiModelProperty(value = "其他提示信息", required = true)
    private String otherMessage;

    @ESField(type = "keyword") //(name="error_pieces")
    @ApiModelProperty(value = "失败条数")
    private Integer errorPieces;

    @ESField(type = "keyword") //(name="success_pieces")
    @ApiModelProperty(value = "成功条数")
    private Integer successPieces;

    @ESField(type = "keyword") //(name="api_type")
    @ApiModelProperty(value = "API类别，是草稿还是正式运行的日志，0 草稿，1 正式")
    private Integer apiType;

    //@OneToMany(targetEntity = TaskDetailLog.class)
    //@JoinESField(type = "keyword") //(name = "log_id", referencedESField(type = "keyword") //Name = "log_id")
    @JSONField(serialize = false, deserialize = false)
    @ApiModelProperty(value = "日志明细")
    private List<CallApiLogDetail> detailLogs;

    public void addDetailLog(CallApiLogDetail detailLog) {
        if (this.detailLogs == null) {
            this.detailLogs = new ArrayList<>();
        }
        this.detailLogs.add(detailLog);
    }
    /**
     * 临时记录 执行步骤
     */
    private int stepNo;

    public CallApiLog(){
        this.stepNo = 0;
    }

    public int plusStepNo(){
        this.stepNo = this.stepNo + 1;
        return this.stepNo;
    }

    @Override
    public String obtainDocumentId() {
        return this.logId;
    }

    @Override
    public JSONObject toJSONObject() {
        return (JSONObject) JSON.toJSON(this);
    }
}
