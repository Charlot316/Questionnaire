<template>
    <el-scrollbar>

        <el-card class="box-card">
            <div class="div1">
                <div>
                    <p class="p1">您的答卷已经提交，感谢您的参与！</p>
                </div>
                <div v-for="(item,index) in votesQuestionVOS" :key="index" >
                    <p class="p3">{{item.content}}</p>
                    <el-row :gutter="2" v-for="tmp in item.votesChoiceVOS" :key="tmp">
                        <el-col :span="1">
                            <div class="grid-content bg-white">
                                <p class="p5">{{tmp.ranking}}</p>
                            </div>
                        </el-col>
                        <el-col :span="23">
                            <div class="grid-content bg-purple-dark">
                                <p class="p4">{{tmp.choiceContent}}
                                    <h class="h1">
                                    <h class="h2">{{tmp.votes}}</h>
                                    票</h>
                                </p>
                                <el-progress :percentage="tmp.percentage" :stroke-width="15" :text-inside="true">
                                </el-progress>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <div class="link-top"></div>
                <div class="div1">
                    <p class="p2">问卷星球 提供技术支持</p>
                </div>
            </div>          
        </el-card>

    </el-scrollbar>
        
   
</template>

<script>
export default {
  data() {
    return {
        data: [],
        questionnaireId: '',
        name: ["题目一","题目二","题目三","题目四","题目5"],
        votesQuestionVOS: []
    }
  },

  created() {
    this.getVotes();
  },

  methods: {
    async getVotes() {
        this.questionnaireId = this.$route.params.questionnaireId;
        const { data: res } = await this.$http.get(`/api/admin/votes?questionnaireId=${this.questionnaireId}`);
        if(res.code === 0){
            this.votesQuestionVOS = res.data.votesQuestionVOS;
        }
        else{
            alert("出现错误")
        }
    },

  }
}
</script>


<style scoped>
.box-card{
    width: 50%;
    margin-top: 2%;
    margin-left: 25%; 
}
.div1{
    width: 80%;
    margin-left: 10%;
}
.p1{
    text-align: center;
    font-weight: bold;
    font-size: 17px;
    margin-top: 1%;
}
.p2{
    text-align: center;
}
.p3{
    color:gray;
    font-size: 20px;
    margin-top: 5%;
    font-weight: bold;
    margin-bottom: 2%;
}
.p4{
    margin-bottom: 0.5%;
}
.h1{
    float: right;
}
.h2{
    color: rgb(32, 93, 224);
}
.div2{
    width: 98%;
    margin-left: 2%;
    float: left;
}
.div3{
    float: left;
}
.link-top {
  margin-top: 15%;
  width: 100%;
  height: 1px;
  border-top: solid #acc0d8 1px;
}
.grid-content {
    border-radius: 4px;
    min-height: 36px;
}
.bg-white {
    background: white;
}
.el-row {
    margin-bottom: 1%;
    margin-bottom: 2%;
}
.el-col {
    border-radius: 4px;
    margin-top: 1%;
}
.p5{
    padding-top: 40%;
    padding-left: 25%;
    font-size: 22px;
    font-weight: bold;
    color: rgb(212, 210, 209);
}
.el-progress__text {
  color: #ffffff;
  float: right;
}
.content{
    margin: 0;
    padding: 0;
    overflow-y: scroll;
    margin-top: 0%;
}
</style>