package pt.up.fe.cosn.gateway.advices.responses;

public class GenericResponse extends SimpleResponse{
    public GenericResponse(Boolean success, String message) {
        super(success, message);
    }
}
