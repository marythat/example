//专项资金申报系统企业信息标准化表格
//当前页面table的className
let currentTable = 'contentTable1';
////非当前页面table的className
let otherTable = 'contentTable';
//表单验证回调
$.validator.setDefaults({
    submitHandler: function () {
        if (clickButton && clickButton == "1") {
            butSubmit(clickButton)
        }
    },
    invalidHandler: function (form, validator) {  //不通过回调
        if (clickButton && clickButton == "0"||flag) {//保存不显示提示
        } else {
            error(validator.errorList[0].element.name)
        }
    }
});

$().ready(function () {
    $(".registerform").validate({
        rules: {
            QYJJ: {
                required: true,
                maxlength: 300
            },
            ZZC1: {
                required: true
            },
            ZZC2: {
                required: true
            },
            ZZC3: {
                required: true
            },
            ZZC4: {
                required: true
            },
            FZZE1: {
                required: true
            },
            FZZE2: {
                required: true
            },
            FZZE3: {
                required: true
            },
            FZZE4: {
                required: true
            },
            SYZQY1: {
                required: true
            },
            SYZQY2: {
                required: true
            },
            SYZQY3: {
                required: true
            },
            SYZQY4: {
                required: true
            },
            ZYYWSR1: {
                required: true
            },
            ZYYWSR2: {
                required: true
            },
            ZYYWSR3: {
                required: true
            },
            ZYYWSR4: {
                required: true
            },
            LRZE1: {
                required: true
            },
            LRZE2: {
                required: true
            },
            LRZE3: {
                required: true
            },
            LRZE4: {
                required: true
            },
            YFJFZC1: {
                required: true
            },
            YFJFZC2: {
                required: true
            },
            YFJFZC3: {
                required: true
            },
            YFJFZC4: {
                required: true
            },
            SJSJ1: {
                required: true
            },
            SJSJ2: {
                required: true
            },
            SJSJ3: {
                required: true
            },
            SJSJ4: {
                required: true
            },
            QYYYZZDZ: {
                required: true
            },
            institutionsCode: {
                required: true
            },
            LXR_XS: {
                required: true
            },
            ZZJGDM_XS: {
                required: true
            },
            ZZJG_LXDH: {
                required: true
            },
            ZYYWSR: {
                required: true
            },
            NNSE: {
                required: true
            },
            ZCZB2_XS: {
                required: true
            },
            CLRQ_XS: {
                required: true
            },
           /* KHYHMC_XS: {
                required: true
            },
            KHYHZH_XS: {
                required: true
            }*/
        },
        messages: {
            QYJJ: {
                required: "请输入企业简介",
                minlength: "企业简介最大为500字，请注意！！"
            },
            ZZC1: {
                required: "必填",
            },
            ZZC2: {
                required: "必填",
            },
            ZZC3: {
                required: "必填",
            },
            ZZC4: {
                required: "必填",
            },
            FZZE1: {
                required: "必填",
            },
            FZZE2: {
                required: "必填",
            },
            FZZE3: {
                required: "必填",
            },
            FZZE4: {
                required: "必填",
            },
            SYZQY1: {
                required: "必填",
            },
            SYZQY2: {
                required: "必填",
            },
            SYZQY3: {
                required: "必填",
            },
            SYZQY4: {
                required: "必填",
            },
            ZYYWSR1: {
                required: "必填",
            },
            ZYYWSR2: {
                required: "必填",
            },
            ZYYWSR3: {
                required: "必填",
            },
            ZYYWSR4: {
                required: "必填",
            },
            LRZE1: {
                required: "必填",
            },
            LRZE2: {
                required: "必填",
            },
            LRZE3: {
                required: "必填",
            },
            LRZE4: {
                required: "必填",
            },
            YFJFZC1: {
                required: "必填",
            },
            YFJFZC2: {
                required: "必填",
            },
            YFJFZC3: {
                required: "必填",
            },
            YFJFZC4: {
                required: "必填",
            },
            SJSJ1: {
                required: "必填",
            },
            SJSJ2: {
                required: "必填",
            },
            SJSJ3: {
                required: "必填",
            },
            SJSJ4: {
                required: "必填",
            },
            QYYYZZDZ: {
                required: "必填",
            },
            institutionsCode: {
                required: "必填",
            },
            LXR_XS: {
                required: "必填",
            },
            ZZJGDM_XS: {
                required: "必填",
            },
            ZZJG_LXDH: {
                required: "必填",
            },
            ZYYWSR: {
                required: "必填",
            },
            NNSE: {
                required: "必填",
            },
            ZCZB2_XS: {
                required: "必填",
            },
            CLRQ_XS: {
                required: "必填",
            },
           /* KHYHMC_XS: {
                required: "必填",
            },
            KHYHZH_XS: {
                required: "必填",
            }*/
        }
    });
    $("#submit").click(function () {
        clickButton = "1"
        if(flag){
            butSubmit(1);
        }
    });
    $("#save").click(function () {
        clickButton = "0"
        butSubmit(0);
    });

    //如果有个性化定制的表格验证
    if(typeof(handleValid) == 'function'){
        handleValid();
    }
});

