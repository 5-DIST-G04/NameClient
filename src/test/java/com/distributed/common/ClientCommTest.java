package com.distributed.common;

import com.distributed.ta.ClientNode;
import com.distributed.ta.NodeData;
import org.junit.*;

import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.*;

public class ClientCommTest {
    private static ClientNode node;

    @BeforeClass
    public static void setUp() throws Exception {
        node = new ClientNode("testNode");
        node.Start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        node.Stop();
    }

    @Test
    public void setPrevNeigbhour() {
        Node testNode = new Node ("testPrevNode", NodeData.getInstance().getThisNode().getIpAddress());
        ClientComm.setPrevNeigbhour(testNode,NodeData.getInstance().getThisNode());
        assertEquals(testNode,NodeData.getInstance().getPrevNode());
    }

    @Test
    public void setNextNeighbour() {
        Node testNode = new Node ("testNextNode", NodeData.getInstance().getThisNode().getIpAddress());
        ClientComm.setNextNeighbour(testNode,NodeData.getInstance().getThisNode());
        assertEquals(testNode,NodeData.getInstance().getNextNode());
    }
}