package com.example.urlshortner.service;

import com.example.urlshortner.entity.UrlMapping;
import com.example.urlshortner.repository.UrlRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Create short URL
    public String shortenUrl(String longUrl) {

        // Generate a random short code
        String shortCode = UUID.randomUUID()
                .toString()
                .substring(0, 6);

        // Create Entity object
        UrlMapping mapping =
                new UrlMapping(shortCode, longUrl);

        // Save Entity into database
        urlRepository.save(mapping);

        // Return short code
        return shortCode;
    }

    // Find original URL
    public String getOriginalUrl(String shortCode) {

        Optional<UrlMapping> mapping =
                urlRepository.findByShortCode(shortCode);

        if (mapping.isPresent()) {

            return mapping.get().getLongUrl();

        }

        return null;
    }
}