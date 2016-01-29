# java-juman-knp
====

京都大学発の形態素解析器jumanと構文解析器knpをjavaから実行して結果をJSONオブジェクトで取得するライブラリです。
knpはまだ未実装なので出来たらv1.0にします。

JSONの生成はjacksonを利用しています。

JUMAN
http://nlp.ist.i.kyoto-u.ac.jp/index.php?cmd=read&page=JUMAN&alias%5B%5D=%E6%97%A5%E6%9C%AC%E8%AA%9E%E5%BD%A2%E6%85%8B%E7%B4%A0%E8%A7%A3%E6%9E%90%E3%82%B7%E3%82%B9%E3%83%86%E3%83%A0JUMAN

KNP
http://nlp.ist.i.kyoto-u.ac.jp/?KNP

## required

- juman >=7
- knp
- java >=8
- gradle >=2.3

## How to use

まだjumanしか実装していないのでknp実装次第、bintrayのmavenリポジトリを利用できるようにするつもりです。
今はjarを作って使ってください。
本プロジェクトをローカルにclone後、gradleかpomを使ってビルドできます。

    // Jumanクラス
    Juman juman = new Juman();
    ObjectNode result = juman.parse("本日は雪が降っています。");

    // Jumanクラスでコマンドパスを指定してインスタンス化
    Juman juman = new Juman("/path/to/sh","/path/to/juman");

## juman取得結果について

