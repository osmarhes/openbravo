<%@page pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="javascript; charset=UTF-8">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title><bean:message key="floors" /></title>
        <link rel=StyleSheet href="css/layout.css" type='text/css' media=screen>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-theme.min.css" rel="stylesheet">
        <script type='text/javascript' src='a.js'></script>
        <script type='text/javascript' src='tableScript.js'></script>
    </head>
    <body>
        <jsp:useBean id="floorId" scope="request" type="java.lang.String" />
        <center>
            <img class="img-responsive" src="images/shinjiru.png" alt="Shinjiru" />
        </center>
        <div>
            <center>
                <form name="FloorForm" method="post" class="pad">
                    <html:select styleClass="form-control input-lg" style="max-width: 400px;" property="floorId" value="${floorId}" onchange="saveFloorId(this.value);retrieveURL( 'sec/floorAjaxAction.do?floorId=' + this.value, 'ble');"  >
                        <html:options collection="floors" property="id" labelProperty="name"  />
                    </html:select>
                </form>
                <br>
                <div class="well" style="max-width: 500px; margin: 0 auto 10px;">
                    <logic:present name="places">
                        <input type="hidden" name="floorId" value="0" />
                        <% ArrayList places = (ArrayList) request.getSession().getAttribute("places");%>
                        <c:forEach var="place" items="${places}">
                            <c:set var="var" value="false" />
                            <c:forEach var="busy" items="${busy}">
                                <c:if test="${place.id == busy.id}">
                                    <button type="button" name="id" onclick="getLocation('${place.id}');" class="btn btn-primary btn-lg btn-block">${place.name}</button>
                                    <c:set var="var" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:if test="${var == false}">
                                <button type="button" name="id" onclick="getLocation('${place.id}');" class="btn btn-default btn-lg btn-block">${place.name}</button>
                            </c:if>
                        </c:forEach>
                    </logic:present>
                </div>
            </center>
            <div class="bottom">
                <form action="logout.do">
                    <center><button class="btn btn-lg btn-danger btn-block" type="submit" style="max-width: 400px;">Logout</button>              
                </form>
            </div>
        </div>
    </body>

</html>
