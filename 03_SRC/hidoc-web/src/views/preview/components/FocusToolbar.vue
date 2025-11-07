<template>
    <!-- 悬浮打印按钮 -->
    <div class="print-button-container">
        <div style="margin: 8px;">
            <button class="print-button" @click="exportMd">
                <Document style="width: 20px; height: 20px; margin: 0px 8px 0px 8px" />
                <span class="print-text">导出 MD</span>
            </button>
        </div>
        <div style="margin: 8px;">
            <button class="print-button" @click="handlePrint">
                <Printer style="width: 20px; height: 20px; margin: 0px 8px 0px 8px" />
                <span class="print-text">打印</span>
            </button>
        </div>
    </div>
</template>

<script>
export default {
    methods: {
        // 导出 MD 功能
        exportMd() {
            this.$emit('focusExportMd', {data: 'data'});
        },
        // 处理打印功能
        handlePrint() {
            // 可以在这里添加打印前的处理逻辑
            window.print();
        }
    }
}
</script>

<style scoped>
/* 基础样式重置 */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    min-height: 100vh;
    padding: 2rem;
    font-family: 'Segoe UI', sans-serif;
    line-height: 1.6;
}

/* 悬浮打印按钮样式 */
.print-button-container {
    position: fixed;
    bottom: 2rem;
    right: 2rem;
    z-index: 9999;
}

.print-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background: #3498db;
    color: white;
    border: none;
    border-radius: 50px;
    cursor: pointer;
    padding: 0.8rem 1.2rem;
    box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
}

.print-icon {
    font-size: 1.2rem;
    margin-right: 0;
    transition: margin-right 0.3s ease;
}

.print-text {
    font-weight: 500;
    max-width: 0;
    opacity: 0;
    transition: max-width 0.3s ease, opacity 0.3s ease, margin-right 0.3s ease;
    white-space: nowrap;
}

/* 鼠标悬停效果 */
.print-button:hover {
    background: #2980b9;
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(52, 152, 219, 0.4);
}

.print-button:hover .print-icon {
    margin-right: 0.8rem;
}

.print-button:hover .print-text {
    max-width: 50px;
    opacity: 1;
    margin-right: 0.4rem;
}

/* 点击效果 */
.print-button:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(52, 152, 219, 0.3);
}

/* 打印样式 - 隐藏按钮 */
@media print {
    .print-button-container {
        display: none !important;
    }
}
</style>