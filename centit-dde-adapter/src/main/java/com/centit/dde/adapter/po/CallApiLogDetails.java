package com.centit.dde.adapter.po;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.search.annotation.ESField;
import com.centit.search.annotation.ESType;
import com.centit.search.document.ESDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
@ESType(indexName = "callapilogdetails", shards = 5)
public class CallApiLogDetails implements ESDocument, Serializable {

    private static final long serialVersionUID =  1L;
    //和CallApiLog 是一对一的关系，避免 查询列表时返回的内容过多单独用一个索引记录
    @ESField(type = "keyword")
    private String logId;

    @ESField(type = "keyword")
    private String taskId;

    @ESField(type = "Date")
    @ApiModelProperty(value = "执行开始时间")
    private Date runBeginTime;

    @ESField(type = "text")
    private List<CallApiLogDetail> detailLogs;

    @Override
    public String obtainDocumentId() {
        return this.logId;
    }

    @Override
    public JSONObject toJSONObject() {
        return (JSONObject) JSON.toJSON(this);
    }
}
