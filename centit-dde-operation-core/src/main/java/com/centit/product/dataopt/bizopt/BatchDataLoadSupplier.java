package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.BizModel;

public abstract class BatchDataLoadSupplier extends DataLoadSupplier {

    /**
     * 迭代 数据读取状态
     */
    protected abstract void iterateModelTag();

    /**
     * 业务数据是否是 批量的
     * 如果是，处理器将反复调用 。知道 get() 返回 null 结束
     * @return 否是 批量的
     */
    public boolean isBatchWise(){
        return true;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public BizModel get() {
        BizModel bizModel = loadData();
        if(bizModel != null && bizModel.isEmpty()){
            return null;
        }
        iterateModelTag();
        return bizModel;
    }
}
