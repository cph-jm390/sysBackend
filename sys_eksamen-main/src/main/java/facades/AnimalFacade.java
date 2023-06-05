package facades;

import entities.Animal;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class AnimalFacade {

    static EntityManagerFactory emf;
    private static AnimalFacade instance;

    public static AnimalFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AnimalFacade();
        }
        return instance;
    }

    public static Animal createAnimal(Animal animal) {
        EntityManager em = emf.createEntityManager();
        animal.setAnimalName(animal.getAnimalName().toLowerCase());
        try {
            em.getTransaction().begin();
            em.persist(animal);
            em.getTransaction().commit();
            return animal;
        } finally {
            em.close();
        }
    }

    public static Animal getAnimalByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Animal.class, name);
        } finally {
            em.close();
        }
    }

    public static List<Animal> addFavoriteAnimal(User user, Animal animal)
    {
        EntityManager em = emf.createEntityManager();

        try {
            user = em.find(User.class, user.getUserName());
            TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a WHERE a.animalName = :name", Animal.class).setParameter("name", animal.getAnimalName());
            if(query.getResultList().size() > 0)
            {
                animal = query.getResultList().get(0);
                System.out.println("Animal already exists");
            }
            user.getAnimalList().add(animal);
            user.setAnimalList(user.getAnimalList());
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return user.getAnimalList();
        } finally {
            em.close();
        }
    }

    public static List<Animal> removeFavoriteAnimal(User user, String animalName)
    {
        EntityManager em = emf.createEntityManager();
        try {
            user = em.find(User.class, user.getUserName());
            user.getAnimalList().removeIf(animal -> animal.getAnimalName().equals(animalName));
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return user.getAnimalList();
        } finally {
            em.close();
        }
    }

}
