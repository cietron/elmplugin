package com.example.untitled.luaLoader.luaModules

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.Misc.VisualizeBoundingBoxImpl
import com.example.untitled.apiImpl.capture.EntitiesInRotatedRectImpl
import com.example.untitled.apiImpl.capture.EyesightEntityImpl
import com.example.untitled.apiImpl.capture.RectangleImpl
import com.example.untitled.luaAdapter.capture.EntitiesInRotatedRectImplLua
import com.example.untitled.luaAdapter.capture.EyesightEntityImplLua
import com.example.untitled.luaAdapter.capture.RectangleImplLua
import com.example.untitled.luaAdapter.event.GetEventManagerLua
import com.example.untitled.luaAdapter.misc.VisualizeBoundingBoxImplLua
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class PlayMod() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()

        library.set("getEyesightEntity", EyesightEntityImplLua(EyesightEntityImpl()))
        library.set("getBoundingBoxEntities", RectangleImplLua(RectangleImpl()))
        library.set("visualizeBox", VisualizeBoundingBoxImplLua(VisualizeBoundingBoxImpl()))
        library.set("getRotatedRect", EntitiesInRotatedRectImplLua(EntitiesInRotatedRectImpl()))
        library.set("getEventManager", GetEventManagerLua(Untitled.Companion.newEventManager))
        env.set("newPmod", library)
        env["package"]["loaded"].set("newPmod", library)
        return library
    }
}