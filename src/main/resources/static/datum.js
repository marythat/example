var base_api_path = "http://127.0.0.1:8071/pt/pa/"
var projectId;
var applicationRecord;
var mustFileExtendName;
var mustFileExtendNames;
let autoInputProperties;
var id;
var status;
var clickButton;
var attachmentIds="";
let operateUrl;
let firstindustypeid;
let dptUser;
let target;
let pdfHeight;
let text;
let mustFileNames;
let acceptCode;
let flag
window.onload=function(){
    new DatePicker({id : "CLRQ", type : "Y-M-D"});
    if(typeof(initDatePicker) == 'function'){
        initDatePicker();
    }

    $('#btnView').css('margin-top',document.getElementsByClassName(currentTable)[0].clientHeight+40+'px')
    var Request=new UrlSearch(); //实例化
    flag = Request.flag
    if(flag) {
        fillRequest()
        setFillField()
    }
    projectId=Request.projectId;
    firstindustypeid=Request.firstindustypeid;
    dptUser=Request.dptUser;
    id=Request.id;
    acceptCode=Request.acceptCode;
    createSelectOption();
    validateForm()
    if (Request.preview) {
        document.getElementById("submit").disabled = true;
        document.getElementById("save").disabled = true;
    }else{
        getData();
        getProjectInformationData();
    }
    if(dptUser){
        document.getElementById("btnView").style.display="none";//隐藏
        document.getElementById("file").style.display="none";
        $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
    }
    validateLoad()
    //用户提交生产受理编号后隐藏一些操作按钮
    if(acceptCode!=undefined&&acceptCode!="null") {
        $("#addButView").hide();
    }
}
function fillRequest(field){
    url = base_api_path + "epspBaseData/getTips?field="+(field?field:"");
    ajax({
        url: url,
        type: 'get',
        timeout: 10000,
        success: function (response) {
            response = JSON.parse(response);
            if (response.success) {
                if(response.data.companyExcelBusinessMap){
                    fillData(response.data.companyExcelBusinessMap)
                }
                if(response.data.filedsTips) {
                    if(!field){
                        operateUrl=response.data.filedsTips.url
                        var fillHtml = '<span>'+response.data.filedsTips.tips+'</span>' +
                            '<button style="background-color: #219AFF" onclick="jumpUrl(operateUrl)">跳转</button>';
                        $(".fillDiv").html(fillHtml)
                    }else{
                        window.parent.postMessage({
                            cmd: 3,
                            params: {
                                url: "/pt/pa/CompanyWorkFlowOperationCompany?projectId=" + id + "&firstindustypeid=" + firstindustypeid,
                                data: response.data.filedsTips
                            }
                        }, '*');
                    }
                }
            }
        },
        //异常处理
        error: function (e) {
        }
    })
}
function jumpUrl(url){
    let tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "post";
    tempForm.action = url;
    tempForm.target = "_parent";    // _blank - URL加载到一个新的窗口

    var hideInput = document.createElement("input");
    hideInput.type = "hidden";
    hideInput.name = "content";
    hideInput.value = "/pt/pa/CompanyWorkFlowOperationCompany?projectId=" + id + "&firstindustypeid=" + firstindustypeid;
    tempForm.appendChild(hideInput);
    if(document.all){    // 兼容不同浏览器
        tempForm.attachEvent("onsubmit",function(){});        //IE
    }else{
        tempForm.addEventListener("submit",function(){},false);    //firefox
    }
    window.parent.document.body.appendChild(tempForm);
    if(document.all){    // 兼容不同浏览器
        tempForm.fireEvent("onsubmit");
    }else{
        tempForm.dispatchEvent(new Event("submit"));
    }
    tempForm.submit();
    window.parent.document.body.removeChild(tempForm);
}
function setFillField() {
    $(".registerform").find("input[name='QYZRS']").attr("disabled", true);
    $(".registerform").find("input[name='QYLX']").attr("disabled", true);
    $(".registerform").find("input[name='XMMC1']").attr("disabled", true);
    $(".registerform").find("input[name='XMMC2']").attr("disabled", true);
    $(".registerform").find("input[name='XMMC3']").attr("disabled", true);
    $(".registerform").find("input[name='QYZRS']").click(function(){
        fillRequest("averageWorker_2019")
    });
    $(".registerform").find("input[name='QYLX']").parent().click(function(){
        fillRequest("reportType_2019")
    });
    $(".registerform").find("input[name='XMMC1']").parent().click(function(){
        fillRequest("businessActive1_2019")
    });
    $(".registerform").find("input[name='XMMC2']").parent().click(function(){
        fillRequest("businessActive2_2019")
    });
    $(".registerform").find("input[name='XMMC3']").parent().click(function(){
        fillRequest("businessCctive3_2019")
    });
}
function fillData(map) {
    $(".registerform").find("input[name='ZZC1']").attr("value", map[2017].allAssets);
    $(".registerform").find("input[name='ZZC2']").attr("value", map[2018].allAssets);
    $(".registerform").find("input[name='ZZC3']").attr("value", map[2019].allAssets);
    $(".registerform").find("input[name='FZZE1']").attr("value", map[2017].allDebt);
    $(".registerform").find("input[name='FZZE2']").attr("value", map[2018].allDebt);
    $(".registerform").find("input[name='FZZE3']").attr("value", map[2019].allDebt);
    $(".registerform").find("input[name='SYZQY1']").attr("value", map[2017].totalOwnerEquity);
    $(".registerform").find("input[name='SYZQY2']").attr("value", map[2018].totalOwnerEquity);
    $(".registerform").find("input[name='SYZQY3']").attr("value", map[2019].totalOwnerEquity);
    $(".registerform").find("input[name='ZYYWSR1']").attr("value", map[2017].mainBusinessIncome);
    $(".registerform").find("input[name='ZYYWSR2']").attr("value", map[2018].mainBusinessIncome);
    $(".registerform").find("input[name='ZYYWSR3']").attr("value", map[2019].mainBusinessIncome);
    $(".registerform").find("input[name='LRZE1']").attr("value", map[2017].totalProfit);
    $(".registerform").find("input[name='LRZE2']").attr("value", map[2018].totalProfit);
    $(".registerform").find("input[name='LRZE3']").attr("value", map[2019].totalProfit);
    $(".registerform").find("input[name='GYZCZ1']").attr("value", map[2017].totalIndustyOutput);
    $(".registerform").find("input[name='GYZCZ2']").attr("value", map[2018].totalIndustyOutput);
    $(".registerform").find("input[name='GYZCZ3']").attr("value", map[2019].totalIndustyOutput);
    $(".registerform").find("input[name='YFJFZC1']").attr("value", map[2017].rdCost);
    $(".registerform").find("input[name='YFJFZC2']").attr("value", map[2018].rdCost);
    $(".registerform").find("input[name='YFJFZC3']").attr("value", map[2019].rdCost);
    $(".registerform").find("input[name='SJSJ1']").attr("value", map[2017].paymentVat);
    $(".registerform").find("input[name='SJSJ2']").attr("value", map[2018].paymentVat);
    $(".registerform").find("input[name='SJSJ3']").attr("value", map[2019].paymentVat);
    $(".registerform").find("input[name='QYZRS']").attr("value", map[2019].averageWorker);
    $(".registerform").find("input[name='QYLX']").attr("value", map[2019].reportType);
    $(".registerform").find("input[name='XMMC1']").attr("value", map[2019].businessActive1);
    $(".registerform").find("input[name='XMMC2']").attr("value", map[2019].businessActive2);
    $(".registerform").find("input[name='XMMC3']").attr("value", map[2019].businessCctive3);
}
function downward() {
    //获取内容盒子
    var contents = document.getElementsByClassName(otherTable)
    var numberInfo = document.getElementById("numberInfo").value;
    var btn = contents.length - 1;//页面数据
    var count = parseInt(numberInfo) + 1;//当前页面
    if (count <= btn) {
        for (var j = 0; j < btn; j++) {
            contents[j].className = contents[j].className.replace(' '+currentTable, '').trim();
        }
        contents[count].className = contents[count].className + ' '+currentTable;
        document.getElementById("numberInfo").value = count;
        var myselect = document.getElementById("pageTurning");
        myselect.options[count].selected = true;
    }
    $('#btnView').css('margin-top',document.getElementsByClassName(currentTable)[0].clientHeight+40+'px')
    $("."+otherTable).attr("style","margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;")
    $("."+currentTable).attr("style","position: absolute;opacity: 1;top: auto;left: auto;width: 98%;")
    if($('#btnView').css("display") == "none") {
        $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
    }
    $("body").css("overflow-x","hidden")
}

