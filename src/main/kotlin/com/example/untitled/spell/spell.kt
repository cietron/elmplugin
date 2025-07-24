package com.example.untitled.spell

import com.example.untitled.Untitled
import com.example.untitled.player.PlayerMessenger
import net.kyori.adventure.text.TextComponent
import org.bukkit.inventory.ItemStack

object spell {

    fun tryExecute(spellIdentifier: String, caster: com.example.untitled.api.player.Player) {
        val spellObj = Untitled.spellManager.getSpell(spellIdentifier)

        if (spellObj == null) {
            Untitled.instance.logger.info("${caster.name} attempted to cast non-existing spell $spellIdentifier.")
            return
        }

        if (!spellObj.preCheck(caster)) {
            return
        }

        if (Untitled.cooldownManager.isCoolingDown(caster, spellIdentifier)) {
            val remainingTicks = Untitled.cooldownManager.getRemainingTicks(caster, spellIdentifier)
            PlayerMessenger.sendCooldownMessage(caster, spellIdentifier, remainingTicks)
            return
        }

        spellObj.execute(caster)
        Untitled.cooldownManager.store(caster, spellIdentifier, spellObj.cooldownTicks)
    }

    fun parseSpellname(itemStack: ItemStack): String? {
        val displayName = itemStack.itemMeta?.displayName()

        if (displayName == null || displayName !is TextComponent) {
            return null
        }
        val spellname = displayName.content()
        return spellname
    }


}
