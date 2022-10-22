<template>
  <el-card class="card">
    <div style="text-align: center;margin-bottom: 30px"><b >修改个人信息</b></div>

    <el-form label-width="60px">
      <el-form-item label="头像">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8888/avatar"
            :headers="token"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            >
          <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>

      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-button type="primary" style="margin-left: 190px;margin-top: 20px" @click="save">确 定</el-button>
    </el-form>
  </el-card>

</template>

<script>
export default {
  name: "Person",
  data(){
    return{
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

    }
  },
  methods:{
    //图片上传成功钩子
    handleAvatarSuccess(res) {
      this.imageUrl = res.data;
      this.form.avatarUrl = this.imageUrl;
    },
    //提交事件
    save(){
      //把表格传给后台，保存到数据库
      this.request.post("/user",this.form).then(res=>{
        if(res.code==='200'){
          this.$message.success("保存成功");
          //把表格的数据更新到user中
          for (let key in this.form){
            this.user[key] = this.form[key];
          }
          //更新localstorage的user
          localStorage.setItem('user',JSON.stringify(this.user));
          this.$emit("refresh")
          this.$router.go(0);
        }else{
          this.$message.error(res.msg)
        }
      })
    },
  },
  created() {
    this.request.get("/userinfo/"+this.user.username).then(res=>{
      if(res.code==='200'){
        this.form = res.data;
      }else{
        alert(res.msg)
      }
    })
    // this.form = this.user;
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
  width: 500px;
  margin: 80px auto;
  padding: 30px;
}
.avatar-uploader {
  padding-bottom: 10px;
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
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>