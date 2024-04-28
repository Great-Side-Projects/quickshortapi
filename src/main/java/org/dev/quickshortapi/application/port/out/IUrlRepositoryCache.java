package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlCache;
import org.springframework.data.repository.CrudRepository;

public interface IUrlRepositoryCache extends CrudRepository<UrlCache, String>{

}
