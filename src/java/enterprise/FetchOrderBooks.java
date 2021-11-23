package enterprise;

import entity.OrderBooks;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class FetchOrderBooks {
    @PersistenceContext
    EntityManager	em;

    public List<OrderBooks> getHistory(String id){
        TypedQuery<OrderBooks> q = em.createNamedQuery(
                                       OrderBooks.ORDER_HISTORY,
                                       OrderBooks.class);
        q.setParameter("userId", id);
        return	q.getResultList();
    }
}
