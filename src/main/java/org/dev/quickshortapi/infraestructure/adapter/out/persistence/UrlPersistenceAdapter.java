package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlMongoTemplate;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.application.port.out.IUrlRepository;

import java.util.Optional;

@PersistenceAdapter
public class UrlPersistenceAdapter implements IUrlPersistencePort{

    private final IUrlRepository UrlRepository;
    private final IUrlMongoTemplate urlMongoTemplate;
    private  final int INCREASE_VISITS_BY_1 = 1;

    public UrlPersistenceAdapter(IUrlMongoTemplate urlMongoTemplate,
                                 IUrlRepository UrlRepository){
        this.urlMongoTemplate = urlMongoTemplate;
        this.UrlRepository = UrlRepository;
    }

    @Override
    public String getShortUrlbyOriginalUrl(String originalUrl) {
        Optional<UrlEntity> existingOriginal = UrlRepository.findByOriginalUrl(originalUrl);
        if (existingOriginal.isPresent()) {
            System.out.println("URL existente findByUrlOriginal: " + existingOriginal.get().getShortUrl());
            return existingOriginal.get().getShortUrl(); // Devolver la URL corta existente si ya está en la base de datos
        }
        return "";
    }

    @Override
    public boolean existCollisionbyShortUrl(String shortUrl) {
        // Verificar si la URL corta ya existe en la base de datos
        if (UrlRepository.findByShortUrl(shortUrl).isPresent())
            return true;
        return false;
    }

    @Override
    public Url save(Url url) {
      UrlEntity urlEntity = UrlRepository.save(UrlMapper.toUrlEntity(url));
      url.setId(urlEntity.getId());
      return url;
    }

    @Override
    public Optional<Url> getUrlOrThrowByShortUrl(String shortUrl) {
        Optional<UrlEntity> url = UrlRepository.findByShortUrl(shortUrl);
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
        if (UrlRepository.findByShortUrl(shortUrl).isPresent()) {
            UrlRepository.deleteByShortUrl(shortUrl);
            System.out.println("URL corta eliminada: " + shortUrl);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UrlEntity> findByUShortUrl(String shortUrl) {
        return  UrlRepository.findByShortUrl(shortUrl);
    }
}
