/**
 * 
 */
package com.mychaelstyle.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

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
    private static Boolean connected = false;
    private static Queue<Socket> socketQueue = new LinkedList<Socket>();
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

    /**
     * サーバーにソケット接続して接続をプール
     * @param pool
     * @throws IOException
     */
    public void connect(Integer pool) throws IOException {
        synchronized(socketQueue){
            if(socketQueue.size()>pool){
                while(socketQueue.size()>pool){
                    Socket socket = socketQueue.poll();
                    if(null!=socket && !socket.isClosed()){
                        try{
                            socket.close();
                        } catch(Exception e){
                            // TODO log
                        }
                    }
                }
            } else {
                while(socketQueue.size()<pool){
                    Socket socket = new Socket(this.host, this.port);
                    socketQueue.add(socket);
                }
            }
            connected = true;
        }
    }

    /**
     * サーバーからソケット接続を切断
     */
    public void close(){
        synchronized(socketQueue){
            while(socketQueue.size()>0){
                Socket socket = socketQueue.poll();
                if(null!=socket && !socket.isClosed()){
                    try{
                        socket.close();
                    } catch(Exception e){
                        // TODO log
                    }
                }
            }
            connected = false;
        }
    }

    /* (non-Javadoc)
     * @see com.mychaelstyle.nlp.Juman#parse(java.lang.String)
     */
    @Override
    public ObjectNode parse(String target) throws IOException, InterruptedException {
        return parse(target,false);
    }

    /**
     * パースします。第２引数をtrueにすると、コネクションプールがない場合は新規で接続し切断することができます。
     * @param target パース対象文字列
     * @param connect コネクションプールがない場合に接続するならtrue
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ObjectNode parse(String target, boolean connect) throws IOException, InterruptedException {
        Socket socket = null;
        try{
            if(connect){
                socket = new Socket(this.host, this.port);
            } else if(connected){
                int counter = 0;
                while(true){
                    if(counter>100) throw new IOException("fail to get a connection.");
                    synchronized(socketQueue){
                        if(socketQueue.size()>0) break;
                    }
                    counter++;
                    Thread.sleep(100);
                }
                synchronized(socketQueue){
                    socket = socketQueue.poll();
                }
                if(socket.isClosed()){
                    socket.connect(new InetSocketAddress(this.host,this.port));
                }
            } else {
                throw new IOException("not connected yet!");
            }
            return parse(target,socket);
        }finally{
            if(connect && null!=socket && !socket.isClosed()){
                socket.close();
            } else if(null!=socket && !socket.isClosed()){
                synchronized(socketQueue){
                    socketQueue.add(socket);
                }
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
        return getResult(target,false);
    }

    public String getResult(String target, Boolean connect) throws IOException, InterruptedException {
        Socket socket = null;
        boolean usePool = false;
        try{
            if(connected){
                int counter = 0;
                while(true){
                    if(counter>100) throw new IOException("fail to get a connection.");
                    synchronized(socketQueue){
                        if(socketQueue.size()>0) break;
                    }
                    counter++;
                    Thread.sleep(100);
                }
                synchronized(socketQueue){
                    usePool = true;
                    socket = socketQueue.poll();
                }
            } else if(connect){
                socket = new Socket(this.host, this.port);
            } else {
                throw new IOException("not connected yet!");
            }
            return getResult(target,socket);
        }finally{
            if(usePool){
                synchronized(socketQueue){
                    socketQueue.add(socket);
                }
            } else if(null!=socket && !socket.isClosed()) {
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