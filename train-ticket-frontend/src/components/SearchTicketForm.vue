<script setup lang="ts">
import { h, onMounted, reactive } from "vue";
import { useSearchStore } from "~/stores/search";
import { useStationsStore } from "~/stores/stations";
import { ElNotification } from "element-plus";

defineProps({
  inline: Boolean
})
const emit = defineEmits(['formUpdated'])

const search = useSearchStore()
const stations = useStationsStore()

const form = reactive({
  start_station_id: search.start_station_id,
  end_station_id: search.end_station_id,
  date: search.date
})

const disabledDate = (time: Date) => {
  let now = new Date();
  return time.getTime() < new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime();
}

const checkNull = () => {
  if (search.start_station_id == undefined || search.end_station_id == undefined || search.date == undefined) {
    ElNotification({
      offset: 70,
      title: '错误',
      message: h('error', { style: 'color: teal' }, "请填写完整信息"),
    })
    return
  }
  emit('formUpdated', form)
}

onMounted(() => {
  stations.fetch()
})
</script>

<template>
  <el-form :model="form" label-width="120px" :inline="inline">
    <el-form-item label="出发站">
      <el-select v-model="form.start_station_id" filterable>
        <el-option v-for="item in stations.rawData" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="到达站">
      <el-select v-model="form.end_station_id" filterable>
        <el-option v-for="item in stations.rawData" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="日期">
      <el-date-picker :clearable="false" v-model="form.date" type="date" placeholder="选择 日期" value-format="YYYY-MM-DD"
        :disabled-date="disabledDate" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="search.$patch(form); checkNull();">
        查询
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped></style>