
document.addEventListener('DOMContentLoaded', function () {
    $('table.apartments').on('click', 'tr:has(td)', function (event) {
        $('.selected').removeClass('selected');
        $(this).addClass('selected');
    });
});