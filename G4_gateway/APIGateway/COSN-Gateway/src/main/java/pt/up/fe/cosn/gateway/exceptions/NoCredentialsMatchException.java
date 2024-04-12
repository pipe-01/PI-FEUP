package pt.up.fe.cosn.gateway.exceptions;

public class NoCredentialsMatchException extends RuntimeException {

  public NoCredentialsMatchException() {
    super("Email / Password combination is wrong.");
  }
}