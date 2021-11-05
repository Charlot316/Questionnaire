<template>
  <div class="header">
    <!-- 折叠按钮 -->

    <div class="logo">
      问卷星球
      &nbsp;&nbsp;&nbsp;<i v-if="
      ($route.path==='/questionnaireeditor')||
      ($route.path==='/recyclebin')||
      ($route.path==='/analysis')||
      ($route.path==='/model')||
      ($route.path==='/typeselection')||
      ($route.path==='/preview')" class="el-icon-back" @mouseenter="warning" @click="returnManage"></i>
    </div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 用户头像 -->
        <div class="user-avator">
          <router-link to="/userinfo">
            <img src="../assets/img/user.png" />
          </router-link>
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            {{ username }}

            <i class="el-icon-caret-bottom"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu v-if="islogin">
              <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>

              <el-dropdown-item command="manage">问卷管理</el-dropdown-item>

              <el-dropdown-item divided command="loginout"
                >退出登录</el-dropdown-item
              >
            </el-dropdown-menu>
            <el-dropdown-menu v-else>
              <router-link to="/login">
                <el-dropdown-item>前往登录</el-dropdown-item>
              </router-link>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      fullscreen: false,
      name: "linxin",
      message: 2,
    };
  },
  computed: {
    username() {
      let username = this.$store.state.username;
      return username;
      // return username ? username : this.name;
    },
    islogin() {
      return this.$store.state.islogin;
    },
  },
  methods: {
    // 用户名下拉菜单选择事件
    async handleCommand(command) {
      if (command == "loginout") {
        const { data: res } = await this.$http.post("/api/user/logout");
        if (res.code === 0) {
          this.$cookieStore.delCookie("userId");
          this.$store.commit("loginout");
          this.$message.success("登出成功");
          this.$router.push({ path: "/login" });
        } else {
          this.$message.error("登出失败");
        }
      } else if (command == "manage") {
        this.$router.push({ path: "/manage" });
      } else if (command == "userInfo") {
        this.$router.push({ path: "/userinfo" });
      }
    },
    returnManage() {
      if(this.$route.path==='/preview'){
        this.$router.go(-1);
      } else {
        this.$router.push('/manage');
      }
    },
    warning(){
      if(this.$route.path==='/questionnaireeditor'){
      }
    }
  },
  mounted() {},
};
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: rgb(255, 255, 255);
}
.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}
.header .logo {
  float: left;
  margin-left: 10px;
  width: 250px;
  line-height: 70px;
}
.header-right {
  float: right;
  padding-right: 50px;
}
.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}
.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
}
.btn-bell-badge {
  position: absolute;
  right: 0;
  top: -2px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}
.btn-bell .el-icon-bell {
  color: #fff;
}
.user-name {
  margin-left: 10px;
}
.user-avator {
  margin-left: 20px;
}
.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}
.el-dropdown-menu__item {
  text-align: center;
}
.logo i {
  padding: 5px;
  font-size: 20px;
  border: 1px solid white;
  border-radius: 30px;
  cursor: pointer;
}
</style>
