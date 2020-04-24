

/************************** 页面加载上次保存数据后的动态操作**************************/

// pdf文件打印
function dowlondPDFBeforeHandle() {
    // 先处理隐藏的元素
    hideClass(true, '.hidePdf')
    downPdf()
    hideClass(false, '.hidePdf')
}

function hideClass(bol, cls) {
    let items = $('.registerform').find(cls)
    for (let i = 0; i < items.length; i++) {
        if (bol == true) {
            items.eq(i).hide()
        } else {
            items.eq(i).show()
        }
    }
}

function initDatePicker() {
    /*new DatePicker({id: "FKRQ_1", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_1", type: "Y-M-D"});
    new DatePicker({id: "FKRQ_2", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_2", type: "Y-M-D"});
    new DatePicker({id: "FKRQ_3", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_3", type: "Y-M-D"});
    new DatePicker({id: "FKRQ_4", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_4", type: "Y-M-D"});*/
}