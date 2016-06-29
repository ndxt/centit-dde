package com.centit.sys.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.OptLog;

public interface OptLogManager extends BaseEntityManager<OptLog> {
    List<String> listOptIds();
}
