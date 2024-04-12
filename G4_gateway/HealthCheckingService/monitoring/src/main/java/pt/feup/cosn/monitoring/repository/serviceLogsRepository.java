package pt.feup.cosn.monitoring.repository;

import org.springframework.data.repository.CrudRepository;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;

import java.util.List;
import java.util.Optional;


public interface serviceLogsRepository extends CrudRepository<serviceLog, Long> {
    List<serviceLog> findAll();
    Optional<serviceLog> findBySer(service ser);
}
