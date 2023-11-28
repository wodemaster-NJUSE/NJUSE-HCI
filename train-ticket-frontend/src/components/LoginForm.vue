<script lang="ts" setup>
import { h, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElNotification } from "element-plus"
import { request } from "~/utils/request"
import { AxiosError, AxiosResponse } from 'axios';
import { Lock, User } from '@element-plus/icons-vue'
import { useUserStore } from "~/stores/user";
import { useRouter } from "vue-router";

const user = useUserStore();
const router = useRouter();

const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive({
  username: '',
  password: '',
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
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (!valid) return
    console.log('submit!')
    const r = request({
      url: '/session',
      method: 'POST',
      data: {
        username: ruleForm.username,
        password: ruleForm.password
      }
    })

    r.then((response: AxiosResponse<any>) => {
      ElNotification({
        offset: 70,
        title: '登录成功',
        message: h('i', { style: 'color: teal' }, response.data.msg),
      })
      user.fetch()
      router.push('/')
    }).catch((error: AxiosError<any>) => {
      console.log(error)
      ElNotification({
        offset: 70,
        title: 'login错误',
        message: h('i', { style: 'color: teal' }, error.response?.data.msg),
      })
    })
  })
}
</script>

<template>
  <el-form class="demo-ruleForm" ref="ruleFormRef" :model="ruleForm" :rules="rules">
    <el-form-item prop="username">
      <el-input v-model="ruleForm.username" type="text" :prefix-icon="User" placeholder="用户名" />
    </el-form-item>
    <el-form-item prop="password">
      <el-input v-model="ruleForm.password" autocomplete="off" type="password" show-password :prefix-icon="Lock"
        placeholder="密码" />
    </el-form-item>
    <el-row justify="center">
      <el-col :span="12" style="display: flex; justify-content: center; align-items: center">
        <el-form-item>
          <el-button type="primary" @click="submitForm(ruleFormRef)">
            登录
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="12" style="display: flex; justify-content: center; align-items: center">
        <el-form-item>
          <el-button @click="$router.push('/register')">
            注册
          </el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<style scoped></style>