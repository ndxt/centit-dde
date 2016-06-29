package com.centit.sys.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.FOptdef;

public interface OptdefManager extends BaseEntityManager<FOptdef> {
    public List<FOptdef> getOptDefByOptID(String sOptID);
}
