package org.wadophone.core

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DoorBellUtilsTest {
    @Test
    fun `matches configured extension from sip uri`() {
        assertTrue(DoorBellUtils.matchesConfiguredDoorbellExtension("sip:100@doorbell.local", "100"))
        assertTrue(DoorBellUtils.matchesConfiguredDoorbellExtension("sip:100@doorbell.local", "200,100"))
        assertFalse(DoorBellUtils.matchesConfiguredDoorbellExtension("sip:200@doorbell.local", "100"))
        assertFalse(DoorBellUtils.matchesConfiguredDoorbellExtension("sip:200@doorbell.local", ""))
    }
}
