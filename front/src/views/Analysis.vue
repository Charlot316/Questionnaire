<template>
  <div>
    <el-row>
      <el-col :span="4"></el-col>
      <el-col :span="16">
        <el-card style="margin-top:40px">
          <el-menu
            default-active="0"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
          >
            <el-menu-item index="0">查看下载问卷</el-menu-item>
            <el-menu-item index="1">统计分析</el-menu-item>
            <el-menu-item index="2">交叉分析</el-menu-item>
          </el-menu>
          <!-- 查看下载问卷 -->
          <div v-if="displayType==0">
            <br />
            <div style="font-size:30px;font-weight:bold;margin-left:5px;margin-bottom:5px">
              {{name}}
              <el-button
                type="primary"
                style="float:right"
                @click="handleDownload"
              >下载数据</el-button>
            </div>
            <br />
            <el-table
              :data="tableData"
              style="width: 100%"
              height="60vh"
            >
              <el-table-column
                prop="username"
                label="作答用户"
              >
              </el-table-column>
              <el-table-column
                prop="time"
                label="提交时间"
              >
              </el-table-column>
              <template
                v-for="(item,key) in tableColumn"
                :key="key"
              >
                <el-table-column
                  :prop="item.prop"
                  :label="item.label"
                >
                </el-table-column>
              </template>
            </el-table>
          </div>
          <!-- 统计分析 -->
          <div v-if="displayType==1">

            <div id="pdfDom">
              <el-row>
                <el-col :span="2"></el-col>
                <el-col :span="20">
                  <div>
                    <br />
                    <div style="font-size:30px;float:center;font-weight:bold;margin-left:5px;margin-bottom:5px;text-align:center;">
                      {{name}}<br />
                      <el-button @click="getPDF">导出报告</el-button>

                    </div>
                    <div
                      style="text-align:center;"
                      v-if="isTest==true"
                    >
                      <el-row>
                        <el-col :span="3"></el-col>
                        <el-col :span="4">最高分{{max}}</el-col>
                        <el-col :span="3"></el-col>
                        <el-col :span="4">最低分{{min}} </el-col>
                        <el-col :span="3"></el-col>
                        <el-col :span="4">平均分{{average}}</el-col>
                        <el-col :span="3"></el-col>
                      </el-row>
                    </div>
                    <br />
                    <div
                      v-for="question in multipleChoices[0]"
                      style="margin-top: 20px"
                      :key="question"
                    >
                      <el-divider></el-divider>
                      <!-- 选择题展示 -->
                      <div v-if="question.type<=2||question.type==5||question.type==6">
                        <div>
                          <div style="font-size:20px;font-weight:bold;margin-left:5px;margin-bottom:5px">
                            {{question.titleText}}
                          </div>
                          <div v-if="isTest==true">
                            <el-row>
                              正答: {{question.correctAnswer}} 正确率: {{question.correctRate}}
                            </el-row>
                          </div>
                          <!-- 显示表格 -->
                          <div v-if="question.showTable==true">
                            <div v-if='question.showZero==true'>
                              <el-table
                                :data="multipleChoicesTableData[0][question.index]"
                                style="width: 100%;float:center;text-align:center;"
                                show-summary
                                :summary-method="(param)=>getSummaries(param,question)"
                                sum-text="总填次"
                              >
                                <el-table-column
                                  prop="id"
                                  label="选项id"
                                  min-width="10%"
                                  sortable
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="content"
                                  label="题目内容"
                                  min-width="60%"
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="count"
                                  label="选择数目"
                                  min-width="20%"
                                  sortable
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="percentage"
                                  label="占比"
                                  min-width="10%"
                                >
                                </el-table-column>
                              </el-table>
                            </div>
                            <div v-else>
                              <el-table
                                :data="multipleChoicesTableData[1][question.index]"
                                style="width: 100%;float:center;text-align:center;"
                                show-summary
                                :summary-method="(param)=>getSummaries(param,question)"
                                sum-text="总填次"
                              >
                                <el-table-column
                                  prop="id"
                                  label="选项id"
                                  min-width="10%"
                                  sortable
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="content"
                                  label="题目内容"
                                  min-width="60%"
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="count"
                                  label="选择数目"
                                  min-width="20%"
                                  sortable
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="percentage"
                                  label="占比"
                                  min-width="10%"
                                >
                                </el-table-column>
                              </el-table>
                            </div>
                          </div>
                          <div style="margin-top: 20px;text-align:right;margin-bottom: 10px;">
                            <el-switch
                              v-model="question.showZero"
                              active-text="显示零数据"
                              style="margin-right:5px"
                              @change="handleShowZero(question)"
                            >
                            </el-switch>
                            <el-switch
                              v-model="question.showTable"
                              active-text="显示表格"
                              style="margin-right:5px"
                            >
                            </el-switch>
                            <el-switch
                              v-model="question.showChart"
                              active-text="显示图表"
                              style="margin-right:5px"
                              @change="$nextTick(() => handleShowChart(question))"
                            >
                            </el-switch>
                          </div>
                        </div>
                        <!-- 图表部分 -->
                        <div v-if="question.showChart==true">
                          <div style="text-align:right;margin-bottom: 10px;">
                            <el-radio-group
                              :key="question"
                              v-model="question.tableType"
                              size="mini"
                              style="margin-bottom:10px"
                            >
                              <el-radio-button
                                :label="1"
                                @click="handleChangeChart($event,question,1)"
                              > <i class="el-icon-pie-chart"></i>饼状图</el-radio-button>
                              <el-radio-button
                                :label="2"
                                @click="handleChangeChart($event,question,2)"
                              > <i class="el-icon-bangzhu"></i>圆环图</el-radio-button>
                              <el-radio-button
                                :label="3"
                                @click="handleChangeChart($event,question,3)"
                              > <i class="el-icon-s-data"></i>柱状图</el-radio-button>
                              <el-radio-button
                                :label="4"
                                @click="handleChangeChart($event,question,4)"
                              > <i class="el-icon-s-operation"></i>条形图</el-radio-button>
                            </el-radio-group>
                          </div>
                          <!-- 容器 -->
                          <div>
                            <div
                              :id="'id'+question.questionId"
                              :style="{ width: '100%', height:question.height+100+'px',marginTop:'10px' }"
                            >
                            </div>
                          </div>
                          <!-- 一些操作 -->
                          <div style="margin-top: 20px;float:center;margin-bottom: 50px;text-align:center">
                            <el-select
                              size="mini"
                              v-model="question.sortType"
                              placeholder="请选择"
                              style="width:15%;margin-right:10px"
                              @change="value=>handleSortChange(value,question)"
                            >
                              <el-option
                                label="次数升序"
                                :value="1"
                              >
                              </el-option>
                              <el-option
                                label="次数降序"
                                :value="2"
                              >
                              </el-option>
                              <el-option
                                label="选项id升序"
                                :value="3"
                              >
                              </el-option>
                              <el-option
                                label="选项id降序"
                                :value="4"
                              >
                              </el-option>
                            </el-select>

                            <el-button
                              icon="el-icon-plus"
                              size="mini"
                              @click="addSize(question)"
                            ></el-button>
                            <el-button
                              icon="el-icon-minus"
                              size="mini"
                              @click="minusSize(question)"
                            > </el-button>
                          </div>
                        </div>
                      </div>
                      <!-- 填空题展示 -->
                      <div v-else>
                        <!-- 放按钮 -->
                        <div>
                          <div style="font-size:20px;font-weight:bold;margin-left:5px;margin-bottom:5px">
                            {{question.titleText}}
                          </div>
                          <div v-if="isTest==true&&question.correctAnswer.trim().length>0">
                            <el-row>
                              标准答案: {{question.correctAnswer}}
                            </el-row>
                          </div>
                          <div style="text-align:right;">
                            <el-switch
                              v-model="question.showTable"
                              active-text="显示表格"
                              style="margin-right:5px"
                            >
                            </el-switch>
                          </div>
                        </div>
                        <div v-if="question.showTable==true">
                          <div style="text-align:right;">
                            <el-button
                              v-if="question.CompletionTableData.length>1"
                              type="primary"
                              style="margin-bottom:6px"
                              @click="question.showType==0?question.showType=1:question.showType=0"
                            >{{question.showType==0?"合并结果":"拆散结果"}}</el-button>
                          </div>
                          <!-- 未合并表格 -->
                          <div v-if="question.CompletionTableData.length>1&&question.showType==0||question.CompletionTableData.length==1">
                            <template
                              v-for="(CompletionTableData,index) in question.CompletionTableData"
                              :key="CompletionTableData"
                            >
                              <div v-if="question.CompletionTableData.length>1">
                                <br />
                                <h5>第{{index+1}}空</h5><br />
                              </div>

                              <el-table
                                :data="CompletionTableData"
                                style="width: 100%;float:center;text-align:center;"
                              >
                                <el-table-column
                                  prop="submitTime"
                                  label="提交时间"
                                  sortable
                                  min-width="30%"
                                >
                                </el-table-column>
                                <el-table-column
                                  prop="content"
                                  label="填写内容"
                                  min-width="70%"
                                >
                                </el-table-column>
                              </el-table>
                            </template>
                          </div>
                          <!-- 合并表格 -->
                          <div v-else>
                            <el-table
                              :data="question.CompletionMergeTableData"
                              style="width: 100%;float:center;text-align:center;"
                            >
                              <el-table-column
                                prop="submitTime"
                                label="提交时间"
                                sortable
                                min-width="30%"
                              >
                              </el-table-column>
                              <el-table-column
                                prop="content"
                                label="填写内容"
                                min-width="70%"
                              >
                              </el-table-column>
                            </el-table>
                          </div>
                        </div>
                      </div>

                    </div>
                  </div>
                </el-col>
                <el-col :span="2"></el-col>
              </el-row>
            </div>
          </div>
          <!-- 交叉分析 -->
          <div v-if="displayType==2">
            <!--选择变量  -->
            <div style="margin-top:10px;">
              <el-row style="margin-top:10px;margin-bottom:10px">
                <el-col :span="1"></el-col>
                <el-col :span="10">自变量 X（一般为样本属性）</el-col>
                <el-col :span="2"></el-col>
                <el-col :span="10">因变量 Y（您要分析的目标题目）</el-col>
                <el-col :span="1"></el-col>
              </el-row>
              <el-row style="margin-top:10px;margin-bottom:10px">
                <el-col :span="1"></el-col>
                <el-col :span="10">
                  <el-select
                    v-model="independentVariable"
                    placeholder="请选择自变量"
                  >
                    <el-option
                      v-for="item in selections"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="2"></el-col>
                <el-col :span="10">
                  <el-select
                    v-model="dependentVariable"
                    placeholder="请选择应变量"
                  >
                    <el-option
                      v-for="item in selections"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="1"></el-col>
              </el-row>
              <el-row style="margin-top:10px;margin-bottom:10px">
                <el-col :span="1"></el-col>
                <el-col :span="23">
                  <el-button
                    type="primary"
                    @click="buttonClickCrossCompute"
                  >分析</el-button>
                </el-col>
              </el-row>
            </div>
            <div v-if="crossOverAnalysis.hasComputed==true">
              <div>
                <div style="font-size:20px;font-weight:bold;margin-left:5px;margin-bottom:5px">
                  {{crossOverAnalysis.titleText}}
                </div>
                <!-- 显示表格 -->
                <div v-if="crossOverAnalysis.isShowPercentage==false">
                  <el-table
                    :data="crossOverAnalysisTableData"
                    style="width: 100%;float:center;text-align:center;"
                  >
                    <el-table-column
                      prop="自变量"
                      label="自变量"
                    >
                    </el-table-column>
                    <template
                      v-for="(item,key) in crossOverAnalysisTableColumn"
                      :key="key"
                    >
                      <el-table-column
                        :prop="item.prop"
                        :label="item.label"
                        sortable
                      >
                      </el-table-column>

                    </template>
                    <el-table-column
                      prop="count"
                      label="填次"
                    >
                    </el-table-column>
                  </el-table>
                </div>
                <div v-else>
                  <el-table
                    :data="crossOverAnalysisPercentageTableData"
                    style="width: 100%;float:center;text-align:center;"
                  >
                    <el-table-column
                      prop="自变量"
                      label="自变量"
                    >
                    </el-table-column>
                    <template
                      v-for="(item,key) in crossOverAnalysisTableColumn"
                      :key="key"
                    >
                      <el-table-column
                        :prop="item.prop"
                        :label="item.label"
                        sortable
                      >
                      </el-table-column>
                    </template>
                    <el-table-column
                      prop="count"
                      label="填次"
                    >
                    </el-table-column>
                  </el-table>
                </div>
                <div style="margin-top: 20px;text-align:right;margin-bottom: 10px;">
                  <el-switch
                    v-model="crossOverAnalysis.isShowPercentage"
                    active-text="显示百分比"
                    inactive-text="显示数值"
                    inactive-color="#13ce66"
                    style="margin-right:5px"
                  >
                  </el-switch>
                </div>
              </div>
              <!-- 图表部分 -->
              <div>
                <div style="text-align:right;margin-bottom: 10px;">

                </div>
                <!-- 容器 -->
                <div>
                  <div
                    id="crossOverAnalysis"
                    :style="{ width: '100%', height:crossOverAnalysis.height+100+'px',marginTop:'10px' }"
                  >
                  </div>
                </div>
                <!-- 一些操作 -->
                <div style="margin-top: 20px;float:center;margin-bottom: 50px;text-align:center">

                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4"></el-col>
    </el-row>
  </div>
