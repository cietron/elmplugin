package com.example.untitled

import com.example.untitled.events.onPlayerInteract
import com.example.untitled.luaLoader.ScriptLoader
import com.example.untitled.luaLoader.ScriptManager
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

class Untitled : JavaPlugin() {

    companion object {
        val scriptManager = ScriptManager()
    }

    override fun onEnable() {
        // Plugin startup logic
        server.pluginManager.registerEvents(onPlayerInteract(), this)
        //        val dataFolder = dataFolder.resolve("scripts")
        //        dataFolder.mkdirs()
        //        val scriptLoc = dataFolder.resolve("hello.lua").createNewFile()
        //        println("Datafolder location: ${scriptLoc}")

        scriptManager.loader = ScriptLoader(dataFolder)
        scriptManager.reload()

        registerCommand()
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
                                ctx1.source.sender.sendMessage(
                                    "Normal: ${scriptManager.storage}"
                                )
                                return@executes 0
                            })
                            .build()
                    )

                event
                    .registrar()
                    ?.register(
                        Commands.literal("reloadScripts")
                            .executes { ctx ->
                                scriptManager.reload()
                                return@executes 0
                            }
                            .build()
                    )
            },
        )
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
