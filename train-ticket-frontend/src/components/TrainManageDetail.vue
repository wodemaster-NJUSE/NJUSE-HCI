<script setup lang="ts">

import { Right, SwitchFilled } from "@element-plus/icons-vue";
import { h, reactive, watch } from "vue";
import { request } from "~/utils/request";
import { useStationsStore } from "~/stores/stations";
import { ElNotification } from "element-plus";
import { parseDate } from "~/utils/date";
import { useRouter } from "vue-router";

const props = defineProps({
  id: Number,
  name: String,
  route_id: Number,
  train_type: String,
  date: Date,
  departure_times: Array<string>,
  arrival_times: Array<string>,
  extra_infos: Array<string>
})

const router = useRouter()
const stations = useStationsStore()

let route = reactive({
  id: 0,
  name: '',
  station_ids: []
});

const getRoute = () => {
  request({
    url: `/admin/route/${props.route_id}`,
    method: 'GET'
  }).then((res) => {
    route.id = res.data.data.id
    route.name = res.data.data.name
    route.station_ids = res.data.data.station_ids
  }).catch((error) => {
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'getRoute错误(trainManage)',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
    console.log(error)
  })
}

watch(() => props.route_id, () => {
  getRoute()
})
getRoute()
</script>

<template>
  <el-text tag="b">
    路线名:&nbsp;&nbsp;
  </el-text>
  <el-text tag="b" type="primary">
    {{ route.name }}
  </el-text>

  <br />
  <br />

  <el-row justify="center" class="el-row">
    <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
      <el-text tag="b">
        {{ route.station_ids.length > 0 ? stations.idToName[route.station_ids?.[0]] : '' }}
      </el-text>
    </el-col>
    <el-col :span="2" style="display: flex; justify-content: center; align-items: center">
      <el-icon size="15">
        <Right />
      </el-icon>
    </el-col>
    <el-col :span="11" style="display: flex; justify-content: left; align-items: center;">
      <el-text style="text-align: center" tag="b">
        {{ route.station_ids?.length > 0 ? stations.idToName[route.station_ids?.[route.station_ids?.length - 1]] : '' }}
      </el-text>
    </el-col>
  </el-row>

  <el-row justify="center">
    <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
      <el-text>
        {{ parseDate(departure_times?.[0] as string) }}
      </el-text>
    </el-col>
    <el-col :span="2" />
    <el-col :span="11" style="display: flex; justify-content: left; align-items: center">
      <el-text>
        {{ parseDate(props.arrival_times?.[props.arrival_times?.length - 1] as string) }}
      </el-text>
    </el-col>
  </el-row>

  <br />

  <el-card shadow="hover" v-for="(stationId, index) in route.station_ids" style="margin-bottom: 0.35%">
    <div style="display: flex; align-items: center">
      <el-space :size="50" style="width: 50%">
        <el-icon size="large">
          <SwitchFilled />
        </el-icon>
        <strong>
          {{ index + 1 }}
        </strong>
        <strong>
          {{ stations.idToName[stationId] }}
        </strong>
      </el-space>

      <el-space spacer="|" size="large">
        <div>
          <strong>
            到点:
          </strong>
          {{ parseDate(props.arrival_times?.[index] as string) }}
        </div>
        <div>
          <strong>
            开点:
          </strong>
          {{ parseDate(props.departure_times?.[index] as string) }}
        </div>

      </el-space>
    </div>
  </el-card>
</template>

<style scoped></style>