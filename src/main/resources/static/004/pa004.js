let textarea_1 = document.getElementById('JGZTQKJYWKZQK')
let textarea_2 = document.getElementById('ZYFWLY')
let textarea_3 = document.getElementById('JGTDZC')
let textarea_4 = document.getElementById('YGJJSTDQK')
let textarea_5 = document.getElementById('ZYYW')
makeExpandingArea(textarea_1)
makeExpandingArea(textarea_2)
makeExpandingArea(textarea_3)
makeExpandingArea(textarea_4)
makeExpandingArea(textarea_5);


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

