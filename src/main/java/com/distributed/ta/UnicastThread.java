package com.distributed.ta;

import java.net.*;
import java.io.*;


public class UnicastThread extends Thread {

    private NodeData ownNode = NodeData.getInstance();

    private Socket clientSocket;

        //private String path;

    UnicastThread(Socket socket, int threadNumber) throws IOException {       //constructor
        this.clientSocket = socket;
        //this.path = "C:\\Users\\yvesk\\OneDrive\\school\\2018-2019\\Distributed systems\\practicum\\text"+threadNumber+".txt";

    }


    public void run () {
        try {
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

                /*try {                                                                       //create file
                    // Create the empty file with default permissions, etc.
                    Files.createFile(path);
                } catch (FileAlreadyExistsException x) {
                    System.err.format("file named %s" +
                            " already exists%n", path);
                } catch (IOException x) {
                    // Some other sort of failure, such as permissions.
                    System.err.format("createFile error: %s%n", x);
                }*/

            String received = in.readLine();
            System.out.println("received: " + received);
            int type = bepaalType(received);
            if (type==1) {
                berekenNieuweNodePrevious(received);
                }
            else if (type==2)
                berekenNieuweNodeServer(received);
            else if  (type==3)
                berekenShutdown(received);



        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int bepaalType(String bericht) {
        int type = 0;
        bericht = bericht.substring(1,bericht.length()-1);
        String[] splits = bericht.split(",");
        if (splits[0].equals("pre")) type=1;
        else if (splits[0].equals("ser")) type=2;
        else if (splits[0].equals("shu")) type=3;
        return type;
    }

    public void berekenNieuweNodePrevious(String bericht) {
        String previousBericht =  bericht.substring(1,bericht.length()-1);
        String[] splits = previousBericht.split(",");
        int ownHashfromPrevious = Integer.parseInt(splits[1]);
        int nextHashFromPrevious = Integer.parseInt(splits[2]);
        String ownIPfromPrevious = splits[3];
        String nextIPfromPrevious = splits[4];

        ownNode.setPreviousHash(ownHashfromPrevious);
        ownNode.setNextHash(nextHashFromPrevious);
        ownNode.setPreviousIP(ownIPfromPrevious);
        ownNode.setNextIP(nextIPfromPrevious);

    }

    public void berekenNieuweNodeServer (String bericht) {
        String serverBericht = bericht.substring(1, bericht.length() - 1);
        String[] splits = serverBericht.split(",");
        int nodes = Integer.parseInt(splits[1]);
        if (nodes < 1) {
            ownNode.setPreviousHash(ownNode.getHash());
            ownNode.setPreviousIP(ownNode.getIpAddress());
            ownNode.setNextHash(ownNode.getPreviousHash());
            ownNode.setNextIP(ownNode.getIpAddress());
        }
    }

    public void berekenShutdown(String bericht) {
        String shutdownBericht =  bericht.substring(1,bericht.length()-1);
        String[] splits = shutdownBericht.split(",");
        int hashSender = Integer.parseInt(splits[1]);
        if (hashSender==ownNode.getNextHash()) {
            ownNode.setNextHash(Integer.parseInt(splits[2]));
            ownNode.setNextIP(splits[3]);
        }
        else if (hashSender==ownNode.getPreviousHash()) {
            ownNode.setPreviousHash(Integer.parseInt(splits[2]));
            ownNode.setPreviousIP(splits[3]);
        }

    }
}




