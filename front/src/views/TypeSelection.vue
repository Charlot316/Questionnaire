<template>
  <div class="div0">
    <div class="div1">
      <el-row :gutter="60">
        <el-col :span="8">
          <el-card
            @mouseenter="choose1()"
            @mouseleave="close1()"
          >
            <div class="div2">
              <img
                src="../assets/img/调查问卷.png"
                class="img1"
                alt
              />
            </div>
            <div class="div3">
              <p class="p1">调查</p>
              <div
                v-if="bool1Down"
                class="div33"
              >
                <p
                  class="p3"
                  @click="getDialog()"
                >创建</p>
              </div>
              <p
                class="p2"
                v-if="bool1Up"
              >丰富题型，逻辑强大</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            @mouseenter="choose2()"
            @mouseleave="close2()"
          >
            <div class="div2">
              <img
                src="../assets/img/在线考试.png"
                class="img1"
                alt
              />
            </div>
            <div class="div3">
              <p class="p1">考试</p>
              <div
                v-if="bool2Down"
                class="div33"
              >
                <p
                  class="p3"
                  @click="getDialog()"
                >创建</p>
              </div>
              <p
                class="p2"
                v-if="bool2Up"
              >限时作答，自动阅卷</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row
        :gutter="60"
        class="row1"
      >
        <el-col :span="8">
          <el-card
            @mouseenter="choose3()"
            @mouseleave="close3()"
          >
            <div class="div2">
              <img
                src="../assets/img/投票列表.png"
                class="img1"
                alt
              />
            </div>
            <div class="div3">
              <p class="p1">投票</p>
              <div
                v-if="bool3Down"
                class="div33"
              >
                <p
                  class="p3"
                  @click="getDialog()"
                >创建</p>
              </div>
              <p
                class="p2"
                v-if="bool3Up"
              >选项随机，实时排行</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            @mouseenter="choose4()"
            @mouseleave="close4()"
          >
            <div class="div2">
              <img
                src="../assets/img/报名管理.png"
                class="img1"
                alt
              />
            </div>
            <div class="div3">
              <p class="p1">报名</p>
              <div
                v-if="bool4Down"
                class="div33"
              >
                <p
                  class="p3"
                  @click="getDialog()"
                >创建</p>
              </div>
              <p
                class="p2"
                v-if="bool4Up"
              >活动报名，次数限额</p>
            </div>
          </el-card>
        </el-col>

      </el-row>
      <el-row
        :gutter="60"
        class="row1"
      >
        <el-col :span="8">
          <el-card
            @mouseenter="choose5()"
            @mouseleave="close5()"
          >
            <div class="div2">
              <img
                src="../assets/img/打卡.png"
                class="img1"
                alt
              />
            </div>
            <div class="div3">
              <p class="p1">疫情打卡</p>
              <div
                v-if="bool5Down"
                class="div33"
              >
                <p
                  class="p3"
                  @click="getDialog()"
                >创建</p>
              </div>
              <p
                class="p2"
                v-if="bool5Up"
              >监测疫情，实时填报</p>
            </div>
          </el-card>
        </el-col>

      </el-row>
      <el-dialog
        v-model="setVisible"
        width="40%"
      >
        <p class="p4">创建{{name[nameIndex]}}问卷</p>
        <el-input
          placeholder="请输入标题"
          class="input1"
          size="large"
          v-model="typeName"
          @keyup.enter="handleEdit"
        ></el-input>
        <el-button
          type="primary"
          class="button1"
          @click="handleEdit"
        >立即创建</el-button>

      </el-dialog>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      bool1Up: true,
      bool1Down: false,
      bool2Up: true,
      bool2Down: false,
      bool3Up: true,
      bool3Down: false,
      bool4Up: true,
      bool4Down: false,
      bool5Up: true,
      bool5Down: false,

      setVisible: false,

      nameIndex: undefined,
      name: ['调查', '投票', '报名', '考试', '疫情打卡'],
      typeName: ""
    };
  },
  created() {
    this.setVisible = false;
  },
  methods: {
    handleEdit() {
      if (this.typeName.trim().length === 0) {
        return this.$message.warning('新问卷名不能为空');
      }
      this.$router.push({ path: '/questionnaireeditor', query: { enterType: 1, name: this.typeName, type: this.nameIndex } })
    },
    choose1() {
      this.bool1Up = false;
      this.bool1Down = true;
    },
    close1() {
      this.bool1Up = true;
      this.bool1Down = false;
    },
    choose2() {
      this.bool2Up = false;
      this.bool2Down = true;
    },
    close2() {
      this.bool2Up = true;
      this.bool2Down = false;
    },
    choose3() {
      this.bool3Up = false;
      this.bool3Down = true;
    },
    close3() {
      this.bool3Up = true;
      this.bool3Down = false;
    },
    choose4() {
      this.bool4Up = false;
      this.bool4Down = true;
    },
    close4() {
      this.bool4Up = true;
      this.bool4Down = false;
    },
    choose5() {
      this.bool5Up = false;
      this.bool5Down = true;
    },
    close5() {
      this.bool5Up = true;
      this.bool5Down = false;
    },
    getDialog() {
      if (this.bool1Down) {
        this.nameIndex = 0;
      }
      else if (this.bool2Down) {
        this.nameIndex = 3;
      }
      else if (this.bool3Down) {
        this.nameIndex = 1;
      }
      else if (this.bool4Down) {
        this.nameIndex = 2;
      }
      else if (this.bool5Down) {
        this.nameIndex = 4;
      }
      this.setVisible = true;
    }
  },
}
</script>

<style scoped>
.dvi0 {
  width: 100%;
  height: 100%;
  text-align: center;
}
.div1 {
  width: 60%;
  margin-left: 30%;
  margin-top: 3%;
}
.row1 {
  margin-top: 4%;
}
.img1 {
  text-align: center;
}
.div2 {
  align-items: center;
  width: 60%;
  margin-left: 41%;
}
.div3 {
  text-align: center;
  margin-top: 5%;
}
.p1 {
  font-size: 20px;
}
.p2 {
  font-size: 12px;
  color: gray;
  margin-top: 5%;
}
.div33 {
  margin-top: 5%;
  font-size: 20px;
  font-weight: bold;
  color: rgb(42, 84, 221);
  background-color: rgb(193, 217, 233);
  padding-top: 2%;
  padding-bottom: 2%;
  height: 50%;
  cursor: pointer;
}
.p4 {
  text-align: center;
  font-size: 30px;
}
.input1 {
  margin-top: 10%;
  font-size: 20px;
}
.button1 {
  margin-top: 10%;
  margin-left: 43%;
  margin-bottom: 5%;
}
</style>