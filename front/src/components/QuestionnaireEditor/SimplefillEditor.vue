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
        <el-row>
          <el-input v-model="s_answer" placeholder="请输入答案"></el-input>
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
  methods: {},
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
