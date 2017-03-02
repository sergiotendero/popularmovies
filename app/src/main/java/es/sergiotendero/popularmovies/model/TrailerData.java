package es.sergiotendero.popularmovies.model;

/**
 * Model class for trailers data
 */
public class TrailerData {

    private String name;
    private String site;
    private String key;

    public TrailerData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
