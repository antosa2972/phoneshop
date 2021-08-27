<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
    <script type="text/javascript">
        <%@include file="/js/main.js"%>
    </script>
</head>
<body class="product-list">
<header>
    <div id="cart-div">
        <h2>
            <spring:theme code="cart"/>
            <c:out value=" ${cart.totalQuantity}, "/>
            <spring:theme code="items"/>
            <c:out value=" ${cart.totalCost}"/>
            <spring:theme code="usd"/>
        </h2>
    </div>
    <h1><a href="${pageContext.servletContext.contextPath}">
        PhoneShop
    </a>
    </h1>
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