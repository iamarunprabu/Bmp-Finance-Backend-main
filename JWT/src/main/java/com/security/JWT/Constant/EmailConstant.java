package com.security.JWT.Constant;

public class EmailConstant {

	public static final String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtp";
//    public static final String USERNAME = "javadeveloper.ja@gmail.com";
//    public static final String PASSWORD = "pexp dcgi sbuu aqna";
//    public static final String FROM_EMAIL = "support@getarrays.com";
    public static final String CC_EMAIL = "";
    public static final String EMAIL_SUBJECT = "BMP Login - New Password";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
//    public static final int DEFAULT_PORT = 587;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    
    public static final String USERNAME = System.getenv("MAIL_USERNAME") !=null?System.getenv("MAIL_USERNAME"):"javadeveloper.ja@gmail.com";
    public static final String PASSWORD = System.getenv("MAIL_PASSWORD")!=null?System.getenv("MAIL_PASSWORD"):"pexp dcgi sbuu aqna";
    public static final String FROM_EMAIL=System.getenv("MAIL_FROM")!=null?System.getenv("MAIL_FROM"):"javadeveloper.ja@gmail.com";
    public static final int DEFAULT_PORT = System.getenv("MAIL_PORT")!=null?Integer.parseInt(System.getenv("MAIL_PORT")):587;
    
    
    
}


