# ShareUmbrella
傘をシェアするためのwebサービスです．<br><br>
リンクは[こちら](https://server-1479597618.ap-northeast-1.elb.amazonaws.com/shareUmbrella/web/index.html)<br>
[2015/12からhttpでウェブカメラを使用できなくなってしまったため(chromeの場合)，<br>自己署名証明書を使ってhttps接続するよう設定しています<br>
正式に発行されている証明書では無いため警告が出ますが，利用する場合はそのまま接続してください．]<br><br>
概要はこちら [ビジネス概要](images/ShareUmbrellaPoster1.png)　[webアプリ概要](images/ShareUmbrellaPoster2.png)<br>

## 使い方
1. トップページへアクセス
2. ログインする(ID:testUser, Pass:testPass)
3. ヘッダのsearch->surroundingをクリック
4. 現在地付近の傘置き場が表示されるので，傘置き場まで移動する
5. ShareUmbrellaの借りるページを開いて，傘についているQRコードを読み取ると傘を借りることができます
6. 傘を利用し終わったら近くの傘置き場まで行き，傘に付いているQRコードを読み取ると傘を返すことができます．

動作確認用QRコード: [free](https://github.com/SoichiSumi/Ingredients/blob/master/qr50.gif)，[premium](https://github.com/SoichiSumi/Ingredients/blob/master/QR60Premium.gif)<br>
レンタル/返却　に成功しましたと出るまで，QRコードをかざし続けてください．<br>

## 工夫点
* シンプルなUI
* 傘置き場検索の際には入力文字列から探したい場所を推測(GooglePlacesを利用)
* なるべくMVCモデルに従った実装
* セッション管理
* サニタイズ
* 傘を借りて，返すことまでを考慮したルートナビゲーション(GoogleMaps,GooglePlacesを利用)

## 実装
**クライアント**<br>
言語：JavaScript，HTML<br>
使用ライブラリ：Bootstrap，QRコードライブラリ，GoogleMapsAPI，GooglePlacesAPI<br>

**サーバサイドアプリケーション**<br>
言語：Java<br>
使用フレームワーク：Jersey<br>

**その他**<br>
サーバ：Tomcat<br>
DBMS：MongoDB<br>

DBの仕様は[Wiki](https://github.com/SoichiSumi/ShareUmbrella/wiki)へ
