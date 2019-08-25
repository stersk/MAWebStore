<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" class="mdl-js">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="icon" href="<c:url value = '/static/img/favicon.ico'/>">

        <title>Store</title>

        <link rel="stylesheet" href="<c:url value = '/static/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value = '/static/css/cart.css'/>">

        <script type="text/javascript" src=<c:url value = "/static/js/jquery-3.4.1.min.js"/>></script>
        <script type="text/javascript" src=<c:url value = "/static/js/bootstrap.min.js"/>></script>

        <script type="text/javascript" src=<c:url value = "/static/js/cart.js"/>></script>
    </head>

    <c:set var="user" value="${user}" scope="session" />
    <c:set var="items" value="${items}" scope="request" />
    <c:set var="orders" value="${orders}" scope="request" />
    <c:set var="counter" value="1" scope="page" />

    <body class="bg-light">
        <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
            <a class="navbar-brand" href="#">Our store</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav mr-auto">
                </ul>
                <c:if test = "${user != null}">
                <form class="form mt-2 mt-md-0">
                    <span class="navbar-text hello-user-text">
                        Hello, <b><c:out value="${user.firstName}" /> <c:out value="${user.login}" /></b>
                    </span>
                    <a class="btn btn-sm btn-outline-secondary" href="#" type="button">Cart</a>
                    <a class="btn btn-sm btn-outline-secondary" href="<c:url value = '/user?action=logout'/>" type="button">Logout</a>
                </form>
                </c:if>
                <c:if test = "${user == null}">
                <form class="form mt-2 mt-md-0">
                    <a class="btn btn-sm btn-outline-secondary" href="<c:url value = '/jsp/registration.jsp'/>" type="submit">Register</a>
                    <a class="btn btn-sm btn-outline-secondary" href="<c:url value = '/'/>" type="submit">Login</a>
                </form>
                </c:if>
                <form class="form-inline mt-2 mt-md-0 d-none">
                    <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
                </form>
            </div>
        </nav>

        <main role="main" class="container-fluid bg-light">
            <div id="alert-container"></div>
            <table class="table table-striped">
                <thead class="thead-dark">
                <tr>
                    <th scope="col" class="row-number col-auto">#</th>
                    <th scope="col">Item</th>
                    <th scope="col">Amount</th>
                    <th scope="col">Price</th>
                    <th scope="col">Sum</th>
                    <th scope="col" class="row-buttons-panel col-auto"></th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orders}" var="order">
                        <c:forEach items="${items}" var="item">
                            <c:if test="${order.itemId == item.id}">
                                <tr data-order-id="${order.id}">
                                    <th scope="row" class="concrete-row-number">${counter}</th>
                                    <c:set var="counter" value="${counter + 1}" scope="page" />
                                    <td class="item-name">${item.name}</td>
                                    <td>${order.amount}</td>
                                    <td>${item.price / 100}</td>
                                    <td>${order.amount * item.price / 100}</td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-outline-secondary">Edit</button>
                                        <button type="button" class="btn btn-sm btn-outline-secondary btn-remove-item">Delete</button>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </tbody>
            </table>

        </main>
    </body>
</html>