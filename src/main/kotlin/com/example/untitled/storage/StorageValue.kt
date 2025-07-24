package com.example.untitled.storage

sealed class StorageValue {
    data class IntValue(val value: Int) : StorageValue()
    data class DoubleValue(val value: Double) : StorageValue()
    data class StringValue(val value: String) : StorageValue()
}