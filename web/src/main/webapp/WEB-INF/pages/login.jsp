<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:masterLogged pageTitle="404">
    <html>
    <head>
        <title><spring:theme code="loginPage"/></title>
    </head>
    <body>
    <br>
    <div id="error-result">
        <c:if test="${not empty error}">
            ${error}
        </c:if>
    </div>
    <form action="${pageContext.servletContext.contextPath}/login" method="post">
        <div><label> User Name : <input type="text" name="username"/> </label></div>
        <div><label> Password: <input type="password" name="password"/> </label></div>
        <div>
            <button class="btn btn-outline-primary" type="submit">Sign in</button>
        </div>
    </form>
    </body>
    </html>
</tags:masterLogged>
