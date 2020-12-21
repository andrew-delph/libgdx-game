package org.example;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Write {
    public static void main(String[] args) {
        TestProto.AndrewMessage.Builder andrewMsg = TestProto.AndrewMessage.newBuilder();

        andrewMsg.setId(2);

        andrewMsg.setData("This is Andrew  Message.");

        andrewMsg.addDataList("this is the first item");

        andrewMsg.addDataList("this is the second item");

        andrewMsg.setCount(99);

        FileOutputStream output = null;

        try {
            output = new FileOutputStream("/Users/adelph/git/learning-java/yolo");
            andrewMsg.build().writeTo(output);
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        TestProto.AndrewMessage read = null;
        try {
            read = TestProto.AndrewMessage.parseFrom(new FileInputStream("/Users/adelph/git/learning-java/yolo"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("about to print");
        System.out.println(read);

    }
}
