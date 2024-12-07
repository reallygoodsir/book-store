<%@ page import="java.util.*" %>
<%@ include file="header.jsp" %>
<jsp:useBean id="weeklySales" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="monthlySales" scope="request" class="java.util.ArrayList"></jsp:useBean>

<%
	String userName = (String) session.getAttribute("userName");
	if (userName == null || userName.isEmpty()) {
		response.sendRedirect("./SignUp.jsp");
	}
%>

<h4>Administrative Reports</h4>
<h1>Welcome, <%=userName%>.</h1>

<div role="tabpanel">
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#weeklySales" aria-controls="home" role="tab" data-toggle="tab">Weekly Sales</a></li>
    <li role="presentation"><a href="#monthlySales" aria-controls="profile" role="tab" data-toggle="tab">Monthly Sales</a></li>
  </ul>

  <!-- Tab panes -->
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="weeklySales">
		<table class="table">
				<thead>
					<th>Week</th>
					<th>Total Sales</th>
					<th>Sales Compared w/ Previous Week</th>
				</thead>
				<tbody>
                    <c:forEach items="${weeklySales}" var="weekRow">
                        <tr style="text-align: left">
                            <c:forEach items= "${weekRow}" var="weekCell">
                                <td>${weekCell}</td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
				</tbody>
		</table>
    </div>
    <div role="tabpanel" class="tab-pane" id="monthlySales">
    		<table class="table">
				<thead>
					<th>Month</th>
					<th>Total Sales</th>
					<th>Sales Compared w/ Previous Month</th>
				</thead>
				<tbody>
				<c:forEach items="${monthlySales}" var="monthRow">
					<tr style="text-align: left">
                        <c:forEach items= "${monthRow}" var="monthCell">
                            <td>${monthCell}</td>
                        </c:forEach>
					</tr>
				</c:forEach>
				</tbody>
		</table>
    </div>
  </div>
</div>

<script>
$('#weeklySales a').click(function (e) {
	  e.preventDefault()
	  $(this).tab('show')
	});
$('#monthlySales a').click(function (e) {
	  e.preventDefault()
	  $(this).tab('show')
	});
</script>
<%@ include file="footer.jsp" %>