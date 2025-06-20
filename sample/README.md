# CLIアプリケーション向けサンプル
## 実行準備
- プロジェクトのルートの `local.properties` に下記項目を追加します。

```properties
# CLIサンプルのTTSとASRの実行に利用します。
MIMI_TOKEN=********

# CLIサンプルのTOKEN出力の実行に利用します。
MIMI_APPLICATION_ID=<your_mimi_application_id>
MIMI_CLIENT_ID=<your_mimi_client_id>
MIMI_CLIENT_SECRET=<your_mimi_client_secret>

# CLIサンプルのASRのタイプを指定します。nict-v1, nict-v2, asr (デフォルト) を選べます。
ASR_TYPE=nict-v2
```

- `MIMI_TOKEN` には、必要なスコープを設定したアクセストークンを追記します。アクセストークンの発行例を下記に示します。アクセストークンの有効期限(expires_in)は 3600 秒 ( 1 時間) です。

```sh
curl -sS -X POST https://auth.mimi.fd.ai/v2/token \
-F grant_type="https://auth.mimi.fd.ai/grant_type/client_credentials" \
-F client_id="<your_mimi_application_id>:<your_mimi_client_id>" \
-F client_secret="<your_mimi_client_secret>" \
--form-string scope="https://apis.mimi.fd.ai/auth/asr/http-api-service;https://apis.mimi.fd.ai/auth/asr/websocket-api-service;https://apis.mimi.fd.ai/auth/nict-asr/http-api-service;https://apis.mimi.fd.ai/auth/nict-asr/websocket-api-service;https://apis.mimi.fd.ai/auth/nict-tts/http-api-service;https://apis.mimi.fd.ai/auth/nict-tra/http-api-service" | jq -r '.accessToken'
```

## サンプルの実行
プロジェクトルートで実行してください。

```sh
# ASR Http (Ktor)
./gradlew :sample:run -Ptarget=AsrKtorHttp
# ASR WebSocket (Ktor)
./gradlew :sample:run -Ptarget=AsrKtorWebSocket
# ASR Http (OkHttp)
./gradlew :sample:run -Ptarget=AsrOkHttpHttp
# ASR WebSocket (OkHttp)
./gradlew :sample:run -Ptarget=AsrOkHttpWebSocket

# TTS (Ktor) -> generate sample\yyyy-MM-DD-hh-mm-ss.wav
./gradlew :sample:run -Ptarget=TtsKtor
# TTS (OkHttp) -> generate sample\yyyy-MM-DD-hh-mm-ss.wav
./gradlew :sample:run -Ptarget=TtsOkHttp

# Print already revoked token. (Ktor)
./gradlew :sample:run -Ptarget=TokenKtor
```
