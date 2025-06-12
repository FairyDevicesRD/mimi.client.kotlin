# mimi.client.kotlin

mimi(R) API Client for Kotlin

## 概要

mimi(R) API Client for Kotlin は Kotlin または Java を使用しているアプリケーションから [mimi(R) API](https://mimi.readme.io/) を簡単に利用するためのライブラリです。  
本ライブラリの詳細な仕様は、[Dokka](./dokka/index.md) を参照ください。

## 対応環境

- JVM (Androidを含む)
- Linux on x86-64 (Kotlin/Native)
- macOS on Apple Silicon (Kotlin/Native)

## 導入手順

実装の詳細については、 [THINKLET向けサンプル](./sample-thinklet/README.md) もしくは [CLIアプリケーション向けサンプル](./sample/README.md) を確認してください。

### 1. アクセストークンの取得
- GitHub Packages経由でライブラリを取得できるように、 [こちら](https://docs.github.com/ja/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) を参考に個人用アクセストークンを発行してください。

> [!NOTE]
> personal access token (classic) には、`read:packages` スコープの設定が必須です。

### 2. アクセストークンを設定
- アクセストークンはクレデンシャルとなるため，バージョン管理されないようにしてください。
- `local.properties` への保存や、記述したファイルを `.gitignore` に追加することを推奨します。

```local.properties
TOKEN=<github token>
USERNAME=<github username>
```

### 3. `settings.gradle.kts` への設定
- GitHub Packagesを参照するための設定を追加します。
- ここでは、手順2においてアクセストークンが `local.properties` に記載済みであるものとします。

```diff
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
+       maven {
+           name = "GitHubPackages"
+           setUrl("https://maven.pkg.github.com/FairyDevicesRD/mimi.client.kotlin")
+           content {
+               includeGroup("ai.fd.mimi.client")
+           }
+           credentials {
+               val properties = java.util.Properties()
+               properties.load(file("local.properties").inputStream())
+               username = properties.getProperty("USERNAME") ?: ""
+               password = properties.getProperty("TOKEN") ?: ""
+           }
+       }
    }
```

### 4. Module(appなど)レベルの `build.gradle.kts` への設定

```diff
 dependencies {
+    val mimi = "0.0.1"

+    // 1. ネットワークエンジンの依存関係を追加
+    // Ktorベースのエンジン
+    implementation("ai.fd.mimi.client:engine-ktor:$mimi")
+    // OkHttpベースのエンジン
+    implementation("ai.fd.mimi.client:engine-okhttp:$mimi")

+    // 2. 使用する mimi のサービス用の依存関係を追加
+    // アクセストークンの発行・管理
+    implementation("ai.fd.mimi.client:service-token:$mimi")
+    // mimi ASR
+    implementation("ai.fd.mimi.client:service-asr:$mimi")
+    // mimi ASR powered by NICT
+    implementation("ai.fd.mimi.client:service-nict-asr:$mimi")
+    // mimi TTS
+    implementation("ai.fd.mimi.client:service-nict-tts:$mimi")
}
```

> [!NOTE]
> - ネットワークエンジンはどちらか片方のみの追加で問題ありません。ご使用になる環境にあわせて選択してください。
> - Ktorベースのエンジンを選択した場合、環境ごとに追加のセットアップが必要になる場合があります。詳細は[チュートリアル](#a-ktorベースのエンジン)を参照してください。

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

TBD
