package com.eyeline.miniapps.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jeck on 10/07/17.
 */
public class Page {
    private String id;
    @JsonProperty(value = "custom_id")
    private String customId;
    private String url;

    private List<Link> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

/*    public String getCustomId() {
        return customId;
    }
*/
    public String getMainIntent() {
        Set<String> intents = this.getIntents();
        if (intents == null || intents.size()==0) return null;
        return intents.iterator().next();
    }

    public Set<String> getIntents() {
        return ServiceAiHelper.getIntents(customId);
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public static class Link {
        @JsonProperty(value = "custom_id")
        private String customId;
        @JsonProperty(value = "page_id")
        private String pageId;
        @JsonProperty(value = "page_custom_id")
        private String pageCustomId;
        private String url;

        public String getCustomId() {
            return customId;
        }

        public Set<String> getIntents() {
            return ServiceAiHelper.getIntents(customId);
        }

        public void setCustomId(String customId) {
            this.customId = customId;
        }

        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPageCustomId() {
            return pageCustomId;
        }

        public void setPageCustomId(String pageCustomId) {
            this.pageCustomId = pageCustomId;
        }

        @Override
        public String toString() {
            return "Link{" +
                    "customId='" + customId + '\'' +
                    ", pageId='" + pageId + '\'' +
                    ", pageCustomId='" + pageCustomId + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Page{" +
                "id='" + id + '\'' +
                ", customId='" + customId + '\'' +
                ", url='" + url + '\'' +
                ", links=" + links +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return id.equals(page.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
