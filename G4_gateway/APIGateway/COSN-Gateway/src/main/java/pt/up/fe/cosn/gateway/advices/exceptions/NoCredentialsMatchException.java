package pt.up.fe.cosn.gateway.advices.exceptions;

public class NoCredentialsMatchException extends RuntimeException {

  public NoCredentialsMatchException() {
    super("Email / Password combination is wrong.");
  }
}