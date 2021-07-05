package musicstore;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class MusicStoreControllerRestIT {

    @Autowired
    TestRestTemplate template;


    @BeforeEach
    void init(){
        template.delete("/api/instruments");
    }


    @Test
    void testAddNewInstruments() {
        InstrumentDto result =
                template.postForObject("/api/instruments",
                        new CreateInstrumentCommand("Fender", InstrumentType.ELECTRIC_GUITAR, 2000),
                        InstrumentDto.class);

        assertEquals("Fender", result.getBrand());
        assertEquals(InstrumentType.ELECTRIC_GUITAR, result.getType());
        assertEquals(2000, result.getPrice());
        assertEquals(LocalDate.now(), result.getReleaseDate());

    }

    @Test
    void testGetInstruments() {


        template.postForObject("/api/instruments",
                new CreateInstrumentCommand("Fender", InstrumentType.ELECTRIC_GUITAR, 2000),
                InstrumentDto.class);

        template.postForObject("/api/instruments",
                new CreateInstrumentCommand("Gibson", InstrumentType.ELECTRIC_GUITAR, 2000),
                InstrumentDto.class);


        List<InstrumentDto> result = template.exchange(
                "/api/instruments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<InstrumentDto>>() {
                }).getBody();

        assertThat(result)
                .extracting(InstrumentDto::getBrand)
                .containsExactly("Fender","Gibson");

    }

    @Test
    void testUpdateInstrumentPrice(){

        template.postForObject("/api/instruments",
                new CreateInstrumentCommand("Fender", InstrumentType.ELECTRIC_GUITAR, 2000),
                InstrumentDto.class);


        template.put("/api/instruments/1", new UpdateInstrumentCommand(1000));

        InstrumentDto result = template.getForObject("/api/instruments/1",InstrumentDto.class);

        assertEquals(1000,result.getPrice());

    }



    @Test
    void testInstrumentNotFound(){
        Problem result = template.getForObject("/api/instruments/1", Problem.class);

        assertEquals(URI.create("instruments/not-found"),result.getType());
        assertEquals(Status.NOT_FOUND, result.getStatus());
    }

    @Test
    void testCreateWithInvalidName(){
        Problem result = template.postForObject("/api/instruments",
                new CreateInstrumentCommand("", InstrumentType.ELECTRIC_GUITAR, 2000),
                Problem.class);
        assertEquals(Status.BAD_REQUEST, result.getStatus());


    }

    @Test
    void testCreateWithInvalidPrice(){
        Problem result = template.postForObject("/api/instruments",
                new CreateInstrumentCommand("Fender", InstrumentType.ELECTRIC_GUITAR, -2000),
                Problem.class);
        assertEquals(Status.BAD_REQUEST, result.getStatus());

    }

}