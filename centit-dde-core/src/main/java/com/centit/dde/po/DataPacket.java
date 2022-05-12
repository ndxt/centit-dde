package com.centit.dde.po;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * @author zhf
 */
@ApiModel
@Data
@Entity
@Table(name = "q_data_packet")
public class DataPacket implements Serializable,DataPacketInterface {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "数据处理ID", hidden = true)
    @Id
    @Column(name = "packet_id")
    @NotBlank(message = "字段不能为空")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String packetId;

    @ApiModelProperty(value = "数据处理名称")
    @Column(name = "packet_name")
    @NotBlank(message = "字段不能为空")
    private String packetName;

    @ApiModelProperty(value = "详细描述")
    @Column(name = "packet_desc")
    private String packetDesc;

    @Column(name = "owner_type")
    @ApiModelProperty(value = "属主类别（D:部门；U:用户）")
    private String ownerType;

    @Column(name = "owner_code")
    @ApiModelProperty(value = "属主代码")
    private String ownerCode;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data_opt_desc_json")
    @ApiModelProperty(value = "数据预处理描述 json格式的数据预处理说明", required = true)
    private JSONObject dataOptDescJson;

    @Column(name = "buffer_fresh_period")
    @ApiModelProperty(value = "缓存时间", required = true)
    private Integer bufferFreshPeriod;

    @Column(name = "buffer_fresh_period_type")
    @ApiModelProperty(value = "缓存单位, 1:分 2:时 3:日  -1:不缓存")
    private Integer bufferFreshPeriodType;

    @Column(name = "recorder")
    @ApiModelProperty(value = "创建人", hidden = true)
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @Column(name = "record_date")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date recordDate;

    @Column(name = "update_date")
    @ApiModelProperty(value = "修改时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateDate;

    @ApiModelProperty(value = "业务模块代码")
    @Column(name = "os_id")
    private String osId;

    @ApiModelProperty(value = "所属业务")
    @Column(name = "opt_id")
    private String optId;

    @Column(name = "task_type")
    @ApiModelProperty(value = "任务类型,1表示普通任务，2表示定时任务", required = true)
    @NotBlank
    private String taskType;

    @Column(name = "task_cron")
    @ApiModelProperty(value = "任务执行定时器")
    private String taskCron;

    @Column(name = "last_run_time")
    @ApiModelProperty(value = "上次执行时间")
    private Date lastRunTime;

    @Column(name = "next_run_time")
    @ApiModelProperty(value = "下次执行时间")
    private Date nextRunTime;

    @Column(name = "is_valid")
    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean isValid;

    @Column(name = "need_rollback")
    @ApiModelProperty(value = "是否回滚并结束")
    private String needRollback;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "return_result")
    @ApiModelProperty(value = "桩数据", required = true)
    private JSONObject returnResult;

    @Column(name = "publish_date")
    @ApiModelProperty(value = "发布时间")
    private Date publishDate;

    @Column(name = "ext_props")
    @ApiModelProperty(value = "队列配置扩展信息，独有配置，公共的配置在资源管理页面添加")
    private JSONObject extProps;

    @Column(name = "opt_code")
    @ApiModelProperty(value = "f_optdef表主键")
    private String optCode;

    @Column(name = "source_id")
    @ApiModelProperty(value = "模板来源")
    @JSONField(serialize = false)
    private String sourceId;

    @Column(name = "log_level")
    @ApiModelProperty(value = "日志记录级别，1=ERROR,3=INFO,7=DEBUG")
    private Integer logLevel;

    @Column(name = "is_disable")
    @ApiModelProperty(value = "是否逻辑删除，T：禁用，F：未禁用", required = true)
    private Boolean isDisable;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "schema_props")
    @ApiModelProperty(value = "模式属性")
    private JSONObject schemaProps;

    @Column(name = "request_body_type")
    @ApiModelProperty(value = "api请求体数据类型  O:对象，F:文件")
    @NotBlank
    private String requestBodyType;

    @OneToMany(targetEntity = DataPacketParam.class)
    @JoinColumn(name = "packetId", referencedColumnName = "packetId")
    private List<DataPacketParam> packetParams;

    public List<DataPacketParam> getPacketParams() {
        if (packetParams == null) {
            packetParams = new ArrayList<>(2);
        }
        return packetParams;
    }

    @Override
    @JSONField(serialize = false)
    public Map<String, Object> getPacketParamsValue() {
        Map<String, Object> params = new HashMap<>(10);
        if (packetParams == null) {
            return params;
        }
        for (DataPacketParam packetParam : packetParams) {
            if (packetParam.getParamType() != null) {
                switch (packetParam.getParamType()) {
                    case "N":
                        params.put(packetParam.getParamName(), NumberBaseOpt.castObjectToLong(packetParam.getParamDefaultValue()));
                        break;
                    case "T":
                        params.put(packetParam.getParamName(), DatetimeOpt.castObjectToSqlDate(packetParam.getParamDefaultValue()));
                        break;
                    default:
                        params.put(packetParam.getParamName(), packetParam.getParamDefaultValue());
                        break;
                }
            }
        }
        return params;
    }

}
