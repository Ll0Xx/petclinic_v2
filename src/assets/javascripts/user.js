//= require common/common.js

$('#petModal').on('show.bs.modal', function (event) {
    const id = event.relatedTarget.getAttribute('data-bs-id');
    const name = event.relatedTarget.getAttribute('data-bs-name');
    $('#pet-modal-name').val(name);
    const type = event.relatedTarget.getAttribute('data-bs-type');
    $('#pet-modal-petType').val(type);
    if (id) {
        $('<input>').attr({
            type: 'hidden',
            id: 'id',
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
                    if(response.success === true){
                        $('#petModal').modal('toggle');
                        updatePetTable(response.result)
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

    if(table === 'pet'){
        $('.pet-table-delete').click(function () {
            deletePet($(this).attr('data-bs-id'));
        });
    }
}

function updatePetTable(pet) {
    const $row = getRow(pet.id);
    if ($row.length <= 0) {
        const $table = $('#petsTable tbody');
        if ($table.children().length <= 1) {
            $('#petListContainer').removeClass('d-none');
            $('#noPetsMessageContainer').addClass('d-none');
        }
        addRow(pet)
    } else {
        $row.find('.pet-name').text(pet.name);
        $row.find('.pet-type').text(pet.petType.name);
        $row.find('.btn-primary').attr('data-bs-name', pet.name);
        $row.find('.btn-primary').attr('data-bs-type', pet.petType.id);
    }
}

function addRow(pet) {
    const $table = $('#petsTable tbody');
    $table.append(
        `<tr class="pet-table-element">
            <td class="pet-id">${pet.id}</td>
            <td class="pet-name">${pet.name}</td>
            <td class="pet-type">${pet.petType.name}</td>
            <td>
                <button type="button" class="btn btn-primary" data-bs-id='${pet.id}' data-bs-name='${pet.name}'
                    data-bs-type='${pet.petType.id}' data-bs-toggle='modal' data-bs-target='#petModal'>
                        Edit
                </button>
                <button type="button" class="pet-table-delete btn btn-danger" data-bs-id='${pet.id}'>
                        delete
                </button>
            </td>
        </tr>`
    )
}

function addIssueRow(issue) {
    const $table = $('#issuesTable tbody');
    $table.append(
        `<tr class="issue-table-element">
            <td>${issue.id}</td>
            <td>${issue.doctor.user.email}</td>
            <td>${issue.doctor.doctorSpecialization.name}</td>
            <td>${issue.pet.name}</td>
            <td>${issue.description}</td>
        </tr>`
    )
}

function createErrorFields(response) {
    response.errors.forEach(item => {
        console.log(item)
        const $field = $(`#pet-modal-${item.field}`);
        $field.parent().append(`
            <p class="pet-modal-message text-danger mt-3">
                ${item.defaultMessage}
            </p>
        `);
        $field.addClass('is-invalid')
    })
}

function deleteErrorMessages(){
    $(`.pet-modal-message`).remove();
}

function deletePet(id) {
    const token = $("meta[name='_csrf']").attr("content");
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
        $('#petsListContainer').addClass('d-none');
        $('#noPetsMessageContainer').removeClass('d-none');
    }
}

function getRow(id){
    return $("#petsTable tr td").filter(function () {
        return $(this).text() == id;
    }).parent();
}
