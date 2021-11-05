<template>
  <div>
    <el-form :model="this" label-width="60px" :label-position="labelPosition">
      <el-form-item label="标题:" class="labelFont">
        <el-row>
          <el-input v-model="s_content" placeholder="请输入标题"></el-input>
        </el-row>
      </el-form-item>
      <el-form-item label="是否必填:" class="labelFont">
        <el-radio-group v-model="s_isCompulsory">
          <el-radio :label="1">
            <span>是</span>
          </el-radio>
          <el-radio :label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="题目备注:" class="labelFont">
        <el-row>
          <el-input
            v-model="s_introduction"
            placeholder="请输入备注(可选)"
          ></el-input>
        </el-row>
      </el-form-item>
      <el-form-item v-if="bigType == 3" label="答案:" class="labelFont">
        <el-row v-for="(item,index) in max" :key="index">
          <el-input v-model="answers[index]" placeholder="请输入答案" @input="changeAnswer()"></el-input>
        </el-row>
      </el-form-item>
      <el-form-item v-if="bigType == 3" label="答案解析:" class="labelFont">
        <el-row>
          <el-input v-model="s_analysis" placeholder="请输入解析"></el-input>
        </el-row>
      </el-form-item>
      <el-form-item v-if="bigType == 3" label="分数设置:" class="labelFont">
        <el-input-number
          v-model="s_score"
          style="width:auto"
          controls-position="right"
          :min="0"
          :max="100"
        >
        </el-input-number>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  emits: [
    "updateContent",
    "updateIntroduction",
    "updateisCompulsory",
    "updatescore",
    "updateanalysis",
    "updateanswer",
  ],
  props: [
    "bigType",
    "enterType",
    "order",
    "type",
    "content",
    "isCompulsory",
    "introduction",
    "max",
    "answerCountBefore",
    "score",
    "analysis",
    "answer",
  ],
  data() {
    return {
      labelPosition: "top",
    };
  },
  computed: {
    answers: {
      get() {
        let temp=[];
        let j = 0;
        if(this.answer!=null){
          for(let i = 0;i<this.answer.length;i++){
            if(this.answer[i]===';'){
              if(j==i){
                temp.push("");
              } else {
                temp.push(this.answer.substring(j,i));
              }
              j=i+1;
            }
          }
          temp.push(this.answer.substring(j));
          return temp;          
        } else {
          let temp = [];
          for(let i = 0;i<this.max;i++){
            temp.push("");
          }
          return temp
        }

      }
    },
    s_content: {
      get() {
        return this.content;
      },
      set(val) {
        this.$emit("updateContent", [this.order, val]);
      },
    },
    s_introduction: {
      get() {
        return this.introduction;
      },
      set(val) {
        let flag = false;
        let count = 0;
        for (let i = 2; i < val.length; i++) {
          if (
            val[i] === "_" &&
            val[i - 1] === "_" &&
            val[i - 2] === "_" &&
            !flag
          ) {
            count++;
            flag = true;
          }
          if (val[i] !== "_") {
            flag = false;
          }
        }

        if (this.enterType == 4 && count < this.answerCountBefore) {
          if (
            val[val.length - 1] === "_" &&
            val[val.length - 2] === "_" &&
            val[val.length - 3] === "_"
          ) {
            val += ",___";
          } else if (
            val[val.length - 1] === "_" &&
            val[val.length - 2] === "_"
          ) {
            val += "_";
          } else if (
            val[val.length - 1] === "_" &&
            val[val.length - 2] !== "_"
          ) {
            val += "__";
          } else if (val[val.length - 1] !== "_") {
            val += "___";
          }
          for (let i = 1; i < this.answerCountBefore - count; i++) {
            val += ",___";
          }
          count = this.answerCountBefore;
          this.$message.warning("该模式不可减少原有题目填空数");
        }
        if (count === 0) {
          this.$message.warning("必须至少含有一个填空");
          if (val[val.length - 1] === "_" && val[val.length - 2] === "_") {
            val += "_";
          } else if (
            val[val.length - 1] === "_" &&
            val[val.length - 2] !== "_"
          ) {
            val += "__";
          } else if (val[val.length - 1] !== "_") {
            val += "___";
          }
          count = 1;
        }
        if(this.bigType==3){
          this.answers.length=count;
          this.changeAnswer();          
        }

        this.$emit("updateIntroduction", [this.order, val, count]);
      },
    },
    s_isCompulsory: {
      get() {
        return this.isCompulsory;
      },
      set(val) {
        this.$emit("updateisCompulsory", [this.order, val]);
      },
    },
    s_answer: {
      get() {
        return this.answer;
      },
      set(val) {
        this.$emit("updateanswer", [this.order, val]);
      },
    },
    s_score: {
      get() {
        return this.score;
      },
      set(val) {
        this.$emit("updatescore", [this.order, val]);
      },
    },
    s_analysis: {
      get() {
        return this.analysis;
      },
      set(val) {
        this.$emit("updateanalysis", [this.order, val]);
      },
    },
  },
  methods: {
    changeAnswer(){
      let temp = this.answers[0];
      for(let i=1;i<this.answers.length;i++){
        if(this.answers[i]==undefined){
          temp+=";"
        } else {
          temp+=";"+this.answers[i];
        }
      }
      this.s_answer=temp;
    }
  },
};
</script>

<style>
.labelFont .el-form-item__label {
  font-size: 20px;
  color: black;
  font-weight: 900;
}
</style>

<style scoped>
.el-icon-delete {
  padding: 10px;
  font-size: 20px;
  border: 1px solid rgb(73, 191, 245);
  border-radius: 30px;
}

i:hover {
  color: aliceblue;
  background-color: rgb(64 158 255);
  cursor: pointer;
}

.el-row {
  margin: 10px;
}
</style>
