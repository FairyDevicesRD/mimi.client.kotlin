package ai.fd.mimi.client

/**
 * Exception thrown when a JSON serialization/deserialization error occurs.
 */
class MimiJsonException : MimiSerializationException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
