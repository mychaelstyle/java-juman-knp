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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author masanori
 *
 */
public class KNPClient extends KNP {
    public static final String HOST_DEFAULT = "localhost";
    public static final Integer PORT_DEFAULT = 31000;
    private static Boolean connected = false;
    private static Queue<Socket> socketQueue = new LinkedList<Socket>();
    private String host = HOST_DEFAULT;
    private Integer port = PORT_DEFAULT;

    private JumanClient juman = null;

    /**
     * コンストラクタ
     * デフォルトはlocalhost:31000
     */
    public KNPClient(){
        super();
        juman = new JumanClient();
    }

    /**
     * コンストラクタ
     * @param host
     * @param port
     */
    public KNPClient(String host, Integer port, String jumanHost, Integer jumanPort){
        super();
        this.host = host;
        this.port = port;
        this.juman = new JumanClient(jumanHost,jumanPort);
    }

    /**
     * サーバーにソケット接続して接続をプール
     * @param pool
     * @throws IOException
     */
    public void connect(Integer pool) throws IOException {
        juman.connect(pool);
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
        juman.close();
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
     * @see com.mychaelstyle.nlp.KNP#parse(java.lang.String)
     */
    @Override
    public ObjectNode parse(String target) throws IOException, InterruptedException {
        return this.parse(target,false);
    }

    public ObjectNode parse(String target,Boolean connect) throws IOException, InterruptedException {
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
            return parse(target,socket);
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

    public ObjectNode parse(String target,Socket socket) throws IOException, InterruptedException {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = new ObjectNode(factory);

        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;
        // get results
        // 一回全部文節で分けるよー
        List<String> clauseaList = new ArrayList<String>();
        StringBuffer clauseaBuf = new StringBuffer();
        try{
            String jumanResult = juman.getResult(target);
            jumanResult = jumanResult.substring(jumanResult.indexOf("200 OK")+7);
            pw = new PrintWriter(socket.getOutputStream(), true);
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            pw.write("RUN -tab\n");
            pw.write(jumanResult);
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
                if("200 Running KNP Server".equalsIgnoreCase(line)
                        || line.startsWith("200 OK")){
                    // TODO: server connection status
                } else if(line.startsWith("#")){
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
            try{
                br.close();
            } catch(Exception e){
                // TODO: log
            }
            try{
                is.close();
            } catch(Exception e){
                // TODO: log
            }
            try{
                pw.close();
            } catch(Exception e){
                // TODO: log
            }
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
}
