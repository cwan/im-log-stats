im-log-stats
============

[イントラマート](http://www.intra-mart.jp/) のログを解析し、統計レポートを作成するツールです。

**生成されるレポートのサンプル**
- [report_sample.html](/downloads/cwan/im-log-stats/report_sample.html)
- [report_sample.csv](/downloads/cwan/im-log-stats/report_sample.html)

## 1. 動作要件

**解析対象のログ**

- intra-mart WebPlatform/AppFramework Ver.6.0, 6,1, 7.0, 7,1, 7,2
- intra-mart Accel Platform 2012 Autumn

**ツール実行環境**

- JRE 1.5 以上
- [Apache Ant](http://ant.apache.org/) 1.7 以上

## 2. 使用方法

1. ダウンロードしたモジュールを任意のフォルダに解凍する。

1. build.xml を編集する。（[後述](#build_xml)）

1. コマンドプロンプトを開き、1.を解凍したフォルダ（build.xmlがあるとところ）に移動する。

    cd C:\work\im-log-stats

1. Ant を実行する。（%ANT_HOME%\bin にパスを通しておく）

    ant -f build.xml
    
1. build.xml に記述したパスにレポートが生成される。

**備考**

Ant 実行中に OutOfMemoryError が発生する場合は、Ant オプションで最大ヒープサイズを設定してください。（ヒープ使用量は、ログが50万行でも 100MB ぐらいです）

    SET ANT_OPTS=-Xmx512m

## 3. <a name="build_xml"></a>build.xml の設定

build.xml を編集し、解析対象のログファイルと生成するレポートの設定を行います。

記述例は、[build_sample.xml](/downloads/cwan/im-log-stats/build_sample.xml) を参照してください。

### 3.1. 共通設定

カスタムタスクのクラスパスと定義を記述します。

    <!-- 実行時クラスパス -->
    <path id="execute.classpath">
        <fileset dir="${basedir}/lib">
            <include name="*.jar" />
        </fileset>
    </path>
    
    <!-- カスタムタスク、ネスト要素の定義 -->
    <taskdef
        resource="net/mikaboshi/intra_mart/tools/log_stats/ant/taskdef.properties"
        classpathref="execute.classpath"
        loaderref="loader1" />
    
    <typedef
        resource="net/mikaboshi/intra_mart/tools/log_stats/ant/typedef.properties"
        classpathref="execute.classpath"
        loaderref="loader1" />

### 3.2. カスタムタスク imLogStats



