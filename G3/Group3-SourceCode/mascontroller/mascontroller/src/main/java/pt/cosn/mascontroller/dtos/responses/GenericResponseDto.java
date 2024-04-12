package pt.cosn.mascontroller.dtos.responses;

import lombok.Data;

@Data
public class GenericResponseDto {

  private boolean error;
  private String message;

  public void setBadState(String message) {
    this.message = message;
    this.error = true;
  }

  public void setGoodState(String message) {
    this.message = message;
    this.error = false;
  }
}
