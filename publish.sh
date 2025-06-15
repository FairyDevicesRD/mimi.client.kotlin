#!/bin/bash

list=(
    ":engine:core"
    ":engine:ktor"
    ":engine:okhttp"
    ":service:token"
    ":service:asr-core"
    ":service:asr"
    ":service:nict-asr"
    ":service:nict-tts"
    ":utils"
)

for task in "${list[@]}"; do
    ./gradlew "$task":publish
    
    if [ $? -ne 0 ]; then
        echo "エラー: $task の実行に失敗しました"
        exit 1
    fi
    
    echo "完了: $task"
    echo "------------------------"
done
