# mimi.client.kotlin

mimi(R) API Client for Kotlin

## 概要

mimi(R) API Client for Kotlin は Kotlin または Java を使用しているアプリケーションから [mimi(R) API](https://mimi.readme.io/) を簡単に利用するためのライブラリです。  
本ライブラリの詳細な仕様は、[Dokka](./dokka/index.md) を参照ください。

## 対応環境

- JVM (Androidを含む)
- Linux on x86-64 (Kotlin/Native)
- macOS on Apple Silicon (Kotlin/Native)

> [!IMPORTANT]
> 現在、GitHub PackagesにPublishしているものは、`JVM` に限られます。

## 導入手順

導入や実装の詳細については、 [THINKLET向けサンプル](./sample-thinklet/README.md) もしくは [CLIアプリケーション向けサンプル](./sample/README.md) を確認してください。

## チュートリアル

このセクションでは、mimi(R) API Client for Kotlin を使用して、mimi(R) APIを利用するための基本的な設定方法を説明します。
このライブラリは、HTTPまたはWebSocketプロトコルを介して mimi(R) APIにアクセスするための `ネットワークエンジン` と、各 mimi サービスにアクセスするための `サービス` から構成されます。

### 1. ネットワークエンジンの設定

#### a. Ktorベースのエンジン

Ktorベースのエンジンを使用する場合、以下のように Ktor の `HttpClient` を構成した上で、mimi用の `MimiNetworkEngine.Factory` を作成します。

```kotlin
val httpClient = HttpClient(CIO) {
    // WebSocket APIを使用する場合
    install(WebSockets)
}
val engineFactory = MimiNetworkEngine.Ktor(httpClient)
```

Ktorでは複数のHTTPエンジンが利用可能なため、[公式ドキュメント](https://ktor.io/docs/client-engines.html)を参考し、必要に応じて依存関係の追加や設定を行ってください。

#### b. OkHttpベースのエンジン

OkHttpベースのエンジンを使用する場合、以下のように OkHttp の `OkHttpClient` を構成した上で、mimi用の `MimiNetworkEngine.Factory` を作成します。

```kotlin
val okhttpClient: OkHttpClient = OkHttpClient.Builder().build()
val engineFactory = MimiNetworkEngine.OkHttp(okhttpClient)
```

### 2. サービスの設定

mimi(R) API Client for Kotlin では、各 mimi サービスにアクセスするためのサービスクラスが提供されています。
サービスクラスの利用には、先ほど作成した `MimiNetworkEngine.Factory` や、サービスに応じて追加の引数を渡してインスタンスを生成します。

詳細は [THINKLET向けサンプル](./sample-thinklet/README.md) もしくは [CLIアプリケーション向けサンプル](./sample/README.md) 、もしくは [mimi(R) API Client for Kotlin の API ドキュメント](https://example.com) を参照してください。

## ライセンス
このプロジェクトはMITライセンスの下で公開されています。詳細は[LICENSE](./LICENSE)ファイルを参照してください。
