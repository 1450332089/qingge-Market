<template>
  <div>
    <div class="main-box">
<!--      选择学校-->
      <span style="margin-right: 20px">请选择学校</span>
      <el-select v-model="schoolId" placeholder="请选择" @change="changeSchool">
        <el-option v-for="item in schools" :key="item.id" :value="item.id" :label="item.name">
        </el-option>
      </el-select>
<!--      商品列表-->
      <div style="width: 1300px;margin: 20px auto;">
        <el-row :gutter="20">
          <el-col :span="6" v-for="good in good" :key="good.id" style="margin-bottom: 20px ">
            <!--            商品格子-->
            <router-link :to="'/marketGood/'+good.id">
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
    <div class="footer">
      <button class="up-button" @click="handleAddGood">发布</button>
    </div>
  </div>


</template>

<script>
import request from "@/utils/request";
const url = "/api/market"
export default {
  name: "SchoolMarket",
  data(){
    return{
      schoolId: '',
      schools:[],
      good: []
    }
  },
  created(){
    this.request.get(url+"/schools").then(res=>{
      if(res.code==='200'){
        let schools = res.data;
        this.schools = schools;
        this.schoolId = schools[0].id;
        this.load();
      }
    })
  },
  methods:{
    changeSchool(){
      this.load();
    },
    load(){
      this.request.get(url+"/schoolGood/"+this.schoolId).then(res=>{
        this.good = res.data;
      })
    },
    handleAddGood(){
      this.$router.push("/addMarketGood")
    },
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
.footer {
  position: fixed;
  bottom: 0;
  color: #333;
  width: 100%;
  text-align: center;
  line-height: 60px;
}
.up-button{
  color: #131313;
  padding: 12px 25px;
  font-size: 18px;
  background-color: #ffb02a;
  border-radius: 10px;
}
</style>