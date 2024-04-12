package pt.up.fe.cosn.gateway.advices;

import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;

@ControllerAdvice
class SignatureExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SimpleResponse signatureException(SignatureException ex) {
      return new SimpleResponse(false, ex.getMessage());
    }
}