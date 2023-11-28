import { defineStore } from "pinia";

export const useSearchStore = defineStore('search', {
  state: () => {
    return {
      start_station_id: undefined,
      end_station_id: undefined,
      date: ''
    }
  }
})