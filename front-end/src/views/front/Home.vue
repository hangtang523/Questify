<template>
  <div class="main-content">
    <el-row :gutter="10">
      <el-col :span="8" v-for="item in pagesList" :key="item.id">
        <div class="card" style="margin-bottom: 10px">
          <div style="display: flex; grid-gap: 10px; margin-bottom: 10px">
            <div style="flex: 1; width: 0">
              <div style="margin-bottom: 10px; font-size: 18px" class="line1">{{ item.name }}</div>
              <div style="color: #666;" class="line1">{{ item.descr }}</div>
            </div>
            <div style="width: 80px">
              <img :src="item.img" alt="" style="width: 100%">
            </div>
          </div>

          <div>
            <el-button icon="el-icon-video-play" type="success" size="mini" style="background-color: rgb(130, 130, 255); border-color: rgb(130, 130, 255)" @click="preview(item.id)">View</el-button>
            <el-button type="primary" size="mini" style="background-color: rgb(91, 165, 133); border-color: rgb(91, 165, 133)" @click="copy(item.id)">Use this template</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {

  data() {
    return {
      pagesList: []
    }
  },
  mounted() {
    this.load()
    console.log("Front Home Page Loaded!");
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    copy(pageId) {
      this.$request.post('/pages/copy?pageId=' + pageId).then(res => {
        if (res.code === '200') {
          this.$message.success("success！")
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    preview(pageId) {
      console.log(pageId);
      console.log("Button clicked!", pageId);
      window.open('/front/preview?pageId=' + pageId)
    },
    load() {
      this.$request.get('/pages/selectAll', {
        params: {open: 'Yes'}
      }).then(res => {
        this.pagesList = res.data || []
      })
    }
  }
}
</script>