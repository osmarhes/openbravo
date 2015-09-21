<%@page contentType="text/javascript" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<span>
    <% boolean rowodd = false; %>
    <c:forEach var="product" items="${auxiliars}" varStatus="nr">
        <tr id="${nr.count - 1}" class="<%= rowodd ? "odd" : "even" %>">
            <button type="button" style="max-width: 500px; margin: 0 auto 10px;" name="id" onclick="ajaxAddProduct('<%=request.getSession().getAttribute("place")%>', ${nr.count - 1}, '${product.name}', '${product.id}', 1);" class="btn btn-default btn-lg btn-block">* ${product.name}</button>
        </tr>
    </c:forEach>
</span>

