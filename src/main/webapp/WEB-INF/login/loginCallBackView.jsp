<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 결과</title>
</head>
<body style="font-family: Arial; text-align: center; margin-top: 100px;">

    <h2>로그인 결과</h2>
    <hr>

    <c:choose>
        <c:when test="${empty error}">
            <p><b>code:</b> ${code}</p>
            <p><b>state:</b> ${state}</p>
            <br>
            <h3>${state} 사용자 정보</h3>

            <c:if test="${not empty id}">
                <p><b>ID:</b> ${id}</p>
            </c:if>
            <c:if test="${not empty name}">
                <p><b>이름:</b> ${name}</p>
            </c:if>
            <c:if test="${not empty gender}">
                <p><b>성별:</b> ${gender}</p>
            </c:if>
            <c:if test="${not empty birthday}">
                <p><b>생일:</b> ${birthday}</p>
            </c:if>
            <c:if test="${not empty email}">
                <p><b>이메일:</b> ${email}</p>
            </c:if>

            <p style="color: green;">로그인 성공</p>
        </c:when>
        <c:otherwise>
            <p style="color: red;">로그인 실패</p>
            <p>${error}</p>
        </c:otherwise>
    </c:choose>

</body>
</html>
