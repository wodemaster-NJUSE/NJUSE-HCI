<script setup lang="ts">

import { h, onMounted, reactive, ref, watch } from "vue";
import { request } from "~/utils/request";
import { ElNotification } from "element-plus";
import { useStationsStore } from "~/stores/stations";
import { useRouter } from "vue-router";
import { StationInfo } from "~/utils/interfaces";

let stations = useStationsStore()
let router = useRouter()

let stationName = ref('')
let stationsFiltered = reactive({
  data: [] as StationInfo[],
})
let toAdd = ref('')
let toRename = ref('')
let toRenameId = ref(0)
let rename = ref(false)

const addStation = () => {
  if (toAdd.value === '') return
  request({
    url: '/admin/station',
    method: 'POST',
    data: {
      name: toAdd.value
    }
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    stationName.value = ''
    stations.fetch()
    filter()
    toAdd.value = ''
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'postStation错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}

const delStation = (id: number) => {
  request({
    url: `/admin/station/${id}`,
    method: 'DELETE'
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    stationName.value = ''
    stations.fetch()
    filter()
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'deleteStation错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}

const renameStation = () => {
  request({
    url: `/admin/station/${toRenameId.value}`,
    method: 'PUT',
    data: {
      name: toRename.value
    }
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    stationName.value = ''
    stations.fetch()
    filter()
    toRename.value = ''
    toRenameId.value = 0
    rename.value = false
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'putStation错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}

const filter = () => {
  stationsFiltered.data = stations.rawData.filter((station) => {
    return station.name.includes(stationName.value)
  })
}

onMounted(() => {
  stations.fetch()
  stationsFiltered.data = [...stations.rawData]
})

watch(stations, () => {
  filter()
})

</script>

<template>
  <el-container>
    <el-header style="position: fixed; width: 100%; z-index: 999">
      <MenuComponent pageIndex="/station" />
    </el-header>
    <el-main style="margin-top:8vh">
      <div style="display: flex; justify-content: center">
        <el-card shadow="hover" style="width: 70vh; height: auto; ">
          <el-form inline style="display: flex; " @submit.native.prevent>
            <el-form-item label="车站名" style="display: flex; flex-grow: 1">
              <el-input v-model="stationName" autofocus @keyup.enter.native="filter" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="filter">
                查询
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>

      <br />
      <br />

      <div style="display: flex; justify-content: center">
        <div style="display: flex; width: 80vh; justify-content: flex-end">
          <el-space>
            <el-input v-model="toAdd" style="width: 20vh" />
            <el-button type="primary" @click="addStation">
              添加
            </el-button>
          </el-space>
        </div>
      </div>

      <br />
      <br />

      <div style="display: flex; justify-content: center">
        <el-collapse style="width: 80vh; display: flex;flex-direction: column;">
          <el-collapse-item v-for="station in stationsFiltered.data" :title="station.name">
            <el-button @click="rename = true; toRenameId = station.id">
              更改
            </el-button>
            <el-button type="danger" @click="delStation(station.id)">
              删除
            </el-button>
          </el-collapse-item>

        </el-collapse>
      </div>
    </el-main>
  </el-container>

  <el-dialog v-model="rename" title="重命名" width="30%" draggable @close="toRenameId = 0; toRename = ''">
    <div>请输入新的站名</div>
    <br />
    <div style="display: flex;">
      <el-space>
        <el-input v-model="toRename" />
        <el-button type="primary" @click="renameStation">
          确定
        </el-button>
      </el-space>
    </div>
  </el-dialog>
</template>

<style scoped></style>