<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <link rel="shortcut icon" href="./favicon.ico" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

        <!-- Optional theme -->
        <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">-->
    </head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="./">All The Awesome</a>
        </div>

        <div id="navbar" class="navbar-collapse collapse">
          <form class="navbar-form navbar-left" action="./Search" method="get">
            <input type="text" class="form-control" name="search-query" id="search-query" size="40" placeholder="ISBN, Title, Author, Keyword...">
          </form>

          <ul class="nav navbar-nav navbar-right">
          	<li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Browse<span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="./Search?search-query=">All</a></li>
                  <%
                      List<String> allGenreNames = (List<String>) request.getAttribute("allGenreNames");
                      for(String genreName : allGenreNames) {
                  %>
                        <li><a href="./Search?search-query=<%= genreName %>"><%= genreName %></a></li>
                  <%
                     }
                  %>
                </ul>
            </li>
            <%
			String userLoggedIn = (String) session.getAttribute("userName");
			if (userLoggedIn != null) {
			%>
				<li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><%=userLoggedIn %> <span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="./CustomerAccount">Account</a></li>
                  <li class="divider"></li>
                  <li><a href="./UserLogout">Logout</a></li>
                </ul>
                </li>
		    <%
			} else {
			%>
	        <li><a href="./SignUp">Sign in/Register</a></li>
			<%
			    }
		    %>
			 <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Admin<span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="./generateReports">View Reports</a></li>
                  <li><a href="./AddBook">Add Book</a></li>
                  <li><a href="./UsersLookup">View Users</a></li>
                </ul>
              </li>
		     <li><a href="./cartUpdate">Cart</a></li>
		  </ul>
        </div>
      </div>
    </nav>
    <div style="padding: 70px 15px; text-align: center;">
    <div class="container">

    
