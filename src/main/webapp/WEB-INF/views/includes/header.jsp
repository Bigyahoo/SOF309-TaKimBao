<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Header Navigation -->
<header class="header">
    <div class="header-container">
        <div class="header-left">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Vstay.vn Logo" class="logo-img" onerror="this.src='https://placehold.co/120x40/0062E0/FFF?text=Vstay.vn'">
            </a>
            <nav class="main-nav">
                <a href="${pageContext.request.contextPath}/" class="nav-item">Khách sạn & Resort</a>
                <a href="${pageContext.request.contextPath}/search" class="nav-item">Điểm đến</a>
                <a href="${pageContext.request.contextPath}/search?deal=flashsale" class="nav-item hot-deal">
                    <span class="badge-dot"></span>
                    Ưu đãi hot
                </a>
                <a href="${pageContext.request.contextPath}/about" class="nav-item">Về chúng tôi</a>
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin" class="nav-item" style="color:#FF6B00; font-weight:700;">Quản trị CRUD</a>
                </c:if>
            </nav>
        </div>

        <div class="header-right">
            <div class="header-utility">
                <button class="utility-btn" type="button">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/></svg>
                    <span>VN | VND</span>
                </button>
                <a href="${pageContext.request.contextPath}/seed" class="utility-link" title="Nạp 40 khách sạn mẫu vào cơ sở dữ liệu">Nạp Dữ Liệu</a>
                <a href="${pageContext.request.contextPath}/account#favorites" class="icon-btn" aria-label="Favorites" title="Khách sạn yêu thích">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l8.72-8.72 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                </a>
            </div>

            <!-- Dynamic Auth User Widget -->
            <div id="authAccountContainer">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div style="display:flex; align-items:center; gap:8px;">
                            <a href="${pageContext.request.contextPath}/account" class="user-profile-widget" style="text-decoration:none; color:inherit; display:flex; align-items:center; gap:8px; background:#E9EDFF; padding:4px 12px 4px 6px; border-radius:30px;">
                                <img src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=80&auto=format&fit=crop" id="headerAvatar" alt="Avatar" class="avatar" style="width:32px; height:32px; border-radius:50%; object-fit:cover;">
                                <div class="user-info">
                                    <span id="headerUserName" class="user-name" style="font-size:13px; font-weight:700; display:block;">${sessionScope.user.fullName}</span>
                                    <span id="headerUserBadge" class="user-badge" style="font-size:10px; font-weight:800; color:#B8860B;">${sessionScope.user.vipTier} 👑</span>
                                </div>
                            </a>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-outline-sm" style="border: 1px solid #cbd5e1; color: #64748b; padding: 6px 10px; border-radius: 20px; font-size: 11px; text-decoration: none;" title="Đăng xuất">Thoát</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn-outline-sm" style="border: 1px solid #0062E0; color: #0062E0; padding: 6px 14px; border-radius: 20px; font-weight: 700; font-size: 12px; text-decoration: none;">
                            Đăng nhập
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>

            <a href="${pageContext.request.contextPath}/search" class="btn-primary btn-sm">Đặt phòng</a>

            <!-- Dark Mode Toggle & Back To Top Buttons -->
            <button id="btnDarkMode" class="icon-btn header-action-btn" aria-label="Toggle Dark Mode" type="button" title="Chế độ tối/sáng">
                🌙
            </button>
            <button id="btnBackToTop" class="icon-btn header-action-btn" aria-label="Back to Top" type="button" title="Lên đầu trang">
                ⬆️
            </button>
        </div>
    </div>
</header>
