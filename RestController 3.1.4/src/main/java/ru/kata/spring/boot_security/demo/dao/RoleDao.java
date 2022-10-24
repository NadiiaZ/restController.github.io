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

@Repository
public class RoleDao {
    private final EntityManagerFactory entityManager;
    private EntityManager em = null;
    private EntityTransaction transaction = null;

    @Autowired
    public RoleDao(EntityManagerFactory entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Role findRoleByName(String name) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();
        List<Role> roleList = em.createQuery
                        ("select r from Role r where r.roleName LIKE :roleName", Role.class)
                .setParameter("roleName", name)
                .getResultList();
        transaction.commit();

        if (roleList.isEmpty()) return null;

        return roleList.get(0);
    }

    @Transactional
    public void save(Role role) {
        em = entityManager.createEntityManager();
        transaction = em.getTransaction();
        transaction.begin();
        em.persist(role);
        transaction.commit();
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        em = entityManager.createEntityManager();

        Session session = em.unwrap(Session.class);
        List<Role> roles = session.createQuery("from Role").getResultList();
        return roles;
    }
}
