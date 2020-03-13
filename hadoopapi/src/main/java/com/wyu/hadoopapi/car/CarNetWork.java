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

/**
 * Created by newuser on 2018/7/13.
 */
public class CarNetWork {
    public static class CarNetWorkMapper extends Mapper<LongWritable,Text,Car,Text>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lines = value.toString();
            String[] datas = lines.split("\\t");
            //if(datas.length==7){
                Car car = new Car(datas[0], TimeUtils.getTime(datas[1]));
                context.write(car,new Text(datas[datas.length-1]));
            //}
        }
    }
    public static class CarNetWorkReducer extends Reducer<Car,Text,Text,Text>{
        @Override
        protected void reduce(Car car, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for(Text value:values){
                context.write(new Text(String.format("%s\t%s",car.getVenicleId(),car.getTime())),new Text(value));
            }
        }
    }
    public static void main(String[] args)throws Exception{
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setJarByClass(CarNetWork.class);

        job.setMapperClass(CarNetWorkMapper.class);
        job.setReducerClass(CarNetWorkReducer.class);

        job.setMapOutputKeyClass(Car.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job,new Path("c:\\gps.csv"));
        Path path = new Path("c:/handle_gps_out");
        FileSystem fs = FileSystem.get(config);
        if(fs.exists(path)){
            fs.delete(path,true);
        }
        FileOutputFormat.setOutputPath(job,path);
        System.exit(job.waitForCompletion(true)?0:1);



    }
}
