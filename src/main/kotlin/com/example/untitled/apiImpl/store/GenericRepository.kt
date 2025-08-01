package com.example.untitled.apiImpl.store

import com.example.untitled.storage.UnsafeStorage

class GenericRepository<T : Any>(val storageBaseKey: String, val storage: UnsafeStorage) : Repository<T> {

    override fun register(identifier: String, obj: T): Boolean {
        val key = this.getKey(identifier)
        if (storage.retrieveRaw<T>(key) != null) {
            return false
        }

        storage.storeRaw(key, obj)
        this.getRegisteredObjects().add(identifier)
        return true
    }

    override fun get(identifier: String): T? {
        val key = this.getKey(identifier)
        return storage.retrieveRaw<T>(key)
    }

    override fun getAll(): List<T> {
        val objects = ArrayList<T>()
        for (objectKey in this.getRegisteredObjects()) {
            objects.add(this.get(objectKey)!!)
        }

        return objects.toList()
    }

    private fun getRegisteredObjects(): MutableList<String> {
        val key = "${storageBaseKey}_RegisteredObjects"
        if (storage.retrieveRaw<MutableList<String>>(key) == null) {
            storage.storeRaw(key, ArrayList<String>())
        }

        return storage.retrieveRaw<MutableList<String>>(key)!!
    }

    private fun getKey(identifier: String): String {
        return "$storageBaseKey#$identifier"
    }
}