package pt.up.fe.cosn.gateway.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String email) {
    super("User with email " + email + " wasn't found.");
  }
}