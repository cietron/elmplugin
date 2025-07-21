package com.example.untitled.luaAdapter

import com.example.untitled.api.spell.SpellContext
import com.example.untitled.apiImpl.Misc.VisualizeBoundingBoxImpl
import com.example.untitled.apiImpl.capture.EntitiesInRotatedRectImpl
import com.example.untitled.apiImpl.capture.EyesightEntityImpl
import com.example.untitled.apiImpl.capture.RectangleImpl
import com.example.untitled.apiImpl.entity.PlayerImpl
import com.example.untitled.apiImpl.spell.GetSpellCasterImpl
import com.example.untitled.luaAdapter.capture.EntitiesInRotatedRectImplLua
import com.example.untitled.luaAdapter.capture.EyesightEntityImplLua
import com.example.untitled.luaAdapter.capture.RectangleImplLua
import com.example.untitled.luaAdapter.misc.VisualizeBoundingBoxImplLua
import com.example.untitled.luaAdapter.spell.GetSpellCasterImplLua
import org.bukkit.entity.Player
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class PlayMod(val caster1: Player) : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set(
            "GetSpellCaster",
            GetSpellCasterImplLua(
                GetSpellCasterImpl(
                    object : SpellContext {
                        override val caster: com.example.untitled.api.player.Player
                            get() = PlayerImpl(caster1.name, caster1.uniqueId)
                    }
                )
            ),
        )
        library.set("getEyesightEntity", EyesightEntityImplLua(EyesightEntityImpl()))
        library.set("getBoundingBoxEntities", RectangleImplLua(RectangleImpl()))
        library.set("visualizeBox", VisualizeBoundingBoxImplLua(VisualizeBoundingBoxImpl()))
        library.set("getRotatedRect", EntitiesInRotatedRectImplLua(EntitiesInRotatedRectImpl()))
        env.set("newPmod", library)
        env["package"]["loaded"].set("newPmod", library)
        return library
    }
}
