import { defineStore } from "pinia";
import { reactive, ref } from "vue";

import axios from 'axios';

export const useMainStore = defineStore('main', () => {
    // 用户对象
    const user = reactive({
        id: "",
        password: "",
        name: "",
        color: ""
    })

    // 用户认证信息
    const jwt = ref("")
    const refresh = async function () {
        let isFresh = false;
        await axios.post('/user/refreshJWT', jwt.value)
            .then(function (response) {
                if (response.data.status) {
                    // JWT更新成功
                    jwt.value = response.data.message;
                    isFresh = true;
                }
            });
        return isFresh;
    }

    // ws客户端
    const ws = ref(null);

    return { user, jwt, refresh, ws }
}, { persist: false })