package facades;

import entities.Animal;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalFacadeTest {
    private static EntityManagerFactory emf;
    private static AnimalFacade facade;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AnimalFacade.getFacade(emf);
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
            em.createQuery("DELETE FROM Animal").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void createAnimal() {
        System.out.println("createAnimal\n");
        EntityManager em = emf.createEntityManager();
        Animal animal = facade.createAnimal(new Animal("test", "test", "test"));
        System.out.println("Animal created: "+animal);
        assertNotNull(animal);
    }

    @Test
    void getAnimalByName() {
        System.out.println("getAnimalByName\n");
        facade.createAnimal(new Animal("test", "test", "test"));
        Animal animal = facade.getAnimalByName("test");
        System.out.println("Animal gotten through name: "+animal);
        assertEquals("test", facade.getAnimalByName("test").getAnimalName());
    }

    @Test
    void addFavoriteAnimal() {
        System.out.println("addFavoriteAnimal\n");
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, "test");
        facade.createAnimal(new Animal("test", "test", "test"));
        List<Animal> addList = facade.addFavoriteAnimal(user, new Animal("test", "test", "test"));
        System.out.println("Animal added to user: "+addList.get(0));
        assertEquals("test", addList.get(0).getAnimalName());
    }

    @Test
    void removeFavoriteAnimal() {
        System.out.println("removeFavoriteAnimal\n");
        EntityManager em = emf.createEntityManager();
        Animal animal = new Animal("test", "test", "test");
        User user = em.find(User.class, "test");
        facade.addFavoriteAnimal(user, animal);
        try{
            em.getTransaction().begin();
            em.refresh(user);
            em.getTransaction().commit();
        }
        catch (Exception e){
            System.out.println("User not found");
        }
        List<Animal> removeList = facade.removeFavoriteAnimal(user, user.getAnimalList().get(0).getAnimalName()); //To check if the animal is removed
        assertEquals(0, removeList.size());
    }
}