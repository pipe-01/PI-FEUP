package cosn.group2.realsystem.dbg;

import cosn.group2.realsystem.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// simulate the 'Gateway' microservice
@RestController
@RequestMapping(path = "/gateway")
class GatewayController {

    // Single item
    @PostMapping("/getUserFromToken")
    User getUserFromToken(@RequestBody Object token) {

        // just generate a random one
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.username = "username";
        user.role = "ServiceProvider";

        return user;
    }
}
