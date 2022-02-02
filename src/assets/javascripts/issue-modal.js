const DEFAULT_PAGE_SIZE = 5;
const DEFAULT_START_PAGE = 0;
const DEFAULT_SORT_FIELD = 'id';
const DEFAULT_DIRECTION = 'asc';
let currentPage = DEFAULT_START_PAGE;
let currentSortField = DEFAULT_SORT_FIELD;
let currentSortDir = DEFAULT_DIRECTION;

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
    loadContent(DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD, DEFAULT_DIRECTION)

    prepareTableHeader('id');
    prepareTableHeader('doctor');
    prepareTableHeader('doctorDoctorSpecialization');
    prepareTableHeader('petName');
    prepareTableHeader('description');

    const $form = $('#issueForm');
    $form.on('submit', function (e) {
        deleteErrorMessages()
        e.preventDefault();
        const token = $("meta[name='_csrf']").attr("content");
        try {
            $.ajax({
                url: $form.attr('action'),
                type: 'post',
                headers: {
                    'X-CSRF-TOKEN': token
                },
                data: $form.serialize(),
                success: function (response) {
                    console.log(response)
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

function loadContent(page, size, sort, direction){
    currentPage = page;
    currentSortField = sort;
    currentSortDir = direction;
    try {
        $.ajax({
            url: `${window.location}/issue?page=${page}&size=${size}&sort=${sort}&dir=${direction}`,
            type: 'get',
            success: function (response) {
                $('.doctor-table-element').remove();
                $('.doctor-table-control').remove();
                response.content.forEach(item => {
                        addRow(item)
                })
                if(response.totalPages > 1){
                    let list = $('<ul/>')
                        .addClass(`doctor-table-control list-group flex-wrap list-group-horizontal`);
                    for (let i = 0; i < response.totalPages; i++) {
                        const li = $('<li/>')
                            .addClass('m-1')
                            .css('list-style', 'none')
                            .appendTo(list);
                        $('<button/>')
                            .text(i)
                            .addClass(`btn btn-${page === i ?  'light' : 'primary'}`)
                            .click(function () { loadContent(i, DEFAULT_PAGE_SIZE, currentSortField, currentSortDir) })
                            .appendTo(li);
                    }

                    list.appendTo($(`#doctorListContainer`));
                }
            },
            error: function (response) {
                console.error(response);
            }
        });
    } catch (e) {
        console.error(e);
    }
}

function prepareTableHeader(id){
    $(`#doctor-table-${id}`).click(function () {
        $(".asc").not(this).removeClass('asc')
        $(".desc").not(this).removeClass('desc')
        const dir = !$(this).hasClass('asc') && !$(this).hasClass('desc') ? 'asc' : $(this).hasClass('asc')? 'desc' : 'asc';
        loadContent(currentPage, DEFAULT_PAGE_SIZE, id, dir);
        if (!$(this).hasClass('asc') && !$(this).hasClass('desc')) {
            $(this).toggleClass('asc');
        } else {
            $(this).toggleClass('asc');
            $(this).toggleClass('desc');
        }
    })
}

function addRow(issue) {
    const $table = $('#doctorTable tbody');
    console.log('issue.description', issue.description)
    $table.append(
        `<tr class="doctor-table-element">
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
        console.log(item)
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
        console.log("deleteIssue")
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
