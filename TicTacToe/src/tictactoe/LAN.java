
package tictactoe;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class LAN {
    Socket s;
    ServerSocket ss;
    DataInputStream dis;
    DataOutputStream dos;
    InputStreamReader isr;
    BufferedReader stdin;
    String data;
    boolean isReady=false;
    boolean isServer=false;
    
    public LAN(){
        
    }
    public void startClient(String ip,int port) {
        try {
            s = new Socket(ip,port);
            socketReady();
        }
        catch (Exception e) {
            
        }
    }
    public void startServer(int port) {
        try {
            ss = new ServerSocket(port);
            s = ss.accept();
            socketReady();
        }
        catch(Exception e) {
            
        }
    }
    private void socketReady() {
        try {
            dis = dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            isr = new InputStreamReader(System.in);
            stdin = new BufferedReader(isr);
            isReady=true;
        }
        catch (Exception e) {
            
        }
        
    }
    public void sendData(String str) {
        try {
            dos.writeUTF(str);
            dos.flush();
        }
        catch (Exception e) {
            
        }
    }
    public String getData() {
        try {
            data=dis.readUTF();
            return data;
        }
        catch (Exception e) {
            return null;
        }
    }
}
