package cosn.group2.Controller;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Task;
import cosn.group2.Service.ExperienceService;
import cosn.group2.Service.TaskService;
import cosn.group2.Service.VerificationService;
import cosn.group2.dto.MAS;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static cosn.group2.Service.VerificationService.AUTHORIZATION_TOKEN;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RestController
@Tag(name = "Task", description = "API for Artificial System Information")
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/health")
    public String health() {
        return "Up and running!";
    }

    @Operation(summary = "Retrieves all tasks by Experience", description = "Retrieves all tasks by Experience", tags = {"task"})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class))))})
    @GetMapping("/task/{id}")
    public List<String> getAllTasks(@PathVariable int id,
                                    @RequestHeader(AUTHORIZATION_TOKEN) Optional<String> token) {

        verification(token);

        return service.getAllTasks(id);
    }

    @Operation(summary = "Retrieves Experiences Content", description = "Retrieves Experiences Content", tags = {"task"})
    @ApiResponses({@ApiResponse(
            responseCode = "200", description = "successful operation",
            content = @Content(array =
            @ArraySchema(schema = @Schema(implementation = Experience.class))))})
    @GetMapping("/experience/{id}")
    public ResponseEntity getExperienceContent(@PathVariable int id,
                                               @RequestHeader(AUTHORIZATION_TOKEN) Optional<String> token) {

        verification(token);

        String filePath = service.getExperienceContent(id);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(Files.size(Paths.get(filePath)));
            return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
        } catch (FileNotFoundException ex){
            System.out.println("Not found");
            return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        }
        catch (IOException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //return service.getExperienceContent(id);
    }

    @PostMapping("/task/execute")
    public ResponseEntity executeTask(@RequestBody MAS mas,
                                      @RequestHeader(AUTHORIZATION_TOKEN) Optional<String> token) {

        verification(token);

        System.out.println("MAS "+ mas.toString());
        service.executeTask(mas.getExperienceID(), mas.algorithm, mas.getDomainModel(), mas.getPath());
        return new ResponseEntity(HttpStatus.OK);
    }

    private void verification(Optional<String> token) {
        verification(token.orElse(null));
    }

    private void verification(String token) {

        try {
            if (!verificationService.isUser(token))
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
