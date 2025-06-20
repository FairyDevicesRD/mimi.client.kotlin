# THINKLET向けサンプル

> [!NOTE]
> SDK自体は、Androidでも動作しますが、  
> 本サンプルは、THINKLET向けのSDKを利用してますので、THINKLETのみインストール・実行可能です。

## 実行準備 (mimi)

プロジェクトのルートの `local.properties` に下記項目を追加します。

```properties
MIMI_APPLICATION_ID=<your_mimi_application_id>
MIMI_CLIENT_ID=<your_mimi_client_id>
MIMI_CLIENT_SECRET=<your_mimi_client_secret>
```

## 実行準備 (THINKLET向けSDK)
### 1. アクセストークンの取得

GitHub Packages経由でライブラリを取得できるように、 [こちら](https://docs.github.com/ja/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) を参考に個人用アクセストークンを発行してください。

> [!NOTE]
> personal access token (classic) には、`read:packages` スコープの設定が必須です。

### 2. アクセストークンを設定

アクセストークンはクレデンシャルとなるため，バージョン管理されないようにしてください。  
記述したファイルを `.gitignore` に追加することを推奨します。  
このサンプルでは、`local.properties` に追記します。

```local.properties
TOKEN=<github token>
USERNAME=<github username>
```

### 3. `settings.gradle.kts` への設定
GitHub Packagesを参照するための設定を追加します。  
ここでは、手順2においてアクセストークンが `local.properties` に記載済みであるものとします。

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
> - Ktorベースのエンジンを選択した場合、環境ごとに追加のセットアップが必要になる場合があります。詳細は[チュートリアル](../README.md#a-ktorベースのエンジン)を参照してください。

<details><summary>libs.versions.toml</summary><div>

```diff
[versions]
+mimi = "0.0.1"
+
 [libraries]
+mimi-kotlin-engine-ktor = { group = "ai.fd.mimi.client", name = "engine-ktor", version.ref = "mimi" }
+mimi-kotlin-engine-okhttp = { group = "ai.fd.mimi.client", name = "engine-okhttp", version.ref = "mimi" }
+mimi-kotlin-service-token = { group = "ai.fd.mimi.client", name = "service-token", version.ref = "mimi" }
+mimi-kotlin-service-asr = { group = "ai.fd.mimi.client", name = "service-asr", version.ref = "mimi" }
+mimi-kotlin-service-nict-asr = { group = "ai.fd.mimi.client", name = "service-nict-asr", version.ref = "mimi" }
+mimi-kotlin-service-nict-tts = { group = "ai.fd.mimi.client", name = "service-nict-tts", version.ref = "mimi" }
```
</div></details>

## サンプルのデバッグインストール
[導入手順](../README.md#導入手順) の設定完了後、プロジェクトルートで下記を実行してください。  
また、アプリ操作には `scrcpy` を用いてください。

```sh
# デバッグビルド＋インストール
./gradlew :sample-thinklet:installDebug
# アプリ起動
adb shell am start -n ai.fd.mimi.sample.thinklet/.MainActivity
```

### サンプルの実行

| THINKLET                 | THINKLET Cube            |
| ------------------------ | ------------------------ |
| ![wear](./docs/wear.jpg) | ![cube](./docs/cube.jpg) |

| Button | Description                                                                                         |
| :----: | :-------------------------------------------------------------------------------------------------- |
|  (1)   | Httpで音声認識。クリックして録音開始・再度クリックしたら停止して認識します。                        |
|  (2)   | WebSocketで音声認識。クリックして録音開始・再度クリックしたら停止します。リアルタイムに認識します。 |
|  (3)   | (1) or (2) の最新の結果を音声認識結果を音声合成して、再生します。                                   |
