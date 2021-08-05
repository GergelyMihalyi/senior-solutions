package employees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoTest {

    private EmployeeDao employeeDao;

    @BeforeEach
    public void init() {
        /*MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost/employees");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();
        */
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
        employeeDao = new EmployeeDao(entityManagerFactory);

    }

    @Test
    public void testSaveThenFindById() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);

        long id = employee.getId();

        Employee another = employeeDao.findById(id);
        assertEquals("John Doe", another.getName());

    }

    @Test
    public void testSaveThenListAll() {
        employeeDao.save(new Employee("John Doe"));
        employeeDao.save(new Employee("Jane Doe"));

        List<Employee> employees = employeeDao.listAll();
        assertEquals(List.of("Jane Doe", "John Doe"), employees.stream().map(Employee::getName).collect(Collectors.toList()));
    }

    @Test
    public void testChangeName() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);

        long id = employee.getId();
        employeeDao.changeName(id, "Jack Doe");

        Employee another = employeeDao.findById(id);
        assertEquals("Jack Doe", another.getName());
    }

    @Test
    public void testDelete() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);
        long id = employee.getId();
        employeeDao.delete(id);
        List<Employee> employees = employeeDao.listAll();
        assertTrue(employees.isEmpty());
    }

    @Test
    public void testEmployeeWithAttributes() {
        Employee employee = new Employee("John Doe", Employee.EmployeeType.HALF_TIME, LocalDate.of(2000, 01, 01));
        employeeDao.save(employee);

        System.out.println(employee.getDateOfBirth());

        Employee employeeFirst = employeeDao.listAll().get(0);
        System.out.println(employeeFirst.getDateOfBirth());
        assertEquals(LocalDate.of(2000, 1, 1), employeeFirst.getDateOfBirth());
    }

    @Test
    public void testSaveEmployeeChangeState() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);
        employee.setName("Jack Doe");
        Employee modifiedEmployee = employeeDao.findById(employee.getId());
        assertEquals("John Doe", modifiedEmployee.getName());
        assertFalse(employee == modifiedEmployee);
    }

    @Test
    public void testMerge() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);
        employee.setName("Jack Doe");
        employeeDao.updateEmployee(employee);
        Employee modifiedEmployee = employeeDao.findById(employee.getId());
        assertEquals("Jack Doe", modifiedEmployee.getName());
    }

    @Test
    public void testFlush() {
        for (int i = 0; i < 10; i++) {
            employeeDao.save(new Employee("John Doe" + i));
        }
        employeeDao.updateEmployeeNames();
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);
        employee.setName("Jack Doe");
        employeeDao.updateEmployee(employee);
        Employee modifiedEmployee = employeeDao.findById(employee.getId());
        assertEquals("Jack Doe", modifiedEmployee.getName());
    }

    @Test
    public void testNicknames() {
        Employee employee = new Employee("John Doe");
        employee.setNicknames(Set.of("Johny", "J"));
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithNicknames(employee.getId());
        assertEquals(Set.of("J", "Johny"), anotherEmployee.getNicknames());
    }

    @Test
    public void testVacations() {
        Employee employee = new Employee("John Doe");
        employee.setVacationBookings(Set.of(
                new VacationEntry(LocalDate.of(2020,1,1),4)
                ,new VacationEntry(LocalDate.of(2020,2,10),4)));
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithVacations(employee.getId());
        System.out.println(anotherEmployee.getVacationBookings());
        assertEquals(2,anotherEmployee.getVacationBookings().size());
    }

    /*@Test
    public void testPhoneNumber() {
        Employee employee = new Employee("John Doe");
        employee.setPhoneNumbers(Map.of("home","1234","work","12345"));
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());

        assertEquals("1234", anotherEmployee.getPhoneNumbers().get("home"));
        assertEquals("12345", anotherEmployee.getPhoneNumbers().get("work"));
    }*/

    @Test
    public void testPhoneNumber(){
        PhoneNumber phoneNumberHome = new PhoneNumber("home", "1234");
        PhoneNumber phoneNumberWork = new PhoneNumber("work", "4324");
        Employee employee = new Employee("John Doe");
        employee.addPhoneNumber(phoneNumberHome);
        employee.addPhoneNumber(phoneNumberWork);
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        assertEquals(2,anotherEmployee.getPhoneNumbers().size());
    }

    @Test
    public void testAddPhoneNumber(){
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);
        employeeDao.addPhoneNumber(employee.getId(), new PhoneNumber("home", "1234"));
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        assertEquals(1,anotherEmployee.getPhoneNumbers().size());
    }

    @Test
    public void testRemove(){
        Employee employee = new Employee("John Doe");

        employee.addPhoneNumber(new PhoneNumber("home", "1234"));
        employee.addPhoneNumber(new PhoneNumber("work", "1234"));
        employeeDao.save(employee);
        employeeDao.delete(employee.getId());
    }
/*
    @Test
    public void testEmployeeWithAddress(){
        Employee employee = new Employee("John Doe");
        Address address = new Address("H-3213","Budapest","Teszt utca");
        employee.setAddress(address);
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findById(employee.getId());
        assertEquals("H-3213", anotherEmployee.getAddress().getZip());
    }
    */
    @Test
    public void testEmployeeWithAddressAttributes(){
        Employee employee = new Employee("John Doe");
        employee.setZip("H-3213");
        employee.setCity("Budapest");
        employee.setLine1("Teszt utca");
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findById(employee.getId());
        assertEquals("H-3213",anotherEmployee.getZip());
    }
}