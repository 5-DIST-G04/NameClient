package com.distributed.ta;

import com.distributed.common.ClientComm;
import com.distributed.common.FileData;
import com.distributed.common.Node;
import com.distributed.common.ServerComm;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager {
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

            replicateFile( filesInDir[i]);
        }
    }

    private void replicateFile(String s) {
        //vraag aan server voor de node waar de HAshNodeID <= hashFile
        FileData fileData = new FileData(s);
        fileData.getFileLog().put(nodeData.getThisNode(),0);        //local=0, replicated=1, downloaded=2
        local.put(fileData.getHash(),fileData);
        int fileHash = fileData.getHash();
        Node replicationNode = ServerComm.getReplicationNode(fileHash);
        if (replicationNode==nodeData.getThisNode()) {
            fileData.setFileOwner(false);
            ClientComm.replicate(fileData, nodeData.getPrevNode());     //dit klopt nog niet want we moeten de echte fileData doorgegeven niet fileData die we zelf net hebben aangemaakt met zelfde naam
        }
        else {
            fileData.setFileOwner(true);
            ClientComm.replicate(fileData,replicationNode);
        }
    }

    public void submitNewFile(FileData fileData){
        if (!fileData.isShutdown()) {
            replicated.put(fileData.getHash(),fileData);
            if (fileData.isFileOwner()) {
                fileData.getFileLog().put(nodeData.getThisNode(), 1);
                owned.put(fileData.getHash(), fileData);
            }
        } else {
            replicateFile(fileData.getName());
        }
    }

    public Map<Integer,FileData> getAllFiles(){
        Map<Integer,FileData> map = new HashMap<>();
        map.putAll(downloaded);
        map.putAll(local);
        map.putAll(replicated);
        map.putAll(owned);
        return map;
    }
}
