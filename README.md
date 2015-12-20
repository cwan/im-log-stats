im-log-stats
============

[イントラマート](http://www.intra-mart.jp/) のログ（リクエストログ、画面遷移ログ、例外ログ）を解析し、統計レポートを作成するツールです。

**生成されるレポートのサンプル**
- [report_sample.html](https://googledrive.com/host/0B5qjh_6P-8VAOFhpbWFLWnZpcFU/report_sample.html) : HTML（シンプル）
- [report_sample_visualize.html](https://googledrive.com/host/0B5qjh_6P-8VAOFhpbWFLWnZpcFU/report_sample_visualize.html) : HTML（グラフ付き）
- [report_sample.csv](https://googledrive.com/host/0B5qjh_6P-8VAOFhpbWFLWnZpcFU/report_sample.csv) : CSV

**ダウンロード**

- [master](https://github.com/cwan/im-log-stats/archive/master.zip) : SNAPSHOT
- [releases](https://github.com/cwan/im-log-stats/releases) : 安定板

## 1. 動作要件

**解析対象のログ**

- intra-mart WebPlatform/AppFramework Ver.6.0, 6,1, 7.0, 7,1, 7,2
- intra-mart Accel Platform 8.0.0 - 8.0.11

**ツール実行環境**

- JRE 1.5 以上
- [Apache Ant](http://ant.apache.org/) 1.7 以上

## 2. 使用方法

1. ダウンロードしたモジュールを任意のフォルダに解凍する。

1. build.xml を編集する。（[後述](#build_xml)）

1. コマンドプロンプトを開き、1.を解凍したフォルダ（build.xmlがあるところ）に移動する。

    cd C:\work\im-log-stats

1. Ant を実行する。（%ANT_HOME%\bin にパスを通しておく）

    ant -f build.xml

1. build.xml に記述したパスにレポートが生成される。

**備考**

Ant 実行中に OutOfMemoryError が発生する場合は、Ant オプションで最大ヒープサイズを設定してください。（ヒープ使用量は、ログが50万行でも 100MB ぐらいです）

    SET ANT_OPTS=-Xmx512m

## 3. <a name="build_xml"></a>build.xml の設定

build.xml を編集し、解析対象のログファイルと生成するレポートの設定を行います。

記述例は、[build_sample.xml](https://github.com/cwan/im-log-stats/blob/master/build_sample.xml) を参照してください。

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
version | intra-mart のバージョン ( 6.0, 6.1, 7.0, 7.1, 7.2, 8.0, 8,0.0, 8,0.1, 8,0.2, 8,0.3, 8,0.4, 8,0.5, 8,0.6, 8,0.7, 8,0.8, 8,0.9, 8,0.10, 8,0.11 ) | No | 8.0
delay | ログファイルのパース中に挿入される遅延時間（ミリ秒）。<br/>（リクエストログ・画面遷移ログは100件毎、例外ログは10ファイル毎）<br/>運用サーバ上で実行する場合、25～100程度を設定すると、時間はかかるがサーバに負荷をかけにくくなる。 | No | 0

**注意**
- version の設定が間違っていると、レポートの生成に失敗する場合があります。（バージョンによってログレイアウトパターンが異なるため）
- version を "8.0" と指定した場合、現時点の最新アップデートとみなします。
- iAP 2014 Spring (8.0.7) 以降、バーチャルテナント対応により、標準のログフォーマットが変更されています。iAP 2013 Winter 以前のログを解析する場合は、version を第3桁まで指定してください。([#25](https://github.com/cwan/im-log-stats/issues/25))

### 3.3. imLogStats のネスト要素 parser

ログファイルのパースに関する設定です。

#### parser の属性

名称 | 説明 | 必須 | デフォルト
:--|:--|:--|:--
charset | ログファイルの文字コード<br/>（Ver.6.x では IM のシステム文字コード、Ver.7.x, 8.x ではIM実行環境の JVM の文字コード） | No | Ant 実行環境の JVM デフォルト文字コード
begin | 開始日時（これより前のログは切り捨てる） | No | 下限なし
end | 終了日時（これより後のログは切り捨てる） | No | 上限なし
tenantId | テナントID（一致しないログは切り捨てる） | No | 指定なし（全てのテナントのログを収集する）
exceptionGroupingType | 例外のグルーピング方法<br/>cause : 根本原因となっているの Caused by の1行目でグルーピング<br/>first-line : 例外メッセージ + スタックトレース1行目でグルーピング | No | cause
errorLimit | パーサエラーの上限。この回数を超えてエラーが発生した場合、処理を打ち切る（レポートは生成されない） | No | 1000
truncateRequestUrl | true の場合、リクエストURLからスキーム、ホスト、ポートを除去したもので集計する | No | false

begin および end では、以下のいずれかの形式で日時を指定してください。  
時刻を省略した場合、begin では 00:00、end では 24:00 で補完されます。

- yyyy/MM/dd HH:mm
- yyyy-MM-dd HH:mm
- yyyy/MM/dd
- yyyy-MM-dd
- today
- today - n (*n = 1, 2, 3 ...*)

`today` を設定した場合、本日の日付が設定されます。  
`today - n` を指定した場合、本日の n 日前の日付が設定されます。  
例えば、前週のログを集計したい場合は、以下のように設定してください。

    <parser
        charset="UTF-8"
        begin="today - 7"
        end="today - 1" />

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


### 3.4. imLogStats のネスト要素 report

生成されるログ統計レポートの設定です。

#### report の属性

名称 | 説明 | 必須 | デフォルト
:--|:--|:--|:--
type | レポートファイルの形式<br/>html : HTML<br/>visualize : グラフ付きHTML (※1)<br/>csv : CSV (Comma Separated Values)<br/>tsv (Tab Separated Values)<br/>template : 独自テンプレート（@templateFile, @templateCharset で指定）  | No | html
span | 期間別統計の集計間隔（分）| No | 5
sessionTimeout | IM環境のセッションタイムアウト時間（分）<br/>（有効セッション数の計算に利用する） | No | 10
requestPageTimeRankSize | リクエスト処理時間総合ランクの行数 | No | 20
requestUrlRankSize | リクエストURL別・処理時間合計ランクの行数 | No | 20
sessionRankSize | セッション別・処理時間合計ランクの行数 | No | 20
name | レポートの名称（タイトル等に使用する） | No | intra-mart ログ統計レポート
signature | レポートの署名 | No | （なし）
output | レポートの出力先ファイルパス(※2) | No | カレントディレクトリ/report_yyyyMMdd-HHmmss (年月日-時分秒)
charset | レポートファイルの文字コード | No | Ant 実行環境の JVM デフォルト文字コード
templateFile | カスタムテンプレートのファイルパス<br/>（@type="template" 以外の場合は無視される） | No | デフォルトのHTMLテンプレート
templateCharset | カスタムテンプレートファイルの文字コード<br/>（@type="template" 以外の場合および @template を指定していない場合は無視される） | No | Ant実行環境のJVMデフォルト文字コード
requestPageTimeRankThresholdMillis | 0以上の値を指定した場合、処理時間がこの値（ミリ秒）以上のリクエストを全てリクエスト処理時間総合ランクに表示する<br/>（この属性を設定した場合、requestPageTimeRankSize の設定は無視される） | No | false
jsspPath | true を指定した場合、レポートに jssp, jssps, jssprpc のページパスが表示される<br/>（ (2f) を /、(5f) を _ に変換 ） | No | false
maxConcurrentRequest | true を指定した場合、期間別統計レポートに最大同時リクエスト数が表示される | No | true
visualizeBaseUrl | グラフ生成に必要な静的コンテンツ（*.js, *.css, *.png）が配置されたURLのベース部分 (※1)(v1.0.15以降)<br/>例: `https://googledrive.com/host/0B5qjh_6P-8VAOFhpbWFLWnZpcFU/visualize` | No | visualize

(※1)
 `visualize` 形式の場合、レポート HTML ファイルと同じフォルダに、`visualize` フォルダを配置するか、visualizeBaseUrlを指定してください（配下の *.js, *.css, *.png ファイルが必要です）。

(※2)
 `output` 属性において、{ } で囲んだ内部に日時フォーマットを指定し、現在日時で動的にファイル名をつけることが可能です。  
 フォーマットの形式は、[java.text.SimpleDateFormat](http://docs.oracle.com/javase/jp/6/api/java/text/SimpleDateFormat.html) と同じです。

    例:
      <report output="report_{yyyyMMdd}.html" />
      ? 現在が2012年10月12日ならば、report_20121012.html が生成されます。

### 3.5. imLogStats のネスト要素 requestLogFiles

解析対象のリクエストログファイルを指定します（複数記述可能）。  
形式は、Ant 標準の [`<fileset>`](http://ant.apache.org/manual/Types/fileset.html) と同じです。  
省略した場合、リクエストログは解析しません。

### 3.6. imLogStats のネスト要素 transitionLogFiles

解析対象の画面遷移ログファイルを指定します（複数記述可能）。  
形式は、Ant 標準の [`<fileset>`](http://ant.apache.org/manual/Types/fileset.html) と同じです。  
省略した場合、画面遷移ログは解析しません。

### 3.7. imLogStats のネスト要素 exceptionLogFiles

解析対象の例外ログファイルを指定します（複数記述可能）。  
形式は、Ant 標準の [`<fileset>`](http://ant.apache.org/manual/Types/fileset.html) と同じです。  
省略した場合、例外ログは解析しません。

## 4. レポートの見方

生成される統計レポートの見方について説明します。  
時間・リクエストURL・セッション（ユーザ）・テナントの4つの軸で統計をとり、大まかなシステムの負荷の傾向が分かるようになっています。

### 4.1. 期間別統計

各ログを時間単位で集計しています。  
この統計からは、負荷の高い時間帯を知ることができます。 また、リクエスト数の多い時間帯にリクエスト処理時間の平均値などが極端に大きくなっている場合は、ピーク時の負荷にシステムが耐えられないことを示しています。

ログ項目 | 説明 | 集計元
:--|:--|:--
集計期間 | ログを集計した開始-終了日時 | リクエストログ、画面遷移ログ、例外ログ
リクエスト数合計 | この期間に受信したHTTPリクエスト数の合計 | リクエストログ
リクエスト処理時間 [ms] | アプリケーションサーバがリクエストを受信してからレスポンスを返すまでの時間（ミリ秒） ([*](#response_time)) | リクエストログ
- 合計 | リクエスト処理時間の合計 | リクエストログ
- 平均値 | リクエスト処理時間の平均値 | リクエストログ
- 最小値 | リクエスト処理時間の最小値 | リクエストログ
- 中央値 | リクエスト処理時間の中央値（処理時間でソートしたとき、中央に位置する値） | リクエストログ
- 90% Line | リクエスト処理時間の90%がこの値以下であるということ | リクエストログ
- 標準偏差 | リクエスト処理時間の標準偏差（値のばらつき具合を表す指標） | リクエストログ
ユニークユーザ数 | この期間内にリクエストを送信した重複を除いたユーザの数 | 画面遷移ログ
ユニークセッション数 | この期間内にリクエストを送信した重複を除いたセッションの数 | リクエストログ、画面遷移ログ
有効セッション数 | この期間において、有効であると見なされるセッションの数<br/>（この期間以前に生成され、まだログアウト・タイムアウトしていないセッション） | リクエストログ、画面遷移ログ
例外数合計 | この期間内に発生した例外の数 | 例外ログ
画面遷移数合計 | この期間内の期間内に発生した画面遷移（REQUEST/FORWARD/INCLUDE）の合計 | 画面遷移ログ
画面遷移例外数合計 | この期間内の画面遷移ログにおいて検出されたエラーの合計 | 画面遷移ログ
最大同時リクエスト数 | この期間内において、同時実行されていたリクエスト数の最大値(※1)  | リクエストログ、画面遷移ログ

(※1)  最大同時リクエスト数から、必要なスレッド数がわかります。ただし、ログが出力されるのはレスポンスが返るときであるため、レスポンスが返らず、実行中のままのスレッドがあるかどうかまでは分かりません。

### 4.2. リクエスト処理時間総合ランク

リクエスト処理時間が大きいものから順にN件抽出したものです。  
この統計からは、最も性能の悪いリクエストURL（機能）が分かります。

report パラメータの requestPageTimeRankThresholdMillis 属性を設定した場合、上位N件ではなく、処理時間が requestPageTimeRankThresholdMillis（ミリ秒）以上のリクエストを全て抽出します。

ログ項目 | 説明 | 集計元
:--|:--|:--
リクエストURL<br/>（遷移先画面パス） | リクエスト先のURLまたは遷移先画面パス ([*](#request_url)) | リクエストログ
JSSPページパス | jssp, jssps, jssprpc のページパス<br/>（ parser パラメータの @jsspPath を "true" に設定した場合のみ表示される） | リクエストログ
処理時間 | アプリケーションサーバがリクエストを受信してからレスポンスを返すまでの時間（ミリ秒） ([*](#response_time)) | リクエストログ
ログ出力日時 | ログが出力された日時（INの場合はリクエスト受信直後、OUTの場合はレスポンス送信前） | リクエストログ
セッションID | このリクエストを送信したセッション | リクエストログ
ユーザID | このリクエストを送信したユーザ（未ログイン状態の場合は、不明の場合がある） | 画面遷移ログ

### 4.3. リクエストURL（遷移先画面パス）別・処理時間合計ランク

リクエストURL（または遷移先画面パス）でグルーピングし、リクエスト処理時間の合計が大きいものから順にN件抽出したものです。  
この統計からは、システムに負荷を与えているリクエストURL（機能）が分かります。また、処理時間が全体的に大きい場合は、サーバリソースが不足していると考えられます。

ログ項目 | 説明 | 集計元
:--|:--|:--
リクエストURL<br/>（遷移先画面パス） | リクエスト先のURLまたは遷移先画面パス ([*](#request_url)) | リクエストログ、画面遷移ログ
JSSPページパス | jssp, jssps, jssprpc のページパス<br/>（ parser パラメータの @jsspPath を "true" に設定した場合のみ表示される） | リクエストログ
リクエスト回数 | このURL（または遷移先画面パス）に対するリクエスト受信回数の合計 | リクエストログ、画面遷移ログ
リクエスト処理時間 [ms] | アプリケーションサーバがリクエストを受信してからレスポンスを返すまでの時間（ミリ秒）([*](#response_time)) | リクエストログ、画面遷移ログ
リクエスト回数% | 全リクエストの合計に対する、このURLの回数が占める比率 | リクエストログ、画面遷移ログ
処理時間% | 全リクエストの合計に対する、このURLの処理時間が占める比率 | リクエストログ、画面遷移ログ

### 4.4. セッション別・処理時間合計ランク

セッションIDでグルーピングし、リクエスト処理時間の合計が大きいものから順にN件抽出したものです。  
一部のユーザの処理時間が極端に大きい場合は、何らかの条件（データ）に依存して性能が悪くなる傾向があると考えられます。

ログ項目 | 説明 | 集計元
:--|:--|:--
セッションID | Servletコンテナが発行したセッションIDです。(※1)  | リクエストログ、画面遷移ログ
ユーザID | このセッションに紐付くユーザ（未ログイン状態の場合は、不明の場合がある） | 画面遷移ログ
リクエスト回数 | のセッションからのリクエスト受信回数の合計 | リクエストログ
処理時間 | このセッションからのリクエスト処理時間の合計（ミリ秒）([*](#response_time)) | リクエストログ
リクエスト回数% | 全リクエストの合計に対する、このセッションの回数が占める比率 | リクエストログ
処理時間% | 全リクエストの合計に対する、このセッションの処理時間が占める比率 | リクエストログ
初回アクセス日時 | このセッションが最初にリクエストを送信した日時 | リクエストログ、画面遷移ログ
最終アクセス日時 | このセッションが最後にリクエストを送信した日時 | リクエストログ、画面遷移ログ

(※1)  セッションIDを含まないリクエストは、「-」となります。このランクでは、セッションIDを含まないリクエストは、異なるユーザ（リモートホスト）からのものでも全て1つにまとめて集計しています。

### 4.5. テナント別統計

[テナント別統計は、Ver.1.0.16 で追加されました。](https://github.com/cwan/im-log-stats/issues/25)

各ログをテナント単位で集計しています。（例外ログは、テナントIDを含まないため対象外です）  
この統計からは、負荷の高いテナントを知ることができます。

ログ項目 | 説明 | 集計元
:--|:--|:--
テナントID | ログを出力したテナント（システム管理者画面等、テナントを特定できないログはブランクになる） | リクエストログ、画面遷移ログ
リクエスト数合計 | このテナントが処理したHTTPリクエスト数の合計 | リクエストログ
リクエスト処理時間 [ms] | アプリケーションサーバがリクエストを受信してからレスポンスを返すまでの時間（ミリ秒） ([*](#response_time)) | リクエストログ
- 合計 | リクエスト処理時間の合計 | リクエストログ
- 平均値 | リクエスト処理時間の平均値 | リクエストログ
- 最小値 | リクエスト処理時間の最小値 | リクエストログ
- 中央値 | リクエスト処理時間の中央値（処理時間でソートしたとき、中央に位置する値） | リクエストログ
- 90% Line | リクエスト処理時間の90%がこの値以下であるということ | リクエストログ
- 標準偏差 | リクエスト処理時間の標準偏差（値のばらつき具合を表す指標） | リクエストログ
ユニークユーザ数 | このテナントにリクエストを送信した重複を除いたユーザの数 | 画面遷移ログ
ユニークセッション数 | このテナントにリクエストを送信した重複を除いたセッションの数 | リクエストログ、画面遷移ログ
画面遷移数合計 | この期間内の期間内に発生した画面遷移（REQUEST/FORWARD/INCLUDE）の合計 | 画面遷移ログ
画面遷移例外数合計 | このテナントの画面遷移ログにおいて検出されたエラーの合計 | 画面遷移ログ
最大同時リクエスト数 | このテナントにおいて、同時実行されていたリクエスト数の最大値(※1)  | リクエストログ、画面遷移ログ

(※1)  最大同時リクエスト数から、必要なスレッド数がわかります。ただし、ログが出力されるのはレスポンスが返るときであるため、レスポンスが返らず、実行中のままのスレッドがあるかどうかまでは分かりません。

### 4.6. 例外

例外ログに出力された例外を、根本原因となっている Caused by の1行目、またはメッセージ・スタックトレース1行目の内容でグルーピングし、発生回数の多い順に並べています。  
例外ログが大量にある場合、どんな種類のエラーが出ているか俯瞰することができます。

### 4.7. 補足

#### <a name="request_url"></a>リクエストURL

リクエストURL（または遷移先画面パス）からは、以下のようにして対応する機能が分かります。

URL パターン | 説明
:--|:--
`/xxx-yyy.service` | im-JavaEE 開発モデルプログラムのURLです。<br/>`WEF-INF/classes/**/service-config-xxx.xml` ファイルから、`<service-id>yyy</service-id>` となっている箇所の ServiceController がリクエストの入り口になります。
`/*.jssp`<br/>`/*.jssps`<br/>`/*.jssprpc` | im-スクリプト開発モデルプログラムの URL です。<br/>(2f)→/, (5f)→_ に変換すると、Resource Service (pages) のソースが分かります。
`/dt` | ポータルの URL です。構成されるポートレット個別のログはありません。
`/services/*` | Web サービスの URL です。
`/HTTPActionEventListener` | バッチプログラムまたは外部ソフトウェア連携プログラムのURLです。
`*.do` | Struts プログラムの URL です。
`*/` | SAStruts プログラムの URL です。
その他 | web.xml のマッピングを確認してください。

リクエストログファイルが1つも指定されなかった場合、画面遷移ログ（遷移タイプ = REQUEST のみ）からリクエスト情報の統計が取得されます。  
ただし、Ver.7.x, 8.x 標準のフィルタ設定では、`*.jssprpc`、`/services/*`、`/HTTPActionEventListener` 等に TransitionLogFilter が設定されていないため、これらの情報は統計から除外されます。  
（Ver.6.x 標準では、TransitionLogFilter にすべての URL がマッピングされています。）

#### <a name="response_time"></a>リクエスト処理時間

リクエスト処理時間は、intra-mart の RequestLogFilter で計測されたリクエストを受け付けてからレスポンスを返すまでかかった時間であり、サーバの処理時間と見なすことができます。  
そのため、静的コンテンツファイル等が含まれない場合があります。 また、Resin のセッション永続化処理などは RequestLogFilter の外で実行されるため、このような処理時間は含まれません。

ユーザの体感速度は、サーバの処理時間だけではなく、ネットワークの遅延時間、ブラウザ上のレンダリングや JavaScript の実行時間なども含めたものとなりますので、 リクエスト処理時間だけで性能の評価を下すことはできません。

## 5. カスタムテンプレート

`report/@templateFile` にテンプレートファイルを指定することによって、レポートの形式を自由に定義することが可能です。  
テンプレートファイルは、[FreeMarker](http://freemarker.sourceforge.net/) の形式で作成してください。

テンプレートに渡されるパラメータを以下に示します。

### 5.1. 共通

パラメータ名 | 型 | 説明
:--|:--|:--
charset | java.lang.String | レポートの文字コード
reportName | java.lang.String | レポート名
signature | java.lang.String | 署名
generatedTime | java.util.Date | レポート生成日時
totalPageTime | long | 合計処理時間（ミリ秒）
totalRequestCount | int | 合計リクエスト回数
projectVersion | java.lang.String | im-log-stats のバージョン
projectUrl | java.lang.String | im-log-stats プロジェクトのURL

### 5.2. パーサパラメータ

パラメータ名 | 型 | 説明
:--|:--|:--
parserParameter.charset | java.lang.String | ログファイルの文字コード
parserParameter.requestLogLayout | java.lang.String | リクエストログのレイアウト
parserParameter.transitionLogLayout | java.lang.String | 画面遷移ログのレイアウト
parserParameter.begin | java.util.Date | 開始日時（未設定の場合はnull）
parserParameter.end | java.util.Date | 終了日時（未設定の場合はnull）
parserParameter.tenantId | java.lang.String | テナントID（未設定の場合はnull）
parserParameter.version.name | java.lang.String | バージョン
parserParameter.exceptionGroupingByCause | boolean | 発端の Caused by の1行目で例外のグルーピングをするならば true、スタックトレースの1行目でグルーピングをするならば false
parserParameter.truncateUrl | boolean | リクエストURLからスキーム、ホスト、ポートを除去するならば true、しないならば false

### 5.3. レポートパラメータ

パラメータ名 | 型 | 説明
:--|:--|:--
reportParameter.span | long | 期間別統計の間隔（分）
reportParameter.sessionTimeout | int | セッションタイムアウト時間（分）
reportParameter.requestPageTimeRankSize | int | リクエスト処理時間総合ランクの出力件数
reportParameter.requestUrlRankSize | int | リクエストURL（遷移先画面パス）別・処理時間合計ランクの出力件数
reportParameter.sessionRankSize | int | セッションランクの出力件数
reportParameter.version.name | java.lang.String | バージョン
reportParameter.requestPageTimeRankThresholdMillis | long | リクエスト処理時間総合ランクを閾値で抽出する場合のミリ秒
reportParameter.jsspPath | boolean | リクエスト処理時間総合ランク、リクエストURL別・処理時間合計ランクに、JSSPページパスの列を表示するかどうか
reportParameter.maxConcurrentRequest | boolean | 期間別統計に、最大同時リクエスト数の列を表示するかどうか
repoerParameter.visualizeBaseUrl | String | visualizeレポートの静的コンテンツベースURL（v1.0.15以降）

### 5.4. 期間別統計

パラメータ名 | 型 | 説明
:--|:--|:--
timeSpanStat.span | long | 期間別統計の間隔（分）
timeSpanStat.list | java.util.List | 期間別統計リスト
- startDate | java.util.Date | 期間開始日時
- endDate | java.util.Date | 期間終了日時
- requestCount | int | リクエスト回数
- pageTimeSum | long | リクエスト処理時間合計
- pageTimeAverage | long | リクエスト処理時間平均値
- pageTimeMin | long | リクエスト処理時間最小値
- pageTimeMedian | long | リクエスト処理時間中央値
- pageTimeP90 | long | リクエスト処理時間90% Line
- pageTimeMax | long | リクエスト処理時間最大値
- pageTimeStandardDeviation | long | リクエスト処理時間標準偏差
- uniqueUserCount | int | ユニークユーザ数
- uniqueSessionCount | int | ユニークセッション数
- activeSessionCount | int | 有効セッション数
- exceptionCount | int | 例外発生数
- transitionCount | int | 画面遷移数
- transitionExceptionCount | int | 画面遷移例外発生数
- maxConcurrentRequestCount | int | 最大同時リクエスト数

### 5.5. リクエスト処理時間総合ランク

パラメータ名 | 型 | 説明
:--|:--|:--
requestPageTimeRank.size | int | リクエスト処理時間総合ランクの出力件数
requestPageTimeRank.requestCount | int | リクエスト数合計
requestPageTimeRank.list | java.util.List | リクエスト処理時間総合ランクリスト
- requestUrl | java.lang.String | リクエストURL（または遷移先画面パス）
- jsspPath | java.lang.String | JSSPページパス（ parser パラメータの @jsspPath を "true" に設定した場合のみ設定される）
- requestPageTime | long | リクエスト処理時間
- date | java.util.Date | ログ出力日時
- sessionId | java.lang.String | セッションID
- userId | java.lang.String | ユーザID

### 5.6. リクエストURL（画面遷移パス）別・処理時間合計ランク

パラメータ名 | 型 | 説明
:--|:--|:--
requestUrlRank.size | int | リクエストURL別・処理時間合計ランクの出力件数
requestUrlRank.total | int | リクエストURL（または遷移先画面パス）数合計
requestUrlRank.list | java.util.List | リクエストURL別・処理時間合計ランクリスト
- url | java.lang.String | リクエストURL（または遷移先画面パス）
- jsspPath | java.lang.String | JSSPページパス（ parser パラメータの @jsspPath を "true" に設定した場合のみ設定される）
- count | int | リクエスト回数
- pageTimeSum | long | リクエスト処理時間合計
- pageTimeAverage | long | リクエスト処理時間平均値
- pageTimeMin | long | リクエスト処理時間最小値
- pageTimeMedian | long | リクエスト処理時間中央値
- pageTimeP90 | long | リクエスト処理時間90% Line
- pageTimeMax | long | リクエスト処理時間最大値
- pageTimeStandardDeviation | long | リクエスト処理時間標準偏差
- countRate | double | リクエスト回数%
- pageTimeRate | double | 処理時間%

### 5.7. セッション別・処理時間合計ランク

パラメータ名 | 型 | 説明
:--|:--|:--
sessionRank.size | int | セッション別・処理時間合計ランクの出力件数
sessionRank.total | int | セッション数数合計
sessionRank | java.util.List | セッション別・処理時間合計ランクリスト
- sessionId | java.lang.String | セッションID
- userId | java.lang.String | ユーザID
- count | int | リクエスト回数
- pageTimeSum | long | リクエスト処理時間合計
- pageTimeAverage | long | リクエスト処理時間平均値
- pageTimeMin | long | リクエスト処理時間最小値
- pageTimeMedian | long | リクエスト処理時間中央値
- pageTimeP90 | long | リクエスト処理時間90% Line
- pageTimeMax | long | リクエスト処理時間最大値
- pageTimeStandardDeviation | long | リクエスト処理時間標準偏差
- countRate | double | リクエスト回数%
- pageTimeRate | double | 処理時間%
- firstAccessTime | java.util.Date | 初回アクセス日時
- lastAccessTime | java.util.Date | 最終アクセス日時

### 5.8. テナント別統計

パラメータ名 | 型 | 説明
:--|:--|:--
tenantStat.list | java.util.List | テナント別統計リスト
- tenantId | java.lang.String | テナントID
- requestCount | int | リクエスト回数
- pageTimeSum | long | リクエスト処理時間合計
- pageTimeAverage | long | リクエスト処理時間平均値
- pageTimeMin | long | リクエスト処理時間最小値
- pageTimeMedian | long | リクエスト処理時間中央値
- pageTimeP90 | long | リクエスト処理時間90% Line
- pageTimeMax | long | リクエスト処理時間最大値
- pageTimeStandardDeviation | long | リクエスト処理時間標準偏差
- uniqueUserCount | int | ユニークユーザ数
- uniqueSessionCount | int | ユニークセッション数
- transitionCount | int | 画面遷移数
- transitionExceptionCount | int | 画面遷移例外発生数
- maxConcurrentRequestCount | int | 最大同時リクエスト数

### 5.9. 例外

パラメータ名 | 型 | 説明
:--|:--|:--
exceptionList | java.util.List | 例外リスト
- level | enum | ログレベル
- message | java.lang.String | ログメッセージ
- firstLineOfStackTrace | java.lang.String | スタックトレースの1行目
- count | int | 回数

### 5.10. 解析対象ログファイル

パラメータ名 | 型 | 説明
:--|:--|:--
logFiles.requestLogFiles | java.util.Collection<java.io.File> | リクエストログ
logFiles.transitionLogFiles | java.util.Collection<java.io.File> | 画面遷移ログ
logFiles.exceptionLogFiles | java.util.Collection<java.io.File> | 例外ログ
logFiles.transitionLogOnly | boolean | 画面遷移ログからリクエスト情報を取得する（リクエストログ無し）ならば true


## 6. ライセンス

[Apache License, Version 2.0](https://github.com/cwan/im-log-stats/blob/master/LICENSE.txt)

## 7. 更新履歴

### Ver.1.0.17 (2015-08-05)

 - [#26 visualizeレポートの「処理時間最大値」の割合表示が出ない不具合を修正](https://github.com/cwan/im-log-stats/issues/26)
 - [#27 2015 Summer (v8.0.11) に対応](https://github.com/cwan/im-log-stats/issues/27)

### Ver.1.0.16 (2014-09-23)
- [#24 report/templateCharset省略時にNullPointerExceptionが発生する不具合を修正](https://github.com/cwan/im-log-stats/issues/24)
- [#25 バーチャルテナント対応](https://github.com/cwan/im-log-stats/issues/25)

### Ver.1.0.15 (2014-01-14)
- [#9 visualizeレポートで、期間別統計の順序が入れ替わる不具合を修正。](https://github.com/cwan/im-log-stats/issues/9)
- [#15 IEで期間別統計グラフの時刻が縦にならない不具合を修正。](https://github.com/cwan/im-log-stats/issues/15)
- [#17 期間別統計グラフの縦軸を割合で表示できるようにした。](https://github.com/cwan/im-log-stats/issues/17)
- [#19 中央値、90%Lineの計算方法を修正。](https://github.com/cwan/im-log-stats/issues/19)
- [#21 visualizeレポートで使用しているjs等をWebから取得できるようにした。](https://github.com/cwan/im-log-stats/issues/21)

### Ver.1.0.14 (2013-05-11)
- [#12 visualizeテンプレートのCSS文法エラーを修正。](https://github.com/cwan/im-log-stats/issues/12)
- [#13 レポート出力の性能改善](https://github.com/cwan/im-log-stats/issues/13)
- [#14 セッション別・処理時間合計ランクに、セッションID無しのリクエストを含めるようにした。](https://github.com/cwan/im-log-stats/issues/14)
- [#15 レポート生成時のログを見やすくした。](https://github.com/cwan/im-log-stats/issues/15)

### Ver.1.0.13 (2013-04-07)
- [#10 期間別統計に最大同時リクエスト数を追加した。](https://github.com/cwan/im-log-stats/issues/10)
- [#11 期間別統計グラフに、画面遷移例外数合計が表示できなかった不具合を修正。](https://github.com/cwan/im-log-stats/issues/11)

### Ver.1.0.12 (2013-02-05)
- ~~[#9 visualizeレポートで、期間別統計の順序が入れ替わる不具合を修正。](https://github.com/cwan/im-log-stats/issues/9)~~

### Ver.1.0.11 (2013-01-29)
- [#4 期間別統計グラフの改善](https://github.com/cwan/im-log-stats/issues/4)
- [#5 リクエスト処理時間総合ランクを、処理時間の閾値で抽出できるようにする。](https://github.com/cwan/im-log-stats/issues/5)
- [#6 jssp ページのソースパスを表示できるようにする。](https://github.com/cwan/im-log-stats/issues/6)
- [#7 レポートのリクエストURLから、スキーム、ホスト、ポートを除去できるようにする。](https://github.com/cwan/im-log-stats/issues/7)
- [#8 im-log-stats のバージョンをレポートに表示する。](https://github.com/cwan/im-log-stats/issues/8)

### Ver.1.0.10 (2012-10-22)
- [#2 レポートにグラフを表示できるようにする。](https://github.com/cwan/im-log-stats/issues/2)
- [#3 パース時のエラーが多い場合に、処理を打ち切るようにする。](https://github.com/cwan/im-log-stats/issues/3)

### Ver.1.0.9 (2012-10-12)
- [#1 パラメータの parser/@begin, parser/@end, report/@output を動的に設定できるようにした。](https://github.com/cwan/im-log-stats/issues/1)

### Ver.1.0.8 (2012-10-08)
- 一般公開
