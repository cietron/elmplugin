package com.example.untitled

import com.example.untitled.apiImpl.event.EventManagerImpl
import com.example.untitled.apiImpl.spell.CooldownManager
import com.example.untitled.apiImpl.spell.SpellManagerImpl
import com.example.untitled.events.Projectile
import com.example.untitled.events.onPlayerInteract
import com.example.untitled.events.onTick
import com.example.untitled.luaLoader.EventManager
import com.example.untitled.luaLoader.LoadStartupScripts
import com.example.untitled.luaLoader.ScriptLoader
import com.example.untitled.luaLoader.ScriptManager
import com.example.untitled.player.BuiltinStatsDisplay
import com.example.untitled.storage.SimpleStorage
import com.example.untitled.storage.StorageImpl
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

class Untitled : JavaPlugin() {

    companion object {

        // Could be a bad idea
        lateinit var instance: Untitled
            private set

        val scriptManager = ScriptManager()
        val eventManager = EventManager()
        val simpleStorage = SimpleStorage()
        val newEventManager = EventManagerImpl()

        // temporary concrete class solution
        val cooldownManager = CooldownManager(newEventManager)

        val storageManager = StorageImpl()
        val spellManager = SpellManagerImpl(storageManager)

    }

    override fun onEnable() {
        // Plugin startup logic
        instance = this

        server.pluginManager.registerEvents(onPlayerInteract(), this)
        server.pluginManager.registerEvents(onTick(), this)
        server.pluginManager.registerEvents(Projectile(), this)

        scriptManager.loader = ScriptLoader(dataFolder)
        scriptManager.reload()

        registerCommand()
        LoadStartupScripts.Companion.load()
        BuiltinStatsDisplay.register()
    }

    fun registerCommand() {
        this.lifecycleManager.registerEventHandler(
            LifecycleEvents.COMMANDS,
            { event ->
                event
                    .registrar()
                    ?.register(
                        Commands.literal("DumpScriptManager")
                            .executes({ ctx1 ->
                                ctx1.source.sender.sendMessage(
                                    "Persistent: ${scriptManager.persistentStorage}"
                                )
                                ctx1.source.sender.sendMessage("Normal: ${scriptManager.storage}")
                                return@executes 0
                            })
                            .build()
                    )

                event
                    .registrar()
                    ?.register(
                        Commands.literal("reloadScripts")
                            .executes { ctx ->
                                cooldownManager.clean()
                                Untitled.newEventManager.clear()
                                storageManager.clear()
                                scriptManager.reload()
                                LoadStartupScripts.Companion.load()
                                cooldownManager.registerEvent()
                                return@executes 0
                            }
                            .build()
                    )

                event
                    .registrar()
                    ?.register(
                        Commands.literal("DumpStorage")
                            .executes({ ctx1 ->
                                simpleStorage.debugDump()
                                storageManager.debugDump()
                                ctx1.source.sender.sendMessage(
                                    "Storage content has been printed to the server console"
                                )
                                return@executes 0
                            })
                            .build()
                    )

                event
                    .registrar()
                    ?.register(
                        Commands.literal("DumpEventManager")
                            .executes({ ctx1 ->
                                newEventManager.debugDump()
                                return@executes 0
                            })
                            .build()
                    )
            },
        )
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
