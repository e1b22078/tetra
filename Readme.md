# マニュアル
### セットアップマニュアル
前提としてJDKがインストールされた仮想サーバーがあるものとします。

まずSSHを用いて仮想サーバーへアクセスします。今回は仮想サーバーを150.89.233.201、ユーザー名はisdev24と仮定します。まずsshコマンドを用いてサーバーへのアクセスを行います。
```
$ ssh isdev24@150.89.233.201
```
`isdev24@150.89.233.XXX's password:`このようなメッセージが表示されたらパスワードを入力してください。次に以下のコマンドを用いてリポジトリのクローンを行います。
```
$ git clone https://github.com/e1b22078/tetra.git
```
以下のコマンドでリポジトリに移動します。
```
$ cd tetra
```
`gradlew`を`bash`を利用して実行します。
```
$ bash ./gradlew
```
プロジェクトを実行します。
```
$ bash ./gradlew bootrun
```

### ユーザマニュアル
* プレイヤー<br>
1.ゲーム選択画面に移動する方法<br>
&emsp;タイトル画面にある「プレイ」ボタンを押すことで、ユーザー名の登録を行うページへ移動します。そのページでユーザ名の登録をした後、「次へ」を押すとゲームを選択する画面に移ります。<br><br>
2.ソロモードについて<br>
&emsp;「ソロモード」のボタンを押すと1人で英単語の勉強をすることができます。最初に問題数を設定します。5問～50問まで選択することができるのでお好みの問題数を選択してください。すべての問題が終了した後何問正解できたかの結果が表示されます。<br><br>
3.クイックマッチについて<br>
&emsp;クイックマッチでは、クイズの対戦をすることができます。「クイックマッチ」ボタンを押すと、ルームに入り待機状態となります。一定の人数になるまでゲームは開始されません。人数が揃うと、「ゲームスタート」ボタンが出るので、ルームの中の1人がボタンを押せば、そのルーム内の全員と英単語クイズで対戦できます。問題は10問出題され、全員が1回のみ答えることができるようになっています。正解すれば10点がもらえ、不正解なら0点となります。また、10秒経つと自動的に次の問題に行き、スコアは0点となります。10問終了後スコアが表示され、1番スコアの高い人の勝利となります。「もどる」ボタンを押すとゲーム選択画面に戻ります。<br><br>
4.ランキングについて<br>
&emsp;タイトル画面にある「ランキング」ボタンを押すと、全プレイヤーのクイックマッチのスコアのランキングを表示スページに移動します。「もどる」リンクを押したらタイトル画面に戻ります。<br><br>
5.ルールについて<br>
&emsp;タイトル画面にある「ルール」のボタンを押すと、ゲームのルールが表示されるページに移動します。「もどる」リンクを押したらタイトル画面に戻ります。<br><br>
* 管理者<br>
&emsp;管理者は、以下の名前とパスワードを入力することで管理者ページに入ることができます。
```
名前:admin
パスワード:isdev
```
管理者は、管理者ページでユーザ情報の削除を行うことができます。削除したいユーザの横にある「削除」ボタンを押すことで実行できます。「ログアウト」リンクを押すとタイトル画面に戻ります。<br><br>
#### 注意
<b>一度ゲームから抜けると次からのプレイ時はスコアがリセットされます。</b>
