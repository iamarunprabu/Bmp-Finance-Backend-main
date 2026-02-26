<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Password Reset Successful</title>
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #f3f7f3;
            margin: 0;
            padding: 0;
        }
        .container {
            background-color: #ffffff;
            margin: 40px auto;
            width: 600px;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.08);
            overflow: hidden;
        }
        .header {
            background-color: #28a745; /* Green theme */
            padding: 25px;
            color: white;
            text-align: center;
        }
        .header h2 {
            margin: 0;
            font-size: 24px;
        }
        .body {
            padding: 30px;
            text-align: center;
            color: #333;
        }
        .message {
            font-size: 16px;
            margin-bottom: 20px;
            line-height: 1.6;
        }
        .password-box {
            background-color: #eafbe8;
            border: 2px solid #28a745;
            padding: 12px 25px;
            border-radius: 8px;
            display: inline-block;
            color: #155724;
            font-size: 18px;
            font-weight: bold;
        }
        .button {
            display: inline-block;
            margin-top: 25px;
            background-color: #28a745;
            color: white;
            padding: 12px 30px;
            border-radius: 25px;
            text-decoration: none;
            font-weight: bold;
        }
        .button:hover {
            background-color: #218838;
        }
        .footer {
            background-color: #28a745;
            color: white;
            text-align: center;
            padding: 15px;
            font-size: 14px;
        }
        @media (max-width: 600px) {
            .container {
                width: 90%;
            }
            .body {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Password Reset Successful ✅</h2>
        </div>
        <div class="body">
            <p class="message">Hi <strong>${firstName}</strong>,</p>
            <p class="message">
                Your password has been successfully reset.<br>
                Here is your new temporary password:
            </p>
            <div class="password-box">${password}</div>
            <p class="message">
                Please keep it Safe.
            </p>
            <a href="${loginUrl}" class="reset-button">Login Now</a>
        </div>
        <div class="footer">
            <p>© 2025 Your Company | All Rights Reserved</p>
        </div>
    </div>
</body>
</html>
