package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;
import java.util.Optional;
import java.util.Random;

@RestController
public class CityController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private static String sensorsURL = "https://cosn-city-service.herokuapp.com";

    private Boolean hasPermission(Optional<User> user, RoleService role){
        return user.get().getRole().getValue().equals(role.getResearcherRole().get().getValue()) ||
                user.get().getRole().getValue().equals(role.getAdministratorRole().get().getValue());
    }

    @GetMapping("/getAllCities")
    @ResponseBody
    public ResponseEntity<Object> getAllCities(@RequestHeader("AuthToken") String authorization) {
        String getStaticSensors = sensorsURL +"/city";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authorization);
        headers.add("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(getStaticSensors, HttpMethod.GET, requestEntity, String.class);

        return ResponseFactory.ok(response);
    }

    @GetMapping("/getSensorByType/static")
    @ResponseBody
    public ResponseEntity<Object> getSensorByType(@RequestHeader("AuthToken") String authorization, @RequestParam("cityId") String cityId, @RequestParam("type") String type,
                                                  @RequestParam("radius") String radius) {
        String getStaticSensors = sensorsURL + "/sensor/static/radius?city_id="+cityId+"&type="+type+"&radius="+radius;

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authorization);
        headers.add("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(getStaticSensors, HttpMethod.GET, requestEntity, String.class);

        return ResponseFactory.ok(response);
    }

    @GetMapping("/getSensorValues/static")
    @ResponseBody
    public ResponseEntity<Object> getSensorValues(@RequestHeader("AuthToken") String authorization, @RequestParam("cityId") String cityId, @RequestParam("radius") String radius) {
        String getStaticSensors = sensorsURL +"/sensor/static/radius?city_id="+cityId+"&radius="+radius;

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authorization);
        headers.add("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(getStaticSensors, HttpMethod.GET, requestEntity, String.class);

        return ResponseFactory.ok(response);
    }

    @GetMapping("/getSensorCity/mobile")
    @ResponseBody
    public ResponseEntity<Object> getSensorCityMobile(@RequestHeader("AuthToken") String authorization, @RequestParam("cityId") String cityId) {
        String getStaticSensors = sensorsURL +"/sensor/mobile/city?city_id="+cityId;

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authorization);
        headers.add("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(getStaticSensors, HttpMethod.GET, requestEntity, String.class);

        return ResponseFactory.ok(response);
    }


    @GetMapping("/getSensorType/mobile")
    @ResponseBody
    public ResponseEntity<Object> getSensorTypeMobile(@RequestHeader("AuthToken") String authorization, @RequestParam("cityId") String cityId, @RequestParam("type") String type) {
        String getStaticSensors = sensorsURL +"/sensor/mobile/city_type?city_id="+cityId+"&type="+type;

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authorization);
        headers.add("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(getStaticSensors, HttpMethod.GET, requestEntity, String.class);

        return ResponseFactory.ok(response);
    }
}
