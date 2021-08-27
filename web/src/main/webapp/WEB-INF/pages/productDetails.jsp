<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="Product Details">
    <body>
    <header>
        <br>
        <form action="${pageContext.servletContext.contextPath}/productList" method="get">
            <button>BACK TO PRODUCT LIST</button>
        </form>
    </header>
    <div class="tableClass">
        <h2>Display</h2>
        <table>
            <tr>
                <td>Size</td>
                <td>${phone.displaySizeInches}</td>
            </tr>
            <tr>
                <td>Resolution</td>
                <td>${phone.displayResolution}</td>
            </tr>
            <tr>
                <td>Technology</td>
                <td>${phone.displayTechnology}</td>
            </tr>
            <tr>
                <td>Pixel density</td>
                <td>${phone.pixelDensity}</td>
            </tr>
        </table>
    </div>
    <div class="tableClass">
        <h2>Dimensions & Weight</h2>
        <table>
            <tr>
                <td>Length</td>
                <td>${phone.lengthMm}mm</td>
            </tr>
            <tr>
                <td>Width</td>
                <td>${phone.widthMm}mm</td>
            </tr>
            <tr>
                <td>Color</td>
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
                <td>Weight</td>
                <td>${phone.weightGr}</td>
            </tr>
        </table>
    </div>
    <div class="tableClass">
        <h2>Camera</h2>
        <table>
            <tr>
                <td>Front</td>
                <td>${phone.frontCameraMegapixels} megapixels</td>
            </tr>
            <tr>
                <td>Back</td>
                <td>${phone.backCameraMegapixels} megapixels</td>
            </tr>
        </table>
    </div>
    <div class="tableClass">
        <h2>Battery</h2>
        <table>
            <tr>
                <td>Talk Time</td>
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
                <td>Stand by</td>
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
                <td>Battery capacity</td>
                <td>${phone.batteryCapacityMah}mAh</td>
            </tr>
        </table>
    </div>
    <div class="tableClass">
        <h2>Other</h2>
        <table>
            <tr>
                <td>Device type</td>
                <td>${phone.deviceType}</td>
            </tr>
            <tr>
                <td>Bluetooth</td>
                <td>${phone.bluetooth}</td>
            </tr>
        </table>
    </div>
    </body>
</tags:master>