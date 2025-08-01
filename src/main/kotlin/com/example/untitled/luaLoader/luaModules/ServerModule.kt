package com.example.untitled.luaLoader.luaModules

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.server.SchedulerImpl
import com.example.untitled.luaAdapter.attribute.RegisterAttribute
import com.example.untitled.luaAdapter.item.RegisterItem
import com.example.untitled.luaAdapter.server.ScheduleTask
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class ServerModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("registerAttribute", RegisterAttribute(Untitled.attributeRepository))
        library.set("scheduleTask", ScheduleTask(SchedulerImpl(Untitled.instance)))
        library.set("registerItem", RegisterItem(Untitled.equipmentRepository))

        env.set("ServerLib", library)
        env["package"]["loaded"].set("ServerLib", library)
        return library
    }
}