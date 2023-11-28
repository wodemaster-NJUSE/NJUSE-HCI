<script setup lang="ts">

import { computed, onMounted, reactive, ref } from "vue";
import { request } from "~/utils/request";
import { parseDate } from "~/utils/date";
import { Right } from "@element-plus/icons-vue";
import { useStationsStore } from "~/stores/stations";
import { useRouter } from "vue-router";
import { OrderDetailData } from "~/utils/interfaces";

let orders = reactive({
  data: [] as OrderDetailData[]
})

const router = useRouter()
const stations = useStationsStore()

let dialog = ref(false)
let id = ref()

let orderDetail = reactive({
  data: Object
}
)


const getOrders = () => {
  request({
    url: '/order',
    method: 'GET'
  }).then((res) => {
    orders.data = res.data.data
  }).catch((error) => {
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    console.log(error)
  })
}

const getTrainName = (id: number) => {
  request({
    url: `/train/${id}`,
    method: 'GET'
  }).then((res) => {
    return res.data.name
  }).catch((err) => {
    console.log(err)
  }
  )
}

onMounted(() => {
  getOrders()
  stations.fetch()
})

</script>

<template>
  <el-card v-for="order in orders.data " style="margin-bottom: 1vh" shadow="hover">
    <div style="display: flex; flex-direction: column">

      <div style="display: flex; justify-content: space-between;">
        <div>
          <el-text size="large" tag="b" type="primary">
            订单号:&nbsp;&nbsp;
          </el-text>
          <el-text size="large" tag="b">
            {{ order.id }}
          </el-text>
        </div>
        <div>
          <el-text size="large" tag="b" type="primary">
            创建日期:&nbsp;&nbsp;
          </el-text>
          <el-text size="large" tag="b">
            {{ parseDate(order.created_at) }}
          </el-text>
        </div>
      </div>

      <div>
        <el-text size="large" tag="b" type="primary">
          订单状态:&nbsp;&nbsp;
        </el-text>
        <el-text size="large" tag="b">
          {{ order.status }}
        </el-text>
      </div>

      <el-row class="el-row">
        <el-col :span="24" style="display: flex; justify-content: center; align-items: center">
          <el-text type="primary" size="large" tag="b">
            {{ getTrainName(order.train_id) }}
          </el-text>
        </el-col>
      </el-row>

      <el-row justify="center" class="el-row">
        <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
          <el-text>
            {{ stations.idToName[order.start_station_id] }}
          </el-text>
        </el-col>
        <el-col :span="2" style="display: flex; justify-content: center; align-items: center">
          <el-icon size="15">
            <Right />
          </el-icon>
        </el-col>
        <el-col :span="11" style="display: flex; justify-content: left; align-items: center;">
          <el-text style="text-align: center">
            {{ stations.idToName[order.end_station_id] }}
          </el-text>
        </el-col>
      </el-row>

      <el-row justify="center">
        <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
          <el-text>
            {{ parseDate(order.departure_time) }}
          </el-text>
        </el-col>
        <el-col :span="2">
        </el-col>
        <el-col :span="11" style="display: flex; justify-content: left; align-items: center">
          <el-text>
            {{ parseDate(order.arrival_time) }}
          </el-text>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="2" :offset="21">
          <el-button type="primary" @click="dialog = true; id = order.id">
            查看详情
          </el-button>
        </el-col>
      </el-row>
    </div>



  </el-card>


  <el-dialog destroy-on-close v-model="dialog" title="订单详情" width="50%">
    <OrderDetail :id="id" />
  </el-dialog>
</template>

<style scoped></style>