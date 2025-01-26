# Auto-ML-Zero

## 環境構築

以下のコマンドで依存関係のインストールする.

```
sh ./setup.sh
```

Java に関するセットアップを実行する.

```
sudo sh -c 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
source /etc/environment
```

`~/.m2/settings.xml`を以下の内容で作成する.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <servers>
    <server>
      <id>github</id>
      <username>{GitHub User Name}</username>
      <password>{GitHub Access Token}</password>
    </server>
  </servers>
</settings>
```
