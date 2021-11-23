package enterprise;

import entity.UserAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class FetchUserAccount {
    @PersistenceContext
    EntityManager em;

    public long countAccount(String id){
        TypedQuery<Long> q;
        // ユーザーIDの件数を取得するクエリを生成
        q = em.createNamedQuery(UserAccount.COUNT_ID, Long.class);

        // 入力されたユーザーIDをプレースホルダーに設定
        q.setParameter("userId", id);
            
        // クエリを実行してレコードの件数を取得
        long countResult = q.getSingleResult();
        return countResult;
    }
}
