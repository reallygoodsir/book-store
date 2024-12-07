<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.bookstore.models.Book" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="books" scope="request" class="java.util.ArrayList"></jsp:useBean>
<%@ include file="header.jsp" %>
<head>
    <title>ATA!</title>
</head>

<h1>ATA: All The Awesome!</h1>

<div class="row">
<c:forEach items="${books}" var="book">
    <div class="col-md-4" style="padding: 5px;">
        <div style="margin:3px; padding:10px; background-color: #eee;">
            <div class="row">
                <div class="col-md-4">
                    <a href="./BookLookup?isbn=${book.isbn}" style="max-height: 130px; max-width: 110px;">
                        <img src="book_images/${book.coverImageFile}"
                             alt="${book.title} cover"
                             style="max-width: inherit; max-height: inherit">
                    </a>
                </div>
                <div class="col-md-8" style="text-align: left; padding-left:10px;">
                    <h4>${book.title}</h4>
                    <h5>${book.price} USD</h5>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
</div>

<%@ include file="footer.jsp" %>