function upward() {
    //获取内容盒子
    var contents = document.getElementsByClassName(otherTable)
    var numberInfo = document.getElementById("numberInfo").value;
    var btn = contents.length - 1;
    var count = numberInfo - 1;
    if (count >= 0) {
        for (var j = 0; j < contents.length; j++) {
            contents[j].className = contents[j].className.replace(' '+currentTable, '').trim();
        }
        contents[count].className = contents[count].className + ' '+currentTable;
        document.getElementById("numberInfo").value = count;
        var myselect = document.getElementById("pageTurning");
        myselect.options[count].selected = true;
    }
    $('#btnView').css('margin-top',document.getElementsByClassName(currentTable)[0].clientHeight+40+'px')
    $("."+otherTable).attr("style","margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;")
    $("."+currentTable).attr("style","position: absolute;opacity: 1;top: auto;left: auto;width: 98%;")
    if($('#btnView').css("display") == "none") {
        $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
    }
    $("body").css("overflow-x","hidden")
}

function func() {
    var myselect = document.getElementById("pageTurning");
    var index = myselect.selectedIndex;
    var val = myselect.options[index].value;
    var contents = document.getElementsByClassName(otherTable)
    if (val <= contents.length - 1) {
        for (var j = 0; j < contents.length; j++) {
            contents[j].className = contents[j].className.replace(' '+currentTable, '').trim();
        }
        contents[val].className = contents[val].className + ' '+currentTable;
        document.getElementById("numberInfo").value = val;
    }
    $('#btnView').css('margin-top',document.getElementsByClassName(currentTable)[0].clientHeight+40+'px')
    $("."+otherTable).attr("style","margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;")
    $("."+currentTable).attr("style","position: absolute;opacity: 1;top: auto;left: auto;width: 98%;")
    if($('#btnView').css("display") == "none") {
        $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
    }
    $("body").css("overflow-x","hidden")
}

