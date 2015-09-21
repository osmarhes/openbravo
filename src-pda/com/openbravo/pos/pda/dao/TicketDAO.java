package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.TicketInfo;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaroslawwozniak
 */
public class TicketDAO extends BaseJdbcDAO implements Serializable {

    public TicketDAO(Connection connection){
        super(connection);
    }
    
    public TicketInfo getTicket(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlStr = "Select CONTENT from SHAREDTICKETS where ID = ?";
        TicketInfo ticket = new TicketInfo();
        try {
            //prepare statement
            ps = getStatement(sqlStr);

            ps.setString(1, id);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            rs.next();
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(rs.getBinaryStream(1)));
            ticket = (TicketInfo) in.readObject();

        } catch (Exception ex) {
            //Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return ticket;
    }

    public void initTicket(String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT) VALUES (?, ?, ?)";
        TicketInfo ticket = new TicketInfo();
        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, id);
            ps.setString(2, ticket.getName());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(ticket);
            ps.setBytes(3, bytes.toByteArray());
            //execute
            ps.executeUpdate();

        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTicket(String ticketId, TicketInfo ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "UPDATE SHAREDTICKETS SET CONTENT = ? WHERE ID = ?";
        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(ticket);
            ps.setBytes(1, bytes.toByteArray());
            ps.setString(2, ticketId);
            //execute
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertTicket(TicketInfo ticket) {

        Connection con = null;
        PreparedStatement ps = null;
        String sqlStr = "INSERT INTO SHAREDTICKETS (ID, NAME, CONTENT) VALUES (?, ?, ?)";

        try {
            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, ticket.getM_sId());
            ps.setString(2, ticket.getName());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(ticket);
            ps.setBytes(3, bytes.toByteArray());

            //execute
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTicket(String id) {
        PreparedStatement ps = null;
        String sqlStr = "DELETE FROM SHAREDTICKETS WHERE ID = ?";
        try {
            //prepare statement
            ps = getStatement(sqlStr);

            ps.setString(1, id);
            //execute
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected TicketInfo map2VO(ResultSet rs) throws SQLException {
        TicketInfo ticket = new TicketInfo();

        return ticket;

    }
}
