import { ElMessageBox, ElMessage } from 'element-plus';

export function copy(text) {
    // text: 要复制的内容， callback: 回调
    var tag = document.createElement('input');
    tag.setAttribute('id', 'at___cp_input');
    tag.value = text;
    document.getElementsByTagName('body')[0].appendChild(tag);
    document.getElementById('at___cp_input').select();
    document.execCommand('copy');
    document.getElementById('at___cp_input').remove();
    ElMessage({
        message: '复制成功',
        type: 'success',
        duration: 1 * 1000
    });
}
