<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tags:master pageTitle="Quick Order">
    <html>
    <head>
        <title><spring:theme code="page.fastOrder"/></title>
    </head>
    <br>
    <h2>
        <spring:theme code="page.fastOrder"/>
    </h2>
    <div id="success-result">
        <c:if test="${not empty success}">
            <spring:theme code="cart.update.success"/>
        </c:if>
        <c:if test="${not empty successfulPhones}">
            <c:forEach var="phone" items="${successfulPhones}">
                <c:out value="${phone.model} added successfuly"/>
                <br>
            </c:forEach>
        </c:if>
    </div>
    <div class="result-error">
        <c:if test="${not empty errors}">
            <spring:theme code="cart.update.error"/>
        </c:if>
    </div>
    <table>
        <tr>
            <td>
                <spring:theme code="model"/>
            </td>
            <td>
                <spring:theme code="quantity"/>
            </td>
        </tr>
        <form:form id="add-form" action="${pageContext.servletContext.contextPath}/quickOrder" method="post"
                   modelAttribute="quickOrderElementsDto">
            <c:forEach begin="0" end="10" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <tr>
                    <td>
                        <form:input path="quickOrderElements[${index}].model"/>
                        <div class="result-error">
                            <form:errors path="quickOrderElements[${index}].model"/>
                        </div>
                    </td>
                    <td>
                        <form:input path="quickOrderElements[${index}].quantity"/>
                        <div class="result-error">
                            <form:errors path="quickOrderElements[${index}].quantity"/>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </form:form>
    </table>
    <br>
    <button form="add-form" class="btn btn-outline-primary" type="submit">
        <spring:theme code="addToCart"/>
    </button>
    </html>
</tags:master>
