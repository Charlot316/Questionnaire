<template>
  <div id="pdfDom">
    <div style="margin-bottom:100px">
      <el-row>
        <el-col :span="4">
          <div style="margin-left: 3vw; width: 18vw;">
            <el-affix :offset="110">
              <el-button
                type="primary"
                style="
                width:  18vw;
                height: 70px;
                font-weight: bold;
                font-size: 2vw;
              "
                @click="$router.push({ path: '/typeselection' })"
              >
                + 新建问卷
              </el-button>
              <el-menu
                style="margin-top: 30px;width:  18vw;"
                default-active="0"
                class="el-menu-vertical-demo"
                @select="handleSelectMenu"
              >
                <el-menu-item index="0">
                  <i class="el-icon-document"></i>
                  <span>全部问卷</span>
                </el-menu-item>
                <el-menu-item index="1">
                  <i class="el-icon-menu"></i>
                  <span>收藏夹</span>
                </el-menu-item>

                <el-menu-item index="2">
                  <i class="el-icon-delete-solid"></i>
                  <span>回收站</span>
                </el-menu-item>
              </el-menu>
            </el-affix>
          </div>
        </el-col>
        <el-col :span="2"></el-col>
        <el-col :span="18">
          <div v-if="recycleBin == false">
            <div style="margin-left: 20px; margin-top: 40px; margin-right: 50px">
              <div style="margin-bottom: 50px">
                <!-- 顶部菜单 -->
                <el-row>
                  <el-col :span="3">
                    <h1>问卷列表</h1>
                  </el-col>
                  <el-col :span="5"></el-col>
                  <!-- 筛选排序方式 -->
                  <el-col :span="4">
                    <el-select
                      v-model="params.type"
                      @change="getQuestionnaire"
                      placeholder="排序方式"
                      class="handle-select mr10"
                      id="排序方式"
                    >
                      <el-option
                        key="0"
                        label="创建时间降序"
                        value="0"
                      ></el-option>
                      <el-option
                        key="1"
                        label="创建时间升序"
                        value="1"
                      ></el-option>
                      <el-option
                        key="2"
                        label="更新时间降序"
                        value="2"
                      ></el-option>
                      <el-option
                        key="3"
                        label="更新时间升序"
                        value="3"
                      ></el-option>
                      <el-option
                        key="4"
                        label="答卷量降序"
                        value="4"
                      ></el-option>
                      <el-option
                        key="5"
                        label="答卷量升序"
                        value="5"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <!-- 筛选状态 -->
                  <el-col :span="3">
                    <el-select
                      v-model="params.status"
                      @change="getQuestionnaire"
                      placeholder="状态"
                      class="handle-select mr10"
                      id="状态"
                    >
                      <el-option
                        key="-1"
                        label="全部状态"
                        value="undefined"
                      ></el-option>
                      <el-option
                        key="0"
                        label="已保存"
                        value="0"
                      ></el-option>
                      <el-option
                        key="1"
                        label="已发布"
                        value="1"
                      ></el-option>
                      <el-option
                        key="2"
                        label="已暂停"
                        value="2"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3">
                    <el-select
                      v-model="params.questionnaireType"
                      @change="getQuestionnaire"
                      placeholder="筛选类型"
                      class="handle-select mr10"
                      id="筛选类型"
                    >
                      <el-option
                        key="-1"
                        label="全部问卷"
                        value="undefined"
                      ></el-option>
                      <el-option
                        key="0"
                        label="普通问卷"
                        value="0"
                      ></el-option>
                      <el-option
                        key="1"
                        label="投票问卷"
                        value="1"
                      ></el-option>
                      <el-option
                        key="2"
                        label="报名问卷"
                        value="2"
                      ></el-option>
                      <el-option
                        key="3"
                        label="考试问卷"
                        value="3"
                      ></el-option>
                      <el-option
                        key="4"
                        label="疫情填报"
                        value="4"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <!-- 搜索名字 -->
                  <el-col :span="6">
                    <el-input
                      placeholder="请输入问卷名进行搜索"
                      v-model="params.keyword"
                      @keyup.enter="
                        params.current = 0;
                        getQuestionnaire();
                      "
                    >
                      <template #append>
                        <el-button
                          icon="el-icon-search"
                          @click="
                            params.current = 0;
                            getQuestionnaire();
                          "
                        >
                        </el-button>
                      </template>
                    </el-input>
                  </el-col>
                </el-row>
              </div>
              <div
                v-for="questionnaire in questionnaires"
                :key="questionnaire"
                class="text item"
                style="margin-top: 20px"
              >
                <el-card
                  shadow="hover"
                  @mouseenter="
                    currentQuestionnaire.questionnaireId = questionnaire.id;
                    currentQuestionnaire.tempName = questionnaire.name;
                  "
                >
                  <template #header>
                    <div class="questionnaire">
                      <el-row>
                        <el-col
                          :span="14"
                          style="font-size: 25px"
                        >
                          <span
                            v-if="questionnaire.isStar === 1"
                            class="el-icon-star-on"
                            style="color: #ffd400; margin-right: 5px"
                          >
                          </span>{{ questionnaire.name }}
                        </el-col>
                        <el-col :span="2">
                          <span v-if="questionnaire.questionnaireType === 0">普通问卷</span>
                          <span v-if="questionnaire.questionnaireType === 1">投票问卷</span>
                          <span v-if="questionnaire.questionnaireType === 2">报名问卷</span>
                          <span v-if="questionnaire.questionnaireType === 3">考试问卷</span>
                          <span v-if="questionnaire.questionnaireType === 4">疫情填报</span>
                        </el-col>
                        <!-- 三种状态 -->
                        <el-col :span="2">
                          <span
                            v-if="questionnaire.status === 0"
                            style="color: #426ab3"
                          >·已保存</span>
                          <span
                            v-if="questionnaire.status === 1"
                            style="color: #7fb80e"
                          >·已发布</span>
                          <span
                            v-if="questionnaire.status === 2"
                            style="color: #d71345"
                          >·已暂停</span>
                        </el-col>
                        <el-col :span="2">答卷:{{ questionnaire.count }}</el-col>
                        <el-col :span="4">{{
                          questionnaire.updateTime
                        }}</el-col>
                      </el-row>
                    </div>
                  </template>

                  <div>
                    <el-row>
                      <el-col :span="3">
                        <el-button
                          type="text"
                          @click="handleOpenDesign(questionnaire)"
                        >
                          <div class="options">
                            <i class="el-icon-edit-outline"></i>设计问卷
                          </div>
                        </el-button>
                      </el-col>
                      <el-col :span="3">
                        <el-button
                          type="text"
                          @click="handleSend(questionnaire)"
                        >
                          <div class="options">
                            <i class="el-icon-position"></i>发送问卷
                          </div>
                        </el-button>
                      </el-col>
                      <el-col :span="3">
                        <el-button type="text">
                          <div
                            class="options"
                            @click="
                              handleOpenAnalysis(questionnaire)
                            "
                          >
                            <i class="el-icon-pie-chart"></i>分析下载
                          </div>
                        </el-button>
                      </el-col>
                      <el-col :span="5"></el-col>
                      <el-col :span="2">
                        <el-button
                          type="text"
                          @click="handlePublish(questionnaire)"
                        >
                          <div
                            class="options"
                            v-if="questionnaire.status === 1"
                          >
                            <i class="el-icon-video-pause"></i>暂停
                          </div>
                          <div
                            class="options"
                            v-else
                          >
                            <i class="el-icon-video-play"></i>发布
                          </div>
                        </el-button>
                      </el-col>
                      <el-col :span="2">
                        <el-button
                          type="text"
                          @click="openCopy"
                        >
                          <div class="options">
                            <i class="el-icon-copy-document"></i>复制
                          </div>
                        </el-button>
                      </el-col>
                      <el-col :span="2">
                        <el-button
                          type="text"
                          @click="handleDelete"
                        >
                          <div class="options">
                            <i class="el-icon-delete">删除</i>
                          </div>
                        </el-button>
                      </el-col>
                      <el-col :span="3">
                        <el-button
                          type="text"
                          @click="handleCollect(questionnaire)"
                        >
                          <div
                            class="options"
                            v-if="questionnaire.isStar === 1"
                          >
                            <i class="el-icon-star-on"></i>取消收藏
                          </div>
                          <div
                            class="options"
                            v-if="questionnaire.isStar === 0"
                          >
                            <i class="el-icon-star-off"></i>收藏
                          </div>
                        </el-button>
                      </el-col>

                      <el-col :span="1"></el-col>
                    </el-row>
                  </div>
                </el-card>
              </div>
            </div>
            <el-pagination
              style="margin-top:20px; margin-left:20px"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="params.current"
              :page-sizes="[5, 10, 20, 50, 100]"
              :page-size="params.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
            >
            </el-pagination>
          </div>
          <div v-if="recycleBin == true">
            <div class="div1">
              <div class="div2">
                <p class="pc">
                  如果您想释放上传文件题的空间，请点击“清空数据”，数据清空后将无法恢复，请谨慎操作！
                </p>
              </div>
              <div class="div3">
                <el-button
                  type="danger"
                  @click="delTotal()"
                >清空回收站</el-button>
              </div>
              <div class="div4">
                <el-table
                  :data="tableData"
                  border
                  style="width: 100%"
                >
                  <el-table-column
                    prop="name"
                    label="问卷名"
                    width="260"
                  >
                  </el-table-column>
                  <el-table-column
                    prop="updateTime"
                    label="发布时间"
                    width="200"
                  >
                  </el-table-column>
                  <el-table-column
                    prop="count"
                    label="答卷数"
                    width="150"
                  >
                  </el-table-column>
                  <el-table-column
                    label="清空数据"
                    width="130"
                  >
                    <template v-slot="scope">
                      <el-button
                        @click="empty(scope.$index)"
                        type="text"
                        size="small"
                      >
                        清空数据
                      </el-button>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="恢复"
                    width="120"
                  >
                    <template v-slot="scope">
                      <el-button
                        @click="recover(scope.$index)"
                        type="text"
                        size="small"
                      >
                        恢复
                      </el-button>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="彻底删除"
                    width="130"
                  >
                    <template v-slot="scope">
                      <el-button
                        @click="del(scope.$index)"
                        type="danger"
                        size="small"
                      >
                        彻底删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div class="d">
                <h class="h1">被删除的问卷保留6个月后将被彻底删除，无法再恢复运行！</h>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 复制弹出框 -->
    <el-dialog
      title="提示"
      v-model="copyVisible"
      width="40%"
    >
      <h2>请输入新问卷名</h2>
      <br />
      <el-input
        v-model="currentQuestionnaire.name"
        @keyup.enter="handleCopy"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="copyVisible = false">取 消</el-button>
          <el-button
            type="primary"
            @click="handleCopy"
          >确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 发布问卷提示框 -->
    <el-dialog
      title="问卷发布"
      v-model="publishVisible"
      width="50%"
      @opened="creatQrCode"
    >
      <div style="text-align:center">
        <h2>问卷链接</h2>
        <br />
        <el-row>
          <el-col :span="20">
            <el-input
              id="copy_element"
              readonly
              v-model="currentCode"
            />
          </el-col>
          <el-col
            :span="4"
            style="text-align:center"
          >
            <el-button @click="handleCopyLink">一键复制</el-button>
          </el-col>
        </el-row>
        <br />

        <h2>问卷二维码</h2>
        <br />
        <div
          class="qrcode"
          id="qrcode"
          ref="qrCodeUrl"
        ></div>
        <br />
        <el-button @click="handleClickExportEcharts"> 下载二维码</el-button>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            @click="publishVisible = false"
          >关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import QRCode from "qrcodejs2";
