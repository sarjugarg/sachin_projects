package com.giftingnetwork.api;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// import org.apache.tomcat.util.buf.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JavaJsonEncoder {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    XmlDomHandler xmlDomHandler;

    Logger logger = LoggerFactory.getLogger(JavaJsonEncoder.class);

    public String toDocument(ResultSet rs, LinkedHashMap<String, String> map, String query) {

        try {
            JSONArray jsonArr = new JSONArray();
            String Offset = query.substring(query.indexOf("OFFSET") + 6, query.indexOf("OFFSET") + 11);
            String limit = query.substring(query.indexOf("LIMIT") + 5, query.indexOf("LIMIT") + 9);
            logger.info("Offset " + Offset);
            logger.info("limit " + limit);
            String totalCount = loginRepository.getTotalCountFromDb(query);
            int pageSize = ((Integer.parseInt(Offset.trim()) / Integer.parseInt(limit.trim())) + 1);

            Map<String, String> childMap = new HashMap<String, String>();

            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            logger.info(" Column count for Query " + numColumns);

            while (rs.next()) {
                JSONObject finalObj = new JSONObject();
                logger.info(" rsmd  Count " + numColumns);

                for (int i = 1; i <= numColumns; i++) {
                    childMap.put(rsmd.getColumnName(i), rs.getObject(i) == null ? " " : rs.getObject(i).toString());
                }
                logger.info(" ChildMap Done ");
                for (Map.Entry<String, String> entry : childMap.entrySet()) {
                    logger.info(" childMap :::::::: Key = " + entry.getKey() + "  Value = " + entry.getValue());
                }

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    logger.info("Key = " + entry.getKey() + "  Value = " + entry.getValue());
                    String[] arr = entry.getValue().split(",");
                    String paramValues = "";
                    for (int i = 0; i < arr.length; i++) {
                        logger.info(" Inside array:: " + arr[i].substring(arr[i].lastIndexOf(".") + 1) + " value  : "
                                + childMap.get(arr[i].substring(arr[i].lastIndexOf(".") + 1)));
                        paramValues = paramValues + childMap.get(arr[i].substring(arr[i].lastIndexOf(".") + 1)) + "";
                    }
                    logger.info("Attributes for " + entry.getKey() + " are :" + paramValues);
                    finalObj.put(entry.getKey(), paramValues);
                }
                logger.info(" Details Provide ");
                jsonArr.put(finalObj);
            }
            JSONObject result = new JSONObject();
            result.put("page_number", pageSize);
            result.put("page_size", limit.trim());
            result.put("total_rows", totalCount);

            JSONObject recordSet = new JSONObject();
            recordSet.put("record", jsonArr);

            JSONObject jsonresult = new JSONObject();

            jsonresult.put("recordset", recordSet);
            jsonresult.put("paging", result);

            JSONObject status = new JSONObject();
            status.put("type", "success");

            JSONObject data = new JSONObject();
            data.put("status", status);
            data.put("result", jsonresult);

            JSONObject command = new JSONObject();  
            command.put("data", data);

            String message = command.toString();
            return message;
        } catch (Exception e) {
            logger.error(e.getCause() + " :error: " + e.getMessage());
            return  xmlDomHandler.genericErrorMethod("Server Error", "Internal Error");
  
        }
    }

}
// for (int i = 1; i < numColumns + 1; i++) {
// String column_name = rsmd.getColumnName(i);
// logger.info(" Type " + column_name + " ; " + rsmd.getColumnType(i));
// if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
// obj.put(column_name, rs.getArray(column_name) == null ? "" :
// rs.getArray(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
// obj.put(column_name, rs.getInt(column_name) == 0 ? "0" :
// rs.getInt(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
// obj.put(column_name, rs.getBoolean(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
// obj.put(column_name, rs.getBlob(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
// obj.put(column_name, rs.getDouble(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
// obj.put(column_name, rs.getFloat(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
// obj.put(column_name, rs.getInt(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
// obj.put(column_name, rs.getNString(column_name) == null ? "" :
// rs.getNString(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
// obj.put(column_name, rs.getString(column_name) == null ? "" :
// rs.getString(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
// obj.put(column_name, rs.getInt(column_name) == 0 ? "" :
// rs.getInt(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
// obj.put(column_name, rs.getInt(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
// obj.put(column_name, rs.getDate(column_name) == null ? "" :
// rs.getDate(column_name) );
// } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
// obj.put(column_name, rs.getTimestamp(column_name) == null ? "" :
// rs.getTimestamp(column_name));
// } else {
// obj.put(column_name, rs.getObject(column_name) == null ? "" :
// rs.getObject(column_name) );
// }
// logger.info(" Result :::::: " + column_name + " ; " + obj.get(column_name));
// }
// json.put(obj);