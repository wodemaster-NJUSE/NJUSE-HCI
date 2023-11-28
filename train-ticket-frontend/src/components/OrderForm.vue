<script setup lang="ts">
import { Right } from "@element-plus/icons-vue";
import { h, reactive, ref } from "vue";
import { ElNotification, FormInstance, FormRules } from "element-plus";
import { useUserStore } from "~/stores/user";
import { request } from "~/utils/request";
import { AxiosError, AxiosResponse } from "axios";
import { useRouter } from "vue-router";
import { useStationsStore } from "~/stores/stations";
import { parseDate } from "~/utils/date";
import { TicketInfo } from "~/utils/interfaces";
import { PropType } from "vue";

const props = defineProps({
  id: Number,
  name: String,
  end_station_id: Number,
  start_station_id: Number,
  departure_time: Number,
  arrival_time: Number,
  duration: String,
  ticket_info: Array as PropType<TicketInfo[]>
})

const stations = useStationsStore()
const user = useUserStore()
const router = useRouter()


const orderFormRef = ref<FormInstance>()
let orderForm = reactive({
  name: user.name,
  type: user.type,
  idn: user.idn,
  phone: user.phone,
  seat_type: '',
})

const orderRules = reactive<FormRules>({
  name: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    min: 2, max: 16, message: '姓名长度不符合要求(2-16)', trigger: 'change'
  }, {
    pattern: /^[\u4e00-\u9fa5]{2,16}$/, message: '姓名只能包含中文', trigger: 'change'
  }],
  idn: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    pattern: /^\d{17}(\d|X)$/, message: '身份证号码不符合要求', trigger: 'change'
  }],
  type: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    pattern: /^(身份证|护照|其他)$/, message: '证件类型不符合要求', trigger: 'change'
  }],
  phone: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    pattern: /^1[3456789]\d{9}$/, message: '手机号码不符合要求', trigger: 'change'
  }],
  seat_type: [{ required: true, message: '此字段为必填项', trigger: 'change' }],
})

const submitOrderForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return

  formEl.validate((valid) => {
    if (!valid) return
    console.log('submit!')
    const r = request({
      url: '/order',
      method: 'POST',
      data: {
        train_id: props.id,
        passenger: {
          name: orderForm.name,
          idn: orderForm.idn,
          phone: orderForm.phone,
          type: orderForm.type
        },
        start_station_id: props.start_station_id,
        end_station_id: props.end_station_id,
        seat_type: orderForm.seat_type
      }
    })

    r.then((response: AxiosResponse<any>) => {
      console.log(response)
      router.push(`/order/${response.data.data.id}`)
    }).catch((error: AxiosError<any>) => {
      console.log(error)
      console.log()
      if (error.response?.data.code == 100003) {
        router.push('/login')
      }
      ElNotification({
        offset: 70,
        title: 'postOrder错误',
        message: h('error', { style: 'color: teal' }, error.response?.data.msg),
      })
    })
  })
}

</script>

<template>
  <div>
    <el-text size="large" type="primary" style="display: flex;justify-content: center">
      <h1>订单确认</h1>
    </el-text>
    <br />
  </div>
  <el-row class="el-row">
    <el-col :span="24" style="display: flex; justify-content: center; align-items: center">
      <el-text type="primary" size="large" tag="b">
        {{ name }}
      </el-text>
    </el-col>
  </el-row>
  <br />
  <el-row justify="center" class="el-row">
    <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
      <el-text>
        {{ stations.idToName[start_station_id ?? -1] }}
      </el-text>
    </el-col>
    <el-col :span="2" style="display: flex; justify-content: center; align-items: center">
      <el-icon size="15">
        <Right />
      </el-icon>
    </el-col>
    <el-col :span="11" style="display: flex; justify-content: left; align-items: center;">
      <el-text style="text-align: center">
        {{ stations.idToName[end_station_id ?? -1] }}
      </el-text>
    </el-col>
  </el-row>
  <el-row justify="center">
    <el-col :span="11" style="display: flex; justify-content: right; align-items: center">
      <el-text>
        {{ parseDate(departure_time) }}
      </el-text>
    </el-col>
    <el-col :span="2" />
    <el-col :span="11" style="display: flex; justify-content: left; align-items: center">
      <el-text>
        {{ parseDate(arrival_time) }}
      </el-text>
    </el-col>
  </el-row>
  <br />
  <br />
  <el-form ref="orderFormRef" :model="orderForm" :rules="orderRules" label-width="120px">
    <el-form-item label="姓名" prop="name">
      <el-input v-model="orderForm.name" disabled />
    </el-form-item>
    <el-form-item label="证件类型" prop="type">
      <el-select v-model="orderForm.type" placeholder="" disabled>
        <el-option label="身份证" value="身份证" />
        <el-option label="护照" value="护照" />
      </el-select>
    </el-form-item>
    <el-form-item label="证件号" prop="idn">
      <el-input v-model="orderForm.idn" disabled />
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="orderForm.phone" disabled />
    </el-form-item>
    <el-form-item label="坐席" prop="seat_type">
      <el-select v-model="orderForm.seat_type">
        <el-option v-for="ticket in props.ticket_info" :value="ticket.type" :label="`${ticket.type}  ${ticket.price}元`"
                   :disabled="ticket.count == 0" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitOrderForm(orderFormRef)">
        提交
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped></style>