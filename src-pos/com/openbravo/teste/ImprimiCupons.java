/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.teste;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.DataLogicSystem;
import static com.openbravo.pos.forms.StartPOS.registerApp;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.AltEncrypter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author osmar
 */
public class ImprimiCupons {

    public static AppConfig config;
    public static Session session;
    public static String place;

   public static void main(String args[]){
       try {
            init(new String[0],args[0], args[1]);
       } catch (Exception e) {
           
       }
   }
   
    public static void init(String args[], String id, String local) throws TicketPrinterException, BasicException, SQLException, ScriptException {

        config = new AppConfig(args);
        config.load();

        // set Locale.
        String slang = config.getProperty("user.language");
        String scountry = config.getProperty("user.country");
        String svariant = config.getProperty("user.variant");
        if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
            Locale.setDefault(new Locale(slang, scountry, svariant));
        }
        
        String sDBUser = config.getProperty("db.user");
        String sDBPassword = config.getProperty("db.password");  
        
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
                // the password is encrypted
                AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
                sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        
        session = new Session(config.getProperty("db.URL"), sDBUser,sDBPassword);

        DataLogicReceipts dlr = new DataLogicReceipts();
        dlr.init(session);

        
        
        TicketInfo ticket = dlr.getSharedTicket(id);

        
        
        if("SendOrder".equals(local)){
            ticket = printSendOrder(ticket);
        } else if("CloseOrder".equals(local)){
            Place placeTemp;
            SentenceList sent = new StaticSentence(
                    session, 
                    "SELECT ID, NAME, X, Y, FLOOR FROM PLACES WHERE ID = '" + id +  "'ORDER BY ID", 
                    null, 
                    new SerializerReadClass(Place.class));
            if(!sent.list().isEmpty()){
                placeTemp = (Place) sent.list().get(0);
                place = placeTemp.getName();
            }
            printTicket("Printer.TicketPreview", ticket);
        }
        
        dlr.updateSharedTicket(id, ticket);
        
