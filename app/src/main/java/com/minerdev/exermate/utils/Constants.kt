package com.minerdev.exermate.utils

object Constants {
    const val DEV_MODE = 0
    const val DEV_MODE_WITHOUT_SERVER = 1
    const val PRODUCTION_MODE = 2
    const val APPLICATION_MODE = DEV_MODE

    const val TAG = "DEBUG_TAG"
    const val FINISH_INTERVAL_TIME = 2000
    const val FILE_MAX_SIZE: Long = 10485760

    var SERVER_IP_ADDRESS: String = ""
    var SERVER_HTTP_PORT: String = ""
    var SERVER_WEB_SOCKET_PORT: String = ""

    const val API_AUTH = "/auth"
    const val API_USER = "/user"
    const val API_POST = "/exerpost"
    const val API_CHATROOM = "/chatroom"

    var USER_EMAIL: String = ""
}