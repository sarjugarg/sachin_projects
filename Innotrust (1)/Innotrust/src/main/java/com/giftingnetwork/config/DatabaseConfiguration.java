package com.giftingnetwork.config;
 

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;


public class DatabaseConfiguration {
    private String url;
    private String username;
    private String driver;
    private String password; 

    public DataSource createDataSource() {
        BasicDataSource dataSource = new  BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password); 
        dataSource.setMaxTotal(100);
        dataSource.setMaxIdle(100);      // 
     //   dataSource.setDefaultQueryTimeout(defaultQueryTimeoutSeconds);  // uery timeout that will be used for Statements created from connections managed by the poo
     //   dataSource.setMaxWaitMillis(maxWaitMillis);     // maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection
        return dataSource;
        
    }

 //   HikariDataSource dataSource1 = new HikariDataSource();
        // DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // dataSource.setDriverClassName(driver);
        // dataSource.setUrl(url);
        // dataSource.setUsername(username);
        // dataSource.setPassword(password); 
       // return dataSource;
 

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}