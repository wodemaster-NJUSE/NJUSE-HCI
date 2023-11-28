<script setup lang="ts">
import { PropType, reactive, ref } from 'vue'
import { VueDraggable } from 'vue-draggable-plus'
import { CloseBold, SwitchFilled } from "@element-plus/icons-vue";
import { useStationsStore } from "~/stores/stations";
import { ElMessage } from "element-plus";

const props = defineProps({
  name: String,
  station_ids: Array as PropType<Array<number>>
})

const emit = defineEmits(['formSubmitted'])

const stations = useStationsStore()

let route = reactive({
  name: '',
  station_ids: [] as Array<number>
})
let edit = ref(false)
let add = ref(false)
let toAddId = ref<number>()
let toEditIndex = ref<number>()
let toEditId = ref<number>()

if (Array.isArray(props.station_ids)) {
  for (let i = 0; i < props.station_ids.length; i++) {
    route.station_ids.push(props.station_ids[i])
  }
}

route.name = <string>props.name;

const addStation = () => {
  if (toAddId.value === undefined) {
    ElMessage({
      message: '站点不能为空',
      type: 'error',
    })
    return
  }
  route.station_ids.push(toAddId.value)
  add.value = false;
}

const editStation = () => {
  if (typeof toEditIndex.value === "number" && typeof toEditId.value === "number") {
    route.station_ids.splice(toEditIndex.value, 1, toEditId.value)
  }
  edit.value = false;
}

const onEnd = () => {
  // refreshIndex()
}

const deleteStation = (index: number) => {
  route.station_ids.splice(index, 1)
}
</script>

<template>
  <div style="display: flex;flex-direction: column">
    <el-form-item>
      <template #label>
        <el-text tag="b" type="primary">
          路线名
        </el-text>
      </template>
      <el-input v-model="route.name" style="margin-right: 60%" />
    </el-form-item>
    <div>
      <el-button @click="add = true" style="margin-bottom: 3%; float:right;">
        添加站点
      </el-button>
    </div>
    <VueDraggable ref="el" v-model="route.station_ids" :animation="100" handle=".handle" @end="onEnd">
      <div v-for="station in route.station_ids" :key="station">
        <el-card style="margin-bottom: 0.25%" shadow="hover" class="container">
          <div style="display: flex; align-items: center;">
            <el-icon class="handle" size="large">
              <SwitchFilled />
            </el-icon>
            <strong style="margin-left: 5%; margin-right: 5%">
              {{ route.station_ids.indexOf(station) + 1 }}
            </strong>
            <div style="width: 80%">
              {{ stations.idToName[station] }}
            </div>
            <div style="display: flex; flex-direction: row; justify-items: center; align-items: center;">
              <el-space>
                <el-button class="change"
                  @click="toEditIndex = route.station_ids.indexOf(station); toEditId = station; edit = true">
                  编辑
                </el-button>
                <div>
                  <el-icon @click="deleteStation(route.station_ids.indexOf(station))"
                    style="display: flex; align-self: flex-end;">
                    <CloseBold />
                  </el-icon>
                </div>
              </el-space>
            </div>
          </div>
        </el-card>
      </div>
    </VueDraggable>
    <el-button @click="$emit('formSubmitted', route)" style="margin-top: 2vh" type="primary">
      确认
    </el-button>

    <!--    <pre>-->
    <!--      {{ route.station_ids }}-->
    <!--    </pre>-->
  </div>

  <el-dialog v-model="add" title="添加站点" width="30%" draggable @close="toAddId = undefined">
    <div>请选择新的站点</div>
    <br />
    <div style="display: flex;">
      <el-space>
        <el-select v-model="toAddId" filterable>
          <el-option v-for="item in stations.rawData" :disabled="route.station_ids.includes(item.id)" :key="item.id"
            :label="item.name" :value="item.id" />
        </el-select>
        <el-button type="primary" @click="addStation">
          确定
        </el-button>
      </el-space>
    </div>
  </el-dialog>

  <el-dialog v-model="edit" title="编辑站点" width="30%" draggable>
    <div>请选择新的站点</div>
    <br />
    <div style="display: flex;">
      <el-space>
        <el-select v-model="toEditId" filterable>
          <el-option v-for="item in stations.rawData"
            :disabled="route.station_ids.includes(item.id) && item.id !== toEditId" :key="item.id" :label="item.name"
            :value="item.id" />
        </el-select>
        <el-button type="primary" @click="editStation">
          确定
        </el-button>
      </el-space>
    </div>
  </el-dialog>
</template>

<style scoped>
.change {
  visibility: hidden
}

.container:hover .change {
  visibility: visible
}
</style>
