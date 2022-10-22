<template>
  <div>
    <div style="padding: 5px 0">
      <el-input v-model="searchText" @keyup.enter.native="load" style="width: 200px"> <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
      <el-button @click="load" type="primary" style="margin: 10px">搜索</el-button>
      <el-button @click="add" type="primary" style="margin: 10px">新增</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" sortable> </el-table-column>
      <el-table-column prop="count" label="商品数量"></el-table-column>
      <el-table-column prop="createTime" label="加入时间"></el-table-column>
      <el-table-column prop="goodsId" label="商品id"></el-table-column>
      <el-table-column prop="standard" label=""></el-table-column>
      <el-table-column prop="userId" label="用户id"></el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle  @click="edit(scope.row)"></el-button>
          <el-popconfirm
              @confirm="del(scope.row.id)"
              title="确定删除？"
          >
            <el-button type="danger" icon="el-icon-delete" circle slot="reference" style="margin-left: 10px"></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
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

    <!-- 弹窗   -->
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">
      <el-form :model="entity">
        <el-form-item label="商品数量" label-width="150px">
          <el-input v-model="entity.count" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="加入时间" label-width="150px">
          <el-date-picker style="width: 80%" v-model="entity.createTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="商品id" label-width="150px">
          <el-input v-model="entity.goodsId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="" label-width="150px">
          <el-input v-model="entity.standard" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="用户id" label-width="150px">
          <el-input v-model="entity.userId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import API from '../../utils/request'
const url = "/api/cart/"

export default {
  name: "Cart",
  data() {
    return {
      options: [],
      searchText: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      dialogFormVisible: false
    };
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
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
    load() {
       API.get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.searchText
          }
       }).then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total
       })
    },
    add() {
      this.entity = {}
      this.dialogFormVisible = true
    },
    edit(row) {
      this.entity = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    save() {
        API.post(url, this.entity).then(res => {
           if (res.code === '200') {
             this.$message.success("保存成功")
             this.load()
             this.dialogFormVisible = false
           } else {
             this.$message.error(res.msg)
           }

        })
    },
    del(id) {
      API.delete(url + id).then(res => {
          if(res.code==='200'){
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.load();
          }else {
            this.$message.error(res.msg);
          }
      })
    }
  },
};
</script>

<style scoped>
</style>
