package employees;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

   /* public Employee findEmployeeByName(String name) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.createQuery("select e from Employee e where e.name = :name", Employee.class)
                .setParameter("name", name)
                .getSingleResult();
        em.close();
        return employee;
    }*/

    public Employee findEmployeeByIdWithPhoneNumbers(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.createQuery("select e from Employee e join fetch e.phoneNumbers where e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        em.close();
        return employee;
    }

    public void addPhoneNumber(long id, PhoneNumber phoneNumber) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        //Employee employee = em.find(Employee.class,id);
        Employee employee = em.getReference(Employee.class, id);
        phoneNumber.setEmployee(employee);
        em.persist(phoneNumber);
        em.getTransaction().commit();
        em.close();
    }

    public Employee findEmployeeByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employees = criteriaQuery.from(Employee.class);
        criteriaQuery.select(employees).where(criteriaBuilder.equal(employees.get("name"), name));
        Employee employee = entityManager.createQuery(criteriaQuery).getSingleResult();
        entityManager.close();
        return employee;
    }

    public List<Employee> listEmployees(int start, int maxResult) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Employee> employees = entityManager
                .createNamedQuery("listEmployees")
                .setFirstResult(start)
                .setMaxResults(maxResult)
                .getResultList();
        entityManager.close();
        return employees;
    }

    public int findParkingPlaceNumberByEmployeeName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        int i = entityManager.createQuery("select p.number from Employee e join e.parkingPlace p where e.name = :name", Integer.class)
                .setParameter("name", name)
                .getSingleResult();
        entityManager.close();
        return i;
    }

    public List<Object[]> listEmployeesBaseData() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Object[]> empDatas = entityManager
                .createQuery("select e.id, e.name from Employee e")
                .getResultList();
        entityManager.close();
        return empDatas;
    }


}
