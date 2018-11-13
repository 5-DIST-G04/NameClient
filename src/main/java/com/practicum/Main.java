package com.practicum;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the server url:");
        String url = input.next();

        JSONService json = new JSONService(url);

        System.out.println("enter the name of this node");
        String name = input.next();
        try {
            json.SubmitName(name);
        } catch (StatusExeption e){
            System.out.printf("the server responded with statuscode: %d\n",e.getStatuscode());
            return;
        }

        while (true){
            System.out.println("Enter the file name you want the location of or enter quit to exit");
            String fileName = input.next();
            if (fileName.equals("quit"))
                break;
            FileLocation fileLocation;
            try {
                fileLocation = json.getFileLocation(fileName);
                System.out.println("the file: " + fileLocation.getFileName() + " is to be found at: " + fileLocation.getIpAddress());
            } catch (StatusExeption e){
                System.out.printf("the server responded with status code: %d\n",e.getStatuscode());
            }
        }

        try {
            json.RemoveName(name);
            System.out.println("the node was unregistered from the nameserver");
        } catch (StatusExeption e){
            System.out.printf("the server responded with status code %d\n",e.getStatuscode());
        }



    }


}


