package com.example.untitled.luaApi.impl.entity

import com.example.untitled.luaApi.api.entity.AddVelocity
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.util.*

class AddVelocityImpl : TwoArgFunction(), AddVelocity {
    override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue? {
        TODO("Not yet implemented")
    }

    override fun add(entityUUID: LuaString, vec: LuaTable) {

        val entity =
            Bukkit.getServer().getEntity(UUID.fromString(entityUUID.tojstring())) as LivingEntity
        val velocity =
            Vector(vec.get("x").todouble(), vec.get("y").todouble(), vec.get("z").todouble())

        entity.velocity.add(velocity)
        TODO("Not yet implemented")
    }
}
