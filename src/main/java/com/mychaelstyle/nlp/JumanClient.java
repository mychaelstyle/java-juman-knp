/**
 * 
 */
package com.mychaelstyle.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * サーバーモードで動作させているJumanのクライアントとして動作する形態素解析ツールクラスです。
 * 
 * juman -S コマンドでサーバー起動してからご利用ください。
 * 
 * @author masanori
 *
 */
public class JumanClient extends Juman {

    public static final String HOST_DEFAULT = "localhost";
    public static final Integer PORT_DEFAULT = 32000;
    private String host = HOST_DEFAULT;
    private Integer port = PORT_DEFAULT;

    /**
     * コンストラクタ
     * デフォルト値 localhost:32000
     */
    public JumanClient(){
        super();
    }

    /**
     * コンストラクタ
     * @param host
     * @param port
     */
    public JumanClient(String host, Integer port){
        super();
        this.host = host;
        this.port = port;
    }

    /* (non-Javadoc)
     * @see com.mychaelstyle.nlp.Juman#parse(java.lang.String)
     */
    @Override
    public ObjectNode parse(String target) throws IOException, InterruptedException {
        Socket socket = null;
        try{
            socket = new Socket(this.host, this.port);
            return parse(target,socket);
        }finally{
            if(null!=socket && !socket.isClosed()){
                socket.close();
            }
        }
    }

    /**
     * サーバー接続を渡して解析する場合のメソッド
     * @param target
     * @param socket
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ObjectNode parse(String target, Socket socket) throws IOException, InterruptedException {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = new ObjectNode(factory);
        ArrayNode morphs = new ArrayNode(factory);

        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;
        try{
            pw = new PrintWriter(socket.getOutputStream(), true);
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            pw.write("RUN -e2\n");
            pw.write(target);
            pw.write("\n");
            pw.flush();
            int c = 0;
            while(is.available() == 0){
                if(c>300) break;
                Thread.sleep(100);
                c++;
            }
            for (;;) {
                String line = br.readLine();
                if (line == null || "EOS".equalsIgnoreCase(line.trim())) break;
                ObjectNode morpheme = this.parseLine(line);
                morphs.add(morpheme);
            }

        }finally{
            try{
                br.close();
            } catch(Exception e){
            }
            try{
                is.close();
            } catch(Exception e){
            }
            try{
                pw.close();
            } catch(Exception e){
            }
        }

        node.put(L_RESULT,Result.OK);
        node.put(L_MESSAGE,"");
        node.put(L_MORPHEMES, morphs);

        return node;
    }

    public String getResult(String target) throws IOException, InterruptedException {
        Socket socket = null;
        try{
            socket = new Socket(this.host, this.port);
            return getResult(target,socket);
        }finally{
            if(null!=socket && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    public String getResult(String target, Socket socket) throws IOException, InterruptedException {
        StringBuffer buf = new StringBuffer();
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;
        try{
            pw = new PrintWriter(socket.getOutputStream(), true);
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            pw.write("RUN\n");
            pw.write(target);
            pw.write("\n");
            pw.flush();
            int c = 0;
            while(is.available() == 0){
                if(c>300) break;
                Thread.sleep(100);
                c++;
            }
            for (;;) {
                String line = br.readLine();
                if (line == null || "EOS".equalsIgnoreCase(line.trim())){
                    buf.append("EOS");
                    break;
                }
                buf.append(line).append("\n");
            }
        }finally{
            try{
                br.close();
            } catch(Exception e){
            }
            try{
                is.close();
            } catch(Exception e){
            }
            try{
                pw.close();
            } catch(Exception e){
            }
        }
        return buf.toString();
    }

}