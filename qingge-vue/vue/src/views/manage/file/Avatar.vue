<template>
  <div>
    <!--          表格-->
    <el-table :data="tableData" background-color="black" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" ></el-table-column>
      <el-table-column  label="头像" width="150" >
        <template   slot-scope="scope">
          <img :src="scope.row.url"  min-width="100" height="100" />
        </template>
      </el-table-column>

      <el-table-column prop="type" label="文件类型" width="180" ></el-table-column>
      <el-table-column prop="size" label="文件大小" width="180" ></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">

<!--          下载-->
          <a :href="scope.row.url">
            <el-button
              size="mini"
              type="success"
              >下载
            </el-button>
          </a>
<!--          删除-->
          <el-button
              size="mini"
              type="danger"
              style="margin-left: 10px"
              @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="flex: 0 0 auto">
      <el-pagination
          :current-page="currentPage"
          :page-sizes="[3, 5, 8, 10]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentPage"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Avatar",
  created() {
    this.load();
  },
  data(){
    return{
      tableData: [],
      total: 0,
      pageSize: 5,
      currentPage: 1,
      multipleSelection: []
    }
  },
  methods:{
    handleSizeChange(pageSize){
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentPage(currentPage){
      this.currentPage = currentPage;
      this.load();
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    load(){
      this.request.get("/avatar/page",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
        }
      }).then(res=>{
            if(res.code==='200'){
              this.tableData = res.data.records;
              for(let s of this.tableData){
                let size = s.size;
                if(size<1024){
                  s.size = size+' Kb';
                }else if(size >1024 && size < 1024*1024){
                  s.size = (size / 1024).toFixed(2) +' Mb'
                }else{
                  s.size = size /1024/1024 .toFixed(2)+' Gb'
                }
              }
              this.total = res.data.total;
            }
          }
      )
    },
    search(){
      this.currentPage = 1;
      this.load();
    },
    // //编辑
    // handleEdit(row){
    //   this.user = JSON.parse(JSON.stringify(row));
    //   this.dialogTitle='编辑用户';
    //   this.dialogFormVisible = true;
    // },

    //删除
    handleDelete(id){
      this.$confirm('确认删除该文件吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete("/avatar/"+id).then(res=>{
          if(res.code==='200'){
            this.$message({
              type: "success",
              message: "删除成功",
              duration: 3000
            });
            this.load();
          }else {
            this.$message.error(res.msg);
          }
        })
      })
    },
  },
}
</script>

<style scoped>

</style>