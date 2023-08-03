import { register, login, logout } from '@/api/user';
import { getToken, setToken, removeToken, getRealName, setRealName, removeRealName, setAvatar, getAvatar, getVipLevel, setVipLevel, removeVipLevel, getRoles, setRoles, removeRoles, setEmail, getEmail } from '@/utils/auth';
import router from '@/router';

const state = {
    token: getToken(),
    realName: getRealName(),
    name: getRealName(),
    avatar: getAvatar(),
    introduction: '',
    roles: getRoles(),
    id: '',
    vipLevel: 0,
    email: getEmail()
};

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token;
    },
    SET_INTRODUCTION: (state, introduction) => {
        state.introduction = introduction;
    },
    SET_NAME: (state, name) => {
        state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
        state.avatar = avatar;
    },
    SET_ROLES: (state, roles) => {
        state.roles = roles;
    },
    SET_ID: (state, id) => {
        state.id = id;
    },
    SET_VIP_LEVEL: (state, vipLevel) => {
        state.vipLevel = vipLevel;
    },
    SET_REAL_NAME: (state, realName) => {
        state.realName = realName;
    },
    SET_EMAIL: (state, email) => {
        state.email = email;
    }
};

const actions = {
    register({ commit }, userInfo) {
        const { avatar, username, email, password, realname, uid, authCode } = userInfo;
        return new Promise((resolve, reject) => {
            register({ avatar: avatar, username: username.trim(), email: email.trim(), password: password, realname: realname, uid: uid, authCode: authCode })
                .then(response => {
                    // const { data } = response;
                    commit('SET_TOKEN', response.token);
                    setToken(response.token);
                    resolve();
                })
                .catch(error => {
                    reject(error);
                });
        });
    },
    login({ commit }, userInfo) {
        const { username, password } = userInfo;
        return new Promise((resolve, reject) => {
            login({ username: username.trim(), password: password })
                .then(response => {
                    // const { data } = response;
                    commit('SET_TOKEN', response.token);
                    commit('SET_NAME', response.meta.sysUser.realName);
                    commit('SET_ROLES', response.meta.sysUser.roles);
                    commit('SET_ID', response.meta.sysUser.id);
                    commit('SET_VIP_LEVEL', response.meta.sysUser.vipLevel);
                    commit('SET_AVATAR', response.meta.sysUser.avatar); // 有效
                    commit('SET_REAL_NAME', response.meta.sysUser.realName);
                    setToken(response.token);
                    setRoles(response.meta.sysUser.roles);
                    setRealName(response.meta.sysUser.realName);
                    setAvatar(response.meta.sysUser.avatar);

                    commit('SET_EMAIL', response.meta.sysUser.email);
                    setEmail(response.meta.sysUser.email);

                    resolve();
                })
                .catch(error => {
                    reject(error);
                });
        });
    },

    // // get user info
    // getInfo({ commit, state }) {
    //     debugger;
    //     return new Promise((resolve, reject) => {
    //         getInfo(state.token)
    //             .then(response => {
    //                 debugger;

    //                 const { data } = response;

    //                 if (!data) {
    //                     reject('Verification failed, please Login again.');
    //                 }

    //                 const { roles, name, avatar, introduction } = data;

    //                 // roles must be a non-empty array
    //                 if (!roles || roles.length <= 0) {
    //                     reject('getInfo: roles must be a non-null array!');
    //                 }

    //                 commit('SET_ROLES', roles);
    //                 commit('SET_NAME', name);
    //                 commit('SET_AVATAR', avatar);
    //                 commit('SET_INTRODUCTION', introduction);
    //                 resolve(data);
    //             })
    //             .catch(error => {
    //                 reject(error);
    //             });
    //     });
    // },

    // user logout
    logout({ commit, state, dispatch }) {
        return new Promise((resolve, reject) => {
            logout(state.token)
                .then(() => {
                    commit('SET_TOKEN', '');
                    commit('SET_ROLES', []);
                    commit('SET_VIP_LEVEL', 0);
                    removeToken();
                    removeRoles();
                    removeRealName();
                    // resetRouter();

                    // reset visited views and cached views
                    // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
                    dispatch('tagsView/delAllViews', null, { root: true });

                    resolve();
                })
                .catch(error => {
                    reject(error);
                });
        });
    },

    // remove token
    resetToken({ commit }) {
        return new Promise(resolve => {
            commit('SET_TOKEN', '');
            commit('SET_ROLES', []);
            removeToken();
            resolve();
        });
    }

    // // dynamically modify permissions
    // async changeRoles({ commit, dispatch }, role) {
    //     const token = role + '-token';
    //     debugger;
    //     commit('SET_TOKEN', token);
    //     setToken(token);

    //     const { roles } = await dispatch('getInfo');

    //     // resetRouter();

    //     // generate accessible routes map based on roles
    //     const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true });
    //     // dynamically add accessible routes
    //     router.addRoutes(accessRoutes);

    //     // reset visited views and cached views
    //     dispatch('tagsView/delAllViews', null, { root: true });
    // }
};

export default {
    namespaced: true,
    state,
    mutations,
    actions
};
