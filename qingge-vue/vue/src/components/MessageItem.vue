<template>
<div>
  <div style="float: left">
    <img :src="message.avatar" class="avatar"/>
  </div>

  <div style="display: inline-block;overflow: hidden;width: 800px">
    <div>
      <span class="user">{{message.nickname}}</span>

      <el-popconfirm
          @confirm="delMessage"
          title="确定删除？"
      >
        <el-button size="mini" style="float: right" slot="reference"  v-show="message.userId===userId">删除</el-button>
      </el-popconfirm>
    </div>
    <div style="margin: 12px 0">
      {{message.message}}
    </div>
    <span style="font-size: 10px">{{ message.createTime }}</span>
  </div>


</div>
</template>

<script>
export default {
  name: "MessageItem",
  props:{
    message: Object,
    userId: Number,
    index: Number,
  },
  created() {
  },
  methods:{
    delMessage(){
      console.log('del')
      this.request.delete("/api/market/message/"+this.message.id).then(res=>{
        if(res.code==='200'){
          this.$message.success("删除成功")
          this.$emit("del-message",this.index)
        }else {
          this.$message.error("删除失败")
        }
      })
    }
  }
}
</script>

<style scoped>
.avatar{
  width: 55px;
  height: 55px;
  border-radius: 50%;
  position: relative;
  top: 4px;
}
.user{
  font-size: 14px;
  font-weight: inherit;
  color: #f69090;
  position: relative;
  word-wrap: break-word;
  line-height: 16px;
}
</style>