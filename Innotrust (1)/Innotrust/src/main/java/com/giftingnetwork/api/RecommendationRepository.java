package com.giftingnetwork.api;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecommendationRepository {

  @Autowired
  JavaXmlEncoder javaXmlEncoder;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  JavaJsonEncoder javaJsonEncoder;

  @Autowired
  ScopesController scopesController;

  @Autowired
  XmlDomHandler xmlDomHandler;

  Logger logger = LoggerFactory.getLogger(RecommendationRepository.class);

  public String getXmlFromQuery(String query, LinkedHashMap<String, String> map) {
    try {

      return jdbcTemplate.query(query, (ResultSet rs) -> {
        String doc = "";
        // String
        if (scopesController.getAcceptType().contains("json")) {
          doc = javaJsonEncoder.toDocument(rs, map, query);
        } else {
          doc = javaXmlEncoder.toDocument(rs, map, query);
        }
        return doc;
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error");
    }
  }

  public String selectWhereFilterValidator(LinkedHashMap<String, String> map, Set<String> whereSet, String apiName) {
    try {
      String query = "SELECT select_paramters ,filtered_parameters,filter_required   FROM  api_table_mapping WHERE   api_name=  '"
          + apiName + "'  ";
      logger.info("[  " + query + " ]");
      return jdbcTemplate.query(query, (ResultSet rs) -> {
        String data = "true";
        while (rs.next()) {
          logger.info("[" + rs.getString("select_paramters") + " ; " + rs.getString("filtered_parameters") + " ; "
              + rs.getString("filter_required") + "]");

          if (rs.getString("select_paramters") == null || rs.getString("filtered_parameters") == null
              || rs.getString("filter_required") == null) {
            return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error For Null");
          }

          List<String> selectNameList = new ArrayList<>(Arrays.asList(rs.getString("select_paramters").split(",")));
          List<String> whereNameList = new ArrayList<>(Arrays.asList(rs.getString("filtered_parameters").split(",")));
          for (String mapValue : map.keySet()) {
            logger.info("MapValue :" + mapValue + ": ");
            if (!selectNameList.contains(mapValue)) {
              logger.info("SelectValue :" + mapValue + ": is not present in Select Parameters ");
              return xmlDomHandler.genericErrorMethod("Client Error", "Syntax  Error");
            }
          }
          if (rs.getString("filter_required").equals("Yes")) {
            if (whereSet.size() == 0) {
              logger.info("Size for Where Parameter is 0 ");
              return xmlDomHandler.genericErrorMethod("Client Error", "Syntax  Error ");
            }

            for (String setValue : whereSet) {
              logger.info("setValue :" + setValue + ": ");
              if (!whereNameList.contains(setValue)) {
                logger.info("WhereValue :" + setValue + ": is not present in Where Parameters ");
                return xmlDomHandler.genericErrorMethod("Client Error", "Syntax  Error ");
              }
            }
          }
        }
        return data;
      });
    } catch (Exception e) {
      logger.error("  Error " + e);
      return xmlDomHandler.genericErrorMethod("Client Error", "Syntax  Error");
    }
  }

  public String getJsonFromQuery(String query, LinkedHashMap<String, String> map) {

    try {
      logger.info("Repo Query : " + query);
      return jdbcTemplate.query(query, (ResultSet rs) -> {
        logger.info("Repo Query Inside   ");

        String doc = javaJsonEncoder.toDocument(rs, map, query);
        return doc;
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error");
    }
  }

}