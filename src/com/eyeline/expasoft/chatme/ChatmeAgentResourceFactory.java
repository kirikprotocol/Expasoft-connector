package com.eyeline.expasoft.chatme;

import com.eyeline.expasoft.chatme.model.ChatmeAgent;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import org.apache.commons.configuration.HierarchicalConfiguration;

import java.util.Properties;

/**
 * Created by jeck on 10/07/17.
 */
public class ChatmeAgentResourceFactory implements ResourceFactory {
    @Override
    public ChatmeAgent build(String id, Properties properties, HierarchicalConfiguration config) throws Exception {
        String username = InitUtils.getString("login", properties);
        String password = InitUtils.getString("password", properties);
        String url = InitUtils.getString("url", properties);
        int num = InitUtils.getInt("prediction-number", 3, properties);
        return new ChatmeAgent(url, username, password, num);
    }

    @Override
    public boolean isHeavyResource() {
        return false;
    }
}