//确定要打印pdf的范围及pdf命名
function comfirmPdf() {
    target = document.getElementsByClassName("right-aside")[0];
    target.style.background = "#FFFFFF";
    pdfHeight = $(".right-aside").eq(0).height()
    $("." + currentTable).attr("style", "display: block;position: relative;opacity: 1;top: auto;left: auto;width: 98%;")
    $("." + otherTable).attr("style", "display: block;margin: 10px auto;position: relative;opacity: 1;top: auto;left: auto;width: 98%;")
    $(".right-aside").eq(0).css("height", document.getElementsByClassName('right-aside')[0].scrollHeight + 20)
    text = $("#QYMC").val();
}

//还原pdf打印时样式修改
function restore() {
    $("." + otherTable).attr("style", "margin: 10px auto;position: absolute;opacity: 0;top: -9999px;left: -9999px;width: 98%;")
    $("." + currentTable).attr("style", "position: absolute;opacity: 1;top: auto;left: auto;width: 98%;")
    $(".right-aside").eq(0).css("height", pdfHeight)
}

function validateLoad() {
    if (firstindustypeid && firstindustypeid == 0) {
        changeRules(true, ".GYZCZ")
        changeRules(true, ".GYTZ")
        changeRules(true, ".ZYWRWPF")
        changeRules(true, ".NYZXHL")
        changeRules(true, ".YSL")
    }
}

//动态添加表单验证自动
function validateForm() {
    ZZCChange(".ZZC")
    FZZEChange(".FZZE")
    SYZQYChange(".SYZQY")
    ZYYWSRChange(".ZYYWSR")
    LRZEChange(".LRZE")
    YFJFZCChange(".YFJFZC")
    SJSJChange(".SJSJ")
    if (firstindustypeid && firstindustypeid == 0) {
        GYZCZChange(".GYZCZ")
        GYTZChange(".GYTZ")
        ZYWRWPFChange(".ZYWRWPF")
        NYZXHLChange(".NYZXHL")
        YSLChange(".YSL")
    }
}

