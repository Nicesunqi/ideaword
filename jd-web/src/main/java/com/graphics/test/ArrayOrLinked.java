package com.graphics.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArrayOrLinked {
    static List<Integer> array=new ArrayList<Integer>();
    static List<Integer> linked=new LinkedList<Integer>();

    public static void main(String[] args) {

    int[]nums = {1,3,2,6,5,8,7,9,10};

        for (int i = 0; i < nums.length-1; i++) {
            if(nums[i] > nums[i + 1]){
                int temp = nums[i];
                nums[i] = nums[i + 1];
                nums[i + 1] = temp;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
        }

//        for(int i=0;i<10000;i++){
//            array.add(i);
//            linked.add(i);
//        }
//        System.out.println("array time:"+getTime(array));
//        System.out.println("linked time:"+getTime(linked));
//        System.out.println("array insert time:"+insertTime(array));
//        System.out.println("linked insert time:"+insertTime(linked));

    }
    public static long getTime(List list){
        long time=System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            int index=Collections.binarySearch(list, list.get(i));
            if(index!=i){
                System.out.println("ERROR!");
            }
        }
        return System.currentTimeMillis()-time;
    }
    public static long insertTime(List list){
        long time=System.currentTimeMillis();
        for(int i=100;i<10000;i++){
            list.add(5000,i);
        }
        return System.currentTimeMillis()-time;

    }

}
