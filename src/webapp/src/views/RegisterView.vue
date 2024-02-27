<script setup>
import axios from 'axios';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

// 表单中填写的user对象
const user = reactive({
    id: "",
    password: "",
    name: "",
    color:"#000000"
})

const password2 = ref("")

const router = useRouter();

// 表单提交改为ajax请求
function register(e) {
    e.preventDefault();

    if(password2.value != user.password) {
        alert("两次密码不一致!");
        return;
    }

    axios.post('/user/register',user)
        .then((response) => {
            if(response.data.status) {
                // 注册成功
                router.push("/login");
            } else {
                // 注册失败
                alert(response.data.message);
            }
        }).catch((error) => {
            alert("注册请求发送失败，"+error.message);
        })
}
</script>

<template>
   <main class="text-center m-lg-5 px-lg-5">
      <h1 class="mb-5">注册用户</h1>
       <form action="/user/register" method="post" @submit="register">
            <div class="mt-5 mb-4 row">
                <div class="col-3 text-nowrap">
                    <label for="id" class="form-label text-nowrap">用户ID:</label>
                </div>
                <div class="col-8">
                    <input required type="text" v-model="user.id" id="id" name="id" class="form-control">
                </div>
            </div>
            <div class="mb-4 row">
                <div class="col-3 text-nowrap">
                    <label for="password" class="form-label text-nowrap">密码:</label>
                </div>
                <div class="col-8">
                    <input required type="password" v-model="user.password" id="password" name="password" class="form-control">
                </div>
            </div>
            <div class="mb-4 row">
                <div class="col-3 text-nowrap">
                    <label for="password2" class="form-label text-nowrap">确认密码:</label>
                </div>
                <div class="col-8">
                    <input required type="password" v-model="password2" id="password2" name="password2" class="form-control">
                </div>
            </div>
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
            
            <div class="text-center mt-5">
                <button type="submit" class="btn btn-primary text-nowrap" style="width: 90%;">注册</button>
            </div>
        </form>
   </main>
</template>