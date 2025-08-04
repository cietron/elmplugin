package com.example.untitled.luaLoader.luaModules

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.server.SchedulerImpl
import com.example.untitled.luaAdapter.attribute.RegisterAttribute
import com.example.untitled.luaAdapter.item.RegisterItem
import com.example.untitled.luaAdapter.server.GetRandomUUID
import com.example.untitled.luaAdapter.server.ScheduleTask
import com.example.untitled.luaAdapter.server.cooldownManager.CooldownManagerClass
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@class ServerLib
 * @custom.LuaDoc ---@field registerAttribute fun(attributeIdentifier: string, defaultValue: number)
 * @custom.LuaDoc ---@field scheduleTask fun(runnable: fun(), ticksAfter: number): nil
 * @custom.LuaDoc ---@field cooldownManager cooldownManager
 * @custom.LuaDoc ---@field getRandomUUID fun(): string
 * @custom.LuaDoc local ServerLib = {}
 */
class ServerModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("registerAttribute", RegisterAttribute(Untitled.attributeRepository))
        library.set("scheduleTask", ScheduleTask(SchedulerImpl(Untitled.instance)))
        library.set("registerItem", RegisterItem(Untitled.equipmentRepository))
        library.set("cooldownManager", CooldownManagerClass().getNewTable(Untitled.cooldownManager))
        library.set("getRandomUUID", GetRandomUUID())

        env.set("ServerLib", library)
        env["package"]["loaded"].set("ServerLib", library)
        return library
    }
}