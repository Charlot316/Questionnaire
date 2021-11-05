<template>
  <div>
    <el-form :model="this" label-width="60px" :label-position="labelPosition">
      <el-form-item label="起始时间:" class="labelFont">
        <el-row class="el-row">
          <el-col :span="11">
            <el-date-picker
              type="datetime"
              placeholder="选择日期"
              v-model="s_startTime"
              style="width: auto;"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">————</el-col>
          <el-col :span="11">
            <el-date-picker
              type="datetime"
              placeholder="选择时间"
              v-model="s_endTime"
              style="width: 100%;"
            ></el-date-picker>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="是否可重复填写:" class="labelFont">
        <el-radio-group v-model="s_isRepeat">
          <el-radio :label="1">
            <span>是</span>
          </el-radio>
          <el-radio :label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否允许未登录用户填写:" class="labelFont">
        <el-radio-group v-model="s_isLogin">
          <el-radio :label="1">
            <span>是</span>
          </el-radio>
          <el-radio :label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否自动生成题号:" class="labelFont">
        <el-radio-group v-model="s_isNumber">
          <el-radio :label="0">
            <span>是</span>
          </el-radio>
          <el-radio :label="1">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="bigType!=3" label="是否限额收集答卷:" class="labelFont">
        <el-row>
          <el-col :span="12">
            <el-radio-group v-model="s_isLimit">
              <el-radio :label="1">
                <span>是</span>
              </el-radio>
              <el-radio :label="0">
                <span>否</span>
              </el-radio>
            </el-radio-group>
          </el-col>
          <el-col :span="12" v-if="s_isLimit">
            <el-input-number
              v-model="s_allCount"
              style="width:auto"
              controls-position="right"
              :min="1"
              :max="1000"
            >
            </el-input-number>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item v-if="bigType==3" label="是否乱序生成题目:" class="labelFont">
        <el-radio-group v-model="s_isOrder">
          <el-radio :label="1">
            <span>是</span>
          </el-radio>
          <el-radio :label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  emits: [
    "updateTheisRepeat",
    "updateTheisLogin",
    "updateThestartTime",
    "updateTheendTime",
    "updateTheisNumber",
    "updateTheisLimit",
    "updateTheallCount",
    "updateTheisOrder",
  ],
  props: ["bigType","startTime", "endTime", "isRepeat", "isLogin", "isAppoint","isNumber","isLimit","allCount","allCountBefore","isOrder"],
  data() {
    return {
      labelPosition: "top",
    };
  },
  computed: {
    s_isRepeat: {
      get() {
        return this.isRepeat;
      },
      set(val) {
        this.$emit("updateTheisRepeat", val);
      },
    },
    s_isLogin: {
      get() {
        return this.isLogin;
      },
      set(val) {
        this.$emit("updateTheisLogin", val);
      },
    },
    s_isNumber: {
      get() {
        return this.isNumber;
      },
      set(val) {
        this.$emit("updateTheisNumber", val);
      },
    },
    s_isLimit: {
      get() {
        return this.isLimit;
      },
      set(val) {
        this.$emit("updateTheisLimit", val);
      },
    },
    s_allCount: {
      get() {
        return this.allCount;
      },
      set(val) {
        this.$emit("updateTheallCount", val);
      },
    },
    s_isOrder: {
      get() {
        return this.isOrder;
      },
      set(val) {
        this.$emit("updateTheisOrder", val);
      },
    },
    s_startTime: {
      get() {
        return this.startTime;
      },
      set(val) {
        let dt = new Date(val);
        let yyyy = dt.getFullYear();
        let MM = (dt.getMonth() + 1).toString().padStart(2, "0");
        let dd = dt
          .getDate()
          .toString()
          .padStart(2, "0");
        let h = dt
          .getHours()
          .toString()
          .padStart(2, "0");
        let m = dt
          .getMinutes()
          .toString()
          .padStart(2, "0");
        let s = dt
          .getSeconds()
          .toString()
          .padStart(2, "0");
        val = yyyy + "-" + MM + "-" + dd + " " + h + ":" + m + ":" + s;
        this.$emit("updateThestartTime", val);
      },
    },
    s_endTime: {
      get() {
        return this.endTime;
      },
      set(val) {
        let dt = new Date(val);
        let yyyy = dt.getFullYear();
        let MM = (dt.getMonth() + 1).toString().padStart(2, "0");
        let dd = dt
          .getDate()
          .toString()
          .padStart(2, "0");
        let h = dt
          .getHours()
          .toString()
          .padStart(2, "0");
        let m = dt
          .getMinutes()
          .toString()
          .padStart(2, "0");
        let s = dt
          .getSeconds()
          .toString()
          .padStart(2, "0");
        val = yyyy + "-" + MM + "-" + dd + " " + h + ":" + m + ":" + s;
        this.$emit("updateTheendTime", val);
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
  margin-left: 0;
  margin-right: 0;
}
</style>
