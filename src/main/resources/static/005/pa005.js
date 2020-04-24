


let textarea_1 = document.getElementById('QYJJ_SQ');
let textarea_2 = document.getElementById('XMJJ');
makeExpandingArea(textarea_1);
makeExpandingArea(textarea_2);

const addHTH = () => {
    let hthLength = document.querySelectorAll('.htrz').length
    const dom = `
        <tr class="htrz">
            <td class="td1">
                <input type="text" class="input1" name="XH_${hthLength + 1}">
            </td>
            <td class="td1">
                <input type="text" class="input1" name="ZXQYMC_${hthLength + 1}">
            </td>
            <td class="td1">
                <input type="text" class="input1" name="SSZJ_${hthLength + 1}">
            </td>
            <td class="td1">
                <input type="text" class="input1" name="DKJE_${hthLength + 1}">
            </td>
            <td class="td1">
                <input type="text" class="input1" name="HDYH_${hthLength + 1}">
            </td>
            <td class="td1">
                <div class="datePicker" id="FKRQ_${hthLength + 1}"></div>
            </td>
            <td class="td1">
                <div class="datePicker" id="HKRQ_${hthLength + 1}"></div>
            </td>
        </tr>`
    $('.htrz').last().after(dom);
    new DatePicker({id: "FKRQ_" + (hthLength + 1), type: "Y-M-D"});
    new DatePicker({id: "HKRQ_" + (hthLength + 1), type: "Y-M-D"});
    //   重置提交按钮的位置
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
}
const removeHTH = () => {
    let hth_length = document.querySelectorAll('.htrz').length
    if (hth_length < 5) {
        alert('不能再删除')
    } else {
        $('.htrz').last().remove()
        //   重置提交按钮的位置
        $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    }
}


/************************** 页面加载上次保存数据后的动态操作**************************/

// 根据内容整理表格的排布
function handleTable() {
    // 申请补助合同
    dynamicsExcute(['FKRQ_', 'HKRQ_'], addHTH, 4)
}


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
    new DatePicker({id: "FKRQ_1", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_1", type: "Y-M-D"});
    new DatePicker({id: "FKRQ_2", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_2", type: "Y-M-D"});
    new DatePicker({id: "FKRQ_3", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_3", type: "Y-M-D"});
    new DatePicker({id: "FKRQ_4", type: "Y-M-D"});
    new DatePicker({id: "HKRQ_4", type: "Y-M-D"});
}


//动态属性获取值
function dynamicReceivablesFinanceAwardJson(){
    var rfaBrr = document.getElementsByClassName("htrz");
    var rfaArr = [];
    for(var i=0;i<rfaBrr.length;i++){
        var brr = {};
        var form = rfaBrr[i].getElementsByTagName('input');
/*        var htrzInputArrform = rfaBrr[i].getElementsByClassName('datePicker-input');
        var feledDate = htrzInputArrform[0];
        brr[feledDate.name] = feledDate.value;*/
        for(var j=0;j<form.length;j++) {
            var feled = form[j];
            switch (feled.type) {
                case undefined:
                case 'button':
                case 'file':
                case 'reset':
                case 'submit':
                case 'hidden':
                    break;
                case 'checkbox':
                case 'radio':
                    if (!feled.checked) {
                        break;
                    }
                default:
                    if (brr[feled.name]) {
                        brr[feled.name] = brr[feled.name] + ',' + feled.value;
                    } else {
                        brr[feled.name] = feled.value;
                    }
            }
        }
        rfaArr.push(brr);
    }
    return rfaArr;
}