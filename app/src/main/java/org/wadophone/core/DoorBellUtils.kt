package org.wadophone.core

object DoorBellUtils {
    fun matchesConfiguredDoorbellExtension(remoteAddress: String?, configuredValues: String): Boolean {
        if (remoteAddress.isNullOrBlank() || configuredValues.isBlank()) {
            return false
        }

        val configuredExtensions = configuredValues
            .split(",")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        if (configuredExtensions.isEmpty()) {
            return false
        }

        val normalizedAddress = remoteAddress.trim().lowercase()
        val sanitizedAddress = normalizedAddress
            .removePrefix("sip:")
            .removePrefix("sips:")
            .substringBefore('@')

        return sanitizedAddress in configuredExtensions ||
            normalizedAddress.substringBefore('@').removePrefix("sip:").removePrefix("sips:") in configuredExtensions
    }
}
