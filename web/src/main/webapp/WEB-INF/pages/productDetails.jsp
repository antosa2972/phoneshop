<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<tags:master pageTitle="Product Details">
    <body>
    <header>
        <br>
        <form action="${pageContext.servletContext.contextPath}/productList" method="get">
            <button><spring:theme code="buttonBackToProductList"/></button>
        </form>
    </header>
    <h3>
        <strong>${phone.model}</strong>
    </h3>
    <img class="img"
         src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
    <br>
    <textarea disabled readonly rows="4" cols="50">
            ${phone.description}
    </textarea>
    <br>
    <div>
        <spring:theme code="price"/>:<strong>${phone.price}</strong> $
        <br>
        <div class="quantity-input">
            <input type="text" id="quantity${phone.id}" name="quantity" value="1"/>
            <div class="result-error" id="result${phone.id}"></div>
            <input id="phoneId${phone.id}" name="phoneId" type="hidden" value="${phone.id}"/>
            <button onclick="addToCart(${phone.id})">Add to cart</button>
        </div>
    </div>
    <div class="table-class">
        <h2><spring:theme code="display"/></h2>
        <table>
            <tr>
                <td><spring:theme code="size"/></td>
                <td>${phone.displaySizeInches}</td>
            </tr>
            <tr>
                <td><spring:theme code="resolution"/></td>
                <td>${phone.displayResolution}</td>
            </tr>
            <tr>
                <td><spring:theme code="technology"/></td>
                <td>${phone.displayTechnology}</td>
            </tr>
            <tr>
                <td><spring:theme code="pixelDensity"/></td>
                <td>${phone.pixelDensity}</td>
            </tr>
        </table>
    </div>
    <div class="table-class">
        <h2><spring:theme code="dimensionAndWeight"/></h2>
        <table>
            <tr>
                <td><spring:theme code="length"/></td>
                <td>${phone.lengthMm}mm</td>
            </tr>
            <tr>
                <td><spring:theme code="width"/></td>
                <td>${phone.widthMm}mm</td>
            </tr>
            <tr>
                <td><spring:theme code="color"/></td>
                <td>
                    <c:choose>
                        <c:when test="${empty phone.colors}">
                                   <span class="result-error">
                                       no info
                                   </span>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="color" items="${phone.colors}">
                                <c:out value="${color.code},"/>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td><spring:theme code="weight"/></td>
                <td>${phone.weightGr}</td>
            </tr>
        </table>
    </div>
    <div class="table-class">
        <h2><spring:theme code="camera"/></h2>
        <table>
            <tr>
                <td><spring:theme code="front"/></td>
                <td>${phone.frontCameraMegapixels} megapixels</td>
            </tr>
            <tr>
                <td><spring:theme code="back"/></td>
                <td>${phone.backCameraMegapixels} megapixels</td>
            </tr>
        </table>
    </div>
    <div class="table-class">
        <h2><spring:theme code="battery"/></h2>
        <table>
            <tr>
                <td><spring:theme code="talkTime"/></td>
                <td>
                    <c:choose>
                        <c:when test="${empty phone.talkTimeHours}">
                                   <span class="result-error">
                                       no info
                                   </span>
                        </c:when>
                        <c:otherwise>
                            ${phone.talkTimeHours} hours
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td><spring:theme code="standBy"/></td>
                <td>
                    <c:choose>
                        <c:when test="${empty phone.standByTimeHours}">
                                   <span class="result-error">
                                       no info
                                   </span>
                        </c:when>
                        <c:otherwise>
                            ${phone.standByTimeHours} hours
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td><spring:theme code="batteryCapacity"/></td>
                <td>${phone.batteryCapacityMah}mAh</td>
            </tr>
        </table>
    </div>
    <div class="table-class">
        <h2><spring:theme code="other"/></h2>
        <table>
            <tr>
                <td><spring:theme code="deviceType"/></td>
                <td>${phone.deviceType}</td>
            </tr>
            <tr>
                <td><spring:theme code="bluetooth"/></td>
                <td>${phone.bluetooth}</td>
            </tr>
        </table>
    </div>
    </body>
</tags:master>