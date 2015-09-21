/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.dao;

import com.openbravo.pos.pda.util.PropertyUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author osmar
 */
public class ConnectionFactory {

    private final PropertyUtils properties;

    public ConnectionFactory() {
        properties = new PropertyUtils();
    }

    public Connection getConnection(){
        try {
            Class.forName(properties.getDriverName());
            return DriverManager.getConnection(properties.getUrl(), properties.getDBUser(), properties.getDBPassword());
        } catch (ClassNotFoundException sqlex) {
            throw new RuntimeException(sqlex);
        } catch (SQLException sqlex) {
            throw new RuntimeException(sqlex);
        }
    }
}
