package pt.cosn.mascontroller.services;


import pt.cosn.mascontroller.dtos.requests.ArtificialSystemRequestDto;

public interface ArtificialSystemService {

  boolean execute(ArtificialSystemRequestDto requestDto);

}
