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
          <el-input v-model="s_introduction" placeholder="请输入备注(可选)">
          </el-input>
        </el-row>
      </el-form-item>
      <el-form-item label="各星级提示:" class="labelFont">
        <el-row class="el-row" v-for="(item, index) in s_texts" :key="index">
          <el-input
            v-model="s_texts[index]"
            placeholder="请输入提示内容"
          ></el-input>
        </el-row>
      </el-form-item>
      <el-form-item label="量级选择:" class="labelFont">
        <el-select v-model="s_max" placeholder="请选择">
          <el-option
            v-for="(item, index) in options"
            :key="index"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
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
    "updateMax",
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
    "texts",
  ],
  data() {
    return {
      labelPosition: "top",
      options: [
        {
          label: "3级量表",
          value: 3,
        },
        {
          label: "4级量表",
          value: 4,
        },
        {
          label: "5级量表",
          value: 5,
        },
        {
          label: "6级量表",
          value: 6,
        },
        {
          label: "7级量表",
          value: 7,
        },
        {
          label: "8级量表",
          value: 8,
        },
        {
          label: "9级量表",
          value: 9,
        },
        {
          label: "10级量表",
          value: 10,
        },
      ],
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
    s_texts: {
      get() {
        return this.texts;
      },
      set(val) {
        this.$emit("updateTexts", [this.order, val]);
      },
    },
    s_max: {
      get() {
        return this.max;
      },
      set(val) {
        this.$emit("updateMax", [this.order, val]);
      },
    },
  },
  methods: {
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
