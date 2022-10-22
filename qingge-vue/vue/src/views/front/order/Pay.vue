<template>
<div style="margin-top: 10px;width: 50%;margin: 10px auto;background-color: white;text-align: center">
  <table style="margin: 20px auto">
    <tr>
      <th>订单号</th>
      <td>{{orderNo}}</td>
    </tr>

    <tr>
      <th>金额</th>
      <td>{{money}} 元</td>
    </tr>

  </table>
  <hr style="width: 280px" />
  <div>
    <span>支付方式：</span>
  </div>
  <img src="../../../resource/img/微信支付.png" style="width: 90px;height: 90px;margin-right: 30px" @click="pay">
  <img src="../../../resource/img/支付宝.png" style="width: 100px;height: 100px" @click="pay">
</div>
</template>

<script>
export default {
  name: "Pay",
  data(){
    return{
      userId: 0,
      money1: 0,
      orderNo: '',
    }
  },
  created() {
    this.money = parseFloat(this.$route.query.money).toFixed(2);
    this.orderNo = this.$route.query.orderNo;
  },

  methods:{
    pay(){
      this.request.get("/api/order/paid/"+this.orderNo).then(res=>{
        if(res.code==='200'){
          alert("您成功支付"+this.money+"元")
          this.$router.replace("/orderlist")
        }else{
          this.$message.error(res.msg)
        }
      })

    }
  }

}
</script>

<style scoped>
tr{
  line-height:40px;
}

</style>