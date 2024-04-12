package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.VisualizationRequest;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class VisualizationController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private Boolean hasPermission(Optional<User> user, RoleService role){
        //TODO Add role authentication filter
        return true;
    }

    @PostMapping("/runVisualization")
    public ResponseEntity<Object> runVisualization(@RequestHeader("AuthToken") String authorization, @RequestBody VisualizationRequest request) throws IOException {
        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        String URI_VISUALIZATION = "https://61d8d203e6744d0017ba8cc5.mockapi.io/Visualization/{id}";

        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", request.getId());

        String result = restTemplate.getForObject(URI_VISUALIZATION, String.class, params);

        return ResponseFactory.ok(result);
    }
}
