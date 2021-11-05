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
    <el-rate
      class="el-rate"
      v-model="s_value"
      show-text
      :texts="texts"
      :max="s_max"
      :disabled="disabled"
    ></el-rate>
  </div>
</template>

<script>
export default {
  emits:["updateValue"],
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
  methods: {},
  computed: {
    s_max:{
      get() {
        return this.max;
      },
    },
    s_value:{
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("updateValue",[this.order,val])
      }
    }
  }
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

  .el-rate {
    display: block;
    width: auto;
    margin-top: 20px;
    margin-bottom: 20px;
    margin-left: 12%;
    margin-right: 12%;
    size: 20;
  }

  .el-rate /deep/ .el-rate__icon {
    font-size: 30px;
  }

  .el-rate /deep/ .el-rate__text {
    line-height: 30px;
    margin-left: 20px;
    font-size: 20px;
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

  .el-rate {
    display: block;
    width: auto;
    margin-top: 2vh;
    margin-bottom: 2vh;
    margin-left: 12%;
    margin-right: 12%;
  }

  .el-rate /deep/ .el-rate__icon {
    font-size: 6vw;
  }

  .el-rate /deep/ .el-rate__text {
    line-height: 6vw;
    margin-left: 2vw;
    font-size: 4vw;
  }

}


</style>
