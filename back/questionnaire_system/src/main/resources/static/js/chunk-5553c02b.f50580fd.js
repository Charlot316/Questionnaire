(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-5553c02b"],{"0b5d":function(e,t,r){"use strict";r("1670")},1670:function(e,t,r){},a55b:function(e,t,r){"use strict";r.r(t);var a=r("7a23"),n=Object(a["gb"])("data-v-c4e7bdfe");Object(a["I"])("data-v-c4e7bdfe");var o={class:"login-wrap"},u={class:"ms-login"},s=Object(a["o"])("div",{class:"ms-title"},"问卷星球登录",-1),i={class:"input1"},c={class:"button1"},l=Object(a["n"])("立即注册");Object(a["G"])();var p=n((function(e,t,r,p,m,d){var b=Object(a["O"])("el-input"),f=Object(a["O"])("el-form-item"),g=Object(a["O"])("el-checkbox"),O=Object(a["O"])("el-form"),j=Object(a["O"])("router-link");return Object(a["F"])(),Object(a["k"])("div",o,[Object(a["o"])("div",u,[s,Object(a["o"])("div",i,[Object(a["o"])(O,{ref:"login",model:m.param,rules:m.rules},{default:n((function(){return[Object(a["o"])(f,{prop:"username"},{default:n((function(){return[Object(a["o"])(b,{placeholder:"用户名","prefix-icon":"el-icon-user","border-radius":"30px",style:{height:"40px"},modelValue:m.param.username,"onUpdate:modelValue":t[1]||(t[1]=function(e){return m.param.username=e}),onKeyup:t[2]||(t[2]=Object(a["eb"])((function(e){return d.submitForm()}),["enter"]))},null,8,["modelValue"])]})),_:1}),Object(a["o"])(f,{prop:"password"},{default:n((function(){return[Object(a["o"])(b,{placeholder:"密码","prefix-icon":"el-icon-lock","border-radius":"30px",style:{height:"40px"},modelValue:m.param.password,"onUpdate:modelValue":t[3]||(t[3]=function(e){return m.param.password=e}),type:"password",onKeyup:t[4]||(t[4]=Object(a["eb"])((function(e){return d.submitForm()}),["enter"]))},null,8,["modelValue"])]})),_:1}),Object(a["o"])(f,null,{default:n((function(){return[Object(a["o"])(g,{label:"下次自动登录",modelValue:m.param.isAutoLogin,"onUpdate:modelValue":t[5]||(t[5]=function(e){return m.param.isAutoLogin=e})},null,8,["modelValue"])]})),_:1})]})),_:1},8,["model","rules"]),Object(a["o"])("button",{class:"button",onClick:t[6]||(t[6]=function(e){return d.submitForm()})},"登录"),Object(a["o"])("button",c,[Object(a["o"])(j,{to:"/register"},{default:n((function(){return[l]})),_:1})])])])])})),m=r("1da1"),d=(r("96cf"),{data:function(){return{param:{username:"",password:"",isAutoLogin:!1},rules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]}}},created:function(){var e=this;return Object(m["a"])(regeneratorRuntime.mark((function t(){var r,a,n,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.post("/api/user/loginTest");case 2:if(r=t.sent,a=r.data,0!==a.code){t.next=10;break}return t.next=7,e.$http.get("/api/user/information");case 7:n=t.sent,o=n.data,0===o.code?(e.param.username=o.data.username,e.param.password=o.data.password,e.param.isAutoLogin=!0,e.$store.commit("login",e.param.username),e.$message.success("登录成功"),e.$router.push({path:"/manage"})):alert("获取用户信息失败");case 10:case"end":return t.stop()}}),t)})))()},methods:{submitForm:function(){var e=this;this.$refs.login.validate(function(){var t=Object(m["a"])(regeneratorRuntime.mark((function t(r){var a,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return!0===e.param.isAutoLogin?e.param.isAutoLogin=1:e.param.isAutoLogin=0,t.next=5,e.$http.post("/api/user/login",e.param);case 5:if(a=t.sent,n=a.data,0===n.code){t.next=9;break}return t.abrupt("return",e.$message.error(n.data));case 9:1===e.param.isAutoLogin?e.param.isAutoLogin=!0:e.param.isAutoLogin=!1,e.$message.success("登录成功"),e.$router.push({path:"/manage"}),e.$store.commit("login",e.param.username);case 13:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())}}});r("0b5d");d.render=p,d.__scopeId="data-v-c4e7bdfe";t["default"]=d}}]);
//# sourceMappingURL=chunk-5553c02b.f50580fd.js.map