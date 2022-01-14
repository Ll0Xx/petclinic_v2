$(document).ready(function (){
    const $checkbox = $('#isDoctor');
    const $selector = $('#doctorSpecializationSelector');
    updateDoctorTypeSelectorVisibility($checkbox, $selector);

    $checkbox.change(function (){
        updateDoctorTypeSelectorVisibility($(this), $selector);
    })

    function updateDoctorTypeSelectorVisibility(checkbox, selector){
        if(checkbox.is(":checked")){
            selector.removeClass("d-none");
        }else{
            selector.addClass("d-none");
        }
    }
})
