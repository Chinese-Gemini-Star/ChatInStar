<script setup>
import axios from 'axios'
import { useMainStore } from '@/stores/main';
import { useRouter } from 'vue-router';
import { jwtDecode } from 'jwt-decode';

const store = useMainStore();
const router = useRouter();

// 表单中填写的user对象
const user = store.user;

// 表单提交改为ajax请求
function login(e) {
    e.preventDefault();
    axios.post('/user/login',user)
        .then((response) => {
            if(response.data.status) {
                // 登录成功
                store.jwt = response.data.message; // 保存token
                user.name = jwtDecode(store.jwt).name; // 保存用户名
                user.color = jwtDecode(store.jwt).color; // 保存图标颜色
                router.push("/");
            } else {
                // 登录失败
                alert(response.data.message);
            }
        }).catch((error) => {
            alert("登录请求发送失败，"+error.message);
        })
}
</script>

<template>
   <main class="text-center m-lg-5 px-lg-5">
      <h1 class="mb-5">星光聊天室</h1>
       <form action="/user/login" method="post" @submit="login">
            <div class="mt-5 mb-4 row">
                <div class="col-3 text-nowrap">
                    <label for="id" class="form-label text-nowrap">用户ID:</label>
                </div>
                <div class="col-8">
                    <input required type="text" v-model="user.id" id="id" name="id" class="form-control">
                </div>
            </div>
            <div class="mb-3 row">
                <div class="col-3 text-nowrap">
                    <label for="password" class="form-label text-nowrap">密码:</label>
                </div>
                <div class="col-8">
                    <input required type="password" v-model="user.password" id="password" name="password" class="form-control">
                </div>
            </div>
            <p class="mt-5">没有账号？<RouterLink to="/register">点此注册</RouterLink></p>
            <div class="text-center mt-3">
                <button type="submit" class="btn btn-primary text-nowrap" style="width: 90%;">登录</button>
            </div>
        </form>
   </main>
</template>