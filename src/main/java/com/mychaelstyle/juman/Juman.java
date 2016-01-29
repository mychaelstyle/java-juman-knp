package com.mychaelstyle.juman;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 */

/**
 * JUMANのJavaラッパークラスです。
 * 
 * http://nlp.ist.i.kyoto-u.ac.jp/index.php?cmd=read&page=JUMAN&alias%5B%5D=%E6%97%A5%E6%9C%AC%E8%AA%9E%E5%BD%A2%E6%85%8B%E7%B4%A0%E8%A7%A3%E6%9E%90%E3%82%B7%E3%82%B9%E3%83%86%E3%83%A0JUMAN
 * 
 * 本クラスを利用するにはシステムにjumanがインストールされている必要があります。
 * 
 * @author masanori nakashima
 */
public class Juman {
    /**
     * デフォルトのshellのパス文字列 /bin/sh
     */
    public static final String PATH_SHELL = "/bin/sh";
    /**
     * デフォルトのjumanコマンドのパス /usr/local/bin/juman
     */
    public static final String PATH_JUMAN = "/usr/local/bin/juman";
    /** JSON要素名: 結果 */
    public static final String L_RESULT = "result";
    /** JSON要素名: メッセージ */
    public static final String L_MESSAGE = "message";
    /** JSON要素名: 形態素情報配列 */
    public static final String L_MORPHEMES = "morphemes";
    /**
     * 形態素情報のJSONラベル定義クラス
     * @author masanori nakashima
     */
    public static final class Morpheme {
        /** JSON要素名: 表記 */
        public static final String L_SIGNAGE = "signage";
        /** JSON要素名: 読み */
        public static final String L_READING = "reading";
        /** JSON要素名: 原型 */
        public static final String L_PROTOTYPE = "prototype";
        /** JSON要素名: 品詞 */
        public static final String L_PART = "part";
        /** JSON要素名: 品詞番号 */
        public static final String L_PART_NUMBER = "part_number";
        /** JSON要素名: 品詞細分類 */
        public static final String L_PART_DETAIL = "part_detail";
        /** JSON要素名: 品詞細分類番号 */
        public static final String L_PART_DETAIL_NUMBER ="part_detail_number";
        /** JSON要素名: 活用型 */
        public static final String L_CONJUGATED_FORM_TYPE = "conjugated_form_type";
        /** JSON要素名: 活用型番号 */
        public static final String L_CONJUGATED_FORM_TYPE_NUMBER = "conjugated_form_type_number";
        /** JSON要素名: 活用形 */
        public static final String L_CONJUGATED_FORM = "conjugated_form";
        /** JSON要素名: 活用形番号 */
        public static final String L_CONJUGATED_FORM_NUMBER = "conjugated_form_number";
        /** JSON要素名: 意味情報マップ */
        public static final String L_MEANINGS = "meanings";
        /** JSON要素名: 意味情報ラベル配列 */
        public static final String L_LABELS = "labels";
    }
    /**
     * 結果の値 定義クラス（定数定義のみ）
     * @author masanori nakashima
     */
    public static final class Result {
        /** JSON値: result OK */
        public static final String OK = "OK";
        /** JSON値: result ERROR */
        public static final String ERROR = "ERROR";
    }

    /**
     * 形態素解析結果値に対するラベル
     */
    public static final String[] ELEMENT_LABELS = new String[]{
            Morpheme.L_SIGNAGE,Morpheme.L_READING,Morpheme.L_PROTOTYPE,
            Morpheme.L_PART,Morpheme.L_PART_NUMBER,
            Morpheme.L_PART_DETAIL,Morpheme.L_PART_DETAIL_NUMBER,
            Morpheme.L_CONJUGATED_FORM_TYPE,Morpheme.L_CONJUGATED_FORM_TYPE_NUMBER,
            Morpheme.L_CONJUGATED_FORM,Morpheme.L_CONJUGATED_FORM_NUMBER
    };
 
    /**
     * shコマンドのパス。デフォルトは/bin/sh
     */
    private String pathShell = PATH_SHELL;
    /**
     * jumanコマンドのパス。デフォルトは/usr/local/bin/juman
     */
    private String pathJuman = PATH_JUMAN;

