<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Product List">
    <head>
        <title><spring:theme code="title"/></title>
        <script src="https://code.jquery.com/jquery-1.8.3.js"></script>
    </head>
    <body>
    <hr>
    <p>
    <form method="get">
        <div class="input-group mb-3 w-25">
            <input name="search" class="form-control" placeholder="search" aria-label="search"
                   aria-describedby="basic-addon2"
                   value="${not empty param.search ? param.search : ''}">
            <div class="input-group-append">
                <button class="btn btn-info"><spring:theme code="search"/></button>
            </div>
        </div>
    </form>
    <h2>
        <spring:theme code="found"/>
        <c:out value="${phoneQuantity}"/> <spring:theme code="phones"/>
    </h2>
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
                <tags:sortLink field="displaySizeInches" order="asc"/>
                <tags:sortLink field="displaySizeInches" order="desc"/>
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
            <tr>
                <td>
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                </td>
                <td><c:out value="${phone.brand}"/></td>
                <td><a href="${pageContext.servletContext.contextPath}/productDetails/${phone.id}"><c:out
                        value="${phone.model}"/></a></td>
                <td>
                    <c:forEach var="color" items="${phone.colors}">
                        <c:out value="${color.code}"/><br>
                    </c:forEach>
                </td>
                <td><c:out value="${phone.displaySizeInches}"/>"</td>
                <td>$ <c:out value="${phone.price}"/></td>
                <td>
                    <input class="quantity-input" type="text" id="quantity${phone.id}" name="quantity" value="1"/>
                    <div class="result-error" id="result${phone.id}"></div>
                    <input id="phoneId${phone.id}" name="phoneId" type="hidden" value="${phone.id}"/>
                </td>
                <td>
                    <button onclick="addToCart(${phone.id})" class="btn btn-success">
                        <spring:theme code="addToCart"/>
                    </button>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div class="pages-links">
        <c:set var="field" scope="request" value="field=${param.field}"/>
        <c:set var="search" scope="request" value="search=${param.search}"/>
        <c:set var="order" scope="request" value="field=${param.order}"/>
        <a href="${pageContext.request.contextPath}/productList?page=${empty param.page ? 1 : (param.page > 1 ? param.page - 1 : 1)}"><<<
            Previous page</a>
        <a href="${pageContext.request.contextPath}/productList?page=${empty param.page ? 2 : (param.page < pages ? param.page + 1 : pages)}">Next
            page >>></a>
    </div>
    </body>
</tags:master>