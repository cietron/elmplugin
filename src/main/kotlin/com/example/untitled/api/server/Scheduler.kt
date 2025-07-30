package com.example.untitled.api.server

interface Scheduler {

    fun scheduleTask(task: Runnable, ticksAfter: Long)

}