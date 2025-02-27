<template>
    <div class="verification-container">
        <input v-for="(code, index) in verificationCodes" :key="index" v-model="verificationCodes[index]" @input="handleInput(index, $event)" @keydown="handleKeyDown(index, $event)" maxlength="1" class="verification-input" />
    </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { defineProps } from 'vue';

const props = defineProps({
    callback: {
        type: Function,
        required: true
    }
});

const verificationCodes = ref(['', '', '', '', '', '']);

const handleInput = (index, event) => {
    const value = event.target.value;
    verificationCodes.value[index] = value;

    // 自动跳到下一个输入框
    if (value && index < verificationCodes.value.length - 1) {
        const nextInput = event.target.nextElementSibling;
        if (nextInput) {
            nextTick(() => {
                nextInput.focus();
            });
        }
    }
    // console.log(`component inner totpcode is: ${verificationCodes.value.join('')}`);
    props.callback(verificationCodes.value.join(''));
};

const handleKeyDown = (index, event) => {
    // 处理删除操作
    if (event.key === 'Backspace' && !event.target.value && index > 0) {
        const prevInput = event.target.previousElementSibling;
        if (prevInput) {
            nextTick(() => {
                prevInput.focus();
            });
        }
    }
};
</script>

<style lang="scss" scoped>
.verification-container {
    display: flex;
}

.verification-input {
    width: 40px;
    height: 40px;
    margin-right: 10px;
    text-align: center;
    font-size: 18px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.verification-input:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 5px #007bff;
}
</style>
