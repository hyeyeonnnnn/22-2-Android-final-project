package ddwu.mobile.finalproject.ma01_20200554;
import java.net.*;
import  java.io.*;
public class NaverAPI {
    //결과 값을 받기 위해서 return
    public static String main(String query,int start) {
        //static: 클래스 생성없이 메서드 사용가능
        String clientId = "8qLMI3AKobvitLIhc2iE";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "DNt5yhSa0A";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(query, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/blog?query="+ text; // json 결과
            apiURL += "&start=" + start;

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}