<template>
  <div style="margin-top: 10px;width: 90%;margin: 10px auto;">
    <div style="background-color: white; padding: 10px;border-radius: 12px">
      <!--收货地址-->
      <div style="padding: 10px; margin-bottom: 20px; border-bottom: 1px solid #eee">
        <div style="font-size: 20px; border-bottom: 2px solid dodgerblue; padding-bottom: 10px; margin-bottom: 20px">
          收货地址
          <el-button style="height: 25px;padding: 5px" @click="addAddress">+</el-button>
        </div>
        <template style="padding: 2px 0; color: #555" v-for="(item,index) in addressData">
          <address-box :address="item"
                       :key="index"
                       :class="index === checkedIndex?'active':' '"
                       style="margin-right: 20px"
                       @edit="editAddress(item)"
                       @delete="deleteAddress(item)"
                       @click.native="select(index)"></address-box>
        </template>
      </div>
<!--      地址弹窗-->
      <el-dialog title="地址信息" :visible.sync="dialogFormVisible">
        <el-form label-width="90px" style="padding: 0 60px">
          <el-form-item label="联系人">
            <el-input v-model="address.linkUser" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="address.linkPhone" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="address.linkAddress" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAddress">确 定</el-button>
        </div>
      </el-dialog>


      <!--        商品确认-->
      <el-table :data="good" stripe style="width: 100%">
        <el-table-column label="商品图片" width="150">
          <template slot-scope="scope">
            <el-image :src="scope.row.imgs" style="width: 100px; height: 100px;" fit="contain"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="standard" label="规格"></el-table-column>
        <el-table-column prop="realPrice" label="单价"></el-table-column>
        <el-table-column prop="num" label="数量"></el-table-column>
        <el-table-column label="价格">
          <template slot-scope="scope">
            {{ (scope.row.realPrice * scope.row.num).toFixed(2) }}
          </template>
        </el-table-column>

      </el-table>

      <div style="margin-top: 10px">
        <div style="background-color: white; padding: 10px">
          <div style="color: red; text-align: right">
            <div>
              <span>总价：</span>
              <span>￥ {{ sumPrice }}</span>
            </div>
            <div style="text-align: right; color: #999; font-size: 12px;">
              优惠： ￥{{ sumDiscount }}
            </div>
            <div style="padding: 10px 0">
              <el-button style="background-color: red; color: white; width: 100px" @click="submitOrder">提交订单</el-button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import API from "@/utils/request";
import addressBox from "@/components/AddressBox"
export default {
  name: "cart",
  data() {
    return {
      userId: 0,
      addressData: [],
      //临时存储地址信息
      address:{},
      checkedIndex : 0,
      dialogFormVisible: false,
      good: {},
      realPrice: -1,
    }
  },
  components:{
    'address-box': addressBox,
  },
  created() {

    this.loadAddress();

    this.good = JSON.parse(this.$route.query.good)
    this.good.realPrice = this.$route.query.realPrice
    this.good.num = this.$route.query.num
    this.good.standard = this.$route.query.standard
    this.good.push(this.good)
  },
  computed: {
    sumPrice: function () {
      let sum = 0;
      this.good.forEach(function (good){
        sum += good.realPrice * good.num;
      })
      return sum.toFixed(2);
    },
    sumDiscount: function (){
      let sum = 0;
      this.good.forEach(function (good){
        sum += (good.realPrice / good.discount -good.realPrice) * good.num;
      })
      return sum.toFixed(2);
    }
  },
  methods: {
    select(index){
      console.log(index)
      this.checkedIndex = index;
    },
    addAddress(){
      this.address = {};
      this.dialogFormVisible = true;
    },
    editAddress(item){
      //深拷贝
      this.address = JSON.parse(JSON.stringify(item));
      this.dialogFormVisible = true;
    },
    deleteAddress(item){
      this.$confirm('您确认删除该地址吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        API.delete("api/address/"+item.id).then(res=>{
          if(res.code==='200'){
            this.$message.success("删除地址成功");
            this.loadAddress();
          }
        })
      })

    },
    saveAddress(){
      this.address.userId = this.userId;
      API.post("/api/address",this.address).then(res=>{
        if(res.code==='200'){
          this.$message.success("保存成功");
          this.loadAddress();
          this.dialogFormVisible = false;
        }else {
          this.$message.error(res.msg);
        }
      })
    },
    loadAddress(){
      API.get("/userid").then(res=>{
        this.userId = res;
        API.get("/api/address/"+ res).then(res=>{
          if(res.code==='200'){
            this.addressData = res.data;
            console.log("address"+this.addressData)
          }
        })
      })
    },

    submitOrder() {
      let address = this.addressData[this.checkedIndex];
      console.log(address)
      if (!address) {
        this.$message({
          type: 'warning',
          message: '请选择收货地址！'
        })
        return
      }
      console.log(JSON.stringify(this.good))
      // 提交订单
      API.post("/api/order", {
        totalPrice: this.sumPrice,
        linkUser: address.linkUser,
        linkPhone: address.linkPhone,
        linkAddress: address.linkAddress,
        state: '待付款',
        good: JSON.stringify(this.good),
      }).then(res => {
        if (res.code === '200') {
          let orderNo = res.data
          //跳转到支付页面
          this.$router.replace({path: 'pay',query:{money: this.sumPrice,orderNo: orderNo}})
        } else {
          this.$message({
            type: 'error',
            message: res.msg
          })
        }
      })
    },

  }
}
</script>

<style scoped>
.active{
  background-color: #f6c169;
  color: #ffffff;
  border: #f17878 2px solid;
}
</style>
