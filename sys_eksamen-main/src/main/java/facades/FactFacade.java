package facades;

import entities.Fact;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class FactFacade {

    static EntityManagerFactory emf;
    private static FactFacade instance;

    public static FactFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FactFacade();
        }
        return instance;
    }

    public static Fact createFact(Fact fact) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(fact);
            em.getTransaction().commit();
            return fact;
        } finally {
            em.close();
        }
    }

    public static Fact getFactById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Fact.class, (long) id);
        } finally {
            em.close();
        }
    }

    public static Fact getFactByBody(String factBody) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createQuery("SELECT f FROM Fact f WHERE f.body = :body", Fact.class).setParameter("body", factBody).getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public static List<Fact> addFavoriteFact(User user, Fact fact) {
        EntityManager em = emf.createEntityManager();

        try {
            user = em.find(User.class, user.getUserName());
            TypedQuery<Fact> query = em.createQuery("SELECT f FROM Fact f WHERE f.body = :body", Fact.class).setParameter("body", fact.getBody());
            Fact existingFact = null;
            try {
                existingFact = query.getSingleResult();
                fact = existingFact;
            } catch (NoResultException ex) {
                // Fact doesn't exist - creates a new one
            }
            if (!user.getFactList().contains(fact)) {
                List<Fact> alteredList = user.getFactList();
                alteredList.add(fact);
                user.setFactList(alteredList);
            } else {
                System.out.println("Fact already in user's fact list");
            }
            System.out.println("FactList: " + user.getFactList());
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return user.getFactList();
        } finally {
            em.close();
        }
    }



    public static List<Fact> removeFavoriteFact(User user, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            user = em.find(User.class, user.getUserName());
            user.getFactList().removeIf(fact -> fact.getId() == id);
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return user.getFactList();
        } finally {
            em.close();
        }
    }

}
