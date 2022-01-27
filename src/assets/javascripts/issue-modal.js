$('#issueModal').on('show.bs.modal', function (event) {
    const id = event.relatedTarget.getAttribute('data-bs-id');
    const pet = event.relatedTarget.getAttribute('data-bs-pet');
    $('#issuePet').val(pet);
    const type = event.relatedTarget.getAttribute('data-bs-doctor');
    $('#issueDoctor').val(type);
    const description = event.relatedTarget.getAttribute('data-bs-description');
    $('#issueDescription').val(description);
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
    const $form = $('#issueForm');
    $form.on('submit', function (e) {
        e.preventDefault();
        try {
            $.ajax({
                url: $form.attr('action'),
                type: 'post',
                data: $form.serialize(),
                success: function (response) {
                    $('#issueModal').modal('toggle');
                    updateIssueTable(response)
                    console.log('updateIssueTable')
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

function updateIssueTable(issue) {
    const $row = getRow(issue.id);
    if ($row.length <= 0) {
        const $table = $('#issueTable tbody');
        if ($table.children().length <= 1) {
            $('#issuesTableContainer').removeClass('d-none');
            $('#issuesListEmptyContainer').addClass('d-none');
        }
        $table.append(
            `<tr>
                    <td>${issue.id}</td>
                    <td class="issue-doctor">${issue.doctor.user.email}</td>
                    <td class="issue-doctorSpecialization">${issue.doctor.doctorSpecialization.name}</td>
                    <td class="issue-pet">${issue.pet.name}</td>
                    <td class="issue-description">${issue.description}</td>
                    <td>
                        <button type="button" class="btn btn-primary" data-bs-id=${issue.id} data-bs-pet=${issue.pet.id} data-bs-doctor=${issue.doctor.id} data-bs-description=${issue.description}
                                data-bs-toggle="modal" data-bs-target="#issueModal">
                            Edit
                        </button>
                        <button type="button" class="btn btn-danger" data-bs-id=${issue.id}
                                onclick="deleteIssue('${issue.id}')">
                            Delete
                        </button>
                    </td>
                </tr>`
        )
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
