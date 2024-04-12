package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class RealSystemController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private static String monitoringURL = "http://algorithmmicroservice.herokuapp.com";

    private Boolean hasPermission(Optional<User> user, RoleService role){
        return true;
    }

    @GetMapping("/getSensorsValues")
    @ResponseBody
    public ResponseEntity<Object> getAllValues(@RequestHeader("AuthToken") String authorization, @RequestParam("sensorsIds") String sensorsIds,
                                                  @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        String getSystemHealthURL = monitoringURL +"/health";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin role allowed."));

        String response = restTemplate.getForObject(getSystemHealthURL, String.class);

        /*Object responseObject = response.getBody();
        //Get response status code
        int statusCodeValue = response.getStatusCodeValue();*/

        return ResponseFactory.ok("Under Construction");
    }
}
