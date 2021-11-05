<template>
  <div>
    <span class="item-content">
      <h3>
        <font v-if="isNumber === 0">{{ order }}.</font>
        {{ content }}<font v-if="isCompulsory === 1">*</font>
      </h3>
    </span>
  </div>
  <div class="item-introduction">
    <p>{{ introduction }}</p>
  </div>
  <div>
    <el-checkbox-group
      class="el-checkbox-group"
      v-model="s_value"
      :disabled="disabled"
    >
      <li v-for="(item, index) in choices" :key="index">
        <el-checkbox class="el-checkbox" :class="checkDisabled()" :label="item.choiceId" :disabled="item.nowCount==0">
          <div>
            <span>{{ item.choiceContent }}</span>
          </div>
          <div v-if="bigType==1" class="Show">
            <span v-if="isShowVotes">{{item.votes}}票</span><span v-if="isShowPercentage">({{item.percentage}}%)</span>
          </div>
          <div v-if="bigType==2" class="Show">
            <span v-if="item.isLimit&&$route.path==='/questionnaireeditor'">剩余{{item.allCount}}</span>
            <span v-else-if="item.isLimit&&$route.path==='/questionnaire'">剩余{{item.nowCount}}</span>
          </div>
        </el-checkbox>
      </li>
    </el-checkbox-group>
  </div>
</template>

<script>
export default {
  emits: ["updateValue"],
  props: [
    "bigType",
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
    "disabled",
    "isShowVotes",
    "isShowPercentage"
  ],
  data() {
    return {};
  },
  computed: {
    s_value: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("updateValue", [this.order, val]);
      },
    },
  },
  methods: {
    checkDisabled(){
      if(this.disabled===false){
        return "checkbox"
      }
    }
  },
};
</script>

<style scoped>
font {
  font-family: "Lucida Handwriting";
  color: red;
}

.el-checkbox {
  position: relative;
}

.el-checkbox div{
  display: inline-block;
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

  .el-checkbox span {
    font-size: 15px;
  }

  .el-checkbox {
    padding: 10px;
    display: block;
    width: auto;
    margin: 10px;
    margin-left: 12%;
    margin-right: 12%;
    transition: 0.2s;
  }

  .el-checkbox-group li {
    height: 40px;
  }

  .Show {
    position: absolute;
    right: 10px;
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

  .el-checkbox span {
    font-size: 4vw;
  }

  .el-checkbox {
    padding: 1vh;
    display: block;
    width: auto;
    margin: 1vh;
    margin-left: 10%;
    margin-right: 10%;
    transition: 0.2s;
  }

  .el-checkbox-group li {
    height: 5vh;
  }

  .Show {
    position: absolute;
    right: 1vw;
  }
}

.item-content {
  text-indent: 2em;
}

.item-introduction {
  text-indent: 2em;
}

.checkbox:hover {
  border: 1px solid rgb(0, 153, 255);
  border-radius: 35px;
}
</style>
