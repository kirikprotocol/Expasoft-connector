package com.eyeline.miniapps.ai.model;

import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import java.util.*;

/**
 * Created by jeck on 10/07/17.
 */
public class ServiceAiHelper {
    Set<Page> pages;

    public ServiceAiHelper(Set<Page> pages) {
        this.pages = pages;
    }

    public Page lookupGlobal(String intent) {
        intent = filterByAvg(intent);
        if (StringUtils.isBlank(intent)) return null;
        for (Page page: pages) {
            Set<String> intents = page.getIntents();
            if (intents.contains(intent)) {
                return page;
            }
        }
        return null;
    }

    private double calcAgerage(List<AiAgent.Prediction> predictions) {
        double sum = 0;
        for (AiAgent.Prediction p: predictions) {
            sum = sum + p.getProba();
        }
        return sum/predictions.size();
    }

    private double calcRelative(AiAgent.Prediction main, List<AiAgent.Prediction> predictions) {
        double sum = 0;
        for (AiAgent.Prediction p: predictions) {
            sum = sum + p.getProba();
        }
        return main.getProba()/sum;
    }

    public List<AiAgent.Prediction> filterByRelative(List<AiAgent.Prediction> predictions, double thr, Log log) {
        List<AiAgent.Prediction> result = new ArrayList<>();
        for (AiAgent.Prediction p: predictions) {
            double rel = this.calcRelative(p, predictions);
            log.info(rel+" --rel-score-of--- " +p);
            if (rel > thr) {
                result.add(p);
            }
        }
        return result;
    }

    public List<AiAgent.Prediction> filterByAvg(List<AiAgent.Prediction> predictions, double thr, Log log) {
        List<AiAgent.Prediction> result = new ArrayList<>();
        double avg = this.calcAgerage(predictions);
        for (AiAgent.Prediction p: predictions) {
            double t = p.getProba() - avg - thr;
            log.info(t+" --avg-score-of--- " +p);
            if (t > 0) {
                result.add(p);
            }
        }
        return result;
    }

    public String bestIntent(Page currentPage, List<AiAgent.Prediction> predictions, Log log) {
        if (predictions==null|| predictions.size()==0) return null;
        for (AiAgent.Prediction prediction: predictions) {
            Page page = lookupLocal(currentPage, prediction.getIntent());
            if (page!=null) return prediction.getIntent();
        }
        for (AiAgent.Prediction prediction: predictions) {
            Page page = lookupTree(currentPage, prediction.getIntent(), log);
            if (page!=null) return prediction.getIntent();
        }
        for (AiAgent.Prediction p: predictions) {
            Page page = lookupGlobal(p.getIntent());
            if (page!=null) return p.getIntent();
        }
        return predictions.get(0).getIntent();
    }

    public Page lookupLocal(Page currentPage, String intent) {
        if (currentPage==null) return null;
        if (StringUtils.isBlank(intent)) return null;
        if (currentPage.getLinks()==null || currentPage.getLinks().size()==0) return null;
        intent = filterByAvg(intent);
        if (intent == null) return null;
        for(Page.Link link: currentPage.getLinks()) {
            Set<String> linkIntents = link.getIntents();
            if (linkIntents.contains(intent)) {
                Page target = this.getById(link.getPageId());
                if (target!=null) {
                    return target;
                }
            }
        }
        return null;
    }

    public Page lookupTree(Page currentPage, String intent, Log log) {
        if (currentPage==null) return null;
        Set<String> pageIntents = currentPage.getIntents();
        if (pageIntents.size()==0) return null;
        if (StringUtils.isBlank(intent)) return null;
        intent = filterByAvg(intent);
        if (intent == null) return null;
        List<String> intentsToCheck = new ArrayList<>();
        for(String baseIntent: pageIntents) {
            if (StringUtils.isBlank(baseIntent)) continue;
            List<String> intents = buildContextIntentsList(baseIntent, intent);
            intentsToCheck.addAll(intents);
        }
        log.info("Page intent: "+pageIntents+", usersIntent: "+intent+", context intents to check: "+intentsToCheck);
        for (String contextIntent: intentsToCheck) {
            Page page = this.lookupGlobal(contextIntent);
            if (page!=null) {
                log.info("Intent: "+contextIntent+" fired! Got page "+page);
                return page;
            }
        }
        return null;
    }


    public Page getByUrl(String url) {
        //http://10.151.0.44:38982/index?sid=1151&pid=1
        String id = UrlUtils.getParameter(url, "pid");
        if (StringUtils.isBlank(id)) {
            id = "1";
        }
        return this.getById(id);
    }

    public Page getById(String id) {
        for (Page page: pages) {
            if (id.equals(page.getId())) {
                return page;
            }
        }
        return null;
    }

    public static String filterByAvg(String id) {
        id = StringUtils.trim(id);
        if (StringUtils.isBlank(id)) return null;
        return id.toLowerCase();
    }

    public static List<String> buildContextIntentsList(String pageIntent, String usersIntent) {
        String[] contexts = StringUtils.split(pageIntent, '.');
        List<String> intentsToCheck = new ArrayList<>();
        for(int i=0; i<contexts.length; i++) {
            String[] contextArray = new String[contexts.length-i + 1];
            System.arraycopy(contexts, 0, contextArray, 0, contexts.length - i);
            contextArray[contexts.length-i] = usersIntent;
            String intentName = String.join(".", Arrays.asList(contextArray));
            intentsToCheck.add(intentName);
        }
        return intentsToCheck;
    }

    public static LinkedHashSet<String> getIntents(String id){
        LinkedHashSet<String> intents = new LinkedHashSet<>();
        String customIds = id!=null?id:"";
        String[] data = customIds.split(" ");
        for (String d: data) {
            intents.add(ServiceAiHelper.filterByAvg(d));
        }
        return intents;
    }
}
