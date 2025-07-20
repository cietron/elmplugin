package com.example.untitled.luaApi.impl.entity

import com.example.untitled.luaApi.api.entity.EntityLocation
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import java.util.*

class EntityLocationImpl : OneArgFunction(), EntityLocation {
    override fun call(arg: LuaValue?): LuaValue? {
        if (arg == null || !arg.isstring()) {
            return error("Entity location parameter is not a string $arg")
        }

        return this.getLocation(arg as LuaString)
    }

    override fun getLocation(entityUUID: LuaString): LuaTable {
        val entityLocation =
            (Bukkit.getServer().getEntity(UUID.fromString(entityUUID.tojstring())) as LivingEntity)
                .location

        val toLuaValue = { d: Double -> valueOf(d) }

        val table = LuaTable()
        table.insert(1, toLuaValue(entityLocation.x))
        table.insert(2, toLuaValue(entityLocation.y))
        table.insert(3, toLuaValue(entityLocation.z))
        return table
    }
}
