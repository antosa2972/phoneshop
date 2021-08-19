<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>
    Hello from product list!
</p>
<p>
    Found
    <c:out value="${phones.size()}"/> phones.
<table border="1px">
    <thead>
    <tr>
        <td>Image</td>
        <td>Brand</td>
        <td>Model</td>
        <td>Color</td>
        <td>Display size</td>
        <td>Price</td>
        <td>Quantity</td>
        <td>Action</td>
    </tr>
    </thead>
    <c:forEach var="phone" items="${phones}">
        <c:if test="${not empty phone.s}">

        </c:if>
        <tr>
            <td>
                <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
            </td>
            <td>${phone.brand}</td>
            <td>${phone.model}</td>
            <td>
                <c:forEach var="color" items ="${phone.colors}">
                    ${color.code},
                </c:forEach>
            </td>
            <td>${phone.displaySizeInches}</td>
            <td>$ ${phone.price}</td>
            <td>
                <input class="quantity" type="text" name="quantity"
                       value="${not empty error && phone.id == errorId ? param.quantity:1}">
                <input type="hidden" name="productId" value="${phone.id}"></td>
            <td>
                <button>Add</button>
            </td>
        </tr>
    </c:forEach>
</table>
</p>