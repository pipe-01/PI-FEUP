package pt.up.fe.cosn.gateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.up.fe.cosn.gateway.entities.Role;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.repositories.RoleRepository;
import pt.up.fe.cosn.gateway.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service  
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Role> getRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Boolean changeUserRole(String email, Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if(roleOptional.isEmpty())
            return false;

        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if(userOptional.isEmpty())
            return false;

        userOptional.get().setRole(roleOptional.get());

        userRepository.save(userOptional.get());

        return true;
    }

    public Optional<Role> getUserRole(){
        return roleRepository.findRoleByName("User");
    }

    public Optional<Role> getAppDeveloperRole(){
        return roleRepository.findRoleByName("AppDeveloper");
    }

    public Optional<Role> getServiceProviderRole(){
        return roleRepository.findRoleByName("ServiceProvider");
    }

    public Optional<Role> getResearcherRole(){
        return roleRepository.findRoleByName("Researcher");
    }

    public Optional<Role> getModeratorRole(){
        return roleRepository.findRoleByName("Moderator");
    }

    public Optional<Role> getAdministratorRole(){
        return roleRepository.findRoleByName("Administrator");
    }

    @PostConstruct
    public void fillBasicRoles() {
        roleRepository.save(new Role("Administrator", 0L));
        roleRepository.save(new Role("Moderator", 100L));
        roleRepository.save(new Role("Researcher", 497L));
        roleRepository.save(new Role("ServiceProvider", 498L));
        roleRepository.save(new Role("AppDeveloper", 499L));
        roleRepository.save(new Role("User", 500L));
    }
}