package com.centit.dde.po;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhf
 */
@ApiModel
@Data
@NoArgsConstructor
@Entity
@Table(name = "Q_DATASET_DEFINE")
public class DataSetDefine implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "查询ID", hidden = true)
    @Id
    @Column(name = "QUERY_ID")
    @NotBlank(message = "字段不能为空")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String queryId;

    @ApiModelProperty(value = "数据包ID", hidden = true)
    @Column(name = "PACKET_ID")
    @NotBlank(message = "字段不能为空")
    private String packetId;

    @ApiModelProperty(value = "查询名称")
    @Column(name = "QUERY_NAME")
    private String queryName;

    @ApiModelProperty(value = "查询描述")
    @Column(name = "QUERY_DESC")
    private String queryDesc;

    /**
     * 数据包类别，默认值为D， 其他类别有
     * D database，
     * J JSON文件， 数据导入导出的中间自定义格式
     * A API，
     * F file ，
     * P directory 文件夹 ,
     */
    @ApiModelProperty(value = "数据集类别，主要有 D database, E excel,C csv,J json, 默认值为D")
    @Column(name = "SET_TYPE")
    @NotBlank(message = "字段不能为空")
    private String setType;

    /**
     * 数据集参数描述
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DATASET_PARAMS_JSON")
    @ApiModelProperty(value = "数据集参数描述")
    private JSONObject datasetParamsJson;
    //{database:"", querySql:""}

    @Column(name = "DATABASE_CODE")
    @ApiModelProperty(value = "数据库代码，引用集成平台中定义的数据库")
    private String databaseCode;

    @ApiModelProperty(value = "带命名参数的sql语句")
    @Column(name = "QUERY_SQL")
    private String querySQL;

    @Column(name = "RECORDER")
    @ApiModelProperty(value = "修改人", hidden = true)
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @Column(name = "RECORD_DATE")
    @ApiModelProperty(value = "修改时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    @JSONField(serialize = false)
    private Date recordDate;

    @OneToMany(targetEntity = DataSetColumnDesc.class)
    @JoinColumn(name = "queryId", referencedColumnName = "queryId")
    private List<DataSetColumnDesc> columns;

    public List<DataSetColumnDesc> getColumns() {
        if(columns==null){
            columns = new ArrayList<>(4);
        }
        return columns;
    }

}