//跳转到表单验证失败页面
function error(val) {
    let contents = $("."+otherTable)
    for(let i = 0; i < contents.length; i++){
        if($('.'+otherTable).eq(i).find("[name="+val+"]").length>0){
            document.getElementById("pageTurning").options[i].selected = true;
            func()
        }
    }
}

//加载事件
function createSelectOption() {
    var contents = document.getElementsByClassName(otherTable);
    //用标记id的td元素 获取值方法
    var thVal = "";
    for (var i = 0; i < contents.length; i++) {
        thVal = contents[i].getElementsByTagName("table")[0].childNodes[1].firstElementChild.firstElementChild.innerText;
        document.getElementById("pageTurning").options.add(new Option(thVal, i));
    }
    document.getElementById("pageTurning").options[0].selected = true;
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

//提交信息
function butSubmit(type) {
    if(type=="1"){
        if(mustFileNames&&mustFileNames!=undefined){
            if(!isEquil(mustFileNames,mustFileExtendNames)){
                alert("请按要求完成上传文件");
                return;
            }
        }
    }
    var message="";
    if(type=="1"){
        status="APPROVAL";
        message="提交成功！";
    }
    if(type=="0"){
        //有受理编号，保存都为回退类型
        if(acceptCode!=undefined&&acceptCode!="null") {
            status="RETURN";
        }else{
            status="DRAFT";
        }
        message="保存成功！";
    }
    if(type=="9"){
        status="DRAFT";
        message="回滚成功！";
    }
    var formJson=formser();


    let autoInputMap = autoInputSave()
    var myForm = {status:status,"id":id,"projectId":projectId,"attachmentIds":attachmentIds,"formJson":JSON.stringify(formJson),"autoInputProperties":autoInputMap};
    ajax({
        url:base_api_path + "applicationRecord/submit",
        type:'post',
        data:JSON.stringify(myForm),
        timeout:10000,
        contentType:"application/json",
        success:function(response){
            var response=JSON.parse(response);
            if(response.code==200){
                id=response.data.id;
                if(type=="1"){
                    //document.getElementById("submit").disabled=true;
                    //document.getElementById("save").disabled=true
                    document.getElementById("btnView").style.display="none";//隐藏
                    $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
                    document.getElementById("file").style.display="none";
                    var inputs=document.getElementsByTagName("input");
                    var textareas=document.getElementsByTagName("textarea");
                    for(var i=0;i<inputs.length;i++){
                        inputs[i].readOnly=true;
                    }
                    for(var i=0;i<textareas.length;i++){
                        textareas[i].readOnly=true;
                    }
                }
                if(type=="9"){
                    //document.getElementById("submit").disabled=false;
                    //document.getElementById("save").disabled=false;
                    document.getElementById("btnView").style.display="none";//隐藏
                    $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
                    var inputs=document.getElementsByTagName("input");
                    var textareas=document.getElementsByTagName("textarea");
                    for(var i=0;i<inputs.length;i++){
                        inputs[i].readOnly=false;
                    }
                    for(var i=0;i<textareas.length;i++){
                        textareas[i].readOnly=false;
                    }
                }
                alert(message);
                window.parent.postMessage({
                    cmd: 1,
                }, '*');
            }else{
                alert(response.message);
            }
        },
        error:function(e){
        }
    })
}
//获取初始信息
function getData() {
    var url="";
    var boxObj=$("input[type='checkbox']");//获取所有的复选框
    if(id!=null&&id!=undefined&&id!="") {
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
                    attachmentIds=response.data.records[0].attachmentIds;
                    if(response.data.records[0].necessaryUpload)mustFileNames=response.data.records[0].necessaryUpload+",";
                    if(attachmentIds!=""){
                        getFileTable();
                    }
                    if ((status) == "APPROVAL" || (status) == "PASSED" || (status) == "REJECTED") {
                        document.getElementById("fileTable").style.display="none";
                        document.getElementById("btnView").style.display = "none";//隐藏
                        $('#fileTable').css('margin-top', document.getElementsByClassName(currentTable)[0].clientHeight + 40 + 'px')
                        document.getElementById("file").style.display="none";
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
                    autoInput()
                    for (var i = 0; i < applicationRecord.length; i++) {
                        for (var key in applicationRecord[i]) {
                            var formInfo=document.getElementsByTagName("form")[0];
                            if(key&&formInfo.elements[key]&&formInfo.elements[key].type!="checkbox") {
                                document.getElementsByTagName("form")[i].elements[key].value = applicationRecord[i][key];
                            }
                            for(var k=0;k<boxObj.length;k++) {
                                if (boxObj[k].id==key&&boxObj[k].value==applicationRecord[i][key]&&boxObj[k].value!="")  //如果值与修改前的值相等
                                {
                                    boxObj[k].checked = true;
                                    break;
                                }
                            }
                        }
                    }
                    validateForm()
                }
            },
            //异常处理
            error: function (e) {
                //console.log(e);
            }
        })
    }else{
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
                        if(key&&document.getElementsByTagName("form")[0].elements[key]) {
                            document.getElementsByTagName("form")[0].elements[key].value = applicationRecord[key];
                        }
                        if(boxObj) {
                            for (var k = 0; k < boxObj.length; k++) {
                                if (boxObj[k].id == key && boxObj[k].value == applicationRecord[key]&&boxObj[k].value!="")  //如果值与修改前的值相等
                                {
                                    boxObj[k].checked = true;
                                    break;
                                }
                            }
                        }
                    }
                    validateForm()
                }
            },
            //异常处理
            error: function (e) {
            }
        })
    }
    var boxObj2=$("input[type='checkbox']");//获取所有的复选框
}

