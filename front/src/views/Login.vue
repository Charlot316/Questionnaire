<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">问卷星球登录</div>
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
          <el-form-item>
            <el-checkbox
              label="下次自动登录"
              v-model="param.isAutoLogin"
            ></el-checkbox>
          </el-form-item>
        </el-form>
        <button
          class="button"
          @click="submitForm()"
        >登录</button>
        <button class="button1">
          <router-link to="/register">立即注册</router-link>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      param: {
        username: "",
        password: "",
        isAutoLogin: false,
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  async created() {
    const { data: res } = await this.$http.post("/api/user/loginTest");
    if (res.code === 0) {
      const { data: res } = await this.$http.get("/api/user/information");
      if (res.code === 0) {
        this.param.username = res.data.username;
        this.param.password = res.data.password;
        this.param.isAutoLogin = true;
        this.$store.commit("login", this.param.username);
        this.$message.success("登录成功");
        this.$router.push({ path: "/manage" });
      }
      else {
        alert("获取用户信息失败");
      }
    }
  },
  methods: {

    submitForm() {
      this.$refs.login.validate(async (valid) => {
        if (!valid) {
          return;
        }
        if (this.param.isAutoLogin === true) this.param.isAutoLogin = 1;
        else this.param.isAutoLogin = 0;
        const { data: res } = await this.$http.post("/api/user/login", this.param);
        if (res.code !== 0)
          return this.$message.error(res.data);
        if (this.param.isAutoLogin === 1) this.param.isAutoLogin = true;
        else this.param.isAutoLogin = false;
        this.$message.success("登录成功");
        this.$router.push({ path: "/manage" });
        this.$store.commit("login", this.param.username);
      });
    },
  },
}
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