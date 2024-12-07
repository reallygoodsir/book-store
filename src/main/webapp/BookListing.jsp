<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<jsp:useBean id="book" scope="request" class="com.bookstore.models.Book"></jsp:useBean>
<jsp:useBean id="authorNames" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="genreNames" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="bookAverageReviewScore" scope="request" class="java.lang.String"></jsp:useBean>
<%@ page import="com.bookstore.models.Review, java.util.List" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">${book.title}</h3>
    </div>
    <div class="panel-body">
        <img src="./book_images/${book.coverImageFile}" alt="${book.title} cover" width="200">
        <form action="./cartUpdate" method="post">
            <h4>Price: ${book.getPrice()} USD</h4>
            <h4>ISBN: ${book.isbn}</h4>
            <h4>Author(s): ${authorNames}</h4>
            <h4>Publisher: ${book.publisher}</h4>
            <h4>Genre(s): ${genreNames}</h4>
            <h4>Description:</h4>
            <p>${book.description}</p>
            <h4>Rating: ${bookAverageReviewScore}</h4>
            <input type="hidden" name="isbn" value="${book.isbn}" />
            <input type="hidden" name="title" value="${book.title}" />
            <input type="hidden" name="quantity" value="1" />
            <input type="submit" name="add" value="Add to Cart" />
        </form>
        <%
            String userNotLoggedIn = (String) request.getAttribute("userNotLoggedIn");
            if(userNotLoggedIn == null) {
                 Review review = (Review) request.getAttribute("userReview");
                 if(review == null ) {
        %>
            <form action="./submitReview" method="post" style="max-width: 330px; margin: 0 auto; padding: 30px">
                            <h2>Submit a Rating</h2>
                            <div class="form-group">
                                <label for="rating">Rating</label>
                                <select name="rating" class="form-control">
                                    <option value="1">1 Star</option>
                                    <option value="2">2 Stars</option>
                                    <option value="3">3 Stars</option>
                                    <option value="4">4 Stars</option>
                                    <option value="5">5 Stars</option>
                                </select>
                                <input type="hidden" name="isbn" value="${book.isbn}" />
                            </div>
                            <div class="form-group">
                                <label for="review-text">Review</label>
                                <textarea rows="4" cols="50" name="review-text" class="form-control"></textarea>
                            </div>
                            <input type="submit" value="Submit Rating" />
            </form>
        <%
                }
            }

            List<Review> allReviewsForBook = (List<Review>) request.getAttribute("allReviewsForBook");
            if(allReviewsForBook != null){
        %>
                <h4>Reviews for this book:</h4>
                        <ul>
                            <%
                                for(Review existingReview : allReviewsForBook) {
                            %>
                                <li>User: <%= existingReview.getUserName() %></li>
                                <li>Rating: <%= existingReview.getRating() %></li>
                                <li>Review: <%= existingReview.getReviewText() %></li>
                                <br><br>
                            <%
                                }
                            %>
                        </ul>
        <%
            }
        %>
    </div>
</div>

<div class="content">
</div>

<%@ include file="footer.jsp" %>
