<template>
  <div>
    <el-form :model="this" label-width="60px" :label-position="labelPosition">
      <el-form-item label="问卷标题:" class="labelFont">
        <el-row>
          <el-input v-model="s_name" placeholder="请输入标题"></el-input>
        </el-row>
      </el-form-item>
      <el-form-item label="问卷备注:" class="labelFont">
        <el-row>
          <el-input v-model="s_introduction" placeholder="请输入备注(可选)"></el-input>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  emits: ["updateTheName", "updateTheIntroduction"],
  props: ["name", "introduction"],
  data() {
    return {
      labelPosition: "top",
    };
  },
  computed: {
    s_name: {
      get() {
        return this.name;
      },
      set(val) {
        this.$emit("updateTheName", val);
      },
    },
    s_introduction: {
      get() {
        return this.introduction;
      },
      set(val) {
        this.$emit("updateTheIntroduction", val);
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
