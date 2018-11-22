package com.practicum;

public class ReceivedData {
    String data;

    int nextNode,previouseNode,receivedNode,ownNode = 352352;
    int totalNodes = 3;


    public ReceivedData(String data) {
        this.data = data;
        identify();
    }


    //find out what we received
    private void identify(){
        switch(data){


        }

    }

    public void mapNodes(){

    }



    //-------------------------------------------------
    public void newNode(){
        if ( totalNodes < 1) { //im the fist node
            previouseNode = ownNode;
            nextNode = ownNode;
        }else {
            //wachten op antwoord van de andere nodes.
            }
        }

    public void otherNode() {

        String multicastedNode = "test";
        receivedNode = Math.abs(multicastedNode.hashCode()) % 32768;

        if (ownNode < receivedNode && receivedNode < nextNode) {
            //update nextNode met reveivedNode value
            nextNode=receivedNode;

            //Stuur naar receivedNode   ownNode en nextNode values
            //...


        } else if (previouseNode < receivedNode && receivedNode < ownNode) {

            // update previouseNode met receivedNode value
            previouseNode = receivedNode;
            //done

        }

    }
    //-------------------------------------------------




    //-------------------    SHUTDOWN    ----------------------------------


    public void Shutdown(){





    }










}
