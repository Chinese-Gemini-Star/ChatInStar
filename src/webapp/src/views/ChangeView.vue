<script setup>
import { useMainStore } from '@/stores/main';
import axios from 'axios';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const store = useMainStore();

// 表单中填写的user对象
const user = reactive({
    id: store.user.id,
    password: store.user.password,
    name: store.user.name,
    color:store.user.color,
    token:store.jwt
})

const isRevisePassword = ref(false);
const password2 = ref("")

const router = useRouter();

// 表单提交改为ajax请求
function change(e) {
    e.preventDefault();

    if(isRevisePassword.value && password2.value != user.password) {
        alert("两次密码不一致!");
        return;
    }   

    user.token = store.jwt; // 更新jwt认证信息
    axios.post('/user/change',user)
        .then((response) => {
            if(response.data.status) {
                // 修改成功
                store.user.password=user.password;
                router.push("/login");
            } else {
                // 修改失败
                alert(response.data.message);
            }
        }).catch((error) => {
            alert("修改请求发送失败，"+error.message);
        })
}
</script>

<template>
   <main class="text-center m-lg-5 px-lg-5">
      <h1 class="mb-5">修改个人信息</h1>
       <form action="/user/register" method="post" @submit="change">
            <div class="mb-3 row">
                <div class="col-3 text-nowrap">
                    <lable for="color">图标颜色：</lable>
                </div>
                <div class="col-1">
                    <input required type="color" v-model="user.color" id="color" name="color" class="form-control form-control-color">
                </div>

                <div class="col-2 text-nowrap">
                    <label for="name" class="form-label text-nowrap">昵称:</label>
                </div>
                <div class="col-5">
                    <input required type="text" v-model="user.name" id="name" name="name" class="form-control">
                </div>
            </div>
            <div class="mb-4 row">
                <div class="col-3 text-nowrap">
                    <label for="password" class="form-label text-nowrap">密码:</label>
                </div>
                <div class="col-8">
                    <input required type="password" v-model="user.password" id="password" name="password" class="form-control" @change="isRevisePassword = user.password != store.user.password">
                </div>
            </div>
            <div class="mb-4 row" v-show="isRevisePassword">
                <div class="col-3 text-nowrap">
                    <label for="password2" class="form-label text-nowrap">确认密码:</label>
                </div>
                <div class="col-8">
                    <input :required="isRevisePassword" type="password" v-model="password2" id="password2" name="password2" class="form-control">
                </div>
            </div>

            <div class="text-center mt-5">
                <button type="submit" class="btn btn-primary text-nowrap" style="width: 90%;">修改</button>
            </div>
        </form>
   </main>
</template>