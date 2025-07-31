package com.example.untitled

import com.example.untitled.apiImpl.entity.AttributeManagerImpl
import com.example.untitled.apiImpl.event.EventManagerImpl
import com.example.untitled.apiImpl.spell.CooldownManager
import com.example.untitled.apiImpl.spell.SpellManagerImpl
import com.example.untitled.apiImpl.spell.SpellRepositoryImpl
import com.example.untitled.commands.PluginCommands
import com.example.untitled.events.*
import com.example.untitled.luaLoader.EventManager
import com.example.untitled.luaLoader.LoadStartupScripts
import com.example.untitled.luaLoader.ScriptLoader
import com.example.untitled.luaLoader.ScriptManager
import com.example.untitled.player.BuiltinStatsDisplay
import com.example.untitled.screen.ScreenManager
import com.example.untitled.storage.SimpleStorage
import com.example.untitled.storage.StorageImpl
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
        val spellManager = SpellManagerImpl(storageManager, cooldownManager)

        val persistentStorage = StorageImpl()
        val attributeManager = AttributeManagerImpl(persistentStorage)

        val spellRepository = SpellRepositoryImpl(storageManager)
        val screenManager = ScreenManager()
    }

    override fun onEnable() {
        // Plugin startup logic
        instance = this

        this.registerEvents()

        scriptManager.loader = ScriptLoader(dataFolder)
        scriptManager.reload()

        registerCommand()
        LoadStartupScripts.load()
        BuiltinStatsDisplay.register()
        LoadStartupScripts.loadServer()
    }

    override fun onDisable() {
        attributeManager.clean()
    }

    fun registerEvents() {
        server.pluginManager.registerEvents(onPlayerInteract(), this)
        server.pluginManager.registerEvents(onTick(), this)
        server.pluginManager.registerEvents(Projectile(), this)
        server.pluginManager.registerEvents(EntityExplode(), this)
        server.pluginManager.registerEvents(InventoryListener(), this)
    }

    fun registerCommand() {
        this.lifecycleManager.registerEventHandler(
            LifecycleEvents.COMMANDS,
            { event ->
                event.registrar()?.register(PluginCommands.dumpScriptManagerCommands(this.logger, scriptManager))
                event.registrar()?.register(PluginCommands.reloadScriptsCommand())
                event.registrar()?.register(PluginCommands.dumpStorageCommand())
                event.registrar()?.register(PluginCommands.dumpEventManagerCommand())
                event.registrar()?.register(PluginCommands.openInventoryCommand())
                event.registrar()?.register(PluginCommands.obtainSpell())
            }
        )
    }


}
