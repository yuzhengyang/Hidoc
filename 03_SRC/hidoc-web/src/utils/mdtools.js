import { config } from '@/utils/config';

export function mdFormat(text) {
    console.log('mdtools 预览预处理');
    let newText = text;

    // ====== 处理图片链接 ======
    {
        let images = getImages(newText);
        for (let i = 0; i < images.length; i++) {
            newText = newText.replaceAll(images[i], psImages(images[i]));
        }
    }

    // ====== 处理站内文章链接 ======
    {
        let quotes = getIlinks(newText);
        for (let i = 0; i < quotes.length; i++) {
            newText = newText.replaceAll(quotes[i], psIlinks(quotes[i]));
        }
    }

    // ====== 处理超链接 ======

    return newText;
}
function getImages(str) {
    var reg = /!\[(.+?)]\(#hd.image->(.+?)\)/g;
    var list = [];
    var result = null;
    do {
        result = reg.exec(str);
        result && list.push(result[0]);
    } while (result);
    return list;
}
function psImages(str) {
    let imageUrl = config().imageServer + '';
    return str.replaceAll('(#hd.image->', '(' + imageUrl);
}

function getIlinks(str) {
    var reg = /\[(.+?)]\(#hd.ilink->(.+?)\)/g;
    var list = [];
    var result = null;
    do {
        result = reg.exec(str);
        result && list.push(result[0]);
    } while (result);
    return list;
}
function psIlinks(str) {
    let linkUrl = window.location.protocol + '//' + window.location.host + '/collected/';
    console.log(linkUrl);
    return str.replaceAll('(#hd.ilink->', '(' + linkUrl);
}
