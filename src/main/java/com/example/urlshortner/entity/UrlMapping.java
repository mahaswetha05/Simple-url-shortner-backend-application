package com.example.urlshortner.entity;

import jakarta.persistence.*;

@Entity

public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String shortCode;

    String longUrl;

    public UrlMapping() {
    }

    public UrlMapping(String shortCode, String longUrl) {
        this.shortCode = shortCode;
        this.longUrl = longUrl;
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

}
