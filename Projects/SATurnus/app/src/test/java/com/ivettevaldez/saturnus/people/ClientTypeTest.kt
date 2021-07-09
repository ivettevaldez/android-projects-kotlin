package com.ivettevaldez.saturnus.people

import org.junit.Assert.assertEquals
import org.junit.Test

class ClientTypeTest {

    companion object {

        private const val CLIENT_ISSUING = "Emisor"
        private const val CLIENT_RECEIVER = "Receptor"

        private const val POSITION_ISSUING: Int = 1
        private const val POSITION_RECEIVER: Int = 2
    }

    @Test
    fun getString_typeIssuing_returnsIssuingString() {
        // Arrange
        // Act
        val result = ClientType.getString(ClientType.Type.ISSUING)
        // Assert
        assertEquals(result, CLIENT_ISSUING)
    }

    @Test
    fun getString_typeReceiver_returnsReceiverString() {
        // Arrange
        // Act
        val result = ClientType.getString(ClientType.Type.RECEIVER)
        // Assert
        assertEquals(result, CLIENT_RECEIVER)
    }

    @Test
    fun getValue_issuingString_returnsIssuingType() {
        // Arrange
        // Act
        val result = ClientType.getValue(CLIENT_ISSUING)
        // Assert
        assertEquals(result, ClientType.Type.ISSUING)
    }

    @Test
    fun getValue_receiverString_returnsReceiverType() {
        // Arrange
        // Act
        val result = ClientType.getValue(CLIENT_RECEIVER)
        // Assert
        assertEquals(result, ClientType.Type.RECEIVER)
    }

    @Test
    fun getPosition_issuingString_returnsCorrectPosition() {
        // Arrange
        // Act
        val result = ClientType.getPosition(CLIENT_ISSUING)
        // Assert
        assertEquals(result, POSITION_ISSUING)
    }

    @Test
    fun getPosition_receiverString_returnsCorrectPosition() {
        // Arrange
        // Act
        val result = ClientType.getPosition(CLIENT_RECEIVER)
        // Assert
        assertEquals(result, POSITION_RECEIVER)
    }
}