

<%@page pageEncoding="UTF-8"
        import="java.util.List, com.openbravo.pos.ticket.ProductInfoExt, com.openbravo.pos.ticket.TicketLineInfo"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"   %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="javascript; charset=UTF-8">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <link rel=StyleSheet href="../css/layout.css" type="text/css" media=screen>
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <link href="../css/bootstrap-theme.min.css" rel="stylesheet">
        <script type="text/javascript" src='../tableScript.js'></script>
        <script type="text/javascript" src='../a.js'></script>
        <script type="text/javascript" src='../js/jquery-1.3.2.min.js'></script>
        <title><bean:message key="tables" /></title>
    </head>
    <body>
                <jsp:useBean id="products" type="List<ProductInfoExt>" scope="request" />
                <jsp:useBean id="place" type="java.lang.String" scope="request" />
                <jsp:useBean id="placeName" type="java.lang.String" scope="request" />
                <jsp:useBean id="floorName" type="java.lang.String" scope="request" />
                <jsp:useBean id="floorId" type="java.lang.String" scope="request" />
                <jsp:useBean id="lines" type="List<TicketLineInfo>" scope="request" />

        <div class="well" style="max-width: 400px; margin: 0 auto 10px;">
            <a href='../showFloors.do?floorId=${floorId}' class="btn btn-default btn-lg" style="width: 49%;"><img src="../images/back.png" class="backImg"> Voltar</a>
            <a href="#" name="place" onclick="window.location='showProducts.do?place=${place}&floorId=${floorId}'" class="btn btn-default btn-lg" style="width: 49%;"><img src="../images/plus_1.png" class="backImg"> Add</a>
        </div>
        <div class="well" style="max-width: 400px; margin: 0 auto 10px;">
            <div class="row-fluid">
                <div class="span12 text-center">
                    <h2><%=floorName%> / ${placeName}</h2>
                </div>
            </div>
            <div>
                <br>
            </div>
            <span class="middle">
                <center>
                    <table border="0" id="table" class="table   ">
                        <thead>
                            <tr>
                                <th class="name"><bean:message key="item" /></th>
                                <th class="normal"><bean:message key="price" /></th>
                                <!--     <th class="units">#</th>-->
                                <th class="normal"># / <bean:message key="value" /></th>
                                <th class="units"></th>
                                <th class="units"></th>

                        </thead>
                        <tbody>
                        <span id="products" >
                            <% boolean rowodd = false; %>
                            <c:forEach var="line" items="${lines}" varStatus="nr">
                                <% rowodd = !rowodd;%>
                                <c:if test="${!line.print}">
                                <tr id="${nr.count - 1}" class="<%= rowodd ? "active" : "info"%>">
                                    <c:choose>
                                        <c:when test="${products[nr.count - 1].com == true}">
                                            <td class="name" onclick="escondeMostra('tr${nr.count - 1}')" >* ${products[nr.count - 1].name}</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="name" onclick="escondeMostra('tr${nr.count - 1}')" >${products[nr.count - 1].name}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="normal"><fmt:formatNumber type="currency" value="${line.price}" maxFractionDigits="2" minFractionDigits="2"/></td>
                                    <td class="normal" id="value${nr.count - 1}"><input type="text" id="input${nr.count - 1}" size="3" onchange="getIndexBackByEditing('${nr.count -1}', '${place}');" value="<fmt:formatNumber type="number" value="${line.multiply}" maxFractionDigits="2" minFractionDigits="0"/>" /> <fmt:formatNumber type="currency" value="${line.value}" maxFractionDigits="2" minFractionDigits="2"/></td>
                                    <td><a href="#" onclick="ajaxCall(3, '${place}', '${nr.count - 1}');"><img src="../images/plus_1.png" alt="add" class="button" /></a></td>
                                    <td><a href="#" onclick="ajaxCall(1, '${place}', '${nr.count - 1}');"><img src="../images/minus_1.png" alt="remove" class="button" /></a></td>
                                </tr>
                                <tr style="display:none;" id="tr${nr.count - 1}">
                                <form action="showPlace.do">
                                    <td colspan="3" >
                                        <input type="hidden" value="${place}" name="id" />
                                        <input type="hidden" value="${floorId}" name="floorId" />
                                        <input type="hidden" value="${nr.count - 1}" name="parameters" />
                                        <input type="hidden" value="6" name="mode" />
                                        <textarea id="obs" name="obs" rows="2" cols="50" style="width: 100%; height: 100%;">${line.productAttSetInstDesc}</textarea>
                                    </td>
                                    <td colspan="2">
                                        <button name="btn" class="btn btn-default btn-lg">Enviar</a>
                                    </td>
                                </form>
                                </tr>
                                </c:if>
                            </c:forEach>
                        </span>
                        </tbody>
                    </table>
                </center>
                <p class="total" id="atotal">Total: <fmt:formatNumber type="currency" value="${total}" maxFractionDigits="2" minFractionDigits="2" /> </p>
                <a href="#" name="place" onclick="window.location='showPlace.do?id=${place}&floorId=${floorId}&mode=5&print=CloseOrder'" class="btn btn-default btn-lg" style="width: 49%;"><img src="../images/money.png" class="backImg"> Fechar</a>
                <a href="#" name="place" onclick="window.location='showPlace.do?id=${place}&floorId=${floorId}&mode=5&print=SendOrder'" class="btn btn-default btn-lg" style="width: 49%;"><img src="../images/printer.png" class="backImg"> Imprimir</a>
                <!--
                    <a href="#" name="place" onclick="confirmDeleting('${floorId}', '${place}');" class="btn btn-default btn-lg btn-block"><img src="../images/minus_1.png" class="backImg"> <bean:message  key="delete" /></a> -->
            </span>
        </div>
            <script type="text/javascript">    
    function escondeMostra(x){    
        if(document.getElementById(x).style.display == "none"){    
            document.getElementById(x).style.display = "";    
        }    
        else{    
            document.getElementById(x).style.display = "none";    
        }    
    }    
            </script>
    </body>
</html>