function UrlSearch() {
    var name,value;
    var str=decodeURIComponent(parent.location.href); //取得整个地址栏
    var num=str.indexOf("?");
    str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]
    var arr=str.split('&'); //各个参数放到数组里
    for(var i=0;i<arr.length;i++){
        num=arr[i].indexOf("=");
        if(num>0){
            name=arr[i].substring(0,num);
            value=arr[i].substr(num+1);
            this[name]=value;
        }
    }
}

function ajax(options){
    options = options ||{};  //调用函数时如果options没有指定，就给它赋值{},一个空的Object
    options.type=(options.type || "GET").toUpperCase();/// 请求格式GET、POST，默认为GET
    options.dataType=options.dataType || "json";    //响应数据格式，默认json
    options.contentType=options.contentType||"application/x-www-form-urlencoded";
    var params=options.data;//options.data请求的数据
    var xhr;
    //考虑兼容性
    if(window.XMLHttpRequest){
        xhr=new XMLHttpRequest();
    }else if(window.ActiveObject){//兼容IE6以下版本
        xhr=new ActiveXobject('Microsoft.XMLHTTP');
    }

    //启动并发送一个请求
    if(options.type=="GET"){
        if (options.url.indexOf("?") == -1) {
            options.url = options.url + (params?"?" + params:'');
        } else {
            options.url = options.url + (params?"&" + params:'');
        }
        xhr.open("GET",options.url,true);
        params = null
    }else if(options.type=="POST"){
        xhr.open("post",options.url,true);

        //设置表单提交时的内容类型
        //Content-type数据请求的格式
        xhr.setRequestHeader("Content-type",options.contentType);
        // ajax.setRequestHeader("X-Requested-With","XMLHttpRequest");
    }
    xhr.setRequestHeader("token",window.localStorage.token);
    xhr.setRequestHeader("iv",window.localStorage.iv);
    xhr.setRequestHeader("phone",window.localStorage.phone)
    xhr.send(params);
    //    设置有效时间
    setTimeout(function(){
        if(xhr.readySate!=4){
            xhr.abort();
        }
    },options.timeout)

    //options.success成功之后的回调函数  options.error失败后的回调函数
    //xhr.responseText,xhr.responseXML  获得字符串形式的响应数据或者XML形式的响应数据
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4){
            var status=xhr.status;
            if(status>=200&&status<300 || status==304){
                options.success&&options.success(xhr.responseText,xhr.responseXML);
            }else{
                options.error&&options.error(status);
            }
        }
    }
}

