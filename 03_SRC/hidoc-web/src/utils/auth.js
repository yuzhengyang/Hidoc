import Cookies from 'js-cookie';

const TokenKey = 'Admin-Token';
const RealNameKey = 'User-RealName';
const VipLevel = 'User-VipLevel';
const Roles = 'User-Roles';
const Avatar = 'User-Avatar';
const EmailKey = 'User-Email';

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

export function getEmail() {
    return Cookies.get(EmailKey);
}

export function setEmail(email) {
    console.log('email: ' + email);
    return Cookies.set(EmailKey, email);
}

export function setRealName(name) {
    console.log('name: ' + name);
    return Cookies.set(RealNameKey, name);
}

export function setAvatar(avatar) {
    return Cookies.set(Avatar, avatar);
}

export function getAvatar() {
    return Cookies.get(Avatar);
}

export function removeRealName() {
    return Cookies.remove(RealNameKey);
}

export function getVipLevel() {
    return Cookies.get(VipLevel);
}

export function setVipLevel(level) {
    console.log('level: ' + level);
    return Cookies.set(VipLevel, level);
}

export function removeVipLevel() {
    return Cookies.remove(VipLevel);
}

export function getRoles() {
    return Cookies.get(Roles);
}

export function setRoles(roles) {
    console.log('roles: ' + roles);
    return Cookies.set(Roles, roles);
}

export function removeRoles() {
    return Cookies.remove(Roles);
}
