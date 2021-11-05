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
      v-for="(item, index) in max"
      :key="index"
      v-model="s_value[index]"
      placeholder="请输入内容"
      :disabled="disabled"
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
    }
  },
  methods: {},
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
