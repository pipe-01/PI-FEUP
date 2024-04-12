package pt.cosn.mascontroller.services.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pt.cosn.mascontroller.dtos.DomainModelDto;
import pt.cosn.mascontroller.dtos.requests.ArtificialSystemRequestDto;
import pt.cosn.mascontroller.services.DomainModelService;

@Service
public class DefaultDomainModelService implements DomainModelService {

  private static final Logger log = LoggerFactory.getLogger(DefaultDomainModelService.class);

  private final RestTemplate restTemplate;

  @Value("${service.domain-model.api}")
  private String END_POINT;

  public DefaultDomainModelService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  @Override
  public DomainModelDto findById(String id, String token) {
    try{
      MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
      headers.add("Authorization", token);
      ResponseEntity<DomainModelDto> entity = restTemplate.exchange(END_POINT + Integer.valueOf(id), HttpMethod.GET, new HttpEntity<Object>(headers),
          DomainModelDto.class);

      return entity.getBody();
    } catch (Exception exception){
      log.error("findById error: ", exception);
    }
    return null;
  }
}
