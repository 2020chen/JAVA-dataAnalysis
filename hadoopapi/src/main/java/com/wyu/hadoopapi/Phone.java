package com.wyu.hadoopapi;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by newuser on 2018/7/12.
 */
public class Phone implements Writable {
    private int upFlow;
    private int downFlow;

    public Phone() {
    }

    public int getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(int upFlow) {
        this.upFlow = upFlow;
    }

    public int getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(int downFlow) {
        this.downFlow = downFlow;
    }

    public Phone(int downFlow, int upFlow) {
        this.downFlow = downFlow;
        this.upFlow = upFlow;
    }

    @Override
    public String toString() {
        return String.format("%d\t%d",upFlow,downFlow);
    }

    public void write(DataOutput dataOutput) throws IOException{
        dataOutput.writeInt(upFlow);
        dataOutput.writeInt(downFlow);
    }

    public void readFields(DataInput dataInput) throws IOException{
        upFlow = dataInput.readInt();
        downFlow = dataInput.readInt();
    }
}
