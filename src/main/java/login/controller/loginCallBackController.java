package login.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet("/loginCallBackView")
public class loginCallBackController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private static final String CLIENT_ID 		= "rmJ2NmRjykTGFlD75Tpe";
	private static final String CLIENT_SECRET  	= "aRtwYZFCjI";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// rediect_url -> code/state 전달
		String code  = request.getParameter("code");
		String state = request.getParameter("state");
		
		// 기본값으로 일단 null
		String name 		= "null";
		String gender  		= "null";
		String birthday 	= "null";
		String id  			= "null";
		String errorMessage =  null;
		
		// 받아온 값이 null이 아닐때
        if (code != null && state != null) {
            try {
                // Access Token 요청
                String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
                        + "&client_id=" + CLIENT_ID
                        + "&client_secret=" + CLIENT_SECRET
                        + "&code=" + code
                        + "&state=" + state;

                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader br;
                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                }

                String inputLine;
                StringBuilder res = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    res.append(inputLine);
                }
                br.close();
                
                // JSON 파싱
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(res.toString());
                String accessToken = (String) json.get("access_token");

                // 사용자 정보 요청
                if (accessToken != null) {
                    URL apiUrl = new URL("https://openapi.naver.com/v1/nid/me");
                    HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
                    apiCon.setRequestMethod("GET");
                    apiCon.setRequestProperty("Authorization", "Bearer " + accessToken);

                    BufferedReader apiBr = new BufferedReader(new InputStreamReader(apiCon.getInputStream(), "UTF-8"));
                    StringBuilder apiRes = new StringBuilder();
                    while ((inputLine = apiBr.readLine()) != null) {
                        apiRes.append(inputLine);
                    }
                    apiBr.close();

                    System.out.println("User Info Response: " + apiRes.toString());

                    JSONObject userJson = (JSONObject) parser.parse(apiRes.toString());
                    System.out.println("Parsed userJson: " + userJson.toJSONString());

                    JSONObject responseJson = (JSONObject) userJson.get("response");
                    if (responseJson == null) {
                        System.out.println("responseJson is null! API 호출 실패 가능");
                    } else {
                        System.out.println("id=" + responseJson.get("id"));
                        System.out.println("name=" + responseJson.get("name"));
                    }

                    id = (String) responseJson.get("id");
                    name = (String) responseJson.get("name");
                    gender = (String) responseJson.get("gender");
                    birthday = (String) responseJson.get("birthday");
                } else {
                    errorMessage = "Access Token 발급 실패";
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "로그인 처리 중 오류 발생: " + e.getMessage();
            }
        } else {
            errorMessage = "code 또는 state 값이 없습니다.";
        }
		
        // JSP에 데이터 전달
        request.setAttribute("code", code);
        request.setAttribute("state", state);
        request.setAttribute("id", id);
        request.setAttribute("name", name);
        request.setAttribute("gender", gender);
        request.setAttribute("birthday", birthday);
        request.setAttribute("error", errorMessage);
		
		// 결과 JSP로 포워딩
		request.getRequestDispatcher("/WEB-INF/login/loginCallBackView.jsp").forward(request, response);

	}
}
