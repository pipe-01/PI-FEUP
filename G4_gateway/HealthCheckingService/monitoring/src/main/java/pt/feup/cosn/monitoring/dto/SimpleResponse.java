package pt.feup.cosn.monitoring.dto;

public class SimpleResponse {
    private boolean sucess;
    private String message;

    public SimpleResponse(){
        sucess = false;
        message = "An error has occurred.";
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAsError(){
        sucess = false;
        message = "An error has occurred.";
    }

    public void setAsSuccess(){
        sucess = true;
        message = "Operation was successful.";
    }

    public void setAsSuccess(String message){
        sucess = true;
        this.message = message;
    }

    public void setAsError(String message){
        sucess = false;
        this.message = message;
    }

    public static SimpleResponse createNew(){
        return new SimpleResponse();
    }
}
