package com.geancarloleiva.a6_demochat.model

class Channel (val name: String, val description: String, val id: String) {

    override fun toString(): String {
        return "$name"
    }
}