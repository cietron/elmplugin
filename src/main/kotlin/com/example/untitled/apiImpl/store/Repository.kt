package com.example.untitled.apiImpl.store

interface Repository<T : Any> {
    fun register(identifier: String, obj: T): Boolean
    fun get(identifier: String): T?
    fun getAll(): List<T>
}