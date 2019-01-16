package com.distributed.ta;

import com.distributed.common.ClientComm;
import com.distributed.common.FileData;
import com.distributed.common.Node;
import com.distributed.common.ServerComm;

import java.io.File;
import java.util.*;

public class FileManager implements FileChannel {
    private NodeData nodeData = NodeData.getInstance();

    private Map<Integer,FileData> downloaded;
    private Map<Integer,FileData> local;
    private Map<Integer,FileData> replicated;
    private Map<Integer,FileData> owned;

    public FileManager(){
        downloaded = new HashMap<>();
        local = new HashMap<>();
        replicated = new HashMap<>();
        owned = new HashMap<>();
        getLocalFiles();
    }

    private void getLocalFiles(){
        File aDirectory = new File("data");
        String[] filesInDir = aDirectory.list();
        for ( int i=0; i<filesInDir.length; i++ )
        {
           fileInit(filesInDir[i]);
        }
    }

    private void fileInit(String fileName){

            FileData fileData = new FileData(fileName);
            fileData.getFileLog().put(nodeData.getThisNode(),0);       //local=0, replicated=1, downloaded=2
            local.put(fileData.getHash(),fileData);
            replicateFile(fileData);
    }

    //wordt opgeroepen als we file gaan replicaten
    private void replicateFile(FileData fileData) {
        //vraag aan server voor de node waar de HAshNodeID <= hashFile
        int fileHash = fileData.getHash();
        // volgende lijn zal ook uitgevoerd worden als we in deze methode komen via shutdown maar geen probleem want blijft werken
        Node replicationNode = ServerComm.getReplicationNode(fileHash);  //vragen aan server naar welke node de file moete gereplicate worden, maw wie de owner is volgens hash alogritme
        if (replicationNode==nodeData.getThisNode()) {                      // als dat deze node zelf blijkt te zijn, replicate de file naar de vorige node en zet isowner op false want die moet dan geen owner wordne (deze node is zelf owner)
            fileData.setFileOwner(false);
            ClientComm.replicate(fileData, nodeData.getPrevNode());     //dit klopt nog niet want we moeten de echte fileData doorgegeven niet fileData die we zelf net hebben aangemaakt met zelfde naam
        }
        else {
            fileData.setFileOwner(true);                                  // normale geval, de server geeft andere node als owner: stuur file naar die node en zet isowner op true (die node wordt owner)
            ClientComm.replicate(fileData,replicationNode);
        }
    }



    public void receiveNewFile(FileData fileData){

        if (!fileData.isShutdown()) {
            replicated.put(fileData.getHash(),fileData);
            fileData.getFileLog().put(nodeData.getThisNode(), 1);
            if (fileData.isFileOwner()) {
                owned.put(fileData.getHash(), fileData);
            }
        } else {
            //edge case: in geval van shutdown kijken we of de file local op deze node gestored is, zoja moet die nog eens naar de vorige node worden gestuurd
            if (storedLocal(fileData)) {                // storedlocal gebruiken om na te gaan of de node al locale owner was van deze file
                fileData.setShutdown(false);            // in dat geval geval doorsturen nog eens naar de previous node, dus zetten we shutdown op false voor de methode in receivedNewFile method in die previousnode
                replicateFile(fileData);                // file replicaten
            }
            else {
                owned.put(fileData.getHash(), fileData);   //als we van shutdown node file krijgen en die is niet local file van deze node, dan wordt deze node gewoon owner (zet in owned ljst)
            }
        }
    }

    private boolean storedLocal(FileData fileData) {
        for (Map.Entry<Integer, FileData > entry : local.entrySet()) {
            if (entry.getKey() == fileData.getHash())
                return true;
        }
        return false;
        }



    public Map<Integer,FileData> getAllFiles(){
        Map<Integer,FileData> map = new HashMap<>();
        map.putAll(downloaded);
        map.putAll(local);
        map.putAll(replicated);
        map.putAll(owned);
        return map;
    }

    public void updateFiles(int newHashInt, Node newNode) {
        // hier gaan we alle elementen van de map owned af, maw alle files waar de node owner van is.
        for (Map.Entry<Integer, FileData > entry : owned.entrySet()) {              //elke entry bestaat uit een file (=key) en de hashmap(=value) waarin dan weer enerzijds de nodes (=keys) te vinden zijn die deze file bezitten en een intger value (=value) die aangeeft of het om replicated, local of downloaded gaat.
            if ((newHashInt<entry.getKey()) && (newHashInt>nodeData.getThisNode().getHash())) {
                //zet de isFileOwner boolean op true
                //replicate file naar de nieuwe node, deze zal weten wat hij er mee moet doen zoals beschreven in methode "receiveFile" in klasse ClientNode
                //deze node blijft bezitter van replicated/local exemplaar van de file dus moet niks aan logbestand wijzigen, de ontvanger, zal het logbestand overnemen en zijn eigen er aan toevoegen.
                //verwijder de fileLog uit de hashmap van fileLogs op deze node, want hij is geen owner meer.
                //dit kan ik waarschijnlijk niet allemaal doen binnen deze for lus want entry kan ik niet gebruiken als file object?
                entry.getValue().setFileOwner(true);
                ClientComm.replicate(entry.getValue(), newNode);
                owned.remove(entry.getKey());                      //verwijder fileLog van deze file uit lijst van fileLog want hij is niet meer owner.
                //gaat die for lus nog correct runnen als ik binnen de lus een entry verwijder?
            }
        }

    }

    private void shutDown(Map<Integer,FileData> allFiles) {
        allFiles = getAllFiles();
        for (Map.Entry<Integer, FileData > entry : replicated.entrySet()) {
            if (entry.getValue().isFileOwner()) {
                entry.getValue().setFileOwner(true);
            }else entry.getValue().setFileOwner(false);
            entry.getValue().getFileLog().remove(nodeData.getThisNode());
            ClientComm.replicate(entry.getValue(), nodeData.getPrevNode());
        }

        for (Map.Entry<Integer, FileData> entry : local.entrySet()) {
            entry.getValue().setDownloaded(false);
            for (Map.Entry<Node,Integer> entry2 : entry.getValue().getFileLog().entrySet()) {
                if (entry2.getValue()==2) entry.getValue().setDownloaded(true);
            }
            //if entry.getValue().isDownloaded() // eigenlijk zouden we dan de owner van de file moeten inlichten dat die de file mag verwijderen.
            entry.getValue().getFileLog().remove(nodeData.getThisNode());
        }
    }


    @Override
    public void update(Object arg) {
        this.fileInit((String) arg);
    }
}
