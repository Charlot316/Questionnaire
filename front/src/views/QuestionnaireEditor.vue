<template>
  <div class="QuestionnaireEditor">
    <el-row :gutter="20">
      <el-col
        :span="6"
        :offset="1"
      >
        <div v-if="preview==0">
          <el-card class="box-card">
            <div class="grid-content" :class="checkType()">
              <el-tabs stretch="true" v-model="activeName">
                <el-tab-pane label="题型选择" name="first">
                  <div>
                    <el-row :gutter="10">
                      <el-col
                        class="xuanti-container"
                        :span="10"
                        :offset="2"
                      >
                        <div
                          class="xuanti"
                          @click="addNewQuestion('radio')"
                        >
                          <svg
                            class="icon"
                            aria-hidden="true"
                          >
                            <use xlink:href="#icon-danxuanti"></use>
                          </svg>
                          <span v-if="questionnaire.type==1">
                            投票单选题
                          </span>
                          <span v-else>
                            单选题
                          </span>
                        </div>
                      </el-col>
                      <el-col
                        class="xuanti-container"
                        :span="10"
                        :offset="2"
                      >
                        <div
                          class="xuanti"
                          @click="addNewQuestion('checkbox')"
                        >
                          <svg
                            class="icon"
                            aria-hidden="true"
                          >
                            <use xlink:href="#icon-duoxuanti"></use>
                          </svg>
                          <span v-if="questionnaire.type==1">
                            投票多选题
                          </span>
                          <span v-else>
                            多选题
                          </span>
                        </div>
                      </el-col>
                    </el-row>
                    <el-row :gutter="10">
                      <el-col
                        class="xuanti-container"
                        :span="10"
                        :offset="2"
                      >
                        <div
                          class="xuanti"
                          @click="addNewQuestion('simplefill')"
                        >
                          <svg
                            class="icon"
                            aria-hidden="true"
                          >
                            <use xlink:href="#icon-danxiangtiankongti"></use>
                          </svg>
                          <span>
                            单项填空题
                          </span>
                        </div>
                      </el-col>
                      <el-col
                        class="xuanti-container"
                        :span="10"
                        :offset="2"
                      >
                        <div
                          class="xuanti"
                          @click="addNewQuestion('multiplefill')"
                        >
                          <svg
                            class="icon"
                            aria-hidden="true"
                          >
                            <use xlink:href="#icon-duoxiangtiankongti"></use>
                          </svg>
                          <span>
                            多项填空题
                          </span>
                        </div>
                      </el-col>
                    </el-row>
                    <el-row :gutter="10">
                      <el-col
                        class="xuanti-container"
                        :span="10"
                        :offset="2"
                        v-if="questionnaire.type!=3"
                      >
                        <div
                          class="xuanti"
                          @click="addNewQuestion('rate')"
                        >
                          <svg
                            class="icon"
                            aria-hidden="true"
                          >
                            <use xlink:href="#icon-pingfenti"></use>
                          </svg>
                          <span>
                            评分题
                          </span>
                        </div>
                      </el-col>
                      <el-col
                        class="xuanti-container"
                        :span="10"
                        :offset="2"
                        v-if="questionnaire.type==4"
                      >
                        <div
                          class="xuanti"
                          @click="addNewQuestion('position')"
                        >
                          <svg
                            class="icon"
                            aria-hidden="true"
                          >
                            <use xlink:href="#icon-diyu"></use>
                          </svg>
                          <span>
                            定位
                          </span>
                        </div>
                      </el-col>
                    </el-row>                
                  </div>
                </el-tab-pane>
                <el-tab-pane label="问卷大纲" name="second">
                  <el-scrollbar>
                    <transition-group tag="div" class="">

                      <div v-for="(item,index) in questionnaire.questions" :key="index"
                      :class="checkIndex(index)"
                      draggable="true"
                      @dragstart="handleDragStart($event, item)"
                      @dragover.prevent="handleDragOver($event, item)"
                      @dragenter="handleDragEnter($event, item)"
                      @dragend="handleDragEnd($event, item)" >
                      <el-row class="dagang">
                        <el-col :span="2" :offset="2">
                          <h3>
                            <font  v-if="questionnaire.isNumber===0">{{ item.order }}.</font>
                          </h3>
                        </el-col>
                        <el-col :span="18">
                          <h3>
                            {{ item.content }}<font v-if="item.isCompulsory===1">*</font>
                          </h3>
                        </el-col>
                      </el-row>
                    </div>  

                    </transition-group>
                                      
                  </el-scrollbar>

                </el-tab-pane>
              </el-tabs>

            </div>
          </el-card>
          <div class="setting">
            <el-row>
              <el-col
                :span="12"
                style="text-align:center"
              >
                <el-button
                  class="el-button"
                  icon="el-icon-setting"
                  type="success"
                  @click="showSettings"
                >
                  <span>设置</span>
                </el-button>
              </el-col>
              <el-col
                :span="12"
                style="text-align:center"
              >
                <el-button
                  class="el-button"
                  icon="el-icon-circle-check"
                  type="success"
                  @click="handleSave(1)"
                >
                  <span>保存</span>
                </el-button>
              </el-col>
            </el-row>
            <el-row class="el-row">
              <el-col
                :span="12"
                style="text-align:center"
              >
                <el-button
                  class="el-button"
                  icon="el-icon-view"
                  type="success"
                  @click="handlePreview"
                >
                  <span>预览</span>
                </el-button>
              </el-col>
              <el-col
                :span="12"
                style="text-align:center"
              >
                <el-button
                  class="el-button"
                  icon="el-icon-document-checked"
                  type="success"
                  @click="handleSaveAndPublish"
                >
                  <span>保存并发布</span>
                </el-button>
              </el-col>
            </el-row>
          </div>        
        </div>
        <div v-else-if="preview==1">
          <div class="setting">
            <el-row class="el-row">
              <el-col
                :span="12"
                style="text-align:center"
              >
                <el-button
                  class="el-button"
                  icon="el-icon-view"
                  type="success"
                  @click="handlePreview"
                >
                  <span>返回编辑界面</span>
                </el-button>
              </el-col>
              <el-col
                :span="12"
                style="text-align:center"
              >
                <el-button
                  class="el-button"
                  icon="el-icon-upload2"
                  type="success"
                  @click="handleExport"
                >
                  <span>导出预览问卷</span>
                </el-button>
              </el-col>
            </el-row>
          </div>        
        </div>
      </el-col>
      <el-col :span="10" >
        <el-card class="box-card">
          <div class=" grid-content middle-container">
            <el-scrollbar ><!--要导出预览的标签-->
            <div id="pdfDom">
              <div
                class="questionnaire-title"
                :class="checkSelecting(-1)"
                @click="selected(-1)"
                
              >
                <div
                  class="name"
                  :class="checkName()"
                >
                  <h1>{{ questionnaire.name }}</h1>
                </div>
                <div>
                  <span>{{questionnaire.introduction }}</span>
                </div>
              </div>

              <transition-group tag="div" class="">

                <div
                class="questionnaire-item"
                v-for="(item, index) in questionnaire.questions"
                :key="index"
                draggable="true"
                @dragstart="handleDragStart($event, item)"
                @dragover.prevent="handleDragOver($event, item)"
                @dragenter="handleDragEnter($event, item)"
                @dragend="handleDragEnd($event, item)" 
              >
                <el-divider class="el-divider"></el-divider>
                <div :class="checkSelecting(index)">
                  <div
                    class="question"
                    @click="selected(index)"
                  >
                    <radio
                      v-if="(item.type === 0)||(item.type === 5)"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    > </radio>
                    <checkbox
                      v-else-if="(item.type === 1)||(item.type === 6)"
                      :="questionnaire.questions[index]"
                      :bigType="questionnaire.type"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    > </checkbox>
                    <simplefill
                      v-else-if="(item.type === 3)||(item.type === 7)"
                      :="questionnaire.questions[index]"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    ></simplefill>
                    <multiplefill
                      v-else-if="(item.type === 4)"
                      :="questionnaire.questions[index]"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    ></multiplefill>
                    <rate
                      v-else-if="(item.type === 2)"
                      :="questionnaire.questions[index]"
                      :isNumber="questionnaire.isNumber"
                      @updateValue="updateValue"
                    ></rate>
                  </div>
                  <div>
                    <el-row v-if="(preview==0)&&(index>=questionnaire.numberBefore)">
                      <el-col
                        :span="1"
                        :offset="4"
                      >
                        <i
                          class="el-icon-document-copy"
                          @click="copyQuestion(index)"
                        ></i>
                      </el-col>
                      <el-col
                        :span="1"
                        :offset="2"
                      >
                        <i
                          class="el-icon-delete"
                          @click="deleteQuestion(index)"
                        ></i>
                      </el-col>
                      <el-col
                        :span="1"
                        :offset="2"
                      >
                        <i
                          class="el-icon-sort-down"
                          @click="sortdownQuestion(index)"
                        ></i>
                      </el-col>
                      <el-col
                        :span="1"
                        :offset="2"
                      >
                        <i
                          class="el-icon-sort-up"
                          @click="sortupQuestion(index)"
                        ></i>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </div>

              </transition-group>
              <div class="questionnaire-item">
                <el-divider class="el-divider"></el-divider>
                <span class="submit-button">
                  <el-button
                    class="el-button"
                    icon="el-icon-s-claim"
                    type="primary"
                  >
                    <span>提交</span>
                  </el-button>
                </span>
              </div>
            </div>
              
              <div class="leave-blank"></div>
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card v-if="preview==0" class="box-card">
          <div class="grid-content right-container">
            <el-scrollbar>
              <setting
                v-if="selecting===-1"
                :bigType="questionnaire.type"
                :isOrder="questionnaire.isOrder"
                :startTime="questionnaire.startTime"
                :endTime="questionnaire.endTime"
                :isRepeat="questionnaire.isRepeat"
                :isLogin="questionnaire.isLogin"
                :isNumber="questionnaire.isNumber"
                :isLimit="questionnaire.isLimit"
                :allCount="questionnaire.allCount"
                :allCountBefore="questionnaire.allCountBefore"
                @updateTheisRepeat="updateTheisRepeat"
                @updateTheisLogin="updateTheisLogin"
                @updateThestartTime="updateThestartTime"
                @updateTheendTime="updateTheendTime"
                @updateTheisNumber="updateTheisNumber"
                @updateTheisLimit="updateTheisLimit"
                @updateTheallCount="updateTheallCount"
                @updateTheisOrder="updateTheisOrder"
              ></setting>
              <title-editor
                v-else-if="selecting===0"
                :name="questionnaire.name"
                :introduction="questionnaire.introduction"
                @updateTheName="updateTheName"
                @updateTheIntroduction="updateTheIntroduction"
              ></title-editor>
              <radio-editor
                v-else-if="(questionnaire.questions[(selecting-1)].type===0)||
                (questionnaire.questions[(selecting-1)].type===5)"
                :="questionnaire.questions[(selecting-1)]"
                :bigType="questionnaire.type"
                :enterType="enterType"
                @addOption="addOption"
                @deleteOption="deleteOption"
                @updateContent="updateContent"
                @updateIntroduction="updateIntroduction"
                @updateisCompulsory="updateisCompulsory"
                @updateisShowVotes="updateisShowVotes"
                @updateisShowPercentage="updateisShowPercentage"
                @updatescore="updatescore"
                @updateisAnswer="updateisAnswer"
                @updateanalysis="updateanalysis"
              ></radio-editor>
              <checkbox-editor
                v-else-if="(questionnaire.questions[(selecting-1)].type===1)||
                (questionnaire.questions[(selecting-1)].type===6)"
                :="questionnaire.questions[(selecting-1)]"
                :bigType="questionnaire.type"
                :enterType="enterType"
                @addOption="addOption"
                @deleteOption="deleteOption"
                @updateContent="updateContent"
                @updateIntroduction="updateIntroduction"
                @updateisCompulsory="updateisCompulsory"
                @updateisShowVotes="updateisShowVotes"
                @updateisShowPercentage="updateisShowPercentage"
                @updatescore="updatescore"
                @updateisAnswer="updateisAnswer"
                @updateanalysis="updateanalysis"
              ></checkbox-editor>
              <simplefill-editor
                v-else-if="questionnaire.questions[(selecting-1)].type===3||
                (questionnaire.questions[(selecting-1)].type===7)"
                :="questionnaire.questions[(selecting-1)]"
                :bigType="questionnaire.type"
                :enterType="enterType"
                @updateContent="updateContent"
                @updateIntroduction="updateIntroduction"
                @updateisCompulsory="updateisCompulsory"
                @updateanalysis="updateanalysis"
                @updatescore="updatescore"
                @updateanswer="updateanswer"
              ></simplefill-editor>
              <multiplefill-editor
                v-else-if="questionnaire.questions[(selecting-1)].type===4"
                :="questionnaire.questions[(selecting-1)]"
                :bigType="questionnaire.type"
                :enterType="enterType"
                @updateContent="updateContent"
                @updateIntroduction="updateIntroduction"
                @updateisCompulsory="updateisCompulsory"
                @updateanalysis="updateanalysis"
                @updatescore="updatescore"
                @updateanswer="updateanswer"
              ></multiplefill-editor>
              <rate-editor
                v-else-if="questionnaire.questions[(selecting-1)].type===2"
                :="questionnaire.questions[(selecting-1)]"
                :bigType="questionnaire.type"
                :enterType="enterType"
                @updateContent="updateContent"
                @updateIntroduction="updateIntroduction"
                @updateMax="updateMax"
                @updateisCompulsory="updateisCompulsory"
                @updateTexts="updateTexts"
              ></rate-editor>
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import "../plugins/iconfont.js";

