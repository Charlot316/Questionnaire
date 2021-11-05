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
      <el-form-item v-if="bigType==3" label="答案解析:" class="labelFont">
        <el-row>
          <el-input
            v-model="s_analysis"
            placeholder="请输入解析"
          ></el-input>
        </el-row>
      </el-form-item>
      <el-form-item v-if="bigType==1" label="是否显示投票数:" class="labelFont">
        <el-radio-group v-model="s_isShowVotes">
          <el-radio :label="1">
            <span>是</span>
          </el-radio>
          <el-radio :label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="bigType==1" label="是否显示百分比:" class="labelFont">
        <el-radio-group v-model="s_isShowPercentage">
          <el-radio :label="1">
            <span>是</span>
          </el-radio>
          <el-radio :label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="bigType==3" label="分数设置:" class="labelFont">
        <el-input-number
          v-model="s_score"
          style="width:auto"
          controls-position="right"
          :min="0"
          :max="100"
        >
        </el-input-number>
      </el-form-item>
      <el-form-item label="选项编辑(请选择正确答案):" class="labelFont">
        <div v-for="(item, index) in choices" :key="index">
          <el-row class="el-row">
            <el-col :span="checkType()">
              <el-input
                style="width: 100%"
                v-model="item.choiceContent"
                placeholder="请输入选项内容"
              ></el-input>
            </el-col>
            <el-col v-if="bigType==3" :span="3" style="text-align:center">
              <el-radio v-model="isAnswer" :label="index" :value="index" >{{}}</el-radio>
            </el-col>
            <el-col :span="6" style="text-align:center">
              <i
                style="width: auto"
                class="el-icon-delete"
                @click="deleteOption(index)"
              ></i>
            </el-col>
          </el-row>
          <el-row v-if="bigType == 2">
            <el-col :span="12">
              <span>是否限额：</span>
              <el-radio-group v-model="item.isLimit">
                <el-radio :label="1">
                  <span>是</span>
                </el-radio>
                <el-radio :label="0">
                  <span>否</span>
                </el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="12" v-if="item.isLimit">
              <el-input-number
                v-model="item.allCount"
                style="width:auto"
                controls-position="right"
                :min="1"
                :max="1000"
              >
              </el-input-number>
            </el-col>
          </el-row>
        </div>
        <el-row>
          <el-button type="primary" @click="addOption">增加新选项</el-button>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  emits: [
    "addOption",
    "deleteOption",
    "updateContent",
    "updateIntroduction",
    "updateisCompulsory",
    "updateisShowVotes",
    "updateisShowPercentage",
    "updatescore",
    "updateisAnswer",
    "updateanalysis"
  ],
  props: [
    "bigType",
    "enterType",
    "order",
    "type",
    "content",
    "isCompulsory",
    "introduction",
    "choices",
    "isShowVotes",
    "isShowPercentage",
    "score",
    "fanswer",
    "analysis"
  ],
  data() {
    return {
      labelPosition: "top",
    };
  },
  computed: {
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
        this.$emit("updateIntroduction", [this.order, val]);
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
    s_isCompulsory: {
      get() {
        return this.isCompulsory;
      },
      set(val) {
        this.$emit("updateisCompulsory", [this.order, val]);
      },
    },
    s_isShowVotes: {
      get() {
        return this.isShowVotes;
      },
      set(val) {
        this.$emit("updateisShowVotes", [this.order, val]);
      },
    },
    s_isShowPercentage: {
      get() {
        return this.isShowPercentage;
      },
      set(val) {
        this.$emit("updateisShowPercentage", [this.order, val]);
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
    isAnswer: {
      get() {
        return this.fanswer;
      },
      set(val) {
        this.$emit("updateisAnswer", [this.order,this.type, val]);
      },
    }
  },
  methods: {
    addOption() {
      this.$emit("addOption", this.order);
    },
    deleteOption(index) {
      this.$emit("deleteOption", [this.order, index,this.type]);
    },
    checkType(){
      if(this.bigType==3){
        return 15
      } else {
        return 18
      }
    },
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
