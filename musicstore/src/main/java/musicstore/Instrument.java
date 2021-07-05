package musicstore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {
    private Long id;
    private String brand;
    private int price;
    private InstrumentType type;
    private LocalDate releaseDate;

}
