import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";
export default createStore({
  state: {
    username: "未登录",
    lists: [],
    fakeId: "-1",
  },
  mutations: {
    login(state, username) {
      state.islogin = true;
      state.username = username;
    },
    changeName(state, username) {
      state.username = username;
    },
    loginout(state) {
      state.islogin = false;
      state.username = "未登录";
      state.user_id = "";
      state.tagsList = [];
    },
    setList(state, obj) {
      state.lists[obj.id] = obj.questionnaire;
    },
    deleteList(state, id) {
      state.lists[id] = {};
    },
    changeFakeId(state) {
      state.fakeId = (parseInt(state.fakeId) - 1).toString();
    },
  },
  actions: {},
  modules: {},
  plugins: [createPersistedState()],
});
