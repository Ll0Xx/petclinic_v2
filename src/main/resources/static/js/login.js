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
                contentType: 'application/json',
                type: 'post',
                data: JSON.stringify(toJsonObject($form.serializeArray())),
                success: function (response) {
                    createErrorFields(response);
                },
                error: function (response) {
                    console.error("error", response);
                }
            });
        } catch (e) {
            console.error(e);
        }
    })

    function createErrorFields(response) {
        response.errors.forEach(item => {
            addErrorMessage(item.field, item.defaultMessage)
            if (item.code === "FieldsValueMatch") {
                addErrorMessage("confirmPassword", item.defaultMessage)
                addErrorMessage("password", item.defaultMessage)
            }
        })
    }

    function addErrorMessage(field, message){
        const $field = $(`#sign-up-${field}`);
        $field.parent().append(`
            <p class="sign-up-error-message text-danger">
                ${message}
            </p>
        `);
        $field.addClass('is-invalid')
    }

    function deleteErrorMessages(){
        $(`.sign-up-error-message`).remove();
        console.log('Delete all error messages');
    }
})

function toJsonObject(formArray) {
    const returnArray = {};
    for (let i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
}
