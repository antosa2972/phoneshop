<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
    <script src="https://code.jquery.com/jquery-1.8.3.js"></script>
    <script type="text/javascript">
        <%@include file="/js/main.js"%>
    </script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
</head>
<body>
<header>
    <div id="cart-div">
        <a href="${pageContext.servletContext.contextPath}/cart">
            <h2>
                <spring:theme code="cart"/>
                <div id="cart-quantity" class="l">
                    <c:out value="${cart.totalQuantity}"/>,
                </div>
                <spring:theme code="items"/>:
                <div id="cart-totalCost" class="l">
                    <c:out value="${cart.totalCost}"/>
                </div>
                <spring:theme code="usd"/>
            </h2>
        </a>
    </div>
    <h2>
        <a href="${pageContext.servletContext.contextPath}">
            PhoneShop
        </a>
    </h2>
    <spring:theme code="helloMessage"/>
</header>
<main>
    <jsp:doBody/>
</main>
<footer>
    <p>
        (c) Expert soft
    </p>
</footer>
</body>
</html>