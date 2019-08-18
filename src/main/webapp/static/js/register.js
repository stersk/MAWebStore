(function() {
    'use strict';

    window.addEventListener('load', function() {
        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.getElementsByClassName('needs-validation');

        // Loop over them and prevent submission
        var validation = Array.prototype.filter.call(forms, function(form) {
            form.addEventListener('submit', function(event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

function check(input) {
    var password = $('#password').val();
    var validationMessage = "";

    if (input.value == "") {
        validationMessage = "Password confirmation is required.";
    } else if (input.value != password) {
        validationMessage = "Password an confirmation password are different. Check your input.";
        input.setCustomValidity('');
    }

    input.setCustomValidity(validationMessage);
    $(input).parent().find('.invalid-feedback').first().text(validationMessage);
};
