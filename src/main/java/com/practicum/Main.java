package com.practicum;

import java.util.Scanner;

public class Main {
    private static String userInput;
    public static void main(String args[]) throws Exception {

         JSONService json = new JSONService();
         Scanner input = new Scanner(System.in);




             System.out.println("Location is ");

             try {


                 while (true) {


                     System.out.print('\f');
                     System.out.println("Enter file name > ");
                     userInput = input.next();
                     FileLocation fileLocation = json.getFileLocation(userInput);

                     if (fileLocation.getIpAddress() != "0" ){
                         System.out.println("Location is " + fileLocation.getIpAddress());
                     } else {
                         System.out.println("File cannot be found...");
                     }




                 }
             } catch (Exception e) {e.printStackTrace();}
         }
    }


