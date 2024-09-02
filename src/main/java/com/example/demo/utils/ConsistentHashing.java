package com.example.demo.utils;

import java.util.SortedMap;
import java.util.TreeMap;

//describe a consistent hashing algorithm
//https://en.wikipedia.org/wiki/Consistent_hashing
public class ConsistentHashing {
    private final SortedMap<Integer, String> circle = new TreeMap<>();
    private final int numberOfReplicas;
    private final int virtualNodes;

    public ConsistentHashing(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
        this.virtualNodes = numberOfReplicas;
    }

    // Hash function to map nodes to the hash circle
    private int hash(String key) {
        return key.hashCode(); // Use a proper hash function here
    }

    // Add a node to the hash ring
    public void addNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            circle.put(hash(node + i), node);
        }
    }

    // Remove a node from the hash ring
    public void removeNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            circle.remove(hash(node + i));
        }
    }

    // Get the node for a particular key
    public String getNode(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hash(key);
        SortedMap<Integer, String> tailMap = circle.tailMap(hash);
        Integer keyHash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        return circle.get(keyHash);
    }

    public static void main(String[] args) {
        print(3);
        print(4);
        print(100);
    }

    public static void print(int numberOfReplicas){
        ConsistentHashing ch = new ConsistentHashing(numberOfReplicas);

        // Add nodes
        ch.addNode("Node1");
        ch.addNode("Node2");
        ch.addNode("Node3");

        // Test with keys
        System.out.println("Node for key 'myKey1': " + ch.getNode("myKey1"));
        System.out.println("Node for key 'myKey2': " + ch.getNode("myKey2"));

        // Remove a node
        ch.removeNode("Node2");
        System.out.println("Node for key 'myKey1' after removal: " + ch.getNode("myKey1"));
    }
}

