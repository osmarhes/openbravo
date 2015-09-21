package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.pda.struts.forms.FloorForm;
import com.openbravo.pos.ticket.ProductInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.ticket.UserInfo;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 *
 * @author jaroslawwozniak
 */
public class PlaceAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String EDITING = "editing";
    private final static String UPDATE = "update";

    /**
     * This is the action called from the Struts framework.
     *
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

        FloorForm floorForm = (FloorForm) form;
        RestaurantManager manager = new RestaurantManager(connection);
        String floorId = (String) floorForm.getFloorId();
        String place = (String) floorForm.getId();
        String str = (String) floorForm.getMode();
        String[] array = null;

        int mode = 0;
        if (!str.equals("")) {
            mode = Integer.valueOf(str);
        }
        List<TicketLineInfo> linesList = new ArrayList<TicketLineInfo>();
        List products = new ArrayList<ProductInfoExt>();
        TicketInfo ticket;
        ProductInfo productInfo;
        switch (mode) {

            //removes products
            case 1:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                array = floorForm.getParameters();

                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        int temp = Integer.valueOf(array[i]);
                        if (linesList.get(temp).getMultiply() > 1) {
                            linesList.get(temp).setMultiply(linesList.get(temp).getMultiply() - 1);
                            productInfo = manager.findProductById(ticket.getLines().get(temp).getProductid());
                            if (!productInfo.isCom() && productInfo.isSpecial()) {
                                for (int j = temp + 1; j < linesList.size(); j++) {
                                    if (manager.findProductById(linesList.get(j).getProductid()).isCom()) {
                                        if(linesList.get(j).getMultiply() <= 1){
                                            linesList.remove(j);
                                            j--;
                                        } else {
                                            linesList.get(j).setMultiply(linesList.get(j).getMultiply() - 1);    //strange                          
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                manager.updateLineFromTicket(place, ticket);
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }

                request.setAttribute("floorName", manager.findFloorById(manager.findPlaceById(place).getFloor()).getName());
                request.setAttribute("place", place);
                request.setAttribute("placeName", manager.findPlaceNameById(place));
                request.setAttribute("floorId", floorId);
                request.setAttribute("lines", linesList);
                request.setAttribute("products", products);
                request.setAttribute("total", manager.getTotalOfaTicket(place));

                return mapping.findForward(SUCCESS);

            case 4:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                array = floorForm.getParameters();
                int var = Integer.parseInt(array[0]);
                productInfo = manager.findProductById(linesList.get(var).getProductid());
                linesList.remove(var);
                if (!productInfo.isCom()) {

                    if (linesList.size() > var && manager.findProductById(linesList.get(var).getProductid()).isCom()) {
                        linesList.remove(var);
                        while (linesList.size() > var && manager.findProductById(linesList.get(var).getProductid()).isCom()) {
                            linesList.remove(var);
                            if (linesList.size() == var) {
                                break;
                            }
                        }
                    }

                }
                manager.updateLineFromTicket(place, ticket);
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }

                request.setAttribute("floorName", manager.findFloorById(manager.findPlaceById(place).getFloor()).getName());
                request.setAttribute("place", place);
                request.setAttribute("placeName", manager.findPlaceNameById(place));
                request.setAttribute("floorId", floorId);
                request.setAttribute("lines", linesList);
                request.setAttribute("products", products);
                request.setAttribute("total", manager.getTotalOfaTicket(place));

                return mapping.findForward(SUCCESS);

            //edits lines
            case 2:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                String[] index = floorForm.getParameters();
                //if null go to default and refresh products. that's why no break
                int temp = Integer.valueOf(index[0]);
                Double multiply = Double.valueOf(index[1]);
                linesList.get(temp).setMultiply(multiply);

                productInfo = manager.findProductById(ticket.getLines().get(temp).getProductid());
                if (!productInfo.isCom() && productInfo.isSpecial()) {
                    for (int j = temp + 1; j < linesList.size(); j++) {
                        if (manager.findProductById(linesList.get(j).getProductid()).isCom()) {
                            linesList.get(j).setMultiply(multiply);    //strange                          
                        }
                    }
                }

                manager.updateLineFromTicket(floorForm.getId(), ticket);
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }

                break;

            //increment product
            case 3:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                array = floorForm.getParameters();
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        temp = Integer.valueOf(array[i]);
                        linesList.get(temp).setMultiply(linesList.get(temp).getMultiply() + 1);    //strange
                        productInfo = manager.findProductById(ticket.getLines().get(Integer.valueOf(array[i])).getProductid());
                        if (!productInfo.isCom() && productInfo.isSpecial()) {
                            for (int j = temp + 1; j < linesList.size(); j++) {
                                if (manager.findProductById(linesList.get(j).getProductid()).isCom()) {
                                    linesList.get(j).setMultiply(linesList.get(j).getMultiply() + 1);    //strange                          
                                }
                            }
                        }
                    }
                }

                manager.updateLineFromTicket(place, ticket);
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }

                request.setAttribute("floorName", manager.findFloorById(manager.findPlaceById(place).getFloor()).getName());
                request.setAttribute("place", place);
                request.setAttribute("placeName", manager.findPlaceNameById(place));
                request.setAttribute("floorId", floorId);
                request.setAttribute("lines", linesList);
                request.setAttribute("products", products);
                request.setAttribute("total", manager.getTotalOfaTicket(place));

                return mapping.findForward(SUCCESS);

            case 5:
                ticket = manager.findTicket(place);

                ticket.setM_User((UserInfo) request.getSession().getAttribute("user"));
                manager.updateLineFromTicket(place, ticket);

                Runtime.getRuntime().exec("java -cp c:\\java\\dist\\Openbravo-POS.jar com.openbravo.teste.ImprimiCupons " + place + " " + floorForm.getPrint());
                Thread.sleep(5000);
                
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }
                
                request.setAttribute("floorName", manager.findFloorById(manager.findPlaceById(place).getFloor()).getName());
                request.setAttribute("place", place);
                request.setAttribute("placeName", manager.findPlaceNameById(place));
                request.setAttribute("floorId", floorId);
                request.setAttribute("lines", linesList);
                request.setAttribute("products", products);
                request.setAttribute("total", manager.getTotalOfaTicket(place));

                return mapping.findForward(SUCCESS);
                
            case 6:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                array = floorForm.getParameters();
                temp = Integer.valueOf(array[0]);
                linesList.get(temp).setProductAttSetInstDesc(floorForm.getObs());
                
                manager.updateLineFromTicket(place, ticket);
                
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }
                
                request.setAttribute("floorName", manager.findFloorById(manager.findPlaceById(place).getFloor()).getName());
                request.setAttribute("place", place);
                request.setAttribute("placeName", manager.findPlaceNameById(place));
                request.setAttribute("floorId", floorId);
                request.setAttribute("lines", linesList);
                request.setAttribute("products", products);
                request.setAttribute("total", manager.getTotalOfaTicket(place));

                return mapping.findForward(SUCCESS);
            //adds new products or just refresh
            default:
                if (manager.findTicket(place) == null) {
                    manager.initTicket(place);
                } else {
                    linesList = manager.findTicketLines(place);
                }
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    if(floorForm.getParameters() != null){
                        if("consulta".endsWith(floorForm.getParameters()[0]))
                            li.setPrint(false);
                    }
                    products.add(manager.findProductById(li.getProductid()));
                }
                break;
        }

        request.setAttribute("floorName", manager.findFloorById(manager.findPlaceById(place).getFloor()).getName());
        request.setAttribute("place", place);
        request.setAttribute("placeName", manager.findPlaceNameById(place));
        request.setAttribute("floorId", floorId);
        request.setAttribute("lines", linesList);
        request.setAttribute("products", products);
        request.setAttribute("total", manager.getTotalOfaTicket(place));

        return mapping.findForward(SUCCESS);
    }
}
