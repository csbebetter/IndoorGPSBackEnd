package mysamples.JustTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ThreadTest2 {
    private static final FileWriter writer;
    static {
        try {
            writer = new FileWriter("D:\\Time-"+System.currentTimeMillis()+"-.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Thread t;

    public static void SaveDataStart() throws IOException {
        t = new Thread(()-> {
            try{
                if(!Thread.currentThread().isInterrupted()){
                    for (int i = 0; i < 50000000; i++) {
                        String record = i + "," +
                                (i + 1) + "," +
                                (i + 2) + "," +
                                (i + 3) + "," +
                                (i + 4) + "," +
                                (i + 5) + "," +
                                (i + 6) + "," +
                                (i + 7) + "\n";
                        writer.write(record);
                        System.out.println(i+"-_-");
                        Thread.sleep(100);
                    }
                }
            }
            catch (InterruptedException | IOException ignored) {}
        });
        t.start();
    }

    public static void SaveDataStop() throws IOException {
        t.interrupt();
        if(t.isInterrupted()){
            writer.close();
        }
        System.out.println("数据存储进程已终止");
    }

    public static void main(String[] args) throws IOException {
        SaveDataStart();

        Scanner s = new Scanner(System.in);
        s.nextLine();

        SaveDataStop();
    }
}
