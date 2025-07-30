package com.example.untitled.luaAdapter.server

import com.example.untitled.api.server.Scheduler
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@field scheduleTask fun(runnable: fun(), ticksAfter: number): nil
 * @custom.LuaDoc ---@param runnable fun() # The Lua function to execute
 * @custom.LuaDoc ---@param ticksAfter number # Number of ticks to wait before running the function
 * @custom.LuaDoc ---@return nil
 */
class ScheduleTask(private val impl: Scheduler) : TwoArgFunction() {
    override fun call(runnable: LuaValue, ticksAfter: LuaValue): LuaValue {
        if (!runnable.isfunction()) {
            return error("First argument must be a function")
        }
        if (!ticksAfter.isnumber()) {
            return error("Second argument must be a number (ticksAfter)")
        }
        val task = Runnable { runnable.call() }
        impl.scheduleTask(task, ticksAfter.tolong())
        return LuaValue.NIL
    }
}