package pt.cosn.mascontroller.controllers;

import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.cosn.mascontroller.dtos.requests.WorkflowExecutionListRequestDto;
import pt.cosn.mascontroller.dtos.requests.WorkflowExecutionRequestDto;
import pt.cosn.mascontroller.dtos.requests.WorkflowExecutionStatusRequestDto;
import pt.cosn.mascontroller.dtos.responses.WorkflowExecutionListResponseDto;
import pt.cosn.mascontroller.dtos.responses.WorkflowExecutionResponseDto;
import pt.cosn.mascontroller.services.ApiGatewayService;
import pt.cosn.mascontroller.services.WorkflowExecutionService;

@RestController
@RequestMapping("/workflow")
public class WorkflowExecutionController {

  private final WorkflowExecutionService workflowExecutionService;

  private final ApiGatewayService apiGatewayService;

  public WorkflowExecutionController(WorkflowExecutionService workflowExecutionService,
      ApiGatewayService apiGatewayService) {
    this.workflowExecutionService = workflowExecutionService;
    this.apiGatewayService = apiGatewayService;
  }

  @GetMapping("/")
  public String welcome() {
    return "Welcome to the MAS Controller.";
  }

  @PostMapping("/findAll")
  public ResponseEntity<WorkflowExecutionListResponseDto> findAll(
      @Valid @RequestBody WorkflowExecutionListRequestDto requestDto, BindingResult result) {
    if (result.hasErrors()) {
      String message = (Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    return new ResponseEntity<>(workflowExecutionService.findByUserId(requestDto.getUserId()),
        HttpStatus.OK);
  }

  @PostMapping("/findById")
  public ResponseEntity<WorkflowExecutionResponseDto> statusById(
      @Valid @RequestBody WorkflowExecutionStatusRequestDto requestDto, BindingResult result) {
    if (result.hasErrors()) {
      String message = (Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    return new ResponseEntity<>(workflowExecutionService.findById(requestDto.getWorkflowExecutionId()),
        HttpStatus.OK);
  }

  @PostMapping("/run")
  public ResponseEntity<WorkflowExecutionResponseDto> run(@RequestHeader Map<String, String> headers,
      @Valid @RequestBody WorkflowExecutionRequestDto executionRequestDto, BindingResult result) {
    String token = headers.get("authtoken");
    if (StringUtils.isEmpty(token) || !apiGatewayService.isTokenValid(token)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing token or it is invalid!");
    }

    if (result.hasErrors()) {
      String message = (Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    return new ResponseEntity<>(workflowExecutionService.run(executionRequestDto, token), HttpStatus.OK);
  }

}
