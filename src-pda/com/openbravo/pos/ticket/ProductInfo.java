package com.openbravo.pos.ticket;

import com.openbravo.pos.pda.util.FormatUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author jaroslawwozniak
 */
public class ProductInfo implements Serializable {

    private String id;
    private String ref;
    private String code;
    private String name;
    private boolean com;
    private boolean scale;
    private String categoryId;
    private String taxcat;
    private double priceBuy;
    private double priceSell;
    private Properties attributes;
    private boolean special;

    public String getTaxcat() {
        return taxcat;
    }

    public void setTaxcat(String taxcat) {
        this.taxcat = taxcat;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryID) {
        this.categoryId = categoryID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCom() {
        return com;
    }

    public void setCom(boolean com) {
        this.com = com;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(double priceBuy) {
        this.priceBuy = priceBuy;
    }

    public double getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(double priceSell) {
        this.priceSell = priceSell;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isScale() {
        return scale;
    }

    public String printPriceSell() {
        return FormatUtils.formatCurrency(priceSell);
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public Properties getAttributes() {
        return attributes;
    }

    public void setAttributes(byte[] attributes) {
        Properties prop = null;
        try {
            if (attributes != null) {
                prop = new Properties();
                prop.loadFromXML(new ByteArrayInputStream(attributes));
            }
        } catch (IOException e) {
        }
        this.attributes = prop;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
    
    
}
