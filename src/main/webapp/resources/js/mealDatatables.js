var ajaxUrl = "ajax/meals/";
var datatableApi;

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
                "desc"
            ]
        ]
    });
    makeEditable();
    setMealEventProcessors();
});

function setMealEventProcessors() {
    $("#filter").click(updateTable);
    $("#resetFilter").click(function () {
        $("#filterForm").find(":input").val("");
        updateTable();
    });
}

function updateTable() {
    var submitData = $("#filterForm").serialize();
    $.get(ajaxUrl, submitData, updateTableCallback);
}