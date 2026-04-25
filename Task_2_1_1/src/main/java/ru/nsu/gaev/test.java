package ru.nsu.gaev;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TransferQueue;

public class test {
    public static boolean solve(String str) {
        int[] counter = new int[5];
        String s = "";
        Deque<String> dq = new ArrayDeque<>();
        for(int i = 0; i < str.length(); i++)
        {
            if(String.valueOf(str.charAt(i)) == "[" || String.valueOf(str.charAt(i)) == "{"
            || String.valueOf(str.charAt(i)) == "(")
            {
                dq.addLast(String.valueOf(str.charAt(i)));
            }
            else{
                s = dq.pollLast();
                if(s == "[")
                {
                    if(s == )
                }
            }
        }
    }
    public static void main(String[] args) {
        System.out.println(solve("()[{}]"));
    }

}
