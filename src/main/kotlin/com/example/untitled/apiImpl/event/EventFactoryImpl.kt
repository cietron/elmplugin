package com.example.untitled.apiImpl.event

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.EventFactory

class EventFactoryImpl : EventFactory {
    override fun createOnTick(): BuiltinEvents.Companion.OnTick {
        return object : BuiltinEvents.Companion.OnTick {}
    }
}