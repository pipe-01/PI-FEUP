package pt.feup.cosn.monitoring.repository;

import org.springframework.data.repository.CrudRepository;
import pt.feup.cosn.monitoring.model.service;

import java.util.List;

public interface serviceRepository extends CrudRepository<service, Long> {
    List<service> findAll();
}
