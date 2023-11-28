<script setup lang="ts">
import { useUserStore } from "~/stores/user";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { request } from "~/utils/request";

const router = useRouter()
const user = useUserStore()

let index = ref(1)

const menuSelect = (key) => {
  index.value = key
  console.log(key);
}

</script>

<template>
  <el-container>
    <el-header style="position: fixed; width: 100%; z-index: 999">
      <MenuComponent pageIndex="/user" />
    </el-header>
    <el-container style="display: flex; align-items: center; height: 85vh">
      <el-aside width="15%">
        <el-menu default-active="1" class="el-menu-vertical-demo"
          style="height: 85vh; display: flex; flex-direction: column; justify-content: center" @select="menuSelect">
          <el-menu-item index="1">
            <strong>
              个人信息
            </strong>
          </el-menu-item>
          <el-menu-item index="2">
            <strong>
              订单信息
            </strong>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main style="display: flex; justify-content: center; align-items: center">
        <div v-show="index == 1">
          <el-text size="large" type="primary" style="display: flex;justify-content: center">
            <h1>个人信息</h1>
          </el-text>
          <br />
          <UserInfoComponent style="width: 35vh; margin: 0 auto" />
        </div>


        <div v-show="index == 2" style="height: 85vh; margin-top: 10vh; width: 65%">
          <el-text size="large" type="primary" style="display: flex;justify-content: center; margin-bottom: 5vh">
            <h1>订单</h1>
          </el-text>

          <UserOrders />
        </div>
      </el-main>
    </el-container>

  </el-container>
</template>

<style scoped></style>