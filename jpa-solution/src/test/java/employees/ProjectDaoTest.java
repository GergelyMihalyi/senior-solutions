package employees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDaoTest {

    private ProjectDao projectDao;

    private EmployeeDao employeeDao;

    @BeforeEach
    public void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
        projectDao = new ProjectDao(entityManagerFactory);
        employeeDao = new EmployeeDao(entityManagerFactory);
    }

    @Test
    public void testSaveProject(){
        Employee john = new Employee("John Doe");
        Employee jane = new Employee("Jane Doe");
        Employee jack = new Employee("Jack Doe");
        employeeDao.save(john);
        employeeDao.save(jane);
        employeeDao.save(jack);

        Project java = new Project("Java");
        Project dotNet = new Project("dotNet");
        Project python = new Project("Python");

        java.addEmployee(john);
        java.addEmployee(jane);

        python.addEmployee(john);
        python.addEmployee(jack);

        dotNet.addEmployee(jack);
        dotNet.addEmployee(jane);

        projectDao.saveProject(java);
        projectDao.saveProject(dotNet);
        projectDao.saveProject(python);

        Project project = projectDao.findProjectByName("Java");
        assertEquals(Set.of("John Doe","Jane Doe"),
                project.getEmployees().stream().map(Employee::getName).collect(Collectors.toSet()));

    }

}