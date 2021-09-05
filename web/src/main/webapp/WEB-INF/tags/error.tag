<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="errors" required="true" type="org.springframework.validation.BindingResult" %>

<div class="result-error">
    <c:if test="${not empty errors.getFieldErrors(name)}">
        <c:if test="${errors.getFieldErrors(name)[0].code eq 'emptyField'}">
            <spring:theme code="order.error.noValue"/>
        </c:if>
        <c:if test="${errors.getFieldErrors(name)[0].code ne 'emptyField'}">
            <spring:theme code="order.error.wrongInput"/>
        </c:if>
    </c:if>
</div>