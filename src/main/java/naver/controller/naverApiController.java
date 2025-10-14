package naver.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/naverView")
public class naverApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 필요한 데이터가 있으면 request.setAttribute("key", value);
		request.setAttribute("message", "JSP, Controller 연동 테스트");
		
		// JSP 포워딩
		request.getRequestDispatcher("/WEB-INF/naver/naverApiView.jsp").forward(request, response);
	}
	
}
