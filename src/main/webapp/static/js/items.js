$( document ).ready(function() {
    $('.btn-add-to-cart').click(addToCart);
});

function addToCart(event) {
    $.ajax({
        type: 'post',
        url: 'items',
        dataType : "json",
        data: {itemId: $(event.target).attr('data-id'),
               action: 'addToCart'},
        success: function(data, textStatus) {
            var alertText = '<div class="alert alert-success alert-dismissible fade show" role="alert">\n' +
            'Item &quot;' + data.name + '&quot; added to you cart\n' +
            '   <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
            '       <span aria-hidden="true">&times;</span>\n' +
            '   </button>\n' +
            '</div>\n';

            $('#alert-container').html(alertText);
            $('.alert').alert();
            setTimeout(function () {
                $('.alert').alert('close');
            }, 5000);
        },
        error: function (data, textStatus) {
            var alertText = '<div class="alert alert-danger alert-dismissible fade show" role="alert">\n' +
                'Item &quot;' + data.name + '&quot; not added to you cart\n' +
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