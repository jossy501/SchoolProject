/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.etzrica.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class Util {

    private static String url;
    private static String username;
    private static String password;
    private static String driver_class;
    private static String dialect;
    public EntityManager getEntitymanager() {
//        Map props = new HashMap();
//        Properties prop = new Properties();
//         try {
//            prop.load(new FileInputStream(new File("C:\\cfg\\rica.cfg")));
//            url = prop.getProperty("dburl");
//            username = prop.getProperty("dbusername");
//            password = prop.getProperty("dbpassword");
//            driver_class = prop.getProperty("dbdriver_class");
//            dialect = prop.getProperty("hibernate_dialect");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        props.put("hibernate.connection.url", url);
//            props.put("hibernate.connection.username", username);
//            props.put("hibernate.connection.password", password);
//            props.put("hibernate.connection.driver_class", driver_class);
//            props.put("hibernate.dialect", dialect);

        return Persistence.createEntityManagerFactory("XPricaPU").createEntityManager();
    }


}
