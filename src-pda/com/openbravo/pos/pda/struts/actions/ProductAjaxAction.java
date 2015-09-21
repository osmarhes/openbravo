package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author jaroslawwozniak
 */
public class ProductAjaxAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String NEXTLEVEL = "nextlevel";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // buscando a conexão do request
        Connection connection = (Connection) request
                .getAttribute("connection");
        
        DynaActionForm inputFormPlace = (DynaActionForm) form;
        RestaurantManager manager = new RestaurantManager(connection);
        List products = new ArrayList<ProductInfoExt>();
        products = manager.findProductsByCategory((String) inputFormPlace.get("categoryId"));
        List rates = new ArrayList<String>();
        rates = manager.findAllTaxRatesByCategory(products);
        request.getSession().setAttribute("products", products);
        request.getSession().setAttribute("rates", rates);
        if (inputFormPlace.get("mode").equals("1")) {
            request.setAttribute("subcategories", manager.findAllSubcategories(inputFormPlace.getString("categoryId")));
            return mapping.findForward(NEXTLEVEL);
        }
        request.setAttribute("subcategories", manager.findAllSubcategories(inputFormPlace.getString("categoryId")));

        return mapping.findForward(SUCCESS);

    }
}