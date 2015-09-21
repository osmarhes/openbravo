<%--
   Openbravo POS is a point of sales application designed for touch screens.
   Copyright (C) 2007-2009 Openbravo, S.L.
   http://sourceforge.net/projects/openbravopos

    This file is part of Openbravo POS.

    Openbravo POS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Openbravo POS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
 --%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/signin.css" rel="stylesheet">
        <!--<link href="css/bootstrap-responsive.css" rel="stylesheet">-->
        <title><bean:message key="welcome.title"/></title>
        
        <html:base/>
    </head>
    <body>
        <center>
                <img class="img-responsive" src="images/shinjiru.png" alt="Shinjiru" />
            </center>
        <div class="container">
            <html:form styleClass="form-signin" action="login.do" method="post">    
                <h2 class="form-signin-heading"><bean:message key="message.welcome" /></h2>
                <logic:messagesPresent >
                    <html:messages id="msg">
                        <div class="alert alert-danger"><bean:write name="msg" /></div>
                    </html:messages>
                </logic:messagesPresent>
                <input name="login" type="login" class="form-control" placeholder="<bean:message key="message.login" />" required="" autofocus="">
                <input name="password" type="password" class="form-control" placeholder="<bean:message key="message.password" />" required="">
                <button class="btn btn-lg btn-danger btn-block" type="submit" ><bean:message key="button.login" /></button>
            </html:form>

        </div>
    </body>
</html>
