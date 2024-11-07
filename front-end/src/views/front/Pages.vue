<template>
  <div class="main-content">

    <div style="display: flex; margin-bottom: 10px">
      <div style="flex: 1">
        <el-button type="primary" class="btn-green" size="medium" @click="handleAdd">Create New questionnaire</el-button>
      </div>
      <div>
        <el-input v-model="name" style="width: 300px; margin-right: 10px" size="medium" placeholder="Please enter a name to search for questionnaires" clearable></el-input>
        <el-button type="primary" size="medium" class="btn-green" @click="load(1)">search</el-button>
      </div>
    </div>

    <div style="margin-bottom: 10px">
      <div class="card" v-for="item in tableData" :key="item.id" style="margin-bottom: 5px">
        <div style="display: flex;">
          <div style="flex: 1; width: 0;">
            <div style="display: flex; align-items: center; margin-bottom: 10px">
              <el-tag v-if="item.saved === 'No'" type="warning">Not Published</el-tag>
              <el-tag v-if="item.saved === 'Yes'" type="success">Published</el-tag>
              <span style="font-size: 20px; margin-left: 10px">{{ item.name }}</span>
            </div>

            <div>
              <el-button type="text" style="color: #2a60c9; font-size: 16px" @click="handleEdit(item)"><i class="el-icon-edit"></i> Edit</el-button>
              <el-button type="text" style="color: #2a60c9; font-size: 16px" @click="design(item.id)"><i class="el-icon-setting"></i> Design</el-button>
              <el-button type="text" style="color: #2a60c9; font-size: 16px" @click="copy(item.id)"><i class="el-icon-document-copy"></i> Copy</el-button>
              <el-button type="text" style="color: #fc4b4b; font-size: 16px" v-if="item.open === 'No'" @click="del(item.id)"><i class="el-icon-delete"></i> Delete</el-button>
              <el-button type="text" style="color: #2a60c9; font-size: 16px" @click="share(item.id)" v-if="item.saved === 'Yes'"><i class="el-icon-share"></i> Share</el-button>
              <el-button type="text" style="color: orange; font-size: 16px" @click="getPageCount(item.id)" v-if="item.saved === 'Yes'"><i class="el-icon-s-marketing"></i> Data</el-button>
            </div>
          </div>

          <div style="width: 100px">
            <img :src="item.img" alt="" style="width: 100%; border-radius: 5px; display: block">
          </div>
        </div>
      </div>
    </div>

    <div v-if="total">
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
        <el-form-item label="name" prop="name">
          <el-input v-model="form.name" placeholder="name"></el-input>
        </el-form-item>
        <el-form-item label="description" prop="descr">
          <el-input v-model="form.descr" placeholder="description"></el-input>
        </el-form-item>
        <el-form-item label="img" prop="img">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Publish or Not" prop="open" v-if="user.role === 'ADMIN'">
          <el-radio-group v-model="form.open">
            <el-radio label="Yes"></el-radio>
            <el-radio label="No"></el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Data" :visible.sync="pageCountVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-if="pageCount.answerCount">
        <div style="margin-bottom: 10px">
          <span>Cllect <strong>{{ pageCount.answerCount }}</strong> Results</span>
        </div>

        <div class="card" v-for="(item, index) in pageCount.questionList" :key="index" style="margin-bottom: 10px">
          <div style="margin-bottom: 10px"><strong>Q: {{ item.name }}</strong></div>
          <div v-if="item.questionItemList?.length">
            <el-table :data="item.questionItemList" v-if="item.type === 'Single Choice' || item.type === 'Multiple Choice'">
              <el-table-column label="content" prop="content"></el-table-column>
              <el-table-column label="count" prop="count"></el-table-column>
              <el-table-column label="percentage">
                <template v-slot="scope">
                  <el-progress :percentage="scope.row.percentage" :color="randomColor"></el-progress>
                </template>
              </el-table-column>
            </el-table>

            <el-table :data="item.questionItemList" v-else>
              <el-table-column label="Content" prop="content"></el-table-column>
            </el-table>
          </div>
          <div style="margin-top: 10px">Answer Number:{{ item.count  }}</div>
        </div>

      </div>
      <div v-else style="text-align: center; color: #888">
        You have not collected any survey results
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="pageCountVisible = false">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Share" :visible.sync="shareVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <div style="display: flex; align-items: center">
        <span style="margin-right: 5px">Link</span> <el-input v-model="link" style="flex: 1; margin-right: 5px" />
        <el-button type="primary" @click="copyLink">Copy</el-button>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="shareVisible = false">Close</el-button>
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
      form: {},
      rules: {
        name: [
          {required: true, message: 'Please enter a name', trigger: 'blur'},
        ],
        descr: [
          {required: true, message: 'Please enter a description', trigger: 'blur'},
        ]
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      pageCount: {},
      pageCountVisible: false,
      shareVisible: false,
      link: '',
      baseUrl: process.env.VUE_APP_BASE_API
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    copy(pageId) {
      this.$request.post('/pages/copy?pageId=' + pageId).then(res => {
        if (res.code === '200') {
          this.$message.success("success！")
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    randomColor() {
      let arr = ['#f56c6c', '#e6a23c', '#5cb87a', '#1989fa', '#6f7ad3']
      return arr[Math.floor(Math.random() * arr.length)]  //  0.8 * 1  = 0
    },
    copyLink() {
      let _input = document.createElement("input");   // 直接构建input

      _input.value = this.link;  // 设置内容
      document.body.appendChild(_input);    // 添加临时实例
      _input.select();   // 选择实例内容
      document.execCommand("Copy");   // 执行复制
      document.body.removeChild(_input)
      this.$message.success("success")
    },
    share(pageId) {
      this.link = location.href.substring(0, location.href.indexOf('/front')) + '/realPage?pageId=' + pageId
      this.shareVisible = true
    },
    getPageCount(pageId) {
      this.$request.get('/pageCount', {
        params: {
          pageId: pageId
        }
      }).then(res => {
        this.pageCount = res.data || {}
        this.pageCountVisible = true
      })
    },
    del(pageId) {   // 单个删除
      this.$confirm('Are you sure you want to delete it?', 'Yes', {type: "warning"}).then(response => {
        this.$request.delete('/pages/delete/' + pageId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('success')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    design(pageId) {
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
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/pages/update' : '/pages/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('success')
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
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImgSuccess(res) {
      this.form.img = res.data  // 把后台返回的图片url赋值给表单属性 img
    },
  }
}
</script>

<style scoped>

</style>