// document.getElementById("qrcode").innerHTML = "";
export default {
  data() {
    return {
      params: {
        type: "0",
        keyword: "",
        status: "undefined",
        isStar: undefined,
        questionnaireType: "undefined",
        current: 0,
        size: 5,
      },
      ret: { questionnaireId: "" },
      tableData: [],
      recycleBin: false,
      total: 0,
      currentQuestionnaire: { name: "" },
      copyVisible: false,
      publishVisible: false,
      questionnaires: [],
      currentCode: "",
      qrcode: null,
    };
  },
  computed: {},

  created() {
    this.getQuestionnaire();
  },
  mounted() { },
  activated() { },
  methods: {
    async handleOpenAnalysis(questionnaire) {
      const { data: res } = await this.$http.get(
        `/api/admin/count?questionnaireId=${questionnaire.id}`
      );
      if (res.code == 0) {
        if (res.data > 0) {
          await this.getQuestionnaire()
          this.$router.push({
            path: '/analysis',
            query: {
              questionnaireId: questionnaire.id,
              name: questionnaire.name,
            },
          });
        }
        else this.$message.warning('尚未收到答卷，分析界面未开启')
      }
      else {
        if (questionnaire.count > 0) {
          this.$router.push({
            path: '/analysis',
            query: {
              questionnaireId: questionnaire.id,
              name: questionnaire.name,
            },
          });
        }
        else this.$message.warning('尚未收到答卷，分析界面未开启')
      }
      this.getQuestionnaire()
    },
    async handleOpenDesign(questionnaire) {
      const { data: res } = await this.$http.get(
        `/api/admin/count?questionnaireId=${questionnaire.id}`
      );
      if (res.code == 0) {
        if (res.data > 0) {
          await this.getQuestionnaire()
          this.$router.push({
            path: '/model',
            query: {
              questionnaireId: questionnaire.id,
            },
          });
        }
        else {
          await this.getQuestionnaire()
          this.$router.push({
            path: '/questionnaireeditor',
            query: {
              enterType: 2,
              questionnaireId: questionnaire.id,
            }
          });
        }
      }
      else {
        if (questionnaire.count > 0) {
          await this.getQuestionnaire()
          this.$router.push({
            path: '/model',
            query: {
              questionnaireId: questionnaire.id,
            },
          });
        }
        else {
          await this.getQuestionnaire()
          this.$router.push({
            path: '/questionnaireeditor',
            query: {
              enterType: 2,
              questionnaireId: questionnaire.id,
            }
          });
        }
      }

    },
    getPdf() {
      this.$getPdf("问卷");
    },
    handleExport() { },
    openCopy() {
      this.copyVisible = true;
      this.currentQuestionnaire.name =
        this.currentQuestionnaire.tempName + "(副本)";
    },
    handleCopyLink() {
      const cInput = document.createElement("input");
      cInput.value = this.currentCode;
      document.body.appendChild(cInput);
      cInput.select(); // 选取文本域内容;
      // 执行浏览器复制命令
      // 复制命令会将当前选中的内容复制到剪切板中（这里就是创建的input标签）
      // Input要在正常的编辑状态下原生复制方法才会生效
      document.execCommand("Copy");
      /// 复制成功后再将构造的标签 移除
      cInput.remove();
      this.$message.success("复制链接成功！");
    },
    async handleSend(questionnaire) {
      this.currentCode = "";
      if (questionnaire.status != 1) {
        this.$confirm("问卷还未发布，是否发布？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const { data: res } = await this.$http.patch(
              `/api/admin/publish`,
              this.currentQuestionnaire
            );
            if (res.code === 0) {
              if (this.params.status !== "undefined") this.getQuestionnaire();
              else {
                questionnaire.status = 1;
              }
              const { data: res } = await this.$http.get(
                `/api/admin/code?questionnaireId=${this.currentQuestionnaire.questionnaireId}`
              );
              if (res.code === 0) {
                this.$message.success("发布问卷成功");
                this.currentCode = `http://localhost:8080/questionnaire?questionnaireId=${questionnaire.id}&code=${res.data}`;
                if (this.params.status !== "undefined") this.getQuestionnaire();
                else {
                  questionnaire.status = 1;
                }
                this.publishVisible = true;
              } else {
                this.$message.error("发布问卷失败");
              }
            } else {
              this.$message.error("发布问卷失败");
            }
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消发布",
            });
          });
      } else {
        const { data: res } = await this.$http.get(
          `/api/admin/code?questionnaireId=${this.currentQuestionnaire.questionnaireId}`
        );
        if (res.code === 0) {
          this.currentCode = `http://localhost:8080/questionnaire?questionnaireId=${questionnaire.id}&code=${res.data}`;
          this.publishVisible = true;
        } else {
          this.$message.error("发布问卷失败");
        }
      }
    },
    handleDelete() {
      this.$confirm("此操作将会把问卷移入回收站，是否继续操作", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          const { data: res } = await this.$http.patch(
            `/api/admin/delete`,
            this.currentQuestionnaire
          );
          if (res.code === 0) {
            this.$message.success("删除成功");
            this.getQuestionnaire();
          } else {
            this.$message.error("删除失败");
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    async handlePublish(questionnaire) {
      if (questionnaire.status === 1) {
        const { data: res } = await this.$http.patch(
          `/api/admin/stop`,
          this.currentQuestionnaire
        );
        if (res.code === 0) {
          this.$message.warning("已暂停问卷");
          if (this.params.status !== "undefined") this.getQuestionnaire();
          else {
            questionnaire.status = 2;
          }
        } else {
          this.$message.error("暂停问卷失败");
        }
      } else {
        const { data: res } = await this.$http.patch(
          `/api/admin/publish`,
          this.currentQuestionnaire
        );
        if (res.code === 0) {
          this.$message.success("发布问卷成功");
          if (this.params.status !== "undefined") this.getQuestionnaire();
          else {
            questionnaire.status = 1;
          }
        } else {
          this.$message.error("发布问卷失败");
        }
      }
    },
    async handleCollect(questionnaire) {
      if (questionnaire.isStar === 1) {
        const { data: res } = await this.$http.patch(
          `/api/admin/noStar`,
          this.currentQuestionnaire
        );
        if (res.code === 0) {
          this.$message.warning("已取消收藏");

          if (this.params.isStar === 1 || this.params.isStar === "1")
            this.getQuestionnaire();
          else {
            questionnaire.isStar = 0;
          }
        } else {
          this.$message.error("取消收藏失败");
        }
      } else {
        const { data: res } = await this.$http.patch(
          `/api/admin/star`,
          this.currentQuestionnaire
        );
        if (res.code === 0) {
          this.$message.success("收藏成功");
          questionnaire.isStar = 1;
        } else {
          this.$message.error("收藏失败");
        }
      }
    },
    async handleCopy() {
      if (this.currentQuestionnaire.name.trim().length === 0) {
        return this.$message.warning("新问卷名不能为空");
      }
      const { data: res } = await this.$http.post(
        `/api/admin/replication`,
        this.currentQuestionnaire
      );
      if (res.code === 0) {
        this.copyVisible = false;
        this.$message.success("复制成功");
        this.getQuestionnaire();
      } else {
        this.$message.error("复制失败");
      }
    },
    handleSelectMenu(index) {
      this.params.status = "undefined";
      if (index !== "2") {
        if (index === 1 || index === "1") this.params.isStar = index;
        else this.params.isStar = undefined;
        this.getQuestionnaire();
        this.recycleBin = false;
      } else {
        this.showInformation();
        this.recycleBin = true;
        this.params.status = 3;
      }
    },
    async getQuestionnaire() {
      const { data: res } = await this.$http.get(
        `/api/admin/information${this.formGetUrl(this.params)}`
      );
      if (res.code === 0) {
        this.total = res.data.total;
        this.questionnaires = res.data.records;
      } else {
        this.$message.error("获取问卷列表失败");
      }
    },
    formGetUrl(obj) {
      let url = "?";
      for (let key in obj) {
        if (obj[key] !== undefined && obj[key] !== "undefined")
          url += `${encodeURIComponent(key)}=${encodeURIComponent(obj[key])}&`;
      }
      return url.slice(0, -1);
    },

    handleSizeChange(size) {
      this.params.size = size;
      this.getQuestionnaire();
    },
    handleCurrentChange(current) {
      this.params.current = current;
      this.getQuestionnaire();
    },

    //生成二维码的函数
    creatQrCode() {
      if (document.getElementById("qrcode") !== null) {
        document.getElementById("qrcode").innerHTML = "";
        var qrcode = new QRCode(this.$refs.qrCodeUrl, {
          text: this.currentCode,
          width: 200, // 二维码宽度，单位像素
          height: 200, // 二维码高度，单位像素
        });
        this.qrcode = qrcode;
      }
    },
    handleClickExportEcharts() {
      //导出Echart
      var canvasData = this.$refs.qrCodeUrl.getElementsByTagName("canvas");
      var a = document.createElement("a");
      var event = new MouseEvent("click"); // 创建一个单击事件
      a.href = canvasData[0].toDataURL("image/png");
      a.download = this.currentQuestionnaire.tempName;
      a.dispatchEvent(event); // 触发a的单击事件
    },
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    del(index) {
      this.$confirm("此操作将永久删除该问卷, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          this.ret.questionnaireId = this.tableData[index].id;
          const { data: res } = await this.$http.post(
            "/api/admin/recycleQuestionnaireDelete",
            this.ret
          );
          if (res.code === 0) {
            this.$message.success("已成功删除");
            this.showInformation();
          } else {
            alert("发生错误");
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    async showInformation() {
      const { data: res } = await this.$http.get(
        "/api/admin/recycleInformation",
        this.params
      );
      if (res.code === 0) {
        this.tableData = res.data.records;
      } else {
        alert("发生错误");
      }
    },

    empty(index) {
      if (this.tableData[index].count === 0) {
        this.$message.error("当前问卷数据已为0，无法继续清空！");
      } else {
        this.$confirm("此操作将清楚此问卷所有数据, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            this.ret.questionnaireId = this.tableData[index].id;
            const { data: res } = await this.$http.post(
              "/api/admin/deleteEmpty",
              this.ret
            );
            if (res.code === 0) {
              this.$message.success("成功清除数据");
              this.showInformation();
            } else {
              alert("发生错误");
            }
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      }
    },

    async recover(index) {
      this.ret.questionnaireId = this.tableData[index].id;
      const { data: res } = await this.$http.post(
        "/api/admin/deleteRecovery",
        this.ret
      );
      if (res.code === 0) {
        this.$message.success("已从回收站恢复成功");
        this.showInformation();
      } else {
        alert("发生错误");
      }
    },

    delTotal() {
      this.$confirm("此操作将清空回收站且无法恢复, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          const { data: res } = await this.$http.post(
            "/api/admin/recycleDelete"
          );
          if (res.code === 0) {
            this.$message.success("已成功清空回收站");
            this.showInformation();
          } else {
            alert("发生错误");
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
  },
};
</script>

<style scoped>
.questionnaire {
  font-size: 15px;
  vertical-align: middle;
}
.options {
  color: #7a7a7a;
  font-size: 17px;
}
.options:hover {
  color: #b4b4b4;
}
.options:active {
  color: #505050;
}
.div1 {
  margin-top: 40px;
  width: 80%;
  margin-left: 10%;
}
.div2 {
  margin-top: 2%;
  width: 50%;
  float: left;
}
.div3 {
  margin-top: 1%;
  float: right;
  margin-bottom: 1%;
}
.div4 {
  margin-top: 10px;
}
h {
  margin-top: 10px;
  color: red;
  padding-top: 5%;
}
.h1 {
  margin-top: 10px;
  color: red;
  padding-top: 5%;
}
.d {
  margin-top: 20px;
  text-align: center;
}
.user-avator {
  width: 20px;
  height: 20px;
  border-radius: 50%;
}
p {
  display: inline-block;
}
.pc {
  font-size: 12px;
  color: blue;
  /* vertical-align:middle; */
  margin-left: 20px;
}
.img {
  width: 20px;
  height: 20px;
  /* vertical-align:middle; */
}

.qrcode {
  display: inline-block;
  margin: 0 auto;
  width: 200;
}
</style>
