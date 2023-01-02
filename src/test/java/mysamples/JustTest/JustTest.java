package mysamples.JustTest;

import com.alibaba.fastjson2.JSONObject;

import java.util.Arrays;
import java.util.List;

public class JustTest {
    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        //string
        object.put("string", "string");
        //int
        object.put("int", 2);
        //boolean
        object.put("boolean", true);
        //array
        List<Integer> integers = Arrays.asList(1, 2, 3);
        object.put("list", integers);
        //null
        object.put("null", null);

        System.out.println(object);
    }
}
