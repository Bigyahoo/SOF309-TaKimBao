<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Không tìm thấy trang | Vstay.vn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background: #f8fafc; color: #1e293b; display: flex; align-items: center; justify-content: center; min-height: 100vh; margin: 0; }
        .error-card { background: white; padding: 48px 36px; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.06); text-align: center; max-width: 500px; width: 90%; }
        .error-code { font-size: 72px; font-weight: 800; color: #0062E0; line-height: 1; margin-bottom: 12px; }
        .error-title { font-size: 22px; font-weight: 700; margin-bottom: 8px; }
        .error-desc { color: #64748b; font-size: 15px; margin-bottom: 24px; }
        .btn-home { display: inline-block; background: #0062E0; color: white; padding: 12px 28px; border-radius: 10px; font-weight: 600; text-decoration: none; transition: 0.2s; }
        .btn-home:hover { background: #004bb5; }
    </style>
</head>
<body>
    <div class="error-card">
        <div class="error-code">404</div>
        <h1 class="error-title">Không tìm thấy trang yêu cầu</h1>
        <p class="error-desc">Trang hoặc khách sạn bạn đang tìm kiếm không tồn tại hoặc đã được chuyển đến địa chỉ mới.</p>
        <a href="${pageContext.request.contextPath}/" class="btn-home">Về Trang Chủ Vstay.vn</a>
    </div>
</body>
</html>
