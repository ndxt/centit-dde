package com.centit.app.service;

import com.centit.app.po.UserMailConfig;
import com.centit.core.service.BaseEntityManager;

public interface UserMailConfigManager extends BaseEntityManager<UserMailConfig> {
    /**
     * 验证当前邮箱账户有效性
     *
     * @param c
     * @return
     */
    boolean isEffective(UserMailConfig c);
}
