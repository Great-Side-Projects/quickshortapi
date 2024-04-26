package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.application.port.out.UrlRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.Optional;

@PersistenceAdapter
public class UrlPersistenceAdapter implements IUrlPersistencePort{

    private final MongoTemplate mongoTemplate;
    private final UrlRepository urlRepository;
    private static final int INCREASE_VISITS_BY_1 = 1;
    private static final String VISITS = "visits";

    public UrlPersistenceAdapter(MongoTemplate mongoTemplate,
                                 UrlRepository urlRepository){
        this.mongoTemplate = mongoTemplate;
        this.urlRepository = urlRepository;
    }

    @Override
    public String getShortUrlbyOriginalUrl(String originalUrl) {
        Optional<UrlEntity> existingOriginal = urlRepository.findByOriginalUrl(originalUrl);
        if (existingOriginal.isPresent()) {
            System.out.println("URL existente findByUrlOriginal: " + existingOriginal.get().getShortUrl());
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
    public void increaseVisits(Url url) {
        try{
            Query query = new Query(Criteria.where("_id").is(url.getId()));
            Update update = new Update().inc(VISITS, INCREASE_VISITS_BY_1);
            mongoTemplate.updateFirst(query, update, UrlEntity.class);
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
}
