<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="books" scope="session" class="java.util.ArrayList"></jsp:useBean>

<div class="container">
    <table class="table">
        <thead>
            <tr>
                <th>View</th>
                <th>Title</th>
                <th>ISBN</th>
                <th>Publish Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${books}" var="book">
                <tr style="text-align: left">
                    <td>
                        <form action="./BookLookup" method="get">
                            <input type="hidden" name="isbn" id="isbn" value="${book.isbn}" />
                            <input type="submit" value="View" />
                        </form>
                    </td>
                    <td>${book.title}</td>
                    <td>${book.isbn}</td>
                    <td>${book.publishDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="footer.jsp" %>
