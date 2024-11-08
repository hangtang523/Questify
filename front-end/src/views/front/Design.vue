<template>
  <div class="main-content">
    <div class="card">
      <div style="display: flex; margin-bottom: 20px">
        <div style="flex: 1">
          <div>
            <span style="font-size: 24px; margin-right: 10px">{{ pages.name }}</span>
            <el-tag v-if="pages.saved === 'No'">Not Published</el-tag>
            <el-tag v-else type="success">Published</el-tag>
          </div>
        </div>
        <div>
          <el-button type="primary" @click="share()"><i class="el-icon-share"></i> Share</el-button>
          <el-button type="info" @click="preview(pages.id)"><i class="el-icon-video-play"></i> View</el-button>
          <el-button type="success" :disabled="pages.saved === 'Yes'" @click="handleSaved"><i
              class="el-icon-s-promotion"></i> Publish
          </el-button>
        </div>
      </div>

      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="addQuestion('Single Choice')">New Single Choice</el-button>
        <el-button type="info" @click="addQuestion('Multiple Choice')">New Multiple Choice</el-button>
        <el-button type="success" @click="addQuestion('Blank')">New Blank</el-button>
      </div>

      <div>
        <div v-for="(item, index) in questionList" :key="item.id" style="margin-bottom: 20px">
          <div>
            <span style="font-weight: bold; margin-right: 5px">Question{{ index + 1 }}</span>
            <el-input v-model="item.name" style="width: 70%; margin-right: 5px"
                      @change="changeQuestionName(item)"></el-input>
            <el-tag style="margin-right: 5px; color: #2a60c9" v-if="item.type === 'Single Choice'">{{ item.type }}</el-tag>
            <el-tag style="margin-right: 5px; color: orange" v-if="item.type === 'Multiple Choice'">{{ item.type }}</el-tag>
            <el-tag style="margin-right: 5px; color: seagreen" v-if="item.type === 'Blank'">{{ item.type }}</el-tag>
            <span style="color: red; cursor: pointer; font-size: 12px" @click="delQuestion(item.id)"><i
                class="el-icon-delete"></i>Delete</span>
          </div>
          <div style="padding-left: 40px" v-if="item.questionItemList?.length">
            <div v-for="(sub, subIndex) in item.questionItemList" :key="sub.id" style="margin: 10px 0">
              <div>
                <span style="margin-right: 5px">Options{{ subIndex + 1 }}</span>
                <el-input v-model="sub.content" style="width: 50%; margin-right: 5px"
                          @change="changeQuestionItem(sub)"></el-input>
                <span style="color: red; cursor: pointer; font-size: 12px" @click="delQuestionItem(sub.id)"><i
                    class="el-icon-delete"></i>Delete</span>
              </div>
            </div>
            <div>
              <span style="margin-right: 5px; color: #2a60c9">New Option</span>
              <el-input v-model="item.newContent" style="width: 50%; margin-right: 5px"></el-input>
              <el-button type="primary" class="btn-green" size="mini" @click="addQuestionItem(item)">Confirm add
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>


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
/*import logs from "@/views/manager/Logs";*/

export default {
  name: "Design",
  data() {
    return {
      pages: {},
      questionList: [],
      shareVisible: false,
      link: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    copyLink() {
      let _input = document.createElement("input");   // 直接构建input

      _input.value = this.link;  // 设置内容
      document.body.appendChild(_input);    // 添加临时实例
      _input.select();   // 选择实例内容
      document.execCommand("Copy");   // 执行复制
      document.body.removeChild(_input)
      this.$message.success("Copy success")
    },
    share() {
      if (this.pages.saved !==  'Yes') {
        this.$message.warning('Unpublished questionnaires are not allowed to be shared')
        return
      }

      this.link = location.href.substring(0, location.href.indexOf('/front')) + '/realPage?pageId=' + this.pages.id
      this.shareVisible = true
    },
    handleSaved() {
      if (!this.validate()) { // 校验没通过  不能发布
        return
      }
      this.$confirm('Are you confirming the release?？', 'Sure', {type: "warning"}).then(response => {
        let data = JSON.parse(JSON.stringify(this.pages))
        data.saved = 'Yes'
        this.$request.put('/pages/update/', data).then(res => {
          if (res.code === '200') {  // 表示成功保存
            this.$message.success('Success')
            this.load()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(err => console.log(err))
    },
    validate() {
      let flag = false  // 检查内容是否为空  当flag = true 的时候就是空的内容
      for (let i = 0; i < this.questionList.length; i++) {
        if (this.questionList[i].type === 'Single Choice' || this.questionList[i].type === 'Multiple Choice') {
          if (!this.questionList[i].questionItemList?.length) {
            this.$message.warning('Single choice and multiple choice questions require the addition of options')
            return false
          }
          this.questionList[i].questionItemList.forEach(item => {
            if (!item.content) {
              flag = true
            }
          })
        }
        if (!this.questionList[i].name) {
          this.$message.warning('Please fill in the title')
          return false
        }
        if (flag) {
          this.$message.warning('Please refine the options')
          return false
        }
      }
      return true
    },
    preview(pageId) {
      if (this.validate()) { // 校验通过了
        window.open('/front/preview?pageId=' + pageId)
      }
    },
    addQuestionItem(question) {
      this.$request.post('/questionItem/add', {questionId: question.id, content: question.newContent}).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('success')
          this.load()
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    addQuestion(type) {
      this.$request.post('/question/addForUser', {name: '', type: type, pageId: this.pages.id}).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('success')
          this.load()
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    changeQuestionName(question) {
      let data = JSON.parse(JSON.stringify(question))
      this.$request.put('/question/update', data).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('success')
          this.load()
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    changeQuestionItem(questionItem) {
      let data = JSON.parse(JSON.stringify(questionItem))
      this.$request.put('/questionItem/update', data).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('success')
          this.load()
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    delQuestion(questionId) {   // 单个删除
      this.$confirm('Are you sure you want to delete it?', 'Yes', {type: "warning"}).then(response => {
        this.$request.delete('/question/delete/' + questionId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('success')
            this.load()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    delQuestionItem(questionItemId) {   // 单个删除
      this.$confirm('Are you sure you want to delete it?？', 'Yes', {type: "warning"}).then(response => {
        this.$request.delete('/questionItem/delete/' + questionItemId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('success')
            this.load()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load() {
      let pageId = this.$route.query.pageId
      this.$request.get('/pages/selectById/' + pageId).then(res => {
        this.pages = res.data || {}
      })

      this.$request.get(`/question/selectByPageId/${pageId}`, {}).then(res => {
        this.questionList = res.data || []
      })

    }
  }
}
</script>

<style scoped>

</style>