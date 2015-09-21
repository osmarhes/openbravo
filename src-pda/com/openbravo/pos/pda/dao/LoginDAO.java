
package com.openbravo.pos.pda.dao;

import com.openbravo.pos.pda.util.StringUtils;
import com.openbravo.pos.ticket.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jaroslawwozniak
 */
public class LoginDAO extends BaseJdbcDAO {

    public LoginDAO(Connection connection) {
        super(connection);
    }
    
    public UserInfo findUser(String login, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfo user = null;
        String sqlStr = "SELECT NAME, APPPASSWORD FROM PEOPLE WHERE NAME = ? AND APPPASSWORD ";
        String end = "";
        if (password.equals("")) {
            end = "IS NULL";
        } else {
            end = " = ?";
        }

        try {
            //prepare statement
            ps = getStatement(sqlStr + end);
            ps.setString(1, login);
            if (!password.equals("")) {
                ps.setString(2, StringUtils.hashString(password));
            }

            //execute
            rs = ps.executeQuery();
            //transform to VO
            user = map2VO(rs);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // close the resources 
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlee) {
                sqlee.printStackTrace();
            }
        }

        return user;
    }

    @Override
    protected UserInfo map2VO(ResultSet rs) throws SQLException {
        UserInfo user = new UserInfo();
        rs.next();
        user.setLogin(rs.getString("name"));
        if (rs.getString("apppassword") == null) {
            user.setPassword("");
        } else {
            user.setPassword(rs.getString("apppassword"));
        }
        return user;
    }
}
