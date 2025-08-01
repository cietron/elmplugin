package com.example.untitled.apiImpl.message

import com.example.untitled.api.message.Message
import com.example.untitled.api.message.MessageHandler
import com.example.untitled.api.player.Player
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot

class ScoreboardMessageHandler : MessageHandler {
    override fun handle(player: Player, msg: Message) {
        val bukkitPlayer = Bukkit.getPlayer(player.uuid) ?: return

        val component = msg.get()
        if (bukkitPlayer.scoreboard.getObjective(msg.identifier) == null) {
            bukkitPlayer.scoreboard.registerNewObjective(msg.identifier, Criteria.DUMMY, component)
        }
        val obj = bukkitPlayer.scoreboard.getObjective(msg.identifier)!!
        obj.displayName(component)
        obj.displaySlot = DisplaySlot.SIDEBAR

        for ((entry, i) in msg.getScoreboardEntries()!!) {
            val score = obj.getScore("$i")
            score.customName(entry)
            score.score = i
        }
    }
}