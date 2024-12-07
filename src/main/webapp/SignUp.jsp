<%@ include file="header.jsp" %>
<style>
    .form-signin {
        max-width: 330px;
        padding: 15px;
        margin: 0 auto;
    }
    .form-signin .form-signin-heading,
    .form-signin .checkbox {
        margin-bottom: 10px;
    }
    .form-signin .checkbox {
        font-weight: normal;
    }
    .form-signin .form-control {
        position: relative;
        height: auto;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        padding: 10px;
        font-size: 16px;
    }
    .form-signin .form-control:focus {
        z-index: 2;
    }
    .form-signin input[type="email"] {
        margin-bottom: -1px;
        border-bottom-right-radius: 0;
        border-bottom-left-radius: 0;
    }
    .form-signin input[type="password"] {
        margin-bottom: 10px;
        border-top-left-radius: 0;
        border-top-right-radius: 0;
    }
    /* Styling for aligned requirement text */
    .requirements {
        font-size: 14px;
        margin-bottom: 15px;
        color: red;
        padding-left: 20px; /* Uniform indentation */
        text-align: left;
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
</style>
<%
    if (request.getParameter("checkErr") != null) {
%>
<div class="alert alert-danger" style="max-width: 330px; margin: 0 auto" role="alert">
    Please sign in to continue checkout
</div>
<%
    }
%>
<form class="form-signin" action="./UserLogin" method="post">
    <h2 class="form-signin-heading">Please sign in</h2>
    <%
         if ("true".equals(request.getAttribute("credentialsError"))) {
    %>
        <div class="requirements">
            <strong>Incorrect username or password</strong>
        </div>
    <%
        }
    %>
    <label for="username" class="sr-only">Username</label>
    <input type="text" class="form-control" name="username" id="username" placeholder="Username"
        value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" />

    <label for="passwd" class="sr-only">Password</label>
    <input type="password" name="passwd" id="passwd" class="form-control" placeholder="Password">

    <button type="submit" class="btn btn-lg btn-primary btn-block">Sign in</button>
</form>

<form action="user-register" method="post" class="form-signin">
    <h2 class="form-signin-heading">Not registered yet?</h2>

    <%
        if ("true".equals(request.getAttribute("userNameError"))) {
    %>
    <div class="requirements">
        <strong>User name requirements:</strong>
        <p>1. Cannot be empty</p>
        <p>2. Must have at least one capital letter</p>
        <p>3. Cannot be shorter than 2 letters or longer than 18 letters</p>
        <p>4. Cannot include any special characters</p>
        <p>5. Must include at least one number</p>
    </div>
    <%
        }
    %>

    <%
        if ("true".equals(request.getAttribute("userNameIsDuplicate"))) {
    %>
    <div class="requirements">
        <strong>Entered username already exists</strong>
    </div>
    <%
        }
    %>


    <label for="userName" class="sr-only">Username:</label>
    <input type="text" class="form-control" name="userName" id="userName" required placeholder="User Name"
        value="<%= request.getParameter("userName") != null ? request.getParameter("userName") : "" %>" />

    <%
        if ("true".equals(request.getAttribute("firstNameError"))) {
    %>
    <div class="requirements">
        <strong>First name requirements:</strong>
        <p>1. Cannot be empty</p>
        <p>2. Length must be between 3 and 15</p>
    </div>
    <%
        }
    %>
    <label for="firstName" class="sr-only">First Name:</label>
    <input type="text" class="form-control" name="firstName" id="firstName" placeholder="First Name" required
        value="<%= request.getParameter("firstName") != null ? request.getParameter("firstName") : "" %>" />

    <%
        if ("true".equals(request.getAttribute("lastNameError"))) {
    %>
    <div class="requirements">
        <strong>Last name requirements:</strong>
        <p>1. Cannot be empty</p>
        <p>2. Length must be between 3 and 15</p>
    </div>
    <%
        }
    %>
    <label for="lastName" class="sr-only">Last Name:</label>
    <input type="text" class="form-control" name="lastName" id="lastName" required placeholder="Last Name"
        value="<%= request.getParameter("lastName") != null ? request.getParameter("lastName") : "" %>" />

    <%
        if ("true".equals(request.getAttribute("passwordError"))) {
    %>
    <div class="requirements">
        <strong>Password requirements:</strong>
        <p>1. Cannot be shorter than 8 characters</p>
        <p>2. Cannot be longer than 40 characters</p>
    </div>
    <%
        }
    %>
    <label for="password" class="sr-only">Password:</label>
    <input type="password" class="form-control" name="password" id="password" required placeholder="Password" />

    <%
        if ("true".equals(request.getAttribute("emailError"))) {
    %>
    <div class="requirements">
        <strong>Email requirements:</strong>
        <p>1. The local part may contain letters, digits, and special characters (._%+-).</p>
        <p>2. The domain must contain letters, digits, and periods, but cannot start or end with a period.</p>
        <p>3. The top-level domain (TLD) must consist of at least 2 letters.</p>
    </div>
    <%
        }
    %>
    <label for="email" class="sr-only">Email:</label>
    <input type="email" class="form-control" name="email" id="email" required placeholder="Email"
        value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" />

    <%
        if ("true".equals(request.getAttribute("phoneNumberError"))) {
    %>
    <div class="requirements">
        <strong>Phone number requirements:</strong>
        <p>1. Must start with a non-zero digit</p>
        <p>2. Must consist of 2 to 15 digits</p>
        <p>3. No spaces, hyphens, or special characters allowed</p>
    </div>
    <%
        }
    %>
    <label for="phoneNumber" class="sr-only">Phone Number:</label>
    <input type="tel" class="form-control" name="phoneNumber" id="phoneNumber" placeholder="Phone Number"
        value="<%= request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "" %>" /></br>

    <input type="submit" class="btn btn-lg btn-primary btn-block" value="Sign Up" />
</form>
<%@ include file="footer.jsp" %>
