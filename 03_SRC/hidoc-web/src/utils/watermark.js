// warterMark.js
'use strict';

let watermark = {};

let setWatermark = (str, str1) => {
    let id = '1.23452384164.123412415';

    if (document.getElementById(id) !== null) {
        document.body.removeChild(document.getElementById(id));
    }

    let can = document.createElement('canvas'); // 创建一个画布
    can.width = 350; // 设置宽度
    can.height = 300; // 高度

    let cans = can.getContext('2d');
    cans.rotate((-15 * Math.PI) / 180); // 水印旋转角度    0 水平
    cans.font = '14px Vedana'; // 字体大小
    cans.fillStyle = '#222222'; // 水印的颜色 F7F7F7 919191
    cans.textAlign = 'left'; // 设置文本内容的当前对齐方式
    cans.textBaseline = 'Middle'; // 设置在绘制文本时使用的当前文本基线
    cans.globalAlpha = 0.1; // 透明度
    cans.fillText(str, can.width / 3, can.height / 2); // 在画布上绘制填色的文本（输出的文本，开始绘制文本的X坐标位置，开始绘制文本的Y坐标位置）
    cans.fillText(str1, can.width / 3, can.height / 2.5); // 根据需求可添加多行水印，在方法中添加str1

    let div = document.createElement('div');
    div.id = id;
    div.style.pointerEvents = 'none';
    div.style.top = '70px';
    div.style.left = '0px';
    div.style.position = 'fixed';
    div.style.zIndex = '100000';
    div.style.width = document.documentElement.clientWidth - 30 + 'px';
    div.style.height = document.documentElement.clientHeight + 'px';
    div.style.background = 'url(' + can.toDataURL('image/png') + ') left top repeat';
    document.body.appendChild(div);
    return id;
};

// 该方法只允许调用一次
watermark.set = (str, str1) => {
    let id = setWatermark(str, str1);
    setInterval(() => {
        if (document.getElementById(id) === null) {
            id = setWatermark(str, str1);
        }
    }, 500);
    window.onresize = () => {
        setWatermark(str, str1);
    };
};

export default watermark;
