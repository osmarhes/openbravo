

package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.CategoryInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 */
public class CategoryDAO extends BaseJdbcDAO {

    public CategoryDAO(Connection connection) {
        super(connection);
    }
    
    public List<CategoryInfo> findAllCategories() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryInfo> vos = null;
        String sqlStr = "select * from CATEGORIES where PARENTID IS NULL order by NAME";

        try {

            //prepare statement
            ps = getStatement(sqlStr);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            vos = transformSet(rs);
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

        return vos;
    }

    public List<CategoryInfo> findAllSubcategories(String id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryInfo> vos = null;
        String sqlStr = "select * from CATEGORIES where PARENTID = ? order by NAME";

        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, id);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            vos = transformSet(rs);
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

        return vos;
    }

    public String findFirstCategory() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryInfo> vos = null;
        String sqlStr = "select * from CATEGORIES where PARENTID IS NULL order by NAME";

        try {

            //prepare statement
            ps = getStatement(sqlStr);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            vos = transformSet(rs);
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

        return vos.get(0).getId();
    }

    @Override
    protected CategoryInfo map2VO(ResultSet rs) throws SQLException {
        CategoryInfo category = new CategoryInfo();
        category.setId(rs.getString("id"));
        category.setParentid(rs.getString("parentid"));
        category.setName(rs.getString("name"));

        return category;
    }
}
