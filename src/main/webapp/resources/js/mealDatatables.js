var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

$.ajaxSetup({
    converters: { "text json": function (stringData) {
        var json = JSON.parse(stringData);
        $(json).each(function () {
            this.dateTime = this.dateTime.replace('T', ' ').substr(0,16);
        });
        return json;
    } }
});

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
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
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "Delete",
                "orderable": false,
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data) {
            $(row).addClass(data.exceed ? 'exceeded' : 'normal');
        },
        "initComplete": makeEditable
    });
});

$(function () {
    $('.date-time-picker').datetimepicker({
        format: 'YYYY-MM-DD HH:mm'
    });
    $('.date-picker').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $('.time-picker').datetimepicker({
        format: 'HH:mm'
    });
});