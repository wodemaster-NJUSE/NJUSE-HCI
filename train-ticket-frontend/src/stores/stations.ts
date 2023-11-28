import { defineStore } from "pinia";
import { request } from "~/utils/request";
import { ElNotification } from "element-plus";
import { h } from "vue";

export const useStationsStore = defineStore('stations', {
    state: () => {
        return {
            rawData: [] as {
                id: number,
                name: string
            }[],
            idToName: {} as Record<number, string>,
            nameToId: {} as Record<string, number>,
        }
    },
    actions: {
        async fetch() {
            request({
                url: '/station',
                method: 'GET'
            }).then((res) => {
                this.rawData = res.data.data
                this.idToName = this.rawData.reduce(function (map, station) {
                    // @ts-ignore
                    map[station.id] = station.name
                    return map
                }, {})
                this.nameToId = this.rawData.reduce(function (map, station) {
                    // @ts-ignore
                    map[station.name] = station.id
                    return map
                }, {})
            }).catch((error) => {
                console.log(error)
                ElNotification({
                    offset: 70,
                    title: 'getStation错误',
                    message: h('error', { style: 'color: teal' }, error.response?.data.msg),
                })
            })
        }
    }
})