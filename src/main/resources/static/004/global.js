/*var base_api_path = "https://test.dg.gov.cn/pt/pa/"*/
var base_api_path = document.location.protocol + "//" + document.domain + "/pt/pa/";
var projectId
var applicationRecord
let autoInputProperties
var id
var status
var clickButton
var attachmentIds = ''
let firstindustypeid
let dptUser
var mustFileNames;
var mustFileExtendNames;
window.onload = function () {
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    var Request = new UrlSearch(); // 实例化
    projectId = Request.projectId
    firstindustypeid = Request.firstindustypeid
    dptUser = Request.dptUser
    id = Request.id
    createSelectOption()

    if (Request.preview) {
        document.getElementById("submit").disabled = true;
        document.getElementById("save").disabled = true;
    } else {
        getData();
        getProjectInformationData();
    }
    if (dptUser) {
        document.getElementById('btnView').style.display = 'none'; // 隐藏
        $('#fileTable').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    }
}

function downward() {
    // 获取内容盒子
    var contents = document.getElementsByClassName('contentTable')
    var numberInfo = document.getElementById('numberInfo').value
    var btn = contents.length - 1; // 页面数据
    var count = parseInt(numberInfo) + 1; // 当前页面
    if (count <= btn) {
        for (var j = 0; j < btn; j++) {
            contents[j].className = contents[j].className.replace(' contentTable1', '').trim()
        }
        contents[count].className = contents[count].className + ' contentTable1'
        document.getElementById('numberInfo').value = count
        var myselect = document.getElementById('pageTurning')
        myselect.options[count].selected = true
    }
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    $('.contentTable').attr('style', 'margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;')
    $('.contentTable1').attr('style', 'position: absolute;opacity: 1;top: auto;left: auto;width: 98%;')
}

function upward() {
    // 获取内容盒子
    var contents = document.getElementsByClassName('contentTable')
    var numberInfo = document.getElementById('numberInfo').value
    var btn = contents.length - 1
    var count = numberInfo - 1
    if (count >= 0) {
        for (var j = 0; j < contents.length; j++) {
            contents[j].className = contents[j].className.replace(' contentTable1', '').trim()
        }
        contents[count].className = contents[count].className + ' contentTable1'
        document.getElementById('numberInfo').value = count
        var myselect = document.getElementById('pageTurning')
        myselect.options[count].selected = true
    }
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    $('.contentTable').attr('style', 'margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;')
    $('.contentTable1').attr('style', 'position: absolute;opacity: 1;top: auto;left: auto;width: 98%;')
}

function func() {
    var myselect = document.getElementById('pageTurning')
    var index = myselect.selectedIndex
    var val = myselect.options[index].value
    var contents = document.getElementsByClassName('contentTable')
    if (val <= contents.length - 1) {
        for (var j = 0; j < contents.length; j++) {
            contents[j].className = contents[j].className.replace(' contentTable1', '').trim()
        }
        contents[val].className = contents[val].className + ' contentTable1'
        document.getElementById('numberInfo').value = val
    }
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
    $('.contentTable').attr('style', 'margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;')
    $('.contentTable1').attr('style', 'position: absolute;opacity: 1;top: auto;left: auto;width: 98%;')
}

// 跳转到表单验证失败页面
function error(val) {
    let contents = $('.contentTable')
    for (let i = 0; i < contents.length; i++) {
        if ($('.contentTable').eq(i).find('[name=' + val + ']').length > 0) {
            document.getElementById('pageTurning').options[i].selected = true
            func()
        }
    }
}

// 加载事件
function createSelectOption() {
    var contents = document.getElementsByClassName('contentTable')
    // 用标记id的td元素 获取值方法
    var thVal = ''
    for (var i = 0; i < contents.length; i++) {
        thVal = contents[i].firstElementChild.childNodes[1].firstElementChild.firstElementChild.innerText
        document.getElementById('pageTurning').options.add(new Option(thVal, i))
    }
    document.getElementById('pageTurning').options[0].selected = true
}

function isEquil(mustFileNamesArr,mustFileExtendNamesArr) {
    mustFileExtendNamesArr = mustFileExtendNamesArr != undefined ? mustFileExtendNamesArr : '';
    var arr1 = mustFileExtendNamesArr.split(",")
    var arr2 = mustFileNamesArr.split(",")
    if (arr1.sort().toString() == arr2.sort().toString()) {
        return true;
    }else{
        return false;
    }
}


