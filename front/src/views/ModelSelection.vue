<template>
  <el-card class="box-card">
    <el-row :gutter="20">
      <el-col>
        <div class="grid-content1 bg-white">
          <p class="p1">请选择修改问卷模式</p>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col
        :span="8"
        @click="chooseModel1()"
      >
        <div
          class="grid-content3 bg1"
          :class="checkType(4)"
        >
          <div class="div2">
            <img
              src="../assets/img/保存.png"
              class="user-avator"
              alt
            />
          </div>
          <div class="div1">
            <p class="p5">保留答卷</p>
            <p class="p6">修改受限制</p>
          </div>
        </div>
      </el-col>
      <el-col
        :span="8"
        @click="chooseModel2()"
      >
        <div
          class="grid-content3 bg1"
          :class="checkType(2)"
        >
          <div class="div2">
            <img
              src="../assets/img/删除.png"
              class="user-avator"
              alt
            />
          </div>
          <div class="div1">
            <p class="p5">删除所有答卷</p>
            <p class="p6">修改不受限制</p>
          </div>
        </div>
      </el-col>
      <el-col
        :span="8"
        @click="chooseModel3()"
      >
        <div
          class="grid-content3 bg1"
          :class="checkType(3)"
        >
          <div class="div2">
            <img
              src="../assets/img/复制.png"
              class="user-avator"
              alt
            />
          </div>
          <div class="div1">
            <p class="p5">复制此问卷并编辑</p>
            <p class="p6">原问卷不受影响</p>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col>
        <div
          class="grid-content2 bg-gray"
          v-if="model1"
        >
          <p class="p2">可以修改问卷的细节，例如更改错别字、添加选项、增加跳题逻辑或者修改题目的属性等。</p>
          <p class="p3">能对问卷做以下操作：<h class="h1">添加新的题目，修改原有题目的标题、备注及选项内容</h>
          </p>
        </div>
        <div
          class="grid-content2 bg-gray"
          v-if="model2"
        >
          <p class="p2">1.删除内容： {{count}}份有效答卷</p>
          <p class="p3">2.答卷<h class="h2">删除后不可恢复</h>，请谨慎选择。建议您先将已回收的答卷下载到本地。</p>
        </div>
        <div
          class="grid-content2 bg-gray"
          v-if="model3"
        >
          <p class="p4">复制一份题目完全一致的新问卷，对复制的问卷进行编辑，不会影响到原始问卷的题目及回收数据</p>
        </div>
      </el-col>
      <el-col>
        <div class="grid-content1 bg-white">
          <el-button
            type="primary"
            @click="next()"
            class="button1"
          >
            下一步
          </el-button>
        </div>
      </el-col>
    </el-row>
  </el-card>

</template>

<script>
export default {
  data() {
    return {
      model1: false,
      model2: true,
      model3: false,
      questionnaireId: "",
      showmode: false,
      count: '',
      type: -1,
    };
  },

  async created() {
    this.questionnaireId = this.$route.query.questionnaireId
    const { data: res } = await this.$http.get(
      `/api/admin/count?questionnaireId=${this.questionnaireId}`
    );
    if (res.code == 0) { this.count = res.data }
  },

  methods: {
    change() {

    },
    chooseModel1() {
      this.model1 = true;
      this.model2 = false;
      this.model3 = false;
      this.type = 4;
      this.showmode = false;
    },
    chooseModel2() {
      this.model1 = false;
      this.model2 = true;
      this.model3 = false;
      this.type = 2;
    },
    chooseModel3() {
      this.model1 = false;
      this.model2 = false;
      this.model3 = true;
      this.type = 3;
    },
    next() {
      if (this.type === -1) {
        alert("请选择修改问卷模式");
      }
      else {
        this.$router.push({
          path: '/questionnaireeditor',
          query: {
            enterType: this.type,
            questionnaireId: this.questionnaireId,
          }
        })
      }
    },
    checkType(val) {
      if (this.type === val) {
        return "selected"
      }
    }
  }
}
</script>


<style scoped>
.box-card {
  width: 60%;
  margin-left: 20%;
  height: 80%;
  margin-top: 30px;
}
.el-row {
  margin-bottom: 20px;
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-gray {
  background: rgb(243, 239, 239);
}
.bg-white {
  background: white;
}
.bg1 {
  background: white;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.grid-content1 {
  border-radius: 4px;
  min-height: 36px;
  margin-top: 20px;
}
.grid-content2 {
  border-radius: 4px;
  min-height: 36px;
  margin-top: 10px;
}
.grid-content3 {
  border-radius: 4px;
  min-height: 130px;
  margin-top: 20px;
  border: 2px solid blue;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.p1 {
  text-align: center;
  font-weight: bold;
  font-size: 25px;
}
.p2 {
  padding-top: 20px;
  padding-left: 20px;
  color: gray;
}
.p3 {
  padding-top: 5px;
  padding-left: 20px;
  padding-bottom: 20px;
  color: gray;
}
.h1 {
  color: black;
}
.h2 {
  color: red;
}
.p4 {
  padding-top: 20px;
  padding-left: 20px;
  color: gray;
  padding-bottom: 30px;
}
.user-avator {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  margin-left: 30px;
  margin-top: 30px;
}
.div1 {
  float: left;
  /* margin-top: 25px;
    margin-left: 20px;
    padding-top: 10px; */
  margin-top: 5%;
  padding-top: 5%;
  margin-left: 3%;
}
.div2 {
  float: left;
}
.p5 {
  font-weight: bold;
  font-size: 25px;
}
.p6 {
  color: gray;
}
.button1 {
  margin-left: 45%;
  margin-top: 30px;
}

.grid-content3:hover {
  background-color: rgb(203, 211, 236);
}
.grid-content3:active {
  background-color: rgb(203, 211, 236);
}

.selected {
  background-color: rgb(203, 211, 236);
}
</style>