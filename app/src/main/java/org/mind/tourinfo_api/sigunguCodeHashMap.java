package org.mind.tourinfo_api;

import android.util.Log;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class sigunguCodeHashMap extends AsyncTask<String, Void, String> {
    HashMap<String, String> regionCodeHashMap = new HashMap<>();
    HashMap<String, String> sigunguCodeMap = new HashMap<>();

    final private String searchType = "areaCode";
    private String option = null;
    private String urlText;
    private String str, receiveMsg;

    public sigunguCodeHashMap() {

    }

    public String getSearchType(){
        return this.searchType;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption(){
        return this.option;
    }

    public void setURL() {
        this.urlText = new makeURL(this).integrateURL();
    }

    public String getURL() {
        return urlText;
    }

    public void regionCode() {
        setOption("null");
    }

    //    public void getSearchType(){
//        return searchType;
//    }
    @Override
    protected String doInBackground(String... params) {
        try {
//            for (int i = 0; i < arr.length; i++) {
//                option = "areaCode=" + arr[i];
//                URL url = new URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/"
//                        + searchType +
//                        "?ServiceKey=" + ServiceKey
//                        + "&" + option
//                        + "&MobileOS=ETC&MobileApp=TestApp&_type=json");
//
//                Log.i("Print testing ::: ", option);
            makeURL ob1 = new makeURL(this);
            URL url = new URL(getURL());
            Log.i("URL test ::: ", url.toString());
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            connect.setRequestProperty("Service-Name", "국문 관광정보 서비스");
            connect.setRequestProperty("Service-Type", "REST");
            connect.setRequestProperty("Content-Type", "application/json");
            connect.setRequestProperty("Response-Time", "0");

            //System.out.println("왜안됑론ㅁ;ㅣㅏㅇ러마;럼;ㅣㄴ아");
            if (connect.getResponseCode() == connect.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(connect.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg test ::::: ", receiveMsg);

                reader.close();
            } else {
                Log.i("result is ", connect.getResponseCode() + " error");
            }

            //}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }

    public void dataParser(String jsonString) {
        String code = null, name = null;
        //String item = null, numOfRows = null, pageNo = null, totalCount = null;

        try {
            JSONObject jObjectT = new JSONObject(jsonString);
            jObjectT = jObjectT.getJSONObject("response");
            jObjectT = jObjectT.getJSONObject("body");
            jObjectT = jObjectT.getJSONObject("items");

            JSONArray jarray = jObjectT.getJSONArray("item");

            for (int i = 0; i < jarray.length(); i++) {
                //System.out.println("jarray length test :: " + jarray.length());
                JSONObject jObject = jarray.getJSONObject(i);

                //System.out.println(jObject);
                code = jObject.optString("code");
                name = jObject.optString("name");

                sigunguCodeMap.put(name, code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String main() {
        String resultText = null, resultData = null;

        regionCode();
        setURL();

        try {
            //System.out.println("Array length test ::: " + urlText);
            // Log.i("OPOTION Text ::::", option);

            resultData = .execute().get();
            dataParser((resultData));

            Iterator<String> iterator = sigunguCodeMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultText += "Code : " + key + "      value : " + sigunguCodeMap.get(key) + "\n";
                Log.i("CODE test :::", sigunguCodeMap.get(key) + key);
            }
            //  Log.i("Print testing ::: ",option);
            //}


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultText;
    }
}

