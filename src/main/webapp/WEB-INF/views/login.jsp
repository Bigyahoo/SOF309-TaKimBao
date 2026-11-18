<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vstay.vn - Đăng nhập & Đăng ký Tài khoản Nghỉ dưỡng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        .alert-error {
            background: #fee2e2;
            border: 1px solid #ef4444;
            color: #b91c1c;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            font-weight: 500;
        }
        .alert-success {
            background: #dcfce7;
            border: 1px solid #22c55e;
            color: #15803d;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            font-weight: 500;
        }
    </style>
</head>
<body>

    <!-- Ambient background glows -->
    <div class="ambient-bg">
        <div class="ambient-blob-1"></div>
        <div class="ambient-blob-2"></div>
    </div>

    <!-- 1. AUTHENTICATION SCREEN -->
    <main class="app-screen active" id="auth-screen">
        <div class="vstay-auth-wrapper">

            <!-- LEFT PANEL: Brand Hero & Benefits -->
            <section class="brand-hero">
                <img id="hero-bg-img" src="https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80" alt="Vstay Resort" class="brand-hero-bg">
                <div class="brand-hero-overlay"></div>

                <div class="brand-hero-content">
                    <a href="${pageContext.request.contextPath}/" class="brand-logo-container">
                        <svg class="brand-logo-svg" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M20 30L42 70C43.5 73 46.5 73 48 70L80 25" stroke="#FFFFFF" stroke-width="12" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M48 25L75 45V70C75 73 72 75 69 75H55" stroke="#60A5FA" stroke-width="9" stroke-linecap="round"/>
                            <circle cx="58" cy="28" r="6" fill="#F59E0B"/>
                        </svg>
                        <span class="brand-logo-text">vstay<span>.vn</span></span>
                    </a>

                    <div class="hero-text-block">
                        <div class="hero-badge">
                            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
                            Trải nghiệm nghỉ dưỡng thượng lưu
                        </div>
                        <h1 class="hero-title">Khám phá 5,000+ Resort & Biệt thự đẳng cấp</h1>
                        <p class="hero-subtitle">Đăng nhập tài khoản Vstay để nhận ưu đãi độc quyền, tích điểm V-Points và trải nghiệm dịch vụ hỗ trợ VIP 24/7.</p>

                        <div class="hero-perks">
                            <div class="perk-item">
                                <div class="perk-icon-wrapper">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                                </div>
                                <span>Tích điểm V-Points nhận ngay tới 5% giá trị mỗi chuyến đi</span>
                            </div>
                            <div class="perk-item">
                                <div class="perk-icon-wrapper">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                                </div>
                                <span>Thành viên Genius VIP ưu đãi giá độc quyền tốt nhất</span>
                            </div>
                            <div class="perk-item">
                                <div class="perk-icon-wrapper">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                                </div>
                                <span>Tài khoản Demo có sẵn: admin@vstay.vn (pass: admin123)</span>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- RIGHT PANEL: Authentication Tabs & Forms -->
            <section class="auth-card-container">
                <div class="auth-card-header">
                    <a href="${pageContext.request.contextPath}/" class="back-home-btn" id="back-home-trigger">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                        Trang chủ
                    </a>
                    <div class="lang-selector">
                        <span>🇻🇳 VIE</span>
                    </div>
                </div>

                <!-- Navigation Tabs -->
                <div class="auth-tabs" role="tablist">
                    <button class="tab-btn ${tab != 'register' && tab != 'forgot' ? 'active' : ''}" id="tab-login" onclick="switchAuthTab('login')">Đăng nhập</button>
                    <button class="tab-btn ${tab == 'register' ? 'active' : ''}" id="tab-register" onclick="switchAuthTab('register')">Đăng ký</button>
                    <button class="tab-btn ${tab == 'forgot' ? 'active' : ''}" id="tab-forgot" onclick="switchAuthTab('forgot')">Quên mật khẩu</button>
                </div>

                <!-- LOGIN FORM PANEL -->
                <div class="auth-form-panel ${tab != 'register' && tab != 'forgot' ? 'active' : ''}" id="panel-login">
                    <div class="form-heading">
                        <h2 class="form-title">Chào mừng trở lại! 👋</h2>
                        <p class="form-desc">Nhập email tài khoản Vstay để tiếp tục chuyến hành trình</p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert-error">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="POST" id="form-login">
                        <input type="hidden" name="redirect" value="${param.redirect}">
                        <div class="form-group">
                            <label class="form-label" for="login-email">Email tài khoản</label>
                            <div class="input-group">
                                <input type="email" name="email" id="login-email" class="form-control" value="${email != null ? email : 'admin@vstay.vn'}" placeholder="example@vstay.vn" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="login-password">Mật khẩu</label>
                            <div class="input-group">
                                <input type="password" name="password" id="login-password" class="form-control" value="admin123" placeholder="••••••••" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                                <button type="button" class="toggle-password" onclick="togglePassVisibility('login-password')">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                                </button>
                            </div>
                        </div>

                        <div class="form-options">
                            <label class="checkbox-container">
                                <input type="checkbox" id="remember-me" checked>
                                <span class="custom-checkbox">
                                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                                </span>
                                Ghi nhớ tôi
                            </label>
                            <a href="javascript:void(0)" class="forgot-link" onclick="switchAuthTab('forgot')">Quên mật khẩu?</a>
                        </div>

                        <button type="submit" class="btn-submit" id="btn-login-submit">
                            <span>Đăng nhập ngay</span>
                        </button>
                    </form>

                    <div class="social-divider">
                        <span>Tài khoản kiểm thử nhanh</span>
                    </div>

                    <div style="background: #f8fafc; border: 1px dashed #cbd5e1; border-radius: 8px; padding: 12px; font-size: 13px; color: #475569; margin-top: 10px;">
                        <div><strong>Admin:</strong> admin@vstay.vn / admin123</div>
                        <div><strong>User:</strong> demo@vstay.vn / demo123</div>
                    </div>
                </div>

                <!-- REGISTER FORM PANEL -->
                <div class="auth-form-panel ${tab == 'register' ? 'active' : ''}" id="panel-register">
                    <div class="form-heading">
                        <h2 class="form-title">Tạo tài khoản mới ✨</h2>
                        <p class="form-desc">Đăng ký để trở thành VIP & nhận ngay 100 V-Points thưởng</p>
                    </div>

                    <c:if test="${not empty registerError}">
                        <div class="alert-error">${registerError}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="POST" id="form-register">
                        <div class="form-group">
                            <label class="form-label" for="reg-fullname">Họ và tên</label>
                            <div class="input-group">
                                <input type="text" name="fullName" id="reg-fullname" class="form-control" value="${fullName}" placeholder="Nguyễn Văn A" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="reg-email">Email liên hệ</label>
                            <div class="input-group">
                                <input type="email" name="email" id="reg-email" class="form-control" value="${email}" placeholder="yourname@gmail.com" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="reg-phone">Số điện thoại</label>
                            <div class="input-group">
                                <input type="tel" name="phone" id="reg-phone" class="form-control" value="${phone}" placeholder="0901234567" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="reg-password">Mật khẩu</label>
                            <div class="input-group">
                                <input type="password" name="password" id="reg-password" class="form-control" placeholder="Ít nhất 6 ký tự" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                                <button type="button" class="toggle-password" onclick="togglePassVisibility('reg-password')">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                                </button>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="reg-confirm-password">Xác nhận mật khẩu</label>
                            <div class="input-group">
                                <input type="password" name="confirmPassword" id="reg-confirm-password" class="form-control" placeholder="Nhập lại mật khẩu" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                                <button type="button" class="toggle-password" onclick="togglePassVisibility('reg-confirm-password')">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                                </button>
                            </div>
                        </div>

                        <div class="form-options">
                            <label class="checkbox-container">
                                <input type="checkbox" id="terms-agree" required checked>
                                <span class="custom-checkbox">
                                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                                </span>
                                Đồng ý với Điều khoản Vstay.vn
                            </label>
                        </div>

                        <button type="submit" class="btn-submit" id="btn-register-submit">
                            <span>Tạo tài khoản VIP</span>
                        </button>
                    </form>
                </div>

                <!-- FORGOT PASSWORD PANEL -->
                <div class="auth-form-panel ${tab == 'forgot' ? 'active' : ''}" id="panel-forgot">
                    <div class="form-heading">
                        <h2 class="form-title">Khôi phục mật khẩu 🔐</h2>
                        <p class="form-desc">Nhập email để nhận liên kết khôi phục mật khẩu tài khoản Vstay.</p>
                    </div>

                    <form onsubmit="alert('Liên kết khôi phục đã được gửi tới email của bạn.'); return false;">
                        <div class="form-group" style="margin-bottom: 2rem;">
                            <label class="form-label" for="forgot-email">Email tài khoản</label>
                            <div class="input-group">
                                <input type="email" id="forgot-email" class="form-control" placeholder="yourname@gmail.com" required>
                                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
                            </div>
                        </div>

                        <button type="submit" class="btn-submit">
                            <span>Gửi email khôi phục</span>
                        </button>
                    </form>
                </div>

                <div style="margin-top: 2rem; text-align: center; font-size: 0.8rem; color: var(--text-muted);">
                    &copy; 2026 Vstay.vn. Dịch vụ nghỉ dưỡng cao cấp hàng đầu Việt Nam.
                </div>
            </section>

        </div>
    </main>

    <script>
        function switchAuthTab(tabName) {
            document.querySelectorAll(".tab-btn").forEach(btn => btn.classList.remove("active"));
            document.querySelectorAll(".auth-form-panel").forEach(panel => panel.classList.remove("active"));

            const targetBtn = document.getElementById("tab-" + tabName);
            const targetPanel = document.getElementById("panel-" + tabName);

            if (targetBtn) targetBtn.classList.add("active");
            if (targetPanel) targetPanel.classList.add("active");
        }

        function togglePassVisibility(inputId) {
            const input = document.getElementById(inputId);
            if (input) {
                input.type = input.type === "password" ? "text" : "password";
            }
        }
    </script>
</body>
</html>
