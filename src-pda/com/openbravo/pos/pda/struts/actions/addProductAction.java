package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.pda.struts.forms.AddedProductForm;
import com.openbravo.pos.ticket.ProductInfo;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

public class addProductAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // buscando a conexão do request
        Connection connection = (Connection) request
                .getAttribute("connection");
        
        AddedProductForm aForm = (AddedProductForm) form;
        RestaurantManager bo = new RestaurantManager(connection);
        String place = aForm.getPlace();
        String productId = aForm.getProductId();

        bo.addLineToTicket(place, productId);
        
        
        
        
        List<ProductInfo> auxiliars = new ArrayList<ProductInfo>();
        auxiliars = bo.findAuxiliars(productId);
        
        if(bo.findProductById(productId).isSpecial()){
            while(!auxiliars.isEmpty()){
                bo.addLineToTicket(place, auxiliars.get(0).getId());
                auxiliars.remove(0);
            }
        }
        request.setAttribute("place", place);
        request.setAttribute("auxiliars", auxiliars);
        request.setAttribute("rates", bo.findAllTaxRatesByCategory(auxiliars));

        return mapping.findForward(SUCCESS);
    }
}
