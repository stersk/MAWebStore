<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>REGISTRATION</title>
</head>
<body>
<h2>Hello, input all date. please!</h2>
<br>

<form action="user" method="post">
    <input type="text" name="action" value="register" hidden>
    <input type="text" size="40" name="login" placeholder="Input login" required>
    <br>
    <input type="password" size="40" name="password" placeholder="Input password" required>
    <br>
    <input type="text" size="40" name="fname" placeholder="Input first name" required>
    <br>
    <input type="text" size="40" name="lname" placeholder="Input last name" required>
    <br>
    <input type="submit">
</form>
<br>
<h3>
    <a href="/web-ma/index.jsp">GO BACK!</a>
</h3>
</body>
</html>
