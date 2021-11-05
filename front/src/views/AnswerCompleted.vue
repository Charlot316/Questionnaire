<template>
    <el-card class="box-card">
        <div>
            <p class="p1">您的答卷已经提交，感谢您的参与！</p>
        </div>
        <div class="link-top"></div>
        <div class="div1">
            <p class="p2">问卷星球 提供技术支持</p>
        </div>
    </el-card>
    

</template>

<script>
export default {
  data() {
    return {
    };
  },

  created() {
      
  },

  methods: {

  }
}
</script>


<style scoped>
.box-card{
    width: 50%;
    margin-top: 8%;
    margin-left: 25%;
}
.p1{
    text-align: center;
    font-weight: bold;
    font-size: 17px;
    margin-top: 1%;
}
.div1{
    margin-top: 2%;
    font-size: 10px;
}
.p2{
    text-align: center;
}
.link-top {
  margin-top: 15%;
  width: 100%;
  height: 1px;
  border-top: solid #acc0d8 1px;
}
</style>