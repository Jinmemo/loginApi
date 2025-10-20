package login.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
public class loginCallBackController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Naver
    private static final String NAVER_CLIENT_ID 	= "rmJ2NmRjykTGFlD75Tpe";
    private static final String NAVER_CLIENT_SECRET = "";

    // Kakao
    private static final String KAKAO_CLIENT_ID = "c731e5a550da589cad0bb73e13fc2f5b";
    private static final String KAKAO_CLIENT_SECRET = "";

    // Google
    private static final String GOOGLE_CLIENT_ID = "524802389903-6chv0ahs8hmh6tq3a7kmnok5hke67krg.apps.googleusercontent.com";
    private static final String GOOGLE_CLIENT_SECRET = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        String state = request.getParameter("state");

        String id = null;
        String name = null;
        String gender = null;
        String birthday = null;
        String errorMessage = null;

        if (code == null || state == null) {
            errorMessage = "code 또는 state 값이 없습니다.";
        } else {
            try {
                JSONParser parser = new JSONParser();
                String accessToken = null;

                // 1. 플랫폼별 Access Token 요청
                switch (state) {
                    case "naver": {
                        String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
                                + "&client_id=" + NAVER_CLIENT_ID
                                + "&client_secret=" + NAVER_CLIENT_SECRET
                                + "&code=" + code
                                + "&state=" + state;

                        accessToken = getAccessToken(apiURL);
                        if (accessToken != null)
                            parseNaverUserInfo(accessToken, request, parser);
                        break;
                    }

                    case "kakao": {
                        String tokenURL = "https://kauth.kakao.com/oauth/token";
                        String params = "grant_type=authorization_code"
                                + "&client_id=" + KAKAO_CLIENT_ID
                                + "&client_secret=" + KAKAO_CLIENT_SECRET
                                + "&redirect_uri=http://localhost:8090/loginApi/loginCallBackView"
                                + "&code=" + code;

                        accessToken = postAccessToken(tokenURL, params);
                        if (accessToken != null)
                            parseKakaoUserInfo(accessToken, request, parser);
                        break;
                    }

                    case "google": {
                        String tokenURL = "https://oauth2.googleapis.com/token";
                        String params = "code=" + code
                                + "&client_id=" + GOOGLE_CLIENT_ID
                                + "&client_secret=" + GOOGLE_CLIENT_SECRET
                                + "&redirect_uri=http://localhost:8090/loginApi/loginCallBackView"
                                + "&grant_type=authorization_code";

                        accessToken = postAccessToken(tokenURL, params);
                        if (accessToken != null)
                            parseGoogleUserInfo(accessToken, request, parser);
                        break;
                    }

                    default:
                        errorMessage = "지원하지 않는 로그인 유형입니다.";
                }

            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "로그인 처리 중 오류 발생: " + e.getMessage();
            }
        }

        // 결과 전달
        request.setAttribute("code", code);
        request.setAttribute("state", state);
        request.setAttribute("error", errorMessage);

        // 결과 JSP로 포워딩
        request.getRequestDispatcher("/WEB-INF/login/loginCallBackView.jsp").forward(request, response);
    }

    // 공통 GET 요청
    private String getAccessToken(String apiURL) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(apiURL).openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        try {
            JSONObject json = (JSONObject) new JSONParser().parse(sb.toString());
            return (String) json.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 공통 POST 요청
    private String postAccessToken(String tokenURL, String params) throws IOException {
        URL url = new URL(tokenURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(params.getBytes("UTF-8"));

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        try {
            JSONObject json = (JSONObject) new JSONParser().parse(sb.toString());
            return (String) json.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Naver 사용자 정보 파싱
    private void parseNaverUserInfo(String token, HttpServletRequest request, JSONParser parser) throws Exception {
        URL url = new URL("https://openapi.naver.com/v1/nid/me");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        JSONObject obj = (JSONObject) parser.parse(sb.toString());
        JSONObject res = (JSONObject) obj.get("response");

        request.setAttribute("id", res.get("id"));
        request.setAttribute("name", res.get("name"));
        request.setAttribute("gender", res.get("gender"));
        request.setAttribute("birthday", res.get("birthday"));
    }

    // Kakao 사용자 정보 파싱
    private void parseKakaoUserInfo(String token, HttpServletRequest request, JSONParser parser) throws Exception {
        URL url = new URL("https://kapi.kakao.com/v2/user/me");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        JSONObject obj = (JSONObject) parser.parse(sb.toString());
        JSONObject kakaoAccount = (JSONObject) obj.get("kakao_account");
        JSONObject profile = (JSONObject) kakaoAccount.get("profile");

        request.setAttribute("id", obj.get("id"));
        request.setAttribute("name", profile.get("nickname"));
        request.setAttribute("gender", kakaoAccount.get("gender"));
        request.setAttribute("birthday", kakaoAccount.get("birthday"));
    }

    // Google 사용자 정보 파싱
    private void parseGoogleUserInfo(String token, HttpServletRequest request, JSONParser parser) throws Exception {
        URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        JSONObject obj = (JSONObject) parser.parse(sb.toString());

        request.setAttribute("id", obj.get("sub"));
        request.setAttribute("name", obj.get("name"));
        request.setAttribute("gender", obj.get("gender"));
        request.setAttribute("birthday", obj.get("birthdate"));
        request.setAttribute("email", obj.get("email"));
    }
}
