package pt.up.fe.cosn.gateway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pt.up.fe.cosn.gateway.advices.responses.algorithmsResponses.AlgorithmResponse;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.advices.responses.algorithmsResponses.WorkflowResponse;
import pt.up.fe.cosn.gateway.entities.Algorithm;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AlgorithmsController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private final String[] mockAlgorithmsList = {"K-means", "DBSCAN", "Neural Network", "LightGBM", "Decision Tree"};
    private final List<Algorithm> Algorithms = new ArrayList<>();

    private static String algorithmServiceURL = "http://algorithmmicroservice.herokuapp.com";

    private Boolean hasPermission(Optional<User> user, RoleService role){
        return user.get().getRole().getValue().equals(role.getResearcherRole().get().getValue()) ||
                user.get().getRole().getValue().equals(role.getAdministratorRole().get().getValue());
    }

    @GetMapping("/getAlgorithms")
    @ResponseBody
    public ResponseEntity<Object> getAlgorithms(@RequestHeader("AuthToken") String authorization) {
        String getAlgorithmsUrlPath = algorithmServiceURL +"/algorithms";
        String tempURL = "https://61d8d203e6744d0017ba8cc5.mockapi.io/Algorithms";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        ResponseEntity<AlgorithmResponse[]> response =
                restTemplate.getForEntity(
                        tempURL,
                        AlgorithmResponse[].class);
        AlgorithmResponse[] algorithms = response.getBody();

        return ResponseFactory.ok(algorithms);
    }

    @GetMapping("/getAlgorithmsName")
    @ResponseBody
    public ResponseEntity<Object> getAlgorithmsNames(@RequestHeader("AuthToken") String authorization) {
        String getAlgorithmsUrlPath = algorithmServiceURL +"/algorithms";
        String tempURL = "https://61d8d203e6744d0017ba8cc5.mockapi.io/Algorithms";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        ResponseEntity<AlgorithmResponse[]> response =
                restTemplate.getForEntity(
                        tempURL,
                        AlgorithmResponse[].class);
        AlgorithmResponse[] algorithms = response.getBody();

        //Return only the map or anything else
        ObjectMapper mapper = new ObjectMapper();

        return ResponseFactory.ok(Arrays.stream(algorithms)
                .map(object -> mapper.convertValue(object, AlgorithmResponse.class))
                .map(AlgorithmResponse::getName)
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAlgorithmById")
    @ResponseBody
    public ResponseEntity<Object> getAlgorithmById(@RequestHeader("AuthToken") String authorization, @RequestHeader("Id") String id) {
        String getAlgorithmsUrlPath = algorithmServiceURL +"/algorithms/"+id;
        String tempURL = "https://61d8d203e6744d0017ba8cc5.mockapi.io/Algorithms/"+id;

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        ResponseEntity<AlgorithmResponse> response =
                restTemplate.getForEntity(
                        tempURL,
                        AlgorithmResponse.class);

        AlgorithmResponse algorithm = response.getBody();
        //Get response status code
        int statusCodeValue = response.getStatusCodeValue();

        return ResponseFactory.ok(algorithm);
    }

    @GetMapping("/getWorkflows")
    @ResponseBody
    public ResponseEntity<Object> getWorkflows(@RequestHeader("AuthToken") String authorization) {
        String getAlgorithmsUrlPath = algorithmServiceURL +"/workflows/";
        String tempURL = "https://61d8d203e6744d0017ba8cc5.mockapi.io/Workflows";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!hasPermission(userOptional, roleService))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation. Only Admin or Researcher role allowed."));

        ResponseEntity<WorkflowResponse[]> response =
                restTemplate.getForEntity(
                        tempURL,
                        WorkflowResponse[].class);
        WorkflowResponse[] workflows = response.getBody();

        return ResponseFactory.ok(workflows);
    }

}
