package com.baeldung.jpa.projections;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProductRepositoryIntegrationTest {
    private static Logger logger = LoggerFactory.getLogger(ProductRepositoryIntegrationTest.class);
    private Session session;
    private Transaction transaction;
    private ProductRepository productRepository;

    @BeforeClass
    public void once() throws IOException {
        productRepository = new ProductRepository();
    }

    @After
    public void tearDown() {
        transaction.rollback();
        session.close();
    }
    
    @Test
    public void givenProductData_whenIdAndNameProjectionUsingJPQL_thenListOfObjectArrayReturned() {
        List<Object[]> resultList = productRepository.findAllIdAndNamesUsingJPQL();
        
        for(Object result: resultList) {
            logger.info("{}", result);
        }
    }
    
    @Test
    public void givenProductData_whenIdAndNameProjectionUsingCriteriaBuilder_thenListOfObjectArrayReturned() {
        List<Object[]> resultList = productRepository.findAllIdAndNamesUsingCriteriaBuilderArray();
                
        for(Object result: resultList) {
            logger.info("{}", result);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void givenProductData_whenNameProjectionUsingJPQL_thenListOfStringReturned() {
        Query query = session.createQuery("select id from Product");
        List resultList = query.getResultList();
        
        for(Object result: resultList) {
            logger.info("{}", result);
        }
    }
    
    @Test
    public void givenProductData_whenNameProjectionUsingCriteriaBuilder_thenListOfStringReturned() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Product> product = query.from(Product.class);
        query.select(builder.array(product.get("name")));
        List<Object[]> resultList = session.createQuery(query).getResultList();
        
        for(Object result: resultList) {
            logger.info("{}", result);
        }
    }
    
    @Test
    public void givenProductData_whenNameProjectionUsingCriteriaBuilderAndTuple_thenListOfStringReturned() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Product> product = query.from(Product.class);
        query.select(builder.tuple(product.get("id")));
        List<Tuple> resultList = session.createQuery(query).getResultList();
        
        for(Object result: resultList) {
            logger.info("{}", result);
        }
    }
}
