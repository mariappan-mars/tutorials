package com.baeldung.jpa.projections;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ProductRepository {
    private EntityManager entityManager;

    public ProductRepository() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpa-projections");
        entityManager = factory.createEntityManager();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object> findAllNamesUsingJPQL() {
        Query query = entityManager.createQuery("select name from Product");
        List<Object> resultList = query.getResultList();
        return resultList;
    }
    
    public List<String> findAllNamesUsingCriteriaBuilderArray() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Product> product = query.from(Product.class);
        query.select(product.get("name"));
        List<String> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> findAllIdAndNamesUsingJPQL() {
        Query query = entityManager.createQuery("select id, name from Product");
        List<Object[]> resultList = query.getResultList();
        return resultList;
    }
    
    public List<Object[]> findAllIdAndNamesUsingCriteriaBuilderArray() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Product> product = query.from(Product.class);
        query.select(builder.array(product.get("id"), product.get("name")));
        List<Object[]> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }
    
    public List<Object[]> findAllIdAndNamesUsingCriteriaQueryMultiselect() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Product> product = query.from(Product.class);
        query.multiselect(product.get("id"), product.get("name"));
        List<Object[]> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }
    
    public List<Tuple> findAllIdAndNamesUsingCriteriaBuilderTuple() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Product> product = query.from(Product.class);
        query.select(builder.tuple(product.get("id"), product.get("name")));
        List<Tuple> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }
    
    public List<Object> findAllIdAndNamesUsingCriteriaBuilderConstruct() {
        return null;
    }
}
