$('#petModal').on('show.bs.modal', function (event) {
    const id = event.relatedTarget.getAttribute('data-bs-id');
    const name = event.relatedTarget.getAttribute('data-bs-name');
    $('#pet-modal-name').val(name);
    const type = event.relatedTarget.getAttribute('data-bs-type');
    $('#pet-modal-petType').val(type);
    console.log(id);
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
    const $form = $('#petForm');
    $form.on('submit', function (e) {
        e.preventDefault();
        deleteErrorMessages();
        try {
            $.ajax({
                url: $form.attr('action'),
                type: 'post',
                data: $form.serialize(),
                success: function (response) {
                    console.log(response)
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

function updatePetTable(pet) {
    const $row = getRow(pet.id);
    if ($row.length <= 0) {
        const $table = $('#petsTable tbody');
        if ($table.children().length <= 1) {
            $('#petsListContainer').removeClass('d-none');
            $('#noPetsMessageContainer').addClass('d-none');
        }
        $table.append(
            `<tr>
                    <td class="pet-id">${pet.id}</td>
                    <td class="pet-name">${pet.name}</td>
                    <td class="pet-type">${pet.petType.name}</td>
                    <td>
                        <button type="button" class="btn btn-primary" data-bs-id='${pet.id}' data-bs-name='${pet.name}'
                            data-bs-type='${pet.petType.id}' data-bs-toggle='modal' data-bs-target='#petModal'>
                            Edit
                        </button>
                        <button type="button" class="btn btn-danger" th:attr="data-bs-id=${pet.id}"
                                onclick="deletePet(${pet.id})">
                            delete
                        </button>
                    </td>
                </tr>`
        )
    } else {
        $row.find('.pet-name').text(pet.name);
        $row.find('.pet-type').text(pet.petType.name);
        $row.find('.btn-primary').attr('data-bs-name', pet.name);
        $row.find('.btn-primary').attr('data-bs-type', pet.petType.id);
    }
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
                    console.log('item deleted', response);
                    deleteRow(response)
                },
                error: function (response) {
                    console.error(response);
                }
            });
        } catch (e) {
            console.error(e);
        }
    }
}

function deleteRow(id){
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