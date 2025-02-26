package ai.fd.mimi.client

/**
 * Exception thrown when serialization/deserialization error occurs.
 */
open class MimiSerializationException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
