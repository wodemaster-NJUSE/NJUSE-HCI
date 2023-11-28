<script lang="ts" setup>
import { h, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElNotification } from "element-plus"
import { request } from "~/utils/request"
import { AxiosError, AxiosResponse } from 'axios';
import { useRouter } from "vue-router";

const router = useRouter();

const ruleFormRef = ref<FormInstance>()

const validateCheckPass = (rule: any, value: any, callback: any) => {
  if (ruleForm.password === '') return
  if (value !== ruleForm.password) {
    callback(new Error("密码不一致"))
  } else {
    callback()
  }
}

const ruleForm = reactive({
  username: '',
  password: '',
  checkPass: '',
  name: '',
  type: '身份证',
  idn: '',
  phone: '',
  rick: false,
})

const rules = reactive<FormRules>({
  username: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    min: 4, max: 16, message: '用户名长度不符合要求(4-16)', trigger: 'change'
  }, {
    pattern: /^[a-z\d-_]*$/, message: '用户名只能包含小写字母,数字,下划线和连字符', trigger: 'change'
  }],
  password: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    min: 8, max: 56, message: '密码长度不符合要求(8-56)', trigger: 'change'
  }, {
    pattern: /^[\x21-\x7e]*$/, message: '密码只能包含字母,数字和符号', trigger: 'change'
  }, {
    pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/,
    message: '密码未达到复杂性要求:密码必须包含大小写字母和数字',
    trigger: 'change'
  }],
  checkPass: [{ required: true, message: '此字段为必填项', trigger: 'change' }, {
    validator: validateCheckPass,
    trigger: 'change'
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
      method: 'POST',
      data: {
        username: ruleForm.username,
        password: ruleForm.password,
        name: ruleForm.name,
        type: ruleForm.type,
        idn: ruleForm.idn,
        phone: ruleForm.phone,
      }
    })
    r.then((response: AxiosResponse<any>) => {
      ElNotification({
        offset: 70,
        title: '注册成功',
        message: h('info', { style: 'color: teal' }, response.data.msg),
      })
      router.push('/login')
    }).catch((error: AxiosError<any>) => {
      console.log(error)
      ElNotification({
        offset: 70,
        title: 'register错误',
        message: h('error', { style: 'color: teal' }, error.response?.data.msg),
      })
    })
  })

}

</script>

<template>
  <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" class="demo-ruleForm" label-width="120px">
    <el-form-item label="用户名" prop="username">
      <el-input v-model="ruleForm.username" type="text" />
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="ruleForm.password" autocomplete="off" type="password" />
    </el-form-item>
    <el-form-item label="密码确认" prop="checkPass">
      <el-input v-model="ruleForm.checkPass" autocomplete="off" type="password" />
    </el-form-item>
    <el-form-item label="姓名" prop="name">
      <el-input v-model="ruleForm.name" type="text" />
    </el-form-item>
    <el-form-item label="证件类型" prop="type">
      <el-select v-model="ruleForm.type" placeholder=" ">
        <el-option value="身份证" />
        <el-option value="护照" />
        <el-option value="其他" />
      </el-select>
    </el-form-item>
    <el-form-item label="证件号码" prop="idn">
      <el-input v-model="ruleForm.idn" type="text" />
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="ruleForm.phone" />
    </el-form-item>
    <el-form-item prop="rick">
      <el-checkbox v-model="ruleForm.rick">
        <span>我已阅读并同意</span>
        <el-link type="primary" href="https://www.bilibili.com/video/BV1GJ411x7h7/"
          target="_blank">《l23o6客户服务中心网站服务条款》</el-link>
      </el-checkbox>
    </el-form-item>

    <el-row justify="start">
      <el-col :span="12" style="display: flex; justify-content: center; align-items: center">
        <el-form-item>
          <el-button type="primary" @click="submitForm(ruleFormRef)" :disabled="!ruleForm.rick">注册</el-button>
        </el-form-item>
      </el-col>
      <el-col :span="7" style="display: flex; justify-content: center; align-items: center">
        <el-form-item>
          <el-button @click="$router.back()">
            返回
          </el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<style scoped></style>

