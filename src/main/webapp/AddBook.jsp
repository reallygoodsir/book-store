<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<style>
    .requirements {
        font-size: 14px;
        margin-bottom: 15px;
        color: red;
    }
    .requirements strong {
        margin-bottom: 8px;
        display: inline-block;
    }
    .requirements p {
        margin: 0;
        text-indent: 0;
        white-space: normal;
    }
    .form-group {
        text-align: center;
        margin-bottom: 15px;
    }
    .form-group label {
        display: inline-block;
        width: 100px;  /* Control label width */
    }
    .form-group input {
        display: inline-block;
        width: 200px;  /* Control input field width */
    }
    .align-left {
        text-align: center;
    }

</style>
<h1 class="align-left">Add a new book!</h1>

<form action="./AddBook" method="post" enctype="multipart/form-data">

    <%-- ISBN Error --%>
    <%
        if ("true".equals(request.getAttribute("isbnError"))) {
    %>
    <div class="requirements">
        <strong>ISBN must be written in either ISBN-10 or ISBN-13:</strong>
        <p>ISBN-10 Template: X-XXX-XXXXX-X</p>
        <p>ISBN-13 Template: XXX-X-XX-XXXXXX-X</p>
    </div>
    <%
        }
    %>

    <%-- Duplicate ISBN Error --%>
    <%
        if ("true".equals(request.getAttribute("isbnIsDuplicate"))) {
    %>
    <div class="requirements">
        <strong>Entered ISBN is duplicate</strong>
    </div>
    <%
        }
    %>

    <p class="form-group">
        <label for="isbn">ISBN:</label>
        <input type="text" name="isbn" id="isbn" required value="<%= request.getAttribute("isbn") != null ? request.getAttribute("isbn") : "" %>" />
    </p>

    <p class="form-group">
        <label for="title">Title:</label>
        <input type="text" name="title" id="title" required value="<%= request.getAttribute("title") != null ? request.getAttribute("title") : "" %>" />
    </p>

	<p class="form-group">
		<label for="publisher">Publisher:</label>
		<input type="text" name="publisher" id="publisher" required value="<%= request.getAttribute("publisher") != null ? request.getAttribute("publisher") : "" %>" />
	</p>

    <%-- Genres Duplicate Error --%>
    <%
        if ("true".equals(request.getAttribute("genresAreDuplicate"))) {
    %>
    <div class="requirements">
        <strong>Genres must not repeat</strong>
    </div>
    <%
        }
    %>

	<p class="form-group">
		<label for="genre1">1st Genre:</label>
		<input type="text" name="genre1" id="genre1" required value="<%= request.getAttribute("genre1") != null ? request.getAttribute("genre1") : "" %>" />
	</p>

	<p class="form-group">
		<label for="genre2">2nd Genre:</label>
		<input type="text" name="genre2" id="genre2" value="<%= request.getAttribute("genre2") != null ? request.getAttribute("genre2") : "" %>" />
	</p>

    <%-- Authors Duplicate Error --%>
    <%
        if ("true".equals(request.getAttribute("authorsAreDuplicate"))) {
    %>
    <div class="requirements">
        <strong>Authors must not repeat</strong>
    </div>
    <%
        }
    %>

	<p class="form-group">
		<label for="author1">1st Author:</label>
		<input type="text" name="author1" id="author1" required value="<%= request.getAttribute("author1") != null ? request.getAttribute("author1") : "" %>" />
	</p>

	<p class="form-group">
		<label for="author2">2nd Author:</label>
		<input type="text" name="author2" id="author2" value="<%= request.getAttribute("author2") != null ? request.getAttribute("author2") : "" %>" />
	</p>

	<p class="form-group">
		<label for="author3">3rd Author:</label>
		<input type="text" name="author3" id="author3" value="<%= request.getAttribute("author3") != null ? request.getAttribute("author3") : "" %>" />
	</p>

    <%-- Price Error --%>
    <%
        if ("true".equals(request.getAttribute("priceError"))) {
    %>
    <div class="requirements">
        <strong>Price requirements:</strong>
        <p>Must be a number</p>
        <p>Value must be 0-9999</p>
    </div>
    <%
        }
    %>

	<p class="form-group">
		<label for="price">Price:</label>
		<input type="text" name="price" id="price" required value="<%= request.getAttribute("price") != null ? request.getAttribute("price") : "" %>" />
	</p>

	<p class="form-group">
		<label for="description">Description:</label>
		<input type="text" name="description" id="description" required value="<%= request.getAttribute("description") != null ? request.getAttribute("description") : "" %>" />
	</p>

    <%-- Publish Date Error --%>
    <%
        if ("true".equals(request.getAttribute("publishDateError"))) {
    %>
    <div class="requirements">
        <strong>Date must be before <%= java.time.LocalDate.now().toString() %></strong>
    </div>
    <%
        }
    %>

	<p class="form-group">
		<label for="publishDate">Publish Date:</label>
		<input type="date" name="publishDate" id="publishDate" required value="<%= request.getAttribute("publishDate") != null ? request.getAttribute("publishDate") : "" %>" />
	</p>

    <%-- Inventory Error --%>
    <%
        if ("true".equals(request.getAttribute("inventoryError"))) {
    %>
    <div class="requirements">
        <strong>Inventory can't be below 1</strong>
    </div>
    <%
        }
    %>

	<p class="form-group">
		<label for="inventory">Inventory:</label>
		<input type="text" name="inventory" id="inventory" required value="<%= request.getAttribute("inventory") != null ? request.getAttribute("inventory") : "" %>" />
	</p>

    <%
        if ("true".equals(request.getAttribute("fileError"))) {
    %>
    <div class="requirements">
        <strong>Must select a book cover</strong>
    </div>
    <%
        }
    %>

	<p class="form-group">
		<label for="coverImage">Cover Image:</label>
		<input type="file" name="coverImage" id="coverImage" />
	</p>

	<p>
        <input type="submit" class="btn btn-primary align-left" />
    </p>

</form>

<%@ include file="footer.jsp" %>
