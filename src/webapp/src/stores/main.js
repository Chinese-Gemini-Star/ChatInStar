import { defineStore } from "pinia";
import { reactive, ref } from "vue";

export const useMainStore = defineStore('main', () => {
    // 用户对象
    const user = reactive({
        id: "",
        password: "",
        name: "",
        color: ""
    })

    const jwt = ref("")

    return { user, jwt }
}, { persist: false })