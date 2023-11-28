<script setup lang="ts">
import { h, onMounted, reactive, ref } from "vue";
import { request } from "~/utils/request";
import { ElNotification } from "element-plus";
import { useStationsStore } from "~/stores/stations";
import { Right } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import { RouteInfo } from "~/utils/interfaces";

const router = useRouter()
const stations = useStationsStore()

let routeName = ref('')
let route_detail_form = ref()
let routes = [] as RouteInfo[]
let routesFiltered = reactive({
  data: [] as RouteInfo[],
})
let toAdd = {
  name: '',
  station_ids: []
}
let toChange: RouteInfo = {
  id: 0,
  name: '',
  station_ids: []
}
let change = ref(false)
let add = ref(false)

const addRoute = (route: RouteInfo) => {
  request({
    url: '/admin/route',
    method: 'POST',
    data: {
      name: route.name,
      station_ids: route.station_ids
    }
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    routeName.value = ''
    refreshData()
    filter()
    add.value = false
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'postRoute错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}

const delRoute = (id: number) => {
  request({
    url: `/admin/route/${id}`,
    method: 'DELETE'
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    routeName.value = ''
    refreshData()
    filter()
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'deleteRoute错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}

const changeRoute = (route: RouteInfo) => {
  request({
    url: `/admin/route/${toChange.id}`,
    method: 'PUT',
    data: {
      name: route.name,
      station_ids: route.station_ids
    }
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    routeName.value = ''
    refreshData()
    filter()
    change.value = false
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'putRoute错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}

const refreshData = () => {
  stations.fetch()
  request({
    url: '/admin/route',
    method: 'GET'
  }).then((res) => {
    routes = res.data.data
    routesFiltered.data = [...routes]
  }).catch((error) => {
    console.log(error)
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'getRoute错误',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
  })
}
const filter = () => {
  routesFiltered.data = routes.filter((routes) => {
    return routes.name.includes(routeName.value)
  })
}

onMounted(() => {
  refreshData()
})

</script>

<template>
  <el-container>
    <el-header style="position: fixed; width: 100%; z-index: 999">
      <MenuComponent pageIndex="/route" />
    </el-header>
    <el-main style="margin-top: 8vh">
      <div style="display: flex; justify-content: center">
        <el-card shadow="hover" style="width: 70vh; height: auto; ">
          <el-form inline style="display: flex; " @submit.native.prevent>
            <el-form-item label="路线名" style="display: flex; flex-grow: 1">
              <el-input v-model="routeName" autofocus @keyup.enter.native="filter" />
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
            <el-button type="primary" @click="add = true; toAdd = { name: '', station_ids: [] }">
              添加
            </el-button>
          </el-space>
        </div>
      </div>

      <br />
      <br />

      <div style="display: flex; justify-content: center">
        <el-collapse style="width: 80vh; display: flex;flex-direction: column;">
          <el-collapse-item v-for="route in routesFiltered.data" :title="route.name">
            <div style="margin-bottom: 5%">
              <el-button @click="change = true; toChange = route;">
                更改
              </el-button>
              <el-button type="danger" @click="delRoute(route.id)">
                删除
              </el-button>
            </div>

            <div style=" display: flex">

              <el-row justify="center" class="el-row" style="width: 50%;">
                <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
                  <el-text tag="b" type="primary" size="large">
                    {{ route.station_ids?.length > 0 ? stations.idToName[route.station_ids?.[0]] : '' }}
                  </el-text>
                </el-col>
                <el-col :span="2" style="display: flex; justify-content: center; align-items: center">
                  <el-icon size="15">
                    <Right />
                  </el-icon>
                </el-col>
                <el-col :span="11" style="display: flex; justify-content: left; align-items: center;">
                  <el-text style="text-align: center" tag="b" type="primary" size="large">
                    {{ route.station_ids?.length > 0 ? stations.idToName[route.station_ids?.[route.station_ids?.length -
                      1]] : '' }}
                  </el-text>
                </el-col>
              </el-row>

              <el-timeline>
                <el-timeline-item v-for="(station_id, index) in route.station_ids" :key="index" hollow>
                  <strong>
                    {{ stations.idToName[station_id] }}
                  </strong>
                </el-timeline-item>
              </el-timeline>
            </div>

          </el-collapse-item>

        </el-collapse>
      </div>

    </el-main>
  </el-container>

  <el-dialog v-model="change" title="更改路线" width="50%" draggable>
    <div>请输入更改后的路线信息</div>
    <br />
    <div>
      <RouteDetailForm v-bind="toChange" @formSubmitted="changeRoute" :key="toChange.id" />
    </div>
  </el-dialog>

  <el-dialog v-model="add" title="添加路线" width="50%" draggable destroy-on-close>
    <div>请输入新的路线信息</div>
    <br />
    <div>
      <RouteDetailForm ref="route_detail_form" v-bind="toAdd" @formSubmitted="addRoute" />
    </div>
  </el-dialog>
</template>

<style scoped></style>