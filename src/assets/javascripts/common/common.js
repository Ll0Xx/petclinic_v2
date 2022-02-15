//= require toast.min.js

const DEFAULT_PAGE_SIZE = 5;
const DEFAULT_START_PAGE = 0;
const DEFAULT_SORT_FIELD = 'id';
const DEFAULT_DIRECTION = 'asc';
const DEFAULT_KEYWORD = '';
let currentPage = DEFAULT_START_PAGE;
let currentSortField = DEFAULT_SORT_FIELD;
let currentSortDir = DEFAULT_DIRECTION;
let currentKeyword = DEFAULT_KEYWORD;


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
            loadContent(table, currentPage, DEFAULT_PAGE_SIZE, id, dir, currentKeyword, loadContentSuccess);
            if (!$(this).hasClass('asc') && !$(this).hasClass('desc')) {
                $(this).toggleClass('asc');
            } else {
                $(this).toggleClass('asc');
                $(this).toggleClass('desc');
            }
        })
    })
}

function loadContent(table, page, size, sort, direction, keyword, loadContentSuccess){
    currentPage = page;
    currentSortField = sort;
    currentSortDir = direction;
    currentKeyword = keyword;
    try {
        $.ajax({
            url: encodeURI(`${window.location}/${table}?page=${page}&size=${size}&sort=${sort}&dir=${direction}&keyword=${keyword}`),
            type: 'get',
            success: function (response) {
                updatePagingControls(response, table).then(() => {
                    loadContentSuccess(table, response.content)
                });
            },
            error: function (response) {
                console.error(response);
            }
        });
    } catch (e) {
        console.error(e);
    }
}

function updatePagingControls(response, table){
    $(`.${table}-table-element`).remove();
    $(`.${table}-table-control`).remove();
    if(response.totalPages > 1){
        let list = $('<ul/>').addClass(`${table}-table-control list-group flex-wrap list-group-horizontal`);
        for (let i = 0; i < response.totalPages; i++) {
            const li = $('<li/>')
                .addClass('m-1')
                .css('list-style', 'none')
                .appendTo(list);
            $('<button/>')
                .text(i)
                .addClass(`btn btn-${currentPage === i ?  'light' : 'primary'}`)
                .click(function () {
                    loadContent(table, i, DEFAULT_PAGE_SIZE, currentSortField, currentSortDir, currentKeyword, loadContentSuccess)
                })
                .appendTo(li);
        }

        list.appendTo($(`#${table}ListContainer`));
    }
    return new Promise((resolve) => {
        resolve();
    })
}

function showToast(message){
    const options = {
        settings: {
            duration: 2000,
        }
    };
    iqwerty.toast.toast(message, options);
}
