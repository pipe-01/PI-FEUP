package pt.up.fe.cosn.gateway.services;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.advices.exceptions.NoCredentialsMatchException;
import pt.up.fe.cosn.gateway.advices.exceptions.UserAlreadyExistsException;
import pt.up.fe.cosn.gateway.repositories.RoleRepository;
import pt.up.fe.cosn.gateway.repositories.UserRepository;
import pt.up.fe.cosn.gateway.utils.Utils;

import javax.annotation.PostConstruct;

@Service  
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Boolean registerUser(User user){
        String email = user.getEmail();
        Boolean exists = userExists(email);
        if(exists)
            throw new UserAlreadyExistsException(email);

        userRepository.save(user);
        return true;
    }

    public Boolean userExists(String email){
        if(email == null)
            return false;

        return userRepository.findUserByEmail(email).isPresent();
    }

    public Optional<User> loginUser(User user) {
        Optional<User> userOptional= userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if(userOptional.isEmpty()){
            throw new NoCredentialsMatchException();
        }

        User userRefreshed = userOptional.get();
        userRefreshed.refreshLastLogin();
        userRepository.save(userRefreshed);

        return userOptional;
    }

    public String createJwtForUser(User user){
        return Utils.createJWT(user.getId().toString(), "GATEWAY", user.getEmail(), 28800000);
    }

    public Optional<User> getUserFromToken(String token) {
        Claims claims = Utils.decodeJWT(token);
        return userRepository.findUserByEmail(claims.getSubject());
    }

    @PostConstruct
    public void fillBasicUsers() {
        userRepository.save(new User("admin@gateway.feup", "adminPass", roleRepository.findById(1L).get(), false));
        userRepository.save(new User("mod@gateway.feup", "modPass", roleRepository.findById(2L).get(), false));
        userRepository.save(new User("user@gateway.feup", "userPass", roleRepository.findById(3L).get(), false));
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
}