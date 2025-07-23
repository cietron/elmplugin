package com.example.untitled.luaAdapter.entity

import com.example.untitled.api.entity.SelectableEntity
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction

class EntityEmitSoundLua(val impl: SelectableEntity) : ThreeArgFunction() {
    override fun call(
        arg1: LuaValue?,
        arg2: LuaValue?,
        arg3: LuaValue?
    ): LuaValue {
        // Check for nulls
        if (arg1 == null || arg2 == null || arg3 == null) {
            return error("emitSound requires 3 arguments: soundName (string), volume (number), pitch (number)")
        }
        // Check types
        if (!arg1.isstring()) {
            return error("emitSound: first argument must be a string (soundName)")
        }
        if (!arg2.isnumber()) {
            return error("emitSound: second argument must be a number (volume)")
        }
        if (!arg3.isnumber()) {
            return error("emitSound: third argument must be a number (pitch)")
        }

        val soundName = arg1.checkjstring()
        val volume = arg2.checknumber().tofloat()
        val pitch = arg3.checknumber().tofloat()

        val result = impl.emitSound(soundName, volume, pitch)
        return LuaValue.valueOf(result)
    }
}