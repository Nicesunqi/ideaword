const mutations = {
  requestCounter(state, num) {
    state.requestCount += num
    if (state.requestCount < 0) {
      state.requestCount = 0
    }
  }
}

export default mutations
