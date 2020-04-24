

//根据内容整理表格的排布
function handleTable(){
    //兼并重组信息
    dynamicsExcute('BGQYMC_', addMaaArray,1);
    //企业推荐申报资本利得奖励股东名单
    dynamicsExcute('line_name_', addJlArray,0);

    dynamicsExcute('line_name_', addPersonTable,0,true);

}



let maaLength = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr').length / 5;

const addMaaArray = () => {
    const dom = `
        <tr class="each_tr">
            <th colspan="4" class="thCol" style="text-align:left">并购企业${maaLength + 1}情况</th>
        </tr>
        <tr class="each_tr">
            <td class="td1" style="width:200px">被并购企业名称</td>
            <td class="td1"><input class="input1" name="BGQYMC_${maaLength + 1}" type="text"></td>
            <td class="td1" style="width:200px">注册地</td>
            <td class="td1"><input class="input1" name="BGQYZCD_${maaLength + 1}" type="text"></td>
        </tr>
        <tr class="each_tr">
            <td class="td1">统一社会信用代码或工商部门登记注册号</td>
            <td class="td1"><input class="input1" name="BGQYCODE_${maaLength + 1}" type="text"></td>
            <td class="td1">注册资金</td>
            <td class="td1"><input class="input1" name="BGQYZCZJ_${maaLength + 1}" type="text"></td>
        </tr>
        <tr class="each_tr">
            <td class="td1">并购交易额</td>
            <td class="td1"><input class="input1" name="BGQYBGJY_${maaLength + 1}" type="text"></td>
            <td class="td1">并购时间</td>
            <td class="td1"><div class="datePicker" id="BGQYBGSJ_${maaLength + 1}"></div></td>
        </tr>
        <tr class="each_tr">
            <td class="td1">并购后持有企业${maaLength + 1}的股权比例</td>
            <td colspan="3" class="td1"><input class="input1" name="BGQYBL_${maaLength + 1}" type="text"></td>
        </tr>`

    $('#zgsglhzc_bzjh  .each_tr').last().after(dom);
    var tmp  = new DatePicker({id: "BGQYBGSJ_" + (maaLength + 1), type: "Y-M-D"})
    maaLength++
    //   重置提交按钮的位置
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
}
const removeMaaArray = () => {
    let tp_length = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr').length / 5
    if (tp_length < 2) {
        alert('不能再删除')
    } else {
        $('#zgsglhzc_bzjh .each_tr').eq(-6).nextUntil('tr.stop').remove()
        maaLength = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr').length / 5

        //   重置提交按钮的位置
        $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    }
}

var personLength = 1;
const addJlArray = () => {
    let jlLength = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr_jl').length
    const dom = `
        <tr class="each_tr_jl">
            <td class="td1" ><input class="input1 GD" name="line_name_${jlLength + 1}" id="personName_${jlLength + 1}" type="text"></td>
            <td class="td1"><input class="input1 GD" name="line_passport_${jlLength + 1}" type="text"></td>
            <td class="td1"><input class="input1 GD" name="line_passport_percent${jlLength + 1}" type="text"></td>
            <td  class="td1"  ><input class="input1 GD" name="line_money_${jlLength + 1}" type="text"></td>
        </tr>`
    $('#zgsglhzc_bzjh  .each_tr_jl_mark').before(dom);

    //   重置提交按钮的位置
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    personLength = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr_jl').length;
    listenPersonTable(personLength);
    //增加必填的属性
    changeRules(true, ".GD");

}
const removeJlArray = () => {
    let tp_length = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr_jl').length
    if (tp_length < 2) {
        alert('不能再删除')
    } else {
        $('#zgsglhzc_bzjh .each_tr_jl').last().remove()

        //   重置提交按钮的位置
        $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    }
    personLength = document.querySelectorAll('#zgsglhzc_bzjh  .each_tr_jl').length;
    removePersonTable(personLength + 1);
}


const listenPersonTable = (eleIndex) => {
    $('#personName_' + (eleIndex)).bind("input change", function (event) {
        var viewName = this.value;
        viewName = $.trim(viewName);
        this.value = viewName;
        if (viewName.length == 0) {
            removePersonTable(eleIndex );
            return true;
        } else {
            addPersonTable(eleIndex,viewName);
            return true;
        }
    });
};
const removePersonTable = (index) => {

    var personDom = document.getElementById("personDom_" + index);
    if (personDom != undefined) {
        $('#personDom_' + index).unbind();
        personDom.remove();
        //包含 table
        document.getElementById("pageTurning").options.length = 0;
        addSelectOption();
    }
}

