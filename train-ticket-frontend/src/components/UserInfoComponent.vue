<script setup lang="ts">

import { h, reactive, ref, watch } from "vue";
import { useUserStore } from "~/stores/user";
import { ElNotification, FormInstance } from "element-plus";
import { request } from "~/utils/request";
import { AxiosError, AxiosResponse } from "axios";
import { useRouter } from "vue-router";

const user = useUserStore()
const router = useRouter()

const ruleFormRef = ref<FormInstance>()
let edit = ref(false);
let form = reactive({
  username: '',
  name: '',
  type: '',
  idn: '',
  phone: ''
});

const setForm = async () => {
  form.username = user.username;
  form.name = user.name;
  form.type = user.type;
  form.idn = user.idn;
  form.phone = user.phone;
}

watch(user, () => {
  setForm()
})

setForm()

const rules = reactive({
  username: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    min: 4, max: 16, message: '用户名长度不符合要求(4-16)', trigger: 'change'
  }, {
    pattern: /^[a-z\d-_]*$/, message: '用户名只能包含小写字母,数字,下划线和连字符', trigger: 'change'
  }],
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
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (!valid) return

    console.log('submit!')

    const r = request({
      url: '/user',
      method: 'PUT',
      data: {
        username: form.username,
        name: form.name,
        type: form.type,
        idn: form.idn,
        phone: form.phone,
      }
    })

    r.then((response: AxiosResponse<any>) => {
      ElNotification({
        offset: 70,
        title: '修改成功',
        message: h('info', { style: 'color: teal' }, response.data.msg),
      })
      user.fetch()
      edit.value = false
    }).catch((error: AxiosError<any>) => {
      console.log(error)
      if (error.response?.data.code == 100003) {
        router.push('/login')
      }
      ElNotification({
        offset: 70,
        title: 'putUser错误',
        message: h('error', { style: 'color: teal' }, error.response?.data.msg),
      })
    })
  })
}
</script>

<template>
  <div style="display: flex; flex-direction: column">
    <div v-if="edit" style="display: flex; flex-direction: row; justify-content: flex-end">
      <el-button @click="edit = false;">取消</el-button>
      <el-button type="primary" @click="submitForm(ruleFormRef)">提交</el-button>
    </div>
    <div v-else style="display: flex; flex-direction: row-reverse; align-items: flex-end">
      <el-button type="primary" @click="edit = true; setForm()">编辑</el-button>
    </div>

    <br />

    <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="30%" class="demo-ruleForm" label-position="right"
      hide-required-asterisk size="large">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" style="width: 25vh" :disabled="true" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" style="width: 25vh" :disabled="!edit" />
      </el-form-item>
      <el-form-item label="证件类型" prop="type">
        <el-select v-model="form.type" placeholder=" " style="width: 25vh" :disabled="!edit">
          <el-option value="身份证" />
          <el-option value="护照" />
          <el-option value="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="证件号码" prop="idn">
        <el-input v-model="form.idn" type="text" style="width: 25vh" :disabled="!edit" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" style="width: 25vh" :disabled="!edit" />
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped></style>