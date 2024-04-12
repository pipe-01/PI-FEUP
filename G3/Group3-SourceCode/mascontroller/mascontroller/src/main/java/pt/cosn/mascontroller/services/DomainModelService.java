package pt.cosn.mascontroller.services;

import pt.cosn.mascontroller.dtos.DomainModelDto;

public interface DomainModelService {

  DomainModelDto findById(String id, String token);

}
