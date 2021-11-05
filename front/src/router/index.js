import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";

const routes = [
  {
    path: "/",
    redirect: "/homepage", //可以理解为初始界面
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/votingresult",
    name: "votingresult",
    component: () => import("../views/VotingResult.vue"),
  },
  {
    path: "/examresult",
    name: "examresult",
    component: () => import("../views/ExamResult.vue"),
  },
  {
    path: "/testIp",
    name: "testIp",
    component: () => import("../views/TestIp.vue"),
  },
  {
    path: "/testdrag",
    name: "testdrag",
    component: () => import("../views/TestDrag.vue"),
  },
  {
    path: "/homepage",
    name: "homepage",
    component: () => import("../views/HomePage.vue"),
  },
  {
    path: "/answercompleted",
    name: "answercompleted",
    component: () => import("../views/AnswerCompleted.vue"),
  },
  {
    path: "/test",
    name: "test",
    component: () => import("../views/Test.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () => import("../views/Register.vue"),
  },

  //   {
  //     path: "/login",
  //     name: "Login",
  //     component: () =>
  //       import(
  //         /* webpackChunkName: "login" */
  //         "../views/Login.vue"
  //       ),
  //   },
  {
    path: "/questionnaire",
    name: "questionnaire",
    component: () => import("../views/Answering.vue"),
    meta: {
      title: "填写问卷",
      //requireAuth: true,
      keepAlive: true,
    },
  },
  {
    path: "/",
    name: "Home", //Home组件里包括了初始的导航栏和侧边栏的样式，它的所有children都会共享Home里的导航栏、侧边栏
    component: Home,
    children: [
      {
        path: "/userinfo",
        name: "userinfo",
        component: () => import("../views/UserInformation.vue"),

        meta: {
          title: "用户信息",
          requireAuth: true,
          keepAlive: true,
        },
      },
      {
        path: "/manage",
        name: "manage",
        component: () => import("../views/Manage.vue"),
        meta: {
          title: "我的问卷",
          requireAuth: true,
          keepAlive: true,
        },
      },
      {
        path: "/model",
        name: "model",
        component: () => import("../views/ModelSelection.vue"),
        meta: {
          title: "模式选择",
          //requireAuth: true,
          keepAlive: true,
        },
      },
      {
        path: "/questionnaireeditor",
        name: "questionnaireeditor",
        component: () => import("../views/QuestionnaireEditor.vue"),
        meta: {
          title: "编辑问卷",
          requireAuth: true,
          keepAlive: true,
        },
      },
      {
        path: "/analysis",
        name: "analysis",
        component: () => import("../views/Analysis.vue"),
        meta: {
          title: "填写问卷",
          //requireAuth: true,
          keepAlive: true,
        },
      },
      {
        path: "/typeselection",
        name: "typeselection",
        component: () => import("../views/TypeSelection.vue"),
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/homepage"
  },
];

const router = createRouter({
  history: createWebHistory("/"),
  routes,
});

export default router;
