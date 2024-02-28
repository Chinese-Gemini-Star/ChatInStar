<script setup>
import router from '@/router';
import { useMainStore } from '@/stores/main';
import { ref } from 'vue';

const store = useMainStore();

// 判断当前颜色是否是浅色
const isLightColor = (color) => {
   const R = parseInt(color.substr(1,2),16);
   const G = parseInt(color.substr(3,2),16);
   const B = parseInt(color.substr(5,2),16);
   // 计算灰度
   const Y = R * 0.30 + G * 0.59 + B * 0.11;
   return Y > 128;
}

let isShowMyDetail = ref(false);

const editMyDetail = () => {
    router.push("/change");
}

</script>

<template>
    <div tabindex="-1" @focus="isShowMyDetail = !isShowMyDetail" @blur="isShowMyDetail = !isShowMyDetail">
        <div class="text-end">
            <span id="myIcon" class="text-center">{{ store.user.id.charAt(0) }}</span>
        </div>
        <div id="myDetail" v-show="isShowMyDetail">
            <p>
                用户ID: {{ store.user.id }}
            </p>
            <hr>
            <p>
                图标颜色: <span :style="{color:store.user.color}">{{ store.user.color }}</span>
            </p>
            <hr>
            <p>
                昵称: {{ store.user.name }}
            </p>
            <hr>
            <p>
                密码: **********
            </p>
            <hr>
            <p class="text-center" @click="editMyDetail"><i class="fa fa-pencil-square-o"></i> 修改个人信息</p>
        </div>
    </div>
</template>

<style scoped>
#myIcon {
    display: inline-block;
    width: 50px;
    height: 50px;
    line-height: 50px;
    text-align: center;
    font-size: 25px;
    border-radius: 50%;
    background-color: v-bind("store.user.color");
    color: v-bind("isLightColor(store.user.color) ? 'black' : 'white'");
}

#myDetail {
    background-color:v-bind("isLightColor(store.user.color) ? 'black' : 'white'");
    color: v-bind("isLightColor(store.user.color) ? 'white' : 'black'");
    margin: 3px 5px 0px;
    padding: 10px 15px 3px;
    border-radius: 5%;
}

#myDetail > button {
    border: 0px;
    background-color: transparent;
    margin: 0px 0px 10px;
    color: v-bind("isLightColor(store.user.color) ? 'white' : 'black'");
}

</style>