package com.example.untitled.commands

import com.example.untitled.Untitled
import com.example.untitled.Untitled.Companion.cooldownManager
import com.example.untitled.Untitled.Companion.newEventManager
import com.example.untitled.Untitled.Companion.persistentStorage
import com.example.untitled.Untitled.Companion.scriptManager
import com.example.untitled.Untitled.Companion.simpleStorage
import com.example.untitled.Untitled.Companion.storageManager
import com.example.untitled.luaLoader.LoadStartupScripts
import com.example.untitled.luaLoader.ScriptManager
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import java.util.logging.Logger

object PluginCommands {
    fun dumpScriptManagerCommands(
        logger: Logger,
        scriptManager: ScriptManager
    ): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("dumpScriptManager")
            .requires { stack -> stack.sender.isOp }
            .executes { ctx1 ->
                ctx1.source.sender.sendMessage("Scriptmanager content has been dumped to server logs")
                logger.info(scriptManager.storage.toString())
                logger.info(scriptManager.persistentStorage.toString())
                return@executes 0
            }.build()
    }

    fun reloadScriptsCommand(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("reloadScripts")
            .requires { stack -> stack.sender.isOp }
            .executes { ctx ->
                cooldownManager.clean()
                newEventManager.clear()
                storageManager.clear()
                scriptManager.reload()
                LoadStartupScripts.load()
                cooldownManager.registerEvent()
                return@executes 0
            }
            .build()
    }

    fun dumpStorageCommand(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("DumpStorage")
            .requires { stack -> stack.sender.isOp }
            .executes({ ctx1 ->
                simpleStorage.debugDump()
                storageManager.debugDump()
                persistentStorage.debugDump()
                ctx1.source.sender.sendMessage(
                    "Storage content has been printed to the server console"
                )
                return@executes 0
            })
            .build()
    }

    fun dumpEventManagerCommand(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("DumpEventManager")
            .requires { stack -> stack.sender.isOp }
            .executes({ ctx1 ->
                newEventManager.debugDump()
                return@executes 0
            })
            .build()
    }

    fun openInventoryCommand(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("openInventory")
            .executes { ctx1 ->
                Untitled.screenManager.openScreen(ctx1.source.sender as Player, "spellEquipScreen")
                return@executes 0
            }.build()
    }

    fun obtainSpell(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("obtainSpell")
            .executes { ctx1 ->
                Untitled.screenManager.openScreen(ctx1.source.sender as Player, "obtainSpell")
                return@executes 0
            }.build()
    }
}