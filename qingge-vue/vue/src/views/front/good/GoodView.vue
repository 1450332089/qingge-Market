<template>
<div class="main-box">
  <div >
<!--    左侧的图片-->
    <div class="image-box">
      <img :src="good.imgs" class="image">
    </div>
<!--    右侧盒子-->
    <div class="detail-box">
<!--      商品名与描述-->
      <div>
        <span style="font-size: 22px" ><strong>{{good.name}}</strong></span><br/>
      </div>
      <div style="margin-top: 20px">
        <span style="font-size: 17px;color: red">{{good.description}}</span>
      </div>
<!--      价格盒子-->

      <div class="price-box" v-if="good.discount<1">
        <dl>
          <div>  <dt>原价</dt>  <dd style="text-decoration: line-through">  {{price}}元  </dd> </div>
          <div>  <dt>折扣</dt>  <dd>  {{ discount }}  </dd>  </div>
          <div>  <dt>现价</dt>  <dd style="color: red;font-size: 25px">  {{realPrice}}元  </dd></div>
        </dl>
      </div>
      <div class="price-box" v-if="good.discount===1">
        <dl>
          <div>  <dt>价格</dt>  <dd style="color: red;font-size: 25px">  {{price}}元  </dd></div>
        </dl>
      </div>
<!--      月销量-->
      <div style="margin-top: 20px">
        <span>月销量：</span>
        <span style="color: red">{{good.sales}}</span><br/>
        <span style="height: 40px" v-if="showStore">库存：{{store}}</span>
      </div>
<!--      选择规格-->
      <div style="margin-top: 15px;height: 50px" v-if="standards.length!==0">
        <el-radio-group v-for="(standard,index) in standards" v-model="checkedStandard" @change="change(standard)" :key="index">
          <el-radio-button class="standard" :label="standard.value" ></el-radio-button>
        </el-radio-group>

      </div>
<!--      选择数量-->
      <div style="margin-top: 20px">
        <el-input-number v-model="count" controls-position="right" :min="1" :max="store" ></el-input-number>
      </div>
<!--      购买按钮组-->
      <div style="margin-top: 30px">
        <el-button class="button" @click="goToOrder">购买</el-button>
        <el-button class="button" @click="addToCart" icon="el-icon-edit">加入购物车</el-button>
      </div>
    </div>
  </div>



</div>
</template>

<script>
import API from "@/utils/request";

export default {
  name: "GoodView",
  data(){
    return{
      good: {},
      goodId: Number,
      price: -1,
      isDiscount : false,
      discount: '',
      standards:[],
      checkedStandard: '',
      store: 0,
      showStore: false,
      count: 1,
    }
  },
  methods:{
    getPriceRange(standards){
      let arr = standards.map(item => {return item.price;})
      //选择排序
      for(let i = 0;i<arr.length;i++){
        // 第一次循环，假设第一个值为最小值，后面i++依此类推
        let min =i
        for(let j=i+1;j<arr.length;j++){
          // 将第一个值循环与后面的值比较，如果后面的值比第一个值小，则将索引赋值给min，直到找到最小值
          if(arr[j] < arr[min]){
            min = j
          }
        }
        [arr[i],arr[min]] = [arr[min],arr[i]]
      }
      if(arr[0]===arr[arr.length-1]){
        return arr[0];
      }else{
        return arr[0]+'元 ~ '+arr[arr.length-1]
      }
    },
    change(standard){
      this.showStore = true;
      this.price = standard.price;
      this.store = standard.store;
    },
    goToOrder(){
      if(this.standards.length!==0){
        if(this.checkedStandard===''){
          this.$message.warning("请选择规格")
          return false;
        }
      }
      console.log(this.good)
      console.log(this.checkedStandard)
      this.$router.push({name: 'preOrder',query: {good: JSON.stringify(this.good), realPrice: this.realPrice, num: this.count, standard: this.checkedStandard }})
    },
    addToCart(){
      //未登录，拦截
      console.log(localStorage.getItem("user"))
      if(!localStorage.getItem("user")){
        this.$router.push("/login")
      }
      if(!this.checkedStandard){
        this.$message.error("请选择规格")
        return false;
      }
      // 从服务器获取当前用户的id，保证安全
      this.request.get("/userid").then(res=> {
        let userId = res
        let cart = {userId: userId,goodId: this.goodId,standard: this.checkedStandard,count: this.count}
        this.request.post("/api/cart",cart).then(res=>{
          if(res.code==='200'){
            this.$message.success("成功添加购物车")
          }
        })
      })
    },
  },

  created() {
    //初始化商品信息
    // this.good = JSON.parse(this.$route.query.good)
    this.goodId = this.$route.params.goodId;
    this.request.get("/api/good/"+this.goodId).then(res=>{
      if(res.code==='200'){
        this.good = res.data;
        let discount = this.good.discount;
        if(discount<1){
          this.isDiscount = true;
          this.discount = discount * 10 + '折';
        }
      }else{
        this.$router.go(0);
      }
    })
    //从服务器获取商品规格信息
    this.request.get("/api/good/standard/"+this.goodId).then(res=>{
      if(res.code==='200'){
        let standards= JSON.parse(res.data)
        this.standards = standards;
        //默认选择第一个标准
        this.price = this.getPriceRange(standards);
      }else {
        //没有规格
        this.price = this.good.price;
        this.store = this.good.store;
        this.showStore = true;
      }
    })



  },
  mounted() {

  },
  computed:{
    //折后价，小数点后2位
    realPrice: function (){
      if(this.good.discount < 1){
        //价格为范围，即不是数字，则返回一个范围
        if(isNaN(this.price)){
          let down = this.price.substring(0,this.price.indexOf('元')) * this.good.discount;
          let up = this.price.substring(this.price.lastIndexOf(' ')) * this.good.discount;
          return down.toFixed(2)+"元 ~ "+ up.toFixed(2);
        }else{
          return  (this.price * this.good.discount).toFixed(2);

        }
      }
      return this.price;
    }
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

  width: 420px;
  display: inline-block;
  margin-left: 50px;
  overflow: hidden;
}
.price-box{
  background-color: #e9e9e9;
  background-image: linear-gradient(to top right, #e3fafa, rgba(159, 250, 246, 0.56));
  border-radius: 5px;
  font: 12px/1.5 "Microsoft Yahei",tahoma,arial;
  padding-bottom: 1px;
  padding-top: 1px;
  margin-right: 20px;
  margin-top: 30px;
}
.price-box div{
  line-height: 20px;
  margin-left: 8px;
  margin-bottom: 5px;
}
.price-box dl dt{
  float: left;
  font-size: 14px;
  line-height: 20px;
}
.price-box dl dd{
  font-size: 18px;
  line-height: 20px;
}
.button{
  width: 130px;
  height: 45px;
  background-color: #96e2e0;
  color: #710a0a;
}
.standard{
  height: 30px;
  margin-right: 10px;
}
</style>