package com.geancarloleiva.a6_demochat.util

const val BASE_URL = "https://geancarloleiva.com/API/DemoChat/api/"
const val SOCKET_URL = "https://geancarloleiva.com/API/DemoChat/api/"
const val USER_CREATE = "${BASE_URL}user/Create.php"
const val USER_LOGIN = "${BASE_URL}user/Validate.php"
const val USER_GET_INFO = "${BASE_URL}user/FindByEmail/"
const val CHANNEL_GET_ALL = "${BASE_URL}channel/"
const val MESSAGE_GET_ALL_BY_CHANNEL = "${BASE_URL}message/byChannel/"

//Broadcast constants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"
const val BROADCAST_CHANNEL_DATA_CHANGE = "BROADCAST_CHANNEL_DATA_CHANGE"