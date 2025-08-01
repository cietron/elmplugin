package com.example.untitled.api.message

import net.kyori.adventure.text.Component

interface Message {
    val type: MessageType
    val identifier: String
    fun get(): Component

    /**
     * Returns scoreboard entries if this is a SCOREBOARD message, null otherwise.
     * Each pair contains (display_component, score_value) where higher scores appear higher.
     */
    fun getScoreboardEntries(): List<Pair<Component, Int>>?

    enum class MessageType(val key: String) {
        SCOREBOARD("scoreboard"),
        ACTION_BAR("action_bar"),
        CHAT("chat")
    }

}
