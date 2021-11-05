<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">用户注册</div>
      <div class="input1">
        <el-form
          ref="login"
          :model="param"
          :rules="rules"
        >
          <el-form-item prop="username">
            <el-input
              placeholder="用户名"
              prefix-icon="el-icon-user"
              border-radius="30px"
              style="height: 40px"
              v-model="param.username"
              @keyup.enter="submitForm()"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="phoneNumber">
            <el-input
              placeholder="手机号码"
              prefix-icon="el-icon-phone"
              border-radius="30px"
              style="height: 40px"
              v-model="param.phoneNumber"
              @keyup.enter="submitForm()"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input
              placeholder="邮箱"
              prefix-icon="el-icon-message"
              border-radius="30px"
              style="height: 40px"
              v-model="param.email"
              @keyup.enter="submitForm()"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              placeholder="密码"
              prefix-icon="el-icon-lock"
              border-radius="30px"
              style="height: 40px"
              v-model="param.password"
              type="password"
              @keyup.enter="submitForm()"
            >
            </el-input>
          </el-form-item>
        </el-form>
        <button
          class="button"
          @click="submitForm()"
        >创建用户</button>
        <button class="button1">
          <router-link to="/login">已有帐号,立即登录</router-link>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    var checkEmail = (rule, value, callback) => {
      const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/;
      if (regEmail.test(value)) {
        return callback();
      }
      callback(new Error("请输入合法的邮箱"));
    };
    var checkPhone = (rule, value, callback) => {
      const regPhone = /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/;
      if (regPhone.test(value)) {
        return callback();
      }
      callback(new Error("请输入合法的手机号码"));
    };
    return {
      param: {
        username: "",
        password: "",
        email: "",
        phoneNumber: "",
        sex: 1
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 1, max: 20, message: "用户名长度在1到20个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "密码长度在6到20个字符", trigger: "blur" }
        ],

        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            validator: checkEmail,
            trigger: "blur",
          }
        ],
        phoneNumber: [
          { requierd: true, message: "手机号码不能为空", trigger: "blur" },
          {
            validator: checkPhone,
            trigger: "blur",
          }
        ]
      },
    };
  },
  created() {
  },
  methods: {
    submitForm() {
      this.$refs.login.validate(async (valid) => {
        if (!valid) {
          return;
        }
        const { data: res } = await this.$http.post("/api/user/register", this.param);
        if (res.code !== 0)
          return this.$message.error(res.data);
        this.$message.success("注册成功");
        // this.$store.commit("login", this.param.username);
        // this.$store.commit("storeId", res.user_id);
        // this.$store.commit("setRole", res.role);
        this.$router.push({ path: "/login" });
      });
    },
  },
};
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-color: #ffdee9;
  background-image: url(../assets/img/loginbg.jpg);
  background-size: 100%;
}
.ms-title {
  width: 100%;
  line-height: 50px;
  padding-left: 8%;
  padding-top: 5%;
  /* text-align: left; */
  font-size: 25px;
  font-weight: 900;
  color: rgb(240, 138, 54);
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 40%;
  width: 500px;
  height: 500px;
  margin: -190px 0 0 -175px;
  border-radius: 15px;
  background-color: #fff;
  /* background: rgba(240, 240, 243, 0.945); */
  overflow: hidden;
}
.ms-content {
  padding: 30px 30px;
}
.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}
.login-tips {
  font-size: 12px;
  line-height: 30px;
  color: #fff;
}
.input1 {
  width: 84%;
  margin-left: 8%;
  margin-top: 5%;
}
.el-form-item {
  margin-top: 5px;
}
.button {
  background-color: rgb(240, 138, 54);
  border: none;
  color: white;
  /* padding: 15px 32px; */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  width: 100%;
  height: 40px;
  border-radius: 20px;
}
.button1 {
  background-color: white;
  border: none;
  color: rgb(240, 138, 54);
  /* padding: 15px 32px; */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  width: 100%;
  height: 40px;
  border-radius: 20px;
}
a {
  color: rgb(240, 138, 54);
}
</style>