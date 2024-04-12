package pt.up.fe.cosn.gateway.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.LoginResponse;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.advices.responses.UserFromTokenResponse;
import pt.up.fe.cosn.gateway.entities.Role;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.advices.exceptions.UserNotFoundException;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.UserFromTokenRequest;
import pt.up.fe.cosn.gateway.requests.UserLoginRequest;
import pt.up.fe.cosn.gateway.requests.UserRegisterRequest;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Hidden
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<Object> getHome(HttpServletResponse response) throws IOException {
        return ResponseFactory.ok("Use /swagger to access swagger page");
    }

    @Hidden
    @GetMapping("/swagger")
    @ResponseBody
    public void getSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @PostMapping ("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserLoginRequest request) {
        if(request == null || request.getEmail() == null || request.getPassword() == null)
            return ResponseFactory.bad(new SimpleResponse(false, "Required fields are empty."));

        Optional<User> userOptional = userService.loginUser(new User(request.getEmail(), request.getPassword(), false));
        String jwtToken = null;

        if(userOptional.isPresent()){
            User user = userOptional.get();
            jwtToken = userService.createJwtForUser(user);
        }

        if(jwtToken == null || jwtToken.isEmpty()){
            return ResponseFactory.expectationFailed(new SimpleResponse(false, "An unexpected error has occurred"));
        }

        return ResponseFactory.ok(new LoginResponse(true, "Login Successful", jwtToken));
    }

    @PostMapping ("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegisterRequest request) {
        if(request == null || request.getEmail() == null || request.getPassword() == null || request.getRoleId() == null)
            return ResponseFactory.bad(new SimpleResponse(false, "Required fields are empty."));

        Optional<Role> roleOptional = roleService.getRoleById(request.getRoleId());

        if(roleOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "Role does not exist."));

        Boolean result = userService.registerUser(new User(request.getEmail(), request.getPassword(), roleOptional.get(), false));

        if(!result){
            return ResponseFactory.expectationFailed(new SimpleResponse(false, "An unexpected error has occurred"));
        }

        return ResponseFactory.ok(new SimpleResponse(true, "Registration Successful"));
    }

    @PostMapping ("/getUserFromToken")
    public ResponseEntity<Object> getUserFromToken(@RequestBody UserFromTokenRequest request) {
        if(request == null || request.getToken() == null)
            return ResponseFactory.bad(new SimpleResponse(false, "Required fields are empty."));

        Optional<User> userOptional = userService.getUserFromToken(request.getToken());

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User related to token does not exist.");
        }

        return ResponseFactory.ok(new UserFromTokenResponse(true, "Login Successful", userOptional.get()));
    }
}
