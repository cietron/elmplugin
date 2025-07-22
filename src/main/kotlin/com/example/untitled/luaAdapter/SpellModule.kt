package com.example.untitled.luaAdapter

import com.example.untitled.api.spell.SpellContext
import com.example.untitled.apiImpl.entity.PlayerImpl
import com.example.untitled.apiImpl.spell.GetSpellCasterImpl
import com.example.untitled.luaAdapter.spell.GetSpellCasterImplLua
import org.bukkit.entity.Player
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class SpellModule(val caster1: Player) : TwoArgFunction() {
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

        env.set("spellLib", library)
        env["package"]["loaded"].set("spellLib", library)
        return library
    }
}