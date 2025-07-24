package com.example.untitled.storage

interface Storage {

    fun store(key: String, value: StorageValue): Boolean

    fun retrieve(key: String): StorageValue?

    fun deleteKey(key: String)

    fun clear()
}

interface UnsafeStorage : Storage {
    /**
     * Overwrites values if the same key was found in the storage space.
     */
    fun storeRaw(key: String, value: Any)

    /**
     * @param T Retrieving stored value with unmatched data type will raise an exception.
     */
    fun <T> retrieveRaw(key: String): T?
}

