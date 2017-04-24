package com.wenka.domain.dao;

import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.slf4j.Logger;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/2/26.
 */
@MappedSuperclass
public abstract class BaseDao<T extends Serializable, PK extends Serializable> {
    protected Logger logger = LoggerFactory.getLogger(BaseDao.class);
    protected static final int DEFAULT_MAX_RESULT = 20;
    private final Class<T> entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private boolean cacheQueries = false;
    private String queryCacheRegion;
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    public BaseDao() {
    }

    public boolean isCacheQueries() {
        return this.cacheQueries;
    }

    public void setCacheQueries(boolean cacheQueries) {
        this.cacheQueries = cacheQueries;
    }

    public String getQueryCacheRegion() {
        return this.queryCacheRegion;
    }

    public void setQueryCacheRegion(String queryCacheRegion) {
        this.queryCacheRegion = queryCacheRegion;
    }

    public Session getSession() {
        Session result = null;

        try {
            result = this.sessionFactory.getCurrentSession();
        } catch (HibernateException var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public T get(PK id) {
        Object o = this.getSession().get(this.entityClass, id);
        return o != null ? (T) o : null;
    }

    public PK save(T model) {
        return (PK) this.getSession().save(model);
    }

    public void update(T model) {
        this.getSession().update(model);
    }

    public void saveOrUpdate(T model) {
        this.getSession().saveOrUpdate(model);
    }

    public void merge(T model) {
        this.getSession().merge(model);
    }

    public void delete(PK id) {
        Serializable obj = this.get(id);
        if (obj != null) {
            this.getSession().delete(obj);
        }
    }

    public void deleteObject(T model) {
        if (model != null) {
            this.getSession().delete(model);
        }
    }

    public boolean exists(PK id) {
        return this.get(id) != null;
    }

    public void flush() {
        this.getSession().flush();
    }

    public void clear() {
        this.getSession().clear();
    }

    public List<T> find(String hql, Object... values) {
        return this.find(hql, Integer.valueOf(-1), Integer.valueOf(-1), values);
    }

    public T findUnique(String hql, Object... values) {
        List list = this.find(hql, values);
        return list != null && list.size() == 1 ? (T) list.get(0) : null;
    }

    public List<T> find(String hql, Integer firstResult, Integer maxResults, Object... values) {
        int _firstResult = 0;
        if (firstResult != null && firstResult.intValue() > 0) {
            _firstResult = firstResult.intValue();
        }

        int _maxResults = 20;
        if (maxResults != null && maxResults.intValue() > 0) {
            _maxResults = maxResults.intValue();
        }

        Query queryObject = this.getSession().createQuery(hql);
        this.prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; ++i) {
                queryObject.setParameter(i, values[i]);
            }
        }

        queryObject.setFirstResult(_firstResult);
        if (maxResults == null || maxResults.intValue() > 0) {
            queryObject.setMaxResults(_maxResults);
        }

        return queryObject.list();
    }

    public List<T> findByNamedParam(String hql, String paramName, Object value) {
        HashMap params = new HashMap();
        params.put(paramName, value);
        return this.findByNamedParam(hql, params);
    }

    public List<T> findByNamedParam(String hql, Map<String, Object> paramAndValues) {
        return this.findByNamedParam(hql, Integer.valueOf(-1), Integer.valueOf(-1), paramAndValues);
    }

    public List<T> findByNamedParam(String hql, Integer firstResult, Integer maxResults, Map<String, Object> paramAndValues) {
        int _firstResult = 0;
        if (firstResult != null && firstResult.intValue() > 0) {
            _firstResult = firstResult.intValue();
        }

        int _maxResults = 20;
        if (maxResults != null && maxResults.intValue() > 0) {
            _maxResults = maxResults.intValue();
        }

        Query queryObject = this.getSession().createQuery(hql);
        this.prepareQuery(queryObject);
        if (paramAndValues != null) {
            Iterator var8 = paramAndValues.entrySet().iterator();

            while (var8.hasNext()) {
                Map.Entry entry = (Map.Entry) var8.next();
                this.applyNamedParameterToQuery(queryObject, (String) entry.getKey(), entry.getValue());
            }
        }

        queryObject.setFirstResult(_firstResult);
        if (maxResults == null || maxResults.intValue() > 0) {
            queryObject.setMaxResults(_maxResults);
        }

        return queryObject.list();
    }

    public Integer getResultSize(String countHql, Map<String, Object> paramAndValues) {
        Query queryObject = this.getSession().createQuery(countHql);
        this.prepareQuery(queryObject);
        if (paramAndValues != null) {
            Iterator o = paramAndValues.entrySet().iterator();

            while (o.hasNext()) {
                Map.Entry result = (Map.Entry) o.next();
                this.applyNamedParameterToQuery(queryObject, (String) result.getKey(), result.getValue());
            }
        }

        Object o1 = queryObject.uniqueResult();
        int result1 = 0;
        if (o1 != null) {
            result1 = Integer.parseInt(o1.toString());
        }

        return Integer.valueOf(result1);
    }

    public List<T> findByCriteria(DetachedCriteria criteria) {
        return this.findByCriteria(criteria, Integer.valueOf(-1), Integer.valueOf(-1));
    }

