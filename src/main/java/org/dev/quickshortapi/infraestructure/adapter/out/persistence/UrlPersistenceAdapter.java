package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlMongoTemplate;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.application.port.out.IUrlRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@PersistenceAdapter
public class UrlPersistenceAdapter implements IUrlPersistencePort{

    private final IUrlRepository urlRepository;
    private final IUrlMongoTemplate urlMongoTemplate;
    private final int INCREASE_VISITS_BY_1 = 1;
    private final int PAGE_SIZE = 100;

    public UrlPersistenceAdapter(IUrlMongoTemplate urlMongoTemplate,
                                 IUrlRepository UrlRepository){
        this.urlMongoTemplate = urlMongoTemplate;
        this.urlRepository = UrlRepository;
    }

    @Override
    public String getShortUrlbyOriginalUrl(String originalUrl) {
        Optional<UrlEntity> existingOriginal = urlRepository.findByOriginalUrl(originalUrl);
        if (existingOriginal.isPresent()) {
            return existingOriginal.get().getShortUrl(); // Devolver la URL corta existente si ya está en la base de datos
        }
        return "";
    }

    @Override
    public boolean existCollisionbyShortUrl(String shortUrl) {
        // Verificar si la URL corta ya existe en la base de datos
        if (urlRepository.findByShortUrl(shortUrl).isPresent())
            return true;
        return false;
    }

    @Override
    public Url save(Url url) {
      UrlEntity urlEntity = urlRepository.save(UrlMapper.toUrlEntity(url));
      url.setId(urlEntity.getId());
      return url;
    }

    @Override
    public Optional<Url> getUrlOrThrowByShortUrl(String shortUrl) {
        Optional<UrlEntity> url = urlRepository.findByShortUrl(shortUrl);
        if(url.isEmpty())
            throw new UrlNotFoundException("URL corta no encontrada");
        return Optional.of(UrlMapper.toUrl(url.get()));
    }

    @Override
    public void increaseVisitsAndUpdateLastVisitedDate(Url url) {
        try{
            urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate(
                    url.getId(),
                    INCREASE_VISITS_BY_1,
                    url.getLastVisitedDate());
        }
        catch (Exception e) {
            throw new UrlInternalServerErrorException("Error interno al incrementar las visitas:" + e.getMessage());
        }
    }

    @Override
    public boolean deleteUrlbyShortUrl(String shortUrl) {
        if (urlRepository.findByShortUrl(shortUrl).isPresent()) {
            urlRepository.deleteByShortUrl(shortUrl);
            System.out.println("URL corta eliminada: " + shortUrl);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UrlEntity> findByUShortUrl(String shortUrl) {
        return  urlRepository.findByShortUrl(shortUrl);
    }

    @Override
    public Page<UrlEntity> getAllUrls(int page) {
       Pageable pageable = PageRequest.of(page, PAGE_SIZE);
       return urlRepository.findAll(pageable);
    }
}
