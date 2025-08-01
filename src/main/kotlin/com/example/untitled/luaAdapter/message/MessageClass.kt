package com.example.untitled.luaAdapter.message

import com.example.untitled.api.message.Message
import com.example.untitled.api.message.Message.MessageType
import com.example.untitled.luaAdapter.util.BaseLuaTable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class message
 * @custom.LuaDoc ---@field type string # Current supported message type: ["scoreboard", "action_bar", "chat"]
 * @custom.LuaDoc ---@field identifier string # Unique identifier for the message
 * @custom.LuaDoc ---@field miniMessage string # miniMessage string.
 * @custom.LuaDoc ---@field scoreboardEntries string[]|nil # miniMessage strings. Only for type "scoreboard".
 * @custom.LuaDoc local message = {}
 */
class MessageClass : BaseLuaTable<Message>("message", true) {
    override fun modifyTable(table: LuaTable, container: Message) {
        table.set("type", container.type.name.lowercase())
        table.set("identifier", container.identifier)
        table.set("miniMessage", PlainTextComponentSerializer.plainText().serialize(container.get()))
        if (container.type == MessageType.SCOREBOARD) {
            val entries = container.getScoreboardEntries()
            if (entries != null) {
                val luaEntries = LuaTable()
                var idx = 1
                for ((text, score) in entries) {
                    val entryTable = LuaTable()
                    entryTable.set("text", PlainTextComponentSerializer.plainText().serialize(text))
                    entryTable.set("score", score)
                    luaEntries.set(idx, entryTable)
                    idx++
                }
                table.set("scoreboardEntries", luaEntries)
            }
        }
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        val type = table.get("type")
        val identifier = table.get("identifier")
        val miniMessage = table.get("miniMessage")
        return type.isstring() && identifier.isstring() && miniMessage.isstring()
    }

    override fun fromTable(table: LuaTable): Message? {
        if (!checkParseTable(table)) return null

        val typeStr = table.get("type").tojstring().uppercase()
        val identifier = table.get("identifier").tojstring()
        val miniMessageString = table.get("miniMessage").tojstring()

        val type = try {
            MessageType.valueOf(typeStr)
        } catch (_: IllegalArgumentException) {
            return null
        }

        val scoreboardEntriesTable = table.get("scoreboardEntries")
        val scoreboardEntries: List<Pair<Component, Int>>? =
            if (type == MessageType.SCOREBOARD && scoreboardEntriesTable.istable()) {
                val entries = mutableListOf<Pair<Component, Int>>()
                val luaTable = scoreboardEntriesTable.checktable()
                val keys = luaTable.keys()
                for (key in keys) {
                    val scoreboardEntryMiniMessage = luaTable.get(key).tojstring()
                    entries.add(Pair(MiniMessage.miniMessage().deserialize(scoreboardEntryMiniMessage), key.toint()))
                }
                entries.toList()
            } else {
                null
            }

        return object : Message {
            override val type: MessageType = type
            override val identifier: String = identifier
            override fun get(): Component {
                return MiniMessage.miniMessage().deserialize(miniMessageString)
            }

            override fun getScoreboardEntries(): List<Pair<Component, Int>>? {
                return scoreboardEntries
            }
        }
    }
}