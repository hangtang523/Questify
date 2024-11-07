<template>
  <div class="main-content">

    <div style="display: flex; margin-bottom: 10px">
      <div style="flex: 1">
        <el-button type="primary" class="btn-green" size="medium" @click="handleAdd()">创建新的问卷</el-button>
      </div>
      <div>
        <el-input v-model="name" style="width: 300px; margin-right: 10px" size="medium" placeholder="请输入名称搜索问卷" clearable></el-input>
        <el-button type="primary" size="medium" class="btn-green" @click="load(1)">搜 索</el-button>
      </div>
    </div>

    <div style="margin-bottom: 10px">
      <div class="card" v-for="item in tableData" :key="item.id" style="margin-bottom: 5px">
        <div style="display: flex;">
          <div style="flex: 1; width: 0;">
            <div style="display: flex; align-items: center; margin-bottom: 10px">
              <el-tag v-if="item.saved === '否'" type="warning">未发布</el-tag>
              <el-tag v-if="item.saved === '是'" type="success">已发布</el-tag>
              <span style="font-size: 20px; margin-left: 10px">{{ item.name }}</span>
            </div>

            <div>
              <el-button type="text" style="color: #2a60c9; font-size: 16px" @click="handleEdit(item)"><i class="el-icon-edit" ></i> 编辑</el-button>
              <el-button type="text" style="color: #2a60c9; font-size: 16px" @click="design(item.id)"><i class="el-icon-setting" ></i> 设计</el-button>
              <el-button type="text" style="color: #2a60c9; font-size: 16px"><i class="el-icon-document-copy"></i> 复制</el-button>
              <el-button type="text" style="color: #2a60c9; font-size: 16px"><i class="el-icon-share"></i> 分享</el-button>
              <el-button type="text" style="color: #fc4b4b; font-size: 16px" @click="del(item.id)"><i class="el-icon-delete"></i> 删除</el-button>
              <el-button type="text" style="color: orange; font-size: 16px"><i class="el-icon-s-marketing"></i> 数据统计</el-button>
            </div>
          </div>

          <div style="width: 100px">
            <img :src="item.img" alt="" style="width: 100%; border-radius: 5px; display: block">
          </div>
        </div>
      </div>
    </div>

    <div>
      <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Information" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item label="Description" prop="descr">
          <el-input v-model="form.descr" placeholder="Description"></el-input>
        </el-form-item>
        <el-form-item label="Cover" prop="img">
          <el-upload
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="public" prop="open">
          <el-radio-group v-model="form.open">
            <el-radio label="Yes"></el-radio>
            <el-radio label="No"></el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">cancel</el-button>
        <el-button type="primary" @click="save">confirm</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "FPages",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 4,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      form: {
        /*name: null,
        descr: null,
        img: null,
        open: 'Yes'*/
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        descr: [
          { required: true, message: '请输入描述', trigger: 'blur' }
        ]
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    del(pageId) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/pages/delete/' + pageId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    design(pageId){
      window.open('/front/design?pageId=' + pageId)
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save(){
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/pages/update' : '/pages/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('save successful')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/pages/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleImgSuccess(res) {
      this.form.img = res.data
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>