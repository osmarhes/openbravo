package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 * @version 1.0
 */
public abstract class BaseJdbcDAO {
    private final Connection connection;

    public BaseJdbcDAO(Connection connection) {
        this.connection = connection;
    }

    protected PreparedStatement getStatement(String query) throws Exception {
        return connection.prepareStatement(query);
    }

    protected List transformSet(ResultSet rs) throws SQLException {
        List voList = new ArrayList();
        Object vo;
        while (rs.next()) {
            vo = map2VO(rs);
            voList.add(vo);
        }
        return voList;
    }

    protected abstract Object map2VO(ResultSet rs) throws SQLException;
}