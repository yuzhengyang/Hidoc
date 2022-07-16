<template>
    <el-row style="margin:10px;">
        <el-col :span="24">
            <el-button type="primary" @click="exportFileList">导出文件列表</el-button>
            <el-button type="success" @click="loadBigObject">大对象加载</el-button>
            <el-button type="info" @click="loadIndexedDbData">读取大对象加载在IndexedDb中的内容</el-button>
            <el-button type="warning">Warning</el-button>
            <el-button type="danger">Danger</el-button>
            <el-button>Default</el-button>
        </el-col>
    </el-row>

    <el-row style="margin:10px;">
        <el-col :span="24">
            <el-button plain>Plain</el-button>
            <el-button type="primary" plain>Primary</el-button>
            <el-button type="success" plain>Success</el-button>
            <el-button type="info" plain>Info</el-button>
            <el-button type="warning" plain>Warning</el-button>
            <el-button type="danger" plain>Danger</el-button>
        </el-col>
    </el-row>

    <el-row style="margin:10px;">
        <el-col :span="24">
            <el-button round>Round</el-button>
            <el-button type="primary" round>Primary</el-button>
            <el-button type="success" round>Success</el-button>
            <el-button type="info" round>Info</el-button>
            <el-button type="warning" round>Warning</el-button>
            <el-button type="danger" round>Danger</el-button>
        </el-col>
    </el-row>

    <el-row style="margin:10px;">
        <el-col :span="24">
            <el-button icon="el-icon-search" circle></el-button>
            <el-button type="primary" icon="el-icon-edit" circle></el-button>
            <el-button type="success" icon="el-icon-check" circle></el-button>
            <el-button type="info" icon="el-icon-message" circle></el-button>
            <el-button type="warning" icon="el-icon-star-off" circle></el-button>
            <el-button type="danger" icon="el-icon-delete" circle></el-button>
        </el-col>

    </el-row>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            collectedCount: 0,
            docCount: 0,
            cursorCount: 0,
            readCount: 0
        };
    },
    mounted() {},
    methods: {
        exportFileList() {
            request({
                url: '/test/export',
                method: 'get'
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                }
            });
        },
        loadBigObject() {
            request({
                url: '/test/loadbig',
                method: 'get'
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);

                    var request = window.indexedDB.open('temp', 1);

                    request.onerror = function (event) {
                        console.log('数据库打开报错');
                    };

                    var db;

                    request.onsuccess = function (event) {
                        db = request.result;
                        console.log('数据库打开成功');

                        for (let i = 0; i < res.data.length; i++) {
                            request = db.transaction(['SysAccessLog'], 'readwrite').objectStore('SysAccessLog').add(res.data[i]);
                        }
                    };

                    request.onupgradeneeded = function (event) {
                        db = event.target.result;
                        var objectStore;
                        if (!db.objectStoreNames.contains('SysAccessLog')) {
                            objectStore = db.createObjectStore('SysAccessLog', { keyPath: 'id' });
                            objectStore.createIndex('myindex', ['ip', 'method'], { unique: false });
                        }
                        console.log('数据表创建成功');
                    };
                }
            });
        },
        loadIndexedDbData() {
            var request = window.indexedDB.open('temp', 1);
            var db;

            // 打开错误回调
            request.onerror = function (event) {
                console.log('数据库打开报错');
            };

            // 打开成功回调
            request.onsuccess = function (event) {
                db = request.result;
                console.log('数据库打开成功');

                // 打开成功后，读取数据并打印到控制台
                var transaction = db.transaction(['SysAccessLog']);
                var objectStore = transaction.objectStore('SysAccessLog');
                
                // 根据主键读取数据（单条）
                // var getRequest = objectStore.get('97829451131518985');

                // 根据索引查询数据（多条）
                var index = objectStore.index('myindex');
                var getRequest = index.openCursor(IDBKeyRange.only(['10.16.12.106', 'GET']));

                // 根据索引查询数据（单条，第一次匹配到的数据）
                // var getRequest = index.get(IDBKeyRange.only(['', '']));

                // 查询错误回调
                getRequest.onerror = function (event) {
                    console.log('事务失败');
                };

                // 查询成功回调
                getRequest.onsuccess = function (event) {
                    // 此处是游标的循环处理
                    var res = event.target.result;
                    if (res) {
                        var key = res.key;
                        var value = res.value;
                        console.log(key + ':' + JSON.stringify(value));
                        res.continue();
                    }
                };
            };

            // 数据库升级回调
            request.onupgradeneeded = function (event) {
                db = event.target.result;
            };
        }
    }
};
</script>

<style>
</style>