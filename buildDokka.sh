#!/bin/bash

WORK_SPACE=$(pwd)

OUTPUT_DIR="$WORK_SPACE/dokka"
mkdir -p "$OUTPUT_DIR"

echo "utils:dokkaGfm を実行中..."
"$WORK_SPACE"/gradlew :utils:dokkaGfm
UTILS_EXIT_CODE=$?
if [ $UTILS_EXIT_CODE -ne 0 ]; then
  echo "utils:dokkaGfm が失敗しました (エラーコード: $UTILS_EXIT_CODE)。処理を中断します。"
  exit $UTILS_EXIT_CODE
fi

echo "service:dokkaGfmMultiModule を実行中..."
"$WORK_SPACE"/gradlew :service:dokkaGfmMultiModule
SERVICE_EXIT_CODE=$?
if [ $SERVICE_EXIT_CODE -ne 0 ]; then
  echo "service:dokkaGfmMultiModule が失敗しました (エラーコード: $SERVICE_EXIT_CODE)。処理を中断します。"
  exit $SERVICE_EXIT_CODE
fi

echo "engine:dokkaGfmMultiModule を実行中..."
"$WORK_SPACE"/gradlew :engine:dokkaGfmMultiModule
ENGINE_EXIT_CODE=$?
if [ $ENGINE_EXIT_CODE -ne 0 ]; then
  echo "engine:dokkaGfmMultiModule が失敗しました (エラーコード: $ENGINE_EXIT_CODE)。処理を中断します。"
  exit $ENGINE_EXIT_CODE
fi

echo "すべての Dokka タスクが成功しました。生成物をコピーします。"

# utils の生成物をコピー
UTILS_OUTPUT="$WORK_SPACE/utils/build/dokka/gfm"
if [ -d "$UTILS_OUTPUT" ]; then
  echo "utils の生成物を $OUTPUT_DIR/utils へコピー"
  cp -r "$UTILS_OUTPUT" "$OUTPUT_DIR/utils"
else
  echo "警告: utils の出力ディレクトリ ($UTILS_OUTPUT) が見つかりません。"
fi

# engine の生成物をコピー
ENGINE_OUTPUT="$WORK_SPACE/engine/build/dokka/gfmMultiModule"
if [ -d "$ENGINE_OUTPUT" ]; then
  echo "engine の生成物を $OUTPUT_DIR/engine へコピー"
  cp -r "$ENGINE_OUTPUT" "$OUTPUT_DIR/engine"
else
  echo "警告: engine の出力ディレクトリ ($ENGINE_OUTPUT) が見つかりません。"
fi

# service の生成物をコピー
SERVICE_OUTPUT="$WORK_SPACE/service/build/dokka/gfmMultiModule"
if [ -d "$SERVICE_OUTPUT" ]; then
  echo "service の生成物を $OUTPUT_DIR/service へコピー"
  cp -r "$SERVICE_OUTPUT" "$OUTPUT_DIR/service"
else
  echo "警告: service の出力ディレクトリ ($SERVICE_OUTPUT) が見つかりません。"
fi

# dokka 直下に index.md を作成
echo "各モジュールの index.md へのリンクを作成します。"
cat <<EOT > "$OUTPUT_DIR"/index.md
# mimi.client.kotlin 

- [Utils](utils/index.md)
- [Service](service/index.md)
- [Engine](engine/index.md)
EOT

echo "ドキュメント生成とコピーが完了しました。最終成果物は $OUTPUT_DIR にあります。"

exit 0