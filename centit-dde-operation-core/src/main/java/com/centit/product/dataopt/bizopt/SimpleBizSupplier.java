package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.BizSupplier;

public class SimpleBizSupplier implements BizSupplier {

    public static final SimpleBizSupplier DUMMY_BIZ_SUPPLIER
        = new SimpleBizSupplier(BizModel.EMPTY_BIZ_MODEL);

    private BizModel bizModel;

    public SimpleBizSupplier(){

    }

    public SimpleBizSupplier(BizModel bizModel) {
        this.bizModel = bizModel;
    }

    /**
     * 业务数据是否是 批量的
     * 如果是，处理器将反复调用 。知道 get() 返回 null 结束
     * @return 否是 批量的
     */
    public boolean isBatchWise(){
        return false;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public BizModel get() {
        return bizModel;
    }

    public BizModel getBizModel() {
        return bizModel;
    }

    public void setBizModel(BizModel bizModel) {
        this.bizModel = bizModel;
    }
}
