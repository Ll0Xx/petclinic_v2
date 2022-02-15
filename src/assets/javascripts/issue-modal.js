//= require common/common.js

$('#issueModal').on('show.bs.modal', function (event) {
    const id = event.relatedTarget.getAttribute('data-bs-id');
    const pet = event.relatedTarget.getAttribute('data-bs-pet');
    $('#issue-modal-pet').val(pet);
    const type = event.relatedTarget.getAttribute('data-bs-doctor');
    $('#issue-modal-doctor').val(type);
    const description = event.relatedTarget.getAttribute('data-bs-description');
    $('#issue-modal-description').val(description);
    $('#issue-modal-id').remove();
    if (id) {
        $('<input>').attr({
            type: 'hidden',
            id: 'issue-modal-id',
            name: 'id',
            value: id
        }).appendTo($('#issueForm'));
    }
});

$(document).ready(function () {
    loadContent('issue', DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD, DEFAULT_DIRECTION, DEFAULT_KEYWORD, loadContentSuccess)
    prepareTableHeaders('issue', ['id', 'doctor', 'doctorDoctorSpecialization', 'petName', 'description'], loadContentSuccess);

    const $form = $('#issueForm');
    $form.on('submit', function (e) {
        deleteErrorMessages()
        e.preventDefault();
        const action = $("#issue-modal-id").length > 0   ? 'update' : 'create';
        try {
            $.ajax({
                url: `${window.location}/issue/${action}`,
                type: 'post',
                contentType: 'application/json',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                data: JSON.stringify(toJsonObject($form.serializeArray())),
                success: function (response) {
                    if(response.success){
                        $('#issueModal').modal('toggle');
                        if($('#issue-modal-id').val()){
                            updateIssueTable(response.result)
                        } else {
                            loadLastPage()
                        }
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

    $("#issue-search-form").on('submit', function (e) {
        deleteErrorMessages()
        e.preventDefault();
        const keyword =  $("#issue-search-keyword").val();
        loadContent('issue', DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD, DEFAULT_DIRECTION, keyword, loadContentSuccess)
    });
})

function loadLastPage(){
    try {
        $.ajax({
            url: `${window.location}/issue/last`,
            contentType: 'application/json',
            type: 'get',
            headers: {
                'X-CSRF-TOKEN': token
            },
            success: function (response) {
                updatePagingControls(response, 'issue' ).then(() => {
                    loadContentSuccess('issue', response.content)
                })
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

function loadContentSuccess(table, items){
    items.forEach(item => {
        addRow(table, item)
    })
    $('.issue-table-delete').click(function () {
        deleteIssue($(this).attr('data-bs-id'));
    });
}

function addRow(table, issue) {
    $("#doctorTable").find('tbody')
        .append($('<tr>')
            .addClass(`${table}-table-element`)
            .append($('<td>').addClass('issue-id').text(issue.id))
            .append($('<td>').addClass('issue-doctor').text(issue.doctor.user.email))
            .append($('<td>').addClass('issue-doctorSpecialization').text(issue.doctor.doctorSpecialization.name))
            .append($('<td>').addClass('issue-pet').text(issue.pet.name))
            .append($('<td>').addClass('issue-description').text(issue.description))
            .append($('<td>')
                .append($('<button>').text('edit').addClass('btn btn-primary').attr(
                    {
                        'data-bs-id': issue.id,
                        'data-bs-pet': issue.pet.id,
                        'data-bs-doctor': issue.doctor.id,
                        'data-bs-description': issue.description,
                        'data-bs-toggle': 'modal',
                        'data-bs-target': '#issueModal'
                    }))
                .append($('<button>').text('delete').addClass('btn btn-danger')
                    .attr('data-bs-id', issue.id).click(function () { deleteIssue(issue.id) })))
        );
}

function updateIssueTable(issue) {
    const $row = getRow(issue.id);
    console.log(issue);
    console.log($row.length);
    if ($row.length <= 0) {
        console.log('add new')
        showTable()
        addRow('issue', issue)
    } else {
        console.log('update old')
        $row.find('.issue-doctor').text(issue.doctor.user.email);
        $row.find('.issue-doctorSpecialization').text(issue.doctor.doctorSpecialization.name);
        $row.find('.issue-pet').text(issue.pet.name);
        $row.find('.issue-description').text(issue.description);

        $row.find('.btn-primary').attr('data-bs-pet', issue.pet.id);
        $row.find('.btn-primary').attr('data-bs-doctor', issue.doctor.id);
        $row.find('.btn-primary').attr('data-bs-description', issue.description);
    }
}

function showTable(){
    const $table = $('#issueTable tbody');
    if ($table.children().length <= 1) {
        $('#issuesTableContainer').removeClass('d-none');
        $('#issuesListEmptyContainer').addClass('d-none');
    }
}

function createErrorFields(response) {
    response.errors.forEach(item => {
        const $field = $(`#issue-modal-${item.field}`);
        $field.parent().append($('<p>').addClass('issue-modal-message text-danger mt-3').text(item.defaultMessage));
        $field.addClass('is-invalid')
    })
}

function deleteErrorMessages(){
    $(`.issue-modal-message`).remove();
}

function deleteIssue(id) {
    if (confirm('Do you want to remove this issue?')) {
        try {
            $.ajax({
                url: `doctor/issue/delete/${id}`,
                type: 'delete',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                success: function (response) {
                    loadContent('issue', currentPage, DEFAULT_PAGE_SIZE, currentSortField, currentSortDir, currentKeyword, loadContentSuccess)

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

function getRow(id){
    return $("#doctorTable tr td").filter(function () {
        return $(this).text() == id;
    }).parent();
}
