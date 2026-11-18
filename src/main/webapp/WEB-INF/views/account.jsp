<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tài khoản cá nhân & Lịch sử đặt phòng | Vstay.vn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        .booking-item-card {
            background: white;
            border-radius: 12px;
            border: 1px solid var(--border-color, #e2e8f0);
            padding: 20px;
            margin-bottom: 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }
        .booking-item-card:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }
    </style>
</head>
<body>

    <jsp:include page="includes/header.jsp"/>

    <!-- Main Account Container -->
    <main class="account-main">
        <div class="account-container">

            <!-- Top Profile Hero Banner -->
            <div class="profile-hero-card">
                <div class="hero-left-profile">
                    <img src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=160&auto=format&fit=crop" alt="Avatar" class="hero-avatar">
                    <div>
                        <div class="hero-name-row">
                            <h1 id="userNameDisplay">${user.fullName}</h1>
                            <span class="verified-badge">✓ Đã xác thực KYC</span>
                        </div>
                        <p class="hero-subtext">Email: ${user.email} · Số điện thoại: ${not empty user.phone ? user.phone : 'Chưa cập nhật'} · Hạng: <strong style="color: #B8860B;">VIP ${user.vipTier.toUpperCase()} 👑</strong></p>
                    </div>
                </div>
                <div class="hero-stats-row">
                    <div class="stat-box">
                        <span class="stat-val"><fmt:formatNumber value="${user.VPoints != null ? user.VPoints : 0}" type="number"/> pts</span>
                        <span class="stat-lbl">Điểm thưởng V-Points</span>
                    </div>
                    <div class="stat-box">
                        <span class="stat-val" id="totalBookingsCount">${bookings != null ? bookings.size() : 0}</span>
                        <span class="stat-lbl">Đã đặt phòng</span>
                    </div>
                    <div class="stat-box">
                        <span class="stat-val">${favorites != null ? favorites.size() : 0}</span>
                        <span class="stat-lbl">Khách sạn yêu thích</span>
                    </div>
                </div>
            </div>

            <!-- Content Grid: Sidebar + Main Tabs -->
            <div class="account-grid">
                
                <!-- Left Sidebar Navigation -->
                <aside class="account-sidebar">
                    <div class="sidebar-menu">
                        <button class="menu-item active" data-tab="bookings" onclick="switchAccountTab('bookings')">
                            <span>🏨 Lịch sử đặt phòng</span>
                            <span class="badge-count" id="sidebarBookingCount">${bookings != null ? bookings.size() : 0}</span>
                        </button>
                        <button class="menu-item" data-tab="favorites" onclick="switchAccountTab('favorites')">
                            <span>❤️ Khách sạn yêu thích</span>
                            <span class="badge-count" id="sidebarFavCount">${favorites != null ? favorites.size() : 0}</span>
                        </button>
                        <button class="menu-item" data-tab="membership" onclick="switchAccountTab('membership')">
                            <span>👑 Thẻ thành viên & Đặc quyền</span>
                        </button>
                        <button class="menu-item" data-tab="settings" onclick="switchAccountTab('settings')">
                            <span>⚙️ Cài đặt tài khoản</span>
                        </button>
                        <a href="${pageContext.request.contextPath}/logout" class="menu-item logout-btn" style="text-decoration:none; text-align:left;">
                            <span>🚪 Đăng xuất tài khoản</span>
                        </a>
                    </div>

                    <!-- VIP Card Box in Sidebar -->
                    <div class="sidebar-vip-box">
                        <div class="vip-top">
                            <span class="vip-tag">CẤP ĐỘ VIP · ${user.vipTier.toUpperCase()}</span>
                            <span class="vip-pts">${user.VPoints != null ? user.VPoints : 0} điểm</span>
                        </div>
                        <p>Giảm ngay 15% tất cả resort 5 sao tại Đà Nẵng, Phú Quốc & Nha Trang.</p>
                        <div class="vip-progress">
                            <div class="progress-bar" style="width: 75%;"></div>
                        </div>
                        <span class="vip-next">Tích luỹ điểm đổi đêm nghỉ miễn phí</span>
                    </div>
                </aside>

                <!-- Right Main Content Area -->
                <div class="account-content-area">

                    <!-- TAB 1: BOOKING HISTORY -->
                    <div class="tab-pane active" id="pane-bookings">
                        <div class="pane-header">
                            <h2>Lịch sử đặt phòng</h2>
                            <p>Quản lý tất cả các đơn đặt phòng khách sạn & resort của bạn</p>
                        </div>

                        <!-- Bookings List -->
                        <div class="bookings-list" id="userBookingsList">
                            <c:choose>
                                <c:when test="${not empty bookings}">
                                    <c:forEach var="b" items="${bookings}">
                                        <div class="booking-item-card">
                                            <div>
                                                <div style="display:flex; align-items:center; gap:8px; margin-bottom:6px;">
                                                    <span style="background:#e0f2fe; color:#0369a1; padding:3px 8px; border-radius:4px; font-size:12px; font-weight:700;">#${b.bookingCode}</span>
                                                    <span style="background:#dcfce7; color:#15803d; padding:3px 8px; border-radius:4px; font-size:12px; font-weight:700;">${b.paymentStatus}</span>
                                                </div>
                                                <h3 style="font-size:17px; font-weight:700; margin-bottom:4px; color:#0f172a;">${b.hotelName}</h3>
                                                <p style="font-size:14px; color:#64748b; margin-bottom:4px;">🛏️ ${b.roomType} · 👥 Khách: ${b.guestName} (${b.guestPhone})</p>
                                                <p style="font-size:13px; color:#475569;">📅 Nhận phòng: <strong>${b.checkIn}</strong> ➔ Trả phòng: <strong>${b.checkOut}</strong> (${b.nights} đêm)</p>
                                            </div>
                                            <div style="text-align:right;">
                                                <div style="font-size:18px; font-weight:800; color:#0062E0; margin-bottom:4px;">
                                                    <fmt:formatNumber value="${b.totalPrice}" type="number"/> đ
                                                </div>
                                                <span style="font-size:12px; color:#16a34a; font-weight:600;">+${b.VPointsEarned} V-Points</span>
                                                <div style="margin-top:8px;">
                                                    <button class="btn-outline-sm" onclick="window.print()" type="button" style="padding:4px 10px; font-size:12px;">In E-Voucher</button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div style="text-align: center; padding: 40px 20px; background: white; border-radius: 12px; border: 1px dashed #cbd5e1;">
                                        <span style="font-size: 40px;">🏨</span>
                                        <h3 style="margin-top: 10px;">Chưa có đơn đặt phòng nào</h3>
                                        <p style="color: #64748b; font-size: 14px; margin-bottom: 16px;">Hãy khám phá các điểm đến hấp dẫn và đặt phòng ngay hôm nay.</p>
                                        <a href="${pageContext.request.contextPath}/search" class="btn-primary btn-sm">Tìm kiếm khách sạn</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- TAB 2: FAVORITE HOTELS -->
                    <div class="tab-pane" id="pane-favorites" style="display:none;">
                        <div class="pane-header">
                            <h2>Khách sạn & Resort yêu thích</h2>
                            <p>Danh sách các chỗ nghỉ bạn đã đánh dấu trái tim ❤️</p>
                        </div>

                        <div class="hotels-grid" id="userFavoritesGrid">
                            <c:choose>
                                <c:when test="${not empty favorites}">
                                    <c:forEach var="fav" items="${favorites}">
                                        <div class="hotel-card" style="margin-bottom:20px;">
                                            <div class="card-image-wrapper">
                                                <img src="${fav.hotel.image}" alt="${fav.hotel.name}" class="hotel-img" onerror="this.src='https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500'">
                                                <button class="btn-fav active" type="button" onclick="toggleFavorite(${fav.hotel.id}, '${pageContext.request.contextPath}'); this.closest('.hotel-card').remove();">❤️</button>
                                            </div>
                                            <div class="card-body">
                                                <h3 class="hotel-title">${fav.hotel.name}</h3>
                                                <div class="hotel-location">
                                                    <span>📍 ${fav.hotel.city} · ${fav.hotel.address}</span>
                                                </div>
                                                <div class="hotel-footer" style="margin-top:10px;">
                                                    <div class="price-box">
                                                        <div class="price"><fmt:formatNumber value="${fav.hotel.price}" type="number"/>₫ <span>/ đêm</span></div>
                                                    </div>
                                                    <a href="${pageContext.request.contextPath}/details?id=${fav.hotel.id}" class="btn-primary btn-sm">Xem chi tiết</a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div style="text-align: center; padding: 40px 20px; background: white; border-radius: 12px; border: 1px dashed #cbd5e1; width:100%;">
                                        <span style="font-size: 40px;">🤍</span>
                                        <h3 style="margin-top: 10px;">Danh sách yêu thích trống</h3>
                                        <p style="color: #64748b; font-size: 14px; margin-bottom: 16px;">Bấm biểu tượng trái tim ở các chỗ nghỉ để lưu lại xem sau.</p>
                                        <a href="${pageContext.request.contextPath}/search" class="btn-primary btn-sm">Khám phá ngay</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- TAB 3: MEMBERSHIP PRIVILEGES -->
                    <div class="tab-pane" id="pane-membership" style="display:none;">
                        <div class="pane-header">
                            <h2>Đặc quyền hội viên VIP</h2>
                            <p>Tận hưởng quyền lợi và ưu đãi độc quyền dành riêng cho bạn</p>
                        </div>

                        <div style="background: linear-gradient(135deg, #1e293b, #0f172a); color: white; padding: 30px; border-radius: 16px; margin-bottom: 24px; position:relative; overflow:hidden;">
                            <span style="background: rgba(255,255,255,0.15); padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 700; color: #fbbf24;">VIP MEMBER PASS</span>
                            <h2 style="font-size: 24px; margin: 12px 0 4px; color:white;">${user.fullName}</h2>
                            <p style="color: #94a3b8; font-size: 14px;">Hạng thẻ: <strong style="color:#fbbf24;">${user.vipTier}</strong> · Điểm tích luỹ: <strong>${user.VPoints} V-Points</strong></p>
                        </div>

                        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px;">
                            <div style="background:white; padding:20px; border-radius:12px; border:1px solid #e2e8f0;">
                                <div style="font-size:24px; margin-bottom:8px;">🎁</div>
                                <h4 style="font-weight:700; margin-bottom:4px;">Giảm giá thêm tới 15%</h4>
                                <p style="font-size:13px; color:#64748b;">Áp dụng tự động vào tất cả khách sạn và resort đối tác Vstay.</p>
                            </div>
                            <div style="background:white; padding:20px; border-radius:12px; border:1px solid #e2e8f0;">
                                <div style="font-size:24px; margin-bottom:8px;">⏰</div>
                                <h4 style="font-weight:700; margin-bottom:4px;">Nhận phòng sớm & Trả muộn</h4>
                                <p style="font-size:13px; color:#64748b;">Linh hoạt thời gian lưu trú lên đến 2 giờ tùy tình trạng phòng.</p>
                            </div>
                            <div style="background:white; padding:20px; border-radius:12px; border:1px solid #e2e8f0;">
                                <div style="font-size:24px; margin-bottom:8px;">☕</div>
                                <h4 style="font-weight:700; margin-bottom:4px;">Miễn phí trà chiều & Buffet</h4>
                                <p style="font-size:13px; color:#64748b;">Nâng cấp bữa sáng và dịch vụ đón tiếp thượng hạng tại chỗ nghỉ.</p>
                            </div>
                        </div>
                    </div>

                    <!-- TAB 4: SETTINGS -->
                    <div class="tab-pane" id="pane-settings" style="display:none;">
                        <div class="pane-header">
                            <h2>Cài đặt tài khoản & Bảo mật</h2>
                            <p>Cập nhật thông tin cá nhân và quản lý bảo mật</p>
                        </div>

                        <div style="background:white; padding:24px; border-radius:12px; border:1px solid #e2e8f0; max-width:600px;">
                            <div style="margin-bottom:16px;">
                                <label style="display:block; font-size:14px; font-weight:600; margin-bottom:6px;">Họ và tên</label>
                                <input type="text" value="${user.fullName}" class="form-input" style="width:100%; padding:10px; border:1px solid #cbd5e1; border-radius:8px;" readonly>
                            </div>
                            <div style="margin-bottom:16px;">
                                <label style="display:block; font-size:14px; font-weight:600; margin-bottom:6px;">Địa chỉ Email</label>
                                <input type="email" value="${user.email}" class="form-input" style="width:100%; padding:10px; border:1px solid #cbd5e1; border-radius:8px;" readonly>
                            </div>
                            <div style="margin-bottom:16px;">
                                <label style="display:block; font-size:14px; font-weight:600; margin-bottom:6px;">Số điện thoại</label>
                                <input type="tel" value="${user.phone}" class="form-input" style="width:100%; padding:10px; border:1px solid #cbd5e1; border-radius:8px;" readonly>
                            </div>
                            <div style="margin-bottom:20px;">
                                <label style="display:block; font-size:14px; font-weight:600; margin-bottom:6px;">Vai trò hệ thống</label>
                                <span style="background:#e0f2fe; color:#0369a1; padding:4px 10px; border-radius:6px; font-weight:700; font-size:13px;">${user.role}</span>
                            </div>
                            <button type="button" class="btn-primary" onclick="alert('Thông tin tài khoản đã được bảo mật an toàn trên máy chủ Vstay!');">Lưu thông tin</button>
                        </div>
                    </div>

                </div>

            </div>
        </div>
    </main>

    <jsp:include page="includes/footer.jsp"/>

    <script>
        function switchAccountTab(tabName) {
            document.querySelectorAll(".account-sidebar .menu-item").forEach(btn => btn.classList.remove("active"));
            document.querySelectorAll(".account-content-area .tab-pane").forEach(pane => {
                pane.classList.remove("active");
                pane.style.display = "none";
            });

            const activeBtn = document.querySelector(`.account-sidebar [data-tab="${tabName}"]`);
            if (activeBtn) activeBtn.classList.add("active");

            const activePane = document.getElementById("pane-" + tabName);
            if (activePane) {
                activePane.classList.add("active");
                activePane.style.display = "block";
            }
        }

        // Check hash in URL (e.g. #favorites)
        if (window.location.hash) {
            const hash = window.location.hash.substring(1);
            if (["bookings", "favorites", "membership", "settings"].includes(hash)) {
                switchAccountTab(hash);
            }
        }
    </script>
</body>
</html>
