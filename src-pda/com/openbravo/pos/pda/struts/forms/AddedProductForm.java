
package com.openbravo.pos.pda.struts.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author jaroslawwozniak
 */
public class AddedProductForm extends org.apache.struts.action.ActionForm {

    private String floorId = "";
    private String place = "";
    private String productId = "";
    private String[] parameters;

    /**
     *
     */
    public AddedProductForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String cat) {
        this.productId = cat;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
}
