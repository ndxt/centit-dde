package com.centit.dde.adapter.po;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
@Table(name = "q_data_packet_draft")
public class DataPacketDraft implements Serializable, DataPacketInterface {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "数据处理ID", hidden = true)
    @Id
    @Column(name = "packet_id")
    @NotBlank
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String packetId;

    @ApiModelProperty(value = "数据处理名称")
    @Column(name = "packet_name")
    @NotBlank
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
    @ApiModelProperty(value = "缓存单位, 1:分 2:时 3:日 -1:不缓存")
    private Integer bufferFreshPeriodType;

    @Column(name = "recorder")
    @ApiModelProperty(value = "创建人", hidden = true)
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @Column(name = "record_date")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW, value = "today()")
    private Date recordDate;

    @Column(name = "update_date")
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date updateDate;

    @ApiModelProperty(value = "业务模块代码", required = true)
    @Column(name = "os_id")
    @NotBlank
    private String osId;

    @ApiModelProperty(value = "所属模块", required = true)
    @Column(name = "opt_id")
    @NotBlank
    private String optId;

    @Column(name = "task_type")
    @ApiModelProperty(value = "任务类型1：GET请求，2：表示定时任务,3：POST请求,4：消息触发5：PUT请求6：DELETE请求7：子模块", required = true)
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
    @ValueGenerator(strategy = GeneratorType.CONSTANT, occasion = GeneratorTime.NEW, value = "T")
    private Boolean isValid;

    @Column(name = "need_rollback")
    @ApiModelProperty(value = "是否回滚并结束")
    private String needRollback;

    @Column(name = "publish_date")
    @ApiModelProperty(value = "发布时间")
    private Date publishDate;

    @Column(name = "ext_props")
    @ApiModelProperty(value = "队列配置扩展信息，独有配置，公共的配置在资源管理页面添加")
    private JSONObject extProps;

    @Column(name = "opt_code")
    @ApiModelProperty(value = "f_optdef表主键")
    private String optCode;

    @Column(name = "template_type")
    @ApiModelProperty(value = "模板(操作)类型：1：新建 2：修改 3：删除 4：查询 5：查看 6：创建流程 7：提交流程 8：http调用 9: 导出Excel")
    private Integer templateType;

    /**
     * template_type  metadata_table_id  这2个字段都是给前端做判断使用的，dde本身不需要这2个字段
     */
    @Column(name = "metadata_table_id")
    @ApiModelProperty(value = "通过元数据模板生成接口时会选择一个表，这个存选择的表ID")
    private String metadataTableId;

    @Column(name = "log_level")
    @ApiModelProperty(value = "日志记录级别，1=ERROR,3=INFO,7=DEBUG")
    private Integer logLevel;

    @Column(name = "is_disable")
    @ApiModelProperty(value = "是否逻辑删除，T：禁用，F：未禁用", required = true)
    @ValueGenerator(strategy = GeneratorType.CONSTANT, occasion = GeneratorTime.NEW, value = "F")
    private Boolean isDisable;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "schema_props")
    @ApiModelProperty(value = "模式属性")
    private JSONObject schemaProps;

    @Column(name = "request_body_type")
    @ApiModelProperty(value = "api数据类型  O:对象，F:文件")
    @NotBlank
    private String requestBodyType;

    @ApiModelProperty(value = "降级等级")
    @Column(name = "FALL_BACK_LEVEL")
    private String fallBackLevel;

    @OneToMany(targetEntity = DataPacketParamDraft.class)
    @JoinColumn(name = "packetId", referencedColumnName = "packetId")
    private List<DataPacketParamDraft> packetParams;

    @ApiModelProperty(hidden = true)
    private Object optMethod;

    @Transient
    @JSONField(serialize = false, deserialize = false)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private DataOptStep innerDataOptStep;

    public void setDataOptDescJson(JSONObject dataOptDescJson) {
        this.dataOptDescJson = dataOptDescJson;
    }

    @Override
    public DataOptStep attainDataOptStep() {
        if (innerDataOptStep == null) {
            if(dataOptDescJson==null)
                return null;
            innerDataOptStep = new DataOptStep(dataOptDescJson);
        }
        return new DataOptStep(innerDataOptStep.getNodeMap(), innerDataOptStep.getLinkMap());
    }

    public List<DataPacketParamDraft> getPacketParams() {
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
        for (DataPacketParamDraft packetParam : packetParams) {
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
