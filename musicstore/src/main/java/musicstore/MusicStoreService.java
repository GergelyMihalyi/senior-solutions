package musicstore;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MusicStoreService {

    private ModelMapper modelMapper;
    private AtomicLong idGenerator = new AtomicLong();
    private List<Instrument> instruments = new ArrayList<>();

    public MusicStoreService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<InstrumentDto> listInstruments(Optional<String> brand, Optional<Integer> price) {
        Type targetListType = new TypeToken<List<InstrumentDto>>(){}.getType();
        List<Instrument> filtered = instruments.stream()
                .filter( i -> brand.isEmpty() || i.getBrand().equalsIgnoreCase(brand.get()))
                .filter(i -> price.isEmpty() || i.getPrice() == price.get())
                .collect(Collectors.toList());
        return modelMapper.map(filtered,targetListType);
    }

    public InstrumentDto createInstrument(CreateInstrumentCommand command) {
        Instrument instrument = new Instrument(idGenerator.incrementAndGet(), command.getBrand(),command.getPrice(),command.getType(), LocalDate.now());
        instruments.add(instrument);
        return modelMapper.map(instrument, InstrumentDto.class);
    }

    public InstrumentDto findInstrumentById(long id) {
        return modelMapper.map(instruments.stream().filter(e -> e.getId() == id).findAny().orElseThrow(() -> new IllegalArgumentException("Employees not found: " + id)),InstrumentDto.class);
    }

    public InstrumentDto updateInstrument(long id, UpdateInstrumentCommand command) {
        Instrument instrument = instruments.stream().filter(e-> e.getId() == id).findFirst().orElseThrow(()->new IllegalArgumentException("Employee not found: " + id));

        if(instrument.getPrice() != command.getPrice()){
            instrument.setPrice(command.getPrice());
            instrument.setReleaseDate(LocalDate.now());
        }

        return modelMapper.map(instrument, InstrumentDto.class);
    }

    public void deleteInstrument(long id) {
        Instrument instrument = instruments.stream().filter(e-> e.getId() == id).findFirst().orElseThrow(()->new IllegalArgumentException("Employee not found: " + id));
        instruments.remove(instrument);
    }

    public void deleteAll() {
        idGenerator = new AtomicLong();
        instruments.clear();

    }
}
