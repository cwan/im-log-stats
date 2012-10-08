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


### 3.4. imLogStats のネスト要素 report

生成されるログ統計レポートの設定です。

名称 | 説明 | 必須 | デフォルト
:--|:--|:--|:--
type | レポートファイルの形式 ( html, csv, tsv, template ) | No | html
span | 期間別統計の集計間隔（分）| No | 5
sessionTimeout | IM環境のセッションタイムアウト時間（分）<br/>（有効セッション数の計算に利用する） | No | 10
requestPageTimeRankSize | リクエスト処理時間総合ランクの行数 | No | 20
requestUrlRankSize | リクエストURL別・処理時間合計ランクの行数 | No | 20
sessionRankSize | セッション別・処理時間合計ランクの行数 | No | 20
name | レポートの名称（タイトル等に使用する） | No | intra-mart ログ統計レポート
signature | レポートの署名 | No | （なし）
output | レポートの出力先ファイルパス | No | カレントディレクトリ/report_yyyyMMdd-HHmmss (年月日-時分秒)
charset | レポートファイルの文字コード | No | Ant 実行環境の JVM デフォルト文字コード
templateFile | カスタムテンプレートのファイルパス | No | デフォルトのHTMLテンプレート<br/>（@type="template" 以外の場合は無視される）
templateCharset | カスタムテンプレートファイルの文字コード | No | Ant実行環境のJVMデフォルト文字コード<br/>（@type="template" 以外の場合および @template を指定していない場合は無視される）

### 3.5. imLogStats のネスト要素 requestLogFiles

解析対象のリクエストログファイルを指定します（複数記述可能）。  
形式は、Ant 標準の [`<fileset>`](http://ant.apache.org/manual/Types/fileset.html) と同じです。  
省略した場合、リクエストログは解析しません。

### 3.6. imLogStats のネスト要素 transitionLogFile

解析対象の画面遷移ログファイルを指定します（複数記述可能）。  
形式は、Ant 標準の [`<fileset>`](http://ant.apache.org/manual/Types/fileset.html) と同じです。  
省略した場合、画面遷移ログは解析しません。

### 3.7. imLogStats のネスト要素 exceptionLogFiles

解析対象の例外ログファイルを指定します（複数記述可能）。  
形式は、Ant 標準の [`<fileset>`](http://ant.apache.org/manual/Types/fileset.html) と同じです。  
省略した場合、例外ログは解析しません。

## 4. レポートの見方

生成される統計レポートの見方について説明します。  
時間・リクエストURL・セッション（ユーザ）の3つの軸で統計をとり、大まかなシステムの負荷の傾向が分かるようになっています。

### 4.1. 期間別統計

各ログを時間単位で集計しています。  
この統計からは、負荷の高い時間帯を知ることができます。 また、リクエスト数の多い時間帯にリクエスト処理時間の平均値などが極端に大きくなっている場合は、ピーク時の負荷にシステムが耐えられないことを示しています。

ログ項目 | 説明 | 集計元
:--|:--|:--
集計期間 | ログを集計した開始-終了日時 | リクエストログ、画面遷移ログ、例外ログ
リクエスト数合計 | この期間に受信したHTTPリクエスト数の合計 | リクエストログ
リクエスト処理時間 [ms] | Application Runtime がリクエストを受信してからレスポンスを返すまでの時間（ミリ秒） ([*](#response_time)) | リクエストログ
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

### 4.2. リクエスト処理時間総合ランク

クエスト処理時間が大きいものから順にN件抽出したものです。  
この統計からは、最も性能の悪いリクエストURL（機能）が分かります。

ログ項目 | 説明 | 集計元
:--|:--|:--
リクエストURL（遷移先画面パス） | リクエスト先のURLまたは遷移先画面パス ([*](#request_url)) | リクエストログ
処理時間 | Application Runtime がリクエストを受信してからレスポンスを返すまでの時間（ミリ秒） ([*](#response_time)) | リクエストログ
ログ出力日時 | ログが出力された日時（INの場合はリクエスト受信直後、OUTの場合はレスポンス送信前） | リクエストログ
セッションID | このリクエストを送信したセッション | リクエストログ
ユーザID | このリクエストを送信したユーザ（未ログイン状態の場合は、不明の場合がある） | 画面遷移ログ

### 4.3. リクエストURL（画面線パス）別・処理時間合計ランク

リクエストURL（または遷移先画面パス）でグルーピングし、リクエスト処理時間の合計が大きいものから順にN件抽出したものです。  
この統計からは、システムに負荷を与えているリクエストURL（機能）が分かります。また、処理時間が全体的に大きい場合は、サーバリソースが不足していると考えられます。

ログ項目 | 説明 | 集計元
:--|:--|:--
リクエストURL（遷移先画面パス） | リクエスト先のURLまたは遷移先画面パス ([*](#request_url)) | リクエストログ
リクエスト回数 | このURL（または遷移先画面パス）に対するリクエスト受信回数の合計 | リクエストログ
リクエスト処理時間 [ms] | Application Runtimeがリクエストを受信してからレスポンスを返すまでの時間（ミリ秒）
（詳細は、上記参照） (*) | リクエストログ
リクエスト回数% | 全リクエストの合計に対する、このURLの回数が占める比率 | リクエストログ
処理時間% | 全リクエストの合計に対する、このURLの処理時間が占める比率 | リクエストログ

### 4.4. セッション別・処理時間合計ランク

セッションIDでグルーピングし、リクエスト処理時間の合計が大きいものから順にN件抽出したものです。  
一部のユーザの処理時間が極端に大きい場合は、何らかの条件（データ）に依存して性能が悪くなる傾向があると考えられます。

ログ項目 | 説明 | 集計元
:--|:--|:--
セッションID | Servletコンテナが発行したセッションIDです。 | リクエストログ、画面遷移ログ
ユーザID | このセッションに紐付くユーザ（未ログイン状態の場合は、不明の場合がある） | 画面遷移ログ
リクエスト回数 | のセッションからのリクエスト受信回数の合計 | リクエストログ
処理時間 | このセッションからのリクエスト処理時間の合計（ミリ秒）([*](#response_time)) | リクエストログ
リクエスト回数% | 全リクエストの合計に対する、このセッションの回数が占める比率 | リクエストログ
処理時間% | 全リクエストの合計に対する、このセッションの処理時間が占める比率 | リクエストログ
初回アクセス日時 | このセッションが最初にリクエストを送信した日時 | リクエストログ、画面遷移ログ
最終アクセス日時 | このセッションが最後にリクエストを送信した日時 | リクエストログ、画面遷移ログ

### 4.5. 例外

例外ログに出力された例外を、根本原因となっている Caused by の1行目、またはメッセージ・スタックトレース1行目の内容でグルーピングし、発生回数の多い順に並べています。  
例外ログが大量にある場合、どんな種類のエラーが出ているか俯瞰することができます。

### 4.6. 補足

#### <a name="request_url">リクエストURL

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
そのため、RequestLogFilter が設定されていない静的コンテンツファイル等は対象外です。 また、Resin のセッション永続化処理などは RequestLogFilter の外で実行されるため、このような処理時間は含まれません。

ユーザの体感速度は、サーバの処理時間だけではなく、ネットワークの遅延時間、ブラウザ上のレンダリングや JavaScript の実行時間なども含めたものとなりますので、 リクエスト処理時間だけで性能の評価を下すことはできません。

