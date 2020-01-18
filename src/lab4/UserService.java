package lab4;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "database")
    private EntityManager em;

    public UserEntity findByLogin(String login) {
        return em.find(UserEntity.class, login);
    }

    public UserEntity findByToken(String token) {
        try{
            Query query = em.createQuery(("select u from UserEntity u WHERE u.authToken = :token"));
            query.setParameter("token", token);
            return (UserEntity) query.getSingleResult();
        } catch (Exception e){
             return  null;
        }


    }

    public void deleteToken(String login) {
        UserEntity user = findByLogin(login);
        user.setAuthToken(null);
        em.merge(user);
    }

    public void updateUser(UserEntity user) {
        em.merge(user);
    }



}
