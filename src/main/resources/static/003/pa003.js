const addHTH = () => {
    let hthLength = document.querySelectorAll('.htrz').length
    const dom = `
        <tr class="htrz">
           <td class="td1"> <textarea class="valid" style="height:auto;resize:none" name="HTH_${hthLength + 1}"  aria-invalid="false"></textarea></td>
           <td class="td1"> <textarea class=" valid" style="height:auto;resize:none" name="RZZLJGMC_${hthLength + 1}"  aria-invalid="false"></textarea></td>
            <td class="td1"><textarea class=" valid" style="height:auto;resize:none" name="SFK_${hthLength + 1}"  aria-invalid="false"></textarea></td>
            <td class="td1"><textarea class=" valid" style="height:auto;resize:none" name="RZE_${hthLength + 1}" aria-invalid="false"></textarea></td>
            <td class="td1"><textarea class=" valid" style="height:auto;resize:none" name="DYNLXZE_${hthLength + 1}" aria-invalid="false"></textarea></td>
            <td class="td1"><div class="datePicker" id="SQHKR_${hthLength + 1}"></div></td>
        </tr>`
    $('.htrz').last().after(dom);
    var tmp  = new DatePicker({id: "SQHKR_" + (hthLength + 1), type: "Y-M-D"});
    //   重置提交按钮的位置
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    /*绑定事件*/
    keyupEvents();
}
const removeHTH = () => {
    let hth_length = document.querySelectorAll('.htrz').length
    if (hth_length < 2) {
        alert('不能再删除')
    } else {
        $('.htrz').last().remove()
        //   重置提交按钮的位置
        $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    }
}


let textarea_1 = document.getElementById('QYJJ_SQ')
let textarea_2 = document.getElementById('XMSSQKJS')
makeExpandingArea(textarea_1)
makeExpandingArea(textarea_2)

/************************** 页面加载上次保存数据后的动态操作**************************/

// 根据内容整理表格的排布
function handleTable() {
    // 申请补助合同
    dynamicsExcute(['HTH_', 'RZZLJGMC_', 'SFK_', 'RZE_', 'DYNLXZE_', 'SQHKR_'], addHTH, 1)
}

/**
 * 根据关键字的数量变化动态执行方法
 * @param head 关键字的开头部门
 * @param func 执行方法
 * @param ignorNum 忽略的触发次数
 */
function dynamicsExcute(headArr, func, ignorNum, bolParm) {
    if (applicationRecord != undefined) {
        // 兼并重组信息
        var index = 0; // 动态变化标号
        for (var headIndex in headArr) {
            var head = headArr[headIndex]
            for (var i = 0; i < applicationRecord.length; i++) {
                for (var key in applicationRecord[i]) {
                    if (key.indexOf(head) > -1) {
                        var current = parseInt(key.substr(head.length, key.length))
                        index = index < current ? current : index
                    }
                }
            }
        }

        var num = index - ignorNum; // 动态变化的次数
        if (num > 0) {
            for (var i = 0; i < num; i++) {
                if (bolParm) {
                    func(i + 1)
                } else {
                    func()
                }
            }
        }
    }
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
    var tmp = new DatePicker({id: "SQHKR_1", type: "Y-M-D"});
    keyupEvents();
}

/*初始合计*/
function initTotal(){
    totalSouFundSqbzhthj2();
    totalSouFundSqbzhthj3();
    totalSouFundSqbzhthj4();
}

/*申请补助合同合计*/
/*为每个input绑定事件*/
function keyupEvents(){
    $(".htrz").each(function () {
        $(this).find("td:eq(2)").keyup(function () {
            totalSouFundSqbzhthj2();//调用合计方法
        });
        $(this).find("td:eq(3)").keyup(function () {
            totalSouFundSqbzhthj3();//调用合计方法
        });
        $(this).find("td:eq(4)").keyup(function () {
            totalSouFundSqbzhthj4();//调用合计方法
        });
    });
}

/*合计*/
function totalSouFundSqbzhthj2() {
    var sqbzhthj2 = 0;
    $(".htrz").each(function (i,e) {
            var indexVal = 'SFK_'+(parseInt(i)+1);
            sqbzhthj2 = sqbzhthj2 + getNumValue($(this).find("textarea[name='"+indexVal+"']"));
    });
    $("input[name='SQBZHTHJ_2']").val(Number(sqbzhthj2.toFixed(2)));
}
function totalSouFundSqbzhthj3() {
    var sqbzhthj3 = 0;
    $(".htrz").each(function (i,e) {
        var indexVal3 = 'RZE_'+(parseInt(i)+1);
        sqbzhthj3 = sqbzhthj3 + getNumValue($(this).find("textarea[name='"+indexVal3+"']"));
    });
    $("input[name='SQBZHTHJ_3']").val(Number(sqbzhthj3.toFixed(2)));
}
function totalSouFundSqbzhthj4() {
    var sqbzhthj4 = 0;
    $(".htrz").each(function (i,e) {
        var indexVal4 = 'DYNLXZE_'+(parseInt(i)+1);
        sqbzhthj4 = sqbzhthj4 + getNumValue($(this).find("textarea[name='"+indexVal4+"']"));
    });
    $("input[name='SQBZHTHJ_4']").val(Number(sqbzhthj4.toFixed(2)));
}

/*用来获取文本框的值，返回float*/
function getNumValue(controlid) {
    var num = controlid.val();
     if (validateInput(num)) {
         num = parseFloat(num);
     }else{
         controlid.val("");
        num = 0;
     }
     return num;
}

function validateInput(inputVal) {
    var flag = false;
    if (inputVal != "") {
        if (isNaN(inputVal)) {
            flag = false; //如果输入字符不是数字
        }else {//输入数字但是小于0
            if (parseFloat(inputVal) < 0){
                flag = false;
            }else{
                flag = true;
            }
        }
    }
     return flag;
}