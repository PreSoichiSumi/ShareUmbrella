# ShareUmbrella
傘をシェアするためのwebサービスです．
サービス概要は[こちら](images/ShareUmbrellaPoster.png)

## 使い方
1. トップページへアクセス
2. ログインする(ID:testUser, Pass:testPass)
3. ヘッダのsearch->surroundingをクリック
4. 現在地付近の傘置き場が表示されるので，傘置き場まで移動する
5. 傘についているQRコードを読み取ると傘を借りることができる

## 工夫点
* シンプルなUI
* 傘を借りて，返すことまでを考慮したルートナビゲーション
* 傘置き場検索の際には入力文字列から探したい場所を推測
* なるべくMVCモデルに従った実装
* セッション管理
* サニタイズ

## 実装
**クライアント**
言語：JavaScript，HTML
使用ライブラリ：Bootstrap，QRコードライブラリ，GoogleMapsAPI，GooglePlacesAPI

**サーバサイドアプリケーション**
言語：Java
使用フレームワーク：Jersey

**その他**
サーバ：Tomcat
DBMS：MongoDB
