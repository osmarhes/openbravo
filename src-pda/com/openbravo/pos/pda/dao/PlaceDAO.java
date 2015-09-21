package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.Place;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jaroslawozniak
 */
public class PlaceDAO extends BaseJdbcDAO {

    public PlaceDAO(Connection connection) {
        super(connection);
    }
    
    public List<Place> findAllPlaceByFloor(String floorId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Place> vos = null;
        String sqlStr = "Select * from PLACES where Floor = ? order by NAME";

        try {

            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, floorId);
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

    public List<Place> findAllBusyPlacesByFloor(String floorId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Place> vos = null;
        String sqlStr = "SELECT * FROM PLACES P, SHAREDTICKETS S WHERE FLOOR = ? AND P.ID = S.ID";

        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, floorId);
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

    public Place findPlaceById(String placeId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Place vo = null;
        String sqlStr = "Select * from PLACES where ID = ?";

        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, placeId);
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

    public void setTableBusy(String placeId, String placeName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "INSERT INTO SHAREDTICKETS (ID, NAME) VALUES (?, ?)";

        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, placeId);
            ps.setString(2, placeName);
            //execute
            rs = ps.executeQuery();

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

    }

    @Override
    protected Place map2VO(ResultSet rs) throws SQLException {

        Place place = new Place();
        place.setId(rs.getString("id"));
        place.setName(rs.getString("name"));
        place.setX(rs.getInt("x"));
        place.setY(rs.getInt("y"));
        place.setFloor(rs.getString("floor"));

        return place;
    }
}
