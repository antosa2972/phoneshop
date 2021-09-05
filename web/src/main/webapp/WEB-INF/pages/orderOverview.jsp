<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<tags:master pageTitle="Order overview">
    <head>
        <title><spring:theme code="page.order.overview"/></title>
    </head>
    <body>
    <br>
    <h2>
        <spring:theme code="order.page"/> ${id}
    </h2>
    <table class="table-bordered w-50">
        <tr>
            <td><spring:theme code="brand"/></td>
            <td><spring:theme code="model"/></td>
            <td><spring:theme code="color"/></td>
            <td><spring:theme code="displaySize"/></td>
            <td><spring:theme code="quantity"/></td>
            <td><spring:theme code="price"/></td>
        </tr>
        <c:forEach var="orderItem" items="${order.orderItems}">
            <tr>
                <td><c:out value="${orderItem.phone.brand}"/></td>
                <td><c:out value="${orderItem.phone.model}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${empty orderItem.phone.colors}">
                                   <span class="result-error">
                                       no info
                                   </span>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="color" items="${orderItem.phone.colors}">
                                <c:out value="${color.code},"/>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${orderItem.phone.displaySizeInches}"/>"</td>
                <td><c:out value="${orderItem.quantity}"/></td>
                <td><c:out value="${orderItem.phone.price.multiply(orderItem.quantity)}"/> <spring:theme code="usd"/></td>
            </tr>
        </c:forEach>
        <tr>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td><spring:theme code="order.subtotal"/></td>
            <td><c:out value="${order.subtotal} $"/></td>
        </tr>
        <tr>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td><spring:theme code="order.delivery"/></td>
            <td><c:out value="${order.deliveryPrice} $"/></td>
        </tr>
        <tr>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td style="border: none!important;"></td>
            <td><spring:theme code="order.total"/></td>
            <td><c:out value="${order.totalPrice} $"/></td>
        </tr>
    </table>
    <br>
    <br>
    <table class="table table-bordered table-borderless w-25">
        <tr>
            <td>
                <spring:theme code="order.data.firstName"/>
            </td>
            <td>
                <c:out value="${order.firstName}"/>
            </td>
        </tr>
        <tr>
            <td>
                <spring:theme code="order.data.lastName"/>
            </td>
            <td>
                <c:out value="${order.lastName}"/>
            </td>
        </tr>
        <tr>
            <td>
                <spring:theme code="order.data.address"/>
            </td>
            <td>
                <c:out value="${order.deliveryAddress}"/>
            </td>
        </tr>
        <tr>
            <td>
                <spring:theme code="order.data.phone"/>
            </td>
            <td>
                <c:out value="${order.contactPhoneNo}"/>
            </td>
        </tr>
        <tr>
            <td>
                <spring:theme code="order.data.additionalInfo"/>
            </td>
            <td>
                <c:out value="${order.additionalInfo}"/>
            </td>
        </tr>
    </table>
    <form action="${pageContext.servletContext.contextPath}/productList">
        <button class="btn btn-outline-primary">
            <spring:theme code="button.back.to.productList"/>
        </button>
    </form>
    </body>
</tags:master>
