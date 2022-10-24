<template>
<div class="wrapper" >
 <div class="login-box">

      <div class="title">
        <b>注 册</b>
      </div>
      <div style="margin-top: 30px">
        <el-form label-width="70px">
          <el-form-item label="用户名">
            <el-input v-model.trim="user.username" aria-required="true"></el-input>
          </el-form-item>
          <el-form-item label="密码" style="margin-top: 25px">
            <el-input v-model.trim="user.password"show-password aria-required="true"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" style="margin-top: 25px">
            <el-input v-model.trim="user.confirmPassword" show-password aria-required="true"></el-input>
          </el-form-item>
          <el-form-item style="margin: 30px 80px">
            <el-button type="success" @click="onSubmit">注册</el-button>
            <el-button @click="$router.push('/login')">返回</el-button>
          </el-form-item>
        </el-form>
      </div>



 </div>
</div>
</template>

<script>
import md5 from "js-md5";

export default {
  name: "Login",
  data(){
    return {
      user: {},
    }
  },
  methods: {
    onSubmit() {
      if(this.user.username==='' || this.user.password==='' ||this.user.confirmPassword===''){
        this.$message.error("账号或密码不能为空")
        return false;
      }
      if(this.user.password!==this.user.confirmPassword){
        this.$message.error("两次密码不一致")
        return false;
      }
      this.user.password = md5(this.user.password);
      this.request.post("/register",this.user).then(res=>{
        if(res.code==='200'){
          this.$message.success("注册成功")
          this.$router.push("/login")
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to top right,#42b983,#B3C0D1);
  overflow: auto;
}
.login-box {
  margin: 220px auto;
  padding: 40px;
  width: 450px;
  height: 310px;
  background-color: #ffffff;
  border-radius: 10px;
}
.title {
  text-align: center;
  margin-top: 10px;
  font-size: 25px;
  color: #42b983
}
</style>