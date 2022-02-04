//= require toast.min.js

const DEFAULT_PAGE_SIZE = 5;
const DEFAULT_START_PAGE = 0;
const DEFAULT_SORT_FIELD = 'id';
const DEFAULT_DIRECTION = 'asc';
let currentPage = DEFAULT_START_PAGE;
let currentSortField = DEFAULT_SORT_FIELD;
let currentSortDir = DEFAULT_DIRECTION;


const token = $("meta[name='_csrf']").attr("content");

function toJsonObject(formArray) {
    const returnArray = {};
    for (let i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;

}

function prepareTableHeaders(table, ids, loadContentSuccess){
    ids.forEach(id =>{
        $(`#${table}-table-${id}`).click(function () {
            $(".asc").not(this).removeClass('asc')
            $(".desc").not(this).removeClass('desc')
            const dir = !$(this).hasClass('asc') && !$(this).hasClass('desc') ? 'asc' : $(this).hasClass('asc')? 'desc' : 'asc';
            loadContent(table, currentPage, DEFAULT_PAGE_SIZE, id, dir, loadContentSuccess);
            if (!$(this).hasClass('asc') && !$(this).hasClass('desc')) {
                $(this).toggleClass('asc');
            } else {
                $(this).toggleClass('asc');
                $(this).toggleClass('desc');
            }
        })
    })
}

function loadContent(table, page, size, sort, direction, loadContentSuccess){
    currentPage = page;
    currentSortField = sort;
    currentSortDir = direction;
    try {
        $.ajax({
            url: `${window.location}/${table}?page=${page}&size=${size}&sort=${sort}&dir=${direction}`,
            type: 'get',
            success: function (response) {
                $(`.${table}-table-element`).remove();
                $(`.${table}-table-control`).remove();
                loadContentSuccess(table, response.content);
                if(response.totalPages > 1){
                    let list = $('<ul/>').addClass(`${table}-table-control list-group flex-wrap list-group-horizontal`);
                    for (let i = 0; i < response.totalPages; i++) {
                        const li = $('<li/>')
                            .addClass('m-1')
                            .css('list-style', 'none')
                            .appendTo(list);
                        $('<button/>')
                            .text(i)
                            .addClass(`btn btn-${page === i ?  'light' : 'primary'}`)
                            .click(function () {
                                loadContent(table, i, DEFAULT_PAGE_SIZE, currentSortField, currentSortDir, loadContentSuccess)
                            })
                            .appendTo(li);
                    }

                    list.appendTo($(`#${table}ListContainer`));
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

function showToast(message){
    const options = {
        settings: {
            duration: 2000,
        }
    };
    iqwerty.toast.toast(message, options);
}

const entityMap = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;',
    '/': '&#x2F;',
    '`': '&#x60;',
    '=': '&#x3D;'
};

function escapeHtml (string) {
    return String(string).replace(/[&<>"'`=\/]/g, function (s) {
        return entityMap[s];
    });
}
