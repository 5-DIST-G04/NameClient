package com.distributed.ta;

/*
LEES
Het is belangrijk dat we de gefailede node zijn naam onthouden om de agent te kunnen gebruiken.
Dus voor we de nameserver contacteren moeten we de naam ergens opslaan.
Na dat de cirkel terug gemaakt is voeren we de failure agent uit om de files goed te zetten.


*/
public class FailureAgent extends Thread {

        int failingNodeID, currentNodeID;
        boolean isFirstTime;// if this is the node that starts it then true else always false!!!

    public FailureAgent(Runnable target, int failingNodeID, int currentNodeID, boolean isFirstTime) {
        super(target);
        this.failingNodeID = failingNodeID;
        this.currentNodeID = currentNodeID;
        this.isFirstTime= isFirstTime;
    }

    public void run(){
        System.out.println("Thread Running");

        //als de agent terug komt bij de originele node die het starte dan stoppen anders verder doen.
        //Dus isFirstTime moet op true staan bij de originele starter node om dit aan te duiden anders hebben
        //we een endless loop.
        if(!(currentNodeID==this.currentNodeID && isFirstTime == false)){


            //hergebruiken van de al geschreven functies.


            //lees lijst van alle files in deze node en check of een van deze files owned was door de failure node
            String[] filesInDir;

            //voor elke file
           /*
           TODO: zelfde check als bij de server hergebruiken om te achterhalen welke files op deze node in
           Todo: failed node zaten door failingNodeID te gebruiken.


           */



        }else{
            System.out.println("We zijn rond gegaan, nu stoppen...");
        }

    }



}
