// export function avatarImage(avatar) {
//     if (avatar && avatar.length > 0) {
//         return require('../assets/avatar/' + avatar.replace('$system$', ''));
//     } else {

//     }
// }

import { config } from '@/utils/config';

export function avatarImage(avatar) {
    if (avatar && avatar.length > 0) {
        if (avatar.substring(0, 1) == '$') {
            return require('../assets/avatar/' + avatar.replace('$system$', ''));
        } else {
            return config().imageServer + avatar;
        }
    } else {
        return require('../assets/avatar/35.jpg'); // 默认葫芦娃
    }
}