    /**
     * コンストラクタ
     * 
     * sh,jumanコマンドはそれぞれデフォルトパスで実行します。
     */
    public Juman(){
        super();
    }
    /**
     * コンストラクタ
     * @param pathShell shコマンドへのパス
     * @param pathJuman jumanコマンドへのパス
     */
    public Juman(String pathShell,String pathJuman){
        super();
        this.pathShell = pathShell;
        this.pathJuman = pathJuman;
    }
    /**
     * JUMANによるパースを実行する
     * 
     * @param target 対象文字列
     * @return JacksonのObjectNodeインスタンス
     * @throws IOException IO例外
     * @throws InterruptedException jumanコマンド実行での例外発生時
     */
    public ObjectNode parse(String target) throws IOException, InterruptedException{
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = new ObjectNode(factory);
        ArrayNode morphs = new ArrayNode(factory);
        if(null==pathShell || pathShell.isEmpty()) pathShell = PATH_SHELL;
        if(null==pathJuman || pathJuman.isEmpty()) pathJuman = PATH_JUMAN;
        String cmd = "echo \""+target+"\" | "+this.pathJuman+" -e2";

        String[] cmdarray = {pathShell, "-c", cmd};
        Process process = Runtime.getRuntime().exec(cmdarray);
        process.waitFor();

        InputStream is = null;
        BufferedReader br = null;
        StringBuffer errorBuf = new StringBuffer();
        // handle errors
        try {
            is = process.getErrorStream();
            br = new BufferedReader(new InputStreamReader(is));
            for (;;) {
                String line = br.readLine();
                if (line == null) break;
                errorBuf.append(line).append("\n");
            }
            if(errorBuf.length()>0){
                node.put(L_RESULT, Result.ERROR);
                node.put(L_MESSAGE,errorBuf.toString());
                return node;
            }
        } finally {
            br.close();
            is.close();
        }
        // get results
        try {
            is = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            for (;;) {
                String line = br.readLine();
                if (line == null || "EOS".equalsIgnoreCase(line)) break;
                ObjectNode morpheme = this.parseLine(line);
                morphs.add(morpheme);
            }
        }finally{
            br.close();
            is.close();
        }
        node.put(L_RESULT,Result.OK);
        node.put(L_MESSAGE,"");
        node.put(L_MORPHEMES, morphs);
        return node;
    }

    /**
     * Jumanの出力行をJSONにパースします。
     * 
     * @param line JUMANが出力した形態素情報の行文字列
     * @return JacksonのObjectNodeインスタンス
     */
    private ObjectNode parseLine(String line){
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode element = new ObjectNode(factory);
        ObjectNode meanings = new ObjectNode(factory);
        ArrayNode labels = new ArrayNode(factory);
        int start = 0;
        int pos = line.indexOf(" ");
        int counter = 0;
        String elm = line.substring(start, pos).trim();
        element.put(ELEMENT_LABELS[counter], elm);
        counter++;
        while(pos>0 && pos<line.length()){
            start = pos;
            pos = line.indexOf(" ",start+1);
            if(pos<0) pos = line.length();
            elm = line.substring(start, pos).trim();
            if(elm.startsWith("\"")) elm = elm.substring(1);
            if(elm.endsWith("\"")) elm = elm.substring(0, elm.length()-1);
            if(counter<ELEMENT_LABELS.length){
                element.put(ELEMENT_LABELS[counter], elm);
            } else if(!"NIL".equalsIgnoreCase(elm)){
                if(elm.indexOf(":")>0){
                    String l = elm.substring(0,elm.indexOf(":"));
                    String v = elm.substring(elm.indexOf(":")+1);
                    if(v.indexOf(":")>0){
                        l = l+":"+(v.substring(0, v.indexOf(":")));
                        v = v.substring(v.indexOf(":"));
                    }
                    meanings.put(l,v);
                } else {
                    labels.add(elm);
                }
            }
            counter++;
        }
        element.put(Morpheme.L_LABELS, labels);
        element.put(Morpheme.L_MEANINGS,meanings);
        return element;
    }
}
