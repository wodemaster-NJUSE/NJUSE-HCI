<script setup lang="ts">

</script>

<template>
  <el-container>
    <el-header style="position: fixed; width: 100%; z-index: 999">
      <MenuComponent pageIndex="/register" />
    </el-header>
    <el-main style="margin-top: 10vh">
      <el-row style="display: flex; justify-content: center; align-content: center; height: 80vh">
        <el-col :span="24" style="display: flex; justify-content: center; align-content: center">
          <el-card shadow="hover" header="注册">
            <RegisterForm />
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<style scoped></style>