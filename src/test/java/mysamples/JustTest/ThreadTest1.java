package mysamples.JustTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThreadTest1 {
    public static List<String> list = new ArrayList<>();

    public static void cin(String lines) {
        list.add(lines);
    }

    public static String print() {
        return list.get(list.size() - 1);
    }

    public static void main(String[] args) throws IOException {
        list.add("开始演示"); //防止0-1=-1的情况
        File f = new File("D:\\Data.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        Thread t = new Thread(() -> {
            String data;
            while (true) {
                try {
                    //判断是否读到最后一行
                    if ((data = br.readLine()) == null) break;
                    try {
                        //向List添加String元素
                        cin(data);
                        //print()
                        System.out.println("->" + data);
                        //延时
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        //防止延时阻塞错误
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    //防止运行错误
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(print());
                    Thread.sleep(400);
                    if (!t.isAlive()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
