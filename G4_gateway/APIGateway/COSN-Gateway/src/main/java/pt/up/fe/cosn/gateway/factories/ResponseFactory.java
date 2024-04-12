package pt.up.fe.cosn.gateway.factories;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {
    public static ResponseEntity<Object> ok(Object body){
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public static ResponseEntity<Object> unauthorized(Object body){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    public static ResponseEntity<Object> bad(Object body){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static ResponseEntity<Object> expectationFailed(Object body){
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(body);
    }
}
