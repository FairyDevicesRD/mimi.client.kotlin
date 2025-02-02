package ai.fd.mimi.client.annotation

/**
 * An annotation which marks an annotated API as experimental.
 */
@RequiresOptIn(message = "This API is experimental and may be changed in the future.")
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalMimiApi
