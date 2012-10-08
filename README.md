im-log-stats
============

[イントラマート](http://www.intra-mart.jp/) のログ（リクエストログ、画面遷移ログ、例外ログ）を解析し、統計レポートを作成するツールです。

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

intra-mart のログファイル（リクエストログ、画面遷移ログ、例外ログ）を解析し、統計レポートを生成します。

#### imLogStats の属性

名称 | 説明 | 必須 | デフォルト
:--|:--|:--|:--
version | intra-mart のバージョン ( 6.0, 6.1, 7.0, 7.1, 7.2, 8.0 ) | No | 7.2
delay | ログファイルのパース中に挿入される遅延時間（ミリ秒）。<br/>（リクエストログ・画面遷移ログは100件毎、例外ログは10ファイル毎）<br/>運用サーバ上で実行する場合、25～100程度を設定すると、時間はかかるがサーバに負荷をかけにくくなる。 | No | 0

*注意*
- version の設定が間違っていると、レポートの生成に失敗する場合があります。（バージョンによってログレイアウトパターンが異なるため）

### 3.3. imLogStats のネスト要素 parser

ログファイルのパースに関する設定です。

#### parser の属性

名称 | 説明 | 必須 | デフォルト
:--|:--|:--|:--
charset | ログファイルの文字コード<br/>（Ver.6.x では IM のシステム文字コード、Ver.7.x, 7.x ではIM実行環境の JVM の文字コード） | No | Ant 実行環境の JVM デフォルト文字コード
begin | 開始日時（これより前のログは切り捨てる） | No | 下限なし
end | 終了日時（これより後のログは切り捨てる） | No | 上限なし
exceptionGroupingType | 例外のグルーピング方法<br/>cause : 根本原因となっているの Caused by の1行目でグルーピング<br/>first-line : 例外メッセージ + スタックトレース1行目でグルーピング | No | cause

begin および end では、以下のいずれかの形式で日時を指定してください。 時刻を省略した場合、begin では 00:00、end では 24:00 で補完されます。

- yyyy/MM/dd HH:mm
- yyyy-MM-dd HH:mm
- yyyy/MM/dd
- yyyy-MM-dd

#### parser のネスト要素 requestLogLayout

リクエストログのレイアウトパターンをタグボディに設定します。（前後の空白はトリミングされます）

Ver.6.x では、imart.xml の以下の値を設定して下さい。

    /intra-mart/platform/service/application/log/request/file/@format

Ver.7.x, 8.x では、im_logger_request.xml の以下の値を設定してください。

    /included/appender[@name="REQUEST_FILE"]/layout/pattern

省略した場合は、imLogStats/@version に指定したバージョンの標準レイアウトが適用されます。

#### parser のネスト要素 transitionLogLayout

画面遷移ログのレイアウトパターンをタグボディに設定します。（前後の空白はトリミングされます）

Ver.6.x では、imart.xml の以下の値を設定して下さい。

    /intra-mart/platform/service/application/log/transition/file/@format

Ver.7.x, 8.x では、im_logger_transition.xml の以下の値を設定してください。

    /included/appender[@name="TRANSITION_FILE]/layout/pattern

省略した場合は、imLogStats/@version に指定したバージョンの標準レイアウトが適用されます。





