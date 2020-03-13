package com.wyu.hadoopapi.car;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by newuser on 2018/7/13.
 */
public class SecondCarNetWork {
    public static class SecondCarNetWorkMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lines = value.toString();
            String[] datas = lines.split("\\t");
            String carId = datas[0];
            String time = datas[1];
            String passageState = datas[2];
            context.write(new Text(time),new Text(String.format("%s\t%s",carId,passageState)));
        }
    }
    public static class SecondCarNetWorkReducer extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashMap<String, Integer> map = new HashMap<String,Integer>();
            for (Text value:values){
                String[] words = value.toString().split("\\t");
                String carId = words[0];
                int passageState = Integer.parseInt(words[1]);
                Integer integer = map.get(carId);
                if (integer == null){
                    map.put(carId,passageState);
                }else{
                    //integer++;
                    map.put(carId,integer == 1?1:passageState);
                }
            }
            int carNums=map.size();
            int passageNum=0;
            Set<Map.Entry<String, Integer>> entries = map.entrySet();
            for (Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();iterator.hasNext();){
                Map.Entry<String, Integer> next = iterator.next();
                Integer value = next.getValue();
                if(value == 1){
                    passageNum++;
                }
            }
            context.write(key,new Text(String.format("%d\t%d\t%f",carNums,passageNum,(passageNum/(carNums*1.0)))));
        }
    }

    public static void main(String[] args)throws Exception {
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setJarByClass(SecondCarNetWork.class);

        job.setMapperClass(SecondCarNetWorkMapper.class);
        job.setReducerClass(SecondCarNetWorkReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("c:\\handle_gps_out\\part-r-00000"));
        Path path = new Path("c:\\handle_pgs_out");
        FileSystem fs = FileSystem.get(config);
        if(fs.exists(path)){
            fs.delete(path,true);
        }
        FileOutputFormat.setOutputPath(job,path);
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
