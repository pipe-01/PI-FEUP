package pt.cosn.mascontroller.services.Impl;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.cosn.mascontroller.dtos.requests.ArtificialSystemRequestDto;
import pt.cosn.mascontroller.dtos.requests.GatewayRequestDto;
import pt.cosn.mascontroller.dtos.responses.GatewayResponseDto;
import pt.cosn.mascontroller.services.ApiGatewayService;

@Service
public class DefaultApiGatewayService implements ApiGatewayService {

  private final RestTemplate restTemplate;

  @Value("${service.gateway.api}")
  private String END_POINT;

  public DefaultApiGatewayService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  @Override
  public boolean isTokenValid(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<GatewayRequestDto> request = new HttpEntity<>(GatewayRequestDto.builder()
        .token(token)
        .build(), headers);

    ResponseEntity<GatewayResponseDto> response = restTemplate.postForEntity(END_POINT, request , GatewayResponseDto.class);
    return response.getBody() != null && response.getBody().isSuccess();
  }
}
