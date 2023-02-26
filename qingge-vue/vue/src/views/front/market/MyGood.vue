<template>
<div>
  <div class="main-box">
    <div style="text-align: center;margin-bottom: 30px">
      <h1>我发布的商品</h1>
    </div>
    <!--      商品列表-->
    <div style="width: 1300px;margin: 20px auto;">
      <el-row :gutter="20">
        <el-col :span="6" v-for="good in good" :key="good.id" style="margin-bottom: 20px ">
          <!--            商品格子-->
          <router-link :to="'/editGood/'+good.id">
            <el-card :body-style="{ padding: '0px',background: '#e3f5f4' }">
              <img :src="good.img" style="width: 100%;height: 300px">
              <div style="padding:5px 10px;">
                <span style="font-size: 18px">{{good.goodName}}</span><br/>
                <span style="color: red;font-size: 15px">￥{{ good.price }}</span>
              </div>
            </el-card>
          </router-link>
        </el-col>
      </el-row>
    </div>
  </div>
</div>
</template>

<script>
import API from "@/utils/request";

export default {
  name: "MyGood",
  data(){
    return{
      good: [],
    }
  },
  created() {
    API.get("/userid").then(res=>{
      API.get("/api/market/userid/"+ res).then(res=>{
        if(res.code==='200'){
          this.good = res.data;
          console.log(this.good)
        }
      })
    })
  }
}
</script>

<style scoped>
.main-box{
  background-color: white;
  width: 1300px;
  border: white 2px solid;
  border-radius: 40px;
  padding: 20px 40px;
  margin: 5px auto;
}
</style>