import { config } from '@/utils/config';
import { nextTick } from 'vue';
import _, { includes } from 'lodash';
import request from '@/utils/request';
import { convertFileSize } from '@/utils/calc';

export function mdFormat(text, extData) {
    console.log('mdtools 预览预处理');
    let newText = text;

    // ====== 处理图片链接 ======
    {
        let images = getImages(newText);
        for (let i = 0; i < images.length; i++) {
            newText = newText.replaceAll(images[i], psImages(images[i]));
        }
    }
    // ====== 处理文件卡片 ======
    // 这里存在一个问题，获取文件内容时，需要请求接口，但是这里是一个同步方法，无法获取到接口返回的数据
    // 所以这里需要进行改造，将文件卡片的内容提前获取到，然后再进行处理
    {
        let files = getFileCards(newText);
        for (let i = 0; i < files.length; i++) {
            newText = newText.replaceAll(files[i], psFileCards(files[i], extData));
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
function getFileCards(str) {
    var reg = /<div(.+?)data-hd-file="(.+?)">(.+?)<\/div>/g;
    var list = [];
    var result = null;
    do {
        result = reg.exec(str);
        result && list.push(result[0]);
    } while (result);
    return list;
}
function psFileCards(str, extData) {
    // 在文件卡片中，有标签包括id信息（data-hd-file），这里根据id获取详细数据用于展示
    // 例如：data-hd-file="5f7c7b1b9f4a5c0001e6b4d3"
    // 通过id获取文件信息，然后展示在卡片中
    const idReg = /data-hd-file="(.+?)"/g;
    let fileIdMatch = idReg.exec(str);
    let fileId = fileIdMatch ? fileIdMatch[1] : null;
    const nameReg = /<div.*?>(.*?)<\/div>/;
    let nameMatch = nameReg.exec(str);
    let fileName = nameMatch ? nameMatch[1] : '保存后预览';
    let fileHtml = '';
    let downloadCount = 0;
    let fileSize = 0;
    let createTime = '';
    let uname = '';
    let ext = '';
    if (fileId && extData && extData.docFileMap) {
        if (extData.docFileMap[fileId] && extData.docFileMap[fileId].fileCursor) {
            fileName = extData.docFileMap[fileId].fileCursor.fileName;
            downloadCount = extData.docFileMap[fileId].fileCursor.downloadCount;
            createTime = extData.docFileMap[fileId].fileCursor.createTime;
            uname = extData.docFileMap[fileId].fileCursor.uname;
        }
        if (extData.docFileMap[fileId] && extData.docFileMap[fileId].file) {
            fileSize = extData.docFileMap[fileId].file.size;
            ext = extData.docFileMap[fileId].file.ext;
        }
    }

    let fileIcon = require('../assets/filetype/other.png');
    if (ext === 'doc' || ext === 'docx') fileIcon = require('../assets/filetype/doc.png');
    if (ext === 'xls' || ext === 'xlsx') fileIcon = require('../assets/filetype/xls.png');
    if (ext === 'ppt' || ext === 'pptx') fileIcon = require('../assets/filetype/ppt.png');
    if (ext === 'pdf') fileIcon = require('../assets/filetype/pdf.png');
    if (ext === 'txt') fileIcon = require('../assets/filetype/txt.png');
    if (ext === 'zip' || ext === 'rar' || ext === '7z') fileIcon = require('../assets/filetype/zip.png');
    if (ext === 'jpg' || ext === 'jpeg' || ext === 'png' || ext === 'gif') fileIcon = require('../assets/filetype/img.png');
    if (ext === 'mp3' || ext === 'wav' || ext === 'flac' || ext === 'ape') fileIcon = require('../assets/filetype/mp3.png');
    if (ext === 'mp4' || ext === 'avi' || ext === 'rmvb' || ext === 'mkv') fileIcon = require('../assets/filetype/mp4.png');
    if (ext === 'exe' || ext === 'msi') fileIcon = require('../assets/filetype/exe.png');
    if (ext === 'html' || ext === 'htm') fileIcon = require('../assets/filetype/html.png');

    let borderStyle = '';//'border: 1px solid gray;';
    let backgroundStyle = `background-color: rgba(197, 197, 197, 0.3); backdrop-filter: blur(5px); border-radius: 10px;box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);`;
    let downloadHerf = `<span style="font-size: 12px;">保存后预览</span>`;
    if (uname && uname.length > 0) downloadHerf = `<a href="${config().baseServer + 'f/d/u/' + uname}" style="font-size: 12px;" download>立即下载</a>`;

    let html = `<div class="no-select" style="${backgroundStyle} width: 370px; height: 120px; padding: 15px; margin: 40px 0px;">
    	<div style="${borderStyle} width: 60px; height: 60px; float: left">
    		<img src="${fileIcon}" style="width: 50px; height: 50px; border-radius: 10px;">
    	</div>
    	<div style="${borderStyle} width: 300px; height: 28px; float: left; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
        ${fileName}
    	</div>
    	<div style="${borderStyle} width: 120px; height: 28px; float: left">
    		<span style="font-size: 12px;">大小: ${convertFileSize(fileSize)}</span>
    	</div>
    	<div style="${borderStyle} width: 120px; height: 28px; float: left">
            <span style="font-size: 12px;">${downloadCount} 次下载</span>
    	</div>
    	<div style="${borderStyle} width: 260px; height: 28px; float: left">
    		<span style="font-size: 12px;">上传于 ${createTime}</span>
    	</div>
    	<div style="${borderStyle} width: 60px; height: 28px; float: left;">
    	</div>
    	<div style="${borderStyle} width: 80px; height: 28px; float: left;">
    		${downloadHerf}
    	</div>
    </div>
    `;
    return html;
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
    if (defaultNumber) {
        if (defaultNumber[0]) number[0] = defaultNumber[0];
        if (defaultNumber[1]) number[1] = defaultNumber[1];
        if (defaultNumber[2]) number[2] = defaultNumber[2];
        if (defaultNumber[3]) number[3] = defaultNumber[3];
        if (defaultNumber[4]) number[4] = defaultNumber[4];
        if (defaultNumber[5]) number[5] = defaultNumber[5];
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
