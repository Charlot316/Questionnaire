<template>
  <div>
    <el-card class="box-card">
      <div class="user-info">
        <span class="line"></span>
        <img
          src="../assets/img/user.png"
          class="user-avator"
          alt
        />
        <span class="line"></span>
      </div>
      
      <div class="list">
        <table
          border="0"
          cellspacing="20"
          class="table"
        >
          <tr>
            <td class="td1"><label class="label">用户名称</label></td>
            <td class="td2"><input
                type="text"
                v-model="username"
                class="input"
              /></td>
          </tr>
          <tr>
            <td class="td1"><label class="label">性别</label></td>
            <!-- <td class="td2"><input type="text" value="男"  class="input" /></td> -->
            <td>
              <el-radio-group v-model="sex">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td1"><label class="label">手机号码</label></td>
            <td class="td2"><input
                type="text"
                v-model="phoneNumber"
                class="input"
              /></td>
          </tr>
          <tr>
            <td class="td1"><label class="label">邮箱</label></td>
            <td class="td2"><input
                type="text"
                v-model="email"
                class="input"
              /></td>
          </tr>
        </table>
        <div>
          <el-button
            type="info"
            plain
            @click="changeUserInformation()"
          >保存</el-button>
        </div>
        <div class="div1">
          <h class="h">修改密码</h>
        </div>
        <div class="link-top"></div>
        <div class="kong">
          <el-button
            type="primary"
            icon="el-icon-edit"
            class="button1"
            @click="showChangePassword()"
          >修改密码</el-button>
        </div>
      </div>

    </el-card>

    <!--编辑用户修改密码的弹出框-->
    <el-dialog
      title="修改密码"
      v-model="editVisibleChangepassword"
      width="30%"
    >
      <el-form
        label-width="100px"
        :model="param"
      >
        <el-form-item
          label="旧密码"
          prop="oldPassword"
        >
          <el-input
            type="password"
            v-model="param.oldPassword"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="新密码"
          prop="newPassword"
        >
          <el-input
            type="password"
            v-model="param.newPassword"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认新密码"
          prop="ensurePassword"
        >
          <el-input
            type="password"
            v-model="param.ensurePassword"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="changePassword()"
          >确定</el-button>
          <el-button
            @click="quitChangePassword()"
            class="button"
          >关闭</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      data:"",
      username: "",
      password: "",
      email: "",
      sex: '',
      poneNumber: "",
      userInformation: { username: "", password: "", email: "", sex: "", phoneNumber: "" },

      retPass: { password: "" },

      //修改密码的弹窗
      editVisibleChangepassword: false,

      //导出的pdf文件名
      htmlTitle: '个人信息',

      //修改密码的信息
      param: {
        oldPassword: "",
        newPassword: "",
        ensurePassword: "",
      },
    };
  },

  methods: {
    getPdf() {
      this.$getPdf();
    },
    onInput() {
      this.$forceUpdate();
    },
    showChangePassword() {
      this.editVisibleChangepassword = true;
    },
    quitChangePassword() {
      this.editVisibleChangepassword = false;
    },

    async showUserInformation() {
      const { data: res } = await this.$http.get("/api/user/information");
      if (res.code === 0) {
        this.username = res.data.username;
        this.password = res.data.password;
        this.sex = res.data.sex;
        this.email = res.data.email;
        this.phoneNumber = res.data.phoneNumber;
      }
      else {
        this.$message.error("发生错误");
      }
    },

    async changeUserInformation() {
      if(!this.phoneNumber.match(/^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/)){
        alert("请输入合法的手机号码！")
      }
      else if(!this.email.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/)){
        alert("请输入合法的邮箱")
      }
      else{
        this.userInformation["username"] = this.username;
        this.userInformation["password"] = this.password;
        this.userInformation["email"] = this.email;
        this.userInformation["phoneNumber"] = this.phoneNumber;
        this.userInformation["sex"] = this.sex;
        const { data: res } = await this.$http.post("/api/user/userInformation", this.userInformation);
        if (res.code === 0) {
          this.$message.success("成功修改个人信息");
        }
        else {
          this.$message.error("修改失败")
        }
      }
      
    },

    async changePassword() {
      if (this.password !== this.param["oldPassword"]) {
        this.$message.error("旧密码输入错误");
      }
      else {
        if (this.param["newPassword"] !== this.param["ensurePassword"]) {
          this.$message.error("新密码和确认密码不一致");
        }
        else {
          if (this.param["newPassword"] === this.password) {
            this.$message.error("新密码不能与旧密码相同")
          }
          else {
            var length = this.param["newPassword"].length
            if(length<6||length>20){
              alert("密码长度应在6到20个字符");
            }
            else{
              this.retPass.password = this.param["newPassword"];
              const { data: res } = await this.$http.post("/api/user/password", this.retPass);
              this.showUserInformation();
              if (res.code === 0) {
                this.$message.success("成功修改个人密码");
                this.param.oldPassword = "";
                this.param.newPassword = "";
                this.param.ensurePassword = "";
              }
              else {
                this.$message.error("发生错误");
              }
            }
          }
        }
      }
    }

  },

  created() {
    this.showUserInformation();
  }
}
</script>


<style scoped>
.user-avator {
  width: 70px;
  height: 70px;
  border-radius: 50%;
}
.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 10px;
  padding-top: 10px;
  margin-bottom: 20px;
  width: 70%;
  margin-left: 15%;
}
.line {
  display: inline-block;
  width: 100%;
  border-top: 1px solid #cccccc;
  vertical-align: 5px;
}
.list {
  width: 50%;
  margin-left: 25%;
  /* border-bottom: 2px solid #ccc; */
}
p {
  display: inline-block;
}
li {
  list-style: none;
}
p1 {
  padding-left: 0%;
  margin-left: 0%;
}
p2 {
  margin-left: 50px;
}
list1 {
  margin: 5px;
}
table {
  margin: auto;
}
td1 {
  color: rgb(236, 43, 43);
}
td2 {
  text-decoration: underline;
}
.card {
  margin-top: 20%;
  width: 50%;
  height: 100%;
}
button {
  margin-left: 45%;
}
input {
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 1px solid black;
  width: 150px;
  font-size: 17px;
}
.button1 {
  margin-left: 6%;
  margin-top: 3%;
}
.label {
  color: gray;
}
.h {
  font-size: 20px;
}
.div1 {
  margin-top: 30px;
  width: 100%;
}
.div2 {
  margin-top: 50px;
}
.box-card {
  width: 60%;
  margin-left: 20%;
  height: 80%;
}
kong {
  margin-top: 50px;
  float: left;
}
.link-top {
  margin-top: 15px;
  width: 100%;
  height: 1px;
  border-top: solid #acc0d8 1px;
}
.img {
  margin-left: 6%;
  margin-top: 2%;
  vertical-align: middle;
}
.pc {
  margin-top: 30px;
  margin-left: 6%;
  font-size: 20px;
  display: inline-block;
}
.pc1 {
  margin-top: 30px;
  margin-left: 6%;
  font-size: 12px;
  display: inline-block;
  color: gray;
}
</style>