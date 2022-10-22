<template>
  <div>
    <div>
      <el-table :data="tableData" border stripe style="width: 80%;margin: 2px auto">
        <el-table-column label="商品">
          <template slot-scope="scope">
            <a :href="'/goodView/'+scope.row.goodId">{{scope.row.goodName}}</a>
          </template>
        </el-table-column>
        <el-table-column  label="图片" >
          <template   slot-scope="scope">
            <img :src="scope.row.img" width="300" height="185" />
          </template>
        </el-table-column>
        <el-table-column prop="showOrder" label="轮播顺序"></el-table-column>

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
    </div>
<!--新增按钮-->
    <div style="text-align: center">
      <el-button @click="add" type="primary" style="margin: 30px;width: 150px">新增</el-button>
    </div>
    <!-- 弹窗   -->

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">
      <el-form :model="entity">
        <el-form-item label="商品id" label-width="150px">
          <el-input v-model="entity.goodId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="轮播顺序" label-width="150px">
<!--          <el-input v-model="entity.showOrder" autocomplete="off" style="width: 80%"></el-input>-->
          <el-select v-model="entity.showOrder">
            <el-option v-for="index in tableData.length" :key="index" :label="index" :value="index">
            </el-option>
          </el-select>
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
import API from '../../../utils/request'
const url = "/api/carousel/"

export default {
  name: "Carousel",
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
      API.get(url).then(res => {
        this.tableData = res.data || []
      })
    },
    add() {
      this.entity = {}
      this.tableData.length++;
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
