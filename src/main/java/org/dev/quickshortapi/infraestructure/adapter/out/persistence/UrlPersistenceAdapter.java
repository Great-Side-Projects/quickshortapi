package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.dev.quickshortapi.application.port.out.*;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@PersistenceAdapter
public class UrlPersistenceAdapter implements IUrlPersistencePort {

    private final IUrlRepository urlRepository;
    private static final int INCREMENT_VISITS_BY_1 = 1;
    private static final int PAGE_SIZE = 100;
    private final IUrlFormatProviderPort urlFormatProviderAdapter;

    public UrlPersistenceAdapter(IUrlRepository urlRepository, IUrlFormatProviderPort urlFormatProviderAdapter){
        this.urlRepository = urlRepository;
        this.urlFormatProviderAdapter = urlFormatProviderAdapter;
    }

    @Override
    public String getShortUrlbyOriginalUrl(String originalUrl) {
        return urlRepository.findShortUrlByOriginalUrl(originalUrl)
                .map(UrlEntity::getShortUrl)
                .orElse("");
    }

    @Override
    public boolean existCollisionbyShortUrl(String shortUrl) {
        return urlRepository.existsByShortUrl(shortUrl);
    }

    @Override
    public Url save(Url url) {
      UrlEntity urlEntity = urlRepository.save(UrlMapper.toUrlEntity(url));
      url.setId(urlEntity.getId());
      return url;
    }

    @Override
    public Optional<Url> getUrlOrThrowByShortUrl(String shortUrl) {
        Optional<UrlEntity> url = urlRepository.findOriginalUrlByShortUrl(shortUrl);
        if(url.isEmpty())
            throw new UrlNotFoundException("URL corta no encontrada");
        return Optional.of(UrlMapper.toUrl(url.get()));
    }

    @Override
    public void incrementVisitsAndUpdateLastVisitedDate(UrlEvent urlEvent) {
        urlRepository.incrementVisitsAndUpdateLastVisitedDateById(
                urlEvent.getId(),
                INCREMENT_VISITS_BY_1,
                urlEvent.getLastVisitedDate());
    }

    @Override
    public boolean deleteUrlbyShortUrl(String shortUrl) {
        if (urlRepository.existsByShortUrl(shortUrl)) {
            urlRepository.deleteByShortUrl(shortUrl);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UrlStatisticsResponse> getStatisticsByShortUrl(String shortUrl) {
        return urlRepository.findUrlStatisticsByShortUrl(shortUrl)
                .map(url -> UrlMapper.toUrlStatisticsResponse(url, urlFormatProviderAdapter));
    }

    @Override
    public Page<UrlResponse> getAllUrls(int page, int pageSize) {
        if (pageSize > PAGE_SIZE)
            pageSize = PAGE_SIZE;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UrlEntity> urls = urlRepository.findAll(pageable);
       return urls.map(url -> UrlMapper.toUrlResponse(url, urlFormatProviderAdapter));
    }
}