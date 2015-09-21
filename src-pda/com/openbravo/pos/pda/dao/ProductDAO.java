package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.ProductInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 */
public class ProductDAO extends BaseJdbcDAO {

    public ProductDAO(Connection connection) {
        super(connection);
    }

    public List<ProductInfo> findAllAuxiliars(String id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductInfo> vos = null;
        String sqlStr = "SELECT * FROM PRODUCTS P, PRODUCTS_COM COM WHERE COM.PRODUCT=? AND P.ID = COM.PRODUCT2 ORDER BY P.NAME";

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

    public List<ProductInfo> findAuxiliarsParent(String id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductInfo> vos = null;
        String sqlStr = "SELECT * FROM PRODUCTS P, PRODUCTS_COM COM WHERE COM.PRODUCT2=? AND P.ID = COM.PRODUCT  ORDER BY P.NAME";

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
    
    
    public ProductInfo findProductById(String productId) {


        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductInfo vos = null;
        String sqlStr = "SELECT * FROM PRODUCTS WHERE ID=?";

        try {

            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, productId);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            vos = (ProductInfo) transformSet(rs).get(0);

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

    public List<ProductInfo> findProductsByCategory(String categoryId) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductInfo> vos = null;
        String sqlStr = "SELECT * FROM PRODUCTS P, PRODUCTS_CAT PCAT WHERE P.CATEGORY= ? AND P.ID = PCAT.PRODUCT" +
                " AND P.ISCOM = 0 ORDER BY PCAT.CATORDER, P.NAME";

        try {

            //prepare statement
            ps = getStatement(sqlStr);
            ps.setString(1, categoryId);
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

    @Override
    protected ProductInfo map2VO(ResultSet rs) throws SQLException {
        ProductInfo product = new ProductInfo();
        product.setId(rs.getString("id"));
        product.setRef(rs.getString("reference"));
        product.setCode(rs.getString("code"));
        product.setName(rs.getString("name"));
        product.setPriceBuy(rs.getDouble("pricebuy"));
        product.setPriceSell(rs.getDouble("pricesell"));
        product.setCategoryId(rs.getString("category"));
        product.setTaxcat(rs.getString("taxcat"));
        product.setCom(rs.getBoolean("iscom"));
        product.setScale(rs.getBoolean("isscale"));
        product.setAttributes(rs.getBytes("attributes"));
        product.setSpecial(rs.getBoolean("special"));
        rs.getInt("special");
        
        return product;
    }
}
