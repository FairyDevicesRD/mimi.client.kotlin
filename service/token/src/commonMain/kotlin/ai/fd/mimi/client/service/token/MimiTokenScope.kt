package ai.fd.mimi.client.service.token

import androidx.annotation.VisibleForTesting

/**
 * A collection of scopes for the Mimi API.
 *
 * See [API Documentation](https://mimi.readme.io/reference/scope) for more information.
 */
object MimiTokenScopes {
    object Authority {
        val Developers: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/developers.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/developers.w")
        )

        val Applications: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/applications.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/applications.w")
        )

        val Clients: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/clients.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/clients.w")
        )
    }

    val Asr: MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> = httpAndWebSocketApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/asr/http-api-service"),
        websocket = scopeOf("https://apis.mimi.fd.ai/auth/asr/websocket-api-service")
    )

    val GoogleAsr: MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> = httpAndWebSocketApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/google-asr/http-api-service"),
        websocket = scopeOf("https://apis.mimi.fd.ai/auth/google-asr/websocket-api-service")
    )

    val NictAsr: MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> = httpAndWebSocketApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/nict-asr/http-api-service"),
        websocket = scopeOf("https://apis.mimi.fd.ai/auth/nict-asr/websocket-api-service")
    )

    val Lid: MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> = httpAndWebSocketApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/lid/http-api-service"),
        websocket = scopeOf("https://apis.mimi.fd.ai/auth/lid/websocket-api-service")
    )

    data object Srs : MimiTokenGroupScope() {
        val Api: MimiTokenHttpWebSocketApiScope = MimiTokenHttpWebSocketApiScope(
            Http = scopeOf("https://apis.mimi.fd.ai/auth/srs/http-api-service"),
            WebSocket = scopeOf("https://apis.mimi.fd.ai/auth/srs/websocket-api-service")
        )

        val SpeakerGroups: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/srs/speaker_groups.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/srs/speaker_groups.w")
        )

        val Speakers: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/srs/speakers.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/srs/speakers.w")
        )

        val Speeches: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/srs/speeches.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/srs/speeches.w")
        )

        val Trainers: MimiTokenReadWriteScope = MimiTokenReadWriteScope(
            Read = scopeOf("https://apis.mimi.fd.ai/auth/srs/trainers.r"),
            Write = scopeOf("https://apis.mimi.fd.ai/auth/srs/trainers.w")
        )

        override val childScopes: Set<MimiTokenScope> = setOf(Api, SpeakerGroups, Speakers, Speeches, Trainers)
    }

    val Tts: MimiTokenApiGroupScope<MimiTokenHttpApiScope> = httpApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/tts/http-api-service")
    )

    val Tra: MimiTokenApiGroupScope<MimiTokenHttpApiScope> = httpApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/tra/http-api-service")
    )

    val Air: MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> = httpAndWebSocketApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/air/http-api-service"),
        websocket = scopeOf("https://apis.mimi.fd.ai/auth/air/websocket-api-service")
    )

    val Emo: MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> = httpAndWebSocketApiScopeOf(
        http = scopeOf("https://apis.mimi.fd.ai/auth/emo-categorical/http-api-service"),
        websocket = scopeOf("https://apis.mimi.fd.ai/auth/emo-categorical/websocket-api-service")
    )
}

/**
 * A group of scopes for reading and writing to the target service.
 *
 * [Read] or [Write] is used to represent the scope of read and write permissions, respectively.
 * Specifying this scope itself will represent both read and write scopes together.
 */
@Suppress("PropertyName")
class MimiTokenReadWriteScope internal constructor(
    val Read: MimiTokenScope,
    val Write: MimiTokenScope
) : MimiTokenGroupScope() {
    override val childScopes: Set<MimiTokenScope> = setOf(Read, Write)
}

/**
 * A group of scopes for using Mimi API services.
 */
@Suppress("PropertyName")
class MimiTokenApiGroupScope<T : MimiTokenApiScope>(val Api: T) : MimiTokenGroupScope() {
    override val childScopes: Set<MimiTokenScope> = Api.childScopes
}

/**
 * An interface representing a scope for the Mimi API.
 */
sealed interface MimiTokenScope

/**
 * An implementation of [MimiTokenScope] which represents a single scope.
 */
internal data class MimiTokenSingleScope(val value: String) : MimiTokenScope

/**
 * An implementation of [MimiTokenScope] which represents multiple scopes.
 */
abstract class MimiTokenGroupScope : MimiTokenScope {
    internal abstract val childScopes: Set<MimiTokenScope>
}

/**
 * An implementation of [MimiTokenScope] which represents a group of scopes for using the Mimi API service.
 */
sealed class MimiTokenApiScope(scopes: Set<MimiTokenScope>) : MimiTokenGroupScope() {
    final override val childScopes: Set<MimiTokenScope> = scopes
}

/**
 * An implementation of [MimiTokenApiScope] which represents a scope for using the HTTP service API.
 */
@Suppress("PropertyName")
class MimiTokenHttpApiScope internal constructor(val Http: MimiTokenScope) : MimiTokenApiScope(setOf(Http))

/**
 * An implementation of [MimiTokenApiScope] which represents a scope for using the HTTP and WebSocket service APIs.
 */
@Suppress("PropertyName")
class MimiTokenHttpWebSocketApiScope internal constructor(
    val Http: MimiTokenScope,
    val WebSocket: MimiTokenScope
) : MimiTokenApiScope(setOf(Http, WebSocket))


@VisibleForTesting
internal inline fun scopeOf(scopeId: String): MimiTokenScope = MimiTokenSingleScope(scopeId)

private inline fun httpAndWebSocketApiScopeOf(
    http: MimiTokenScope,
    websocket: MimiTokenScope
): MimiTokenApiGroupScope<MimiTokenHttpWebSocketApiScope> =
    MimiTokenApiGroupScope(MimiTokenHttpWebSocketApiScope(http, websocket))

private inline fun httpApiScopeOf(http: MimiTokenScope): MimiTokenApiGroupScope<MimiTokenHttpApiScope> =
    MimiTokenApiGroupScope(MimiTokenHttpApiScope(http))

internal fun Set<MimiTokenScope>.getContainingScopes(): Set<MimiTokenSingleScope> =
    flatMap { it.getContainingScopes() }.toSet()

@VisibleForTesting
internal fun MimiTokenScope.getContainingScopes(): Set<MimiTokenSingleScope> = when (this) {
    is MimiTokenSingleScope -> setOf(this)
    is MimiTokenGroupScope -> childScopes.getContainingScopes()
}
