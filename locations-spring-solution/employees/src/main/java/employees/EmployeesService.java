package employees;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeesService {

    private ModelMapper modelMapper;


    private EmployeesDao employeeDao;


    public List<EmployeeDto> listEmployees(Optional<String> prefix) {
       return employeeDao.findAll().stream()
               .map(e->modelMapper.map(e, EmployeeDto.class))
               .collect(Collectors.toList());
    }

    public EmployeeDto findEmployeeById(long id) {
        return modelMapper.map(employeeDao.findById(id),EmployeeDto.class);
    }

    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        Employee employee = new Employee( command.getName());
        employeeDao.createEmployee(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public EmployeeDto updateEmployee(long id, UpdateEmployeeCommand command) {
        Employee employee = new Employee(id, command.getName());
        employeeDao.updateEmployee(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public void deleteEmployee(long id) {
        employeeDao.deleteById(id);
    }

    public void deleteAllEmployees(){
        employeeDao.deleteAll();
    }
}
