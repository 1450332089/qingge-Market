<template>
<div class="list">
  <template v-for="order in orders">
    <order-item :order="order" style="margin-bottom: 5px"></order-item>
  </template>
</div>
</template>

<script>
import OrderItem from "@/components/OrderItem";
import API from "@/utils/request";
export default {
  name: "OrderList",
  components:{
    'order-item': OrderItem,
  },
  data() {
    return{
      orders: {},
    }
  },
  created() {
    API.get("/userid").then(res=>{
      API.get("/api/order/userid/"+ res).then(res=>{
        if(res.code==='200'){
          this.orders = res.data;
          console.log(this.orders)
        }
      })
    })
  }
}
</script>

<style scoped>
.list{
  width: 60%;
  margin: 10px auto;

}
</style>