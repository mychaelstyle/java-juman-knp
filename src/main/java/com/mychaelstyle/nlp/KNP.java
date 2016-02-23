package com.mychaelstyle.nlp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 */

/**
 * KNPコマンドのJavaラッパークラスです。
 * 
 * http://nlp.ist.i.kyoto-u.ac.jp/index.php?cmd=read&page=JUMAN&alias%5B%5D=%E6%97%A5%E6%9C%AC%E8%AA%9E%E5%BD%A2%E6%85%8B%E7%B4%A0%E8%A7%A3%E6%9E%90%E3%82%B7%E3%82%B9%E3%83%86%E3%83%A0JUMAN
 * 
 * 本クラスを利用するにはシステムにjumanとknpがシステムにインストールされている必要があります。
 * 
 * @author masanori nakashima
 */
public class KNP {
    /**
     * デフォルトのknpコマンドのパス /usr/local/bin/knp
     */
    public static final String PATH_KNP = "/usr/local/bin/knp";

    /**
     * shコマンドのパス。デフォルトは/bin/sh
     */
    private String pathShell = Juman.PATH_SHELL;
    /**
     * jumanコマンドのパス。デフォルトは/usr/local/bin/juman
     */
    private String pathJuman = Juman.PATH_JUMAN;
    /**
     * knpコマンドのパス。デフォルトは/usr/local/bin/knp
     */
    private String pathKnp = PATH_KNP;

    /**
     * コンストラクタ
     */
    public KNP(){
        super();
    }
    /**
     * コンストラクタ
     * @param pathShell shコマンドへのパス文字列
     * @param pathJuman jumanコマンドへのパス文字列
     * @param pathKnp knpコマンドへのパス文字列
     */
    public KNP(String pathShell,String pathJuman,String pathKnp){
        super();
        this.pathShell = pathShell;
        this.pathJuman = pathJuman;
        this.pathKnp = pathKnp;
    }
    /**
     * JUMANによるパースを実行する
     * 
     * @param target
     * @param pathShell
     * @param pathJuman
     * @param pathKnp
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ObjectNode parse(String target) throws IOException, InterruptedException{
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = new ObjectNode(factory);

        if(target.contains("*")){
            target = target.replaceAll("\\*", "＊");
        }
        if(target.contains("+")){
            target = target.replaceAll("\\+", "＋");
        }
        if(target.contains(":")){
            target = target.replaceAll(":", "：");
        }
        if(target.contains("/")){
            target = target.replaceAll("/", "／");
        }
        if(target.contains("<")){
            if(!target.contains("(")){
                target = target.replaceAll("<", "(");
            } else if(!target.contains("[")){
                target = target.replaceAll("<", "[");
            }
        }
        if(target.contains(">")){
            if(!target.contains(")")){
                target = target.replaceAll(">", ")");
            } else if(!target.contains("]")){
                target = target.replaceAll(">", "]");
            }
        }

        if(null==pathShell || pathShell.isEmpty()) pathShell = Juman.PATH_SHELL;
        if(null==pathJuman || pathJuman.isEmpty()) pathJuman = Juman.PATH_JUMAN;
        if(null==pathKnp || pathKnp.isEmpty()) pathKnp = PATH_KNP;
        String cmd = "echo \""+target+"\" | "+ this.pathJuman
                +" -e2 | "+pathKnp + " -tab";
System.out.println(cmd);
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
                node.put("result", "ERROR");
                node.put("message",errorBuf.toString());
                return node;
            }
        } finally {
            br.close();
            is.close();
        }
        // get results
        // 一回全部文節で分けるよー
        List<String> clauseaList = new ArrayList<String>();
        StringBuffer clauseaBuf = new StringBuffer();
        try {
            is = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            for (;;) {
                String line = br.readLine();
                if (line == null || "EOS".equalsIgnoreCase(line)) break;
                if(line.startsWith("#")){
                    line = line.substring(1, line.length()).trim();
                    String[] ress = line.split(" ");
                    node.put("S-ID", ress[0].replace("S-ID:", ""));
                    node.put("KNP", ress[1].replace("KNP:", ""));
                    node.put("DATE", ress[2].replace("DATE:", ""));
                    node.put("SCORE", ress[3].replace("SCORE:", ""));
                } else {
                    if(line.startsWith("*")){
                        if(clauseaBuf.length()>0){
                            clauseaList.add(clauseaBuf.toString());
                        }
                        clauseaBuf = new StringBuffer();
                        clauseaBuf.append(line);
                    } else {
                        clauseaBuf.append("\n"+line);
                    }
                }
            }
            if(clauseaBuf.length()>0){
                clauseaList.add(clauseaBuf.toString());
            }
        }finally{
            br.close();
            is.close();
        }
        // 文節ごとに内容をパースするよー
        ArrayNode clauseas = new ArrayNode(factory);
        for(String str:clauseaList){
            ObjectNode clausea = this.parseClausea(str);
            clauseas.add(clausea);
        }
        node.put("clauseas", clauseas);
        return node;
    }

    /**
     * 文節の解析結果をパース
     * @param str 文節の解析結果文字列
     * @return
     */
    private ObjectNode parseClausea(String str){
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode clausea = new ObjectNode(factory);
        String[] lines = str.split("\n");
        // 1行目は文節全体情報
        // 係り先文節番号
        String targetClausea = lines[0].substring(1, lines[0].indexOf("<")).trim();
        clausea.put("target", targetClausea);
        // タグ部分抽出
        String tags = lines[0].substring(lines[0].indexOf("<")).trim();
        ArrayNode attributes = this.parseTagLine(tags);
        clausea.put("attributes", attributes);
System.out.println(clausea.toString());
System.out.println();

        // 文節の中身の基本句を分析
        ArrayNode phrases = new ArrayNode(factory);
        StringBuffer buf = new StringBuffer();
        String basicPhrasesStr = str.substring(str.indexOf("\n+")+1);
        String basicStr = null;
        while(basicPhrasesStr.indexOf("\n+")>=0){
            basicStr = basicPhrasesStr.substring(0, basicPhrasesStr.indexOf("\n+"));
            ObjectNode phrase = this.parsePhrase(basicStr);
            buf.append(phrase.get("phrase").asText());
            phrases.add(phrase);
            basicPhrasesStr = basicPhrasesStr.substring(basicPhrasesStr.indexOf("\n+", 1)+1);
        }
        if(basicPhrasesStr.length()>0){
            ObjectNode phrase = this.parsePhrase(basicPhrasesStr);
            buf.append(phrase.get("phrase").asText());
            phrases.add(phrase);
        }
        clausea.put("clausea", buf.toString());
        clausea.put("phrases", phrases);
        return clausea;
    }