//自动填充的数据
function autoInput() {
    if(typeof(handleTable) == 'function'){
        handleTable();
    }
    $(".registerform").find("input[name='QYMC']").attr("value", autoInputProperties['QYMC']);
    $(".registerform").find("input[name='DGS_TYSHXYDM']").attr("value", autoInputProperties['DGS_TYSHXYDM']);
    $(".registerform").find("input[name='QYLX']").attr("value", autoInputProperties['QYLX']);
    $(".registerform").find("input[name='ZCZB2']").attr("value", autoInputProperties['ZCZB2']);
    $(".registerform").find("input[name='DGS_TZZEBZ']").attr("value", autoInputProperties['DGS_TZZEBZ']);
    $(".registerform").find("input[name='CLRQ']").attr("value", autoInputProperties['CLRQ']);
    $(".registerform").find("input[name='street']").attr("value", autoInputProperties['street']);
    $(".registerform").find("input[name='ZS']").attr("value", autoInputProperties['ZS']);
    $(".registerform").find("input[name='FDDBR']").attr("value", autoInputProperties['FDDBR']);
    $(".registerform").find("input[name='LXDH']").attr("value", autoInputProperties['LXDH']);
    $(".registerform").find("input[name='leaderPerson']").attr("value", autoInputProperties['leaderPerson']);
    $(".registerform").find("input[name='leaderPhone']").attr("value", autoInputProperties['leaderPhone']);
    $(".registerform").find("input[name='managePerson']").attr("value", autoInputProperties['managePerson']);
    $(".registerform").find("input[name='managePhone']").attr("value", autoInputProperties['managePhone']);
    $(".registerform").find("input[name='userName']").attr("value", autoInputProperties['userName']);
    $(".registerform").find("input[name='mobile']").attr("value", autoInputProperties['mobile']);
    $(".registerform").find("input[name='email']").attr("value", autoInputProperties['email']);
    $(".registerform").find("input[name='ZYYWSR']").attr("value", autoInputProperties['ZYYWSR']);
    $(".registerform").find("input[name='ZYYWSR3']").attr("value", autoInputProperties['ZYYWSR']);
    $(".registerform").find("input[name='QYYYZZDZ']").attr("value", autoInputProperties['ZS']);
    $(".registerform").find("input[name='userName']").attr("value", autoInputProperties['userName']);
    $(".registerform").find("input[name='LXR_XS']").attr("value", autoInputProperties['userName']);
    $(".registerform").find("input[name='ZZJGDM_XS']").attr("value", autoInputProperties['DGS_TYSHXYDM']);
    $(".registerform").find("input[name='ZZJG_LXDH']").attr("value", autoInputProperties['mobile']);
    $(".registerform").find("input[name='ZCZB2_XS']").attr("value", autoInputProperties['ZCZB2']);
    $(".registerform").find("input[name='CLRQ_XS']").attr("value", autoInputProperties['CLRQ']);
    $(".registerform").find("input[name='KHYHMC']").attr("value", autoInputProperties['KHYHMC']);
    $(".registerform").find("input[name='KHYHMC_XS']").attr("value", autoInputProperties['KHYHMC']);
    $(".registerform").find("input[name='KHYHZH']").attr("value", autoInputProperties['KHYHZH']);
    $(".registerform").find("input[name='KHYHZH_XS']").attr("value", autoInputProperties['KHYHZH']);
}

//自动保存的数据
function autoInputSave() {
    let autoInputMap = {}
    autoInputMap['QYMC'] = $(".registerform").find("input[name='QYMC']")[0].value
    autoInputMap['DGS_TYSHXYDM'] = $(".registerform").find("input[name='DGS_TYSHXYDM']")[0].value
    autoInputMap['QYLX'] = $(".registerform").find("input[name='QYLX']")[0].value
    autoInputMap['ZCZB2'] = $(".registerform").find("input[name='ZCZB2']")[0].value
    autoInputMap['DGS_TZZEBZ'] = $(".registerform").find("input[name='DGS_TZZEBZ']")[0].value
    autoInputMap['CLRQ'] = $(".registerform").find("input[name='CLRQ']")[0].value
    autoInputMap['street'] = $(".registerform").find("input[name='street']")[0].value
    autoInputMap['ZS'] = $(".registerform").find("input[name='ZS']")[0].value
    autoInputMap['FDDBR'] = $(".registerform").find("input[name='FDDBR']")[0].value
    autoInputMap['LXDH'] = $(".registerform").find("input[name='LXDH']")[0].value
    autoInputMap['leaderPerson'] = $(".registerform").find("input[name='leaderPerson']")[0].value
    autoInputMap['leaderPhone'] = $(".registerform").find("input[name='leaderPhone']")[0].value
    autoInputMap['managePerson'] = $(".registerform").find("input[name='managePerson']")[0].value
    autoInputMap['managePhone'] = $(".registerform").find("input[name='managePhone']")[0].value
    autoInputMap['userName'] = $(".registerform").find("input[name='userName']")[0].value
    autoInputMap['mobile'] = $(".registerform").find("input[name='mobile']")[0].value
    autoInputMap['email'] = $(".registerform").find("input[name='email']")[0].value
    /*autoInputMap['ZYYWSR']=$(".registerform").find("input[name='ZYYWSR']")[0].value*/
    autoInputMap['KHYHMC'] = $(".registerform").find("input[name='KHYHMC']")[0].value
    autoInputMap['KHYHZH'] = $(".registerform").find("input[name='KHYHZH']")[0].value
    return JSON.stringify(autoInputMap)
}

