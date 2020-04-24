//动态table表单转json
function dynamicTableJson() {
    var kzArr = document.getElementsByClassName("kz");
    var brr1 = [];
    for(var i=0;i<kzArr.length;i++){
        var brr = {};
        var form=kzArr[i].getElementsByTagName('input');
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
        brr1.push(brr);
    }
    return brr1;
}


const addKZSB = () => {
    let kzsbLength = document.querySelectorAll('.kz').length

    const dom = `
	<tr class="kz">
		   <td class="td1"><input class="input1" name="code${kzsbLength + 1}" type="text"></td>
		   <td class="td1"><input class="input1" name="facility${kzsbLength + 1}" type="text"></td>
		   <td class="td1"><input class="input1" name="amount${kzsbLength + 1}" type="text"></td>
		   <td class="td1"><input class="input1" name="model${kzsbLength + 1}" type="text"></td>
		   <td class="td1"><div class="datePicker" id="contractDate${kzsbLength + 1}"></div></td>
		   <td class="td1"><input class="input1" name="sellPrice${kzsbLength + 1}" type="text"></td>
		   <td class="td1"><input class="input1" name="proceeds${kzsbLength + 1}" type="text"></td>
		   <td class="td1"><input class="input1" name="clientName${kzsbLength + 1}" type="text"></td>
	</tr>`
    $('.kz').last().after(dom);
    var tmp  = new DatePicker({id: "contractDate" + (kzsbLength + 1), type: "Y-M-D"});
    //重置提交按钮的位置
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
}
const removeKZSB = () => {
    let kzsb_length = document.querySelectorAll('.kz').length
    if (kzsb_length < 2) {
        alert('不能再删除')
    } else {
        $('.kz').last().remove()
        //   重置提交按钮的位置
        $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    }
}


// 根据内容整理表格的排布
function handleTable() {
    // 申请补助合同
    dynamicsExcute(['code', 'facility', 'amount', 'model', 'contractDate', 'sellPrice'
        , 'proceeds', 'clientName'], addKZSB, 1)
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
