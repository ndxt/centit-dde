package com.centit.dde.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
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
import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@Entity
@Table(name = "q_data_packet_template")
public class DataPacketTemplate  implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "数据处理ID", hidden = true)
    @Id
    @Column(name = "id")
    @NotBlank(message = "字段不能为空")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String id;

    @Column(name = "template_type")
    @ApiModelProperty(value = "1:api网关，2：元数据，3：第三方接口调用")
    private Integer templateType;

    @Column(name = "os_id")
    @ApiModelProperty(value = "应用ID")
    private String osId;

    @Column(name = "opt_id")
    @ApiModelProperty(value = "业务模块id")
    private String optId;

    @Column(name = "content")
    @ApiModelProperty(value = "模板内容")
    private JSONObject content;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date createTime;


    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateTime;

    @Column(name = "create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @Column(name = "is_valid")
    @ApiModelProperty(value = "是否可用")
    private Integer isValid;


}
