/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.struts.filter;

import com.openbravo.pos.pda.dao.ConnectionFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author osmar
 */
public class FiltroConexao implements Filter {

    private Connection connection = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        connection = new ConnectionFactory().getConnection();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Conexao Nula");
                connection = new ConnectionFactory().getConnection();
            }

            // "pendura um objeto no Request"
            request.setAttribute("connection", connection);

            chain.doFilter(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(FiltroConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
