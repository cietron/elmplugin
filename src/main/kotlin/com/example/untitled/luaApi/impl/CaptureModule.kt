package com.example.untitled.luaApi.impl

import com.example.untitled.luaApi.impl.capture.EyesightTargetImpl
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class CaptureModule() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("eyesightTarget", EyesightTargetImpl())
        env.set("capture", library)
        env["package"]["loaded"].set("capture", library)
        return library
    }
}
