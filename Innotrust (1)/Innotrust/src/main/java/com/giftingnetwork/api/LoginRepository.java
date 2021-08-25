package com.giftingnetwork.api;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginRepository {

    Logger logger = LoggerFactory.getLogger(LoginRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    XmlDomHandler xmlDomHandler;

    public String getUserDetailsFromAuthUser(String clientID, String username, String password) {
        try {
            logger.info("[ SELECT count(*)   FROM auth_user WHERE username=  '" + username + "'  and  password = '"
                    + password + "'   ]");
            return jdbcTemplate.query("SELECT count(*)   FROM auth_user WHERE username=  '" + username
                    + "'  and  password = '" + password + "' ", (ResultSet rs) -> {
                        String data = "";
                        while (rs.next()) {
                            logger.info(" AuthUser authentication result Value " + rs.getString(1));
                            data = rs.getString(1).equals("1") ? "true" : "false";
                        }
                        return data;
                    });
        } catch (Exception e) {
            logger.error("  Error " + e);
            return xmlDomHandler.genericErrorMethod("LoginError", "Internal Error");
        }
    }

    public String checkCurrentSessionInLocalsessionDb(String username, String password) {
        try {
            return jdbcTemplate.query("SELECT init_time   FROM token_db_mapping  WHERE username=  '" + username
                    + "'  and  password = '" + password + "' ", (ResultSet rs) -> {
                        String data = "true";
                        while (rs.next()) {
                            logger.info("Init Value :   " + rs.getString(1));
                            String session_value = BootApplication.LoginSessionTimeOut;
                            DateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                            Date d = null;
                            try {
                                d = formatter.parse(rs.getString(1));
                            } catch (ParseException e) {
                                logger.error("  Error " + e);
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            cal.add(Calendar.MINUTE, Integer.parseInt(session_value));
                            Date date = java.util.Calendar.getInstance().getTime();
                            logger.info("Init time after  " + rs.getString(1) + " mins : " + cal.getTime()
                                    + " . Current Time : " + date + " ");
                            if (cal.getTime().before(date)) {
                                logger.info(" Session Time expire for  " + username);
                                deleteByUserNamePassword(username, password);
                            } else {
                                logger.info(" Session Going On for  " + username);
                                data = "false";
                            }
                        }
                        return data;
                    });
        } catch (Exception e) {
            logger.error("  Error " + e);
            return "false";
        }
    }

    public Boolean savesessioninLocalsessionDb(String sessionId, String clientID, String username, String password,
            String userIp) {
        deleteFromTokenDbByUserNamePassWord(sessionId, clientID, username, password);
        try {
            String query = " insert into token_db_mapping  ( token_id , client_id ,username , password , init_time  ,clientIp )   values(  ? , ? , ? , ? , now() , ? ) ";
            logger.info("[" + query + "]");
            jdbcTemplate.update(query, new Object[] { sessionId, clientID, username, password, userIp });
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

    public String getColumnNameWithParameters(String api_name, String parameter_name) {
        try {
            String query = "SELECT  table_name ,column_name   FROM  api_column_value_mapping WHERE   api_name=  '"
                    + api_name + "'  and   parameter_name = '" + parameter_name + "'     ";
            // logger.info("[ " + query + "]");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                String data = parameter_name;
                while (rs.next()) {
                    // logger. info( " Result for table_name ,column_name [" + rs.getString(1) + "."
                    // + rs.getString(2) + "]");
                    data = rs.getString(1) + "." + rs.getString(2);
                }
                return data;
            });
        } catch (Exception e) {
            logger.error("  Error " + e);
            return xmlDomHandler.genericErrorMethod("LoginError", "Internal Error");
        }
    }

    public String getExtendedQueryFromDB(String api_name) {
        try {
            return jdbcTemplate.query(
                    " select mapped_tables_values from api_table_mapping where api_name=  '" + api_name + "' ",
                    (ResultSet rs) -> {
                        String data = "";
                        while (rs.next()) {
                            data = rs.getString(1);
                        }
                        logger.info("  " + data);
                        return data;
                    });
        } catch (Exception e) {
            logger.error("  Error " + e);
            return xmlDomHandler.genericErrorMethod("LoginError", "Internal Error");
        }
    }

    public String getParammeterValuesFromApiTable(LinkedHashMap<String, String> map, String apiName) {
        try {
            String query = " SELECT parameter_name,table_name, column_name   FROM api_column_value_mapping WHERE api_name =  '"
                    + apiName + "'  ";
            logger.info(" [" + query + " ] ");
            return jdbcTemplate.query(query, (ResultSet rs) -> {
                String value = " ";
                while (rs.next()) {
                    logger.info(" Response Value  " + rs.getString(1));
                    map.put(rs.getString("parameter_name"),
                            rs.getString("table_name") + "." + rs.getString("column_name"));
                    value += rs.getString("table_name") + "." + rs.getString("column_name") + " ,";
                }
                return value;
            });
        } catch (Exception e) {
            logger.error("  Error " + e);
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
            logger.error("  Error " + e);
            return "false";
        }
    }

    public void setMaximumPageSizeConfiguration() {
        try {
            String query = " select config_param , param_value  from config  where config_param = 'MAXIMUM_PAGE_SIZE'  ";
            logger.info("  [" + query + " ] " + jdbcTemplate);
            jdbcTemplate.query(query, (ResultSet rs) -> {
                BootApplication.MaxPageSize = rs.getObject("param_value").toString();
                while (rs.next()) {
                    logger.info("  [" + rs.getString(1) + " ] [" + rs.getString(2) + "]");
                    BootApplication.MaxPageSize = rs.getString("param_value");

                }
            });
            logger.info("  Maximum Page size  :  " + BootApplication.MaxPageSize);
        } catch (Exception e) {
            logger.error("  Error " + e.getMessage());
        }
    }

    public void setSessionTimeOutConfiguration() {
        try {

            String query = " select config_param , param_value  from config  where config_param  = 'SESSION_TIME_OUT'  ";
            logger.info("  [" + query + " ] " + jdbcTemplate);

            jdbcTemplate.query(query, (ResultSet rs) -> {
                logger.info(" Column count for " + rs.getObject("param_value").toString() + " ;;  "
                        + rs.getObject(2).toString());
                BootApplication.LoginSessionTimeOut = rs.getObject("param_value").toString();
            });
        } catch (Exception e) {
            logger.error("  Error " + e.getMessage());
        }
    }

}
// String getSessionTimeOutInMinutes() {
// try {
// String query = " SELECT param_value FROM config WHERE config_param=
// 'sessionTimeOutInMinutes' ";
// logger.info(" [" + query + " ] ");
// return jdbcTemplate.query(query, (ResultSet rs) -> {
// String data = null;
// while (rs.next()) {
// logger. info(" Response Value " + rs.getString(1));
// data = rs.getString(1);
// }
// return data;
// });
// } catch (Exception e) {
// logger.error(" Error " + e);
// return "false";
// }
// }

// public int getAssignedPageSizeLimitValue() {
// try {
// String query = " SELECT param_value FROM config WHERE config_param=
// 'MAXIMUM_PAGE_SIZE' ";
// logger.info(" [" + query + " ] ");
// return jdbcTemplate.query(query, (ResultSet rs) -> {
// int value = 0;
// while (rs.next()) {
// logger. info(" Response Value " + rs.getInt(1));
// value = rs.getInt(1);
// }
// return value;
// });
// } catch (Exception e) {
// logger.error(" Error " + e);
// return 0;
// }
// }
