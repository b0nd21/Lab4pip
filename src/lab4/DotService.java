package lab4;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DotService {
    @PersistenceContext(unitName = "database")
    private EntityManager em;

    public void save(DotEntity dotEntity){
        dotEntity.setInarea(AreaCheck.isInArea(new Dot(dotEntity.getX(),dotEntity.getY(),dotEntity.getR())));
        em.persist(dotEntity);
    }

    public List<DotEntity> getDots(UserEntity userEntity){
        Query query = em.createQuery(("select d from DotEntity d WHERE d.user = :id"));
        query.setParameter("id", userEntity);
        return query.getResultList();
    }

    public List<DotEntity> changedRDots(UserEntity userEntity, Double r){
        Query query = em.createQuery(("select d from DotEntity d WHERE d.user = :id"));
        query.setParameter("id", userEntity);
        List<DotEntity> oldDots = query.getResultList();
        List<DotEntity> newDots = new ArrayList<>();
        for (DotEntity d:oldDots){
            DotEntity dotEntity = new DotEntity(d.getX(), d.getY(), r,AreaCheck.isInArea(new Dot(d.getX(),d.getY(),r)), userEntity);
            newDots.add(dotEntity);
        }
        return  newDots;
    }
}

