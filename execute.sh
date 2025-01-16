#!/bin/bash

# シェルスクリプトを実行するためのディレクトリに移動
cd "$(dirname "$0")"

# メインクラスをコマンドライン引数から取得
if [ -z "$1" ]; then
    echo "Error: No main class provided."
    echo "Usage: $0 package.name.MainClass"
    exit 1
fi

MAIN_CLASS=$1

# Mavenを使ってプロジェクトをコンパイルし、指定されたメインクラスを実行
mvn clean compile exec:java -Dexec.mainClass="$MAIN_CLASS"