/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

/**
 *
 * @author maverick
 */
public class Application {

//    java  -Dlog4j.configuration=file:./conf/log4j.properties -cp ./lib/*:./CdrProcessses.jar com.gl.FileScpProcess.Application CP1 smart sm_msc01
    
    public static void main(String[] args) {
        String process_parameter = args[0];
        String operator_parameter = args[1];
        String source_parameter = args[2];
        if (process_parameter != null && operator_parameter != null && source_parameter != null) {
            processMethod(process_parameter, operator_parameter, source_parameter);
        } else {
            System.out.println("Error: pass correct argument to run application.");
        }
        System.exit(0);
    }

    private static void processMethod(String process_parameter, String operator_parameter, String source_parameter) {

        switch (process_parameter) {
                case "CP1":    new com.gl.FileScpProcess.CP1FileTransfer().cp1(operator_parameter, source_parameter);
                break;
                case "CP2":  new com.gl.FileScpProcess.CP2FileTransfer().cp2(operator_parameter,source_parameter);break;
		case "CP3":  new com.gl.FileScpProcess.CP3FileTransfer().cp3(operator_parameter,source_parameter);break;
		default :   System.out.println("Method doesn't exist");break;
        }
    }
}
