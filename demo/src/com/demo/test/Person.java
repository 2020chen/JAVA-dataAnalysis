package com.demo.test;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newuser on 2018/7/7.
 */
public class Person {
    private List<String> ack=new ArrayList<String>();
    private List<String> blockLocation=new ArrayList<String>();
    private int blockSizeNum;

    public synchronized void addAck(String ack){
        this.ack.add(ack);
    }
    public synchronized int getAck(){
        return this.ack.size();
    }
    public synchronized void saveBlockLocation(String splitLocation){
        this.blockLocation.add(splitLocation);
    }

    class HandleMapThread implements Runnable{
        private String value;

        public HandleMapThread(String value) {
            this.value = value;
        }

        @Override
        public void run() {

            String[] words = value.split(",|\r\n");
            FileWriter fw = null;
            String splitLocation = String.format("d:/%s_test.txt",Thread.currentThread().getName());
           saveBlockLocation(splitLocation);
            try {
                fw = new FileWriter(splitLocation);
                for (String word:words){
                    fw.write(String.format("%s,%d%s",word,1,System.lineSeparator()));
                    fw.flush();
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addAck(Thread.currentThread().getName());
           // System.out.println(Thread.currentThread().getName() + "-----" + value);

        }
    }

    static class HandleReducerThread implements Runnable{
        private String blockLocation;

        public  HandleReducerThread(String blockLocation){
            this.blockLocation = blockLocation;
        }

        @Override
        public void run() {
            LineNumberReader numberReader = null;
            try {
                numberReader = new LineNumberReader(new FileReader(blockLocation));
                String line;
                while((line = numberReader.readLine() )!=null){
                    String[] words = line.split(",");
                    String key = words[0];

                    int value = Integer.parseInt(words[1]);
                    System.out.println(Thread.currentThread().getName()+""+String.format("%s--%s",key,value));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(Thread.currentThread().getName()+"-----"+blockLocation);
        }
    }

    public void splitBlock(String fileName,int blockSize)throws Exception{
        FileReader fr = new FileReader(fileName);
        char[] buffer = new char[blockSize];
        int len;
        while((len = fr.read(buffer))!=-1){
            //System.out.println(new String(buffer)+"-----");
            //System.out.println("*************");
            //System.out.println(new String(buffer,0,len)+"-----");

            String value = new String(buffer,0,len);
            new Thread(new HandleMapThread(value)).start();
            blockSizeNum++;
        }

    }


    public static void main(String[] args) throws Exception{
        Person hadoop = new Person();
        hadoop.splitBlock("d:/test.txt", 15);
        while(true){
            if(hadoop.getAck() == hadoop.blockSizeNum){
                break;
            }
        }
        System.out.println("start reduce...");
        for(String sl:hadoop.blockLocation){
            new Thread(new HandleReducerThread(sl)).start();

        }

    }
}
