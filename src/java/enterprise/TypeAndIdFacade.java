/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise;

import entity.TypeAndId;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Owner
 */
@Stateless
public class TypeAndIdFacade extends AbstractFacade<TypeAndId> {

    @PersistenceContext(unitName = "BookStorePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeAndIdFacade() {
        super(TypeAndId.class);
    }
    
}
