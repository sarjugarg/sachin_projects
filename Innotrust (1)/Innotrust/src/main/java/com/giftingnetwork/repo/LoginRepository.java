package com.giftingnetwork.repo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData; 
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.giftingnetwork.BootApplication;
import com.giftingnetwork.controller.ScopesController;
import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.model.GenericModel;
import com.giftingnetwork.util.GenericFunctions;

import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginRepository {

    Logger   logger = LoggerFactory.getLogger(LoginRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    XmlDomHandler xmlDomHandler;

    @Autowired
    GenericFunctions genericFunctions;

    @Autowired
    ScopesController scopesController;

    public String getUserDetailsFromAuthUser(GenericModel genericModel) {
        try {
            String query = " select count(username)  from auth_user  inner join contact on  contact.auth_user_id  = auth_user.auth_user_id  inner join contact_type_contact on contact.contact_id =contact_type_contact.contact_id  inner join contact_type on  contact_type_contact.contact_type_id =contact_type.contact_type_id   where   contact_type.contact_type = 'Api User'     and  auth_user.username=  '"
                    + genericModel.getUsername() + "'  and  auth_user.password = '" + genericModel.getPassword()
                    + "'         ";
            logger.info("[" + query + "] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                String data = "";
                while (rs.next()) {
                    logger.info(" AuthUser authentication result Value " + rs.getString(1));
                    data = rs.getString(1).equals("1") ? "true" : "false";
                }
                return data;
            });
        } catch (Exception e) {
            logger.error("Error " + e);
            return "false";
            // return xmlDomHandler.genericErrorMethod("LoginError", "Internal Error");
        }
    }

    public Boolean savesessioninLocalsessionDb(String sessionId, String clientID, String username, String password,
            String userIp, String refresh_token, Date session_token_timeout, Date refresh_token_timeout) {
        deleteFromTokenDbByUserNamePassWord(sessionId, clientID, username, password);
        try {
            String query = " insert into token_db_mapping  ( token_id , client_id ,username , password   ,clientIp ,refresh_token  ,refresh_token_expire_time ,  token_id_expire_time,  init_time  )   values (  ? , ? , ? , ? ,  ?, ? ,? ,?  ,now()   ) ";
            logger.info("[" + query + "]");
            jdbcTemplate.update(query, new Object[] { sessionId, clientID, username, password, userIp, refresh_token,
                    refresh_token_timeout, session_token_timeout });
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    private Boolean deleteFromTokenDbByUserNamePassWord(String sessionId, String clientID, String username,
            String password) {
        try {
            String query = " delete from token_db_mapping  where username  = ?  ";
            logger.info("[" + query + "]");
            jdbcTemplate.update(query, new Object[] { username });
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

    }

    public Boolean deletefromTokenBySessionId(String sessionId) {
        try {
            sessionId = sessionId.substring(7);
            String query = " delete from token_db_mapping  where token_id  = ? ";
            logger.info("[" + query + "]");
            jdbcTemplate.update(query, new Object[] { sessionId });
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public Boolean deleteByUserNamePassword(String username, String password) {
        try {
            String query = " delete from token_db_mapping  where username   = ?  and password = ? ";
            logger.info("[" + query + "]");
            jdbcTemplate.update(query, new Object[] { username, password });
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public String getExtendedQueryFromDB(String api_name, String parent) {
        try {
            
            return jdbcTemplate.query(" select mapped_tables_values from api_table_mapping where api_name=  '"
                    + api_name + "' and parent =  '" + parent + "' ", (ResultSet rs) -> {
                        String data = "";
                        while (rs.next()) {
                            data = rs.getString(1);
                        }
                        return data;
                    });
        } catch (Exception e) {
            logger.error("Error " + e);
            return "false";
        }
    }

    public String getParammeterValuesFromApiTable1(LinkedHashMap<String, String> map, String apiName, String parent) {
        try {
            String query = " SELECT parameter_name, table_name, child   FROM api_parent_child_object_mapping WHERE api_name =  '"
                    + apiName + "' and  parent = '" + parent
                    + "'  and     sub_child_exist = 'N'    order by show_order     ";
            // logger.info(" [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                String value = " ";
                while (rs.next()) {
                    map.put(rs.getString("parameter_name"), rs.getString("child"));
                    value += rs.getString("table_name") + "." + rs.getString("child") + " ,";
                }
                return value;
            });
        } catch (Exception e) {
            logger.error("  Error " + e);
            return "false";
        }
    }

    public String getParammeterValuesFromApiTable(LinkedHashMap<String, String> map, String apiName) {
        try {
            String query = " SELECT parameter_name, table_name, child  , parent   FROM api_parent_child_object_mapping WHERE api_name =  '"
                    + apiName + "'  and  parent = '" + apiName + "' and  sub_child_exist = 'N'    order by id     ";
            logger.info(" [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                String value = " ";
                while (rs.next()) {
                    map.put(rs.getString("parameter_name"), rs.getString("table_name") + "." + rs.getString("child"));
                    value += rs.getString("table_name") + "." + rs.getString("child") + " ,";
                }
                return value;
            });
        } catch (Exception e) {
            logger.error("Error " + e);
            return "false";
        }
    }

    public String getTotalCountFromDb(String query) {
        try {
            query = query.substring(0,
                    (query.contains("ORDER") ? query.indexOf("ORDER") + 0 : query.indexOf("LIMIT") + 0));
            logger.info(" [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                int rowValue = 0;
                while (rs.next()) {
                    rowValue++;
                }
                return String.valueOf(rowValue);
            });
        } catch (Exception e) {
            logger.error("Error " + e);
            return "false";
        }
    }

    public void setSessionTimeOutConfiguration() {
        try {
            String query = " select config_param , param_value  from config  where config_param  = 'SESSION_TIME_OUT'  ";
            jdbcTemplate.query(query, (ResultSet rs) -> {
                BootApplication.LoginSessionTimeOut = rs.getObject("param_value").toString();
            });
        } catch (Exception e) {
            logger.error("Error " + e.getMessage());
        }
    }

    public Date checkRefreshTokenExistance(String refresh_token) {
        try {
            String query = " select  refresh_token_expire_time from token_db_mapping   where refresh_token_expire_time > now() and  refresh_token  = '"
                    + refresh_token + "'  ";
            logger.info("  [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                Date value = null;
                while (rs.next()) {
                    value = rs.getTimestamp(1);
                }
                return value;
            });

        } catch (Exception e) {
            logger.error("Error " + e.getMessage());
            return null;
        }

    }

    public Boolean deletebyRefreshTokenFromDb(String accessToken) {
        {
            try {
                String query = " delete from token_db_mapping  where refresh_token  = ? ";
                logger.info("[" + query + "]");
                int i = jdbcTemplate.update(query, new Object[] { accessToken });
                if (i > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }

    }

    public ResultSet getParentChildMapping(String apiName) {
        String query = "  select parent , array_agg( child) as child , sub_child_exist from public.api_parent_child_object_mapping  where api_name = '"
                + apiName + "'  group by parent ,sub_child_exist ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rs) -> {
            return rs;
        });
    }

    public Map<String, String> getParentMapping(String apiName) {
        Map<String, String> map = new HashMap<String, String>();
        String query = "select a.parent , array_agg( a.child) as child , a.sub_child_exist from  ( select parent ,child,sub_child_exist from  api_parent_child_object_mapping  where api_name = '"
                + apiName + "'  order by show_order ) a  group by a.parent ,a.sub_child_exist ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rset) -> {
            while (rset.next()) {
                if (rset.getString("sub_child_exist").equals("N")) {
                    map.put(rset.getString("parent"), rset.getString("child").replace("{", "").replace("}", ""));
                }
            }
            return map;
        });
    }

    public Map<String, String> getChildMapping(String apiName) {
        Map<String, String> map = new HashMap<String, String>();

        String query = "select   a.parent , array_agg( a.child) as child , a.sub_child_exist from  ( select parent ,child,sub_child_exist from  api_parent_child_object_mapping  where api_name = '"
                + apiName + "'  order by show_order ) a  group by a.parent ,a.sub_child_exist ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rset) -> {
            while (rset.next()) {
                if (rset.getString("sub_child_exist").equals("Y")) {
                    map.put(rset.getString("parent"), rset.getString("child").replace("{", "").replace("}", ""));
                }
            }
            return map;
        });
    }

    public void setDefaultPageSizeConfiguration() {
        try {
            String query = " select config_param , param_value  from config  where config_param = 'DEFAULT_PAGE_SIZE'  ";
            logger.info("  [" + query + " ] " + jdbcTemplate);
            jdbcTemplate.query(query, (ResultSet rs) -> {
                BootApplication.DefaultPageSize = rs.getObject("param_value").toString();
                while (rs.next()) {
                    BootApplication.DefaultPageSize = rs.getString("param_value");
                }
            });
            logger.info("  default Page size  :  " + BootApplication.DefaultPageSize);
        } catch (Exception e) {
            logger.error("  Error " + e.getMessage());
        }
    }

    public void setDefaultMinPageSizeConfiguration() {
        try {
            String query = " select config_param , param_value  from config  where config_param = 'MANIMUM_PAGE_SIZE'  ";
            logger.info("  [" + query + " ] " + jdbcTemplate);
            jdbcTemplate.query(query, (ResultSet rs) -> {
                BootApplication.DefaultMinPageSize = rs.getObject("param_value").toString();
                while (rs.next()) {
                    BootApplication.DefaultMinPageSize = rs.getString("param_value");
                }
            });
            logger.info("  Manimum Page size  :  " + BootApplication.DefaultMinPageSize);
        } catch (Exception e) {
            logger.error("  Error " + e.getMessage());
        }
    }

    public void setDefaultMaxPageSizeConfiguration() {
        try {
            String query = " select config_param , param_value  from config  where config_param = 'MAXIMUM_PAGE_SIZE'  ";
            logger.info("  [" + query + " ] " + jdbcTemplate);
            jdbcTemplate.query(query, (ResultSet rs) -> {
                BootApplication.DefaultMaxPageSize = rs.getObject("param_value").toString();
                while (rs.next()) {
                    BootApplication.DefaultMaxPageSize = rs.getString("param_value");
                }
            });
            logger.info("  Maximum Page size  :  " + BootApplication.DefaultMaxPageSize);
        } catch (Exception e) {
            logger.error("  Error " + e.getMessage());
        }
    }

    public Map<String, String> getDbColumnWithParameters(String apiName) {
        try {
            String query = " SELECT parameter_name,table_name, child   FROM api_parent_child_object_mapping WHERE api_name =  '"
                    + apiName + "' and searchable = 'Y'  and sub_child_exist = 'N'   ";
            logger.info(" [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                Map<String, String> map = new HashMap<String, String>();
                while (rs.next()) {
                    map.put(rs.getString("parameter_name"), rs.getString("table_name") + "." + rs.getString("child"));
                }
                return map;
            });
        } catch (Exception e) {
            logger.error("Error " + e);
            return null;
        }
    }

    public String getIdNameByApiFromDb(String apiName) {
        try {
            String query = "  select param_value from config where  config_param = '" + apiName + "'  ";
            logger.info("  [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                String value = null;
                while (rs.next()) {
                    logger.info("Param Name  " + rs.getObject(1).toString());
                    value = rs.getObject(1).toString();
                }
                return value;
            });

        } catch (Exception e) {
            logger.error("Error " + e.getMessage());
            return null;
        }
    }

    public JSONObject getDynamicValuesForFund(JSONObject funddata, String statement_id) {
        try {
            String query = " select * from fund_statement where fund_statement_id = " + statement_id;
            logger.info(" [" + query + " ] ");
            JSONObject funddata1 = new JSONObject();
            TreeMap<String, String> sorted = new TreeMap<>();
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                ResultSetMetaData rsmd = rs.getMetaData();
                int numColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    logger.info(" rsmd  Count " + numColumns);
                    for (int i = 1; i <= numColumns; i++) {
                        if (rsmd.getColumnName(i).startsWith("fund_") && rs.getObject(i) != null
                                && Character.isDigit(rsmd.getColumnName(i).charAt(5))) {
                    //        funddata.put( rsmd.getColumnName(i),   ( rsmd.getColumnName(i).contains("begin_mv")  || rsmd.getColumnName(i).contains("shares")  ||  rsmd.getColumnName(i).contains("end_mv")  ||  rsmd.getColumnName(i).contains("pct")        )   ?    new DecimalFormat("#,###,##0.00").format(Double.valueOf( rs.getObject(i).toString()  ))  :     rs.getObject(i).toString());
                            sorted.put( rsmd.getColumnName(i),   ( rsmd.getColumnName(i).contains("begin_mv")  || rsmd.getColumnName(i).contains("shares")  ||  rsmd.getColumnName(i).contains("end_mv")  ||  rsmd.getColumnName(i).contains("pct")        )   ?    new DecimalFormat("#,###,##0.00").format(Double.valueOf( rs.getObject(i).toString()  ))  :     rs.getObject(i).toString());
                        }
                    }
                }
                sorted.forEach(     (k,v) ->     funddata.put(k,v )   ) ;
        //        gfg.forEach(  (k,v) -> System.out.println("Key = "+ k + ", Value = " + v));
                return funddata1;
            });
        } catch (Exception e) {
            logger.error(e.getMessage() + "Error " + e.getLocalizedMessage());
            return null;
        }
    }

    public Map<String, String> propertyTypeMapValues(String apiName) {
        Map<String, String> map = new HashMap<String, String>();
        String query = " select  child , prop_type  from api_parent_child_object_mapping where  api_name = '" + apiName
                + "'    and  prop_type is not null ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rset) -> {
            while (rset.next()) {
                map.put(rset.getString("child"), rset.getString("prop_type"));
            }
            return map;
        });

    }

    public JSONObject getRecordsFromDb(String apiName, String header, String id, Map<String, String> childSubChildMap,
            Map<String, String> apiObjectQueries) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // String query = apiObjectQueries.get(header) ;
        // String query = "Select "
        // + genericFunctions.Chars(getParammeterValuesFromApiTable1(map,
        // apiName, header), 1) + " "
        // + getExtendedQueryFromDB(apiName, header); // get main Table

        String query = apiObjectQueries.get(header).replace("?", " '" + id + "' ");
        // logger.info("Query ::" + query);
        // String childName = childSubChildMap.containsKey(header) ?
        // childSubChildMap.get(header) : null;

        String childName = getChildValuesForForParent(apiName, header);

        JSONArray jsonArr = new JSONArray();
        return jdbcTemplate.query(query, (ResultSet rs) -> {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // logger.info(" Column count " + numColumns);
            int i = 0;
            while (rs.next()) {

                JSONObject apiValues = new JSONObject();
                try {
                    Field changeMap = apiValues.getClass().getDeclaredField("map");
                    changeMap.setAccessible(true);
                    changeMap.set(apiValues, new LinkedHashMap<>());
                    changeMap.setAccessible(false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    logger.error(e.getMessage());
                }
                for (int l = 1; l <= numColumns; l++) {
                    try {
                        logger.info(rsmd.getColumnName(l) + "  " + rsmd.getColumnType(l));
                        apiValues.put(rsmd.getColumnName(l), rs.getObject(l) == null ? " "
                                : rsmd.getColumnType(l) == 93 ? new SimpleDateFormat("MM-dd-yyyy").format(
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getObject(l).toString()))
                                        : rsmd.getColumnType(l) == 91 ? new SimpleDateFormat("MM-dd-yyyy").format(
                                                new SimpleDateFormat("yyyy-MM-dd").parse(rs.getObject(l).toString()))
                                                : rs.getObject(l).toString());
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                        apiValues.put(rsmd.getColumnName(l),
                                rs.getObject(l) == null ? " " : rs.getObject(l).toString());
                    }
                }

                // logger.info(" Response " + apiValues.toString());

                if (childName != null) { // get child having header as orga etc
                    JSONObject l3Object = new JSONObject();
                    try {
                        Field changeMap = l3Object.getClass().getDeclaredField("map");
                        changeMap.setAccessible(true);
                        changeMap.set(l3Object, new LinkedHashMap<>());
                        changeMap.setAccessible(false);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        logger.info(e.getMessage());
                    }
                    l3Object = getSingleRecordObj(apiName, childName, id);
                    // logger.info("Response ::: " + l3Object.toString());
                    for (String key : JSONObject.getNames(l3Object)) {
                        Object value = l3Object.get(key);
                        logger.info(" " + key);
                        if (!apiValues.has(key)) {
                            apiValues.put(key, value);
                        }
                    }
                    // logger.info(" Response " + apiValues.toString());
                }
                jsonArr.put(apiValues);
                i++;
            }
            // logger.info(" Values of i " + i);
            JSONObject apiValuesSingle = new JSONObject();

            if (i == 0) {
                try {
                    Field changeMap = apiValuesSingle.getClass().getDeclaredField("map");
                    changeMap.setAccessible(true);
                    changeMap.set(apiValuesSingle, new LinkedHashMap<>());
                    changeMap.setAccessible(false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    logger.info(e.getMessage());
                }
                for (int l = 1; l <= numColumns; l++) {
                    // logger.info(" " + rsmd.getColumnName(l));
                    apiValuesSingle.put(rsmd.getColumnName(l), " ");
                }
                // logger.info("Response L2 " + apiValuesSingle.toString());

                if (childName != null) {
                    logger.info("Going to Get   " + childName + " Values ");
                    JSONObject l3Object = new JSONObject();
                    try {
                        Field changeMap = l3Object.getClass().getDeclaredField("map");
                        changeMap.setAccessible(true);
                        changeMap.set(l3Object, new LinkedHashMap<>());
                        changeMap.setAccessible(false);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        logger.error(e.getMessage());
                    }
                    l3Object = getSingleRecordObj(apiName, childName, id);
                    // logger.info(" Response L3 " + l3Object.toString());
                    for (String key : JSONObject.getNames(l3Object)) {
                        Object value = l3Object.get(key);
                        logger.info(" " + key);
                        if (!apiValuesSingle.has(key)) {
                            apiValuesSingle.put(key, value);
                        }
                    }
                    // logger.info(" Response " + apiValuesSingle.toString());
                }
                jsonArr.put(apiValuesSingle);
            }
            logger.info(jsonArr.toString());
            JSONObject jsn = new JSONObject();
            logger.info(" Required Value  " + i);
            if (i == 0) {
                // logger.info(" Response " + header + " :: " + apiValuesSingle);
                jsn.put(header, apiValuesSingle);
            } else if (i == 1) {
                // logger.info(" Response " + header + " :: " + jsonArr.getJSONObject(0));

                jsn.put(header, jsonArr.getJSONObject(0));
            } else {
                jsn.put(header, jsonArr);
            }
            return jsn;
        });
    }

    public String getChildValuesForForParent(String apiName, String header) {
        String dbName = scopesController.getDBName();
        scopesController.setDBName("localDb");
        String query = " select  distinct child  from api_parent_child_object_mapping where api_name = '" + apiName
                + "' and parent = '" + header + "' and sub_child_exist = 'Y'   ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rset) -> {
            String data = null;
            while (rset.next()) {
                data = rset.getString(1);
            }
            scopesController.setDBName(dbName);
            return data;
        });
    }

    public JSONObject getSingleRecordObj(String apiName, String header, String id) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String dbName = scopesController.getDBName();
        scopesController.setDBName("localDb");
        String query = "Select "
                + genericFunctions.removeLastChars(getParammeterValuesFromApiTable1(map, apiName, header), 1) + " "
                + getExtendedQueryFromDB(apiName, header); // get main Table
        query = query.replace("?", " '" + id + "' ");
        // logger.info("Query ::" + query);
        scopesController.setDBName(dbName);
        JSONObject apiValues = new JSONObject();
        try {
            Field changeMap = apiValues.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(apiValues, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            logger.error(e.getMessage());
        }
        JSONObject apitmp = new JSONObject();
        return jdbcTemplate.query(query, (ResultSet rs) -> {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // logger.info(" Column count " + numColumns);
            while (rs.next()) {
                for (int l = 1; l <= numColumns; l++) {
                    apiValues.put(rsmd.getColumnName(l), rs.getObject(l) == null ? " " : rs.getObject(l).toString());
                }
            }
            apitmp.put(header, apiValues);
            return apitmp;
        });
    }

    public String getadditionMappedQueryByApiAndParent(String apiName, String filterName) {
        String query = "  select mapped_tables_values from api_table_mapping where api_name = '" + apiName
                + "' and parent = (  select  concat (parent, '_addition' )  from api_parent_child_object_mapping  where api_name = '"
                + apiName + "' and  parameter_name = '" + filterName + "'  and searchable ='Y'  )  ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rset) -> {
            String data = null;
            while (rset.next()) {
                data = rset.getString(1) + " ";
            }
            return data;
        });
    }

    public Map apiObjectValueDetails(String apiName, Map<String, String> parentChildmap,
            Map<String, String> childSubChildMap) {
        String query = "select a.parent , array_agg( a.child) as child , a.sub_child_exist from  ( select parent ,child,sub_child_exist from  api_parent_child_object_mapping  where api_name = '"
                + apiName + "'  order by show_order ) a  group by a.parent ,a.sub_child_exist ";
        logger.info("  [" + query + " ] ");
        Map<String, String> map = new HashMap<>();
        return jdbcTemplate.query(query, (ResultSet rs) -> {
            String data = null;
            while (rs.next()) {
                data = rs.getString("parent") + " :" + rs.getString("sub_child_exist") + ":";
                if (rs.getString("sub_child_exist").equals("Y")) {
                    childSubChildMap.put(rs.getString("parent"),
                            rs.getString("child").replace("{", "").replace("}", ""));
                }
                if (rs.getString("sub_child_exist").equals("N")) {
                    parentChildmap.put(rs.getString("parent"), rs.getString("child").replace("{", "").replace("}", ""));
                }
            }
            return map;
        });

    }

    public void getObjectQueries(String apiName, Map<String, String> parentChildMap,
            Map<String, String> apiObjectQueries) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : parentChildMap.entrySet()) {
            if (!entry.getKey().equals("data")) {
                String header = entry.getKey();
                String query = "Select "
                        + genericFunctions.removeLastChars(getParammeterValuesFromApiTable1(map, apiName, header), 1)
                        + " " + getExtendedQueryFromDB(apiName, header); // get main Table
                apiObjectQueries.put(header, query);
            }
        }

    }

    public String getColumnNameByParent(String apiName, String parameter_name) {
        String query = "    select   table_name , child   from api_parent_child_object_mapping  where api_name = '"
                + apiName + "' and   parameter_name  = '" + parameter_name + "'   ";
        logger.info("  [" + query + " ] ");
        return jdbcTemplate.query(query, (ResultSet rset) -> {
            String data = null;
            while (rset.next()) {
                data = rset.getString("table_name") + "." + rset.getString("child");
            }
            return data;
        });
    }

}
