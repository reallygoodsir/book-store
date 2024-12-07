<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="header.jsp" %>

<%
    String userName = (String) request.getAttribute("userName");
    Boolean isRegistered = (Boolean) request.getAttribute("isRegistered");
    if (isRegistered) {
%>
    <h1><%=userName%> registered successfully</h1>
<%
    } else {
%>
        <h1><%=userName%>failed to register</h1>
<%
    }
%>

<%@ include file="footer.jsp" %>