package com.centit.product.dataopt.core;

import java.util.function.Supplier;

public interface BizSupplier extends Supplier<BizModel> {

    /**
     * 业务数据是否是 批量的
     * 如果是，处理器将反复调用 。知道 get() 返回 null 结束
     * @return 否是 批量的
     */
    boolean isBatchWise();

}
