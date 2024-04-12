package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.RoleListResponse;
import pt.up.fe.cosn.gateway.advices.responses.RoleResponse;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.entities.Role;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.UserRoleChangeRequest;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.util.Optional;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("/role/{roleId}")
    public ResponseEntity<Object> getRoleById(@PathVariable(value="roleId") Long id) {
        if(id == null)
            return ResponseFactory.bad(new SimpleResponse(false, "Required fields are empty."));

        Optional<Role> roleOptional = roleService.getRoleById(id);

        if(roleOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "Role does not exist."));

        return ResponseFactory.ok(new RoleResponse(true, "Role retrieval successful.", roleOptional.get()));
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getAllRoles(){
        return ResponseFactory.ok(new RoleListResponse(true, "Full role retrieval successful.", roleService.getAllRoles()));
    }

    @PostMapping("/changeUserRole")
    public ResponseEntity<Object> changeUserRole(@RequestHeader("AuthToken") String authorization, @RequestBody UserRoleChangeRequest request){

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(!userOptional.get().getRole().getName().equalsIgnoreCase(roleService.getAdministratorRole().get().getName()))
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        if(request == null || request.getEmail() == null || request.getRoleId() == null)
            return ResponseFactory.bad(new SimpleResponse(false, "Required fields are empty."));

        Boolean result = roleService.changeUserRole(request.getEmail(), request.getRoleId());

        if(!result){
            return ResponseFactory.expectationFailed(new SimpleResponse(false, "An unexpected error has occurred."));
        }

        return ResponseFactory.ok(new SimpleResponse(true, "User role change was successful."));
    }
}
