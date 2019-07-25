package com.popularmovies.Utilities.Objects;

public class Trailer {

    String name;
    String link;

    public Trailer() {
    }

    public Trailer(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
