package com.distributed.ta;

import java.util.Scanner;


public class Main {

    public static void main(String args[]) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("debug");
        System.out.println("enter the name of this node");
        String name = input.next();

        ClientNode clientNode = new ClientNode(name);
        clientNode.Start();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", NodeData.getInstance().getBaseUri()));
        System.in.read();
        clientNode.Stop();
    }
}






