package com.giftingnetwork.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test1 {

    public static void mainc(String[] args) {


        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 30);
       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
     //   SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        
        System.out.println(df.format(now.getTime()));



        String inputString = "sachin"; // get user input
        String outputString = ""+inputString.charAt(0);
        for (int i = 1; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            outputString += Character.isUpperCase(c) ? "_" + c : c;
        }
        System.out.println(outputString);

    }

    public void main(String args) {

        // String xmlStr = " <SqlCommand><Where> <Field Name=\"LastUpdated\"
        // Operator=\"&lt;\">2020-05-24</Field> <Field Name=\"LastCreated\"
        // Operator=\"&gt;\">2020-05-11</Field> </Where><Paging page_number=\"1\"
        // page_size=\"3\" order_by=\"CreatedOn\" /><Select><Field Name=\"Name\"/>
        // <Field Name=\"CreatedOn\"/><Field Name=\"EIN\"/><Field
        // Name=\"Status\"/><Field Name=\"GuidestarLink\"/><Field
        // Name=\"IRSName\"/><Field Name=\"LastUpdated\"/><Field
        // Name=\"Mission\"/><Field Name=\"Email\"/><Field Name=\"Programs\"/><Field
        // Name=\"WebSite\"/></Select></SqlCommand> ";

        // String jsn = XML.toJSONObject(xmlStr).toString();

        System.out.println(args);
        System.out.println("###############");
        System.out.println("");

        JSONObject json = new JSONObject(args);
        printJsonObject(json);
        System.out.println("*************");
        System.out.println("*************");
        System.out.println("*************");

        JSONObject selectBlock = json.getJSONObject("SqlCommand").optJSONObject("Select");
        if (selectBlock instanceof JSONObject) {
            JSONArray selectBlockMulti = selectBlock.optJSONArray("Field");
            if (selectBlockMulti instanceof JSONArray) {
                for (int i = 0; i < selectBlockMulti.length(); i++) {
                    System.out.println(selectBlockMulti.getJSONObject(i).getString("Name"));
                }
            } else {
                System.out.println(selectBlock.optJSONObject("Field").getString("Name"));
            }
        }

        JSONObject whereBlock = json.getJSONObject("SqlCommand").optJSONObject("Where");
        if (whereBlock instanceof JSONObject) {
            JSONArray whereBlockMulti = whereBlock.optJSONArray("Field");
            if (whereBlockMulti instanceof JSONArray) {
                for (int i = 0; i < whereBlockMulti.length(); i++) {
                    System.out.println(whereBlockMulti.getJSONObject(i).getString("Name"));
                    System.out.println(whereBlockMulti.getJSONObject(i).getString("Operator"));
                    System.out.println(whereBlockMulti.getJSONObject(i).getString("Content"));
                }
            } else {
                System.out.println(whereBlock.optJSONObject("Field").getString("Name"));
                System.out.println(whereBlock.optJSONObject("Field").getString("Operator"));
                System.out.println(whereBlock.optJSONObject("Field").getString("Content"));
            }
        }

        JSONObject pagingBlock = json.getJSONObject("SqlCommand").optJSONObject("Paging");
        if (pagingBlock instanceof JSONObject) {
            System.out.println(pagingBlock.getString("page_number"));
            System.out.println(pagingBlock.getString("page_size"));
            System.out.println(pagingBlock.getString("order_by"));
        }

        JSONArray selectArr = json.getJSONObject("SqlCommand").getJSONObject("Select").getJSONArray("Field");
        for (int i = 0; i < selectArr.length(); i++) {
            System.out.println(selectArr.getJSONObject(i).getString("Name"));
        }

        System.out.println("$$$$$$$$$$$$$$$");

        JSONArray whereArr = json.getJSONObject("SqlCommand").optJSONObject("Where").getJSONArray("Field");
        if (whereArr instanceof JSONArray) {
            System.out.println("   arr  Loop ");
            // for (int i = 0; i < arr.length(); i++) {
            // System.out.println(arr.getJSONObject(i).getString("Name"));
            // System.out.println(arr.getJSONObject(i).getString("Operator"));
            // System.out.println(arr.getJSONObject(i).getString("content"));
            // }
        }
        JSONObject whereObj = json.getJSONObject("SqlCommand").getJSONObject("Where").optJSONObject("Field");
        if (whereObj instanceof JSONObject) {
            System.out.println(" inside  Loop ");
        }

    }

    public static void printJsonObject(JSONObject jsonObj) {
        jsonObj.keySet().forEach(keyStr -> {
            Object keyvalue = jsonObj.get(keyStr);
            System.out.println("key: " + keyStr + " value: " + keyvalue);

            if (keyvalue instanceof JSONObject) {
                System.out.println(" inside  Loop ");
                printJsonObject((JSONObject) keyvalue);
            }
        });
    }

    public static void readFromJsonFile(String text) {
        try {
            JSONObject obj = new JSONObject(text);
            JSONArray arr = obj.getJSONArray("Select");
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).getString("Field");
                System.out.println(" Value :: " + name);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

}

