package ai.fd.mimi.client.service.token

import kotlin.test.Test
import kotlin.test.assertEquals

class MimiTokenScopesTest {

    @Test
    fun testAuthority_Developers() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/developers.r",
            (MimiTokenScopes.Authority.Developers.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/developers.w",
            (MimiTokenScopes.Authority.Developers.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Authority.Developers.Read,
                MimiTokenScopes.Authority.Developers.Write
            ),
            MimiTokenScopes.Authority.Developers.getContainingScopes()
        )
    }

    @Test
    fun testAuthority_Applications() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/applications.r",
            (MimiTokenScopes.Authority.Applications.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/applications.w",
            (MimiTokenScopes.Authority.Applications.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Authority.Applications.Read,
                MimiTokenScopes.Authority.Applications.Write
            ),
            MimiTokenScopes.Authority.Applications.getContainingScopes()
        )
    }

    @Test
    fun testAuthority_Clients() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/clients.r",
            (MimiTokenScopes.Authority.Clients.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/clients.w",
            (MimiTokenScopes.Authority.Clients.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Authority.Clients.Read,
                MimiTokenScopes.Authority.Clients.Write
            ),
            MimiTokenScopes.Authority.Clients.getContainingScopes()
        )
    }

    @Test
    fun testAsr() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/asr/http-api-service",
            (MimiTokenScopes.Asr.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/asr/websocket-api-service",
            (MimiTokenScopes.Asr.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Asr.Api.Http,
                MimiTokenScopes.Asr.Api.WebSocket
            ),
            MimiTokenScopes.Asr.Api.getContainingScopes()
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Asr.Api.Http,
                MimiTokenScopes.Asr.Api.WebSocket
            ),
            MimiTokenScopes.Asr.getContainingScopes()
        )
    }

    @Test
    fun testGoogleAsr() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/google-asr/http-api-service",
            (MimiTokenScopes.GoogleAsr.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/google-asr/websocket-api-service",
            (MimiTokenScopes.GoogleAsr.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.GoogleAsr.Api.Http,
                MimiTokenScopes.GoogleAsr.Api.WebSocket
            ),
            MimiTokenScopes.GoogleAsr.Api.getContainingScopes()
        )
        assertEquals(
            setOf(
                MimiTokenScopes.GoogleAsr.Api.Http,
                MimiTokenScopes.GoogleAsr.Api.WebSocket
            ),
            MimiTokenScopes.GoogleAsr.getContainingScopes()
        )
    }

    @Test
    fun testNictAsr() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/nict-asr/http-api-service",
            (MimiTokenScopes.NictAsr.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/nict-asr/websocket-api-service",
            (MimiTokenScopes.NictAsr.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.NictAsr.Api.Http,
                MimiTokenScopes.NictAsr.Api.WebSocket
            ),
            MimiTokenScopes.NictAsr.Api.getContainingScopes()
        )
        assertEquals(
            setOf(
                MimiTokenScopes.NictAsr.Api.Http,
                MimiTokenScopes.NictAsr.Api.WebSocket
            ),
            MimiTokenScopes.NictAsr.getContainingScopes()
        )
    }

    @Test
    fun testLid() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/lid/http-api-service",
            (MimiTokenScopes.Lid.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/lid/websocket-api-service",
            (MimiTokenScopes.Lid.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Lid.Api.Http,
                MimiTokenScopes.Lid.Api.WebSocket
            ),
            MimiTokenScopes.Lid.Api.getContainingScopes()
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Lid.Api.Http,
                MimiTokenScopes.Lid.Api.WebSocket
            ),
            MimiTokenScopes.Lid.getContainingScopes()
        )
    }

    @Test
    fun testSrs_Api() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/http-api-service",
            (MimiTokenScopes.Srs.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/websocket-api-service",
            (MimiTokenScopes.Srs.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Srs.Api.Http,
                MimiTokenScopes.Srs.Api.WebSocket
            ),
            MimiTokenScopes.Srs.Api.getContainingScopes()
        )
    }

    @Test
    fun testSrs_SpeakerGroups() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/speaker_groups.r",
            (MimiTokenScopes.Srs.SpeakerGroups.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/speaker_groups.w",
            (MimiTokenScopes.Srs.SpeakerGroups.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Srs.SpeakerGroups.Read,
                MimiTokenScopes.Srs.SpeakerGroups.Write
            ),
            MimiTokenScopes.Srs.SpeakerGroups.getContainingScopes()
        )
    }

    @Test
    fun testSrs_Speakers() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/speakers.r",
            (MimiTokenScopes.Srs.Speakers.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/speakers.w",
            (MimiTokenScopes.Srs.Speakers.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Srs.Speakers.Read,
                MimiTokenScopes.Srs.Speakers.Write
            ),
            MimiTokenScopes.Srs.Speakers.getContainingScopes()
        )
    }

    @Test
    fun testSrs_Speeches() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/speeches.r",
            (MimiTokenScopes.Srs.Speeches.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/speeches.w",
            (MimiTokenScopes.Srs.Speeches.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Srs.Speeches.Read,
                MimiTokenScopes.Srs.Speeches.Write
            ),
            MimiTokenScopes.Srs.Speeches.getContainingScopes()
        )
    }

    @Test
    fun testSrs_Trainers() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/trainers.r",
            (MimiTokenScopes.Srs.Trainers.Read as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/srs/trainers.w",
            (MimiTokenScopes.Srs.Trainers.Write as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Srs.Trainers.Read,
                MimiTokenScopes.Srs.Trainers.Write
            ),
            MimiTokenScopes.Srs.Trainers.getContainingScopes()
        )
    }

    @Test
    fun testSrs() {
        assertEquals(
            setOf(
                MimiTokenScopes.Srs.Api.Http,
                MimiTokenScopes.Srs.Api.WebSocket,
                MimiTokenScopes.Srs.SpeakerGroups.Read,
                MimiTokenScopes.Srs.SpeakerGroups.Write,
                MimiTokenScopes.Srs.Speakers.Read,
                MimiTokenScopes.Srs.Speakers.Write,
                MimiTokenScopes.Srs.Speeches.Read,
                MimiTokenScopes.Srs.Speeches.Write,
                MimiTokenScopes.Srs.Trainers.Read,
                MimiTokenScopes.Srs.Trainers.Write
            ),
            MimiTokenScopes.Srs.getContainingScopes()
        )
    }

    @Test
    fun testTts() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/tts/http-api-service",
            (MimiTokenScopes.Tts.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(MimiTokenScopes.Tts.Api.Http),
            MimiTokenScopes.Tts.Api.getContainingScopes()
        )
        assertEquals(
            setOf(MimiTokenScopes.Tts.Api.Http),
            MimiTokenScopes.Tts.getContainingScopes()
        )
    }

    @Test
    fun testTra() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/tra/http-api-service",
            (MimiTokenScopes.Tra.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(MimiTokenScopes.Tra.Api.Http),
            MimiTokenScopes.Tra.Api.getContainingScopes()
        )
        assertEquals(
            setOf(MimiTokenScopes.Tra.Api.Http),
            MimiTokenScopes.Tra.getContainingScopes()
        )
    }

    @Test
    fun testAir() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/air/http-api-service",
            (MimiTokenScopes.Air.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/air/websocket-api-service",
            (MimiTokenScopes.Air.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Air.Api.Http,
                MimiTokenScopes.Air.Api.WebSocket
            ),
            MimiTokenScopes.Air.Api.getContainingScopes()
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Air.Api.Http,
                MimiTokenScopes.Air.Api.WebSocket
            ),
            MimiTokenScopes.Air.getContainingScopes()
        )
    }

    @Test
    fun testEmo() {
        assertEquals(
            "https://apis.mimi.fd.ai/auth/emo-categorical/http-api-service",
            (MimiTokenScopes.Emo.Api.Http as MimiTokenSingleScope).value
        )
        assertEquals(
            "https://apis.mimi.fd.ai/auth/emo-categorical/websocket-api-service",
            (MimiTokenScopes.Emo.Api.WebSocket as MimiTokenSingleScope).value
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Emo.Api.Http,
                MimiTokenScopes.Emo.Api.WebSocket
            ),
            MimiTokenScopes.Emo.Api.getContainingScopes()
        )
        assertEquals(
            setOf(
                MimiTokenScopes.Emo.Api.Http,
                MimiTokenScopes.Emo.Api.WebSocket
            ),
            MimiTokenScopes.Emo.getContainingScopes()
        )
    }

    @Test
    fun testGetContainingScopes() {
        assertEquals(
            setOf(
                MimiTokenScopes.Asr.Api.Http,
                MimiTokenScopes.Asr.Api.WebSocket,
                MimiTokenScopes.Lid.Api.Http,
                MimiTokenScopes.Lid.Api.WebSocket,
            ),
            setOf(MimiTokenScopes.Asr, MimiTokenScopes.Lid).getContainingScopes()
        )
    }

    @Test
    fun testGetContainingScopes_Duplicated() {
        assertEquals(
            setOf(
                MimiTokenScopes.Asr.Api.Http,
                MimiTokenScopes.Asr.Api.WebSocket
            ),
            setOf(MimiTokenScopes.Asr.Api, MimiTokenScopes.Asr.Api.Http).getContainingScopes()
        )
    }
}
