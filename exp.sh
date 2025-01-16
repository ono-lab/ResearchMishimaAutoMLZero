#!/bin/bash

# シェルスクリプトを実行するディレクトリに移動
cd "$(dirname "$0")"

# メインクラスの指定
MAIN_CLASS="experiments.TExperimentRunner"

# コマンドライン引数を取得
ARGS="$@"

# Mavenを使ってプロジェクトをコンパイルし、指定されたメインクラスを実行
mvn clean compile exec:java \
    -Dexec.mainClass="$MAIN_CLASS" \
    -Dexec.args="$ARGS"