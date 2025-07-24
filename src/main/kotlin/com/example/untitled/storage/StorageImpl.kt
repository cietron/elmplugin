package com.example.untitled.storage

class StorageImpl : UnsafeStorage {

    private val storageSpace = HashMap<String, StorageValue>()
    private val unsafeStorageSpace = HashMap<String, Any>()

    override fun storeRaw(key: String, value: Any) {
        this.unsafeStorageSpace.put(key, value)
    }

    override fun <T> retrieveRaw(key: String): T? {
        val obj = this.unsafeStorageSpace.get(key)

        obj ?: return null

        return obj as? T
            ?: throw Exception("Retrieved raw object $key with incorrect data type. Excepted type: ${obj::class.qualifiedName}")

    }

    override fun store(key: String, value: StorageValue): Boolean {
        this.storageSpace.put(key, value)
        return true
    }

    override fun retrieve(key: String): StorageValue? {
        return this.storageSpace.get(key)
    }

    override fun deleteKey(key: String) {
        this.storageSpace.remove(key)
        this.unsafeStorageSpace.remove(key)
    }

    override fun clear() {
        this.storageSpace.clear()
        this.unsafeStorageSpace.clear()
    }
}