//from表单转json
function formser() {
    var form = document.getElementsByTagName("form");
    var arr1 = [];

    var dynamicTableVlue = null;
    if(typeof(dynamicTableJson) == 'function'){
        dynamicTableVlue=dynamicTableJson();
    }

    /*兼并重组信息动态数据*/
    var dynamicReorganizationInformationValue = null;
    if(typeof (dynamicReorganizationInformationJson) == 'function'){
        dynamicReorganizationInformationValue = dynamicReorganizationInformationJson();
    }
    /*企业兼并重组并购企业情况数据*/
    var dynamicIncentiveShareholedersValue = null;
    if(typeof (dynamicIncentiveShareholedersJson) == 'function'){
        dynamicIncentiveShareholedersValue = dynamicIncentiveShareholedersJson();
    }
    /*奖励个人申请表*/
    var dynamicPersionalApplicationFormValue = null;
    if(typeof (dynamicPersionalApplicationFormValJson) == 'function'){
        dynamicPersionalApplicationFormValue = dynamicPersionalApplicationFormValJson();
    }
    /*应收款融资奖励项目*/
    var dynamicReceivablesFinanceAwardValue = null;
    if(typeof (dynamicReceivablesFinanceAwardJson) == 'function'){
        dynamicReceivablesFinanceAwardValue = dynamicReceivablesFinanceAwardJson();
    }

    for(var j=0;j<form.length;j++) {
        var arr = {};
        for (var i = 0; i < form[j].elements.length; i++) {
            var feled = form[j].elements[i];
            switch (feled.type) {
                case undefined:
                case 'button':
                case 'file':
                case 'reset':
                case 'submit':
                case 'hidden':
                    break;
                case 'checkbox':
                    if (!feled.checked&&!arr[feled.name]) {
                        arr[feled.name] = "";
                    }
                    if(feled.checked){
                        arr[feled.name] = feled.value;
                    }
                    break;
                case 'radio':
                    if (!feled.checked) {
                        arr[feled.name] = "";
                    }else{
                        arr[feled.name] = feled.value;
                    }
                    break;
                default:
                    if(feled.name) {
                        /* if (arr[feled.name]) {
                             arr[feled.name] = arr[feled.name] + ',' + feled.value;
                         } else {}*/
                        arr[feled.name] = feled.value;


                    }
            }
        }
        if(dynamicTableVlue!=null&&dynamicTableVlue!=undefined){
            arr["dynamicTableVlue"] = dynamicTableVlue;
        }
        if(dynamicReorganizationInformationValue != null && dynamicReorganizationInformationValue!=undefined){
            arr["dynamicReorganizationInformationValue"] = dynamicReorganizationInformationValue;
        }
        if(dynamicIncentiveShareholedersValue !=null && dynamicIncentiveShareholedersValue != undefined){
            arr["dynamicIncentiveShareholedersValue"] = dynamicIncentiveShareholedersValue;
        }
        if(dynamicPersionalApplicationFormValue!=null && dynamicPersionalApplicationFormValue != undefined){
            arr["dynamicPersionalApplicationFormValue"] = dynamicPersionalApplicationFormValue;
        }
        if(dynamicReceivablesFinanceAwardValue !=null && dynamicReceivablesFinanceAwardValue != undefined){
            arr["dynamicReceivablesFinanceAwardValue"] = dynamicReceivablesFinanceAwardValue;
        }

        arr1.push(arr);
    }
    return arr1;
}


