package facades;

import entities.Fact;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FactFacadeTest {
    private static EntityManagerFactory emf;
    private static FactFacade facade;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = FactFacade.getFacade(emf);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new User("test", "test"));
            em.getTransaction().commit();
        } catch (RollbackException e) {
            System.out.println("User already exists");
        } finally {
            em.close();
        }
    }


    @BeforeEach
    void setUp() {
        System.out.println("_______________________________________________________\nTESTING:\n");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Fact").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void createFact() {
        System.out.println("createFact\n");
        Fact fact = facade.createFact(new Fact("test"));
        System.out.println("Fact created: "+fact);
        assertEquals("test", fact.getBody());
    }

    @Test
    void getFactById() {
        System.out.println("getFactById\n");
        facade.createFact(new Fact("test"));
        System.out.println("Fact gotten through id: "+facade.getFactById(facade.getFactByBody("test").getId().intValue()));
        assertNotNull(facade.getFactById(facade.getFactByBody("test").getId().intValue()));
    }

    @Test
    void getFactByBody() {
        System.out.println("getFactByBody\n");
        facade.createFact(new Fact("test"));
        System.out.println("Fact gotten through body: "+facade.getFactByBody("test"));
        assertEquals("test", facade.getFactByBody("test").getBody());
    }

    @Test
    void addFavoriteFact() {
        System.out.println("addFavoriteFact\n");
        EntityManager em = emf.createEntityManager();
        facade.createFact(new Fact("test"));
        List<Fact> addList = facade.addFavoriteFact(em.find(User.class, "test"), new Fact("test"));
        assertEquals("test", addList.get(0).getBody());
    }

    @Test
    void removeFavoriteFact() {
        System.out.println("removeFavoriteFact\n");
        EntityManager em = emf.createEntityManager();
        Fact fact = facade.createFact(new Fact("test"));
        User user = em.find(User.class, "test");
        facade.addFavoriteFact(user, fact); //To check if the fact is added
        try{
            em.getTransaction().begin();
            em.refresh(user);
            em.getTransaction().commit();
        }
        catch (Exception e){
            System.out.println("User not found");
        }
        List<Fact> addList = user.getFactList();
         System.out.println("Added to list. Size of list: "+addList.size());
        List<Fact> removeList = facade.removeFavoriteFact(user, user.getFactList().get(0).getId().intValue()); //To check if the fact is removed
        System.out.println("Removed from list. Size of list: "+removeList.size());
        assertTrue(addList.size() > removeList.size());
    }
}