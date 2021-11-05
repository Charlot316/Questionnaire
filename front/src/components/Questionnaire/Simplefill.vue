<template>
  <div>
    <span class="item-content">
      <h3>
        <font  v-if="isNumber===0">{{ order }}.</font>
        {{ content }}<font v-if="isCompulsory===1">*</font>
      </h3>
    </span>
  </div>
  <div class="item-introduction">
    <p>{{ introduction }}</p>
  </div>
  <div>
    <el-input
      class="el-input"
      :class="checkType()"
      v-model="s_value"
      :placeholder="placeholder"
      :disabled="disabled"
      :readonly="readonly"
      @click="getPosition()"
    ></el-input>
  </div>
</template>

<script>
export default {
  emits: ["updateValue"],
  props: [
    "questionId",
    "order",
    "type",
    "content",
    "introduction",
    "isCompulsory",
    "isNumber",
    "choices",
    "max",
    "texts",
    "value",
    "disabled"
  ],
  data() {
    return {
    };
  },
  computed: {
    s_value:{
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("updateValue",[this.order,val])
      }
    },
    placeholder:{
      get(){
        if(this.type==3){
          return "请输入内容"
        } else {
          return "点击获取地址"
        }
      }
    },
    readonly:{
      get(){
        if(this.type==7){
          return true;
        } else {
          return false;
        }
      }
    }
  },
  methods: {
    checkType(){
      if(this.type==7&&this.disabled==false){
        return "position"
      }
    },
    getPosition(){
      if(this.type==7){
        this.$confirm('是否授权获取地址?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.getLocation()
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消授权'
          });
        });
      }
    },
    getLocation () {
      const self = this;
      AMap.plugin('AMap.Geolocation', function () {
        var geolocation = new AMap.Geolocation({
          // 是否使用高精度定位，默认：true
          enableHighAccuracy: true,
          // 设置定位超时时间，默认：无穷大
          timeout: 10000
        });
 
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', onComplete);
        AMap.event.addListener(geolocation, 'error', onError);
 
        function onComplete (data) {
          // data是具体的定位信息
          self.province = data.addressComponent.province;
          self.city = data.addressComponent.city;
          self.district = data.addressComponent.district;
          self.s_value = self.province+" "+self.city+" "+self.district;
          self.$message({
            type: 'success',
            message: '获取成功!'
          });
        }
 
        function onError (data) {
          // 定位出错
          // 调用IP定位
        }
      });
    },
  },
};
</script>

<style scoped>

font {
  font-family: "Lucida Handwriting";
  color: red;
}

.item-content {
  text-indent: 2em;
}

.item-introduction {
  text-indent: 2em;
}

.position /deep/ .el-input__inner{
  cursor: pointer;
}

@media screen and (min-width: 481px){

  h3 {
    margin-left: 10%;
    margin-right: 10%;
    margin-bottom: 10px;
  }

  p {
    margin-left: 10%;
    margin-right: 10%;
    margin-bottom: 10px;
    font-size: 14px;
    color: rgba(109, 109, 109, 0.836);
  }

  .el-input {
    display: block;
    width: auto;
    margin-top: 20px;
    margin-bottom: 10px;
    margin-left: 12%;
    margin-right: 12%;
  }

}

@media screen and (max-width: 480px){
  
  h3 {
    font-size: 5vw;
    margin-left: 5%;
    margin-right: 5%;
    margin-bottom: 1vh;
  }

  p {
    margin-left: 5%;
    margin-right: 5%;
    font-size: 3vw;
    color: rgba(109, 109, 109, 0.836);
  }

  .el-input {
    display: block;
    width: auto;
    margin-top: 2vh;
    margin-bottom: 2vh;
    margin-left: 12%;
    margin-right: 12%;
  }
}


</style>
