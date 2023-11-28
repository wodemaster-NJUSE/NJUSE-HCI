function parseDate(date: number | string | undefined) {
    if (date === undefined) {
        return ''
    }
    return new Date(date).toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai', year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

function calDuration(departure: number, arrival: number) {
    let duration = arrival - departure
    let day = Math.floor(duration / (24 * 60 * 60 * 1000))
    let hour = Math.floor((duration % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000))
    let minute = Math.floor((duration % (60 * 60 * 1000)) / (60 * 1000))
    if (day === 0) {
        if (hour === 0) {
            return `${minute}分钟`
        } else {
            return `${hour}小时${minute}分钟`
        }
    } else {
        return `${day}天${hour}小时${minute}分钟`
    }
}

export { parseDate, calDuration }