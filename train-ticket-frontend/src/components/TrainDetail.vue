<script setup lang="ts">
import { request } from "~/utils/request";
import { computed, onMounted, reactive } from "vue";
import { Right } from "@element-plus/icons-vue";
import { useStationsStore } from "~/stores/stations";
import { parseDate } from "../utils/date";
import { TrainDetailInfo } from "~/utils/interfaces";

const props = defineProps({
  trainId: Number
})

const stations = useStationsStore()

let data = reactive<{ data: TrainDetailInfo }>({
  data: {
    id: 0,
    name: "",
    station_ids: [],
    date: '',
    departure_times: [],
    arrival_times: [],
    extra_infos: [],
  }
});

const refreshData = () => {
  request({
    url: `/train/${props.trainId}`,
    method: 'GET'
  }).then((res) => {
    data.data = res.data.data
    console.log("submit")
  }).catch((error) => {
    console.log(error)
  })
}

const tableData = computed(() => {
  return data.data.station_ids?.map(
    (station_id, index) => {
      return {
        index: index,
        station_name: stations.idToName[station_id],
        departure_time: parseDate(data.data.departure_times[index]),
        arrival_time: parseDate(data.data.arrival_times[index]),
        extra_info: data.data.extra_infos[index]
      }
    }
  )
})

onMounted(() => {
  refreshData()
})
</script>

<template>
  <el-row class="el-row">
    <el-col :span="24">
      <br>
    </el-col>
  </el-row>

  <el-row class="el-row" style="margin-bottom: 1vh">
    <el-col :span="24" style="display: flex; justify-content: center; align-items: center">
      <el-text type="primary" size="large" tag="b">
        <h1>{{ data.data.name }}</h1>
      </el-text>
    </el-col>
  </el-row>

  <el-row class="el-row">
    <el-col :span="24" style="display: flex; justify-content: center; align-items: center">
      <el-text tag="b">
        <h3>{{ data.data.date }}</h3>
      </el-text>
    </el-col>
  </el-row>

  <el-row justify="center" class="el-row">
    <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
      <el-text>
        <h3>{{ stations.idToName[data.data.station_ids[0]] }}</h3>
      </el-text>
    </el-col>
    <el-col :span="2" style="display: flex; justify-content: center; align-items: center">
      <el-icon size="15">
        <Right />
      </el-icon>
    </el-col>
    <el-col :span="11" style="display: flex; justify-content: left; align-items: center;">
      <el-text style="text-align: center">
        <h3>{{ stations.idToName[data.data.station_ids[data.data.station_ids.length - 1]] }}</h3>
      </el-text>
    </el-col>
  </el-row>

  <el-row justify="center">
    <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
      <el-text>
        {{ parseDate(data.data.departure_times[0]) }}
      </el-text>
    </el-col>
    <el-col :span="2">
    </el-col>
    <el-col :span="11" style="display: flex; justify-content: left; align-items: center">
      <el-text>
        {{ parseDate(data.data.arrival_times[data.data.station_ids.length - 1]) }}
      </el-text>
    </el-col>
  </el-row>


  <el-table :data="tableData" style="width: 80%; margin: 0 auto; margin-top: 5vh">
    <el-table-column prop="index" label="站序" />
    <el-table-column prop="station_name" label="站名" />
    <el-table-column prop="arrival_time" label="到达时间" />
    <el-table-column prop="departure_time" label="出发时间" />
    <el-table-column prop="extra_info" label="其他" />
  </el-table>
</template>

<style scoped>
.el-row {
  height: 36px;
}
</style>