package pt.up.fe.cosn.gateway.advices;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;

@ControllerAdvice
class MalformedJwtExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SimpleResponse malformedJwtException(MalformedJwtException ex) {
      return new SimpleResponse(false, "Jwt token given was malformed.");
    }
}