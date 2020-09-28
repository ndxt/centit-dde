package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.BizSupplier;

public class SimpleBizSupplier implements BizSupplier {

    public static final SimpleBizSupplier DUMMY_BIZ_SUPPLIER
        = new SimpleBizSupplier(BizModel.EMPTY_BIZ_MODEL);

    private BizModel bizModel;

    public SimpleBizSupplier(BizModel bizModel) {
        this.bizModel = bizModel;
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

}