// JSONArray whereBlockMulti = whereBlock.optJSONArray("Field");
// if (whereBlockMulti instanceof JSONArray) {
// for (int i = 0; i < whereBlockMulti.length(); i++) {
// System.out.println(whereBlockMulti.getJSONObject(i).getString("Name"));
// System.out.println(whereBlockMulti.getJSONObject(i).getString("Operator"));
// System.out.println(whereBlockMulti.getJSONObject(i).getString("Content"));
// }
// } else

// String mess;
// JSONObject data1 = new JSONObject();
// JSONObject status = new JSONObject();
// JSONObject ite = new JSONObject();
// ite.put("code", "code");
// ite.put("message", "message");
// ite.put("refcode", "refCode");
// ite.put("type", "Error");
// status.put("status", ite);
// data1.put("data", status);
// mess = data1.toString();

// System.out.println( " ******* " + mess);
// System.out.println( " ******* ") ;

// String message;
// JSONObject data = new JSONObject();
// JSONObject result = new JSONObject();
// JSONObject recordSet = new JSONObject();
// JSONObject record = new JSONObject();
// JSONObject item = new JSONObject();
// item.put("Status", "test");
// item.put("UserSessionID", "45454545455");
// record.put("record" ,item);

// recordSet.put("recordSet" ,record);

// recordSet.put("page_number", "1");
// result.put("result" ,recordSet);

// data.put("status","" ); // data
// data.put("data",result );

// message = data.toString();

// System.out.println( " " + message ) ;

// System.out.println ("");
// System.out.println ("");
// System.out.println ("");
// System.out.println ("");

// String str = "
// {\"SqlCommand\":{\"Select\":{\"Field\":[{\"_Name\":\"Name\"},{\"_Name\":\"CreatedOn\"},{\"_Name\":\"EIN\"},{\"_Name\":\"Status\"},{\"_Name\":\"GuidestarLink\"},{\"_Name\":\"IRSName\"},{\"_Name\":\"LastUpdated\"},{\"_Name\":\"Mission\"},{\"_Name\":\"Email\"},{\"_Name\":\"Programs\"},{\"_Name\":\"WebSite\"}]},\"Where\":{\"Field\":[{\"_Name\":\"LastUpdated\",\"_Operator\":\"<\",\"__text\":\"2020-05-24\"},{\"_Name\":\"LastCreated\",\"_Operator\":\">=\",\"__text\":\"2020-05-24\"}]},\"Paging\":{\"_page_number\":\"1\",\"_page_size\":\"3\",\"_order_by\":\"CreatedOn\"}}}
// ";

// String json =
// "{\"name\":\"JSON\",\"integer\":1,\"double\":2.0,\"boolean\":true,\"nested\":{\"id\":42},\"array\":[1,2,3]}";
// JSONObject json = new JSONObject(str);
// String xml = XML.toString(json);
// System.out.println(xml);

// String xml = U.jsonToXml(str);
// System.out.println(xml);

// String jsn =
// "{\"SqlCommand\":{\"Paging\":{\"page_size\":3,\"order_by\":\"CreatedOn\",\"page_number\":1},\"Select\":{\"Field\":[{\"Name\":\"Name\"},{\"Name\":\"CreatedOn\"},{\"Name\":\"EIN\"},{\"Name\":\"Status\"},{\"Name\":\"GuidestarLink\"},{\"Name\":\"IRSName\"},{\"Name\":\"LastUpdated\"},{\"Name\":\"Mission\"},{\"Name\":\"Email\"},{\"Name\":\"Programs\"},{\"Name\":\"WebSite\"}]},\"Where\":{\"Field\":{\"Operator\":\"<\",\"content\":\"2020-05-24\",\"Name\":\"LastUpdated\"}}}}";
// String xml = XML.toString(json);
// System.out.println("----------------");
// System.out.println(xml);

// readFromJsonFile(jsn);

// for (int i = 0; i < arr.length(); i++) {
// System.out.println(arr.getJSONObject(i).getString("Name"));
// System.out.println(arr.getJSONObject(i).getString("Operator"));
// System.out.println(arr.getJSONObject(i).getString("content"));
// }

// printJsonObject(a) ;

// .keySet().forEach(keyStr -> {
// Object keyvalue = jsonObj.get(keyStr);
// System.out.println("key-----: " + keyStr + " value: " );
// if (keyvalue instanceof JSONObject) {
// System.out.println(" inside Loop ");
// printJsonObject((JSONObject) keyvalue);
// }
// });
