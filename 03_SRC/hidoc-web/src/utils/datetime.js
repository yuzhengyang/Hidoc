
import * as moment from 'moment';

export function datetimeFormat(valueTime, fmt) {
    var o = {
        'M+': valueTime.getMonth() + 1, //月份
        'd+': valueTime.getDate(), //日
        'H+': valueTime.getHours(), //小时
        'm+': valueTime.getMinutes(), //分
        's+': valueTime.getSeconds(), //秒
        'q+': Math.floor((valueTime.getMonth() + 3) / 3), //季度
        S: valueTime.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (valueTime.getFullYear() + '').substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));
    return fmt;
}
export function datetimeDiff(valueTime) {
    if (valueTime == undefined || valueTime == '') return '';
    let result = moment().diff(moment(valueTime), 'days') + ' 天前';
    return result;
}
