# ShareUmbrella
傘をシェアするためのwebサービスです．<br>
リンクは[こちら](http://urx.blue/qbh3)<br>

https://server-1479597618.ap-northeast-1.elb.amazonaws.com/shareUmbrella/web/index.html
サービス概要は[こちら](images/ShareUmbrellaPoster.png)<br>

## 使い方
1. トップページへアクセス
2. ログインする(ID:testUser, Pass:testPass)
3. ヘッダのsearch->surroundingをクリック
4. 現在地付近の傘置き場が表示されるので，傘置き場まで移動する
5. 傘についているQRコードを読み取ると傘を借りることができます

## 工夫点
* シンプルなUI
* 傘を借りて，返すことまでを考慮したルートナビゲーション(GoogleMaps,GooglePlacesを利用)
* 傘置き場検索の際には入力文字列から探したい場所を推測(GooglePlacesを利用)
* なるべくMVCモデルに従った実装
* セッション管理
* サニタイズ

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
