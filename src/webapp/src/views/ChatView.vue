<script setup>
    import MyInfo from '@/components/MyInfo.vue'
    import ChatPanel from '@/components/ChatPanel.vue'
</script>

<template>
    <MyInfo id="header"/>
    <ChatPanel id="main"/>
</template>

<style scoped>
#header {
    display: block;
    position: absolute;
    top: 0px;
    right: 0px;
    margin: 15px 15px 0px;
    border: none;
    background-color: transparent;
}

#main {
    height: 90%;
    padding: 0 !important;
}
</style>