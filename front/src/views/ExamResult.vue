<template>
    <el-scrollbar class="bg">
        <el-card class="box-card">
            <div>
                <p class="p1">考试分析</p>
            </div>
            <div class="div1">
                <el-row>
                    <el-col :span="12">
                        <div class="grid-content bg-white">
                            <p class="p2">{{score}}</p>
                            <div class="p3">
                                <p>总分{{allScore}}</p> 
                            </div>
                            
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div class="grid-content bg-white">
                            <p class="p4">{{correctCount}}</p>
                            <p class="p5">答对</p>
                            <div class="p3">
                                <p >共{{allCount}}题</p>
                            </div>
                            
                        </div>
                    </el-col>
                </el-row>
            </div> 
            <div>
                <div>
                    <p class="p6">答题解析</p>
                </div>
                <div class="div3" v-for="(item,index) in result" :key="index">
                    <div class="div6">
                        <h> <h class="h1">{{item.name}}</h> (分值{{item.score}}分)</h>
                    </div>
                    <div class="div4">
                        <p class="p9">你的答案：{{item.trueAnswer}}</p>
                        <p class="p9">正确答案：{{item.correctAnswer}}</p>
                    </div>
                    <div class="div5" v-if="item.isCorrect===0">
                        <p class="p13">未进行评分</p>
                    </div>
                    <div class="div5" v-if="item.isCorrect===1">
                        <p class="p7">回答正确</p>
                        <p class="p8">+{{item.realScore}}分</p>
                    </div>
                    <div v-if="item.isCorrect===2">
                        <p class="p11">回答错误</p>
                        <p class="p12">+{{item.realScore}}分</p>
                    </div>
                </div>
            </div>   

            <div class="link-top"></div>
            <div class="div7">
                <p class="p10">问卷星球 提供技术支持</p>
            </div> 
        </el-card>
    </el-scrollbar>
        
</template>

<script>
export default {
  data() {
    return {
        result: [
            // {
            //     id: "",
            //     name: "",
            //     isCorrect: "",
            //     correctAnswer: "",
            //     trueAnswer: " ",
            //     score: ""
            // }
        ],
        score: "",
        allScore: "",
        correctCount: "",
        allCount: "",
        questionnaireId: "",
        ip: "",
        submitRecorId: ""
    }
  },

  created() {
    this.getInformation()
  },

  methods: {

      async getInformation(){
        this.questionnaireId = this.$route.params.questionnaireId
        this.ip = this.$route.params.ip
        this.submitRecorId = this.$route.params.submitRecordId
        const { data: res } = await this.$http.get
          (`/api/admin/examAnalysis?questionnaireId=${this.questionnaireId}&ip=${this.ip}&submitRecordId=${this.submitRecorId}`);
        if(res.code === 0){
            this.score = res.data.score;
            this.allScore = res.data.allScore
            this.correctCount = res.data.correctCount
            this.allCount = res.data.questionContentVOS.length
            for(let i = 0; i < res.data.questionContentVOS.length; i ++){
                let model = {}
                model["id"] = res.data.questionContentVOS[i].id
                model["name"] = res.data.questionContentVOS[i].content
                model["isCorrect"] = res.data.examAnswerResultVOS[i].isCorrect
                if(res.data.examAnswerResultVOS[i].score === null){
                    model["score"] = 0
                }
                else{
                    model["score"] = res.data.examAnswerResultVOS[i].score
                } 
                model["realScore"] = res.data.examAnswerResultVOS[i].realScore
                
                if(res.data.examAnswerResultVOS[i].type === 0 || res.data.examAnswerResultVOS[i].type === 3){
                    model["trueAnswer"] = res.data.examAnswerResultVOS[i].trueAnswer[0];
                    model["correctAnswer"] = res.data.examAnswerResultVOS[i].correctAnswer[0];
                }
                else{
                    let tmp1= ""
                    let tmp2 = ""
                    for(let j = 0; j<res.data.examAnswerResultVOS[i].trueAnswer.length;j++){
                        tmp1 = tmp1 + "┋" + res.data.examAnswerResultVOS[i].trueAnswer[j]
                    }
                    model["trueAnswer"] = tmp1
                    for(let j = 0; j<res.data.examAnswerResultVOS[i].correctAnswer.length;j++){
                        tmp2 = tmp2 + "┋" + res.data.examAnswerResultVOS[i].correctAnswer[j]
                    }
                    model["correctAnswer"] = tmp2
                }
                this.result[i] = model;
            }
        }
        else{
            alert("发生错误")
        }
        
      }

  }
}
</script>


<style scoped>
.box-card{
    width: 50%;
    margin-top: 2%;
    margin-left: 25%; 
}
.p1{
    text-align: center;
    font-weight: bold;
    font-size: 20px;
    margin-top: 1%;
}
.div1{
    border: 1px solid (247, 243, 243);
    width: 50%;
    height: 100%;
    margin-left: 25%;
    margin-top: 5%;
}
.div2{
    font-size: 30px;
    color: red;
    font-weight: bold;
}
.grid-content {
    border-radius: 4px;
    min-height: 115px;
}
.bg-white {
    background: white;
    border: 1px solid rgb(247, 243, 243);
}
.bg-purple-light {
    background: #e5e9f2;
}
.p2{
    font-size: 80px;
    /* font-weight: bold; */
    text-align: center;
    color: red;
}
.p3{
    background-color: rgb(236, 231, 231);
    /* padding-top: 1%; */
    text-align: center;
    color: gray;
    padding: 0;
    position: absolute; 
    /* width: 100%;   */
    width: 50%;
    bottom: 0px; 
}
.p4{
    text-align: center;
    font-size: 39px;
    padding-top: 10%;
}
.p5{
    text-align: center;
    color: gray;
    padding-bottom: 2%;
}
.p6{
    font-weight: bold;
    font-size: 17px;
    margin-top: 3%;
}
.div3{
    margin-top: 7%;
}
.h1{
    font-size: 18px;
    font-weight: bold;
}
.p7{
    float: left;
    margin-top: 1%;
    color: green;
    font-weight: bold;
    background-color: rgb(236, 231, 231);
}
.p8{
    float: right;
    margin-top: 1%;
    color: green;
    font-weight: bold;
    font-size: 15px;
}
.p9{
    margin-top: 0.5%;
    color: gray;
    font-size: 15px;
}
.div4{
    margin-top: 2%;
}
.div5{
    background-color: rgb(230, 59, 59);
    border-radius: 5px;
    margin-bottom: 3%;
}
.div6{
    margin-top: 2%;
}
.link-top {
  margin-top: 15%;
  width: 100%;
  height: 1px;
  border-top: solid #acc0d8 1px;
}
.div7{
    width: 80%;
    margin-left: 10%;
}
.p10{
    text-align: center;
}
.p11{
    float: left;
    margin-top: 1%;
    color: red;
    font-weight: bold;
    background-color: rgb(236, 231, 231);
}
.p13{
    float: left;
    margin-top: 1%;
    color: blue;
    font-weight: bold;
    background-color: rgb(236, 231, 231);
}
.p12{
    float: right;
    margin-top: 1%;
    color: red;
    font-weight: bold;
    font-size: 15px;
}
.bg{
    background-image: url(../assets/img/bg.png);
}
</style>