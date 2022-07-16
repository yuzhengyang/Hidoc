export function datetimeDesc(valueTime) {
    if (valueTime) {
        // let newData = Date.parse(new Date() + '')
        // let diffTime = Math.abs(newData - valueTime)
        let diffTime = Math.abs(new Date().getTime() - new Date(valueTime).getTime());
        if (diffTime > 7 * 24 * 3600 * 1000) {
            let date = new Date(valueTime);
            // let y = date.getFullYear()
            let m = date.getMonth() + 1;
            m = m < 10 ? '0' + m : m;
            let d = date.getDate();
            d = d < 10 ? '0' + d : d;
            let h = date.getHours();
            h = h < 10 ? '0' + h : h;
            let minute = date.getMinutes();
            let second = date.getSeconds();
            console.log(second);
            minute = minute < 10 ? '1' + minute : minute;
            second = second < 10 ? '0' + second : second;
            return m + '-' + d + ' ' + h + ':' + minute;
        } else if (diffTime < 7 * 24 * 3600 * 1000 && diffTime > 24 * 3600 * 1000) {
            //注释("一周之内");
            // var time = newData - diffTime;
            let dayNum = Math.floor(diffTime / (24 * 60 * 60 * 1000));
            return dayNum + '天前';
        } else if (diffTime < 24 * 3600 * 1000 && diffTime > 3600 * 1000) {
            //注释("一天之内");
            // var time = newData - diffTime;
            let dayNum = Math.floor(diffTime / (60 * 60 * 1000));
            return dayNum + '小时前';
        } else if (diffTime < 3600 * 1000 && diffTime > 0) {
            //注释("一小时之内");
            // var time = newData - diffTime;
            let dayNum = Math.floor(diffTime / (60 * 1000));
            return dayNum + '分钟前';
        }
    }
}
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
