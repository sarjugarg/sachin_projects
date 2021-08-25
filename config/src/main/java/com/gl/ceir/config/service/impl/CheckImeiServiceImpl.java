package com.gl.ceir.config.service.impl;

import com.gl.Rule_engine.RuleEngineApplication;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.gl.Rule_engine.RuleEngineApplication;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.CheckImeiValuesEntity;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.RuleEngineMapping;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.CheckImeiRepository;
import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionImpl;

@Service
public class CheckImeiServiceImpl {

    private static final Logger logger = Logger.getLogger(CheckImeiServiceImpl.class);

    @Autowired
    CheckImeiRepository checkImeiRepository;

    @Autowired
    AuditTrailRepository auditTrailRepository;

    @PersistenceContext
    EntityManager entityManager;

    public String getResult(String user_type, String feature, String imei, Long imei_type) {
        String rulePass = "true";

        try {
            Connection conn = getSQlConnection();
            BufferedWriter bw = null;
            String expOutput = "";
//            ArrayList<Rule> rule_details = new ArrayList<Rule>();
            int i = imei_type.intValue();
            String deviceIdValue = null;
            switch (i) {
                case 0:
                    deviceIdValue = "IMEI";
                    break;
                case 1:
                    deviceIdValue = "MEID";
                    break;
                case 2:
                    deviceIdValue = "ESN";
                    break;
            }
//            deviceIdValue = "IMEI";       // to be remove if another values came ,
            List<RuleEngineMapping> ruleList = checkImeiRepository.getByFeatureAndUserTypeOrderByRuleOrder(feature, user_type);
            logger.info("RuleList is " + ruleList);
            for (RuleEngineMapping cim : ruleList) {
                Rule rule = new Rule(cim.getName(), cim.getOutput(), cim.getRuleMessage());
//                rule_details.add(rule);
//            }
//            logger.info("Rules Populated");  // optimse
//            for (Rule rule : rule_details) {

                String[] my_arr = {rule.rule_name, "1", "NONCDR", ((rule.rule_name.equals("IMEI_LUHN_CHECK") || rule.rule_name.equals("IMEI_LENGTH")) ? imei : imei.substring(0, 14)), "4", "5", "6", "7", "8", deviceIdValue, "", " ", " ", ""};
                logger.info("Rule : " + rule.rule_name);
                expOutput = RuleEngineApplication.startRuleEngine(my_arr, conn, bw);
                logger.info("Rule Output By Engine :" + expOutput + " , Expected Output : " + rule.output);
                if (rule.output.equalsIgnoreCase(expOutput)) {    // go to next rule(  rule_engine _mapping )
                    logger.info("Rule Passed");
                } else {
                    logger.info("Rule failed at " + rule.rule_name);
                    rulePass = rule.rule_name;
                    break;
                }
            }
            logger.info("Conn Close");
            conn.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        } finally {

//            auditTrailRepository.save(new AuditTrail(checkImeiValuesEntity.getUserId(), checkImeiValuesEntity.getUserName(),
//                    Long.valueOf(checkImeiValuesEntity.getUserTypeId()), checkImeiValuesEntity.getUserType(),
//                    Long.valueOf(checkImeiValuesEntity.getFeatureId()), Features.CONSIGNMENT, "VIEW_ALL", "", "NA",
//                    checkImeiValuesEntity.getRoleType(), checkImeiValuesEntity.getPublicIp(), checkImeiValuesEntity.getBrowser()));
//            logger.info("AUDIT : Saved view request in audit.");

        }
        return rulePass;
    }

    private Connection getSQlConnection() throws SQLException {
        Connection c1 = null;
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(CheckImeiServiceImpl.class)
                .buildMetadata();

        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
                .build();

        try {
            c1 = sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
            logger.info("Connection for Rule " + c1);
            logger.info(c1.getMetaData().getDatabaseProductName());

        } catch (Exception e) {
            logger.info(" Erorr E1 " + e);
        }
        return c1;

    }

}

//        Connection c = sessionFactory.
//                getSessionFactoryOptions().getServiceRegistry().
//                getService(ConnectionProvider.class).getConnection();
//
//        Session sess = null;
//        try {
//            sess = sessionFactory.getCurrentSession();
//        } catch (org.hibernate.HibernateException he) {
//            sess = sessionFactory.openSession();
//        }
//
//        c = ((SessionImpl) sess.getSession()).connection();
//        logger.info("Connection for Rule " + c);
//        logger.info(c.getMetaData().getDatabaseProductName());
