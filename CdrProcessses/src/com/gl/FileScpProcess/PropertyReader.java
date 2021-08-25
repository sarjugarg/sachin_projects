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
  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyReader {

    private InputStream inputStream;
    Properties prop;
    static Logger logger = Logger.getLogger(PropertyReader.class);

    public PropertyReader() {
        loadProperties();
    }

    Properties loadProperties() {
        try {
            prop = new Properties();
            String currentDirectory = System.getProperty("user.dir");
            String propFileName = currentDirectory + "/conf/conf.properties";
            inputStream = new FileInputStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (IOException io) {
            logger.error(io.toString(), (Throwable) io);
            //System.exit(-1);
        }
        return prop;
    }

    public String getPropValue(String key) throws IOException {
        prop = loadProperties();
        if (Objects.nonNull(prop)) {
            String value = prop.getProperty(key);
            return value;
        } else {
            return null;
        }
    }

     public void closePropStream() {
        try {
            inputStream.close();
        } catch (IOException io) {
            logger.error(io.toString(), (Throwable) io);
            //System.exit(-1);

        }

    }
}
 
