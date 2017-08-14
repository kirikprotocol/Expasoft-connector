package com.eyeline.expasoft.chatme.utils;

import com.eyeline.expasoft.chatme.model.ChatmeAgent;
import com.eyeline.expasoft.chatme.model.IntentResponse;
import com.eyeline.expasoft.chatme.model.Intents;

/**
 * Created by jeck on 21/07/17.
 */
public class AgentUtils {
    public static void deleteAllIntents(ChatmeAgent agent) throws Exception {
        Intents intents = agent.getIntents();
        for (Intents.Info info:intents.getIntents()){
            try{
                IntentResponse res = agent.deleteIntent(info.getName());
                System.out.print(res);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        agent.doSave();
    }
}
