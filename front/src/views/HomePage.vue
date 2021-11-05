<template>
  <div class="login-wrap">
    <div class="div1">
        <button
          class="button1"
          @click="$router.push({
                    path: '/login',
                })"
        >登录</button>
        <button
          class="button2"
          @click="$router.push({
                    path: '/register',
                })"
        >注册</button>
    </div>
    <div class="div2">
        <p class="p1">问卷星球</p>
        <p class="p2"><h>问卷星球</h>是一款在线表单制作工具，可以帮助你轻松完成信息收集与整理。</p>
        <button
          class="button3"
          @click="$router.push({
                    path: '/register',
                })"
        >免费使用</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      
    };
  },
  methods: {

  },
}
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-color: #ffdee9;
  background-image: url(../assets/img/bg1.png);
  background-size: 100%;
}
.button1 {
  background-color: rgb(36, 184, 243);
  border: none;
  color: white;
  /* padding: 15px 32px; */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  width: 5%;
  height: 30px;
  border-radius: 20px;
  margin-left: 80%;
}
.button2 {
  background-color: rgb(185, 201, 224);
  border: none;
  color: rgb(20, 176, 238);
  /* padding: 15px 32px; */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  width: 5%;
  height: 30px;
  border-radius: 20px;
  margin-left: 1%;
}
.button3 {
  background-color: rgb(41, 169, 243);
  border: none;
  color: white;
  /* padding: 15px 32px; */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 18px;
  margin: 4px 2px;
  cursor: pointer;
  width: 10%;
  height: 45px;
  border-radius: 25px;
  margin-left: 45%;
  margin-top: 2%;
}
.div1{
    padding-top : 1%;
}
.button1:hover{
    color: rgb(36, 184, 243);
    background-color: white;
}
.button2:hover{
    color: white;
    background-color: rgb(36, 184, 243);
}
.button3:hover{
    background-color: rgb(6, 158, 247);
}
.p1{
    text-align: center;
    color: rgb(41, 169, 243);
    font-size: 50px;
    margin-top: 2%;
}
.p2{
    text-align: center;
    color: white;
    font-size: 20px;
    margin-top: 2%;
}
.h1{
    color: rgb(240, 138, 54);;
}
.div2{
  margin-left: 20%;
  margin-top: 10%;
}
</style>