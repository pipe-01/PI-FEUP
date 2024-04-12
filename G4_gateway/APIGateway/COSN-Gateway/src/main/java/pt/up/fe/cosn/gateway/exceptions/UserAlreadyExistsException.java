package pt.up.fe.cosn.gateway.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String email) {
    super("The email address " + email + " is already registered.");
  }
}