<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vstay.vn - Nền tảng đặt phòng khách sạn & resort hàng đầu Việt Nam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>

    <jsp:include page="includes/header.jsp"/>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Hero Section -->
        <section class="hero-section">
            <div class="hero-bg-overlay"></div>
            <div class="hero-container">
                <div class="hero-badge">NỀN TẢNG ĐẶT PHÒNG SỐ 1 VIỆT NAM</div>
                <h1 class="hero-title">Tìm nơi nghỉ dưỡng <span class="highlight">lý tưởng</span> tại Việt Nam</h1>
                <p class="hero-subtitle">Hơn 12.000 khách sạn, resort ven biển và homestay độc đáo với mức giá tốt nhất được đảm bảo</p>

                <!-- Interactive Search Card Widget -->
                <div class="search-card">
                    <div class="search-tabs" id="searchTabsContainer">
                        <button class="tab-btn active" type="button" data-type="all">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>
                            Tất cả chỗ nghỉ
                        </button>
                        <button class="tab-btn" type="button" data-type="Khách sạn">Khách sạn & Resort</button>
                        <button class="tab-btn" type="button" data-type="Villa">Homestay & Villa</button>
                        <button class="tab-btn" type="button" data-type="Resort">Resort ven biển</button>
                    </div>

                    <!-- Functional Search Form -->
                    <form action="${pageContext.request.contextPath}/search" method="GET" id="heroSearchForm" class="search-fields-grid">
                        <input type="hidden" name="category" id="searchCategoryInput" value="">
                        
                        <div class="search-field">
                            <label class="field-label" for="inputLocation">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#0062E0" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
                                Địa điểm / Tên khách sạn
                            </label>
                            <input type="text" name="location" id="inputLocation" class="field-input" value="Đà Nẵng" placeholder="Bạn muốn đến đâu?" required>
                        </div>

                        <div class="search-field">
                            <label class="field-label" for="inputDates">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#0062E0" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                                Nhận phòng - Trả phòng
                            </label>
                            <input type="text" id="inputDates" class="field-input" value="15/04 – 18/04/2026 (3 đêm)">
                        </div>

                        <div class="search-field">
                            <label class="field-label" for="inputGuests">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#0062E0" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                                Số khách & Phòng
                            </label>
                            <input type="text" id="inputGuests" class="field-input" value="2 người lớn · 1 phòng">
                        </div>

                        <button type="submit" class="btn-search" id="btnSubmitHeroSearch">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                            <span>Tìm kiếm</span>
                        </button>
                    </form>
                </div>
            </div>
        </section>

        <!-- Flash Sale Banner with Countdown -->
        <section class="flash-sale-section">
            <div class="section-container">
                <div class="flash-sale-card">
                    <div class="flash-sale-info">
                        <span class="flash-badge">⚡ FLASH SALE CUỐI TUẦN</span>
                        <h3 class="flash-title">Giảm tới 50% các khu nghỉ dưỡng cao cấp</h3>
                        <p class="flash-desc">Áp dụng cho đặt phòng từ nay đến hết tháng 4. Số lượng có hạn!</p>
                    </div>
                    <div class="flash-timer-wrapper">
                        <span class="timer-label">Kết thúc sau:</span>
                        <div class="timer-box">
                            <div class="timer-unit">
                                <span class="timer-number" id="flashHours">08</span>
                                <span class="timer-sub">Giờ</span>
                            </div>
                            <span class="timer-sep">:</span>
                            <div class="timer-unit">
                                <span class="timer-number" id="flashMinutes">45</span>
                                <span class="timer-sub">Phút</span>
                            </div>
                            <span class="timer-sep">:</span>
                            <div class="timer-unit">
                                <span class="timer-number" id="flashSeconds">12</span>
                                <span class="timer-sub">Giây</span>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/search?deal=flashsale" class="btn-flash">Săn Deal Ngay</a>
                    </div>
                </div>
            </div>
        </section>

        <!-- Top Destinations Section -->
        <section class="destinations-section">
            <div class="section-container">
                <div class="section-header">
                    <div>
                        <h2 class="section-title">Điểm Đến Hàng Đầu Việt Nam</h2>
                        <p class="section-subtitle">Những địa danh du lịch nổi tiếng được yêu thích nhất mùa lễ hội</p>
                    </div>
                    <button class="section-collapse-btn" id="toggleDestinationsBtn" title="Thu gọn / Mở rộng" type="button">
                        <span class="collapse-text">Thu gọn</span>
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
                    </button>
                </div>

                <div class="destinations-grid">
                    <a href="${pageContext.request.contextPath}/search?location=Đà Nẵng" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=500" alt="Đà Nẵng" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Đà Nẵng</h3>
                            <span class="dest-count">1.420 chỗ nghỉ</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Hà Nội" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=500" alt="Hà Nội" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Hà Nội</h3>
                            <span class="dest-count">2.150 chỗ nghỉ</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Hồ Chí Minh" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=500" alt="TP. Hồ Chí Minh" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">TP. Hồ Chí Minh</h3>
                            <span class="dest-count">3.200 chỗ nghỉ</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Phú Quốc" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1540202404-a2f29016b523?w=500" alt="Phú Quốc" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Phú Quốc</h3>
                            <span class="dest-count">890 resort</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Nha Trang" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500" alt="Nha Trang" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Nha Trang</h3>
                            <span class="dest-count">980 chỗ nghỉ</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Đà Lạt" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=500" alt="Đà Lạt" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Đà Lạt</h3>
                            <span class="dest-count">1.120 homestay</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Hội An" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=500" alt="Hội An" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Hội An</h3>
                            <span class="dest-count">650 boutique hotel</span>
                        </div>
                    </a>
                    <a href="${pageContext.request.contextPath}/search?location=Vũng Tàu" class="destination-card">
                        <img src="https://images.unsplash.com/photo-1582719508461-905c673771fd?w=500" alt="Vũng Tàu" class="dest-img">
                        <div class="dest-overlay">
                            <h3 class="dest-name">Vũng Tàu</h3>
                            <span class="dest-count">740 villa</span>
                        </div>
                    </a>
                </div>
            </div>
        </section>

        <!-- Featured Hotels Section -->
        <section class="featured-hotels-section">
            <div class="section-container">
                <div class="section-header">
                    <div>
                        <h2 class="section-title">Khách Sạn & Resort Nổi Bật</h2>
                        <p class="section-subtitle">Được khách hàng đánh giá cao nhất về vị trí và chất lượng phục vụ</p>
                    </div>
                    <div class="header-actions">
                        <!-- City Filter Pills -->
                        <div class="filter-pills" id="cityFilterPills">
                            <button class="pill-btn active" data-city="all">Tất cả</button>
                            <button class="pill-btn" data-city="Đà Nẵng">Đà Nẵng</button>
                            <button class="pill-btn" data-city="Hà Nội">Hà Nội</button>
                            <button class="pill-btn" data-city="Hồ Chí Minh">TP. HCM</button>
                            <button class="pill-btn" data-city="Phú Quốc">Phú Quốc</button>
                            <button class="pill-btn" data-city="Nha Trang">Nha Trang</button>
                            <button class="pill-btn" data-city="Đà Lạt">Đà Lạt</button>
                        </div>
                        <button class="section-collapse-btn" id="toggleHotelsBtn" title="Thu gọn / Mở rộng" type="button">
                            <span class="collapse-text">Thu gọn</span>
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
                        </button>
                    </div>
                </div>

                <!-- Hotel Cards Grid -->
                <div class="hotels-grid" id="featuredHotelsGrid">
                    <c:forEach var="h" items="${featuredHotels}">
                        <div class="hotel-card" data-city="${h.city}">
                            <div class="card-image-wrapper">
                                <img src="${h.image}" alt="${h.name}" class="hotel-img" onerror="this.src='https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500'">
                                <c:if test="${not empty h.badge}">
                                    <span class="badge-tag">${h.badge}</span>
                                </c:if>
                                <button class="btn-fav" type="button" aria-label="Yêu thích" onclick="toggleFavorite(${h.id}, '${pageContext.request.contextPath}')" data-id="${h.id}">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l8.72-8.72 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                                </button>
                            </div>
                            <div class="card-body">
                                <div class="hotel-stars">
                                    <c:forEach begin="1" end="${h.stars != null ? h.stars : 5}">★</c:forEach>
                                </div>
                                <h3 class="hotel-title">${h.name}</h3>
                                <div class="hotel-location">
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#64748b" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
                                    <span>${h.city} · ${h.address}</span>
                                </div>

                                <div class="review-score">
                                    <span class="score-box">${h.rating != null ? h.rating : '9.0'}</span>
                                    <div class="score-text">
                                        <strong>${h.ratingText != null ? h.ratingText : 'Xuất sắc'}</strong>
                                        <span>${h.reviewsCount != null ? h.reviewsCount : '150'} đánh giá</span>
                                    </div>
                                </div>

                                <div class="hotel-footer">
                                    <div class="price-box">
                                        <c:if test="${h.oldPrice != null && h.oldPrice > h.price}">
                                            <span class="old-price"><fmt:formatNumber value="${h.oldPrice}" type="number" groupingUsed="true"/>₫</span>
                                        </c:if>
                                        <div class="price"><fmt:formatNumber value="${h.price}" type="number" groupingUsed="true"/>₫ <span>/ đêm</span></div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/details?id=${h.id}" class="btn-primary btn-sm">Xem phòng trống</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Why Choose Vstay -->
        <section class="features-section">
            <div class="section-container">
                <div class="section-header text-center">
                    <h2 class="section-title">Tại Sao Chọn Vstay.vn?</h2>
                    <p class="section-subtitle">Chúng tôi mang lại trải nghiệm đặt phòng mượt mà, tiện lợi và tin cậy nhất</p>
                </div>

                <div class="features-grid">
                    <div class="feature-card">
                        <div class="feature-icon icon-blue">
                            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M12 6v6l4 2"/></svg>
                        </div>
                        <h3 class="feature-title">Xác Nhận Tức Thì</h3>
                        <p class="feature-desc">Mã đặt phòng được gửi ngay qua email và tin nhắn trong vòng 30 giây sau thanh toán.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon icon-green">
                            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                        </div>
                        <h3 class="feature-title">Cam Kết Giá Tốt Nhất</h3>
                        <p class="feature-desc">Hoàn tiền 100% phần chênh lệch nếu bạn tìm thấy giá thấp hơn trên các nền tảng khác.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon icon-orange">
                            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
                        </div>
                        <h3 class="feature-title">Hỗ Trợ 24/7 Bằng Tiếng Việt</h3>
                        <p class="feature-desc">Đội ngũ chuyên viên du lịch bản địa luôn sẵn sàng hỗ trợ bạn bất kể ngày đêm.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon icon-purple">
                            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
                        </div>
                        <h3 class="feature-title">Tích Lũy V-Points</h3>
                        <p class="feature-desc">Tích luỹ điểm thưởng đổi đêm nghỉ miễn phí và nâng hạng phòng thành viên VIP.</p>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="includes/footer.jsp"/>

</body>
</html>
