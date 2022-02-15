$(document).ready(function () {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const tab = urlParams.get('tab')
    if (tab) {
        $(`[href="#tabs-tabs-${tab}"]`).tab('show');
    }

    $('#tabs a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })

    currentSortField = DEFAULT_SORT_FIELD;
});
