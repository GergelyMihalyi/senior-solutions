package musicstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instruments")
public class MusicStoreController {
    private MusicStoreService service;

    public MusicStoreController(MusicStoreService service) {
        this.service = service;
    }

    @GetMapping
    public List<InstrumentDto> listInstruments(@RequestParam Optional<String> brand, @RequestParam Optional<Integer> price) {
        return service.listInstruments(brand, price);
    }

    @GetMapping("/{id}")
    public InstrumentDto findInstrumentById(@PathVariable("id") long id) {
        return service.findInstrumentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstrumentDto createInstrument(@Valid @RequestBody CreateInstrumentCommand command) {
        return service.createInstrument(command);
    }

    @PutMapping("/{id}")
    public InstrumentDto updateInstrument(@PathVariable("id") long id, @RequestBody UpdateInstrumentCommand command) {
        return service.updateInstrument(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstrument(@PathVariable("id") long id){
        service.deleteInstrument(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(){
        service.deleteAll();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException iae) {
        Problem problem = Problem.builder()
                .withType(URI.create("instruments/not-found"))
                .withTitle("not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(iae.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
