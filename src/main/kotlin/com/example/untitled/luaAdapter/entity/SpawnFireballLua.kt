package com.example.untitled.luaAdapter.entity

import com.example.untitled.api.entity.SpawnFireball
import com.example.untitled.luaAdapter.util.Vector3dTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@param spawnPoint vector3d # The spawn location of the fireball
 * @custom.LuaDoc ---@param velocity vector3d # The velocity vector of the fireball
 * @custom.LuaDoc ---@return string|nil # UUID of the spawned fireball, or nil if failed
 * @custom.LuaDoc function PLib.spawnFireball(spawnPoint, velocity) end
 */
class SpawnFireballLua(private val impl: SpawnFireball) : TwoArgFunction() {
    override fun call(spawnPoint: LuaValue, velocity: LuaValue): LuaValue {
        if (!spawnPoint.istable() || !velocity.istable()) {
            return NIL
        }

        val spawnVec = Vector3dTable.toJoml(spawnPoint.checktable())
            ?: return error("Invalid spawnPoint table for SpawnFireballLua")

        val velocityVec = Vector3dTable.toJoml(velocity.checktable())
            ?: return error("Invalid velocity table for SpawnFireballLua")

        val uuid = impl.spawn(spawnVec, velocityVec)
        return if (uuid != null) LuaValue.valueOf(uuid.toString()) else LuaValue.NIL
    }
}