</template>

<script>
export default {

  data() {
    return {
      max: undefined,
      min: undefined,
      average: undefined,
      isTest: false,
      tableData: [],
      name: "",
      tableColumn: [],
      displayType: 0,
      questionnaireId: undefined,
      multipleChoices: [[], []],
      multipleChoicesTableData: [[], []],
      charts: [],
      crossOverAnalysis: {},
      crossOverAnalysisTableData: [],
      crossOverAnalysisPercentageTableData: [],
      crossOverAnalysisTableColumn: [],
      independentVariable: undefined,
      dependentVariable: undefined,
      selections: [],
      crossChart: undefined,
    };
  },
  computed: {},

  async created() {
    this.name = this.$route.query.name
    this.questionnaireId = this.$route.query.questionnaireId
    this.charts = []
    this.multipleChoices = [[], []]
    this.multipleChoicesTableData = [[], []]
    this.tableColumn = []
    this.tableData = []
    this.crossOverAnalysisTableData = []
    this.crossOverAnalysisTableColumn = []
    this.crossOverAnalysisPercentageTableData = []
    this.getDetailTable()
    this.getMultipleChoice()
  },
  mounted() {
    window.addEventListener('resize', () => {
      if (this.displayType == 1) {
        for (var i = 0; i < this.charts.length; i++) {
          if (this.charts[i]) this.charts[i].resize();
        }
      }
      if (this.displayType == 2 && this.crossChart) this.crossChart.resize();
    }, false)

  },
  unmounted() {
    window.onresize = null;
  },
  activated() { },
  methods: {
    getPDF() {
      this.$getPdf("统计分析");
    },
    getSummaries(param, question) {
      const { columns, data } = param;
      const sums = []; //声明变量
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "填写次数";
          return;
        }
        const values = data.map((item) => Number(item[column.property]));
        if (
          column.property == "count"
        ) {
          sums[index] = question.sum
        } else {
          // if (column.property == "content") sums[index] = "(计算的是总填写次数而不是选项选择次数)"
        }
      });
      return sums;
    },
    buttonClickCrossCompute() {
      if (this.independentVariable == undefined || this.dependentVariable == undefined) return this.$message.error("请选择自变量和因变量")
      if (this.independentVariable == this.dependentVariable) return this.$message.error("自变量和因变量不能相同")
      this.crossOverCompute()
    },
    async crossOverCompute() {
      if (this.independentVariable == undefined || this.dependentVariable == undefined) return
      if (this.independentVariable == this.dependentVariable) return
      this.crossOverAnalysisTableData = []
      this.crossOverAnalysisTableColumn = []
      this.crossOverAnalysisPercentageTableData = []
      this.crossOverAnalysis = {
        sum: undefined,
        showZero: true,
        hasComputed: false,
        tableType: 1,
        isShowPercentage: false,
        showTable: true,
        height: 500,
        sortType: 1,
        showChart: true,
        dataset: { dimensions: [], source: [] },
        toolbox: {
          feature: {
            saveAsImage: { backgroundColor: '#FFFFFF' }
          }
        },
        legend: {
          data: [],
        },
        xAxis: {
          type: 'category',
        },
        yAxis: {},
        tooltip: {
          trigger: 'axis',
          axisPointer: {            // Use axis to trigger tooltip
            type: 'shadow'        // 'shadow' as default; can also be 'line' or 'shadow'
          }
        },
        grid: {
          x: 30, //左侧与y轴的距离 
          y: 50, //top部与x轴的距离 
          x2: 30, //右侧与y轴的距离 
          y2: 20 //bottom部与x轴的距离
        },
        series: []
      }
      var { data: res1 } = await this.$http.get(
        `/api/admin/crossAnalysis?questionnaireId=${this.questionnaireId}&independentVariableId=${this.dependentVariable}&dependentVariableId=${this.independentVariable}`
      );
      if (res1.code === 0) {
        var questionContentVOS = res1.data.questionContentVOS
        this.crossOverAnalysis.dataset.dimensions.push('自变量')
        for (var i = 0, questionContentVOSLen = questionContentVOS.length; i < questionContentVOSLen; i++) {
          this.crossOverAnalysis.dataset.dimensions.push(questionContentVOS[i].content)
          this.crossOverAnalysis.series.push({ name: questionContentVOS[i].content, type: 'bar', stack: '总量是' })
          this.crossOverAnalysis.legend.data.push(questionContentVOS[i].content)
          this.crossOverAnalysisTableColumn.push({ prop: questionContentVOS[i].content, label: questionContentVOS[i].content, })
        }
      }
      else {
        return this.$message.error("获取交叉分析数据失败")
      }
      var { data: res } = await this.$http.get(
        `/api/admin/crossAnalysis?questionnaireId=${this.questionnaireId}&independentVariableId=${this.independentVariable}&dependentVariableId=${this.dependentVariable}`
      );
      if (res.code === 0) {
        var questionContentVOS = res.data.questionContentVOS
        var answerResultVOS = res.data.answerResultVOS
        for (var i = 0, questionContentVOSLen = questionContentVOS.length; i < questionContentVOSLen; i++) {
          var VOS = undefined
          if (answerResultVOS[i].type == 2) {
            VOS = res.data.answerResultVOS[i].scoreAnswerVOS
          }
          else if (answerResultVOS[i].type <= 1 || answerResultVOS[i].type == 5 || answerResultVOS[i].type == 6) {
            VOS = res.data.answerResultVOS[i].choiceAnswerVOS
          }
          var tempList = []
          var tempObj = { '自变量': questionContentVOS[i].content, count: answerResultVOS[i].sum }
          var tmpObjPercentage = { '自变量': questionContentVOS[i].content, count: answerResultVOS[i].sum }
          tempList.push(questionContentVOS[i].content)
          for (var j = 0, VOSLen = VOS.length; j < VOSLen; j++) {
            tempList.push(VOS[j].count)
            tempObj[VOS[j].content] = VOS[j].count
            tmpObjPercentage[VOS[j].content] = VOS[j].percentage
          }
          this.crossOverAnalysis.dataset.source.push(tempList)
          this.crossOverAnalysisTableData.push(tempObj)
          this.crossOverAnalysisPercentageTableData.push(tmpObjPercentage)
        }
      }
      else {
        return this.$message.error("获取交叉分析数据失败")
      }
      this.crossOverAnalysis.hasComputed = true
      this.$nextTick(() => {
        var ele = document.getElementById("crossOverAnalysis")
        ele.removeAttribute('_echarts_instance_')
        let myChart = this.$echarts.init(ele)

        myChart.setOption(this.crossOverAnalysis)
        this.crossChart = myChart
      })
    },
    async addSize(question) {
      if (question.height < 500) {
        question.height += 20
        if (question.showZero == true) {
          this.charts[question.index].setOption(question)
        } else {
          await this.charts[question.index].setOption(question)
          this.charts[question.index].setOption(this.multipleChoices[1][question.index])
        }
        await this.charts[question.index].resize()
        question.height -= 0
        if (question.showZero == true) {
          this.charts[question.index].setOption(question)
        } else {
          await this.charts[question.index].setOption(question)
          this.charts[question.index].setOption(this.multipleChoices[1][question.index])
        }
        await this.charts[question.index].resize()
      }
      else { this.$message.warning("不能再扩大图片了") }
    },
    async minusSize(question) {
      if (question.height > 70) {
        question.height -= 20
        if (question.showZero == true) {
          this.charts[question.index].setOption(question)
        } else {
          await this.charts[question.index].setOption(question)
          this.charts[question.index].setOption(this.multipleChoices[1][question.index])
        }
        await this.charts[question.index].resize()
        question.height += 0
        if (question.showZero == true) {
          this.charts[question.index].setOption(question)
        } else {
          await this.charts[question.index].setOption(question)
          this.charts[question.index].setOption(this.multipleChoices[1][question.index])
        }
        await this.charts[question.index].resize()
      }
      else { this.$message.warning("不能再缩小图片了") }
    },
    handleSortChange(value, question) {
      if (value == 1) {
        question.dataset[1].transform.config = { dimension: 'count', order: 'asc' }
        this.multipleChoices[1][question.index].dataset[1].transform.config = { dimension: 'count', order: 'asc' }

      } else if (value == 2) {
        question.dataset[1].transform.config = { dimension: 'count', order: 'desc' }
        this.multipleChoices[1][question.index].dataset[1].transform.config = { dimension: 'count', order: 'desc' }
      } else if (value == 3) {
        question.dataset[1].transform.config = { dimension: 'id', order: 'asc' }
        this.multipleChoices[1][question.index].dataset[1].transform.config = { dimension: 'id', order: 'asc' }
      }
      else if (value == 4) {
        question.dataset[1].transform.config = { dimension: 'id', order: 'desc' }
        this.multipleChoices[1][question.index].dataset[1].transform.config = { dimension: 'id', order: 'desc' }
      }
      if (question.showZero == true) {
        this.charts[question.index].setOption(question)
      } else {
        this.charts[question.index].setOption(this.multipleChoices[1][question.index])
      }
    },
    async handleDownload() {
      this.$http.post('/api/admin/excel', { questionnaireId: this.questionnaireId }, {
        responseType: 'blob' // 设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
      }).then(
        res => {
          const linkNode = document.createElement('a')
          linkNode.download = this.name + ".xlsx" // a标签的download属性规定下载文件的名称
          linkNode.style.display = 'none'
          linkNode.href = URL.createObjectURL(res.data) // 生成一个Blob URL
          document.body.appendChild(linkNode)
          linkNode.click() // 模拟在按钮上的一次鼠标单击
          URL.revokeObjectURL(linkNode.href) // 释放URL 对象
          document.body.removeChild(linkNode)
        }
      ).catch(() => {
        this.$message({
          message: '下载失败！请重试！',
          type: 'error',
          duration: 1000,
        });
      })
    },
    async getDetailTable() {
      const { data: res } = await this.$http.get(
        `/api/admin/answering?questionnaireId=${this.questionnaireId}`
      );
      if (res.code === 0) {
        res.data.questionContentVOS.map((item) => {
          this.tableColumn.push({ prop: `question${item.id}`, label: item.content })
        })
        res.data.userAnsweringVOS.map((item) => {
          var temp = {}
          temp.username = item.username
          temp.time = item.time
          item.answerContentVOS.map((subItem) => {
            temp[`question${subItem.id.toString()}`] = subItem.answerContent
          })
          this.tableData.push(temp)
        })
      } else { this.$message.error("获取答卷内容失败") }
    },
    handleShowZero(question) {
      if (question.showChart == true) {
        if (question.showZero == true) {
          this.charts[question.index].setOption(question)
        } else {
          this.charts[question.index].setOption(this.multipleChoices[1][question.index])
        }
      }
    },
    async handleShowChart(question) {
      if (question.showChart == true) {
        var ele = document.getElementById('id' + question.questionId)
        ele.removeAttribute('_echarts_instance_')
        let myChart = this.$echarts.init(ele)
        if (question.showZero == true) {
          myChart.setOption(question)
        } else {
          await myChart.setOption(question)
          myChart.setOption(this.multipleChoices[1][question.index])
        }
        this.charts[question.index] = myChart
      }
    },
    handleChangeChart(e, question, value) {
      if (e.target.tagName === 'INPUT') return
      if (value == 1) {
        question.series.type = 'pie'
        question.series.radius = '100%'
        question.series.label.formatter = '{b} 次数{@count}({@percentage})'
        question.xAxis = {}
        question.yAxis = {}
        this.multipleChoices[1][question.index].xAxis = {}
        this.multipleChoices[1][question.index].yAxis = {}
        this.multipleChoices[1][question.index].series.radius = '100%'
        this.multipleChoices[1][question.index].series.type = 'pie'
        this.multipleChoices[1][question.index].series.label.formatter = '{b} 次数{@count}({@percentage})'
      } else if (value == 2) {
        question.series.type = 'pie'
        question.series.radius = ['80%', '100%']
        question.series.label.formatter = '{b} 次数{@count}({@percentage})'
        question.xAxis = {}
        question.yAxis = {}
        this.multipleChoices[1][question.index].xAxis = {}
        this.multipleChoices[1][question.index].yAxis = {}
        this.multipleChoices[1][question.index].series.radius = ['80%', '100%']
        this.multipleChoices[1][question.index].series.type = 'pie'
        this.multipleChoices[1][question.index].series.label.formatter = '{b} 次数{@count}({@percentage})'
      } else if (value == 3) {
        question.series.type = 'bar'
        question.xAxis = {
          type: 'category',
        }
        question.yAxis = { type: 'value' }
        question.series.encode = { x: 'content', y: 'count', value: 'count', itemName: 'content' }
        question.series.label.formatter = '次数{@count}({@percentage})'
        this.multipleChoices[1][question.index].xAxis = {
          type: 'category',
        }
        this.multipleChoices[1][question.index].yAxis = { type: 'value' }
        this.multipleChoices[1][question.index].series.encode = { x: 'content', y: 'count', value: 'count', itemName: 'content' }
        this.multipleChoices[1][question.index].series.type = 'bar'
        this.multipleChoices[1][question.index].series.label.formatter = '次数{@count}({@percentage})'
      }
      else if (value == 4) {
        question.series.type = 'bar'
        question.xAxis = { type: 'value' }
        question.yAxis = {
          axisLabel: { interval: 0, rotate: 90 },
          type: 'category',
        }
        question.series.encode = { x: 'count', y: 'content', value: 'count', itemName: 'content' }
        question.series.label.formatter = '次数{@count}({@percentage})'
        question.series.label.formatter = '{b} 次数{@count}({@percentage})'
        this.multipleChoices[1][question.index].series.label.formatter = '{b} 次数{@count}({@percentage})'
        this.multipleChoices[1][question.index].xAxis = { type: 'value' }
        this.multipleChoices[1][question.index].yAxis = {
          axisLabel: { interval: 0, rotate: 90 },
          type: 'category',
        }
        this.multipleChoices[1][question.index].series.encode = { x: 'count', y: 'content', value: 'count', itemName: 'content' }
        this.multipleChoices[1][question.index].series.type = 'bar'
        this.multipleChoices[1][question.index].series.label.formatter = '次数{@count}({@percentage})'
      }
      if (question.showZero == true) {
        this.charts[question.index].setOption(question)
      } else {
        this.charts[question.index].setOption(this.multipleChoices[1][question.index])
      }

    },
    async getMultipleChoice() {
      const { data: res } = await this.$http.get(
        `/api/admin/answerAnalysis?questionnaireId=${this.questionnaireId}`
      );
      if (res.code === 0) {
        var questionContentVOS = res.data.questionContentVOS
        var answerResultVOS = res.data.answerResultVOS
        var VOS = undefined
        if (res.data.examAnalysisVO != null) {
          this.isTest = true
          this.max = res.data.examAnalysisVO.max
          this.min = res.data.examAnalysisVO.min
          this.average = res.data.examAnalysisVO.average
        }
        for (var i = 0, questionContentVOSLen = questionContentVOS.length; i < questionContentVOSLen; i++) {
          var temp = {}
          var tempWithoutZero = {}
          this.multipleChoicesTableData[0][i] = []
          this.multipleChoicesTableData[1][i] = []
          var answer = ""
          if (this.isTest == true && questionContentVOS[i].correctAnswer && questionContentVOS[i].correctAnswer.length > 0) {
            for (var j = 0, len = questionContentVOS[i].correctAnswer.length; j < len; j++) {
              answer += questionContentVOS[i].correctAnswer[j]
              if (j + 1 < len) answer += "、"
            }
          }
          if (answerResultVOS[i].type <= 2 || answerResultVOS[i].type == 5 || answerResultVOS[i].type == 6) {
            this.selections.push({ value: answerResultVOS[i].questionId, label: questionContentVOS[i].content })
            temp = {
              sum: answerResultVOS[i].sum,
              correctAnswer: answer,
              correctRate: undefined,
              showZero: true,
              tableType: 1,
              showTable: true,
              height: 300,
              sortType: 1,
              showChart: true,
              dataset: [{ dimensions: ['id', 'content', 'count', 'percentage'], source: [] }, {
                transform: {
                  type: 'sort',
                  config: { dimension: 'count', order: 'asc' }
                }
              }],
              toolbox: {
                feature: {
                  saveAsImage: { backgroundColor: '#FFFFFF' }
                }
              },
              xAxis: {
                type: 'category',
              },
              yAxis: { type: 'value' },
              tooltip: {},
              grid: {
                x: 30, //左侧与y轴的距离 
                y: 50, //top部与x轴的距离 
                x2: 30, //右侧与y轴的距离 
                y2: 20 //bottom部与x轴的距离
              },
              legend: {
                bottom: 0,
                orient: "horizontal", // 图例列表的布局朝向。 'horizontal'  'vertical'
              },
              series: {
                type: 'pie',
                encode: { x: 'content', y: 'count', value: 'count', itemName: 'content' },
                datasetIndex: 1,
                radius: '100%',
                center: ['50%', '60%'],
                label: {
                  show: true,
                  formatter: '{b} 次数{@count}({@percentage})',
                },
              }
            }
            tempWithoutZero = {
              dataset: [{ dimensions: ['id', 'content', 'count', 'percentage'], source: [] }, {
                transform: {
                  type: 'sort',
                  config: { dimension: 'count', order: 'asc' }
                }
              }],
              toolbox: {
                feature: {
                  saveAsImage: { backgroundColor: '#FFFFFF' }
                }
              },
              xAxis: {
                type: 'category',
              },
              yAxis: {},
              tooltip: {},
              grid: {
                x: 30, //左侧与y轴的距离 
                y: 50, //top部与x轴的距离 
                x2: 30, //右侧与y轴的距离 
                y2: 20 //bottom部与x轴的距离
              },
              legend: {
                bottom: "bottom",
                orient: "horizontal", // 图例列表的布局朝向。 'horizontal'  'vertical'
              },
              series: {

                type: 'pie',
                encode: { x: 'content', y: 'count', value: 'count', itemName: 'content' },
                datasetIndex: 1,
                radius: '100%',
                center: ['50%', '60%'],
                label: {
                  show: true,
                  formatter: '{b} 次数{@count}({@percentage})',
                },
              }
            }
            temp.correctRate = answerResultVOS[i].correctRate
            if (answerResultVOS[i].type == 2) {
              VOS = res.data.answerResultVOS[i].scoreAnswerVOS
            }
            else if (answerResultVOS[i].type <= 1 || answerResultVOS[i].type == 5 || answerResultVOS[i].type == 6) {
              VOS = res.data.answerResultVOS[i].choiceAnswerVOS
            }
            for (var j = 0, VOSLen = VOS.length; j < VOSLen; j++) {
              var tempList = []
              tempList.push(VOS[j].id)
              tempList.push(VOS[j].content)
              tempList.push(parseInt(VOS[j].count))
              tempList.push(VOS[j].percentage)
              temp.dataset[0].source.push(tempList)

              this.multipleChoicesTableData[0][i].push({ id: VOS[j].id, content: VOS[j].content, count: parseInt(VOS[j].count), percentage: VOS[j].percentage })
              if (parseInt(VOS[j].count) > 0) {
                tempWithoutZero.dataset[0].source.push(tempList)
                this.multipleChoicesTableData[1][i].push({ id: VOS[j].id, content: VOS[j].content, count: parseInt(VOS[j].count), percentage: VOS[j].percentage })
              }
            }
          }
          // 填空题
          else {
            const { data: res } = await this.$http.get(
              `/api/admin/completion?questionId=${answerResultVOS[i].questionId}`
            );
            if (res.code === 0) {
              temp.showType = 0
              temp.correctAnswer = answer
              temp.showTable = true
              temp.CompletionTableData = []
              temp.CompletionMergeTableData = []
              var submitTimeList = res.data.submitTimeList
              var content = res.data.content
              for (var k = 0, contentLength = content.length; k < contentLength; k++) {
                var tempTable = []
                for (var j = 0, submitTimeListLength = submitTimeList.length; j < submitTimeListLength; j++) {
                  tempTable.push({ submitTime: submitTimeList[j], content: content[k][j] })
                }
                temp.CompletionTableData.push(tempTable)
              }
              if (contentLength > 1) {
                for (var j = 0, submitTimeListLength = submitTimeList.length; j < submitTimeListLength; j++) {
                  var string = ""
                  for (var k = 0, contentLength = content.length; k < contentLength; k++) {
                    string += content[k][j]
                    if (k + 1 < contentLength) string += "┋"
                  }
                  temp.CompletionMergeTableData.push({ submitTime: submitTimeList[j], content: string })
                }
              }
            }
            else {
              this.$message.error("获取填空题信息失败")
            }
          }
          tempWithoutZero.titleText = temp.titleText = questionContentVOS[i].content
          tempWithoutZero.type = temp.type = answerResultVOS[i].type
          tempWithoutZero.questionId = temp.questionId = answerResultVOS[i].questionId
          tempWithoutZero.index = temp.index = i
          this.multipleChoices[0].push(temp)
          this.multipleChoices[1].push(tempWithoutZero)
        }
      } else { this.$message.error("获取答卷内容失败") }
    },
    drawMultipleChoice() {
      for (var i = 0, multipleChoicesLen = this.multipleChoices[0].length; i < multipleChoicesLen; i++) {
        if (this.multipleChoices[0][i].type <= 2 || this.multipleChoices[0][i].type == 5 || this.multipleChoices[0][i].type == 6) {
          var ele = document.getElementById('id' + this.multipleChoices[0][i].questionId)
          ele.removeAttribute('_echarts_instance_')
          let myChart = this.$echarts.init(ele)
          myChart.setOption(this.multipleChoices[0][i])
          this.charts[i] = []
          this.charts[i] = myChart
        }
      }
    },
    handleSelect(index) {
      this.displayType = index
      if (index == 1) {
        this.$nextTick(() => this.drawMultipleChoice())
      }
      else if (index == 2) {
        if (this.selections.length == 0) {
          this.$message.error("本功能只支持选择、评分题的交叉分析，本问卷没有可供交叉分析的选择题、评分题")
        }
        else if (this.selections.length == 1) {
          this.$message.error("本问卷只有一个选择题或评分题，无法进行交叉分析")
        }
        this.crossOverCompute()
      }
    }
  },
};
</script>

<style scoped>
.el-table th.gutter {
  display: table-cell !important;
}
.el-table--border th.gutter:last-of-type {
  display: block !important;
}
</style>