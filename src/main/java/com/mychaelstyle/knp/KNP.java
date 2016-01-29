package com.mychaelstyle.knp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mychaelstyle.juman.Juman;

/**
 * 
 */

/**
 * KNPのJavaラッパークラスです。
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

    public KNP(){
        super();
    }
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

        if(null==pathShell || pathShell.isEmpty()) pathShell = Juman.PATH_SHELL;
        if(null==pathJuman || pathJuman.isEmpty()) pathJuman = Juman.PATH_JUMAN;
        if(null==pathKnp || pathKnp.isEmpty()) pathKnp = PATH_KNP;
        String cmd = "echo \""+target+"\" | "+ this.pathJuman
                +" | "+pathKnp;
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
        try {
            is = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            for (;;) {
                String line = br.readLine();
                if (line == null || "EOS".equalsIgnoreCase(line)) break;
System.out.println(line);
                // TODO : 未実装よー
            }
        }finally{
            br.close();
            is.close();
        }
        return node;
    }
}