//资产总额
$(".registerform").find("input[name='ZZC1']").bind("input propertychange", function (event) {
    ZZCChange(".ZZC")
    $(".registerform").valid()
});
$(".registerform").find("input[name='ZZC2']").bind("input propertychange", function (event) {
    ZZCChange(".ZZC")
    $(".registerform").valid()
});
$(".registerform").find("input[name='ZZC3']").bind("input propertychange", function (event) {
    ZZCChange(".ZZC")
    $(".registerform").valid()
});
$(".registerform").find("input[name='ZZC4']").bind("input propertychange", function (event) {
    ZZCChange(".ZZC")
    $(".registerform").valid()
});

function ZZCChange(key) {
    if ($(".registerform").find("input[name='ZZC1']")[0].value || $(".registerform").find("input[name='ZZC2']")[0].value ||
        $(".registerform").find("input[name='ZZC3']")[0].value || $(".registerform").find("input[name='ZZC4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//负债总额
$(".registerform").find("input[name='FZZE1']").bind("input propertychange", function (event) {
    FZZEChange(".FZZE")
    $(".registerform").valid()
});
$(".registerform").find("input[name='FZZE2']").bind("input propertychange", function (event) {
    FZZEChange(".FZZE")
    $(".registerform").valid()
});
$(".registerform").find("input[name='FZZE3']").bind("input propertychange", function (event) {
    FZZEChange(".FZZE")
    $(".registerform").valid()
});
$(".registerform").find("input[name='FZZE4']").bind("input propertychange", function (event) {
    FZZEChange(".FZZE")
    $(".registerform").valid()
});

function FZZEChange(key) {
    if ($(".registerform").find("input[name='FZZE1']")[0].value || $(".registerform").find("input[name='FZZE2']")[0].value ||
        $(".registerform").find("input[name='FZZE3']")[0].value || $(".registerform").find("input[name='FZZE4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//所有者权益
$(".registerform").find("input[name='SYZQY1']").bind("input propertychange", function (event) {
    SYZQYChange(".SYZQY")
    $(".registerform").valid()
});
$(".registerform").find("input[name='SYZQY2']").bind("input propertychange", function (event) {
    SYZQYChange(".SYZQY")
    $(".registerform").valid()
});
$(".registerform").find("input[name='SYZQY3']").bind("input propertychange", function (event) {
    SYZQYChange(".SYZQY")
    $(".registerform").valid()
});
$(".registerform").find("input[name='SYZQY4']").bind("input propertychange", function (event) {
    SYZQYChange(".SYZQY")
    $(".registerform").valid()
});

function SYZQYChange(key) {
    if ($(".registerform").find("input[name='SYZQY1']")[0].value || $(".registerform").find("input[name='SYZQY2']")[0].value ||
        $(".registerform").find("input[name='SYZQY3']")[0].value || $(".registerform").find("input[name='SYZQY4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//主营业务收入
$(".registerform").find("input[name='ZYYWSR1']").bind("input propertychange", function (event) {
    ZYYWSRChange(".ZYYWSR")
    $(".registerform").valid()
});
$(".registerform").find("input[name='ZYYWSR2']").bind("input propertychange", function (event) {
    ZYYWSRChange(".ZYYWSR")
    $(".registerform").valid()
});
$(".registerform").find("input[name='ZYYWSR3']").bind("input propertychange", function (event) {
    $(".registerform").find("input[name='ZYYWSR']").val($(".registerform").find("input[name='ZYYWSR3']")[0].value)
    ZYYWSRChange(".ZYYWSR")
    $(".registerform").valid()
});
$(".registerform").find("input[name='ZYYWSR4']").bind("input propertychange", function (event) {
    ZYYWSRChange(".ZYYWSR")
    $(".registerform").valid()
});
//2019主营业务收入
$(".registerform").find("input[name='ZYYWSR']").bind("input propertychange", function (event) {
    $(".registerform").find("input[name='ZYYWSR3']").val($(".registerform").find("input[name='ZYYWSR']")[0].value)
    $(".registerform").valid()
});


function ZYYWSRChange(key) {
    if ($(".registerform").find("input[name='ZYYWSR1']")[0].value || $(".registerform").find("input[name='ZYYWSR2']")[0].value ||
        $(".registerform").find("input[name='ZYYWSR3']")[0].value || $(".registerform").find("input[name='ZYYWSR4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//工业总产值
$(".registerform").find("input[name='GYZCZ1']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYZCZChange(".GYZCZ")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='GYZCZ2']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYZCZChange(".GYZCZ")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='GYZCZ3']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYZCZChange(".GYZCZ")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='GYZCZ4']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYZCZChange(".GYZCZ")
        $(".registerform").valid()
    }
});

function GYZCZChange(key) {
    if ($(".registerform").find("input[name='GYZCZ1']")[0].value || $(".registerform").find("input[name='GYZCZ2']")[0].value ||
        $(".registerform").find("input[name='GYZCZ3']")[0].value || $(".registerform").find("input[name='GYZCZ4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//利润总额
$(".registerform").find("input[name='LRZE1']").bind("input propertychange", function (event) {
    LRZEChange(".LRZE")
    $(".registerform").valid()
});
$(".registerform").find("input[name='LRZE2']").bind("input propertychange", function (event) {
    LRZEChange(".LRZE")
    $(".registerform").valid()
});
$(".registerform").find("input[name='LRZE3']").bind("input propertychange", function (event) {
    LRZEChange(".LRZE")
    $(".registerform").valid()
});
$(".registerform").find("input[name='LRZE4']").bind("input propertychange", function (event) {
    LRZEChange(".LRZE")
    $(".registerform").valid()
});

function LRZEChange(key) {
    if ($(".registerform").find("input[name='LRZE1']")[0].value || $(".registerform").find("input[name='LRZE2']")[0].value ||
        $(".registerform").find("input[name='LRZE3']")[0].value || $(".registerform").find("input[name='LRZE4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//研发经费支出
$(".registerform").find("input[name='YFJFZC1']").bind("input propertychange", function (event) {
    YFJFZCChange(".YFJFZC")
    $(".registerform").valid()
});
$(".registerform").find("input[name='YFJFZC2']").bind("input propertychange", function (event) {
    YFJFZCChange(".YFJFZC")
    $(".registerform").valid()
});
$(".registerform").find("input[name='YFJFZC3']").bind("input propertychange", function (event) {
    YFJFZCChange(".YFJFZC")
    $(".registerform").valid()
});
$(".registerform").find("input[name='YFJFZC4']").bind("input propertychange", function (event) {
    YFJFZCChange(".YFJFZC")
    $(".registerform").valid()
});

function YFJFZCChange(key) {
    if ($(".registerform").find("input[name='YFJFZC1']")[0].value || $(".registerform").find("input[name='YFJFZC2']")[0].value ||
        $(".registerform").find("input[name='YFJFZC3']")[0].value || $(".registerform").find("input[name='YFJFZC4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//实缴税金
$(".registerform").find("input[name='SJSJ1']").bind("input propertychange", function (event) {
    SJSJChange(".SJSJ")
    $(".registerform").valid()
});
$(".registerform").find("input[name='SJSJ2']").bind("input propertychange", function (event) {
    SJSJChange(".SJSJ")
    $(".registerform").valid()
});
$(".registerform").find("input[name='SJSJ3']").bind("input propertychange", function (event) {
    SJSJChange(".SJSJ")
    $(".registerform").valid()
});
$(".registerform").find("input[name='SJSJ4']").bind("input propertychange", function (event) {
    SJSJChange(".SJSJ")
    $(".registerform").valid()
});

function SJSJChange(key) {
    if ($(".registerform").find("input[name='SJSJ1']")[0].value || $(".registerform").find("input[name='SJSJ2']")[0].value ||
        $(".registerform").find("input[name='SJSJ3']")[0].value || $(".registerform").find("input[name='SJSJ4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//工业投资
$(".registerform").find("input[name='GYTZ1']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYTZChange(".GYTZ")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='GYTZ2']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYTZChange(".GYTZ")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='GYTZ3']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYTZChange(".GYTZ")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='GYTZ4']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        GYTZChange(".GYTZ")
        $(".registerform").valid()
    }
});

function GYTZChange(key) {
    if ($(".registerform").find("input[name='GYTZ1']")[0].value || $(".registerform").find("input[name='GYTZ2']")[0].value ||
        $(".registerform").find("input[name='GYTZ3']")[0].value || $(".registerform").find("input[name='GYTZ4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//主要污染物排放当量
$(".registerform").find("input[name='ZYWRWPF1']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        ZYWRWPFChange(".ZYWRWPF")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='ZYWRWPF2']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        ZYWRWPFChange(".ZYWRWPF")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='ZYWRWPF3']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        ZYWRWPFChange(".ZYWRWPF")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='ZYWRWPF4']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        ZYWRWPFChange(".ZYWRWPF")
        $(".registerform").valid()
    }
});

function ZYWRWPFChange(key) {
    if ($(".registerform").find("input[name='ZYWRWPF1']")[0].value || $(".registerform").find("input[name='ZYWRWPF2']")[0].value ||
        $(".registerform").find("input[name='ZYWRWPF3']")[0].value || $(".registerform").find("input[name='ZYWRWPF4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//能源总消费量
$(".registerform").find("input[name='NYZXHL1']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        NYZXHLChange(".NYZXHL")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='NYZXHL2']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        NYZXHLChange(".NYZXHL")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='NYZXHL3']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        NYZXHLChange(".NYZXHL")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='NYZXHL4']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        NYZXHLChange(".NYZXHL")
        $(".registerform").valid()
    }
});

function NYZXHLChange(key) {
    if ($(".registerform").find("input[name='NYZXHL1']")[0].value || $(".registerform").find("input[name='NYZXHL2']")[0].value ||
        $(".registerform").find("input[name='NYZXHL3']")[0].value || $(".registerform").find("input[name='NYZXHL4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

//年用水量
$(".registerform").find("input[name='YSL1']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        YSLChange(".YSL")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='YSL2']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        YSLChange(".YSL")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='YSL3']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        YSLChange(".YSL")
        $(".registerform").valid()
    }
});
$(".registerform").find("input[name='YSL4']").bind("input propertychange", function (event) {
    if (firstindustypeid && firstindustypeid == 0) {
        YSLChange(".YSL")
        $(".registerform").valid()
    }
});

function YSLChange(key) {
    if ($(".registerform").find("input[name='YSL1']")[0].value || $(".registerform").find("input[name='YSL2']")[0].value ||
        $(".registerform").find("input[name='YSL3']")[0].value || $(".registerform").find("input[name='YSL4']")[0].value) {
        changeRules(false, key)
    } else {
        changeRules(true, key)
    }
}

function changeRules(val, key) {
    let items = $(".registerform").find(key)
    if (val) {
        for (let i = 0; i < items.length; i++) {
            items.eq(i).rules("add", {required: true, messages: {required: "必填"}});
        }
    } else {
        for (let i = 0; i < items.length; i++) {
            items.eq(i).rules("remove");
        }
    }
}



/**
 * 根据关键字的数量变化动态执行方法
 * @param head 关键字的开头部门
 * @param func 执行方法
 * @param ignorNum 忽略的触发次数
 */
function dynamicsExcute(head, func,ignorNum,bolParm) {
    if(applicationRecord != undefined){
        //兼并重组信息
        var index = 0;// 动态变化标号
        for (var i = 0; i < applicationRecord.length; i++) {
            for (var key in applicationRecord[i]) {
                if (key.indexOf(head) > -1) {
                    var current = parseInt(key.substr(head.length, key.length));
                    index = index < current ? current : index;
                }
            }
        }
        var num = index - ignorNum;//动态变化的次数
        if (num > 0) {
            for (var i = 0; i<num; i++) {
                if(bolParm){
                    func(i+1);
                }else{
                    func();
                }

            }
        }
    }
}



// textarea自适应高度
function makeExpandingArea(el) {
    var timer = null
    var setStyle = function (el, auto) {
        if (auto) el.style.height = 'auto'
        el.style.height = el.scrollHeight + 'px'
    }
    var delayedResize = function (el) {
        if (timer) {
            clearTimeout(timer)
            timer = null
        }
        timer = setTimeout(function () {
            setStyle(el)
        }, 200)
    }
    if (el.addEventListener) {
        el.addEventListener('input', function () {
            setStyle(el, 1)
        }, false)
        setStyle(el)
    } else if (el.attachEvent) {
        el.attachEvent('onpropertychange', function () {
            setStyle(el)
        })
        setStyle(el)
    }
    $('#btnView').css('margin-top', document.getElementsByClassName('contentTable1')[0].clientHeight + 40 + 'px')
}

//pdf文件打印
function dowlondPDFBeforeHandle(){
    //先处理隐藏的元素
    hideClass(true, '.hidePdf');
    downPdf();
    hideClass(false, '.hidePdf');
}

function hideClass(bol,cls){
    let items = $(".registerform").find(cls)
    for(let i = 0; i < items.length; i++) {
        if(bol == true) {
            items.eq(i).hide();
        }else {
            items.eq(i).show();
        }
    }
}