// 提交信息
function butSubmit(type) {
    if(type=="1"){
        if(mustFileNames&&mustFileNames!=undefined){
            if(!isEquil(mustFileNames,mustFileExtendNames)){
                alert("请按要求完成上传文件");
                return;
            }
        }
    }
    var message = ''
    if (type == '1') {
        status = 'APPROVAL'
        message = '提交成功！'
    }
    if (type == '0') {
        status = 'DRAFT'
        message = '保存成功！'
    }
    if (type == '9') {
        status = 'DRAFT'
        message = '回滚成功！'
    }
    var formJson = formser();
    var autoInputMap = autoInputSave()
    var myForm = {
        status: status,
        'id': id,
        'projectId': projectId,
        'attachmentIds': attachmentIds,
        'formJson': JSON.stringify(formJson),
        'autoInputProperties': autoInputMap
    }
    $.ajax({
        url: base_api_path + 'applicationRecord/submit',
        type: 'post',
        data: JSON.stringify(myForm),
        timeout: 10000,
        async: false,
        contentType: 'application/json',
        success: function (response) {
            /*var response = JSON.parse(response)*/
            if (response.code == 200) {
                id = response.data.id
                if (type == '1') {
                    // document.getElementById("submit").disabled=true
                    // document.getElementById("save").disabled=true
                    document.getElementById('btnView').style.display = 'none'; // 隐藏
                    $('#fileTable').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
                    document.getElementById('file').style.display = 'none'
                    var inputs = document.getElementsByTagName('input')
                    var textareas = document.getElementsByTagName('textarea')
                    for (var i = 0; i < inputs.length; i++) {
                        inputs[i].readOnly = true
                    }
                    for (var i = 0; i < textareas.length; i++) {
                        textareas[i].readOnly = true
                    }
                }
                if (type == '9') {
                    // document.getElementById("submit").disabled=false
                    // document.getElementById("save").disabled=false
                    document.getElementById('btnView').style.display = 'none'; // 隐藏
                    $('#fileTable').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
                    var inputs = document.getElementsByTagName('input')
                    var textareas = document.getElementsByTagName('textarea')
                    for (var i = 0; i < inputs.length; i++) {
                        inputs[i].readOnly = false
                    }
                    for (var i = 0; i < textareas.length; i++) {
                        textareas[i].readOnly = false
                    }
                }
                alert(message)
                window.parent.postMessage({
                    cmd: 1
                }, '*')
            } else {
                alert(response.message)
            }
        },
        error: function (e) {
            console.log(e)
        }
    })
}


function UrlSearch() {
    var name, value
    var str = parent.location.href; // 取得整个地址栏
    var num = str.indexOf('?')
    str = str.substr(num + 1); // 取得所有参数   stringvar.substr(start [, length ]
    var arr = str.split('&'); // 各个参数放到数组里
    for (var i = 0; i < arr.length; i++) {
        num = arr[i].indexOf('=')
        if (num > 0) {
            name = arr[i].substring(0, num)
            value = arr[i].substr(num + 1)
            this[name] = value
        }
    }
}

function ajax(options) {
    options = options || {};  //调用函数时如果options没有指定，就给它赋值{},一个空的Object
    options.type = (options.type || "GET").toUpperCase();/// 请求格式GET、POST，默认为GET
    options.dataType = options.dataType || "json";    //响应数据格式，默认json
    options.contentType = options.contentType || "application/x-www-form-urlencoded";
    var params = options.data;//options.data请求的数据
    var xhr;
    //考虑兼容性
    if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest();
    } else if (window.ActiveObject) {//兼容IE6以下版本
        xhr = new ActiveXobject('Microsoft.XMLHTTP');
    }

    //启动并发送一个请求
    if (options.type == "GET") {
        if (options.url.indexOf("?") == -1) {
            options.url = options.url + (params ? "?" + params : '');
        } else {
            options.url = options.url + (params ? "&" + params : '');
        }
        xhr.open("GET", options.url, true);
        params = null
    } else if (options.type == "POST") {
        xhr.open("post", options.url, true);

        //设置表单提交时的内容类型
        //Content-type数据请求的格式
        xhr.setRequestHeader("Content-type", options.contentType);
        // ajax.setRequestHeader("X-Requested-With","XMLHttpRequest");
    }
    xhr.setRequestHeader("token", window.localStorage.token);
    xhr.setRequestHeader("iv", window.localStorage.iv);
    xhr.send(params);
    //    设置有效时间
    setTimeout(function () {
        if (xhr.readySate != 4) {
            xhr.abort();
        }
    }, options.timeout)

    //options.success成功之后的回调函数  options.error失败后的回调函数
    //xhr.responseText,xhr.responseXML  获得字符串形式的响应数据或者XML形式的响应数据
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status >= 200 && status < 300 || status == 304) {
                options.success && options.success(xhr.responseText, xhr.responseXML);
            } else {
                options.error && options.error(status);
            }
        }
    }
}

