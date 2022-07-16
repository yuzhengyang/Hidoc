export function avatarImage(avatar) {
    if (avatar && avatar.length > 0) {
        return require('../assets/avatar/' + avatar.replace('$system$', ''));
    } else {
        return require('../assets/avatar/35.jpg'); // 默认葫芦娃
    }
}
