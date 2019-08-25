$( document ).ready(function() {
    $('.btn-remove-item').click(removeFromCart);
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
            'Item &quot;' + $($(event.target).closest('tr'),'.item-name').first().text() + '&quot; removed from you cart\n' +
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

            refillRowNumbers();
        },
        error: function (data, textStatus) {
            var alertText = '<div class="alert alert-danger alert-dismissible fade show" role="alert">\n' +
                'Item &quot;' + $($(event.target).closest('tr'),'.item-name').first().text() + '&quot; not removed to you cart\n' +
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

function refillRowNumbers() {
    var rows = $('tr');
    for (var i = 0; i < rows.length; i++){
        // Element with index 0 is a header
       $('th.concrete-row-number', rows.get(i)).text(i);
    }
}