$(document).ready(function () {
    bindEventCheckAllCheckBox('checkAll');
    enableOrDisableDeleteAll();
    autoCheckCheckboxAll('checkAll');
});

function bindEventCheckAllCheckBox(id) {    // chọn checkbox cha, tất cả checkbox con được chọn
    $('#' + id).on('change', function () {
        if(this.checked) {
            // enable all checkboxes
            $(this).closest('table').find('input[type=checkbox]').prop('checked', true);
        } else {
            // disable all checkboxes
            $(this).closest('table').find('input[type=checkbox]').prop('checked', false);
        }
    });
}

function enableOrDisableDeleteAll() {   // khi không có checkbox nào được chọn thì disable nút DeleteAll và ngược lại
    $('input[type=checkbox]').click(function () {
        // khi nút checkbox cha không được chọn
        if($(this).attr('id') == 'checkAll' && $(this).prop('checked') == false) {
            // bỏ chọn hết checkbox con
            $(this).closest('table').find('input[type=checkbox]').prop('checked', false);
        }
        if($('input[type=checkbox]:checked').length > 0) {
            $('#deleteAll').prop('disabled', false);
        } else {
            $('#deleteAll').prop('disabled', true);
        }
    });
}

function autoCheckCheckboxAll(id) {     // nếu chọn hết checkbox con, checkbox cha sẽ được chọn
    var totalChildCheckbox = $('#' + id).closest('table').find('tbody input[type=checkbox]').length;    // các checkbox con nằm trong <tbody>
    $('#' + id).closest('table').find('tbody input[type=checkbox]').each(function () {      // với mỗi checkbox con thì
        var tableObj = $('#' + id).closest('table');
        $(this).on('change', function () {      // nếu thay đổi
            // ta tìm số checkbox con đã được chọn
            var totalChildCheckboxChecked = $(tableObj).find('tbody input[type=checkbox]:checked').length;
            if(totalChildCheckboxChecked == totalChildCheckbox) {   // nếu tất cả checkbox con được chọn
                $('#' + id).prop('checked', true);
            } else {
                $('#' + id).prop('checked', false);
            }
        });
    });
}