const addPersonTable = (index,personName) => {
    var personDom = document.getElementById("personDom_" + index);
    if (personDom != undefined) {
        $("#personName_" + index).val(personName);
        //包含 table
    } else {
        const dom =
            `<div class="contentTable" id="personDom_${index}">
                <table>
                    <tr>
                        <th class="thCol" colspan="9">东莞市鼓励和支持“倍增计划”企业兼并重组资金资本利得奖励个人申请表</th>
                    </tr>
                    <tr>
                        <th class="thCol" rowspan="6" style="width: 200px;">股东个人基本信息</th>
                        <td class="td1">企业名称</td>
                        <td class="td1" colspan="7"><input class="input1" name="QYMC_GL_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1">申请股东姓名</td>
                        <td class="td1"><input class="input1" name="GDMC_${index}" type="text" id="personName_${index}"></td>
                        <td class="td1">性别</td>
                        <td class="td1"><input class="input1" name="XB_${index}" type="text"></td>
                        <td class="td1">国籍</td>
                        <td class="td1"><input class="input1" name="GJ_${index}" type="text"></td>
                        <td class="td1">户籍</td>
                        <td class="td1"><input class="input1" name="HJ_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1">身份证号（护照、回乡证、台胞证）号码</td>
                        <td class="td1" colspan="8"><input class="input1" name="PASSPORT_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1">持股比例（%）</td>
                        <td class="td1" colspan="3"><input class="input1" name="CGBL_${index}" type="text"></td>
                        <td class="td1">持股股本（元）</td>
                        <td class="td1" colspan="3"><input class="input1" name="CGGB_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1">联系电话</td>
                        <td class="td1" colspan="8"><input class="input1" name="LXDH_GL_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1">是否在企业任职及职务</td>
                        <td class="td1" colspan="8"><input class="input1" name="ZZZW_${index}" type="text"></td>
                    </tr>

                    <tr>
                        <th class="thCol" rowspan="2">资本利得个人所得税缴纳情况</th>
                        <td class="td1">2018年资本利得收入（元）</td>
                        <td class="td1" colspan="3"><input class="input1" name="ZBSR2018_${index}" type="text"></td>
                        <td class="td1">2018年缴纳资本利得个人所得税（元）</td>
                        <td class="td1" colspan="3"><input class="input1" name="GRSDS2018_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1">2019年资本利得收入（元）</td>
                        <td class="td1" colspan="3"><input class="input1" name="ZBSR2019_${index}" type="text"></td>
                        <td class="td1">2019年缴纳资本利得个人所得税（元）</td>
                        <td class="td1" colspan="3"><input class="input1" name="GRSDS2019_${index}" type="text"></td>
                    </tr>
                    <tr>
                        <td class="td1" colspan="9">
                            <div class="td_bk">
                                <p style="text-align: left;">申请人声明：</p>
                                <p style="text-align: left;text-indent: 2em;">1.本申请表内所填全部资料皆真实、无误，本人无任何虚报申请书所填资料情况。</p>
                                <p style="text-align: left;text-indent: 2em;">2.本人愿意接受有关部门对上述申报内容进行审查与公示。</p>
                                <p style="text-align: left;text-indent: 2em;">3.本人明白，若存在虚报申请资料情况的，将被取消申请资格；任何因提供虚假资料而被批准给予补贴的，有关部门将有权收回，并承担有关法律责任。</p>
                                <p style="text-align: right;padding-right: 10em;">申请人：（签字）</p>
                                <p style="text-align: right;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 日</p>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="thCol">倍增计划试点企业推荐意见</th>
                        <td class="td1" colspan="8">
                            <div class="td_bk">
                                <p style="text-align: left;">同意推荐该申请人申报东莞市鼓励和支持“倍增计划”试点企业兼并重组资助资金资本利得奖励</p>
                                <p style="text-align: right;padding-right: 10em;">企业名称（盖章）：</p>
                                <p style="text-align: right;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 日</p>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>`
        $('div .contentTable').last().after(dom);
        //   重置提交按钮的位置
        $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')


        addSelectOption();
    }


}

//加载事件
function addSelectOption() {
    var optionIndex = 0;
    $.each(document.getElementById("pageTurning").options, function (i, v) {
        if (v.selected == true) {
            optionIndex = i;
            return false;
        }
    });

    document.getElementById("pageTurning").options.length = 0;
    var contents = document.getElementsByClassName(otherTable);
    //用标记id的td元素 获取值方法
    var thVal = "";
    for (var i = 0; i < contents.length; i++) {
        thVal = contents[i].firstElementChild.childNodes[1].firstElementChild.firstElementChild.innerText;
        document.getElementById("pageTurning").options.add(new Option(thVal, i));
    }
    //标记选中的页面
    document.getElementById("pageTurning").options[optionIndex].selected = true;
}




function initDatePicker(){
    var tmp = new DatePicker({id: "BGQYBGSJ", type: "Y-M-D"});
}

/*奖励个人申请表*/
function dynamicPersionalApplicationFormValJson(){
    var pafLength = $("#exportToPdf  div[id^='personDom_']").length;
    var pafArr = $("#exportToPdf  div[id^='personDom_']");
    var pdfbrr = [];
    for(var i=0;i<pafLength;i++){
        var brr = {};
        var form=pafArr[i].getElementsByTagName('input');
        for(var j=0;j<form.length;j++) {
            var feled = form[j];

                    if (brr[feled.name]) {
                        brr[feled.name] = brr[feled.name] + ',' + feled.value;
                    } else {
                        brr[feled.name] = feled.value;
                    }
        }
        pdfbrr.push(brr);
    }
    return pdfbrr;
}