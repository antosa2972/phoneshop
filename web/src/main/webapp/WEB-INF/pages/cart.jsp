<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tags:master pageTitle="Cart">
    <head>
        <title><spring:theme code="cartPage"/></title>
    </head>
    <body>
    <br>
    <form method="get" action="${pageContext.servletContext.contextPath}/productList">
        <button class="btn btn-outline-primary">
            <spring:theme code="buttonBackToProductList"/>
        </button>
    </form>
    <h2><spring:theme code="cart"/></h2>
    <div id="error-result">
        <c:if test="${error eq true}">
            <spring:theme code="cart.update.error"/>
        </c:if>
    </div>
    <div id="error-result">
        <c:if test="${empty cart}">
            <spring:theme code="cart.empty.msg"/>
        </c:if>
    </div>
    <div id="success-result">
        <c:if test="${successUpdate eq true}">
            <spring:theme code="cart.update.success"/>
        </c:if>
    </div>
    <div id="success-result">
        <c:if test="${successDelete eq true}">
            <spring:theme code="cart.delete.success"/>
        </c:if>
    </div>
    <form:form id="update-form" method="post" action="${pageContext.servletContext.contextPath}/cart"
               modelAttribute="phoneArrayDto">
        <table>
            <thead>
            <tr>
                <td>
                    <spring:theme code="brand"/>
                </td>
                <td>
                    <spring:theme code="model"/>
                </td>
                <td>
                    <spring:theme code="color"/>
                </td>
                <td>
                    <spring:theme code="displaySize"/>
                </td>
                <td>
                    <spring:theme code="price"/>
                </td>
                <td>
                    <spring:theme code="quantity"/>
                </td>
                <td>
                    <spring:theme code="action"/>
                </td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
                <tr>
                    <td>
                        <c:out value="${cartItem.phone.brand}"/>
                    </td>
                    <td>
                        <c:out value="${cartItem.phone.model}"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty cartItem.phone.colors}">
                                   <span class="result-error">
                                       no info
                                   </span>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="color" items="${cartItem.phone.colors}">
                                    <c:out value="${color.code},"/>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:out value="${cartItem.phone.displaySizeInches}"/>"
                    </td>
                    <td>
                        <c:out value="${cartItem.phone.price}"/> $
                    </td>
                    <td>
                        <input class="quantity-input" type="text" id="quantity${cartItem.phone.id}" name="quantity"
                               value="${not empty errorsId && errorsId.contains(cartItem.phone.id) ? paramValues['cartItem.quantity'][status.index] : cartItem.quantity }"/>
                        <div class="result-error" id="result${cartItem.phone.id}"></div>
                        <c:if test="${fn:contains(errorsId, cartItem.phone.id)}">
                            <div class="result-error">
                                <spring:theme code="wrongInputOrStock"/>
                            </div>
                        </c:if>
                        <input id="phoneId${cartItem.phone.id}" name="phoneId" type="hidden"
                               value="${cartItem.phone.id}"/>
                    </td>
                    <td>
                        <button class="btn btn-danger"
                                formaction="${pageContext.servletContext.contextPath}/cart/${cartItem.phone.id}">
                            <spring:theme code="deleteFromCartBtn"/>
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form:form>
    <button form="update-form" class="btn btn-secondary">
        <spring:theme code="updateCart"/>
    </button>
    <button form="update-form" class="btn btn-secondary">
        <spring:theme code="makeOrder"/>
    </button>
    </body>
</tags:master>