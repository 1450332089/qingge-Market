<template>
  <div>
    <div class="header">
      销量第{{index}}
    </div>
    <div class="body">
      <div style="display: inline-block;margin-left: 40px">
        <router-link :to="'/goodview/'+good.id">
          <img :src="good.imgs" style="width: 120px;height:120px">
        </router-link>
      </div>
      <div style="display: inline-block;line-height: 40px;padding: 20px" >
        <table>
          <tr>
            <th>商品id</th>
            <th style="width: 300px">商品名称</th>
            <th>分类</th>
            <th>销售额</th>
          </tr>
          <tr>
            <td>{{good.id}}</td>
            <router-link :to="'/goodview/'+good.id">
              <td style="width: 300px">{{good.name}}</td>
            </router-link>
            <td>{{categories[good.categoryId-1].name}}</td>
            <td>￥{{good.saleMoney}}</td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "IncomeItem",
  props:{
    good: Object,
    index: Number,
    categories: Array
  }
}
</script>

<style scoped>
.header{
  background-image: linear-gradient(to bottom right, #e0f8bc,rgba(0,183,255,0.43));
  padding: 10px;
  color: #e04d76;
  font-size: 20px;
  font-weight: bolder;
}
.body{
  background-color: #daf3ff;
  padding: 5px;
}
td{
  width: 120px;
  text-align: center;
  font-size: 20px;
  font-weight: lighter;
}
th{
  width: 120px;
  text-align: center;
  font-size: 18px;
  color: #00b7ff;
  font-weight: normal;
}
a{
text-decoration: none;

}
</style>