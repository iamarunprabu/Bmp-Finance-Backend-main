package com.security.JWT.Constant;

public final class  SecurityConstant {
	
	public static final long EXPIRE_TIME =  432_000_000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String JWT_TOKEN_HEADER = "Jwt-Token";
	public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot be Verified";
	public static final String GET_ARRAYS_LLC = "Get Arrays, LLC";
	public static final String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
	public static final String AUTHORITIES = "authorities";
	public static final String FORBIDDEN_MESSAGE = "You Need to Log-in to access this Page";
	public static final String ACCESS_DENIED_MESSAGE = "You do not have Permission to access this page";
	public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
	public static final String[] PUBLIC_URLS = {"/user/login","/user/logout", "/user/register", "user/resetpassword/**", "/user/image/**","/api/loan/next-loan_number","/api/loan/plan/all","/api/investment/save"};
	public static String USER_LOGGED_OUT_SUCCESSFULLY = "user_logged_out_successfully";
	public static String LOAN_SUCCESS = "Loan Request Create Successfully";
	//public static final String[] PUBLIC_URLS = {"/**"};

}
