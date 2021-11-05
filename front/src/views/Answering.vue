<template>
  <div class="QuestionnaireEditor">
    <el-row>
      <el-col
        class="Container"
        :span="checkSpan()"
        :offset="checkOffset()"
      >

        <el-card class="box-card">
          <el-affix v-if="questionnaire.type==3" class="affix" :offset="22">
            <el-tag type="warning">
              剩余时间：{{year?(yearString+'年:'+dayString+'天:'+hourString+'时:'+minuteString+'分:'+secondString+'秒'):
              (day?(dayString+'天:'+hourString+'时:'+minuteString+'分:'+secondString+'秒'):
              (hour? hourString+'时:'+minuteString+'分:'+secondString+'秒' : minuteString+'分:'+secondString+'秒'))}}
            </el-tag>
          </el-affix>
          <div class="grid-content middle-container">
            <el-scrollbar>
              <div class="questionnaire-title">
                <h1>{{ questionnaire.name }}</h1>
                <span>{{questionnaire. introduction }}</span>
              </div>
              <div
                class="questionnaire-item"
                v-for="(item, index) in questionnaire.questions"
                :key="index"
              >
                <el-divider class="el-divider"></el-divider>
                <div>
                  <div class="question">
                    <radio
                      v-if="(item.type === 5)||(item.type === 0)"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    > </radio>
                    <checkbox
                      v-else-if="(item.type === 6)||(item.type === 1)"
                      :key="checkboxKey"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    > </checkbox>
                    <simplefill
                      v-else-if="(item.type === 3)||(item.type === 7)"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    ></simplefill>
                    <multiplefill
                      v-else-if="(item.type === 4)"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    ></multiplefill>
                    <rate
                      v-else-if="(item.type === 2)"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    ></rate>
                  </div>
                </div>
              </div>
              <div class="questionnaire-item">
                <el-divider class="el-divider"></el-divider>
                <span class="submit-button">
                  <el-button
                    class="el-button"
                    icon="el-icon-s-claim"
                    type="primary"
                    v-if="enabled==true"
                    @click="handleSubmit"
                  >
                    <span>提交</span>
                  </el-button>
                </span>
              </div>
              <div class="leave-blank"></div>
            </el-scrollbar>
          </div>

        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import "../plugins/iconfont.js";

import Radio from "../components/Questionnaire/Radio.vue";
import Checkbox from "../components/Questionnaire/Checkbox.vue";
import Simplefill from "../components/Questionnaire/Simplefill.vue";
import Multiplefill from "../components/Questionnaire/Multiplefill.vue";
import Rate from "../components/Questionnaire/Rate.vue";

