package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.util.Optional;

@RestController
public class DomainModelController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private static String domainModelURL = "https://domainservice20220116221152.azurewebsites.net";

    private String mockDomainModel = "Domain Model";

    private Boolean hasPermission(Optional<User> user, RoleService role){
        return user.get().getRole().getValue().equals(role.getResearcherRole().get().getValue()) ||
                user.get().getRole().getValue().equals(role.getAdministratorRole().get().getValue());
    }

    @GetMapping("/getAllDomainModels")
    @ResponseBody
    public ResponseEntity<Object> getAllDomainModels(@RequestHeader("AuthToken") String authorization) {
        String getSystemHealthURL = domainModelURL +"/api/DomainModel";

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

        ResponseEntity<String> response = restTemplate.exchange(getSystemHealthURL, HttpMethod.GET, requestEntity, String.class);

        return ResponseFactory.ok(response.getBody());
    }
}