function downPdf(){

    if(confirm("您确认下载该PDF文件吗?")) {
        comfirmPdf(acceptCode);
        var target = $("#exportToPdf");    // 这个dom元素是要导出pdf的div容器
        var w = target.width(); // 获得该容器的宽
        var h = target.height(); // 获得该容器的高
        var offsetTop = target.offset().top; // 获得该容器到文档顶部的距离
        var offsetLeft = target.offset().left; // 获得该容器到文档最左的距离
        var canvas = document.createElement("canvas");
        var abs = 0;
        var win_i = $(window).width(); // 获得当前可视窗口的宽度（不包含滚动条）
        var win_o = window.innerWidth; // 获得当前窗口的宽度（包含滚动条）
        if (win_o > win_i) {
            abs = (win_o - win_i) / 2; // 获得滚动条长度的一半
        }
        canvas.width = w * 2; // 将画布宽&&高放大两倍
        canvas.height = h * 2;
        var context = canvas.getContext("2d");
        context.scale(2, 2);
        //context.translate(-offsetLeft-abs,-offsetTop);
        // 这里默认横向没有滚动条的情况，因为offset.left(),有无滚动条的时候存在差值，因此
        // translate的时候，要把这个差值去掉
        html2canvas(target).then(function (canvas) {
            var contentWidth = canvas.width;
            var contentHeight = canvas.height;
            //一页pdf显示html页面生成的canvas高度;
            var pageHeight = contentWidth / 592.28 * 841.89;
            //未生成pdf的html页面高度
            var leftHeight = contentHeight;
            //页面偏移
            var position = 0;
            //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
            var imgWidth = 595.28;
            var imgHeight = 592.28 / contentWidth * contentHeight;

            //添加水印信息begin
            if(acceptCode!=undefined&&acceptCode!="null") {
                canvas.style.display = 'none';
                var cans = canvas.getContext('2d');
                for (var p = 0; p < 15; p++) {
                    for (var q = 0; q < 20; q++) {
                        // 绘制文字  注意：：绘制斜体文字 旋转以后会发生位移，所以必须在旋转之后进行位置的调整；
                        //drawText((q * -120),-(50 * p),((p * 250)),((q * 200)+200),cans,"受理编号")
                        drawText((q * -120), -(50 * p), ((p * 250)), ((q * 200) + 200), cans, acceptCode)
                    }
                }
            }

            var pageData = canvas.toDataURL('image/jpeg', 1.0);
            var pdf = new jsPDF('', 'pt', 'a4');
            //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
            //当内容未超过pdf一页显示的范围，无需分页
            if (leftHeight < pageHeight) {
                pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
            } else { // 分页
                while (leftHeight > 0) {
                    pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight - 20)
                    leftHeight -= pageHeight + 30;
                    position -= 841.89;
                    //避免添加空白页
                    if (leftHeight > 0) {
                        pdf.addPage();
                    }
                }
            }
            var date = new Date();
            var seperator1 = "";
            var seperator2 = "";
            var month = date.getMonth() + 1;
            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + "" + date.getHours() + seperator2 + date.getMinutes()
                + seperator2 + date.getSeconds();

            pdf.save(text + currentdate + ".pdf");
            restore(pdfHeight)
        })
    }

}

