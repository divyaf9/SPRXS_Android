package com.divya.sprxs.model;

public class Weblink {

    private Long id;
    private String type;
    private String url;

    public Weblink(Long id, String type, String url) {
        this.id = id;
        this.type = type;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
