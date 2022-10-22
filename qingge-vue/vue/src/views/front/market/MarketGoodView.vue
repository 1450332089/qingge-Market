<template>
  <div class="main-box">
    <div>
      <!--    左侧的图片-->
      <div class="image-box">
        <img :src="good.img" class="image">
      </div>
      <!--    右侧盒子-->
      <div class="detail-box">


        <div class="price-box">
          <!--      商品名与描述-->
          <div>
            <span style="font-size: 22px" ><strong>{{good.goodName}}</strong></span><br/>
          </div>
          <div style="margin-top: 16px">
            <span style="font-size: 17px;color: red">{{good.description}}</span>
          </div>
          <!--      发布者信息-->
          <div class="user-info">
            <span  >发布者昵称：</span>
            <span style="color: #078275">{{good.nickname}}</span><br/>
            <span>发布者id：</span>
            <span style="color: #078275" >&nbsp;&nbsp;&nbsp;{{good.userId}}</span><br/>
            <span>发布时间：</span>
            <span style="color: #078275" >&nbsp;&nbsp;&nbsp;{{good.createTime}}</span><br/>
          </div>
          <!--      价格-->
          <div>  <dt>价格</dt>  <dd style="color: red;font-size: 25px">  {{good.price}}元  </dd></div>

        </div>
      </div>
    </div>
<!--    留言-->
    <div class="message-box">

      <el-input
          type="textarea"
          :rows="2"
          resize="none"
          placeholder="请输入留言"
          style="width: 90%;"
          v-model="inputMessage">
      </el-input>
      <el-button class="message-button" @click="sendMessage">发送</el-button>
      <message-item v-for="(message,index) in messages"
                    :message="message"
                    :userId="userId"
                    :index="index"
                    :key="message.id"
                    @del-message="delMessage"
                    style="margin: 20px 0"></message-item>
    </div>
  </div>

</template>

<script>
import MessageItem from "@/components/MessageItem";
const url = "/api/market"
export default {
  name: "MarketGoodView",
  data(){
    return{
      userId: 0,
      goodId: 0,
      good: {},
      inputMessage: '',
      messages:[],
    }
  },
  components:{
    'message-item': MessageItem,
  },
  created() {
    this.goodId = this.$route.params.goodId;
    this.request.get("/userid").then(res=>{
      this.userId = res;
    })
    //获取商品信息
    this.request.get(url+"/good/"+this.goodId).then(res=>{
      if(res.code==='200'){
        this.good = res.data;
      }
    })
    //获取留言
      this.loadMessage();
  },
  methods:{
    //发送留言
    sendMessage(){
      if(this.inputMessage===''){
        this.$message.error("不能为空！")
        return false;
      }
      this.request.get("/userid").then(res=>{
          this.userId = res;
          this.request.post(url+"/message",{
            goodId: this.goodId,
            userId: this.userId,
            message: this.inputMessage
          }).then(res=>{
            if(res.code==='200'){
              this.$message.success("留言成功！")
              this.inputMessage = '';
              this.loadMessage();
            }else{
              this.$message.error("留言失败")
            }
          })
      })
    },
    delMessage(index){
      console.log(index)
      this.messages.splice(index,1)
    },
    loadMessage(){
      this.request.get(url+"/message/"+this.goodId).then(res=>{
        if(res.code==='200'){
          this.messages = res.data;
          console.log(this.messages)
        }
      })
    },
  }
}
</script>

<style scoped>
.main-box {
  width: 1060px;
  margin: 20px auto;
  padding: 30px;
  background-color: #ffffff;
  overflow: hidden;
}
.image-box{
  height: 420px;
  width: 420px;
  border: #f2f2f2 1px solid;
  text-align: center;
  margin-left: 80px;
  margin-top: 30px;
  display: inline-block;
  overflow: hidden;
}
.image{
  height: 100%;
  width: 350px;
}
.detail-box{
  height: 420px;
  width: 400px;
  display: inline-block;
  margin-left: 50px;
  overflow: hidden;
}
.price-box{
  background-color: #e9e9e9;
  background-image: linear-gradient(to top right, #e3fafa, rgba(159, 250, 246, 0.56));
  border-radius: 5px;
  font: 12px/1.5 "Microsoft Yahei",tahoma,arial;
  padding: 30px;
  margin-right: 20px;
  margin-top: 30px;
}
.price-box div{
  line-height: 20px;
  margin-left: 8px;
  margin-bottom: 18px;
}
.price-box dt{
  float: left;
  font-size: 16px;
  line-height: 20px;
}
.price-box dd{
  font-size: 18px;
  line-height: 20px;
}
.user-info span{
  font-size: 18px;
}
.message-button{
  width: 8%;
  height: 54px;
  margin-left: 10px;
  background-color: #64c7ee;
  color: white;
}
.message-box{
  width: 85%;
  margin: 30px auto;
}
</style>