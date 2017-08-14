package mobi.eyeline.miniapps.bot.mnp;

import java.util.List;

/**
 * Created by jeck on 15/03/17.
 */
public class Mno {
    String id;
    String title;
    String region;
    List<String> masks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Mno{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", region='" + region + '\'' +
                ", masks=" + masks +
                '}';
    }

    public List<String> getMasks() {
        return masks;
    }

    public void setMasks(List<String> masks) {
        this.masks = masks;
    }
}
