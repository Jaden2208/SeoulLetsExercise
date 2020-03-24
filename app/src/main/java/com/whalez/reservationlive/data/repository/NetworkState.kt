package com.whalez.reservationlive.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "문제가 발생했습니다!")
        val ENDOFLIST: NetworkState = NetworkState(Status.FAILED, "페이지의 끝에 도달했습니다.")
    }
}