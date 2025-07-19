package com.example.untitled.luaApi.impl.entity

import com.example.untitled.luaApi.api.entity.KillEntity
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import java.util.*

class KillEntityImpl : OneArgFunction(), KillEntity {
    override fun call(arg: LuaValue?): LuaValue? {
        if (arg !is LuaString) {
            return LuaValue.error("arg is not an string")
        }
        try {
            UUID.fromString(arg.tojstring())
        } catch (exception: IllegalArgumentException) {
            return error("arg is not an UUID string")
        }

        this.kill(arg)
        return NIL
    }

    override fun kill(entityUUID: LuaString) {
        val uuid = UUID.fromString(entityUUID.tojstring())
        //        Bukkit.getServer().getEntity(uuid)
        (Bukkit.getServer().getEntity(uuid) as LivingEntity).damage(200.0)
    }
}
