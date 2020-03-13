package com.wyu.hadoopapi.car.texi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.*;

/**
 * Created by newuser on 2018/7/14.
 */
public class SecondTexiNetWork {
    public static class SecondTexiNetWorkMapper extends Mapper<LongWritable,Text,Text,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lines = value.toString();
            String[] datas = lines.split("\\t");
            String texiId = datas[0];
            String time = datas[1];
            String speed = datas[2];
            String passageState = datas[3];
            context.write(new Text(time),new Text(String.format("%s\t%s\t%s",texiId,speed,passageState)));
        }
    }
    public static class SecondTexiNetWorkReducer extends Reducer<Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int s1 = 0;
            int s2 = 0;
            HashMap<String, Integer> map = new HashMap<String,Integer>();
            for (Text value:values){
                String[] words = value.toString().split("\\t");
                String texiId = words[0];
                int speed = Integer.parseInt(words[1]);
                int passageState = Integer.parseInt(words[2]);
                if(passageState == 1) {
                    s1 += speed;
                    s2++;
                }else s1+=0;
                Integer integer = map.get(texiId);
                if (integer == null){
                    map.put(texiId,passageState);
                }else{
                    map.put(texiId, integer == 1 ? 1 : passageState);
                }

            }
            int carNums=map.size();
            int passageNum=0;
            Set<Map.Entry<String,Integer>> entries = map.entrySet();
            for (Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();iterator.hasNext();){
                Map.Entry<String, Integer> next = iterator.next();
                Integer value = next.getValue();
                if(value == 1){
                    passageNum++;
                }
            }
            context.write(key, new Text(String.format("%d\t%d\t%f\t%f", carNums, passageNum, (passageNum / (carNums * 1.0)),(s1/(s2*1.0)))));
        }
    }

    public static void main(String[] args)throws Exception {
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setJarByClass(SecondTexiNetWork.class);

        job.setMapperClass(SecondTexiNetWorkMapper.class);
        job.setReducerClass(SecondTexiNetWorkReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("c:\\handle_spg_out\\part-r-00000"));
        Path path = new Path("c:\\handle_sgp_out");
        FileSystem fs = FileSystem.get(config);
        if(fs.exists(path)){
            fs.delete(path,true);
        }
        FileOutputFormat.setOutputPath(job, path);
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
