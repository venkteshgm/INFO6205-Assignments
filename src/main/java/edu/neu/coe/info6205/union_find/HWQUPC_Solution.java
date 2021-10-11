package edu.neu.coe.info6205.union_find;
import java.util.Random;

public class HWQUPC_Solution {
    public static boolean isComplete(UF h){
        int check = h.find(0);
        if(h.size()<2){
            return true;
        }else {
            for (int i = 1; i < h.size(); i++) {
                if (h.find(i) != check) {
                    return false;
                }
            }
            return true;
        }
    }
    public static int union_find(int n){
        Random random = new Random();
        int connections = 0;
        UF_HWQUPC h = new UF_HWQUPC(n, true);
        while(!isComplete(h)){
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            if(!h.connected(x,y)){
                h.union(x,y);
            }
            connections++;
        }
        return connections;
    }
    public static void main(String[] args){
        int q = 1000;
        for(int n=50;n<q;n+=50) {
//            System.out.println("number of pairs generated for " + n + " HWQUPC sites is " + union_find(n));
//            System.out.println(union_find(n));
            System.out.println(union_find(n)+" number of random pairs generated towards connecting "+n+" sites");
        }

    }
}
