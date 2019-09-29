package com.centit.dde.po;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @ClassName TaskExchange
 * @Date 2019/3/20 9:21
 * @Version 1.0
 */
@Data
@Entity
@ApiModel
@Table(name = "D_EXCHANGE_TASK")
public class TaskExchange implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TASK_ID")
    @ApiModelProperty(value = "任务ID", hidden = true)
    @ValueGenerator(strategy = GeneratorType.UUID)
    @NotBlank
    private String taskId;

    @Column(name = "PACKET_ID")
    @ApiModelProperty(value = "包ID", required = true)

    private String packetId;
    @Column(name = "TASK_NAME")
    @ApiModelProperty(value = "任务名称", required = true)
    @NotBlank
    private String taskName;

    @Column(name = "TASK_TYPE")
    @ApiModelProperty(value = "任务类型,1表示普通任务，2表示定时任务", required = true)
    @NotBlank
    private String taskType;

    @Column(name = "TASK_CRON")
    @ApiModelProperty(value = "任务执行定时器")
    private String taskCron;

    @Column(name = "TASK_DESC")
    @ApiModelProperty(value = "任务描述")
    private String taskDesc;

    @JSONField(serialize=false)
    @Column(name = "EXCHANGE_DESC_JSON")
    @ApiModelProperty(value = "数据交换json格式的数据预处理说明", required = true)
    private String exchangeDescJson;

    @Column(name = "LAST_RUN_TIME")
    @ApiModelProperty(value = "上次执行时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date lastRunTime;

    @Column(name = "NEXT_RUN_TIME")
    @ApiModelProperty(value = "下次执行时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date nextRunTime;

    @Column(name = "IS_VALID")
    @ApiModelProperty(value = "是否启用", required = true)
    private String isValid;

    @Column(name = "CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date createTime;

    @Column(name = "CREATED")
    @ApiModelProperty(value = "创建人员", hidden = true)
    private String created;

    @Column(name = "LAST_UPDATE_TIME")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    @ApiModelProperty(value = "最后更新时间", hidden = true)
    private Date lastUpdateTime;
    @ApiModelProperty(value = "业务模块代码")
    @Column(name = "APPLICATION_ID")
    private String  applicationId;
    @JSONField(serialize = false)
    public String getExchangeDescJson() {
        return exchangeDescJson;
    }

    public void setExchangeDescJson(String exchangeDescJson) {
        this.exchangeDescJson = exchangeDescJson;
    }

    public JSONObject getExchangeDesc() {
        if(StringUtils.isBlank(exchangeDescJson)) {
            return null;
        }
        try {
            return JSONObject.parseObject(exchangeDescJson);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
