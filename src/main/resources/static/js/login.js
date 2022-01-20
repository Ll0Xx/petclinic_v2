$(document).ready(function (){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    if(urlParams.has('error')){
        $("#login-error-message").removeClass("d-none");
        $("#login-email").addClass('is-invalid');
        $("#login-password").addClass('is-invalid');
    }

    const $checkbox = $('#isDoctor');
    const $selector = $('#doctorSpecializationSelector');
    updateDoctorTypeSelectorVisibility($checkbox, $selector);

    $checkbox.change(function (){
        updateDoctorTypeSelectorVisibility($(this), $selector);
    })

    function updateDoctorTypeSelectorVisibility(checkbox, selector){
        if(checkbox.is(":checked")){
            selector.removeClass("d-none");
            selector.attr("name", "doctorSpecialization")
            selector.attr("required")
        }else{
            selector.addClass("d-none");
            selector.removeAttr("name")
            selector.removeAttr("required")
        }
    }

    const $form = $('#registerForm');
    $form.on('submit', function (e) {
        deleteErrorMessages();
        e.preventDefault();
        try {
            $.ajax({
                url: $form.attr('action'),
                type: 'post',
                data: $form.serialize(),
                success: function (response) {
                    console.log("success", response)
                    response.errors.forEach(item =>{
                        addErrorMessage(item.field, item.defaultMessage)
                        if (item.code === "FieldsValueMatch") {
                            addErrorMessage("confirmPassword", item.defaultMessage)
                            addErrorMessage("password", item.defaultMessage)
                        }
                    })
                    // $form.addClass('was-validated');
                },
                error: function (response) {
                    console.error("error", response);
                }
            });
        } catch (e) {
            console.error(e);
        }
    })

    function addErrorMessage(field, message){
        console.log(`Create error message for field ${field} with message ${message}`);
        $(`#sign-up-${field}`).parent().append(`
            <p class="sign-up-error-message text-danger">
                ${message}
            </p>
        `);
        $(`#sign-up-${field}`).addClass('is-invalid')
    }

    function deleteErrorMessages(){
        $(`.sign-up-error-message`).remove();
        console.log('Delete all error messages');
    }
})