// from表单转json
function formser() {
    var form = document.getElementsByTagName('form')
    var arr1 = []
    for (var j = 0; j < form.length; j++) {
        var arr = {}
        for (var i = 0; i < form[j].elements.length; i++) {
            var feled = form[j].elements[i]
            switch (feled.type) {
                case undefined:
                case 'button':
                case 'file':
                case 'reset':
                case 'submit':
                case 'hidden':
                    break
                case 'checkbox':
                case 'radio':
                    if (!feled.checked) {
                        break
                    }
                default:
                    if (arr[feled.name]) {
                        arr[feled.name] = arr[feled.name] + ',' + feled.value
                    } else {
                        arr[feled.name] = feled.value
                    }
            }
        }
        arr1.push(arr)
    }
    return arr1
}

// 将html页面导出.pdf格式文件(适用于jQuery、vue库)
function downPdf(pdfName) {
    if (confirm('您确认下载该PDF文件吗?')) {
        var target = document.getElementsByClassName('right-aside')[0]
        target.style.background = '#FFFFFF'
        let height = $('.right-aside').eq(0).height()
        $('.contentTable1').attr('style', 'display: block;position: relative;opacity: 1;top: auto;left: auto;width: 98%;')
        $('.contentTable').attr('style', 'display: block;margin: 10px auto;position: relative;opacity: 1;top: auto;left: auto;width: 98%;')
        $('.contentTable').css('position', 'relative')
        $('.contentTable1').css('position', 'relative')
        $('.right-aside').eq(0).css('height', document.getElementsByClassName('right-aside')[0].scrollHeight + 20)
        var text = $('#QYMC').val()
        html2canvas(target, {
            onrendered: function (canvas) {
                var contentWidth = canvas.width
                var contentHeight = canvas.height
                // 一页pdf显示html页面生成的canvas高度
                var pageHeight = contentWidth / 592.28 * 841.89
                // 未生成pdf的html页面高度
                var leftHeight = contentHeight
                // 页面偏移
                var position = 0
                // a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
                var imgWidth = 595.28
                var imgHeight = 592.28 / contentWidth * contentHeight

                // 添加水印信息begin
                /*                canvas.style.display='none'
                                var cans = canvas.getContext('2d')
                                for (var p = 0; p <8; p++) {
                                    for (var q = 0; q <6; q++) {
                                        // 绘制文字  注意：：绘制斜体文字 旋转以后会发生位移，所以必须在旋转之后进行位置的调整；
                                        drawText((q * -120),-(50 * p),((p * 250)),((q * 200)+200),cans,text)
                                    }
                                }*/

                let pageData = canvas.toDataURL('image/jpeg', 1.0)
                var PDF = new jsPDF('', 'pt', 'a4')
                if (leftHeight < pageHeight) {
                    PDF.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight)
                } else {
                    while (leftHeight > 0) {
                        PDF.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                        leftHeight -= pageHeight
                        position -= 841.89
                        if (leftHeight > 0) {
                            PDF.addPage()
                        }
                    }
                }

                var date = new Date()
                var seperator1 = ''
                var seperator2 = ''
                var month = date.getMonth() + 1
                var strDate = date.getDate()
                if (month >= 1 && month <= 9) {
                    month = '0' + month
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = '0' + strDate
                }
                var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                    + '' + date.getHours() + seperator2 + date.getMinutes()
                    + seperator2 + date.getSeconds()

                PDF.save(text + currentdate + '.pdf')

                // PDF.save(text+".pdf")
                $('.contentTable').attr('style', 'margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;')
                $('.contentTable1').attr('style', 'position: absolute;opacity: 1;top: auto;left: auto;width: 98%;')
                $('.right-aside').eq(0).css('height', height)
            }
        })
    }
}

function drawText(tsx, tsy, x, y, ctx, title) {
    ctx.save(); // 保存原来的状态  绘制字体都是需要旋转倾斜  那么之前绘制的图片就要进行状态的保存
    ctx.rotate(-Math.PI / 6); // 绘制倾斜字体
    ctx.translate(tsx, tsy); // 发生位移进行位移的恢复
    ctx.font = '16px serif'
    ctx.fillStyle = 'red'
    ctx.fillText(title, x, y)
    ctx.restore(); // 状态的恢复
}

