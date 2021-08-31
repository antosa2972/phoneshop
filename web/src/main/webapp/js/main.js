function addToCart(phoneId) {
    var phoneDto = {}
    phoneDto["id"] = $("#phoneId" + phoneId).val();
    phoneDto["quantity"] = $("#quantity" + phoneId).val();
    $.ajax({
        type: 'POST',
        url: '/phoneshop-web/ajaxCart',
        data: JSON.stringify(phoneDto),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (dataReceived) {
            $('#cart-quantity').text(dataReceived['totalQuantity'] + ',');
            $('#cart-totalCost').text(dataReceived['totalCost']);
            $('#result' + phoneId).text('');
            $('#error-result').text('');
            $('#ajax-errors').text('');
            $('#success-result').text('Product added to cart successfully');
        },
        error: function (message) {
            $('#success-result').text('');
            $('#error-result').text('Error ' + message.status + ' while adding to cart');
            $('#ajax-errors').text(message.responseText);
            $('#result' + phoneId).text('Wrong input');
        }
    });
}