    public List<T> findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults) {
        int _firstResult = 0;
        if (firstResult != null && firstResult.intValue() > 0) {
            _firstResult = firstResult.intValue();
        }

        int _maxResults = 20;
        if (maxResults != null && maxResults.intValue() > 0) {
            _maxResults = maxResults.intValue();
        }

        Criteria executableCriteria = criteria.getExecutableCriteria(this.getSession());
        this.prepareCriteria(executableCriteria);
        executableCriteria.setFirstResult(_firstResult);
        if (maxResults == null || maxResults.intValue() > 0) {
            executableCriteria.setMaxResults(_maxResults);
        }

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return executableCriteria.list();
    }

    public List<Map<String, Object>> findBySQL(String sql, Object... values) {
        return this.findBySQL(sql, Integer.valueOf(-1), Integer.valueOf(-1), values);
    }

    public Map<String, Object> findUniqueBySQL(String sql, Object... values) {
        List list = this.findBySQL(sql, values);
        return list != null && list.size() == 1 ? (Map) list.get(0) : null;
    }

    public List<Map<String, Object>> findBySQL(String sql, Integer firstResult, Integer maxResults, Object... values) {
        int _firstResult = 0;
        if (firstResult != null && firstResult.intValue() > 0) {
            _firstResult = firstResult.intValue();
        }

        int _maxResults = 20;
        if (maxResults != null && maxResults.intValue() > 0) {
            _maxResults = maxResults.intValue();
        }

        SQLQuery queryObject = this.getSession().createSQLQuery(sql);
        queryObject.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        this.prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; ++i) {
                queryObject.setParameter(i, values[i]);
            }
        }

        queryObject.setFirstResult(_firstResult);
        if (maxResults == null || maxResults.intValue() > 0) {
            queryObject.setMaxResults(_maxResults);
        }

        return queryObject.list();
    }

    public List<Map<String, Object>> findByNamedParamSQL(String hql, String paramName, Object value) {
        HashMap params = new HashMap();
        params.put(paramName, value);
        return this.findByNamedParamSQL(hql, params);
    }

    public List<Map<String, Object>> findByNamedParamSQL(String hql, Map<String, Object> paramAndValues) {
        return this.findByNamedParamSQL(hql, Integer.valueOf(-1), Integer.valueOf(-1), paramAndValues);
    }

    public List<Map<String, Object>> findByNamedParamSQL(String sql, Integer firstResult, Integer maxResults, Map<String, Object> paramAndValues) {
        int _firstResult = 0;
        if (firstResult != null && firstResult.intValue() > 0) {
            _firstResult = firstResult.intValue();
        }

        int _maxResults = 20;
        if (maxResults != null && maxResults.intValue() > 0) {
            _maxResults = maxResults.intValue();
        }

        SQLQuery queryObject = this.getSession().createSQLQuery(sql);
        queryObject.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        this.prepareQuery(queryObject);
        if (paramAndValues != null) {
            Iterator var8 = paramAndValues.entrySet().iterator();

            while (var8.hasNext()) {
                Map.Entry entry = (Map.Entry) var8.next();
                this.applyNamedParameterToQuery(queryObject, (String) entry.getKey(), entry.getValue());
            }
        }

        queryObject.setFirstResult(_firstResult);
        if (maxResults == null || maxResults.intValue() > 0) {
            queryObject.setMaxResults(_maxResults);
        }

        return queryObject.list();
    }

    protected Iterator<T> iterate(String hql, Object... values) {
        Query queryObject = this.getSession().createQuery(hql);
        this.prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; ++i) {
                queryObject.setParameter(i, values[i]);
            }
        }

        return queryObject.iterate();
    }

    protected void closeIterator(Iterator<T> it) {
        Hibernate.close(it);
    }

    public int bulkUpdate(String hql, Object... values) {
        Query queryObject = this.getSession().createQuery(hql);
        this.prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; ++i) {
                queryObject.setParameter(i, values[i]);
            }
        }

        return queryObject.executeUpdate();
    }

    protected void prepareQuery(Query queryObject) {
        if (this.isCacheQueries()) {
            queryObject.setCacheable(true);
            if (this.getQueryCacheRegion() != null) {
                queryObject.setCacheRegion(this.getQueryCacheRegion());
            }
        }

    }

    protected void prepareCriteria(Criteria criteria) {
        if (this.isCacheQueries()) {
            criteria.setCacheable(true);
            if (this.getQueryCacheRegion() != null) {
                criteria.setCacheRegion(this.getQueryCacheRegion());
            }
        }

    }

    protected void applyNamedParameterToQuery(Query queryObject, String paramName, Object value) {
        if (value instanceof Collection) {
            queryObject.setParameterList(paramName, (Collection) value);
        } else if (value instanceof Object[]) {
            queryObject.setParameterList(paramName, (Object[]) ((Object[]) value));
        } else {
            queryObject.setParameter(paramName, value);
        }

    }

    public long getCount(String hql, Map<String, Object> paramAndValues) {
        String countHql = "SELECT count(1)  ";
        hql = countHql + hql;
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setProperties(paramAndValues);
        Object o = query.uniqueResult();
        if (o != null) {
            Long result = (Long) o;
            return result.longValue();
        } else {
            return 0L;
        }
    }
}

