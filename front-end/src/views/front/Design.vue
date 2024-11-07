<template>
  <div class="main-content">
    <div class="card">
      <div style="display: flex; margin-bottom: 20px">
        <div style="flex: 1">
          <div>
            <span style="font-size: 24px; margin-right: 10px">{{ pages.name }}</span>
            <el-tag v-if="pages.saved === '否'">未发布</el-tag>
            <el-tag v-else type="success">已发布</el-tag>
          </div>
        </div>
        <div>
          <el-button type="primary"><i class="el-icon-share"></i> 分享</el-button>
          <el-button type="info"><i class="el-icon-video-play"></i> 预览</el-button>
          <el-button type="success"><i class="el-icon-s-promotion"></i> 发布</el-button>
        </div>
      </div>

      <div style="margin-bottom: 20px">
        <el-button type="primary">新建单选题</el-button>
        <el-button type="info">新建多选题</el-button>
        <el-button type="success">新建填空题</el-button>
      </div>

      <div>
        <div v-for="(item, index) in questionList" :key="item.id" style="margin-bottom: 20px">
          <div>
            <span style="font-weight: bold; margin-right: 5px">题目{{ index+1 }}</span>
            <el-input v-model="item.name" style="width: 70%; margin-right: 5px"></el-input>
            <el-tag style="margin-right: 5px; color: #2a60c9" v-if="item.type === 'Single Choice'">{{ item.type }}</el-tag>
            <el-tag style="margin-right: 5px; color: orange" v-if="item.type === '多选题'">{{ item.type }}</el-tag>
            <el-tag style="margin-right: 5px; color: seagreen" v-if="item.type === '填空题'">{{ item.type }}</el-tag>
            <span style="color: red; cursor: pointer; font-size: 12px"><i class="el-icon-delete"></i>删除</span>
          </div>
          <div style="padding-left: 40px" v-if="item.questionItemList?.length">
            <div v-for="(sub, subIndex) in item.questionItemList" :key="sub.id" style="margin: 10px 0">
              <div>
                <span style="margin-right: 5px">选项{{ subIndex+1 }}</span>
                <el-input v-model="sub.content" style="width: 50%; margin-right: 5px"></el-input>
                <span style="color: red; cursor: pointer; font-size: 12px"><i class="el-icon-delete"></i>删除</span>
              </div>
            </div>
            <div>
              <span style="margin-right: 5px; color: #2a60c9">新选项</span>
              <el-input v-model="item.newContent" style="width: 50%; margin-right: 5px"></el-input>
              <el-button type="primary" class="btn-green" size="mini">确认添加</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Design",
  data() {
    return {
      pages: {},
      questionList: []
    }
  },
  created() {
    this.load()
  },
  methods: {
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