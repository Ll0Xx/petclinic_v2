//= require common/common.js

$('#issueModal').on('show.bs.modal', function (event) {
    const id = event.relatedTarget.getAttribute('data-bs-id');
    const pet = event.relatedTarget.getAttribute('data-bs-pet');
    $('#issue-modal-pet').val(pet);
    const type = event.relatedTarget.getAttribute('data-bs-doctor');
    $('#issue-modal-doctor').val(type);
    const description = event.relatedTarget.getAttribute('data-bs-description');
    $('#issue-modal-description').val(description);
    if (id) {
        $('<input>').attr({
            type: 'hidden',
            id: 'id',
            name: 'id',
            value: id
        }).appendTo($('#issueForm'));
    }
});

$(document).ready(function () {
    loadContent('issue', DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD, DEFAULT_DIRECTION, addRow)
    prepareTableHeaders('issue', ['id', 'doctor', 'doctorDoctorSpecialization', 'petName', 'description'], addRow);

    const $form = $('#issueForm');
    $form.on('submit', function (e) {
        deleteErrorMessages()
        e.preventDefault();
        const token = $("meta[name='_csrf']").attr("content");
        try {
            $.ajax({
                url: $form.attr('action'),
                type: 'post',
                contentType: 'application/json',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                data: JSON.stringify(toJsonObject($form.serializeArray())),
                success: function (response) {
                    if(response.success){
                        $('#issueModal').modal('toggle');
                        updateIssueTable(response.result)
                    }else{
                        createErrorFields(response);
                    }
                },
                error: function (response) {
                    console.error(response);
                    $('#issueModal').modal('toggle');
                }
            });
        } catch (e) {
            console.error(e);
        }
    })
})

function addRow(table, issue) {
    const $table = $('#doctorTable tbody');
    $table.append(
        `<tr class="${table}-table-element">
            <td>${issue.id}</td>
            <td class="issue-doctor">${issue.doctor.user.email}</td>
            <td class="issue-doctorSpecialization">${issue.doctor.doctorSpecialization.name}</td>
            <td class="issue-pet">${issue.pet.name}</td>
            <td class="issue-description">${issue.description}</td>
            <td>
               <button type="button" class="btn btn-primary" data-bs-id='${issue.id}' data-bs-pet='${issue.pet.id}' 
                    data-bs-doctor='${issue.doctor.id}' data-bs-description='${issue.description}' data-bs-toggle="modal" 
                    data-bs-target="#issueModal">
                    Edit
                </button>
                <button type="button" class="btn btn-danger" data-bs-id=${issue.id}
                        onclick="deleteIssue('${issue.id}')">
                    Delete
                </button>
            </td>
        </tr>`
    )
}

function updateIssueTable(issue) {
    const $row = getRow(issue.id);
    if ($row.length <= 0) {
        const $table = $('#issueTable tbody');
        if ($table.children().length <= 1) {
            $('#issuesTableContainer').removeClass('d-none');
            $('#issuesListEmptyContainer').addClass('d-none');
        }
        addRow(issue)
    } else {
        $row.find('.issue-doctor').text(issue.doctor.user.email);
        $row.find('.issue-doctorSpecialization').text(issue.doctor.doctorSpecialization.name);
        $row.find('.issue-pet').text(issue.pet.name);
        $row.find('.issue-description').text(issue.description);

        $row.find('.btn-primary').attr('data-bs-pet', issue.pet.id);
        $row.find('.btn-primary').attr('data-bs-doctor', issue.doctor.id);
        $row.find('.btn-primary').attr('data-bs-description', issue.description);
    }
}

function createErrorFields(response) {
    response.errors.forEach(item => {
        const $field = $(`#issue-modal-${item.field}`);
        $field.parent().append(`
            <p class="issue-modal-message text-danger mt-3">
                ${item.defaultMessage}
            </p>
        `);
        $field.addClass('is-invalid')
    })
}

function deleteErrorMessages(){
    $(`.issue-modal-message`).remove();
}

function deleteIssue(id) {
    const token = $("meta[name='_csrf']").attr("content");
    if (confirm('Do you want to remove this pet from the database??')) {
        try {
            $.ajax({
                url: `doctor/issue/delete/${id}`,
                type: 'delete',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                success: function (response) {
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
    const $table = $('#issueTable tbody');
    if ($table.children().length <= 0) {
        $('#issuesTableContainer').addClass('d-none');
        $('#issuesListEmptyContainer').removeClass('d-none');
    }
}

function getRow(id){
    return $("#issueTable tr td").filter(function () {
        return $(this).text() == id;
    }).parent();
}
