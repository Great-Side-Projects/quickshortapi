package org.dev.quickshortapi.infraestructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.common.WebAdapter;
import org.dev.quickshortapi.domain.UrlStatisticsResponse;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.service.UrlService;
import org.dev.quickshortapi.domain.UrlShortenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@WebAdapter
@RestController
@RequestMapping("/url")
@Tag(name = "Url", description = "Url shortener")
public class UrlController {

    private final IUrlServicePort urlService;

    UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "shorten a url",
            description = "shorten a url and return the short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Short url created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlShortenResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @PostMapping("/shorten")
    public UrlShortenResponse shorten(
            @Parameter(description = "Url to shorten")
            @RequestBody
            UrlCommand urlCommand) {
        return urlService.shortenUrl(urlCommand);
    }

    @Operation(summary = "Redirect to original url",
            description = "Redirect to original url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "308",
                    description = "Found url by short url and redirect to original url"),
            @ApiResponse(responseCode = "404",
                    description = "Url not found",
                    content = @Content) })
    @GetMapping("/{shortUrl}")
    public ResponseEntity redirect(
            @Parameter(description = "Short url to redirect")
            @PathVariable String shortUrl) {
        //Todo: effiecient way to redirect?
        String urlOriginal = urlService.redirectUrl(shortUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(urlOriginal));
        return new ResponseEntity<>(httpHeaders, HttpStatus.PERMANENT_REDIRECT);
    }

    @Operation(summary = "Get statistics of a short url",
            description = "Get statistics of a short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found url by short url and return statistics",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlStatisticsResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @GetMapping("/statistics/{shortUrl}")
    public UrlStatisticsResponse statistics(
            @Parameter(description = "Short url to get statistics")
            @PathVariable String shortUrl) {
        return urlService.getUrlStatistics(shortUrl);
    }

    @Operation(summary = "Delete a short url",
            description = "Delete a short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Url deleted"),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @DeleteMapping("/{shortUrl}")
    public void delete(
            @Parameter(description = "Short url to delete")
            @PathVariable String shortUrl) {
        urlService.deleteUrlbyShortUrl(shortUrl);
    }

    @Operation(summary = "Delete cache of a short url",
            description = "Delete cache of a short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Url cache deleted"),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @DeleteMapping("/cache/{shortUrl}")
    public void deleteCache(
            @Parameter(description = "Short url to delete cache")
            @PathVariable String shortUrl) {
        urlService.deleteCachebyShortUrl(shortUrl);
    }
}
