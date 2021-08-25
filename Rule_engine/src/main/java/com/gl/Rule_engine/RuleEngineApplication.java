package com.gl.Rule_engine;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.io.BufferedWriter;
import org.apache.log4j.Logger;

public class RuleEngineApplication {

    static final Logger logger = Logger.getLogger(RuleEngineApplication.class);

    public static String startRuleEngine(String[] args, Connection conn, BufferedWriter bw) {
        String reslt = "";

        if ("1".equalsIgnoreCase(args[1])) {
            if ("LBD".equalsIgnoreCase(args[0])) {
                reslt = LBD.executeRule(args, conn);
            }
            if ("IMEI_LENGTH".equalsIgnoreCase(args[0])) {
                reslt = IMEI_LENGTH.executeRule(args, conn);
            }
            if ("EXISTS_IN_GSMA_TAC_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_GSMA_TAC_DB.executeRule(args, conn);
            }
            if ("IMEI_LUHN_CHECK".equalsIgnoreCase(args[0])) {
                reslt = IMEI_LUHN_CHECK.executeRule(args, conn);
            }
            if ("EXISTS_IN_TYPE_APPROVED_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_TYPE_APPROVED_DB.executeRule(args, conn);
            }
            if ("EXISTS_IN_USAGE_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_USAGE_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_IMPORTER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_IMPORTER_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_DISTRIBUTOR_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_DISTRIBUTOR_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_RETAILER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_RETAILER_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_MANUFACTURER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_MANUFACTURER_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_CUSTOM_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_CUSTOM_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_END_USER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_END_USER_DB.executeRule(args, conn);
            }
            if ("EXISTS_IN_REGULARIZED_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_REGULARIZED_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_TAX_PAID_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_TAX_PAID_DB.executeRule(args, conn);
            }
            if ("EXIST_REGULARIZED".equalsIgnoreCase(args[0])) {
                reslt = EXIST_REGULARIZED.executeRule(args, conn);
            }
            if ("EXIST_IN_VIP_LIST".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_VIP_LIST.executeRule(args, conn);
            }
            if ("DUPLICATE_USAGE_CHECK".equalsIgnoreCase(args[0])) {
                reslt = DUPLICATE_USAGE_CHECK.executeRule(args, conn);
            }
            if ("EXIST_IN_BLACKLIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_BLACKLIST_DB.executeRule(args, conn);
            }
            if ("IMEI_NULL".equalsIgnoreCase(args[0])) {
                reslt = IMEI_NULL.executeRule(args, conn);
            }
            if ("EXISTS_IN_GREYLIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_GREYLIST_DB.executeRule(args, conn);
            }
            if ("SYS_REG".equalsIgnoreCase(args[0])) {
                reslt = SYS_REG.executeRule(args, conn);
            }
            if ("USER_REG".equalsIgnoreCase(args[0])) {
                reslt = USER_REG.executeRule(args, conn);
            }
            if ("EXISTS_IN_FOREIGN_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_FOREIGN_DB.executeRule(args, conn);
            }
            if ("EXISTS_IN_WHITELIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_WHITELIST_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_GSMABLACKLIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_GSMABLACKLIST_DB.executeRule(args, conn);
            }
            if ("FOREIGN_SIM".equalsIgnoreCase(args[0])) {
                reslt = FOREIGN_SIM.executeRule(args, conn);
            }
            if ("TAC_FORMAT".equalsIgnoreCase(args[0])) {
                reslt = TAC_FORMAT.executeRule(args, conn);
            }

            if ("EXISTS_IN_TYPE_APPROVED_TAC".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_TYPE_APPROVED_TAC.executeRule(args, conn);
            }

            if ("SAME_OPERATOR_UNBLOCK".equalsIgnoreCase(args[0])) {
                reslt = SAME_OPERATOR_UNBLOCK.executeRule(args, conn);
            }

            if ("EXIST_IN_LAWFUL_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_LAWFUL_DB.executeRule(args, conn);
            }

            if ("EXIST_IN_OPERATOR_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_OPERATOR_DB.executeRule(args, conn);
            }
            if ("EXISTS_IN_ALL_ACTIVE_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_ALL_ACTIVE_DB.executeRule(args, conn);
            }
            if ("EXIST_IN_END_USER_DEVICE_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_END_USER_DEVICE_DB.executeRule(args, conn);
            }

            if ("SAME_DEVICETYPE_RECOVERY".equalsIgnoreCase(args[0])) {
                reslt = SAME_DEVICETYPE_RECOVERY.executeRule(args, conn);
            }
            if ("SAME_DEVICETYPE_UNBLOCK".equalsIgnoreCase(args[0])) {
                reslt = SAME_DEVICETYPE_UNBLOCK.executeRule(args, conn);
            }
            if ("SAME_SERIAL_RECOVERY".equalsIgnoreCase(args[0])) {
                reslt = SAME_SERIAL_RECOVERY.executeRule(args, conn);
            }
            if ("SAME_SERIAL_UNBLOCK".equalsIgnoreCase(args[0])) {
                reslt = SAME_SERIAL_UNBLOCK.executeRule(args, conn);
            }

        }

        if ("2".equalsIgnoreCase(args[1])) {

            if ("LBD".equalsIgnoreCase(args[0])) {
                reslt = LBD.executeAction(args, conn, bw);
            }
            if ("IMEI_LENGTH".equalsIgnoreCase(args[0])) {
                reslt = IMEI_LENGTH.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_GSMA_TAC_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_GSMA_TAC_DB.executeAction(args, conn, bw);
            }
            if ("IMEI_LUHN_CHECK".equalsIgnoreCase(args[0])) {
                reslt = IMEI_LUHN_CHECK.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_TYPE_APPROVED_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_TYPE_APPROVED_DB.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_USAGE_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_USAGE_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_IMPORTER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_IMPORTER_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_DISTRIBUTOR_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_DISTRIBUTOR_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_RETAILER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_RETAILER_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_MANUFACTURER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_MANUFACTURER_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_CUSTOM_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_CUSTOM_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_END_USER_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_END_USER_DB.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_REGULARIZED_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_REGULARIZED_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_TAX_PAID_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_TAX_PAID_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_REGULARIZED".equalsIgnoreCase(args[0])) {
                reslt = EXIST_REGULARIZED.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_VIP_LIST".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_VIP_LIST.executeAction(args, conn, bw);
            }
            if ("DUPLICATE_USAGE_CHECK".equalsIgnoreCase(args[0])) {
                reslt = DUPLICATE_USAGE_CHECK.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_BLACKLIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_BLACKLIST_DB.executeAction(args, conn, bw);
            }
            if ("IMEI_NULL".equalsIgnoreCase(args[0])) {
                reslt = IMEI_NULL.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_GREYLIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_GREYLIST_DB.executeAction(args, conn, bw);
            }
            if ("SYS_REG".equalsIgnoreCase(args[0])) {
                reslt = SYS_REG.executeAction(args, conn, bw);
            }
            if ("USER_REG".equalsIgnoreCase(args[0])) {
                reslt = USER_REG.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_FOREIGN_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_FOREIGN_DB.executeAction(args, conn, bw);
            }

            if ("EXISTS_IN_WHITELIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_WHITELIST_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_GSMABLACKLIST_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_GSMABLACKLIST_DB.executeAction(args, conn, bw);
            }
            if ("FOREIGN_SIM".equalsIgnoreCase(args[0])) {
                reslt = FOREIGN_SIM.executeAction(args, conn, bw);
            }
            if ("TAC_FORMAT".equalsIgnoreCase(args[0])) {
                reslt = TAC_FORMAT.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_TYPE_APPROVED_TAC".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_TYPE_APPROVED_TAC.executeAction(args, conn, bw);
            }
            if ("SAME_OPERATOR_UNBLOCK".equalsIgnoreCase(args[0])) {
                reslt = SAME_OPERATOR_UNBLOCK.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_LAWFUL_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_LAWFUL_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_OPERATOR_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_OPERATOR_DB.executeAction(args, conn, bw);
            }
            if ("EXISTS_IN_ALL_ACTIVE_DB".equalsIgnoreCase(args[0])) {
                reslt = EXISTS_IN_ALL_ACTIVE_DB.executeAction(args, conn, bw);
            }
            if ("EXIST_IN_END_USER_DEVICE_DB".equalsIgnoreCase(args[0])) {
                reslt = EXIST_IN_END_USER_DEVICE_DB.executeAction(args, conn, bw);
            }
             if ("SAME_DEVICETYPE_RECOVERY".equalsIgnoreCase(args[0])) {
                reslt = SAME_DEVICETYPE_RECOVERY.executeAction(args, conn,bw);
            }
            if ("SAME_DEVICETYPE_UNBLOCK".equalsIgnoreCase(args[0])) {
                reslt = SAME_DEVICETYPE_UNBLOCK.executeAction(args, conn,bw);
            }
            if ("SAME_SERIAL_RECOVERY".equalsIgnoreCase(args[0])) {
                reslt = SAME_SERIAL_RECOVERY.executeAction(args, conn,bw);
            }
            if ("SAME_SERIAL_UNBLOCK".equalsIgnoreCase(args[0])) {
                reslt = SAME_SERIAL_UNBLOCK.executeAction(args, conn,bw);
            }
            
            
        }

        return reslt;
    }

}

//Rule Engine for different purposes..
// ....   19	Consignment	IMEI is already in use in the network	con_failed_rule.csv	Fail	CON_RULE_0001	IMEI is already in use in the network    device_usages _db
//            20	Consignment	IMEI is marked as stolen	con_failed_rule.csv	Fail	CON_RULE_0002	IMEI is marked as stolen   // lbd
//              21	Consignment	TAC in the IMEI is not approved TAC from GSMA	con_failed_rule.csv	Fail	CON_RULE_0003	TAC in the IMEI is not approved TAC from GSMA  // exit in tac_db
//   	22	Consignment	TAC in IMEI is not approved TAC from TRC	con_failed_rule.csv	Fail	CON_RULE_0004	TAC in IMEI is not approved TAC from TRC     // blcklist_db
//	23	Consignment	IMEI is null	con_failed_rule.csv	Fail	CON_RULE_0005	IMEI is null
//	24	Consignment	IMEI is not as per the length and specification	con_failed_rule.csv	Fail	CON_RULE_0006	IMEI is not as per the length and specification
//25	Consignment	IMEI does not pass the checksum test  	con_failed_rule.csv	Fail	CON_RULE_0007	IMEI does not pass the checksum test  