function templetPdf(templetName){
    if(id!=undefined&&id!="null") {
        url = base_api_path + "applicationRecord/exportDetail/"+id+"."+templetName;
        window.location = url;
    }else{
        alert("请先保存表单信息在进行导出PDF");
    }

}

function templetFtlPdf(templetCompleteName){
    if(id!=undefined&&id!="null") {
        url = base_api_path + "applicationRecord/exportDetail/"+id+"."+templetCompleteName;
        window.location = url;
    }else{
        alert("请先保存表单信息在进行导出PDF");
    }

}

function drawText(tsx,tsy,x,y,ctx,title) {
    ctx.save();//保存原来的状态  绘制字体都是需要旋转倾斜  那么之前绘制的图片就要进行状态的保存
    ctx.rotate(-Math.PI/6);//绘制倾斜字体
    ctx.translate(tsx,tsy);//发生位移进行位移的恢复
    ctx.font = "16px serif";
    ctx.fillStyle = 'red';
    ctx.fillText(title, x,y);
    ctx.restore();//状态的恢复
}

function readLocalFile(extProperty) {
    var fileObj;
    if(extProperty){
        fileObj= document.getElementById("MustUploadFile").files[0];
    }else{
        fileObj= document.getElementById("uploadFile").files[0];
    }
    // js 获取文件对象
    if(fileObj==null||fileObj==undefined)return;
    if(fileObj.size>31457280){
        alert("上传文件不能超过30MB");
        return;
    }
    var url =  base_api_path + "commAttachment/ftpStore"; // 接收上传文件的后台地址
    var form = new FormData(); // FormData 对象
    form.append("file", fileObj); // 文件对象
    if(extProperty){
        form.append("extProperty", mustFileExtendName);
    }
    $.ajax({
        type:"post",
        url:url,
        data:form,
        async: false,
        contentType: false,
        processData: false,
        headers:{"token":window.localStorage.token,"iv":window.localStorage.iv},
        success: function (data) {
            if(data.code==200){
                if(data.data!=null){
                    attachmentIds+= data.data.id+",";
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

function CheckThe(obj,name){
    var  checkItem=document.getElementsByName(name);
    for(var i=0;i <checkItem.length;i++){
        checkItem[i].checked=false;
    }
    if(obj.checked==true){
        obj.checked=false;
    }else{
        obj.checked=true;
    }
}

function beforeUpload(){
    window.parent.postMessage({
        cmd: 2,
    }, '*');
}

function deleteFile(rowId) {
    attachmentIds=attachmentIds.replace(rowId+",","");
    getFileTable();
}
function getProjectInformationData(){
    if(projectId!=null&&projectId!=undefined&&projectId!="") {
        $.ajax({
            url: base_api_path+"/projectInformation/queryOne?id=" + projectId,
            type: "get",
            headers: {"token": window.localStorage.token, "iv": window.localStorage.iv},
            success: function (response) {
                if(response.data.necessaryUpload)mustFileNames = response.data.necessaryUpload+",";
            },
            error(e) {
                alert("System error, please try again later");
            }
        })
    }
}

