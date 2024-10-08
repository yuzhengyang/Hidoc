import { config } from '@/utils/config';
import { nextTick } from 'vue';
import _, { includes } from 'lodash';

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

    // ====== 处理站内连接地址 ======
    {
        let quotes = getUrls(newText);
        for (let i = 0; i < quotes.length; i++) {
            newText = newText.replaceAll(quotes[i], psUrls(quotes[i]));
        }
    }

    // ====== 处理uname资源地址 ======
    {
        let quotes = getUnames(newText);
        for (let i = 0; i < quotes.length; i++) {
            newText = newText.replaceAll(quotes[i], psUnames(quotes[i]));
        }
    }

    // ====== 处理超链接 ======

    // ====== 处理章节序号 ======
    {
        newText = setChapterNumber(newText);
    }

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

function getUrls(str) {
    var reg = /#hd.url:\/\//g;
    var list = [];
    var result = null;
    do {
        result = reg.exec(str);
        result && list.push(result[0]);
    } while (result);
    return list;
}
function psUrls(str) {
    let baseUrl = config().baseServer + '';
    return str.replaceAll('#hd.url://', baseUrl);
}

function getUnames(str) {
    var reg = /#hd.uname:\/\//g;
    var list = [];
    var result = null;
    do {
        result = reg.exec(str);
        result && list.push(result[0]);
    } while (result);
    return list;
}
function psUnames(str) {
    let baseUrl = config().imageServer + '';
    return str.replaceAll('#hd.uname://', baseUrl);
}
function setChapterNumber(text, defaultNumber) {
    if (text.indexOf('# @ ') < 0) return text;

    let number = [0, 0, 0, 0, 0, 0];
    if(defaultNumber){
        if(defaultNumber[0]) number[0] = defaultNumber[0];
        if(defaultNumber[1]) number[1] = defaultNumber[1];
        if(defaultNumber[2]) number[2] = defaultNumber[2];
        if(defaultNumber[3]) number[3] = defaultNumber[3];
        if(defaultNumber[4]) number[4] = defaultNumber[4];
        if(defaultNumber[5]) number[5] = defaultNumber[5];
    }
    
    let array = _.split(text, '\n');
    for (let i = 0; i < array.length; i++) {
        if (array[i].startsWith('# @ ')) {
            array[i] = array[i].replace('# @ ', `# ${++number[0]} `);
            number = [number[0], 0, 0, 0, 0, 0];
        }
        if (array[i].startsWith('## @ ')) {
            if (number[0] === 0) ++number[0];
            array[i] = array[i].replace('## @ ', `## ${number[0]}.${++number[1]} `);
            number = [number[0], number[1], 0, 0, 0, 0];
        }
        if (array[i].startsWith('### @ ')) {
            if (number[0] === 0) ++number[0];
            if (number[1] === 0) ++number[1];
            array[i] = array[i].replace('### @ ', `### ${number[0]}.${number[1]}.${++number[2]} `);
            number = [number[0], number[1], number[2], 0, 0, 0];
        }
        if (array[i].startsWith('#### @ ')) {
            if (number[0] === 0) ++number[0];
            if (number[1] === 0) ++number[1];
            if (number[2] === 0) ++number[2];
            array[i] = array[i].replace('#### @ ', `#### ${number[0]}.${number[1]}.${number[2]}.${++number[3]} `);
            number = [number[0], number[1], number[2], number[3], 0, 0];
        }
        if (array[i].startsWith('##### @ ')) {
            if (number[0] === 0) ++number[0];
            if (number[1] === 0) ++number[1];
            if (number[2] === 0) ++number[2];
            if (number[3] === 0) ++number[3];
            array[i] = array[i].replace('##### @ ', `##### ${number[0]}.${number[1]}.${number[2]}.${number[3]}.${++number[4]} `);
            number = [number[0], number[1], number[2], number[3], number[4], 0];
        }
        if (array[i].startsWith('###### @ ')) {
            if (number[0] === 0) ++number[0];
            if (number[1] === 0) ++number[1];
            if (number[2] === 0) ++number[2];
            if (number[3] === 0) ++number[3];
            if (number[4] === 0) ++number[4];
            array[i] = array[i].replace('###### @ ', `###### ${number[0]}.${number[1]}.${number[2]}.${number[3]}.${number[4]}.${++number[5]} `);
            number = [number[0], number[1], number[2], number[3], number[4], number[5]];
        }
    }

    return _.join(array, '\n');
}
