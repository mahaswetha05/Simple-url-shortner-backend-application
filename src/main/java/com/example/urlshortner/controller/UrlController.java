package com.example.urlshortner.controller;

import com.example.urlshortner.dto.UrlRequest;
import com.example.urlshortner.service.UrlService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // Create short URL
    @PostMapping("/api/shorten")
    public String shortenUrl(
            @RequestBody UrlRequest request) {

        String shortCode =
                urlService.shortenUrl(request.getUrl());

        return "http://localhost:8080/" + shortCode;
    }

    // Redirect short URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        String originalUrl =
                urlService.getOriginalUrl(shortCode);

        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(302)
                .location(URI.create(originalUrl))
                .build();
    }
}