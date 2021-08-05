package employees;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;
import java.util.List;

public class EmployeeDao {

    private EntityManagerFactory entityManagerFactory;

    public EmployeeDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void save(Employee employee) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
        em.close();
    }

    public Employee findById(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.find(Employee.class, id);
        em.close();
        return employee;
    }

    public Employee findEmployeeByIdWithNicknames(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.createQuery("select e from Employee e join fetch e.nicknames where e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        em.close();
        return employee;
    }

    public List<Employee> listAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Employee> employees = em
                .createQuery("select e from Employee e order by e.name", Employee.class)
                .getResultList();
        em.close();
        return employees;
    }

    public void changeName(Long id, String name) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Employee employee = em.find(Employee.class, id);
        employee.setName(name);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Employee employee = em.getReference(Employee.class, id);
        em.remove(employee);
        em.getTransaction().commit();
        em.close();
    }

    public void updateEmployee(Employee employee) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.merge(employee);
        em.getTransaction().commit();
        em.close();
    }

    public List<Employee> updateEmployeeNames() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Employee> employees = em
                .createQuery("select e from Employee e order by e.name", Employee.class)
                .getResultList();
        em.getTransaction().begin();
        for (Employee employee : employees) {
            employee.setName(employee.getName() + " ***");
            em.flush();
            System.out.println("Modositva");
        }
        em.getTransaction().commit();
        em.close();
        return employees;
    }

    public Employee findEmployeeByIdWithVacations(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.createQuery("select e from Employee e join fetch e.vacationBookings where e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        em.close();
        return employee;
    }

    public Employee findEmployeeByIdWithPhoneNumbers(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.createQuery("select e from Employee e join fetch e.phoneNumbers where e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        em.close();
        return employee;
    }

    public void addPhoneNumber(long id,PhoneNumber phoneNumber) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        //Employee employee = em.find(Employee.class,id);
        Employee employee = em.getReference(Employee.class,id);
        phoneNumber.setEmployee(employee);
        em.persist(phoneNumber);
        em.getTransaction().commit();
        em.close();
    }

}
