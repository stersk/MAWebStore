$( document ).ready(function() {
    $('.btn-remove-item').click(removeFromCart);
    $('.btn-edit-item').click(openEditDialog);
    $('#item-amount').change(onAmountChange);
    $('#amount-edit-commit-btn').click(onItemAmountCommit);
    $('#confirm-btn').click(setCartConfirmOperation);
    $('#discard-btn').click(setCartDiscardOperation);
});

function removeFromCart(event) {
    $.ajax({
        type: 'post',
        url: 'cart',
        dataType : "json",
        data: {orderId: $(event.target).closest('tr').attr('data-order-id'),
               action: 'removeFromOpenCart'},
        success: function(data, textStatus) {
            var alertText = '<div class="alert alert-success alert-dismissible fade show" role="alert">\n' +
            'Item &quot;' + $('.item-name', $(event.target).closest('tr')).first().text() + '&quot; removed from you cart\n' +
            '   <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
            '       <span aria-hidden="true">&times;</span>\n' +
            '   </button>\n' +
            '</div>\n';

            $('#alert-container').html(alertText);
            $('.alert').alert();
            setTimeout(function () {
                $('.alert').alert('close');
            }, 5000);

            $(event.target).closest('tr').remove();

            refillRowNumbersAndConfirmationVisibility();
            updateTotalCartSumElement(data.cartSum);
        },
        error: function (data, textStatus) {
            var alertText = '<div class="alert alert-danger alert-dismissible fade show" role="alert">\n' +
                'Item &quot;' + $('.item-name', $(event.target).closest('tr')).first().text() + '&quot; not removed to you cart\n' +
                '   <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                '       <span aria-hidden="true">&times;</span>\n' +
                '   </button>\n' +
                '</div>\n';

            $('#alert-container').html(alertText);
            $('.alert').alert();
            setTimeout(function () {
                $('.alert').alert('close');
            }, 5000);
        }
    });
}

function refillRowNumbersAndConfirmationVisibility() {
    var rows = $('tr');

    // Element with index 0 is a header
    for (var i = 0; i < rows.length; i++){
        $('th.concrete-row-number', rows.get(i)).text(i);
    }

    if (rows.length == 1) { // Element with index 0 is a header
        $('#confirm-btn').addClass('d-none');
        $('#discard-btn').addClass('d-none');
    } else {
        $('#confirm-btn').removeClass('d-none');
        $('#discard-btn').removeClass('d-none');
    }
}

function openEditDialog(event) {
    var rowElement = $(event.target).closest('tr');

    $('#changeAmountModal').attr('data-order-id', rowElement.attr('data-order-id'));

    $('#changeAmountModalLabel').text("Change amount of " + $('.item-name',rowElement).first().text().trim());
    $('#itemPrice').val($('.item-price',rowElement).first().text().trim());
    $('#item-amount').val($('.item-amount',rowElement).first().text().trim());

    $('#itemSum').val(($('#itemPrice').val() * $('#item-amount').val()).toFixed());

    $('#changeAmountModal').modal('show');
}

function onAmountChange(event) {
    $('#itemSum').val(($('#itemPrice').val() * $(event.target).val()).toFixed(2));
}

function onItemAmountCommit(event) {
    var rowElement = $('tr[data-order-id=' + $(event.target).closest('.modal').first().attr('data-order-id') + ']').first();
    var amountElement = $('.item-amount', rowElement).first();

    if (amountElement.text().trim() == $('#item-amount').val()) {
        $('#changeAmountModal').modal('hide');
        return;
    }

    $.ajax({
        type: 'post',
        url: 'cart',
        dataType : "json",
        data: {orderId: rowElement.attr('data-order-id'),
               amount: $('#item-amount').val(),
               action: 'updateItemAmountInOrder'},
        success: function(data, textStatus) {
            var alertText = '';

            if ($('#item-amount').val() == 0) {
                alertText = '<div class="alert alert-success alert-dismissible fade show" role="alert">\n' +
                    'Item &quot;' + $('.item-name', rowElement).first().text() + '&quot; removed from your cart\n' +
                    '   <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                    '       <span aria-hidden="true">&times;</span>\n' +
                    '   </button>\n' +
                    '</div>\n';

                rowElement.remove();

            } else {
                alertText = '<div class="alert alert-success alert-dismissible fade show" role="alert">\n' +
                    'Item &quot;' + $('.item-name', rowElement).first().text() + '&quot; amount updated\n' +
                    '   <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                    '       <span aria-hidden="true">&times;</span>\n' +
                    '   </button>\n' +
                    '</div>\n';

                var price = $('.item-price').first().text().trim();
                var newAmount = $('#item-amount').val();
                var newSum = (newAmount * price).toFixed(2);

                amountElement.text(newAmount);
                $('.item-sum', rowElement).first().text(newSum);
            }

            $('#alert-container').html(alertText);
            $('.alert').alert();
            setTimeout(function () {
                $('.alert').alert('close');
            }, 5000);

            $(event.target).closest('tr').remove();

            refillRowNumbersAndConfirmationVisibility();
            updateTotalCartSumElement(data.cartSum);
            $('#changeAmountModal').modal('hide');
        },
        error: function (data, textStatus) {
            var alertText = '<div class="alert alert-danger alert-dismissible fade show" role="alert">\n' +
                'Item &quot;' + $('.item-name', rowElement).first().text() + '&quot; amount not changed\n' +
                '   <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                '       <span aria-hidden="true">&times;</span>\n' +
                '   </button>\n' +
                '</div>\n';

            $('#alert-container').html(alertText);
            $('.alert').alert();
            setTimeout(function () {
                $('.alert').alert('close');
            }, 5000);
        }
    });
}

function updateTotalCartSumElement(value) {
    var totalSum =( value / 100).toLocaleString(undefined, {
        style: 'currency',
        currencyDisplay: 'symbol',
        currency: 'USD',
    });

    $('#cart-sum').text('Total cart sum: ' + totalSum);
}

function setCartConfirmOperation(event) {
    $('#cart-operation-action').val('confirm');
}

function setCartDiscardOperation(event) {
    $('#cart-operation-action').val('discard');
}