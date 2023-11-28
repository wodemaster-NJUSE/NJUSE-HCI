<script setup lang="ts">
import { PropType, ref } from "vue";
import { SwitchFilled } from '@element-plus/icons-vue'
import { useStationsStore } from "~/stores/stations";
import { calDuration, parseDate } from "~/utils/date";
import { useUserStore } from "~/stores/user";
import { useRouter } from "vue-router";
import { TicketInfo } from "~/utils/interfaces";

const props = defineProps({
  id: Number,
  name: String,
  start_station_id: Number,
  end_station_id: Number,
  departure_time: Number,
  arrival_time: Number,
  ticket_info: Array as PropType<TicketInfo[]>
})

const router = useRouter()
const user = useUserStore()
const stations = useStationsStore()

let drawer = ref(false)
let dialog = ref(false)

const handleOrder = () => {
  dialog.value = true
  if (user.name === '') {
    router.push('/login')
  }
}
</script>

<template>
  <div style="margin: 0 40vh">
    <el-descriptions :column="4" border>
      <el-descriptions-item :span="2" width="25%" align="center">
        <template #label>
          <el-text type="primary" tag="b" size="large">
            车次
          </el-text>
        </template>
        <el-text type="primary" tag="b" size="large">
          {{ name }}
        </el-text>
      </el-descriptions-item>
      <el-descriptions-item label="历时" :span="2" width="25%" align="center">
        {{ calDuration(departure_time ?? -1, arrival_time ?? -1) }}
      </el-descriptions-item>
      <el-descriptions-item label="出发站" :span="2" width="25%" align="center">
        {{ stations.idToName[start_station_id ?? -1] }}
      </el-descriptions-item>
      <el-descriptions-item label="到达站" :span="2" width="25%" align="center">
        {{ stations.idToName[end_station_id ?? -1] }}
      </el-descriptions-item>
      <el-descriptions-item label="出发时间" :span="2" width="25%" align="center">
        {{ parseDate(departure_time) }}
      </el-descriptions-item>
      <el-descriptions-item label="到达时间" :span="2" width="25%" align="center">
        {{ parseDate(arrival_time) }}
      </el-descriptions-item>
      <el-descriptions-item v-for="ticket in ticket_info" :label="ticket.type" :span="2" width="25%" align="center">
        {{ ticket.count }}
      </el-descriptions-item>
    </el-descriptions>

    <div style="display: flex; justify-content: flex-end; margin-top: 3vh;">
      <el-button @click="drawer = true">
        查看详情
      </el-button>
      <el-button type="primary" @click="handleOrder">
        购买
      </el-button>
    </div>

    <el-divider>
      <el-icon>
        <SwitchFilled />
      </el-icon>
    </el-divider>
  </div>

  <el-drawer v-model="drawer" direction="rtl" size="50%" destroy-on-close>
    <TrainDetail :trainId="id" />
  </el-drawer>

  <el-dialog v-model="dialog" title="Tips" width="50%" draggable destroy-on-close>
    <OrderForm v-bind="props"></OrderForm>
  </el-dialog>
</template>

<style scoped></style>