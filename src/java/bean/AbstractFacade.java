/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author buddhika
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> findBySQL(String temSQL) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        return qry.getResultList();
    }

    public T findFirstBySQL(String temSQL) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        List<T> lst = qry.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return lst.get(0);
        } else {
            return null;
        }
    }

    public List<T> findBySQL(String temSQL, Map<String, Date> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();

            String pPara = (String) m.getKey();
            Object pVal = m.getValue();
            if (pVal instanceof Date) {
                Date pValDate = (Date) m.getValue();
                qry.setParameter(pPara, pValDate, TemporalType.DATE);
            } else {
                qry.setParameter(pPara, pVal);
            }
            System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        return qry.getResultList();
    }

    public Long findLongBySQL(String temSQL, Map<String, Date> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();

            String pPara = (String) m.getKey();
            Object pVal = m.getValue();
            if (pVal instanceof Date) {
                Date pValDate = (Date) m.getValue();
                qry.setParameter(pPara, pValDate, TemporalType.DATE);
            } else {
                qry.setParameter(pPara, pVal);
            }
//            System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        Long l;
        try {
            l = (Long) qry.getSingleResult();
        } catch (Exception e) {
            l = 0l;
        }
        if (l == null) {
            l = 0l;
        }
        return l;
    }

}
