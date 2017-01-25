/**
 * Created by yul04 on 2017/1/10.
 */

import lib280.exception.AfterTheEnd280Exception;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;

import java.util.Random;
import java.util.Scanner;

public class A1Q1 {

    Scanner in = new Scanner(System.in);

    public static Sack [] generatePlunder ( int howMany ) {
        Random generator = new Random ();
        Sack grain [] = new Sack [ howMany ];
        for ( int i =0; i < howMany ; i ++) {
            grain [i] = new Sack (
                    Grain . values ()[ generator . nextInt ( Grain . values (). length )] ,
                    generator . nextDouble () * 100 );
        }
        return grain ;
    }

    public LinkedList280<Sack> [] SackArray(){
        LinkedList280<Sack> SackList [] = new LinkedList280 [5];
        for (int i = 0; i < 5; i++){
            SackList[i] = new LinkedList280<Sack>();
        }
        System.out.print("Please enter the number of grain object you want to create:");
        int a = in.nextInt();
        Sack[] grain = generatePlunder(a);
        for (int i = 0; i < grain.length; i++){
            if(grain[i].getType().toString() == "WHEAT"){
                SackList[0].insertLast(grain[i]);
            }
            else if(grain[i].getType().toString() == "BARLEY"){
                SackList[1].insertLast(grain[i]);
            }
            else if(grain[i].getType().toString() == "OATS"){
                SackList[2].insertLast(grain[i]);
            }
            else if(grain[i].getType().toString() == "RYE"){
                SackList[3].insertLast(grain[i]);
            }
            else{
                SackList[4].insertLast(grain[i]);
            }
        }
        showResult(SackList);
        in.close();
        return SackList;
    }

    public void getTotalWeight(LinkedList280<Sack> Grain){
        double totoalWeight = 0.0;
        LinkedIterator280<Sack> tempIterator = new LinkedIterator280<Sack>(Grain);
        if(tempIterator.itemExists()){
            String temp = tempIterator.item().getType().toString();
            while (tempIterator.itemExists()){
                totoalWeight = totoalWeight + tempIterator.item().getWeight();
                tempIterator.goForth();
            }
            System.out.print("Jack plundered " + totoalWeight + " pounds of " + temp + "\n");
        }
    }

    public void showResult(LinkedList280<Sack> [] SackArrayCreate){
        String ax []= {"WHEAT", "BARLEY", "OATS", "RYE", "OTHER"};
        for(int i = 0; i < 5; i++){
            if (!SackArrayCreate[i].isEmpty()){
                getTotalWeight(SackArrayCreate[i]);
            }
            else{
                System.out.print("Jack plundered " + 0.0 + " pounds of " + ax[i] + "\n");
            }
        }
    }

    public static void main(String argv[]){
        LinkedList280<Sack> SackList [] = new LinkedList280 [5];
        for (int i = 0; i < 5; i++){
            SackList[i] = new LinkedList280<Sack>();
        }
        Sack[] grain = generatePlunder(10);
        for (int i = 0; i < grain.length; i++){
            if(grain[i].getType().toString().equals("WHEAT")){
                SackList[0].insertLast(grain[i]);
            }
            else if(grain[i].getType().toString().equals("BARLEY")){
                SackList[1].insertLast(grain[i]);
            }
            else if(grain[i].getType().toString().equals("OATS")){
                SackList[2].insertLast(grain[i]);
            }
            else if(grain[i].getType().toString().equals("RYE")){
                SackList[3].insertLast(grain[i]);
            }
            else{
                SackList[4].insertLast(grain[i]);
            }
        }
        String ax []= {"WHEAT", "BARLEY", "OATS", "RYE", "OTHER"};
        for(int i = 0; i < 5; i++){
            if (!SackList[i].isEmpty()){
                double totoalWeight = 0.0;
                LinkedIterator280<Sack> tempIterator = new LinkedIterator280<Sack>(SackList[i]);
                if(tempIterator.itemExists()){
                    String temp = tempIterator.item().getType().toString();
                    while (tempIterator.itemExists()){
                        totoalWeight = totoalWeight + tempIterator.item().getWeight();
                        tempIterator.goForth();
                    }
                    System.out.print("Jack plundered " + totoalWeight + " pounds of " + temp + "\n");
                }
            }
            else{
                System.out.print("Jack plundered " + 0.0 + " pounds of " + ax[i] + "\n");
            }
        }

    }

}
