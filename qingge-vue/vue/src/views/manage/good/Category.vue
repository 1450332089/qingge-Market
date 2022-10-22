<template>
  <div >
    <div style="width: 60%;margin: 30px auto">
      <el-table :data="icons"  stripe >
        <!--      下级分类表-->
        <el-table-column type="expand" label="下级分类" width="100px">
          <template slot-scope="scope">
            <el-table :data="scope.row.categories" :header-cell-style="{background:'#cbefea',color: 'black'}" >
              <el-table-column label="分类id" prop="id"></el-table-column>
              <el-table-column label="分类名称" prop="name"></el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button type="primary" size="mini" @click="handleEditCategory(scope.row)">修改</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
          <!---->
        </el-table-column>
        <el-table-column label="id" prop="id" width="60px"></el-table-column>
        <el-table-column label="icon">
          <template slot-scope="scope">
            <i class="iconfont" v-html="scope.row.value"></i>
          </template>
        </el-table-column>

        <el-table-column
            fixed="right"
            label="操作"
            width="200">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle  @click="handleEditIcon(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

<!--icon修改弹窗-->
    <el-dialog title="修改图标" :visible.sync="dialogFormVisible">
      <el-form :model="icon">
        <el-form-item label="图标" label-width="100px">
          <i class="iconfont" v-html="icon.value"></i>
        </el-form-item>
        <el-form-item label="更改图标" label-width="100px">
          <el-select  placeholder="请选择图标"  v-model="icon.value">
            <el-option v-for="item in iconStore" :value="item"  :key="item">
              <i class="iconfont" v-html="item"></i>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveIcon">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import API from '../../../utils/request'
import icons from '@/utils/icons'
export default {
  name: "Category",
  data() {
    return {
      options: [],
      searchText: '',
      user: {},
      //从icons.js中引入常量iconStore
      iconStore: icons.iconStore,
      icons: [],
      icon: {},
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      dialogFormVisible: false,
    };
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.load()
    console.log(this.iconStore)
  },
  methods: {
    load() {
       API.get("/api/icon").then(res => {
          this.icons = res.data
         console.log(this.icons)
       })
    },
    handleEditCategory(category){
      this.$prompt('请输入修改后的名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        category.name = value;
        this.request.post("/api/category",category).then(res=>{
          if(res.code === '200'){
            this.$message.success("修改成功")
          }else{
            this.$message.error("修改失败")
          }
        })
      })
    },
    handleEditIcon(icon){
      this.icon = JSON.parse(JSON.stringify(icon));
      this.dialogFormVisible = true;
    },
    saveIcon(){
      //删除无用的属性
      delete this.icon.categories
      this.request.post("/api/icon",this.icon).then(res=>{
        if(res.code==='200'){
          this.$message.success("修改成功");
          this.dialogFormVisible=false;
        }else{
          this.$message.error("修改失败")
        }
      })
    },
  },
};
</script>

<style scoped>
@import "../../../resource/css/icon.css";
</style>
