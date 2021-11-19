<template>
    <el-container>
        <el-header>
            <el-row>
                <el-col :span="16">
                    <el-radio-group v-model="pagePosition" style="margin-bottom: 30px">
                        <el-radio-button label="preview">公开文集</el-radio-button>
                        <el-radio-button label="team">团队</el-radio-button>
                        <el-radio-button label="mydoc">我的</el-radio-button>
                        <el-radio-button label="javadoc">JavaDoc</el-radio-button>
                    </el-radio-group>
                </el-col>
                <el-col :span="8" style="text-align:right;">
                    <el-input v-model="input3" placeholder="搜索一下" class="input-with-select">
                        <template #prepend>
                            <el-select v-model="select" placeholder="Select" style="width: 110px">
                                <el-option label="全文" value="1"></el-option>
                                <el-option label="关键字" value="2"></el-option>
                            </el-select>
                        </template>
                        <template #append>
                            <el-button>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </el-button>
                        </template>
                    </el-input>
                </el-col>
            </el-row>
        </el-header>
        <el-main>
            <div v-if="pagePosition === 'preview'">
                <preview-page></preview-page>
            </div>
            <div v-if="pagePosition === 'team'">
                <el-icon color="#409EFC" class="no-inherit">
                    <Guide />准备中...
                </el-icon>
            </div>
            <div v-if="pagePosition === 'mydoc'">
                <el-icon color="#409EFC" class="no-inherit">
                    <Guide />准备中...
                </el-icon>
            </div>
            <div v-if="pagePosition === 'javadoc'">
                <javadoc-page></javadoc-page>
            </div>
        </el-main>
    </el-container>
</template>


<script>
import previewPage from '../preview';
import javadocPage from '../javadoc';
import { Search, Share, Guide } from '@element-plus/icons';
export default {
    data() {
        return {
            pagePosition: 'preview',
            select: '',
            input3: ''
        };
    },
    components: { previewPage, javadocPage, Search, Guide },
    methods: {}
};
</script>

<style>
</style>