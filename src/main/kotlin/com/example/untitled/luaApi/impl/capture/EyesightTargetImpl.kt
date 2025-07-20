package com.example.untitled.luaApi.impl.capture

import com.example.untitled.luaApi.api.capture.EyesightTarget
import org.bukkit.Bukkit
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import java.util.*

class EyesightTargetImpl : OneArgFunction(), EyesightTarget {
    override fun call(arg: LuaValue?): LuaValue? {
        if (arg !is LuaString) {
            return error("arg is not an string")
        }
        try {
            UUID.fromString(arg.tojstring())
        } catch (exception: IllegalArgumentException) {
            return error("arg is not an UUID string")
        }

        return this.get(arg) ?: NIL
    }

    override fun get(casterUUID: LuaString): LuaString? {
        val uuid = UUID.fromString(casterUUID.tojstring())
        val player = Bukkit.getServer().getPlayer(uuid)

        val hit = player!!.rayTraceEntities(9)?.hitEntity

        if (hit == null) {
            return null
        }

        return valueOf(hit.uniqueId.toString())
    }
}
