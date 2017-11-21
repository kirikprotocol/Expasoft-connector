package com.eyeline.miniapps.connector;

import com.eyeline.miniapps.filter.Filter;
import com.eyeline.miniapps.ai.model.AiAgent;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSInitializer;
import com.eyelinecom.whoisd.sads2.resource.ResourceStorage;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jeck on 24/07/17.
 */
@Path("ai/{aiAgent}")
public class AiResource implements AiAgent{
    private static final Logger log = Logger.getLogger(AiResource.class);

    private ResourceStorage storage;
    private String filterName = "ai-chat-filters";

    @PathParam("aiAgent")
    String aiAgent;

    public AiResource() {
        storage = SADSInitializer.getResourceStorage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Prediction> predict(@QueryParam("q") String query) {
        AiAgent agent;
        Filter filter = null;
        try{
            storage.lock();
            agent = (AiAgent) storage.get(aiAgent);
            filter = (Filter) storage.get(filterName);
        } catch (Exception e){
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        } finally {
            storage.unlock();
        }
        if (filter!=null) {
            String filteredQuery = filter.filter(query);
            if (log.isDebugEnabled()) {
                log.debug("query '"+query+"' -> '"+filteredQuery+"'");
            }
            query = filteredQuery;
        }
        try {
            return agent.predict(query);
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