export default {
  emits: ["addRadioOption"],
  components: {
    Radio,
    Checkbox,
    Simplefill,
    Multiplefill,
    Rate,
  },

  data() {
    return {
      year: '',
      day: '',
      hour: '',
      minute: '',
      second: '',
      timer: '',
      remainTime: '',
      checkboxKey: 0,
      device: "",
      selecting: -1,
      questionnaire: {
        enabled: true,
        name: "",
        introduction: "",
        startTime: "",
        endTime: "",
        isRepeat: 0,
        isLogin: 0,
        isAppoint: 0,
        isNumber: 0,
        questions: [
        ],
        type: 0,
      },
    };
  },
  async created() {
    if ((navigator.userAgent.match(/(phone|pad|pod|iPad|iPod|iPhone|ios|Android|Mobile|IEMobile|BlackBerry|JUC|Fennec|wOSBrowser|WebOS|MQQBrowser|BrowserNG|Symbian|Windows Phone)/i))) {
      this.device = "mobile"
    } else {
      this.device = "PC"
    }
    this.enabled = true;
    const { data: res } = await this.$http.post(
      `/api/admin/questionnaire/answering`, { questionnaireId: this.$route.query.questionnaireId, code: this.$route.query.code, ip: sessionStorage.getItem('ip') }
    );
    if (res.code === 0) {
      this.questionnaire = res.data;
      this.initWebSocket();
      for (let i = 0; i < this.questionnaire.questions.length; i++) {
        this.questionnaire.questions[i].order = (i + 1);
        this.questionnaire.questions[i].disabled = false;
        switch (this.questionnaire.questions[i].type) {
          case 0:
            this.questionnaire.questions[i].value = null;
            break;
          case 1:
            this.questionnaire.questions[i].value = [];
            break;
          case 2:
            this.questionnaire.questions[i].value = null;
            break;
          case 3:
            this.questionnaire.questions[i].value = null;
            break;
          case 4:
            this.questionnaire.questions[i].value = [];
            break;
          case 5:
            this.questionnaire.questions[i].value = null;
            break;
          case 6:
            this.questionnaire.questions[i].value = [];
            break;
          case 7:
            this.questionnaire.questions[i].value = null;
            break;
          default:
            break;
        }
      }
    } else { 
      this.questionnaire = {}; this.enabled = false; this.$message.error(res.data) 
    }
    this.remainTime=parseInt((new Date(this.questionnaire.endTime).getTime()-new Date().getTime())/1000)
    if (this.remainTime > 0) {
      this.year=Math.floor(this.remainTime / 3600/24/365)
      this.day = Math.floor((this.remainTime / 3600/24) % 365)
      this.hour = Math.floor((this.remainTime / 3600) % 24)
      this.minute = Math.floor((this.remainTime / 60) % 60)
      this.second = Math.floor(this.remainTime % 60)
      this.countDowm()
    }
  },
  methods: {
    checkSpan() {
      if (this.device === "mobile") {
        return 22
      } else {
        return 10
      }
    },
    checkOffset() {
      if (this.device === "mobile") {
        return 1
      } else {
        return 7
      }
    },
    async handleSubmit() {
      var submitData = { questionSubmitDTOS: [] };
      submitData.questionnaireId = this.$route.query.questionnaireId
      submitData.ip = sessionStorage.getItem('ip');
      var checkValidation = true
      this.questionnaire.questions.map((item) => {
        if (item.isCompulsory == 1) {
          switch (item.type) {
            case 6:
            case 1:
              if (item.value.length == 0) checkValidation = false//选项数大于0
              break;
            case 2:
              if (!item.value > 0) checkValidation = false//作答大于0
              break;
            case 3:
            case 7:
              if (item.value == null) checkValidation = false
              else if (item.value.trim().length == 0) checkValidation = false//做答不为空
              break;
            case 4:
              var countFill = 0
              item.value.map((subItem) => {
                countFill++;
                if (!subItem) checkValidation = false
                else if (subItem.trim().length == 0) checkValidation = false//每一个空都不为空
              })
              if (countFill != item.max) checkValidation = false//空数等于填数
              break;
            default:
              if (item.value == null) checkValidation = false
              break;
          }

        }

        if (Array.isArray(item.value)) {
          if (item.type == 1 || item.type == 6) item.value.sort()
          if (item.type == 4) {
            var count = 0;
            while (count < item.max) {
              if (item.value[count])
                submitData.questionSubmitDTOS.push({ questionId: item.questionId, content: item.value[count] })
              else submitData.questionSubmitDTOS.push({ questionId: item.questionId, content: "" })
              count++;
            }
          }
          else {

            if (item.value.length > 0) {
              item.value.map((subItem) => {
                submitData.questionSubmitDTOS.push({ questionId: item.questionId, content: subItem })
              })
            }
            else {

              submitData.questionSubmitDTOS.push({ questionId: item.questionId, content: "" })
            }

          }
        }
        else if (item.type !== 2 && item.value !== null && item.value && item.value !== undefined || item.type == 2 && item.value > 0) {

          submitData.questionSubmitDTOS.push({ questionId: item.questionId, content: item.value })
        }
        else {

          submitData.questionSubmitDTOS.push({ questionId: item.questionId, content: "" })
        }
      })
      if (checkValidation) {
        const { data: res } = await this.$http.post(
          `/api/admin/questionnaireContent`, submitData
        );

        if (res.code === 0) {
          if(this.questionnaire.type===3){
            this.questionnaire.submitRecordId=res.data;
          }
          this.$message.success("提交答卷成功");
          if (this.questionnaire.type === 1) {
            this.$router.push({
              name: 'votingresult',
              params: {
                questionnaireId: this.$route.query.questionnaireId,
              }
            })
          } else if (this.questionnaire.type === 3) {
            this.$router.push({
              name: 'examresult',
              params: {
                questionnaireId: this.$route.query.questionnaireId,
                ip: sessionStorage.getItem('ip'),
                submitRecordId: this.questionnaire.submitRecordId,
              }
            })
          }
          else {
            this.$router.push("/answercompleted")
          }

        } else {
          this.$message.error(res.data)
        }
      }
      else {
        this.$message.error("请作答完所有必答题")
      }

    },
    updateValue(params) {
      this.questionnaire.questions[params[0] - 1].value = params[1];
    },
    initWebSocket() { //初始化weosocket 
      const wsuri = "ws://localhost:8090/websocket/" + this.$route.query.questionnaireId + "&" + new Date().getTime();
      this.websock = new WebSocket(wsuri)
      this.websock.onmessage = this.websocketonmessage
      this.websock.onopen = this.websocketonopen
      this.websock.onerror = this.websocketonerror
      this.websock.onclose = this.websocketclose
    },
    websocketonopen() { //连接建立之后执行send方法发送数据
    },
    /**
     * 连接建立失败,断开连接
     * 1.查询一次数据库数据
     * 2.查询完后再次建立socket连接
     * */
    websocketonerror() {//连接建立失败重连
      //this.websock.onclose()
      this.initWebSocket()
    },
    websocketonmessage(e) { //数据接收
      if (e.data != "连接成功！") {
        let temp = JSON.parse(e.data)
        for (let i = 0; i < this.questionnaire.questions.length; i++) {
          this.questionnaire.questions[i].order = (i + 1);
          for (let j = 0; j < this.questionnaire.questions[i].choices.length; j++) {
            this.questionnaire.questions[i].choices[j].nowCount = temp.questions[i].choices[j].nowCount;
          }
        }
      }


    },
    websocketsend(Data) {//数据发送
      this.websock.send(Data)
    },
    websocketclose(e) {  //关闭
    },
    countDowm () {
      var self = this
      clearInterval(this.timer)
      this.timer = setInterval(function () {
        if(self.year===0){
          if(self.day===0){
            if (self.hour === 0) {
              if (self.minute !== 0 && self.second === 0) {
                if(self.minute===5){
                  self.$message.warning("考试剩余5分钟，注意填写必答题")
                }
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.second = 0
                self.handleSubmit()
                clearInterval(self.timer)
              } else {
                self.second -= 1
              }
            } else {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.hour -= 1
                self.minute = 59
                self.second = 59
              } else {
                self.second -= 1
              }
            }          
          } else {
            if (self.hour === 0) {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.second = 59
                self.minute =59
                self.hour =23
                self.day -= 1
              } else {
                self.second -= 1
              }
            } else {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.hour -= 1
                self.minute = 59
                self.second = 59
              } else {
                self.second -= 1
              }
            }
          }
        } else {
          if(self.day===0){
            if (self.hour === 0) {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.second = 59
                self.minute = 59
                self.hour = 23
                self.day = 364
                self.year -=1
              } else {
                self.second -= 1
              }
            } else {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.hour -= 1
                self.minute = 59
                self.second = 59
              } else {
                self.second -= 1
              }
            }          
          } else {
            if (self.hour === 0) {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.second = 59
                self.minute =59
                self.hour =23
                self.day -= 1
              } else {
                self.second -= 1
              }
            } else {
              if (self.minute !== 0 && self.second === 0) {
                self.second = 59
                self.minute -= 1
              } else if (self.minute === 0 && self.second === 0) {
                self.hour -= 1
                self.minute = 59
                self.second = 59
              } else {
                self.second -= 1
              }
            }
          }
        }
      }, 1000)
    },
    formatNum (num) {
      return num < 10 ? '0' + num : '' + num
    }
  },
  computed: {
    tagsList() {
      return this.$store.state.tagsList.map((item) => item.name);
    },
    collapse() {
      return this.$store.state.collapse;
    },
    yearString () {
      return this.formatNum(this.year)
    },
    dayString () {
      return this.formatNum(this.day)
    },
    hourString () {
      return this.formatNum(this.hour)
    },
    minuteString () {
      return this.formatNum(this.minute)
    },
    secondString () {
      return this.formatNum(this.second)
    }
  },
};
</script>

<style scoped>
.Container {
  position:relative;
}

.affix {
  width:100%;
  height: 0px
}

.affix /deep/ .el-affix--fixed {
  text-align: right;
}

.QuestionnaireEditor {
  background-color: #f0f0f0;
}

.box-card {
  margin-top: 20px;
}

.box-card /deep/ .el-card__body {
  padding: 0;
}

.middle-container {
  margin-top: 10px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  height: 100vh;
}

.questionnaire-title {
  text-align: center;
}

.questionnaire-title h1 {
  margin-top: 20px;
  margin-bottom: 10px;
}

.questionnaire-title span {
  font-size: 14px;
  color: rgba(109, 109, 109, 0.836);
}

.questionnaire-item .submit-button {
  display: block;
  text-align: center;
}

.questionnaire-item .el-button {
  font-size: 20px;
  width: 200px;
  height: 50px;
}

.question {
  margin-bottom: 20px;
}

.leave-blank {
  height: 200px;
}

.el-divider {
  padding: 0, 12%;
  height: 5px;
  width: auto;
  margin-left: 10%;
  margin-right: 10%;
  background-color: rgb(126, 127, 129, 0.24);
}
</style>
