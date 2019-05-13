package com.baeldung.jpa.projections;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateProjectionsIntegrationTest {
    private static Logger logger = LoggerFactory.getLogger(HibernateProjectionsIntegrationTest.class);
    private static Session session;
    private Transaction transaction;

    @BeforeClass
    public static void init() {
        Configuration configuration = getConfiguration();
        configuration.addAnnotatedClass(Product.class);
        session = configuration.buildSessionFactory().getCurrentSession();
    }    
    
    @Before
    public void before() {
        transaction = session.beginTransaction();        
    }   
    
    @After
    public void after() {
        if(transaction.isActive()) {
            transaction.rollback();
        }
    }  

    private static Configuration getConfiguration() {
        Configuration cfg = new Configuration();
        cfg.setProperty(AvailableSettings.DIALECT,
            "org.hibernate.dialect.H2Dialect");
        cfg.setProperty(AvailableSettings.HBM2DDL_AUTO, "none");
        cfg.setProperty(AvailableSettings.DRIVER, "org.h2.Driver");
        cfg.setProperty(AvailableSettings.URL,
            "jdbc:h2:mem:myexceptiondb2;DB_CLOSE_DELAY=-1;;INIT=RUNSCRIPT FROM 'src/test/resources/products.sql'");
        cfg.setProperty(AvailableSettings.USER, "sa");
        cfg.setProperty(AvailableSettings.PASS, "");
        cfg.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        return cfg;
    }
    
    
    @SuppressWarnings("deprecation")
    @Test
    public void givenProductData_whenIdAndNameProjectionUsingCriteria_thenListOfObjectArrayReturned() {
        Criteria criteria = session.createCriteria(Product.class);
        criteria = criteria.setProjection(Projections.projectionList()
            .add(Projections.id())
            .add(Projections.property("name")));
        List resultList = criteria.list();
        for (Object result : resultList) {
            logger.info("{}", result);
        }
    }
    
    
    @Test
    public void givenProductData_whenNameProjectionUsingCriteriaBuilder_thenListOfStringReturned() {
        Criteria criteria = session.createCriteria(Product.class);
        criteria = criteria.setProjection(Projections.property("name"));
        List resultList = criteria.list();
        for(Object result: resultList) {
            logger.info("{}", result);
        }
    }
    
    @Test
    public void givenProductData_whenCountByCategoryUsingCriteriaBuider_thenOK() {
        Criteria criteria = session.createCriteria(Product.class);
        criteria = criteria.setProjection(Projections.projectionList()
            .add(Projections.groupProperty("category"))
            .add(Projections.rowCount()));
        List countByCategory = criteria.list();
        for (Object result : countByCategory) {
            logger.info("{}", result);
        }
    }
}
