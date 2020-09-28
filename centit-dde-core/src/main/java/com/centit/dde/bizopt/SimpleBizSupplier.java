package com.centit.dde.bizopt;

import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizSupplier;

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
