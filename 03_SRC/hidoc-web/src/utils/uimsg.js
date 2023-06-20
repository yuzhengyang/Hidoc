// storage 消息通知刷新页面信息
//
export function uiRefresh(page, element, id) {
    localStorage.setItem('ui-msg', JSON.stringify({ page: page, element: element, id: id, date: Date.now() }));
}
