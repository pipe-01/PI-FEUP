package pt.cosn.mascontroller.services.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.cosn.mascontroller.dtos.AlgorithmDto;
import pt.cosn.mascontroller.services.AlgorithmService;

@Service
public class DefaultAlgorithmService implements AlgorithmService {

  private static final Logger log = LoggerFactory.getLogger(DefaultAlgorithmService.class);

  private final RestTemplate restTemplate;

  @Value("${service.algorithm.api}")
  private String END_POINT;

  public DefaultAlgorithmService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  @Override
  public AlgorithmDto findById(String id) {
    try{
      return restTemplate.getForObject(END_POINT + id, AlgorithmDto.class);
    }catch (Exception exception){
      log.error("findById error: ", exception);
    }
    return null;
  }
}
