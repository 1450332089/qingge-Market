<template>
  <el-card class="card">
    <div style="text-align: center;margin-bottom: 30px;color: #ffb02a"><h1>发布商品</h1></div>

    <el-form label-width="60px" :model="good">
      <el-form-item label="学校">
        <el-select v-model="schoolId" placeholder="请选择">
          <el-option v-for="item in schools" :key="item.id" :value="item.id" :label="item.name">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图片">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8888/file/upload"
            :headers="token"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="good.img" :src="good.img" class="picture">

          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>

      <el-form-item label="商品名">
        <el-input v-model="good.goodName"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input type="textarea" v-model="good.description"></el-input>
      </el-form-item>
      <el-form-item label="价格">
        <el-input v-model="good.price" ></el-input>
      </el-form-item>
      <el-button type="primary" style="margin-left: 190px;margin-top: 20px" @click="save">确 定</el-button>
    </el-form>
  </el-card>

</template>

<script>
export default {
  name: "AddGood",
  data(){
    return{
      schoolId: '',
      schools:[],
      good: {
        img: '',
        goodName: '',
        description: '',
        price: '',
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  methods:{
    //图片上传成功钩子
    handleAvatarSuccess(res) {
      this.good.img = res.data;
    },
    //提交事件
    save(){
      this.request.post("/api/market/good?schoolId="+this.schoolId,this.good).then(res=>{
        if(res.code==='200'){
          this.$message.success("发布成功")
        }
        console.log(res)
      })
    },
  },
  created() {
    this.request.get("/api/market/schools").then(res=>{
      if(res.code==='200'){
        let schools = res.data;
        this.schools = schools;
      }
    })
  },
  computed:{
    token() {
      return{ token: this.user.token}
    }
  }
}
</script>

<style scoped>
.card{
  width: 600px;
  margin: 30px auto;
  padding: 30px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  border: #cccccc 1px solid;
  font-size: 28px;
  color: #8c939d;
  width: 280px;
  height: 280px;
  line-height: 280px;
  text-align: center;
}

.picture {
  width: 280px;
  height: 280px;
  display: block;
}
</style>