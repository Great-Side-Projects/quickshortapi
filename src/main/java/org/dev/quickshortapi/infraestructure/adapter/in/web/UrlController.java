package org.dev.quickshortapi.infraestructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.common.WebAdapter;
import org.dev.quickshortapi.application.port.out.UrlStatisticsResponse;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.service.UrlService;
import org.dev.quickshortapi.application.port.out.UrlShortenResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@WebAdapter
@RestController
@RequestMapping("/url")
@Tag(name = "Url", description = "Url shorten and redirect operations")
@Validated
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
            @ApiResponse(responseCode = "303",
                    description = "Found url by short url and redirect to original url"),
            @ApiResponse(responseCode = "404",
                    description = "Url not found",
                    content = @Content) })
    @GetMapping("/{shorturl}")
    public HttpEntity<Object> redirect(
            @Parameter(description = "Short url to redirect")
            @PathVariable String shorturl) {
        String urlOriginal = urlService.redirectUrl(shorturl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(urlOriginal));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(httpHeaders).build();
    }

    @Operation(summary = "Get statistics of a short url",
            description = "Get statistics of a short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found url by short url and return statistics",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlStatisticsResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @GetMapping("/statistics/{shorturl}")
    public UrlStatisticsResponse statistics(
            @Parameter(description = "Short url to get statistics")
            @PathVariable String shorturl) {
        return urlService.getUrlStatistics(shorturl);
    }

    @Operation(summary = "Delete a short url",
            description = "Delete a short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Url deleted"),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @DeleteMapping("/{shorturl}")
    public void delete(
            @Parameter(description = "Short url to delete")
            @PathVariable String shorturl) {
        urlService.deleteUrlbyShortUrl(shorturl);
    }

    @Operation(summary = "Delete cache of a short url",
            description = "Delete cache of a short url", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Url cache deleted"),
            @ApiResponse(responseCode = "404", description = "Url not found",
                    content = @Content) })
    @DeleteMapping("/cache/{shorturl}")
    public void deleteCache(
            @Parameter(description = "Short url to delete cache")
            @PathVariable String shorturl) {
        urlService.deleteCachebyShortUrl(shorturl);
    }

    @Operation(summary = "Get all urls",
            description = "Get all urls paginated", tags = "Url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found urls",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Urls not found",
                    content = @Content) })
    @GetMapping("/all")
       public ResponseEntity<Page<UrlResponse>> getAllUrls(
                @Parameter(description = "Page number")
               @RequestParam int page ,
                @Parameter(description = "Page size between 1 and 100")
               @RequestParam @Min(1) @Max(100) int pageSize) {
        return ResponseEntity.ok(urlService.getAllUrls(page,pageSize));
    }
}
