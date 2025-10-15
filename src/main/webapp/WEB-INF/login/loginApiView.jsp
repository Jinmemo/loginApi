<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>로그인 API</title>
</head>
	<body style="width: 1200px; height: 1200px; margin: 0; font-family: Arial, sans-serif;">
		<div style="display: flex; justify-content: center; align-items: center; height: 100%;">
			<div style="display: flex; flex-direction: column; align-items: center; gap: 20px;">
				
				<!-- Naver -->
				<button id="naverButton"
					onclick="location.href='https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=rmJ2NmRjykTGFlD75Tpe&redirect_uri=http://localhost:8090/loginApi/loginCallBackView&state=testState'"
					style="width: 280px; height: 55px; background-color: #03C75A; border: none; color: white; font-size: 17px; border-radius: 8px; cursor: pointer; display: flex; align-items: center; justify-content: center;">
					<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="white" style="margin-right:10px;">
						<path d="M3 3h6.586L15 11.586V3h6v18h-6.586L9 12.414V21H3z"/>
					</svg>
					Naver 로그인
				</button>

	
				<!-- Kakao -->
				<button id="kakaoButton" 
					style="width: 280px; height: 55px; background-color: #FEE500; border: none; color: #3C1E1E; font-size: 17px; border-radius: 8px; cursor: pointer; display: flex; align-items: center; justify-content: center;">
					<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="#3C1E1E" style="margin-right:10px;">
						<path d="M12 3C6.477 3 2 6.917 2 11.727c0 2.84 1.737 5.352 4.404 6.958L5.5 21.5l3.517-1.98c.952.232 1.969.358 3.013.358 5.523 0 10-3.918 10-8.727C22 6.917 17.523 3 12 3z"/>
					</svg>
					Kakao 로그인
				</button>
	
				<!-- Google -->
				<button id="googleButton" 
					style="width: 280px; height: 55px; background-color: white; border: 1px solid #ccc; color: #555; font-size: 17px; border-radius: 8px; cursor: pointer; display: flex; align-items: center; justify-content: center;">
					<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 48 48" style="margin-right:10px;">
						<path fill="#EA4335" d="M24 9.5c3.54 0 6.38 1.46 8.31 2.68l6.13-5.94C34.69 3.14 29.77 1 24 1 14.78 1 7.07 6.92 3.69 14.88l7.58 5.89C12.7 13.11 17.91 9.5 24 9.5z"/>
						<path fill="#34A853" d="M46.5 24.5c0-1.54-.14-3.02-.39-4.47H24v8.48h12.68c-.56 2.85-2.25 5.25-4.77 6.87l7.26 5.62C43.54 37.06 46.5 31.2 46.5 24.5z"/>
						<path fill="#FBBC05" d="M11.27 28.56A14.47 14.47 0 0 1 10 24c0-1.56.27-3.06.75-4.47l-7.58-5.89A23.97 23.97 0 0 0 0 24c0 3.86.92 7.51 2.52 10.79l8.75-6.23z"/>
						<path fill="#4285F4" d="M24 48c6.48 0 11.92-2.13 15.89-5.8l-7.26-5.62c-2.02 1.36-4.6 2.17-8.63 2.17-6.09 0-11.3-3.61-13.47-8.97l-8.75 6.23C7.07 41.08 14.78 48 24 48z"/>
					</svg>
					Google 로그인
				</button>
	
			</div>
		</div>
	</body>
</html>