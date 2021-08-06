package employees;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeesService {

    private ModelMapper modelMapper;


    private EmployeesRepository repository;


    public List<EmployeeDto> listEmployees(Optional<String> prefix) {
       return repository.findAll().stream()
               .map(e->modelMapper.map(e, EmployeeDto.class))
               .collect(Collectors.toList());
    }

    public EmployeeDto findEmployeeById(long id) {
        return modelMapper.map(repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("employee not found")),
                EmployeeDto.class);
    }

    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        Employee employee = new Employee( command.getName());
        repository.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Transactional
    public EmployeeDto updateEmployee(long id, UpdateEmployeeCommand command) {
        Employee employee = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("employee not found"));
        employee.setName(command.getName());
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public void deleteEmployee(long id) {
        repository.deleteById(id);
    }

    public void deleteAllEmployees(){
        repository.deleteAll();
    }
}
