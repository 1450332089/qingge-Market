<template>
  <div>
    <div>
      <el-select v-model="searchMode" placeholder="请选择订单类型" style="width: 150px;margin-right: 10px">
        <el-option value="已支付" label="已支付"></el-option>
        <el-option value="已发货" label="已发货"></el-option>
        <el-option value="已收货" label="已收货"></el-option>
      </el-select>
      <el-input v-model="searchText" @keyup.enter.native="load" style="width: 200px"> <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
      <el-button @click="reset" type="warning" style="margin: 10px">重置</el-button>
      <el-button @click="load" type="primary" style="margin: 10px">搜索</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="50" sortable> </el-table-column>
      <el-table-column prop="orderNo" label="订单编号" width="200"></el-table-column>
      <el-table-column prop="totalPrice" label="总价" width="100"></el-table-column>
      <el-table-column prop="userId" label="下单人id" width="100"></el-table-column>
      <el-table-column prop="linkUser" label="联系人" width="150"></el-table-column>
      <el-table-column prop="linkPhone" label="联系电话"></el-table-column>
      <el-table-column prop="linkAddress" label="送货地址" width="300"></el-table-column>
      <el-table-column prop="state" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.state==='已支付'">{{scope.row.state}}</el-tag>
          <el-tag type="primary" v-if="scope.row.state==='已发货'">{{scope.row.state}}</el-tag>
          <el-tag type="info" v-if="scope.row.state==='已收货'">{{scope.row.state}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间"></el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="mini"  @click="showDetail(scope.row)">详情</el-button>
          <el-popconfirm
              @confirm="delivery(scope.row)"
              title="确定发货吗？"
              v-if="scope.row.state==='已支付'"
          >
            <el-button type="primary" size="mini" slot="reference" style="margin-left: 10px">发货</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
<!--    分页-->
    <div style="margin-top: 10px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[3, 5, 8, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
<!--    详情弹窗-->
    <el-dialog :visible.sync="dialogFormVisible">
      <el-table :data="detail" background-color="black" >
        <el-table-column  label="图片" width="150" >
          <template   slot-scope="scope">
            <img :src="scope.row.img"  min-width="100" height="100" />
          </template>
        </el-table-column>

        <el-table-column prop="goodId" label="商品id"  ></el-table-column>
        <el-table-column prop="goodName" label="商品名称"  ></el-table-column>
        <el-table-column prop="standard" label="商品规格"  ></el-table-column>
        <el-table-column prop="price" label="单价"  ></el-table-column>
        <el-table-column prop="discount" label="折扣"></el-table-column>
        <el-table-column label="实价" >
          <template slot-scope="scope">
            {{scope.row.price * scope.row.discount}}
          </template>
        </el-table-column>
        <el-table-column prop="count" label="数量" ></el-table-column>
        <el-table-column label="总价" >
          <template slot-scope="scope">
            {{scope.row.price * scope.row.discount * scope.row.count }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import API from '../../utils/request'
const url = "/api/order/"

export default {
  name: "Order",
  data() {
    return {
      options: [],
      searchMode: '',
      searchText: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 8,
      entity: {},
      total: 0,
      dialogFormVisible: false,
      detail:[],
    };
  },
  created() {
    this.load()
  },
  methods: {
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    reset(){
      this.searchMode = '';
      this.searchText = '';
      this.load()
    },
    load() {
       API.get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            orderNo: this.searchText,
            state: this.searchMode
          }
       }).then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total
       })
    },
    showDetail(row){
        this.request.get("/api/order/orderNo/"+row.orderNo).then(res=>{
          if(res.code==='200'){
            this.detail=[];
            this.detail.push(res.data);
            this.dialogFormVisible = true;
          }
        })
    },
    //发货
    delivery(order){
        this.request.get("/api/order/delivery/"+order.orderNo).then(res=>{
          if(res.code==='200'){
            this.$message.success("成功发货");
            order.state = '已发货'
          }
        })
    },

  },
};
</script>

<style scoped>
</style>