function readLocalFile(extProperty) {
    var fileObj;
    if (extProperty) {
        fileObj = document.getElementById("MustUploadFile").files[0];
    } else {
        fileObj = document.getElementById("uploadFile").files[0];
    }
    // js 获取文件对象
    if (fileObj == null || fileObj == undefined) return;
    if (fileObj.size > 10485760) {
        alert("上传文件不能超过10MB");
        return;
    }
    var url = base_api_path + "commAttachment/ftpStore"; // 接收上传文件的后台地址
    var form = new FormData(); // FormData 对象
    form.append("file", fileObj); // 文件对象
    if (extProperty) {
        form.append("extProperty", mustFileExtendName);
    }
    $.ajax({
        type: "post",
        url: url,
        data: form,
        async: false,
        contentType: false,
        processData: false,
        headers: {"token": window.localStorage.token, "iv": window.localStorage.iv},
        success: function (data) {
            if (data.code == 200) {
                if (data.data != null) {
                    attachmentIds += data.data.id + ",";
                    alert("上传成功！");
                    getFileTable();
                }
            }
        }, error(e) {
            alert("System error, please try again later");
        }
    })
}

function getFileTable(pageNo) {
    mustFileExtendNames="";
    let isShow=dptUser||status =="APPROVAL" || status== "PASSED" || status == "REJECTED";
    var fileTable=document.getElementById('fileTable');
    fileTable.innerHTML="";
    // var ta=fileTable.getElementsByTagName('table')[0];
    // if (ta) {ta.parentNode.removeChild(ta)}
    var tab = document.createElement("table");
    var url =  base_api_path + "commAttachment/getList?pageSize=100&attachmentIds="+attachmentIds;
    $.ajax({
        type:"get",
        url:url,
        async: false,
        headers: {"token":window.localStorage.token,"iv":window.localStorage.iv},
        success:function (data) {
            var records=data.data.records;
            if(records.length>0){
                tab.insertRow(0);
                let cell0=tab.rows[0].insertCell(0);
                cell0.style.width="10%";
                cell0.innerHTML = "编号";
                let cell1=tab.rows[0].insertCell(1);
                cell1.style.width="35%";
                cell1.innerHTML = "文件名称";
                let cell2=tab.rows[0].insertCell(2);
                cell2.style.width="10%";
                cell2.innerHTML = "必要附件";
                let cell3=tab.rows[0].insertCell(3);
                cell3.style.width="35%";
                cell3.innerHTML = "必要附件名称";
                if(!isShow){
                    let cell4=tab.rows[0].insertCell(4);
                    cell4.style.width="10%";
                    cell4.innerHTML = "操作";
                }
                let index=1;
                for(var i=1;i<records.length+1;i++){
                    if(records[i-1].extProperty)mustFileExtendNames+=records[i-1].extProperty+",";
                    let startNumber=(pageNo?pageNo*10:0)+1;
                    let endNumber=startNumber+10;
                    if(i>=startNumber&&i<endNumber) {
                        tab.insertRow(index);
                        let cells0=tab.rows[index].insertCell(0);
                        cells0.style.width="10%";
                        cells0.innerHTML = i;
                        let cells1=tab.rows[index].insertCell(1);
                        cells1.style.width="35%";
                        cells1.innerHTML = "<a href='" + base_api_path + "commAttachment/ftpDownload/" + records[i - 1].filePath + "'>" + records[i - 1].fileName + "</a>";
                        let cells2=tab.rows[index].insertCell(2);
                        cells2.style.width="10%";
                        let cells3=tab.rows[index].insertCell(3);
                        cells3.style.width="35%";
                        if (records[i - 1].extProperty) {
                            cells2.innerHTML = "是";
                            cells3.innerHTML = records[i - 1].extProperty;
                        } else {
                            cells2.innerHTML = "否";
                            cells3.innerHTML = "";
                        }
                        if (!isShow) {
                            let cells4=tab.rows[index].insertCell(4);
                            cells4.style.width="10%";
                            cells4.innerHTML = "<button onclick='deleteFile(" + records[i - 1].id + ")'>删除</button>";
                        }
                        index++;
                    }
                }
                fileTable.appendChild(tab);
                let str="<div style='float: right;margin-top: 10px;margin-right:20px;'>";
                for(let i=0;i<Math.ceil(records.length/10);i++){
                    str+="<button style='margin-left:5px' onclick='getFileTable("+i+")'>"+(i+1)+"</button>";
                }
                str+="</div>"
                fileTable.innerHTML+=str;
                if($('#btnView').css("display") == "none") {
                    $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
                }
                $("body").css("overflow-x","hidden")
            }
        },
        error(e){
            alert("System error, please try again later");
        }
    })
}
function CheckThe(obj, name) {
    var checkItem = document.getElementsByName(name)
    for (var i = 0; i < checkItem.length; i++) {
        checkItem[i].checked = false
    }
    if (obj.checked == true) {
        obj.checked = false
    } else {
        obj.checked = true
    }
}

