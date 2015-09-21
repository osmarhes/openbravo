

package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.Floor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 */
public class FloorDAO extends BaseJdbcDAO {

    public FloorDAO(Connection connection) {
        super(connection);
    }
    
    public List<Floor> findAllFloors() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Floor> vos = null;
        String sqlStr = "select * from FLOORS order by NAME";

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

    public Floor findFloorById(String floorId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Floor vo = null;
        String sqlStr = "select * from FLOORS where ID = ? order by NAME";

        try {

            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, floorId);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            rs.next();
            vo = map2VO(rs);
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

        return vo;
    }

    @Override
    protected Floor map2VO(ResultSet rs) throws SQLException {

        Floor floor = new Floor();
        floor.setId(rs.getString("id"));
        floor.setImage(rs.getString("image"));
        floor.setName(rs.getString("name"));

        return floor;
    }
}
