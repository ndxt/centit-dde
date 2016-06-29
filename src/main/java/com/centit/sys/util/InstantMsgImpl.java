package com.centit.sys.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ScriptSessions;

import com.centit.sys.service.CodeRepositoryManager;

public class InstantMsgImpl implements IInstantMsg {
    private static final String DEFAULT_SCRIPT = "jQuery.receive.receiveMessages";


    @Override
    public void pushAll(String script, String... params) {
        sendMessageAll(script, params);
    }

    @Override
    public void push(List<String> userCodes, String script, String... params) {
        sendMessage(getScriptSessionId(userCodes), script, params);
    }


    private void sendMessage(final List<String> scriptSessionIds, final String script, final String... params) {
        Browser.withAllSessionsFiltered(new ScriptSessionFilter() {

                                            public boolean match(ScriptSession session) {
                                                return scriptSessionIds.contains(session.getId());
                                            }
                                        }, new Runnable() {
                                            @Override
                                            public void run() {
                                                ScriptBuffer sb = new ScriptBuffer();

                                                sb.appendCall(script, params);

                                                Collection<ScriptSession> sessions = Browser.getTargetSessions();

                                                for (ScriptSession scriptSession : sessions) {
                                                    scriptSession.addScript(sb);
                                                }
                                            }
                                        }
        );
    }

    private List<String> getScriptSessionId(List<String> userCodes) {
        List<String> scriptSessionId = new ArrayList<String>();
        for (String userCode : userCodes) {
            scriptSessionId.add(CodeRepositoryManager.USERCODE_SCRIPTSESSION.get(userCode));
        }

        return scriptSessionId;
    }

    private void sendMessageAll(final String script, final String... params) {
        Browser.withAllSessions(new Runnable() {
            @Override
            public void run() {
                ScriptSessions.addFunctionCall(script, params);
            }
        });
    }
}
