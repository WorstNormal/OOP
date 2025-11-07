package ru.nsu.gaev;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        System.out.println("After put('one', 1): " + hashTable.get("one"));
        hashTable.update("one", 1.0);
        System.out.println("After update('one', 1.0): " + hashTable.get("one"));
        hashTable.put("two", 2);
        System.out.println("Does it contain key 'two': " + hashTable.containsKey("two"));

        hashTable.remove("two"); // Operation 3
        System.out.println("Does it contain key 'two' after removal: " + hashTable.containsKey("two"));
        System.out.println("Table state: " + hashTable.toString());
        System.out.println("--- Iterator and ConcurrentModificationException check ---");
        for (Entry<String, Number> entry : hashTable) {
            if (entry.key.equals("one")) {
                System.out.println("Attempting to modify during iteration...");
                hashTable.put("three", 3);
            }
        }
        HashTable<String, Number> anotherTable = new HashTable<>();
        anotherTable.put("one", 1.0);
        System.out.println("Table equals another table: " + hashTable.equals(anotherTable));
    }
}