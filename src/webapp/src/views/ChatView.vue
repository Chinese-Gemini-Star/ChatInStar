<script setup>
import router from '@/router';
import { useMainStore } from '@/stores/main';
import { reactive, ref } from 'vue';

const store = useMainStore();

// 当前聊天消息
const messages = ref([]);

// 当前填写的消息
const myMessage = reactive({
    userid: store.user.id,
    username: store.user.name,
    color:store.user.color,
    time:"", // 单独传入
    content:"", // 与页面绑定
})

// 判断当前消息的用户是否是自己
const isMe = (message) => {
    return message.userid == store.user.id ? 'rtl' : 'ltr';
}

// 判断当前颜色是否是浅色
const isLightColor = (color) => {
   const R = parseInt(color.substr(1,2),16);
   const G = parseInt(color.substr(3,2),16);
   const B = parseInt(color.substr(5,2),16);
   // 计算灰度
   const Y = R * 0.30 + G * 0.59 + B * 0.11;
   return Y > 128;
}

let sendMessage;

let isShowMyDetail = ref(false);

const editMyDetail = () => {
    alert("aaaa");
}

// websocket连接部分
(function() {
    // websocket对象,将用户名和密码同步发送二重验证身份
    const ws = new WebSocket("ws://localhost:8080/chat?token=" + store.jwt);

    // 成功连接
    ws.onopen = () => {
    }

    // 收到消息
    ws.onmessage = (event) => {
        const reply = JSON.parse(event.data);
        if(reply.type == -1) {
            alert(reply.content);
        } else if(reply.type == 0) {
            messages.value.push(reply.content);
        }
    }

    // 发送消息
    sendMessage = () => {
        // 空消息不发送
        if(myMessage.content == "") return;
        // 绑定当前时间
        myMessage.time = new Date().toISOString();
        ws.send(JSON.stringify({
            type:0,
            content:myMessage
        }));
        // 清空当前消息
        myMessage.content = "";
    }

    // 发生异常
    ws.onerror = () => {
        alert("聊天室连接失败");
        router.push("/login");
    }

    // 连接断开
    ws.onclose = (event) => {
        alert(event.reason ? event.reason : "聊天室异常断开");
        router.push("/login");
    }
})();

</script>

<template>
    <header tabindex="-1" @focus="isShowMyDetail = !isShowMyDetail" @blur="isShowMyDetail = !isShowMyDetail">
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
            <p class="text-center" @click="editMyDetail()"><i class="fa fa-pencil-square-o"></i> 修改个人信息</p>
        </div>
    </header>
    <main class="m-lg-5">
        <div id="messages">
            <div class="mb-2" v-for="message in messages" :key="message.id" :class="isMe(message)">
                <span id="userIcon" :style="{'background-color': message.color}"></span>
                <span class="message d-inline-block">
                    <span class="small"> {{ message.userid }}:{{ message.username }}</span>
                    <hr class="m-0 mt-1 mb-2">
                    {{ message.content }}
                </span>
            </div>
        </div>
        <div id="editor" class="d-flex flex-column">
            <textarea v-model="myMessage.content" class="d-block form-control auto-fill" @keyup.ctrl.enter = "sendMessage()"></textarea>
            <button class="btn btn-info" @click="sendMessage">发送</button>
        </div>
    </main>
</template>

<style scoped>
header {
    display: block;
    position: absolute;
    top: 0px;
    right: 0px;
    margin: 15px 15px 0px;
    border: none;
    background-color: transparent;
}

main {
    height: 90%;
    padding: 0 !important;
}

.auto-fill {
    flex-grow: 1;
}

.ltr {
    direction: ltr;
}

.rtl {
    direction: rtl;
}

#messages {
    overflow: auto;
    height: 64%;
    margin: 2vh;
    border-radius: 25px;
    background-color: rgb(255, 255, 255, 0.5);
    padding: 2vh;
}

#editor {
    height: 30%;
    margin: 2vh;
    border-radius: 25px;
    background-color: rgb(255, 255, 255, 0.5);
}

#editor > button {
    margin-left: auto;
    width: 10vmax;
}

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

#userIcon {
    display: inline-block;
    border-radius: 50%;
    padding: 8px;
    margin-right: 10px;
}

.message {
    border-radius: 10px;
    padding: 8px;
    margin-right: 10px;
    background-color: rgb(255, 255, 255, 1);
    min-width: 15%;
}
</style>