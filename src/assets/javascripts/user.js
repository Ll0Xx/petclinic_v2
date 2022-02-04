//= require common/common.js

$('#petModal').on('show.bs.modal', function (event) {
    const id = event.relatedTarget.getAttribute('data-bs-id');
    const name = event.relatedTarget.getAttribute('data-bs-name');
    $('#pet-modal-name').val(name);
    const type = event.relatedTarget.getAttribute('data-bs-type');
    $('#pet-modal-petType').val(type);
    $('#pet-modal-id').remove();
    if (id) {
        $('<input>').attr({
            type: 'hidden',
            id: 'pet-modal-id',
            name: 'id',
            value: id
        }).appendTo($('#petForm'));
    }
});

$(document).ready(function () {
    loadContent('pet', DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD, DEFAULT_DIRECTION, loadContentSuccess);
    loadContent('issue', DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD, DEFAULT_DIRECTION, loadContentSuccess);

    prepareTableHeaders('pet', ['id', 'name', 'petType'], loadContentSuccess);
    prepareTableHeaders('issue', ['id', 'doctor', 'doctorDoctorSpecialization', 'petName', 'description'], loadContentSuccess);

    const $form = $('#petForm');
    $form.on('submit', function (e) {
        e.preventDefault();
        deleteErrorMessages();
        try {
            $.ajax({
                url: $form.attr('action'),
                contentType: 'application/json',
                type: 'post',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                data:JSON.stringify(toJsonObject($form.serializeArray())),
                success: function (response) {
                    console.log()
                    if(response.success === true){
                        console.log(response)
                        $('#petModal').modal('toggle');
                        if($('#pet-modal-id').val()){
                            updatePetTable(response.result)
                        } else {
                            loadLastPage()
                        }
                    }else{
                        createErrorFields(response);
                    }
                },
                error: function (response) {
                    console.error(response);
                    $('#petModal').modal('toggle');
                }
            });
        } catch (e) {
            console.error(e);
        }
    })
})

function loadContentSuccess(table, items){
    items.forEach(item => {
        if (table === 'pet') {
            addRow(item)
        } else {
            addIssueRow(item)
        }
    })
}

function loadLastPage(){
    try {
        $.ajax({
            url: `${window.location}/pet/latest`,
            contentType: 'application/json',
            type: 'get',
            headers: {
                'X-CSRF-TOKEN': token
            },
            success: function (response) {
                updatePagingControls(response, 'pet', loadContentSuccess)
                showTable()
            },
            error: function (response) {
                console.error(response);
            }
        });
    } catch (e) {
        console.error(e);
    }
}

function updatePetTable(pet) {
    const $row = getRow(pet.id);
    if ($row.length <= 0) {
        showTable()
        addRow(pet)
    } else {
        $row.find('.pet-name').text(pet.name);
        $row.find('.pet-type').text(pet.petType.name);
        $row.find('.btn-primary').attr('data-bs-name', pet.name);
        $row.find('.btn-primary').attr('data-bs-type', pet.petType.id);
    }
}

function showTable(){
    const $table = $('#petsTable tbody');
    if ($table.children().length <= 1) {
        $('#petListContainer').removeClass('d-none');
        $('#noPetsMessageContainer').addClass('d-none');
    }
}

function addRow(pet) {
    $("#petsTable").find('tbody')
        .append($('<tr>')
            .addClass('pet-table-element')
            .append($('<td>').addClass('pet-id').text(pet.id))
            .append($('<td>').addClass('pet-name').text(pet.name))
            .append($('<td>').addClass('pet-type').text(pet.petType.name))
            .append($('<td>')
                .append($('<button>').text('edit').addClass('btn btn-primary').attr(
                    {
                        'data-bs-id': pet.id,
                        'data-bs-name': pet.name,
                        'data-bs-type': pet.petType.id,
                        'data-bs-toggle': 'modal',
                        'data-bs-target': '#petModal'
                    }))
                .append($('<button>').text('delete').addClass('pet-table-delete btn btn-danger')
                .attr('data-bs-id', pet.id).click(function () { deletePet(pet.id) })))
        );
}

function addIssueRow(issue) {
    $("#issuesTable").find('tbody')
        .append($('<tr>')
            .addClass('issue-table-element')
            .append($('<td>').text(issue.id))
            .append($('<td>').text(issue.doctor.user.email))
            .append($('<td>').text(issue.doctor.doctorSpecialization.name))
            .append($('<td>').text(issue.pet.name))
            .append($('<td>').text(issue.description)))

}

function createErrorFields(response) {
    response.errors.forEach(item => {
        const $field = $(`#pet-modal-${item.field}`);
        $field.parent().append($('<p>').addClass('pet-modal-message text-danger mt-3').text(item.defaultMessage));
        $field.addClass('is-invalid')
    })
}

function deleteErrorMessages(){
    $(`.pet-modal-message`).remove();
}

function deletePet(id) {
    if (confirm('Do you want to remove this pet from the database??')) {
        try {
            $.ajax({
                url: `user/pet/delete/${id}`,
                type: 'delete',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                success: function (response) {
                    deletePetTableRow(response)
                },
                error: function (response) {
                    showToast(response.responseJSON.message)
                    console.error(response);
                }
            });
        } catch (e) {
            console.error(e);
        }
    }
}

function deletePetTableRow(id){
    const $row = getRow(id);
    $row.remove();
    const $table = $('#petsTable tbody');
    if ($table.children().length <= 0) {
        $('#petListContainer').addClass('d-none');
        $('#noPetsMessageContainer').removeClass('d-none');
    }
}

function getRow(id){
    return $("#petsTable tr td").filter(function () {
        return $(this).text() == id;
    }).parent();
}
