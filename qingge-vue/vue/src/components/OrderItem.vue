<template>
<div>
  <div class="header">
    <span style="line-height: 40px">{{order.create_time}}</span>
    <span style="line-height: 40px;margin-left: 30px">订单编号： {{order.order_no}}</span>
  </div>
  <div class="body">
    <div style="display: inline-block;margin-right: 20px">
      <router-link :to="'goodview/'+order.good_id">
        <img :src="order.imgs" style="width: 100px;height:100px">
      </router-link>
    </div>
    <div style="display: inline-block;line-height: 40px" >
      <table>
        <tr>
          <th>商品</th>
          <th>规格</th>
          <th>数量</th>
          <th>总价</th>
          <th>收货人</th>
          <th>订单状态</th>
        </tr>
        <tr>
          <a :href="'goodview/'+order.good_id">
            <td>{{order.good_name}}</td>
          </a>
          <td>{{order.standard}}</td>
          <td>{{order.count}}</td>
          <td>{{order.total_price}}</td>
          <el-popover
              placement="bottom-start"
              width="200"
              trigger="hover"
              :content=address>
            <td slot="reference" style="color: #42b983">{{ order.link_user }}</td>
          </el-popover>
<!--          订单状态-->
          <template v-if="order.state==='已发货'">
            <td style="color: #42b983">{{order.state}}</td>
            <td>
              <el-button style="margin-left: 20px;" size="mini" type="primary" @click="receive">确认收货</el-button>
            </td>
          </template>

          <template v-else-if="order.state==='已收货'">
            <td style="color: #42b983"><a class="el-icon-check"></a>{{order.state}}</td>
          </template>

          <template v-else-if="order.state==='已支付'">
            <td style="color: #3b62f8"> {{order.state}}</td>
            <td>
              <el-button size="mini" type="info" plain disabled>等待发货</el-button>
            </td>
          </template>

          <template v-else>
            <td>{{order.state}}</td>
            <td>
              <el-button style="margin-left: 20px" size="mini" type="success" @click="pay">去支付</el-button>
            </td>
          </template>

        </tr>
      </table>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: "OrderItem",
  props:{
    order: Object,
  },
  created() {
    console.log(this.order)
  },
  data(){
    return{
      address: '电话:'+this.order.link_phone+' 地址:'+this.order.link_address,
    }
  },
  methods:{
    //跳转到支付页面
    pay(){
      this.$router.push({path: 'pay',query:{money: this.order.total_price,orderNo: this.order.order_no}})
    },
    //确认收货
    receive(){

      this.$confirm('是否确认收货?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {

        this.request.get("/api/order/received/"+this.order.order_no).then(res=>{
          if(res.code==='200'){
            this.$message.success("收货成功");
            this.order.state='已收货'
          }
        })
      })

    }
  }
}
</script>

<style scoped>
.header{
  background-color: #daf3ff;
  height: 40px;
}
.body{
  background-color: white;
  padding: 20px;
}
th,td{

  width: 120px;
  text-align: center;
}
th{
  font-size: 15px;
  color: #00b7ff;
  font-weight: normal;
}
</style>