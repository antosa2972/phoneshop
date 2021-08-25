<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <title><spring:theme code="title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<p>
    <spring:theme code="helloMessage"/>
</p>
<p>
    <spring:theme code="found"/>
    <c:out value="${phoneQuantity}"/> <spring:theme code="phones"/>
</p>
<hr>
<p>
<div class="under-head">
    <form method="get">
        <input name="search" value="${not empty param.search ? param.search : ''}"/>
        <button><spring:theme code="search"/></button>
    </form>
    <div id="cart-div">
        <spring:theme code="cart"/>
        <c:out value="${cart.totalQuantity}"/>
        <spring:theme code="items"/>
        <c:out value="${cart.totalCost}"/>
        <spring:theme code="usd"/>
    </div>
</div>
<div id="success-result">
</div>
<div id="error-result">
</div>
<div id="ajax-errors">
</div>
<table border="1px">
    <thead>
    <tr>
        <td><spring:theme code="image"/></td>
        <td>
            <spring:theme code="brand"/>
            <tags:sortLink field="brand" order="asc"/>
            <tags:sortLink field="brand" order="desc"/>
        </td>
        <td>
            <spring:theme code="model"/>
            <tags:sortLink field="model" order="asc"/>
            <tags:sortLink field="model" order="desc"/>
        </td>
        <td><spring:theme code="color"/></td>
        <td>
            <spring:theme code="displaySize"/>
            <tags:sortLink field="display_size" order="asc"/>
            <tags:sortLink field="display_size" order="desc"/>
        </td>
        <td>
            <spring:theme code="price"/>
            <tags:sortLink field="price" order="asc"/>
            <tags:sortLink field="price" order="desc"/>
        </td>
        <td><spring:theme code="quantity"/></td>
        <td><spring:theme code="action"/></td>
    </tr>
    </thead>
    <c:forEach var="phone" items="${phones}">
        <form:form method="post" id="${phone.id}" modelAttribute="phoneDto">
            <tr>
                <td>
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                </td>
                <td><c:out value="${phone.brand}"/></td>
                <td><c:out value="${phone.model}"/></td>
                <td>
                    <c:forEach var="color" items="${phone.colors}">
                        <c:out value="${color.code}"/><br>
                    </c:forEach>
                </td>
                <td><c:out value="${phone.displaySizeInches}"/>"</td>
                <td>$ <c:out value="${phone.price}"/></td>
                <td>
                    <input class="quantity-input" type="text" id="quantity${phone.id}" name="quantity" value="1"/>
                    <div class="result-error" id="result${phone.id}">

                    </div>
                    <input id="phoneId${phone.id}" name="phoneId" type="hidden" value="${phone.id}"/>
                </td>
                <td>
                    <button>
                        <spring:theme code="addToCart"/>
                    </button>
                </td>
            </tr>
        </form:form>
    </c:forEach>
</table>
<div class="pages-links">
    <a href="${pageContext.request.contextPath}/productList?field=${not empty param.field ? param.field : ''}
    &order=${not empty param.order ? param.order : ''}&search=${not empty param.search ? param.search : ''}
    &page=${empty param.page ? 1 : (param.page > 1 ? param.page - 1 : 1)}"><<< Previous page</a>
    <a href="${pageContext.request.contextPath}/productList?field=${not empty param.field ? param.field : ''}
    &order=${not empty param.order ? param.order : ''}&search=${not empty param.search ? param.search : ''}
    &page=${empty param.page ? 2 : (param.page < pages ? param.page + 1 : pages)}">Next page >>></a>
</div>
</body>
<script src="https://code.jquery.com/jquery-1.8.3.js"></script>
<script>
    <c:forEach var="phone" items="${phones}">
    jQuery(document).ready(function ($) {
        $("#${phone.id}").submit(function (event) {
            event.preventDefault();
            addToCart(${phone.id});
        });
    })
    </c:forEach>

    function addToCart(phoneId) {
        var id = $("#phoneId" + phoneId).val();
        var quantity = $("#quantity" + phoneId).val();
        $.ajax({
            type: 'POST',
            url: 'ajaxCart',
            data: 'id=' + id + '&quantity=' + quantity,
            success: function (message) {
                $('#result' + phoneId).text('');
                $('#error-result').text('');
                $('#ajax-errors').text('');
                $('#success-result').text('Product added to cart successfully');
            },
            error: function (message) {
                $('#success-result').text('');
                $('#error-result').text('Error ' + message.status + ' while adding to cart');
                $('#ajax-errors').text(message.responseText);
                $('#result' + phoneId).text('Wrong input');
            }
        });
    }
</script>