package com.example.untitled.apiImpl.server

import com.example.untitled.api.server.Scheduler
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class SchedulerImpl(val plugin: Plugin) : Scheduler {
    override fun scheduleTask(task: Runnable, ticksAfter: Long) {
        val p = object : BukkitRunnable() {
            override fun run() {
                task.run()
            }
        }.runTaskLater(plugin, ticksAfter)
    }
}