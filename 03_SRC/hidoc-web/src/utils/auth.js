import Cookies from 'js-cookie';

const TokenKey = 'Admin-Token';
const RealNameKey = 'User-RealName';

export function getToken() {
    return Cookies.get(TokenKey);
}

export function setToken(token) {
    console.log('token: ' + token);
    return Cookies.set(TokenKey, token);
}

export function removeToken() {
    return Cookies.remove(TokenKey);
}

export function getRealName() {
    return Cookies.get(RealNameKey);
}

export function setRealName(name) {
    console.log('name: ' + name);
    return Cookies.set(RealNameKey, name);
}

export function removeRealName() {
    return Cookies.remove(RealNameKey);
}