        session.close();

    }

    private static TicketInfo printSendOrder(TicketInfo ticket) throws SQLException, ScriptException, TicketPrinterException, BasicException{
        // Print an order ticket to the Kitchen & Bar\Dessert  
        boolean kitchen = false;
        boolean kitchen1 = false;
        boolean bar = false;
        boolean sushi = false;
        boolean sushi1 = false;
        boolean change_kitchen = false;
        boolean change_kitchen1 = false;
        boolean change_bar = false;
        boolean change_sushi = false;
        boolean change_sushi1 = false;
        TicketLineInfo line;

        for (int i = 0; i < ticket.getLinesCount(); i++) {

            line = ticket.getLine(i);

            // Set Discount(printkb=NULL) to N/A so it does not error on next section. 
            if (line.getProperty("printkb") == null) {
                line.setProperty("printkb", "N/A");
            }
            if (line.getProperty("sendstatus") == null) {
                line.setProperty("sendstatus", "No");
            }

            if ((line.getProperty("printkb").equals("Kitchen")) && (line.getProperty("sendstatus").equals("No"))) {
                   kitchen = true; //There is something to print to kitchen 
            } else if ((line.getProperty("printkb").equals("Kitchen2")) && (line.getProperty("sendstatus").equals("No"))) {
                kitchen1 = true; //There is something to print to bar
            } else if ((line.getProperty("printkb").equals("Bar")) && (line.getProperty("sendstatus").equals("No"))) {
                bar = true; //There is something to print to bar 
            } else if ((line.getProperty("printkb").equals("Sushi")) && (line.getProperty("sendstatus").equals("No"))) {
                sushi = true; //There is something to print to Sushi 
            } else if ((line.getProperty("printkb").equals("Sushi2")) && (line.getProperty("sendstatus").equals("No"))) {
                sushi1 = true; //There is something to print to Sushi 
            } else if ((line.getProperty("printkb").equals("Kitchen")) && (line.getProperty("sendstatus").equals("Cancel"))) {
                change_kitchen = true; //There is something to change for kitchen 
            } else if ((line.getProperty("printkb").equals("Kitchen2")) && (line.getProperty("sendstatus").equals("Cancel"))) {
                change_kitchen1 = true; //There is something to change for kitchen
            } else if ((line.getProperty("printkb").equals("Bar")) && (line.getProperty("sendstatus").equals("Cancel"))) {
                change_bar = true; //There is something to change for bar
            } else if ((line.getProperty("printkb").equals("Sushi")) && (line.getProperty("sendstatus").equals("Cancel"))) {
                change_sushi = true; //There is something to change for Sushi  
            } else if ((line.getProperty("printkb").equals("Sushi2")) && (line.getProperty("sendstatus").equals("Cancel"))) {
                change_sushi1 = true; //There is something to change for Sushi  
            }
        }
        List<String> resources = new ArrayList<String>();
        if ((change_kitchen && kitchen) || (change_kitchen && !kitchen)) {
            resources.add("Printer.TicketChange_Kitchen"); //Print changed kitchen items to kitchen printer 
        }
        if ((change_kitchen1 && kitchen1) || (change_kitchen1 && !kitchen1)) {
            resources.add("Printer.TicketChange_Kitchen2"); //Print changed kitchen items to kitchen printer 
        }
        if ((change_bar && bar) || (change_bar && !bar)) {
            resources.add("Printer.TicketChange_Bar"); //Print changed bar items to bar printer 
        }
        if ((change_sushi && sushi) || (change_sushi && !sushi)) {
            resources.add("Printer.TicketChange_Sushi"); //Print changed Sushi items to sushi printer 
        }
        if ((change_sushi1 && sushi1) || (change_sushi1 && !sushi1)) {
            resources.add("Printer.TicketChange_Sushi2"); //Print changed Sushi items to sushi printer 
        }
        if (kitchen && !change_kitchen) {
            resources.add("Printer.TicketKitchen"); //Print kitchen items to kitchen printer 
        }
        if (kitchen1 && !change_kitchen1) {
            resources.add("Printer.TicketKitchen2"); //Print kitchen items to kitchen printer 
        }
        if (bar && !change_bar) {
            resources.add("Printer.TicketBar"); //Print bar items to bar printer 
        }
        if (sushi && !change_sushi) {
            resources.add("Printer.TicketSushi"); //Print sushi items to bar printer 
        }
        if (sushi1 && !change_sushi1) {
            resources.add("Printer.TicketSushi2"); //Print sushi items to bar printer 
        }
        for (String resource : resources) {
            ImprimiCupons.printTicket(resource, ticket);
        }
        String imprimir;
        if (kitchen || kitchen1 || bar || sushi || sushi1) {
            imprimir = "Seu pedido foi enviado para:  ";
            if (kitchen || kitchen1) {
                imprimir += " Cozinha";
            }
            if (bar) {
                imprimir += " Bar";
            }
            if (sushi || sushi1) {
                imprimir += " Sushi";
            }
        } else if (change_kitchen || change_kitchen1 || change_bar || change_sushi || change_sushi1) {
            imprimir = "Seu pedido foi cancelado com sucesso.";
        } else {
            imprimir = "Não há nada novo para enviar.";
        }
        
        System.out.println(imprimir);
        
        //Set printkb property of item to Yes so it is not printed again 
        for (int i = ticket.getLinesCount() - 1; i >= 0; i--) {

            line = ticket.getLine(i);
            String a = line.getProperty("sendstatus");
            String b = "Cancel";

            if ((((line.getProperty("printkb").equals("Kitchen")) || line.getProperty("printkb").equals("Kitchen/Sushi")) || (line.getProperty("printkb").equals("Kitchen2")) || (line.getProperty("printkb").equals("Bar")) || (line.getProperty("printkb").equals("Sushi")) || (line.getProperty("printkb").equals("Sushi2"))) && (line.getProperty("sendstatus").equals("No"))) {
                line.setProperty("sendstatus", "Yes");
                line.setPrint(true);
            }

            //Remove cancelled lines
            if (a.equals(b)) {
                ticket.removeLine(i);
            }
            
        }
        
        return ticket;
    }
    
    private static void printTicket(String resources, TicketInfo info) throws SQLException, ScriptException, TicketPrinterException, BasicException {
        //DataLogicReceipts dlr = new DataLogicReceipts();
        //dlr.init(session);

        TicketInfo ticket = info;
        
        DataLogicSystem system = new DataLogicSystem();
        system.init(session);

        DeviceTicket deviceTicket = new DeviceTicket(null, config);

        TicketParser sales = new TicketParser(deviceTicket, system);

        String resource = system.getResourceAsXML(resources);

        if (resource != null) {
            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
            script.put("ticket", ticket);
            script.put("place", place);
            sales.printTicket(script.eval(resource).toString());
        }
     }
}
