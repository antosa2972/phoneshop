<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:masterLogged pageTitle="Orders">
    <html>
    <head>
        <title><spring:theme code="admin.ordersPage"/></title>
    </head>
    <body>
    <h2>
        <spring:theme code="admin.ordersPage"/>:
    </h2>
    <table>
        <tr>
            <td>
                <spring:theme code="admin.orderNumber"/>
            </td>
            <td>
                <spring:theme code="admin.customer"/>
            </td>
            <td>
                <spring:theme code="admin.phoneNum"/>
            </td>
            <td>
                <spring:theme code="admin.address"/>
            </td>
            <td>
                <spring:theme code="admin.date"/>
            </td>
            <td>
                <spring:theme code="admin.totalPrice"/>
            </td>
            <td>
                <spring:theme code="admin.status"/>
            </td>
        </tr>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/admin/orders/${order.id}">
                        <c:out value="${order.id}"/>
                    </a>
                </td>
                <td>
                    <c:out value="${order.firstName}"/>
                    <c:out value="${order.lastName}"/>
                </td>
                <td>
                    <c:out value="${order.contactPhoneNo}"/>
                </td>
                <td>
                    <c:out value="${order.deliveryAddress}"/>
                </td>
                <td>
                    <c:out value="${order.date.toString()}"/>
                </td>
                <td>
                    <c:out value="${order.totalPrice}"/>
                </td>
                <td>
                    <c:out value="${order.status.name()}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
    </body>
    </html>
</tags:masterLogged>
