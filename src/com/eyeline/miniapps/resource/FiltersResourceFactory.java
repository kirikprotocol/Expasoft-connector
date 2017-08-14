package com.eyeline.miniapps.resource;

import com.eyeline.expasoft.chatme.filter.ChainFilter;
import com.eyeline.expasoft.chatme.filter.ConditionFilter;
import com.eyeline.expasoft.chatme.filter.Filter;
import com.eyelinecom.whoisd.sads2.common.Configurator;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSInitializer;
import com.eyelinecom.whoisd.sads2.registry.ResourceConsumerConfigurator;
import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import com.eyelinecom.whoisd.sads2.resource.ResourceStorage;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by jeck on 19/07/17.
 */
public class FiltersResourceFactory  implements ResourceFactory {
    private static Log logger = new Log4JLogger(Logger.getLogger(FiltersResourceFactory.class));

    @SuppressWarnings("unchecked")
    @Override
    public Filter build(String id, Properties properties, HierarchicalConfiguration config) throws Exception {
        FilterConfigurator configurator = new FilterConfigurator();
        // This is hack to get rid of dependency on SADSInitializer
        String resourceStorageProviderClass = SADSInitUtils.getString("resource-storage-provider", SADSInitializerResourceStorageProvider.class.getName(), properties);
        Callable<ResourceStorage> provider = (Callable<ResourceStorage>) Class.forName(resourceStorageProviderClass).newInstance();
        configurator.setResourceStorage(provider.call());
        return configurator.configure(config);
    }

    @Override
    public boolean isHeavyResource() {
        return false;
    }

    public static class FilterConfigurator extends ResourceConsumerConfigurator<Filter> {

        @Override
        public ChainFilter configure(HierarchicalConfiguration config) throws Exception {
            List filterConfigs = config.configurationsAt("filter");
            List<Filter> filters = new ArrayList<Filter>();
            for (Object filterConfObj:filterConfigs) {
                HierarchicalConfiguration filterConfig = (HierarchicalConfiguration) filterConfObj;
                String filterClassname = filterConfig.getString("class");
                Filter filter = (Filter) Class.forName(filterClassname).getConstructors()[0].newInstance();
                if (filter instanceof Initable) {
                    Properties filterProperties = Configurator.parseProperties(filterConfig);
                    if (filter instanceof ConditionFilter) {
                        ChainFilter subfilter = this.configure(filterConfig);
                        filterProperties.put("filter", subfilter);
                        filterProperties.putAll(this.getResources(config));
                    }
                    ((Initable) filter).init(filterProperties);
                }
                filters.add(filter);
            }
            return new ChainFilter(filters);
        }

        @Override
        protected Category getLogger() {
            return ((Log4JLogger)logger).getLogger();
        }
    }

    public static class SADSInitializerResourceStorageProvider implements Callable<ResourceStorage> {

        @Override
        public ResourceStorage call() throws Exception {
            return SADSInitializer.getResourceStorage();
        }
    }
}
