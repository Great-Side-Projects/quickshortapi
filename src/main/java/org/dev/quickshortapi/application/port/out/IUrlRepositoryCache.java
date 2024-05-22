package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntityCache;
import org.springframework.data.repository.CrudRepository;

public interface IUrlRepositoryCache extends CrudRepository<UrlEntityCache, String>{

}
