package com.centit.dde.ws;

/**
 * Created by sx on 2014/12/10.
 */
public interface ValidatorWs {
    String validatorUserinfo(String userName, String userPin);

    String validatorDataOptId(String userName, String dataOptId, String dataOptType);
}
