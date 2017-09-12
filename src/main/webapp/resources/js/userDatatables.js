var ajaxUrl = "ajax/admin/users/";
var datatableApi;


// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
    setUserEventProcessors();
});

function setUserEventProcessors() {
    $(".enableUserCheckbox").click(function () {
        enableUser($(this));
    });
}

function updateTable() {
    $.get(ajaxUrl, updateTableCallback);
}

function enableUser(checkbox) {
    var enabled = checkbox.is(":checked");
    var id = getRowId(checkbox);
    $.ajax({
        url: ajaxUrl + 'enable/' + id,
        type: 'POST',
        data: 'enabled=' + enabled,
        success: function () {
            checkbox.closest('tr').toggleClass('disabled');
            successNoty(enabled ? messages['user.enabled'] : messages['user.disabled']);
        },
        error: function () {
            checkbox.prop("checked", !enabled);
        }
    });
}