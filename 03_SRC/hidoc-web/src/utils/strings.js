import { ElMessageBox, ElMessage } from 'element-plus';

export function length(text) {
    var lens = 0; // 中文算2个字
    for (let i = 0; i < text.length; i++) {
       if ((text.charCodeAt(i) >= 0) && (text.charCodeAt(i) <= 255))
           { lens = lens + 1;}
       else{ lens = lens + 2; } 
    }
    return lens;
}
