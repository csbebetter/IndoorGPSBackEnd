package mysamples.JustTest;

import com.alibaba.fastjson2.JSONObject;

public class test {
    private static tes t =new tes();
    public static void main(String[] args){
        t.test();
        System.out.println(t.abs);

        JSONObject object = new JSONObject();
        object.put("e","f");
        JSONObject detail = object.getJSONObject("detail");
        System.out.println(detail==null);
        System.out.println(object.isEmpty());
    }
}

