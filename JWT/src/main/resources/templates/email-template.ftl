<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            margin: 0;
            padding: 0;
        }
        .container {
            background-color: #fff;
            margin: 40px auto;
            width: 600px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .header {
            background-color: #007bff;
            padding: 20px;
            color: white;
            text-align: center;
            border-radius: 10px 10px 0 0;
        }
        .body {
            padding: 30px;
            text-align: center;
        }
        .password {
            background-color: #f0f0f0;
            padding: 10px 20px;
            border-radius: 8px;
            display: inline-block;
            color: #007bff;
            font-size: 18px;
        }
        .footer {
            background-color: #007bff;
            color: white;
            text-align: center;
            padding: 15px;
            border-radius: 0 0 10px 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Welcome, ${firstName}!</h2>
        </div>
        <div class="body">
            <p>Your account has been created successfully.</p>
            <p>Here is your password:</p>
            <div class="password">${password}</div>
            <p>Please keep it safe and do not share it with anyone.</p>
        </div>
        <div class="footer">
            <p>© 2025 Your Company. All rights reserved.</p>
        </div>
    </div>
</body>
</html>