//获取初始信息
function getData() {
    var url = "";
    var boxObj = $("input[type='checkbox']");//获取所有的复选框
    if (id != null && id != undefined && id != "") {
        url = base_api_path + "applicationRecord/queryOne?id=" + id;
        ajax({
            url: url,
            type: 'get',
            timeout: 10000,
            success: function (response) {
                var response = JSON.parse(response);
                if (response.data.records.length > 0) {
                    id = response.data.records[0].id;
                    projectId = response.data.records[0].projectId;
                    status = response.data.records[0].status;
                    attachmentIds = response.data.records[0].attachmentIds;
                    if (response.data.records[0].necessaryUpload) mustFileNames = response.data.records[0].necessaryUpload + ",";
                    if (attachmentIds != "") {
                        getFileTable();
                    }
                    if ((status) == "APPROVAL" || (status) == "PASSED" || (status) == "REJECTED") {
                        document.getElementById("btnView").style.display = "none";//隐藏
                        $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
                        document.getElementById("file").style.display = "none";
                        var inputs = document.getElementsByTagName("input");
                        var textareas = document.getElementsByTagName("textarea");
                        for (var i = 0; i < inputs.length; i++) {
                            inputs[i].readOnly = true;
                        }
                        for (var i = 0; i < textareas.length; i++) {
                            textareas[i].readOnly = true;
                        }
                    }
                    applicationRecord = JSON.parse(response.data.records[0].formJson);
                    autoInputProperties = JSON.parse(response.data.records[0].autoInputProperties);
                    /*autoInput()*/
                    for (var i = 0; i < applicationRecord.length; i++) {
                        for (var key in applicationRecord[i]) {
                            if (key && document.getElementsByTagName("form")[0].elements[key]) {
                                document.getElementsByTagName("form")[i].elements[key].value = applicationRecord[i][key];
                            }
                            for (var k = 0; k < boxObj.length; k++) {
                                if (boxObj[k].id == key && boxObj[k].value == applicationRecord[i][key])  //如果值与修改前的值相等
                                {
                                    boxObj[k].checked = true;
                                    break;
                                }
                            }
                        }
                    }
                    /*validateForm()*/
                }
            },
            //异常处理
            error: function (e) {
                //console.log(e);
            }
        })
    } else {
        url = base_api_path + "applicationRecord/queryAutoInputProperties";
        ajax({
            url: url,
            type: 'get',
            timeout: 10000,
            success: function (response) {
                response = JSON.parse(response);
                if (response.message) {
                    let applicationRecord = JSON.parse(response.message);
                    for (var key in applicationRecord) {
                        if (key && document.getElementsByTagName("form")[0].elements[key]) {
                            document.getElementsByTagName("form")[0].elements[key].value = applicationRecord[key];
                        }
                        if (boxObj) {
                            for (var k = 0; k < boxObj.length; k++) {
                                if (boxObj[k].id == key && boxObj[k].value == applicationRecord[key])  //如果值与修改前的值相等
                                {
                                    boxObj[k].checked = true;
                                    break;
                                }
                            }
                        }
                    }
                    /*validateForm()*/
                }
            },
            //异常处理
            error: function (e) {
            }
        })
    }
}

function getProjectInformationData() {
    if (projectId != null && projectId != undefined && projectId != "") {
        $.ajax({
            url: base_api_path + "/projectInformation/queryOne?id=" + projectId,
            type: "get",
            headers: {"token": window.localStorage.token, "iv": window.localStorage.iv},
            success: function (response) {
                if (response.data.necessaryUpload) mustFileNames = response.data.necessaryUpload + ",";
            },
            error(e) {
                alert("System error, please try again later");
            }
        })
    }
}

function beforeUpload() {
    window.parent.postMessage({
        cmd: 2,
    }, '*');
}

function deleteFile(rowId) {
    attachmentIds = attachmentIds.replace(rowId + ",", "");
    getFileTable();
}

//自动保存的数据
function autoInputSave() {
    let autoInputMap = {}
    autoInputMap['QYMC'] = $(".registerform").find("input[name='QYMC']")[0].value
    autoInputMap['ZS'] = $(".registerform").find("input[name='ZS']")[0].value
    autoInputMap['DGS_TYSHXYDM'] = $(".registerform").find("input[name='DGS_TYSHXYDM']")[0].value
    return JSON.stringify(autoInputMap);
}