import TitleEditor from "../components/QuestionnaireEditor/TitleEditor.vue"
import Setting from "../components/QuestionnaireEditor/Setting.vue"

import Radio from "../components/Questionnaire/Radio.vue";
import Checkbox from "../components/Questionnaire/Checkbox.vue";
import Simplefill from "../components/Questionnaire/Simplefill.vue";
import Multiplefill from "../components/Questionnaire/Multiplefill.vue";
import Rate from "../components/Questionnaire/Rate.vue";

import RadioEditor from "../components/QuestionnaireEditor/RadioEditor.vue";
import CheckboxEditor from "../components/QuestionnaireEditor/CheckboxEditor.vue";
import SimplefillEditor from "../components/QuestionnaireEditor/SimplefillEditor.vue"
import MultiplefillEditor from "../components/QuestionnaireEditor/MultiplefillEditor.vue"
import RateEditor from "../components/QuestionnaireEditor/RateEditor.vue"

export default {
  emits: ["addRadioOption"],
  components: {
    Radio,
    RadioEditor,
    Checkbox,
    CheckboxEditor,
    Simplefill,
    SimplefillEditor,
    Multiplefill,
    MultiplefillEditor,
    Rate,
    RateEditor,
    TitleEditor,
    Setting,
  },

  data() {
    return {
      activeName: "first",
      preview: 0,
      selecting: -1,
      enterType: undefined,
      questionnaireId: undefined,
      questionnaire: {
        name: "",
        introduction: "",
        startTime: "",
        endTime: "",
        isRepeat: 0,
        isLogin: 0,
        isAppoint: 0,
        isNumber: 0,
        isLimit: 0,
        allCount: 100,
        isOrder: 0,
        questions: [
        ],
        type: 0,
        numberBefore: 0,
      },
    };
  },
  async created() {
    this.enterType = this.$route.query.enterType
    this.questionnaireId = this.$route.query.questionnaireId



    if (this.enterType == 1) {
      this.questionnaireId = "f"+this.$store.state.fakeId
      this.$store.commit("changeFakeId")
      this.questionnaire = {
        name: this.$route.query.name,
        introduction: "问卷备注",
        startTime: "",
        endTime: "",
        isRepeat: 1,
        isLogin: 1,
        isAppoint: 0,
        isNumber: 0,
        isLimit: 0,
        allCount: 100,
        isOrder: 0,
        questions: [
        ],
        type: this.$route.query.type,
        numberBefore: 0,
      };
      if(this.questionnaire.type==1){
        this.questionnaire.questions=[
          {
            order: 1,
            type: 5,
            content: "投票单选标题",
            isCompulsory: 1,
            introduction: "投票单选备注",
            choices: [
              {
                choiceId: 1,
                choiceContent: "选项1",
                isLimit: 0,
                allCount: 10,
                votes: 0,
                percentage: 0,
              },
              {
                choiceId: 2,
                choiceContent: "选项2",
                isLimit: 0,
                allCount: 10,
                votes: 0,
                percentage: 0,
              },
              {
                choiceId: 3,
                choiceContent: "选项3",
                isLimit: 0,
                allCount: 10,
                votes: 0,
                percentage: 0,
              },
              {
                choiceId: 4,
                choiceContent: "选项4",
                isLimit: 0,
                allCount: 10,
                votes: 0,
                percentage: 0,
              },
            ],
            value: null,
            disabled: true,
            isShowVotes: 1,
            isShowPercentage: 1,
          }
        ]
      } else if(this.questionnaire.type==2){
        this.questionnaire.questions=[
          {
            order: 1,
            type: 3,
            content: "姓名",
            isCompulsory: 1,
            introduction: "",
            value: null,
            disabled: true,
          },
          {
            order: 2,
            type: 3,
            content: "手机号",
            isCompulsory: 1,
            introduction: "",
            value: null,
            disabled: true,
          },
          {
            order: 3,
            type: 0,
            content: "报名单选标题",
            isCompulsory: 1,
            introduction: "报名单选备注",
            choices: [
              {
                choiceId: 1,
                choiceContent: "选项1",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 2,
                choiceContent: "选项2",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 3,
                choiceContent: "选项3",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 4,
                choiceContent: "选项4",
                isLimit: 0,
                allCount: 10,
              },
            ],
            value: null,
            disabled: true,
          }
        ]
      } else if(this.questionnaire.type==4){
        this.questionnaire.questions=[
          {
            order: 1,
            type: 3,
            content: "学号",
            isCompulsory: 1,
            introduction: "",
            value: null,
            disabled: true,
          },
          {
            order: 2,
            type: 3,
            content: "姓名",
            isCompulsory: 1,
            introduction: "",
            value: null,
            disabled: true,
          },
          {
            order: 3,
            type: 0,
            content: "当前体温",
            isCompulsory: 1,
            introduction: "",
            choices: [
              {
                choiceId: 1,
                choiceContent: "35.0°C以下",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 2,
                choiceContent: "35°C~35.5°C",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 3,
                choiceContent: "35.5°C~36.0°C",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 4,
                choiceContent: "36.0°C~36.5°C",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 5,
                choiceContent: "36.5°C~37.0°C",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 6,
                choiceContent: "37.0°C~37.5°C",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 7,
                choiceContent: "37.5°C~38.0°C",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 8,
                choiceContent: "38.0°C以上",
                isLimit: 0,
                allCount: 10,
              },
            ],
            value: null,
            disabled: true,
          },
          {
            order: 4,
            type: 0,
            content: "近14日内，是否去过高危风险地区",
            isCompulsory: 1,
            introduction: "高危风险地区包括：......",
            choices: [
              {
                choiceId: 1,
                choiceContent: "是",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 2,
                choiceContent: "否",
                isLimit: 0,
                allCount: 10,
              },
            ],
            value: null,
            disabled: true,
          },
          {
            order: 5,
            type: 0,
            content: "是否有新冠症状",
            isCompulsory: 1,
            introduction: "症状包括：......",
            choices: [
              {
                choiceId: 1,
                choiceContent: "是",
                isLimit: 0,
                allCount: 10,
              },
              {
                choiceId: 2,
                choiceContent: "否",
                isLimit: 0,
                allCount: 10,
              },
            ],
            value: null,
            disabled: true,
          },
          {
            order: 6,
            type: 7,
            content: "所在地址",
            isCompulsory: 1,
            introduction: "",
            value: null,
            disabled: true,
          },
        ]
      }else if(this.questionnaire.type==3){
        this.questionnaire.questions=[
          {
            order: 1,
            type: 3,
            content: "学号",
            isCompulsory: 0,
            introduction: "",
            score: 0,
            value: null,
            disabled: true,
          },
          {
            order: 2,
            type: 3,
            content: "姓名",
            isCompulsory: 0,
            introduction: "",
            score: 0,
            value: null,
            disabled: true,
          },
        ]
      }
    } else {
      const { data: res } = await this.$http.get(
        `/api/admin/questionnaireContent?questionnaireId=${this.questionnaireId}`
      );
      if (res.code === 0) {
        this.questionnaire = res.data;
        if (this.enterType == 4) {
          this.questionnaire.numberBefore = this.questionnaire.questions.length;
        } else {
          this.questionnaire.numberBefore = 0;
          if (this.enterType == 3) {
            this.questionnaire.name += "(副本)"
          }
        }
        for (let i = 0; i < this.questionnaire.questions.length; i++) {
          this.questionnaire.questions[i].order = (i + 1);
          this.questionnaire.questions[i].disabled = true;
          switch (this.questionnaire.questions[i].type) {
            case 0:
              this.questionnaire.questions[i].value = null;
              if(this.questionnaire.type==3){
                for(let j = 0;j<this.questionnaire.questions[i].choices.length;j++){
                  if(this.questionnaire.questions[i].choices[j].isAnswer){
                    this.questionnaire.questions[i].fanswer=j;
                    break;
                  }
                }
              }
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;               
              }
              break;
            case 1:
              this.questionnaire.questions[i].value = [];
              if(this.questionnaire.type==3){
                this.questionnaire.questions[i].fanswer=[];
                for(let j = 0;j<this.questionnaire.questions[i].choices.length;j++){
                  if(this.questionnaire.questions[i].choices[j].isAnswer){
                    this.questionnaire.questions[i].fanswer.push(j);
                  }
                }
              }
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            case 2:
              this.questionnaire.questions[i].value = null;
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].max;
              }
              break;
            case 3:
              this.questionnaire.questions[i].value = null;
              break;
            case 4:
              this.questionnaire.questions[i].value = [];
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].max;
              }
              break;
            case 5:
              this.questionnaire.questions[i].value = null;
              for(let j = 0;j<this.questionnaire.questions[i].choices.length;j++){
                this.questionnaire.questions[i].choices[j].votes=0;
                this.questionnaire.questions[i].choices[j].percentage=0;
              }
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            case 6:
              this.questionnaire.questions[i].value = [];
              for(let j = 0;j<this.questionnaire.questions[i].choices.length;j++){
                this.questionnaire.questions[i].choices[j].votes=0;
                this.questionnaire.questions[i].choices[j].percentage=0;
              }
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            default:
              break;
          }
        }
      } else { this.$message.error("获取问卷内容失败") }

    }
  },
  methods: {
    handlePreview() {
      if(this.preview==0){
        this.preview=1;
        this.selecting=-1;
        for(let i=0;i<this.questionnaire.questions.length;i++){
          this.questionnaire.questions[i].disabled = false;
        }
      }
      else if(this.preview==1){
        this.preview=0;
        for (let i = 0; i < this.questionnaire.questions.length; i++) {
          this.questionnaire.questions[i].disabled = true;
          switch (this.questionnaire.questions[i].type) {
            case 0:
              this.questionnaire.questions[i].value = null;
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            case 1:
              this.questionnaire.questions[i].value = [];
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            case 2:
              this.questionnaire.questions[i].value = null;
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].max;
              }
              break;
            case 3:
              this.questionnaire.questions[i].value = null;
              break;
            case 4:
              this.questionnaire.questions[i].value = [];
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].max;
              }
              break;
            case 5:
              this.questionnaire.questions[i].value = null;
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            case 6:
              this.questionnaire.questions[i].value = [];
              if (this.enterType == 4) {
                this.questionnaire.questions[i].answerCountBefore = this.questionnaire.questions[i].choices.length;
              }
              break;
            case 7:
              this.questionnaire.questions[i].value = null;
              break;
            default:
              break;
          }
        }
      }
      // this.$store.commit("setList", { id: this.questionnaireId, questionnaire: this.questionnaire })

      // this.$router.push({ path: '/preview', query: { questionnaireId: this.questionnaireId } })
    },
    async handleSaveAndPublish() {
      var judge = await this.handleSave(2)
      if (judge == true) {
        const { data: res } = await this.$http.patch(
          `/api/admin/publish`, { questionnaireId: this.questionnaireId }
        );
        if (res.code === 0) {
          this.$message.success("保存并发布问卷成功")
          this.$router.push({ path: '/manage' })
        }
        else {
          this.$message.error("发布问卷失败")
        }
      }
    },
    async handleSave(index) {
      if (this.questionnaire.name === "") {
        this.$message.error("问卷标题不可为空");
        return false;
      }
      for (let i = 0; i < this.questionnaire.questions.length; i++) {
        if (this.questionnaire.questions[i].content === "") {
          this.$message.error("所有题目标题均不可为空");
          return false;
        }
      }
      if (this.enterType == 1) { //新建问卷
        const { data: res } = await this.$http.post(
          `/api/admin/questionnaire`, this.questionnaire
        );
        if (res.code === 0) {
          if (index == 1) this.$message.success("保存成功");
          this.questionnaireId = res.data
          this.enterType=2
          return true;
        } else {
          this.$message.error("保存失败")
          return false;
        }
      }
      else {
        this.questionnaire.id = this.questionnaireId
        if (this.enterType == 2) {
          const { data: res } = await this.$http.post(//编辑问卷删除答卷
            `/api/admin/editQuestionnaireDelete`, this.questionnaire
          );
          if (res.code === 0) {
            if (index == 1) this.$message.success("保存成功");
            this.questionnaireId = res.data
            return true;
          } else {
            this.$message.error("保存失败")
            return false;
          }
        }

        else if (this.enterType == 3) {//复制问卷编辑
          const { data: res } = await this.$http.post(
            `/api/admin/editQuestionnaireCopy`, this.questionnaire
          );
          if (res.code === 0) {
            if (index == 1) this.$message.success("保存成功");
            this.questionnaireId = res.data
            return true;
          } else {
            this.$message.error("保存失败")
            return false;
          }
        }
        else if (this.enterType == 4) {//编辑问卷保留答卷
          const { data: res } = await this.$http.post(
            `/api/admin/editQuestionnaireSave`, this.questionnaire
          );
          if (res.code === 0) {
            if (index == 1) this.$message.success("保存成功")
            this.questionnaireId = res.data
            return true;
          } else {
            this.$message.error("保存失败")
            return false;
          }
        }
      }
    },
    showSettings() {
      this.selecting = -1;
    },
    selected(index) {
      if(this.preview==0){
        this.selecting = (index + 1);
      }
    },
    checkSelecting(index) {
      if (this.selecting === (index + 1))
        return "selected"
    },
    addNewQuestion(type) {
      let index;
      if (this.selecting === -1) {
        index = this.questionnaire.questions.length;
      } else {
        index = this.selecting;
      }
      if (this.enterType == 4 && index < this.questionnaire.numberBefore) {
        this.$message.error("该模式不可将新问题插入原问题间")
        return false;
      }
      switch (type) {
        case 'radio':
          this.questionnaire.questions.splice(index, 0,
            {
              order: (index + 1),
              type: 0,
              content: "单选标题",
              isCompulsory: 1,
              introduction: "单选备注",
              choices: [
                {
                  choiceId: 1,
                  choiceContent: "选项1",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,

                },
                {
                  choiceId: 2,
                  choiceContent: "选项2",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
                {
                  choiceId: 3,
                  choiceContent: "选项3",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
                {
                  choiceId: 4,
                  choiceContent: "选项4",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
              ],
              value: null,
              disabled: true,
            }
          );
          if(this.questionnaire.type==1){
            this.questionnaire.questions[index].type=5;
            this.questionnaire.questions[index].content="投票单选标题";
            this.questionnaire.questions[index].introduction="投票单选备注";
            this.questionnaire.questions[index].isShowVotes=1;
            this.questionnaire.questions[index].isShowPercentage=1;
            for(let i = 0;i<this.questionnaire.questions[index].choices.length;i++){
              this.questionnaire.questions[index].choices[i].votes=0;
              this.questionnaire.questions[index].choices[i].percentage=0;
            }
          } else if(this.questionnaire.type==3){
            this.questionnaire.questions[index].isCompulsory=0;
            this.questionnaire.questions[index].score=5;
            this.questionnaire.questions[index].fanswer=0;
            this.questionnaire.questions[index].choices[0].isAnswer=1;
            for(let i = 1;i<this.questionnaire.questions[index].choices.length;i++){
              this.questionnaire.questions[index].choices[i].isAnswer=0;
            }
          }
          break;
        case 'checkbox':
          this.questionnaire.questions.splice(index, 0,
            {
              order: (index + 1),
              type: 1,
              content: "多选标题",
              isCompulsory: 1,
              introduction: "多选备注",
              choices: [
                {
                  choiceId: 1,
                  choiceContent: "选项1",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
                {
                  choiceId: 2,
                  choiceContent: "选项2",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
                {
                  choiceId: 3,
                  choiceContent: "选项3",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
                {
                  choiceId: 4,
                  choiceContent: "选项4",
                  isLimit: 0,
                  allCount: 10,
                  nowCount: 10,
                },
              ],
              value: [],
              disabled: true,
            }
          );
          if(this.questionnaire.type==1){
            this.questionnaire.questions[index].type=6;
            this.questionnaire.questions[index].content="投票多选标题";
            this.questionnaire.questions[index].introduction="投票多选备注";
            this.questionnaire.questions[index].isShowVotes=1;
            this.questionnaire.questions[index].isShowPercentage=1;
            for(let i = 0;i<this.questionnaire.questions[index].choices.length;i++){
              this.questionnaire.questions[index].choices[i].votes=0;
              this.questionnaire.questions[index].choices[i].percentage=0;
            }
          }else if(this.questionnaire.type==3){
            this.questionnaire.questions[index].isCompulsory=0;
            this.questionnaire.questions[index].score=5;
            this.questionnaire.questions[index].fanswer=[0];
            this.questionnaire.questions[index].choices[0].isAnswer=1;
            for(let i = 1;i<this.questionnaire.questions[index].choices.length;i++){
              this.questionnaire.questions[index].choices[i].isAnswer=0;
            }
          }
          break;
        case 'simplefill':
          this.questionnaire.questions.splice(index, 0,
            {
              order: (index + 1),
              type: 3,
              content: "单项填空",
              isCompulsory: 1,
              introduction: "单项填空备注",
              value: null,
              disabled: true,
            }
          );
          if(this.questionnaire.type==3){
            this.questionnaire.questions[index].isCompulsory=0;
            this.questionnaire.questions[index].score=5;
            this.questionnaire.questions[index].answer="";
          }
          break;
        case 'multiplefill':
          this.questionnaire.questions.splice(index, 0,
            {
              order: (index + 1),
              type: 4,
              content: "多项填空",
              isCompulsory: 1,
              introduction: "题目___备注___(连续三个以上下划线自动生成填空)",
              max: 2,
              value: [],
              disabled: true,
            }
          );
          if(this.questionnaire.type==3){
            this.questionnaire.questions[index].isCompulsory=0;
            this.questionnaire.questions[index].score=5;
            this.questionnaire.questions[index].answer="";
          }
          break;
        case 'rate':
          this.questionnaire.questions.splice(index, 0,
            {
              order: (index + 1),
              type: 2,
              content: "评分题",
              isCompulsory: 1,
              introduction: "评分题备注",
              max: 5,
              texts: [
                "1",
                "2",
                "3",
                "4",
                "5"
              ],
              value: null,
              disabled: true,
            });
          break;
        case 'position':
          this.questionnaire.questions.splice(index, 0,
            {
              order: (index + 1),
              type: 7,
              content: "所在地址",
              isCompulsory: 1,
              introduction: "定位备注",
              value: null,
              disabled: true,
            });
          break;
      }
      for (let i = 0; i < this.questionnaire.questions.length; i++) {
        this.questionnaire.questions[i].order = (i + 1)
      }
    },
    copyQuestion(index) {
      let temp = JSON.parse(JSON.stringify(this.questionnaire.questions[index]));
      this.questionnaire.questions.splice((index + 1), 0, temp);
      for (let i = 0; i < this.questionnaire.questions.length; i++) {
        this.questionnaire.questions[i].order = (i + 1)
      }
      this.selecting = (index + 2);
    },
    sortdownQuestion(index) {
      if (index === (this.questionnaire.questions.length - 1)) {
        this.$message.error("该问题在可编辑问题中为最后一题，无法下移")
      } else {
        [this.questionnaire.questions[index], this.questionnaire.questions[index + 1]] = [this.questionnaire.questions[index + 1], this.questionnaire.questions[index]];
        [this.questionnaire.questions[index].order, this.questionnaire.questions[index + 1].order] = [this.questionnaire.questions[index + 1].order, this.questionnaire.questions[index].order];
        this.selecting = (index + 2);
      }
    },
    sortupQuestion(index) {
      if (index - this.questionnaire.numberBefore === 0) {
        this.$message.error("该问题在可编辑问题中为第一题，无法上移");
      } else {
        [this.questionnaire.questions[index], this.questionnaire.questions[index - 1]] = [this.questionnaire.questions[index - 1], this.questionnaire.questions[index]];
        [this.questionnaire.questions[index].order, this.questionnaire.questions[index - 1].order] = [this.questionnaire.questions[index - 1].order, this.questionnaire.questions[index].order];
        this.selecting = index;
      }
    },
    deleteQuestion(index) {
      this.questionnaire.questions.splice(index, 1);
      for (let i = 0; i < this.questionnaire.questions.length; i++) {
        this.questionnaire.questions[i].order = (i + 1)
      }
      if (index === (this.selecting - 1)) {
        this.selecting = -1;
      } else {
        if (index < (this.selecting - 1)) {
          this.selecting--;
        }
      }
    },
    addOption(order) {
      this.questionnaire.questions[order - 1].choices.push({
        choiceId: this.questionnaire.questions[order - 1].choices.length + 1,
        choiceContent: "选项" + (this.questionnaire.questions[order - 1].choices.length + 1).toString(),
        isLimit: 0,
      })
      if(this.questionnaire.type==1){
        this.questionnaire.questions[order-1].choices[this.questionnaire.questions[order-1].choices.length-1].votes=0
        this.questionnaire.questions[order-1].choices[this.questionnaire.questions[order-1].choices.length-1].percentage=0
      }
    },
    deleteOption(params) {
      if (this.enterType == 4 && params[1] < this.questionnaire.questions[params[0] - 1].answerCountBefore) {
        this.$message.error("不可删除原有选项");
        return false;
      }
      if (this.questionnaire.questions[params[0] - 1].choices.length === 1) {
        this.$message.error("选择题至少需要一个选项");
      } else {
        if(this.questionnaire.type==3&&params[2]==0&&this.questionnaire.questions[params[0]-1].fanswer===params[1]){
          this.questionnaire.questions[params[0]-1].fanswer=0;
          this.questionnaire.questions[params[0]-1].choices[0].isAnswer=1;
        } else if(this.questionnaire.type==3&&params[2]==1){
          let temp=this.questionnaire.questions[params[0]-1].fanswer.indexOf(params[1]);
          if(temp!=-1){
            this.questionnaire.questions[params[0]-1].fanswer.splice(temp,1);
          }
          for(let i = 0;i<=this.questionnaire.questions[params[0]-1].choices.length;i++){
            let j=this.questionnaire.questions[params[0]-1].fanswer.indexOf(i);
            if(j!=-1&&i>params[1]){
              this.questionnaire.questions[params[0]-1].fanswer[j]-=1;
            }
          }
        }        
        this.questionnaire.questions[params[0] - 1].choices.splice(params[1], 1);
      }

    },
    updateContent(params) {
      if (params[1] === "") {
        this.$message.warning("题目标题不可为空");
      }
      this.questionnaire.questions[params[0] - 1].content = params[1];
    },
    updateIntroduction(params) {
      if (this.questionnaire.questions[params[0] - 1].type === 4) {
        this.questionnaire.questions[params[0] - 1].max = params[2];
      }
      this.questionnaire.questions[params[0] - 1].introduction = params[1];
    },
    updateanalysis(params) {
      this.questionnaire.questions[params[0] - 1].analysis = params[1];
    },
    updateisCompulsory(params) {
      this.questionnaire.questions[params[0] - 1].isCompulsory = params[1];
    },
    updateisShowVotes(params){
      this.questionnaire.questions[params[0] - 1].isShowVotes = params[1];
    },
    updateisShowPercentage(params){
      this.questionnaire.questions[params[0] - 1].isShowPercentage = params[1];
    },
    updatescore(params){
      this.questionnaire.questions[params[0] - 1].score = params[1];
    },
    updateanswer(params){
      this.questionnaire.questions[params[0] - 1].answer = params[1];
    },
    updateisAnswer(params){
      if(params[1]==0){
        for(let i=0;i<this.questionnaire.questions[params[0] - 1].choices.length;i++){
          if(i!=params[2]){
            this.questionnaire.questions[params[0] - 1].choices[i].isAnswer=0;
          } else {
            this.questionnaire.questions[params[0] - 1].choices[i].isAnswer=1;
          }
        }
        this.questionnaire.questions[params[0]-1].fanswer=params[2];        
      } else if(params[1]==1){
        for(let i=0;i<this.questionnaire.questions[params[0] - 1].choices.length;i++){
          if(params[2].indexOf(i)==-1){
            this.questionnaire.questions[params[0] - 1].choices[i].isAnswer=0;
          } else {
            this.questionnaire.questions[params[0] - 1].choices[i].isAnswer=1;
          }
        }
        this.questionnaire.questions[params[0]-1].fanswer=params[2];
      }

    },
    updateTexts(params) {
      this.questionnaire.questions[params[0] - 1].texts = params[1];
    },
    updateValue(params) {
      this.questionnaire.questions[params[0] - 1].value = params[1];
    },
    updateMax(params) {
      if (this.enterType == 4 && params[1] < this.questionnaire.questions[params[0] - 1].answerCountBefore) {
        this.$message.error("该模式评分题量级不可少于原题量级");
        return false;
      }
      this.questionnaire.questions[params[0] - 1].max = params[1];
      this.questionnaire.questions[params[0] - 1].texts.length = params[1];
      for (let i = 0; i < this.questionnaire.questions[params[0] - 1].texts.length; i++) {
        if (this.questionnaire.questions[params[0] - 1].texts[i] === undefined) {
          this.questionnaire.questions[params[0] - 1].texts[i] = (i + 1).toString();
        }
      }
      if (this.questionnaire.questions[params[0] - 1].value > this.questionnaire.questions[params[0] - 1].max) {
        this.questionnaire.questions[params[0] - 1].value = this.questionnaire.questions[params[0] - 1].max;
      }
    },
    updateTheName(val) {
      if (val === "") {
        this.$message.warning("问卷标题不可为空");
      }
      this.questionnaire.name = val;
    },
    updateTheIntroduction(val) {
      this.questionnaire.introduction = val;
    },
    updateTheisRepeat(val) {
      this.questionnaire.isRepeat = val;
    },
    updateTheisLogin(val) {
      this.questionnaire.isLogin = val;
    },
    updateThestartTime(val) {
      this.questionnaire.startTime = val;
    },
    updateTheendTime(val) {
      this.questionnaire.endTime = val;
    },
    updateTheisNumber(val) {
      this.questionnaire.isNumber = val;
    },
    updateTheisLimit(val) {
      this.questionnaire.isLimit = val;
    },
    updateTheallCount(val) {
      this.questionnaire.allCount = val;
    },
    updateTheisOrder(val) {
      this.questionnaire.isOrder = val;
    },
    checkName() {
      if (this.questionnaire.name === "") {
        return "NameisNull"
      }
    },
    checkType(){
      if(this.activeName==='first'){
        return "left-container1"
      } else {
        return "left-container2"
      }
    },

    handleExport(){
      this.$getPdf(this.questionnaire.name);
    },

    handleDragStart(e,item){
    this.dragging = item;
    },
    handleDragEnd(e,item){
      this.dragging = null
    },
    //首先把div变成可以放置的元素，即重写dragenter/dragover
    // DataTransfer 对象用来保存，通过拖放动作，拖动到浏览器的数据。
    // 如果dropEffect 属性设定为none,则不允许被拖放到目标元素中。
    handleDragOver(e) {
      e.dataTransfer.dropEffect = 'move'// e.dataTransfer.dropEffect="move";//在dragenter中针对放置目标来设置!
    },
    handleDragEnter(e,item){
        const newItems = [...this.questionnaire.questions]
      
        e.dataTransfer.effectAllowed = "move"
        //为需要移动的元素设置dragstart事件
        if(item === this.dragging){
          return
        }
        const src = newItems.indexOf(this.dragging)
        const dst = newItems.indexOf(item)
        if((this.checkIndex(src)==="cantDrag")||(this.checkIndex(dst)==="cantDrag")){
          return
        }
        else{
          // 替换
          newItems.splice(dst, 0, ...newItems.splice(src, 1))
          // 让item的颜色等于新交换的颜色
          this.questionnaire.questions = newItems
          for (let i = 0; i < this.questionnaire.questions.length; i++) {
            this.questionnaire.questions[i].order = (i + 1)
          }
        }
        
    },
    checkIndex(index){
      if(this.enterType==4&&index<this.questionnaire.numberBefore){
        return "cantDrag"

      }
    }
  },
  computed: {

  },
}
</script>

<style scoped>
.QuestionnaireEditor {
  background-color: #f0f0f0;
}

.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.left-container1 {
  margin-top: 10px;
  border-radius: 4px;
  padding: 12px;
  height: 250px;
}

.left-container2 {
  margin-top: 10px;
  border-radius: 4px;
  padding: 12px;
  height: 600px;
}

.middle-container {
  padding: 12px;
  margin-top: 10px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  height: 810px;
}

.right-container {
  padding: 12px;
  margin-top: 10px;
  border-radius: 4px;
  height: 700px;
}

.box-card /deep/.el-card__body{
  padding: 0;
}

.xuanti-container {
  margin-top: 5px;
  margin-bottom: 5px;
  height: 50px;
}

.xuanti {
  line-height: 25px;
  padding: 10px;
  border: 1px solid rgb(0, 153, 255);
  border-radius: 4px;
  display: inline-block;
  font-size: 0.9vw;
  transition: 0.2s;
}

.xuanti:hover {
  font-size: 1vw;
  cursor: pointer;
}

.setting {
  margin-top: 30px;
}

.setting .el-button {
  text-align: center;
  font-size: 1vw;
  width: 80%;
  height: 50px;
}

.selected {
  padding-top: 10px;
  padding-bottom: 10px;
  border-radius: 4px;
  background: #f8f8f8;
  box-shadow:  5px 5px 11px #5f5f5f,
              -5px -5px 11px #ffffff;
}

.questionnaire-title:hover,
.questionnaire-item:hover,
.question:hover{
  background: #f8f8f8;
}

.questionnaire-title,
.questionnaire-item {
  margin-left: 20px;
  margin-right: 20px;
}
.questionnaire-title {
  margin-top: 20px;
  text-align: center;
}

.questionnaire-title span {
  font-size: 14px;
  color: rgba(109, 109, 109, 0.836);
}

.name {
  line-height: 50px;
}

.NameisNull {
  height: 50px;
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

.setting .el-row {
  margin-top: 20px;
}

.el-icon-document-copy {
  margin-bottom: 10px;
  font-size: 20px;
  padding: 7px;
  border: 1px solid rgb(0 147 233);
  border-radius: 18px;
  transition: 0.2s;
}

.el-icon-document-copy:hover {
  color: white;
  background-color: rgb(0 147 233);
  cursor: pointer;
}

.el-icon-delete {
  font-size: 20px;
  padding: 7px;
  border: 1px solid #f56c6c;
  border-radius: 18px;
  transition: 0.2s;
}

.el-icon-delete:hover {
  color: white;
  background-color: #f56c6c;
  cursor: pointer;
}

.el-icon-sort-down {
  font-size: 20px;
  padding: 7px;
  border: 1px solid rgb(128 208 199);
  border-radius: 18px;
  transition: 0.2s;
}

.el-icon-sort-down:hover {
  color: white;
  background-color: rgb(128 208 199);
  cursor: pointer;
}

.el-icon-sort-up {
  font-size: 20px;
  padding: 7px;
  border: 1px solid rgb(128 208 199);
  border-radius: 18px;
  transition: 0.2s;
}

.el-icon-sort-up:hover {
  color: white;
  background-color: rgb(128 208 199);
  cursor: pointer;
}

font {
  font-family: "Lucida Handwriting";
  color: red;
}

.dagang {
  margin-top: 10px;
  margin-bottom: 10px;
}

.cantDrag{
  background-color: rgb(230, 230, 230);
}
</style>
