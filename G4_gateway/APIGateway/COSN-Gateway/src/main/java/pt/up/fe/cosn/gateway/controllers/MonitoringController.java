package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MonitoringController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private static String monitoringURL = "http://algorithmmicroservice.herokuapp.com";

    private Boolean hasPermission(Optional<User> user, RoleService role){
        return user.get().getRole().getValue().equals(role.getResearcherRole().get().getValue());
    }

    @GetMapping("/getSystemHealth")
    @ResponseBody
    public ResponseEntity<Object> getSystemHealth(@RequestHeader("AuthToken") String authorization) {
        String getSystemHealthURL = monitoringURL +"/health";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin role allowed."));

        String response =
                restTemplate.getForObject(
                        getSystemHealthURL,
                        String.class);

        /*Object responseObject = response.getBody();
        //Get response status code
        int statusCodeValue = response.getStatusCodeValue();*/

        return ResponseFactory.ok(response);
    }

    @GetMapping("/getAllSystemsLogs")
    @ResponseBody
    public ResponseEntity<Object> getAllSystemsLogs(@RequestHeader("AuthToken") String authorization) {
        String getSystemHealthURL = monitoringURL +"/health";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin role allowed."));

        String response =
                restTemplate.getForObject(
                        getSystemHealthURL,
                        String.class);

        /*Object responseObject = response.getBody();
        //Get response status code
        int statusCodeValue = response.getStatusCodeValue();*/

        return ResponseFactory.ok(response);
    }
}
