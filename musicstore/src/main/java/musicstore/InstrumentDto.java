package musicstore;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InstrumentDto {
    private Long id;
    private String brand;
    private int price;
    private InstrumentType type;
    private LocalDate releaseDate;
}