JUMANの結果取得JSONフォーマット

    {
        "result":"OK/ERROR",
        "message":"エラーの場合のエラーメッセージ",
        "morphemes": [ // 形態素の情報配列。要素はオブジェクト
            {
                "signage": "計算",                    // 表記
                "reading": "けいさん",                 // 読み
                "prototype": "計算",                  // 原型
                "part": "名詞",                       // 品詞
                "part_number": "6",                  // 品詞番号
                "part_detail": "サ変名詞",             // 品詞細分類
                "part_detail_number": "2",           // 品詞細分類番号
                "conjugated_form": "*",              // 活用形
                "conjugated_form_number": "0",       // 活用形番号
                "conjugated_form_type": "*",         // 活用型
                "conjugated_form_type_number": "0",  // 活用型番号
                "labels": [                          // 単要素 意味情報の配列
                    "補文ト"
                ], 
                "meanings": {                        // ペア要素 意味情報のマップ
                    "カテゴリ": "抽象物", 
                    "代表表記": "計算/けいさん"
                }, 
            },
            ...(形態素の数分繰り返し)...
        ]

JUMANの結果取得JSONのサンプル

    本システムは，計算機による日本語の解析の研究を目指す多くの研究者に共通に使える形態素解析ツールを提供するために開発されました。

    {
        "message": "", 
        "morphemes": [
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [
                    "内容語"
                ], 
                "meanings": {
                    "代表表記": "本/ほん"
                }, 
                "part": "接頭辞", 
                "part_detail": "名詞接頭辞", 
                "part_detail_number": "1", 
                "part_number": "13", 
                "prototype": "本", 
                "reading": "ほん", 
                "signage": "本"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "ドメイン": "科学・技術", 
                    "代表表記": "システム/しすてむ"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "システム", 
                "reading": "しすてむ", 
                "signage": "システム"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "副助詞", 
                "part_detail_number": "2", 
                "part_number": "9", 
                "prototype": "は", 
                "reading": "は", 
                "signage": "は"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "特殊", 
                "part_detail": "読点", 
                "part_detail_number": "2", 
                "part_number": "1", 
                "prototype": "，", 
                "reading": "，", 
                "signage": "，"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [
                    "補文ト"
                ], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "代表表記": "計算/けいさん"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "計算", 
                "reading": "けいさん", 
                "signage": "計算"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "人工物-その他", 
                    "代表表記": "機/き", 
                    "漢字読み": "音"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "機", 
                "reading": "き", 
                "signage": "機"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [
                    "連語"
                ], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "格助詞", 
                "part_detail_number": "1", 
                "part_number": "9", 
                "prototype": "に", 
                "reading": "に", 
                "signage": "に"
            }, 
            {
                "conjugated_form": "基本形", 
                "conjugated_form_number": "2", 
                "conjugated_form_type": "子音動詞ラ行", 
                "conjugated_form_type_number": "10", 
                "labels": [
                    "連語"
                ], 
                "meanings": {}, 
                "part": "動詞", 
                "part_detail": "*", 
                "part_detail_number": "0", 
                "part_number": "2", 
                "prototype": "よる", 
                "reading": "よる", 
                "signage": "よる"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "代表表記": "日本/にほん", 
                    "地名": "国"
                }, 
                "part": "名詞", 
                "part_detail": "地名", 
                "part_detail_number": "4", 
                "part_number": "6", 
                "prototype": "日本", 
                "reading": "にほん", 
                "signage": "日本"
            }, 
            {
                "conjugated_form": "0", 
                "conjugated_form_number": "*", 
                "conjugated_form_type": "4", 
                "conjugated_form_type_number": "*", 
                "labels": [
                    "0"
                ], 
                "meanings": {
                    "代表表記": "日本/にほん", 
                    "地名": "国"
                }, 
                "part": "日本", 
                "part_detail": "6", 
                "part_detail_number": "地名", 
                "part_number": "名詞", 
                "prototype": "にっぽん", 
                "reading": "日本", 
                "signage": "@"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "代表表記": "語/ご", 
                    "漢字読み": "音"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "語", 
                "reading": "ご", 
                "signage": "語"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "接続助詞", 
                "part_detail_number": "3", 
                "part_number": "9", 
                "prototype": "の", 
                "reading": "の", 
                "signage": "の"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "ドメイン": "教育・学習;科学・技術", 
                    "代表表記": "解析/かいせき"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "解析", 
                "reading": "かいせき", 
                "signage": "解析"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "接続助詞", 
                "part_detail_number": "3", 
                "part_number": "9", 
                "prototype": "の", 
                "reading": "の", 
                "signage": "の"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "ドメイン": "科学・技術", 
                    "代表表記": "研究/けんきゅう"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "研究", 
                "reading": "けんきゅう", 
                "signage": "研究"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "格助詞", 
                "part_detail_number": "1", 
                "part_number": "9", 
                "prototype": "を", 
                "reading": "を", 
                "signage": "を"
            }, 
            {
                "conjugated_form": "基本形", 
                "conjugated_form_number": "2", 
                "conjugated_form_type": "子音動詞サ行", 
                "conjugated_form_type_number": "5", 
                "labels": [], 
                "meanings": {
                    "代表表記": "目指す/めざす"
                }, 
                "part": "動詞", 
                "part_detail": "*", 
                "part_detail_number": "0", 
                "part_number": "2", 
                "prototype": "目指す", 
                "reading": "めざす", 
                "signage": "目指す"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "代表表記": "多く/おおく", 
                    "形容詞派生": "多い/おおい"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "多く", 
                "reading": "おおく", 
                "signage": "多く"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "接続助詞", 
                "part_detail_number": "3", 
                "part_number": "9", 
                "prototype": "の", 
                "reading": "の", 
                "signage": "の"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "ドメイン": "科学・技術", 
                    "代表表記": "研究/けんきゅう"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "研究", 
                "reading": "けんきゅう", 
                "signage": "研究"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [
                    "内容語"
                ], 
                "meanings": {
                    "カテゴリ": "人", 
                    "代表表記": "者/しゃ"
                }, 
                "part": "接尾辞", 
                "part_detail": "名詞性名詞接尾辞", 
                "part_detail_number": "2", 
                "part_number": "14", 
                "prototype": "者", 
                "reading": "しゃ", 
                "signage": "者"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "格助詞", 
                "part_detail_number": "1", 
                "part_number": "9", 
                "prototype": "に", 
                "reading": "に", 
                "signage": "に"
            }, 
            {
                "conjugated_form": "ダ列基本連用形", 
                "conjugated_form_number": "8", 
                "conjugated_form_type": "ナノ形容詞", 
                "conjugated_form_type_number": "22", 
                "labels": [], 
                "meanings": {
                    "代表表記": "共通だ/きょうつうだ"
                }, 
                "part": "形容詞", 
                "part_detail": "*", 
                "part_detail_number": "0", 
                "part_number": "3", 
                "prototype": "共通だ", 
                "reading": "きょうつうに", 
                "signage": "共通に"
            }, 
            {
                "conjugated_form": "基本形", 
                "conjugated_form_number": "2", 
                "conjugated_form_type": "母音動詞", 
                "conjugated_form_type_number": "1", 
                "labels": [], 
                "meanings": {
                    "代表表記": "使える/つかえる", 
                    "可能動詞": "使う/つかう"
                }, 
                "part": "動詞", 
                "part_detail": "*", 
                "part_detail_number": "0", 
                "part_number": "2", 
                "prototype": "使える", 
                "reading": "つかえる", 
                "signage": "使える"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "形・模様", 
                    "代表表記": "形態/けいたい"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "形態", 
                "reading": "けいたい", 
                "signage": "形態"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "代表表記": "素/そ", 
                    "漢字読み": "音"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "素", 
                "reading": "そ", 
                "signage": "素"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "ドメイン": "教育・学習;科学・技術", 
                    "代表表記": "解析/かいせき"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "解析", 
                "reading": "かいせき", 
                "signage": "解析"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "人工物-その他", 
                    "代表表記": "ツール/つーる"
                }, 
                "part": "名詞", 
                "part_detail": "普通名詞", 
                "part_detail_number": "1", 
                "part_number": "6", 
                "prototype": "ツール", 
                "reading": "つーる", 
                "signage": "ツール"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "格助詞", 
                "part_detail_number": "1", 
                "part_number": "9", 
                "prototype": "を", 
                "reading": "を", 
                "signage": "を"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "代表表記": "提供/ていきょう"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "提供", 
                "reading": "ていきょう", 
                "signage": "提供"
            }, 
            {
                "conjugated_form": "基本形", 
                "conjugated_form_number": "2", 
                "conjugated_form_type": "サ変動詞", 
                "conjugated_form_type_number": "16", 
                "labels": [
                    "付属動詞候補（基本）"
                ], 
                "meanings": {
                    "代表表記": "する/する", 
                    "自他動詞:自": ":成る/なる"
                }, 
                "part": "動詞", 
                "part_detail": "*", 
                "part_detail_number": "0", 
                "part_number": "2", 
                "prototype": "する", 
                "reading": "する", 
                "signage": "する"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "代表表記": "為/ため"
                }, 
                "part": "名詞", 
                "part_detail": "副詞的名詞", 
                "part_detail_number": "9", 
                "part_number": "6", 
                "prototype": "ため", 
                "reading": "ため", 
                "signage": "ため"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "助詞", 
                "part_detail": "格助詞", 
                "part_detail_number": "1", 
                "part_number": "9", 
                "prototype": "に", 
                "reading": "に", 
                "signage": "に"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {
                    "カテゴリ": "抽象物", 
                    "ドメイン": "科学・技術", 
                    "代表表記": "開発/かいはつ"
                }, 
                "part": "名詞", 
                "part_detail": "サ変名詞", 
                "part_detail_number": "2", 
                "part_number": "6", 
                "prototype": "開発", 
                "reading": "かいはつ", 
                "signage": "開発"
            }, 
            {
                "conjugated_form": "未然形", 
                "conjugated_form_number": "3", 
                "conjugated_form_type": "サ変動詞", 
                "conjugated_form_type_number": "16", 
                "labels": [
                    "付属動詞候補（基本）"
                ], 
                "meanings": {
                    "代表表記": "する/する", 
                    "自他動詞:自": ":成る/なる"
                }, 
                "part": "動詞", 
                "part_detail": "*", 
                "part_detail_number": "0", 
                "part_number": "2", 
                "prototype": "する", 
                "reading": "さ", 
                "signage": "さ"
            }, 
            {
                "conjugated_form": "基本連用形", 
                "conjugated_form_number": "8", 
                "conjugated_form_type": "母音動詞", 
                "conjugated_form_type_number": "1", 
                "labels": [], 
                "meanings": {
                    "代表表記": "れる/れる"
                }, 
                "part": "接尾辞", 
                "part_detail": "動詞性接尾辞", 
                "part_detail_number": "7", 
                "part_number": "14", 
                "prototype": "れる", 
                "reading": "れ", 
                "signage": "れ"
            }, 
            {
                "conjugated_form": "タ形", 
                "conjugated_form_number": "7", 
                "conjugated_form_type": "動詞性接尾辞ます型", 
                "conjugated_form_type_number": "31", 
                "labels": [], 
                "meanings": {
                    "代表表記": "ます/ます"
                }, 
                "part": "接尾辞", 
                "part_detail": "動詞性接尾辞", 
                "part_detail_number": "7", 
                "part_number": "14", 
                "prototype": "ます", 
                "reading": "ました", 
                "signage": "ました"
            }, 
            {
                "conjugated_form": "*", 
                "conjugated_form_number": "0", 
                "conjugated_form_type": "*", 
                "conjugated_form_type_number": "0", 
                "labels": [], 
                "meanings": {}, 
                "part": "特殊", 
                "part_detail": "句点", 
                "part_detail_number": "1", 
                "part_number": "1", 
                "prototype": "。", 
                "reading": "。", 
                "signage": "。"
            }
        ], 
        "result": "OK"
    }

## knpの取得結果について

