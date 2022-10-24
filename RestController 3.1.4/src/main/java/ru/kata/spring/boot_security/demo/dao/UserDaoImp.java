package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImp implements UserDao{
    private final EntityManagerFactory entityManager;
    private EntityManager em = null;
    private EntityTransaction transaction = null;

    @Autowired
    public UserDaoImp(EntityManagerFactory entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> showAllUsers() {
        em = entityManager.createEntityManager();
        Session session = em.unwrap(Session.class);
        List <User> userList = session.createQuery("from User").getResultList();
        return userList;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();;
        User userById = em.getReference(User.class, id);
        transaction.commit();
        return userById;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByUsername (String username) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();
        List <User> userByUsername = em.createQuery
                        ("select u from User u where u.surname LIKE :username", User.class)
                        .setParameter("username", username)
                                .getResultList();
        transaction.commit();
        if (userByUsername.isEmpty()) {
            return null;
        } else {
            return userByUsername.get(0);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();
        User deletedUser = em.find(User.class, id);
        em.remove(deletedUser);
        transaction.commit();
    }

    @Override
    @Transactional
    public void update(int id, User user, Set<Role> roles) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();
        User userUpdate = em.find(User.class, id);
        userUpdate.setName(user.getName());
        userUpdate.setSurname(user.getSurname());
        userUpdate.setEmail(user.getEmail());
        if(!roles.isEmpty())
            userUpdate.setUserRoles(roles);
        em.merge(userUpdate);
        transaction.commit();
    }

    @Override
    @Transactional
    public void save(User user) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();
        em.persist(user);
        transaction.commit();
    }
}
