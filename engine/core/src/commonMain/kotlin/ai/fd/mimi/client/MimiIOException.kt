package ai.fd.mimi.client

/**
 * Exception thrown when an I/O error occurs.
 */
class MimiIOException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