    /**
     * フレーズの解析結果文字列をパース
     * @param str フレーズの解析結果文字列
     * @return
     */
    private ObjectNode parsePhrase(String str){
System.out.println("------------ "+str);
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode phrase = new ObjectNode(factory);
        String[] lines = str.split("\n");
        // 係り先
System.out.println("------------lines[0] "+lines[0]);
        String target = lines[0].substring(1, lines[0].indexOf("<")).trim();
        phrase.put("target", target);
        String tags = lines[0].substring(lines[0].indexOf("<")).trim();
        ArrayNode attributes = this.parseTagLine(tags);
        phrase.put("attributes", attributes);

        Juman juman = new Juman(this.pathShell,this.pathJuman);
        ArrayNode morphs = new ArrayNode(factory);
        StringBuffer buf = new StringBuffer();
        for(int i=1; i < lines.length; i++){
            String line = lines[i];
            String org = line.substring(0,line.indexOf(" "));
            buf.append(org);
            String jumanStr = line.substring(0,line.indexOf("<"));
            String eTagsStr = line.substring(line.indexOf("<"));
            ObjectNode jumanResult = juman.parseLine(jumanStr);
            ArrayNode eAttributes = this.parseTagLine(eTagsStr);
            jumanResult.put("attributes", eAttributes);
            morphs.add(jumanResult);
        }
        phrase.put("phrase",buf.toString());
        phrase.put("morphemes", morphs);
        return phrase;
    }

    /**
     * タグ行をパース
     * @param str
     * @return
     */
    private ArrayNode parseTagLine(String str){
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode attributes = new ArrayNode(factory);
        str = str.substring(1, str.length());
        str = str .substring(0, str.length()-1);
        String[] sTagsList = str.split("><");
        for(String tagLine:sTagsList){
            if(tagLine.contains(":")){
                ObjectNode attribute = new ObjectNode(factory);
                String key = tagLine.substring(0,tagLine.indexOf(":"));
                String val = tagLine.substring(tagLine.indexOf(":")+1);
                if(val.contains("/")){
                    ArrayNode values = new ArrayNode(factory);
                    String[] vals = val.split("/");
                    for(String v:vals){
                        values.add(v);
                    }
                    attribute.put(key, values);
                    attributes.add(attribute);
                } else {
                    attribute.put(key, val);
                    attributes.add(attribute);
                }
            } else {
                attributes.add(tagLine);
            }
        }
        return attributes;
    }
}
