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

###

```
def Setup()
  v9[0] = 0.1292114413108869
  v9[1] = -0.2505831388740483
  v9[2] = 0.12620974617641995
  v9[3] = -0.0927704715743739
  v4[0] = 0.25345634793978156
  v4[1] = -0.08198893900983063
  v4[2] = 0.8014245536744797
  v4[3] = 0.010866990780659367
  s3 = 1.4885836334477234
  v1[0] = -0.2070414761633693
  v1[1] = 0.10284085463491824
  v1[2] = 0.2167110168418553
  v1[3] = 0.01683729653416114
  m0[0, 0] = -0.0979146690138854
  m0[0, 1] = 0.04255247850261564
  m0[0, 2] = -0.3568448431607237
  m0[0, 3] = -1.3577788838503895
  m0[1, 0] = 6.035751444417415
  m0[1, 1] = -0.6687873811147038
  m0[1, 2] = -0.16350991278782046
  m0[1, 3] = -0.15973189880280841
  m0[2, 0] = 1.3238446302023488
  m0[2, 1] = -0.2353881355163895
  m0[2, 2] = 2.590326515451852
  m0[2, 3] = 5.842738211926392
  m0[3, 0] = -3.172535391257986
  m0[3, 1] = -2.2259992680585774
  m0[3, 2] = 1.4764090181451432
  m0[3, 3] = 0.314381472387436

def Predict():
  v8 = dot(m0, v0)
  v10 = maximum(v8, v1)
  s1 = dot(v9, v10)

def Learn():
  s2 = s0 - s1
  v5 = s2 * v9
  v7 = heaviside(v8, 1.0)
  v6 = s3 * v7
  v3 = v5 * v6
  v2 = s2 * v4
  m1 = outer(v3, v0)
  v1 = v1 + v2
  m0 = m0 + m1
```
