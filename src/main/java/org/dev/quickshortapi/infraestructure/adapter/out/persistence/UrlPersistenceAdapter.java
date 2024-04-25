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
    private static final String VISITAS = "visitas";

    public UrlPersistenceAdapter(MongoTemplate mongoTemplate,
                                 UrlRepository urlRepository){
        this.mongoTemplate = mongoTemplate;
        this.urlRepository = urlRepository;
    }

    @Override
    public String getShortUrlbyOriginalUrl(String urlOriginal) {
        Optional<UrlEntity> existenteoriginal = urlRepository.findByUrlOriginal(urlOriginal);
        if (existenteoriginal.isPresent()) {
            System.out.println("URL existente findByUrlOriginal: " + existenteoriginal.get().getUrlCorta());
            return existenteoriginal.get().getUrlCorta(); // Devolver la URL corta existente si ya está en la base de datos
        }
        return "";
    }

    @Override
    public boolean existCollisionbyShortUrl(String urlCorta) {
        // Verificar si la URL corta ya existe en la base de datos
        if (urlRepository.findByUrlCorta(urlCorta).isPresent())
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
    public Optional<Url> getUrlOrThrowByShortUrl(String urlCorta) {
        Optional<UrlEntity> url = urlRepository.findByUrlCorta(urlCorta);
        if(url.isEmpty())
            throw new UrlNotFoundException("URL corta no encontrada");
        return Optional.of(UrlMapper.toUrl(url.get()));
    }

    @Override
    public void increaseVisits(Url url) {
        try{
            Query query = new Query(Criteria.where("_id").is(url.getId()));
            Update update = new Update().inc(VISITAS, INCREASE_VISITS_BY_1);
            mongoTemplate.updateFirst(query, update, UrlEntity.class);
        }
        catch (Exception e) {
            throw new UrlInternalServerErrorException("Error interno al incrementar las visitas:" + e.getMessage());
        }
    }

    @Override
    public boolean deleteUrlbyShortUrl(String urlCorta) {
        if (urlRepository.findByUrlCorta(urlCorta).isPresent()) {
            urlRepository.deleteByUrlCorta(urlCorta);
            System.out.println("URL corta eliminada: " + urlCorta);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UrlEntity> findByUrlCorta(String urlCorta) {
        return  urlRepository.findByUrlCorta(urlCorta);
    }
}
