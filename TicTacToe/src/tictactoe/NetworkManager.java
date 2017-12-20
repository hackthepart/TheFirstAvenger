/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import tictactoe.GameWindow;

/**
 *
 * @author manas
 */
public class NetworkManager {

    private boolean server;
    private boolean client;
    private String name = "";
    protected ServerSocket ss;
    protected Socket connectionSocket;
    private static String ipAddress, port;
    private boolean check1;
    private boolean check2;
    private boolean check3;
    private int row, column;
    protected GameWindow gm;
    

    public void NetworkManagerSub(GameWindow gm) {
        this.gm = gm;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleNetworkIO();
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
        });
        t1.start();
    }

    public void handleNetworkIO() throws IOException {
        Thread process = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (server) {
                        gm.connectionTrancation.setText("Waiting For Player to Join..");
                        ServerSocket ss = new ServerSocket(8080);
                        connectionSocket = ss.accept();
                        System.out.println("Connected to player at " + connectionSocket.getRemoteSocketAddress());
                        gm.connectionTrancation.setText("Connected to player at " + connectionSocket.getRemoteSocketAddress());
                        gm.gameRequisites();
                        Thread serverCom = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int c;
                                    while ((c = connectionSocket.getInputStream().read()) != -1) {
                                        messageResolver((char) c);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        serverCom.start();
                    } else {
                        if (client) {
                            Thread clientCom = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        int c;
                                        connectionSocket = new Socket("localhost", Integer.parseInt(port));
                                        gm.connectionTrancation.setText("Conntected to Host At " + connectionSocket.getRemoteSocketAddress());
                                        InputStreamReader in = new InputStreamReader(connectionSocket.getInputStream());
                                        gm.gameRequisites();
                                        while ((c = in.read()) != -1) {
                                            messageResolver((char) c);
                                        }
                                    } catch (IOException exp) {
                                        exp.printStackTrace();
                                    }
                                }
                            });
                            clientCom.start();

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        process.start();
    }

    public void addressManager(String ipAddress, String port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void messageResolver(char c) {
        if (c != '#' && check1 == false && c != '^') {
            name = name + c;
            gm.playerNameSetter(name);
        } else {
            if (c == '^') {
                check1 = true;
            } else {
                if (c != '#' && check2 == false) {
                    if (c == 'X') {
                        gm.optionSetter(c, 'O');
                    } else {
                        if (c == 'O') {
                            gm.optionSetter(c, 'X');
                        } else {
                            check2 = true;
                        }
                    }
                } else {
                    if (c == '&') {
                        check3 = true;
                    } else {
                        if (check3) {
                            row = (int) c;
                            check3 = false;
                        } else {
                            if (c != '%') {
                                column = (int) c;
                            } else {
                                System.out.println(row + "\t" + column);
                                gm.moveManager(row, column);
                            }
                        }
                    }
                }
            }

        }
    }

    public void statusAssign(boolean server, boolean client) {
        this.server = server;
        this.client = client;
    }
}
