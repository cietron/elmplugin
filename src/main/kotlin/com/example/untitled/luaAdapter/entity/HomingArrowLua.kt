package com.example.untitled.luaAdapter.entity

import com.example.untitled.api.entity.HomingArrow
import com.example.untitled.luaAdapter.entity.selectable.SelectableEntityImplLua
import com.example.untitled.luaAdapter.util.Vector3dTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction

/**
 * @custom.LuaDoc ---@param victim selectable_entity
 * @custom.LuaDoc ---@param spawnPoint vector3d
 * @custom.LuaDoc ---@param speed number
 * @custom.LuaDoc ---@return string|nil # returns the UUID of the spawned arrow, or nil if failed
 * @custom.LuaDoc function PLib.spawnHomingArrow(victim, spawnPoint, speed) end
 */
class HomingArrowLua(private val impl: HomingArrow) : ThreeArgFunction() {
    override fun call(victim: LuaValue, spawnPoint: LuaValue, speed: LuaValue): LuaValue {
        if (!victim.istable() || !spawnPoint.istable() || !speed.isnumber()) {
            return NIL
        }

        val victimContainer = SelectableEntityImplLua().fromLuaValue(victim)
            ?: return error("Invalid victim table for ChasingArrowLua")

        val spawnVector = Vector3dTable.toJoml(spawnPoint as LuaTable)
            ?: return error("Invalid spawnPoint table for ChasingArrowLua")

        val parsedSpeed = speed.todouble()

        val uuid = impl.spawn(victimContainer.entity, spawnVector, parsedSpeed)
        return if (uuid != null) valueOf(uuid.toString()) else NIL
    }
}