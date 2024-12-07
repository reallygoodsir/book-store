<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.bookstore.models.User"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="users" scope="request" class="java.util.ArrayList"></jsp:useBean>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">User Admin</h3>
	</div>
	<div class="panel-body">
		<c:forEach items="${users}" var="user">
			<tr style="text-align: left">
				<td>First Name: ${user.firstName}</td><br />
				<td>Last Name: ${user.lastName}</td><br />
				<td>User Name: ${user.userName}</td><br />
				<td>Email: ${user.email}</td><br />
				<td>Phone Number: ${user.phoneNumber}</td><br />
				<td>Signup Date: ${user.signDate}</td><br />
				<td>Last seen on: ${user.lastDate}</td><br />
				<td>
					<form action="./UserDelete" method="post">
						<input type="hidden" name="userName" id="userName" value="${user.userName}" />
						<input type="submit" value="delete" />
					</form>
				</td>
				<br />
			</tr>
		</c:forEach>
	</div>
</div>
<%@ include file="footer.jsp"%>