<%@ include file="header.jsp" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bookstore.models.Transaction" %>
<%@ page import="com.bookstore.models.Order" %>
<%@ page import="com.bookstore.models.User" %>
<%@ page import="com.bookstore.models.*" %>
<%@ page import="com.bookstore.db.*" %>

<%
    User resultUser = (User) request.getAttribute("user");
    String userName = resultUser.getUserName();
    if (userName == null || userName.isEmpty()) {
        response.sendRedirect("./SignUp.jsp");
    }
%>

<h1>Welcome, <%= userName %>.</h1>

<div role="tabpanel">
    <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#orders" aria-controls="home" role="tab" data-toggle="tab">Orders</a></li>
    <li role="presentation"><a href="#account-info" aria-controls="profile" role="tab" data-toggle="tab">Account Info</a></li>
    <li role="presentation"><a href="#ratings" aria-controls="messages" role="tab" data-toggle="tab">Ratings</a></li>
  </ul>

    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="orders">
            <h2>Order History</h2>

            <%-- Query distinct transactions for this user --%>
            <%
                Map<Transaction, List<Order>> transactions = (Map<Transaction, List<Order>>) request.getAttribute("transactions");
                for (Map.Entry<Transaction, List<Order>> entry : transactions.entrySet()) {
                    Transaction transaction = entry.getKey();
                    List<Order> orders = entry.getValue();
            %>
                    <div class="panel panel-default" style="max-width: 600px; margin:20px auto;">
                        <div class="panel-heading">
                            <h3 class="panel-title" style="text-align: left;">
                                Order number <%= transaction.getTransactionId() %>, <%= transaction.getPurchaseDate() %>
                            </h3>
                        </div>
                        <div class="panel-body">
                            <table class="table" style="text-align: left;">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Title</th>
                                        <th>ISBN</th>
                                        <th>Quantity</th>
                                        <th>Price</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Order order : orders) { %>
                                        <tr>
                                            <td>
                                                <a href="./BookLookup?isbn=<%= order.getIsbn() %>">
                                                    <img src="./book_images/<%= order.getImageName() %>" style="max-width:40px; text-decoration: none">
                                                </a>
                                            </td>
                                            <td><%= order.getTitle() %></td>
                                            <td><%= order.getIsbn() %></td>
                                            <td><%= order.getQuantity() %></td>
                                            <td><%= order.getPrice() %></td>
                                        </tr>
                                    <% } %>
                                    <tr>
                                        <td colspan="4" style="text-align: right;">Total</td>
                                        <td><%= transaction.getTotalPrice() %></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
            <%
                }
            %>
        </div>
        <div role="tabpanel" class="tab-pane" id="account-info" >
                	<div class="container" style="max-width:90%; margin: 20px auto 0px auto">
                        <div class="panel panel-default" id="account-info-static">
                            <h2 style="padding:15px">Account Information for <%= resultUser.getUserName() %></h2>
                            <table class="table" style="max-width: 500px; margin-left: auto; margin-right: auto; padding:20px">
                                <tr style="text-align: left"><td>First Name</td><td><%= resultUser.getFirstName() %></td></tr>
                                <tr style="text-align: left"><td>Last Name</td><td><%= resultUser.getLastName() %></td></tr>
                                <tr style="text-align: left"><td>Email</td><td><%= resultUser.getEmail() %></td></tr>
                                <tr style="text-align: left"><td>Phone Number</td><td><%= resultUser.getPhoneNumber() %></td></tr>
                            </table>
                            <button id="edit-info-btn" class="btn btn-primary" style="margin: 20px auto 20px auto;">Edit</button>
                        </div>
                        <div class="panel panel-default" id="account-info-update" style="display:none">
                            <h2 style="padding:15px">Account Information for <%= resultUser.getUserName() %></h2>
                            <form action="./updateUser" method="post">
                                <table class="table" style="max-width: 500px; margin-left: auto; margin-right: auto; padding:20px">
                                    <tr style="text-align: left"><td>First Name</td><td><input type="text" name="fName" value="<%= resultUser.getFirstName() %>" required /></td></tr>
                                    <tr style="text-align: left"><td>Last Name</td><td><input type="text" name="lName" value="<%= resultUser.getLastName() %>" required /></td></tr>
                                    <tr style="text-align: left"><td>Email</td><td><input type="email" name="email" value="<%= resultUser.getEmail() %>" required /></td></tr>
                                    <tr style="text-align: left"><td>Phone Number</td><td><input type="tel" name="phone" value="<%= resultUser.getPhoneNumber() %>" required /></td></tr>
                                </table>
                                <button id="cancel-edit-info-btn" class="btn btn-primary" style="margin: 20px auto 20px auto;">Cancel</button>
                                <input type="submit" class="btn btn-primary" value="Update" />
                            </form>
                        </div>
                    </div>
        </div>
            <div role="tabpanel" class="tab-pane" id="ratings">
                    	<div class="container" style="max-width:90%; margin: 20px auto 0px auto">
                    		<h2 style="padding:15px">Reviews written by <%=userName %></h2>
                    		<%
                    		    Map<Review, Book> reviews = (Map<Review, Book>) request.getAttribute("reviews");
                				for (Map.Entry<Review, Book> entryReviews : reviews.entrySet()) {
                                    Review review = entryReviews.getKey();
                                    Book book = entryReviews.getValue();

                			%>
                				<div class="panel panel-default" style="max-width: 600px; margin:20px auto 20px auto;">
                					<div class="panel-heading">
                		    			<h3 class="panel-title" style="text-align: left"><%= book.getTitle() %></h3>
                		  			</div>
                		  			<div class="panel-body">
                		  				<p>Rating: <%= review.getRating() %></p>
                		  				<p>Review: <%= review.getReviewText() %>
                		  			</div>
                	  			</div>
                			<%} %>
                    	</div>
                </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $('#orders a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });
        $('#account-info a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });
        $('#ratings a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });
        $('#edit-info-btn').click(function (e) {
            e.preventDefault();
            $('#account-info-static').hide();
            $('#account-info-update').show();
        });
        $('#cancel-edit-info-btn').click(function (e) {
            e.preventDefault();
            $('#account-info-static').show();
            $('#account-info-update').hide();
        });
    });
</script>

<%@ include file="footer.jsp" %>