<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vstay.vn - Kết quả tìm kiếm khách sạn ${not empty location ? location : 'Việt Nam'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>

    <jsp:include page="includes/header.jsp"/>

    <!-- Sticky Search Query Bar -->
    <div class="search-summary-bar">
        <div class="search-summary-container">
            <form action="${pageContext.request.contextPath}/search" method="GET" class="summary-chips" style="display:flex; width:100%; align-items:center; justify-content:space-between;">
                <div style="display:flex; gap:12px; align-items:center; flex-wrap:wrap;">
                    <div class="summary-chip">
                        <span class="chip-icon">📍</span>
                        <input type="text" name="location" id="searchLocationInput" value="${location}" placeholder="Địa điểm (ví dụ: Đà Nẵng, Hà Nội...)" class="summary-input" style="border:none; outline:none; font-family:inherit; font-size:14px; font-weight:600; width:200px;">
                    </div>
                    <div class="summary-chip">
                        <span class="chip-icon">📅</span>
                        <span>15/04 – 18/04/2026 (3 đêm)</span>
                    </div>
                    <div class="summary-chip">
                        <span class="chip-icon">👥</span>
                        <span>2 người lớn · 1 phòng</span>
                    </div>
                </div>
                <button type="submit" class="btn-outline-sm" id="btnApplySearch">🔄 Tìm kiếm lại</button>
            </form>
        </div>
    </div>

    <!-- Main Results Section -->
    <main class="main-container">
        <!-- Breadcrumbs & Section Title -->
        <div class="breadcrumb-header">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">Trang chủ</a> &gt; <a href="${pageContext.request.contextPath}/search">Khách sạn Việt Nam</a> &gt; <span>${not empty location ? location : 'Tất cả'}</span>
            </div>
            <div class="title-row">
                <div>
                    <h1 class="page-title">Khách sạn & Resort ${not empty location ? 'tại '.concat(location) : 'hàng đầu Việt Nam'}</h1>
                    <p class="results-count">Tìm thấy <strong id="totalHotelsCount">${totalResults}</strong> chỗ nghỉ phù hợp với điều kiện của bạn</p>
                </div>
                <div class="guarantee-badge">
                    🛡️ Cam kết giá tốt nhất không phí ẩn
                </div>
            </div>
        </div>

        <!-- 2-Column Results Layout -->
        <div class="content-layout">
            
            <!-- Left Filter Sidebar -->
            <aside class="sidebar">
                <!-- Map Preview Card -->
                <div class="map-preview-card">
                    <div class="map-bg"></div>
                    <button class="btn-map" type="button" onclick="alert('Bản đồ vệ tinh các khách sạn đang hoạt động.');">🗺️ Xem bản đồ (${totalResults})</button>
                </div>

                <!-- Filter Form -->
                <form action="${pageContext.request.contextPath}/search" method="GET" class="filter-box" id="filterSidebarForm">
                    <input type="hidden" name="location" value="${location}">
                    <input type="hidden" name="sort" value="${sort}">

                    <div class="filter-header">
                        <h3>Bộ lọc chọn lọc</h3>
                        <a href="${pageContext.request.contextPath}/search?location=${location}" class="clear-filter-btn">Xóa tất cả</a>
                    </div>

                    <!-- Price Filter -->
                    <div class="filter-group">
                        <label class="filter-label">Khoảng giá tối đa / đêm</label>
                        <div class="price-inputs">
                            <span>500.000 đ</span>
                            <span id="priceValueDisplay">
                                <c:choose>
                                    <c:when test="${not empty maxPrice}">
                                        <fmt:formatNumber value="${maxPrice}" type="number"/> đ
                                    </c:when>
                                    <c:otherwise>10.000.000 đ</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <input type="range" name="maxPrice" id="priceRange" min="500000" max="10000000" step="500000" value="${not empty maxPrice ? maxPrice : 10000000}" class="range-slider" oninput="document.getElementById('priceValueDisplay').textContent = new Intl.NumberFormat('vi-VN').format(this.value) + ' đ';">
                        <button type="submit" class="btn-primary btn-sm" style="margin-top:10px; width:100%; border:none; border-radius:6px; padding:6px;">Áp dụng mức giá</button>
                    </div>

                    <!-- Category Filter -->
                    <div class="filter-group">
                        <label class="filter-label">Loại hình lưu trú</label>
                        <label class="checkbox-item"><input type="radio" name="category" value="" ${empty category ? 'checked' : ''} onchange="this.form.submit()"> Tất cả chỗ nghỉ</label>
                        <label class="checkbox-item"><input type="radio" name="category" value="Resort" ${category == 'Resort' ? 'checked' : ''} onchange="this.form.submit()"> Resort Nghỉ dưỡng ven biển</label>
                        <label class="checkbox-item"><input type="radio" name="category" value="Khách sạn" ${category == 'Khách sạn' ? 'checked' : ''} onchange="this.form.submit()"> Khách sạn 4-5 sao</label>
                        <label class="checkbox-item"><input type="radio" name="category" value="Villa" ${category == 'Villa' ? 'checked' : ''} onchange="this.form.submit()"> Homestay & Biệt thự Villa</label>
                    </div>

                    <!-- Star Ratings -->
                    <div class="filter-group">
                        <label class="filter-label">Hạng sao</label>
                        <div class="star-grid">
                            <a href="${pageContext.request.contextPath}/search?location=${location}&stars=5&category=${category}" class="star-btn filter-star ${stars == 5 ? 'active' : ''}">5 sao ★</a>
                            <a href="${pageContext.request.contextPath}/search?location=${location}&stars=4&category=${category}" class="star-btn filter-star ${stars == 4 ? 'active' : ''}">4 sao ★</a>
                            <a href="${pageContext.request.contextPath}/search?location=${location}&stars=3&category=${category}" class="star-btn filter-star ${stars == 3 ? 'active' : ''}">3 sao ★</a>
                            <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}" class="star-btn filter-star ${empty stars ? 'active' : ''}">Tất cả sao</a>
                        </div>
                    </div>

                    <!-- Cities Shortcut -->
                    <div class="filter-group">
                        <label class="filter-label">Thành phố phổ biến</label>
                        <c:forEach var="c" items="${cities}">
                            <label class="checkbox-item">
                                <a href="${pageContext.request.contextPath}/search?location=${c}" style="text-decoration:none; color:inherit; display:flex; justify-content:space-between; width:100%;">
                                    <span>${c}</span>
                                    <span style="color:#0062E0;">➔</span>
                                </a>
                            </label>
                        </c:forEach>
                    </div>
                </form>
            </aside>

            <!-- Right Results List Column -->
            <section class="results-column">
                <!-- Sorting Bar -->
                <div class="sort-bar">
                    <div class="sort-options">
                        <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=recommended" class="sort-tab ${sort == 'recommended' || empty sort ? 'active' : ''}">Gợi ý hàng đầu</a>
                        <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=price-asc" class="sort-tab ${sort == 'price-asc' ? 'active' : ''}">Giá thấp nhất</a>
                        <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=price-desc" class="sort-tab ${sort == 'price-desc' ? 'active' : ''}">Giá cao nhất</a>
                        <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=rating" class="sort-tab ${sort == 'rating' ? 'active' : ''}">Đánh giá cao nhất</a>
                        <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=discount" class="sort-tab deal-tab ${sort == 'discount' ? 'active' : ''}">Khuyến mãi khủng 🔥</a>
                    </div>
                    <label class="tax-note-chk"><input type="checkbox" checked disabled> Đã bao gồm thuế VN</label>
                </div>

                <!-- Hotel Cards Render Container -->
                <div id="hotelsList" class="hotels-list-container">
                    <c:choose>
                        <c:when test="${not empty hotels}">
                            <c:forEach var="hotel" items="${hotels}">
                                <div class="search-hotel-card">
                                    <div class="card-media">
                                        <img src="${hotel.image}" alt="${hotel.name}" onerror="this.src='https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500'">
                                        <button class="card-heart-btn" aria-label="Favorite" onclick="toggleFavorite(${hotel.id}, '${pageContext.request.contextPath}')" data-id="${hotel.id}">❤️</button>
                                        <div class="media-badges">
                                            <c:if test="${not empty hotel.badge}">
                                                <span class="badge-blue">${hotel.badge}</span>
                                            </c:if>
                                            <c:if test="${not empty hotel.badgeSub}">
                                                <span class="badge-glass">${hotel.badgeSub}</span>
                                            </c:if>
                                        </div>
                                        <c:if test="${hotel.galleryCount != null && hotel.galleryCount > 0}">
                                            <span class="gallery-count-pill">+${hotel.galleryCount} ảnh</span>
                                        </c:if>
                                    </div>

                                    <div class="card-details">
                                        <div class="hotel-stars">
                                            <c:forEach begin="1" end="${hotel.stars != null ? hotel.stars : 5}">★</c:forEach>
                                            <span class="stars-label">${hotel.stars != null ? hotel.stars : 5} sao</span>
                                        </div>
                                        <h2 class="card-hotel-name">
                                            <a href="${pageContext.request.contextPath}/details?id=${hotel.id}" style="color:inherit; text-decoration:none;">${hotel.name}</a>
                                        </h2>
                                        <p class="card-address">📍 ${hotel.address} · <span class="dist">${not empty hotel.distance ? hotel.distance : 'Cách trung tâm 500m'}</span></p>
                                        
                                        <div class="room-highlight-box">
                                            <strong>🛏 ${not empty hotel.roomType ? hotel.roomType : 'Phòng Deluxe sang trọng'}</strong>
                                        </div>

                                        <ul class="inclusions-list">
                                            <c:forEach var="inc" items="${hotel.inclusionsList}">
                                                <li><span class="check-icon">✓</span> ${inc}</li>
                                            </c:forEach>
                                            <c:if test="${empty hotel.inclusionsList}">
                                                <li><span class="check-icon">✓</span> Miễn phí bữa sáng buffet</li>
                                                <li><span class="check-icon">✓</span> Hồ bơi vô cực & Gym</li>
                                            </c:if>
                                        </ul>
                                        <c:if test="${not empty hotel.urgency}">
                                            <div class="urgency-tag">🔥 ${hotel.urgency}</div>
                                        </c:if>
                                    </div>

                                    <div class="card-pricing">
                                        <div class="score-badge-row">
                                            <div class="score-info">
                                                <span class="score-text">${not empty hotel.ratingText ? hotel.ratingText : 'Xuất sắc'}</span>
                                                <span class="score-reviews">${hotel.reviewsCount != null ? hotel.reviewsCount : '120'} đánh giá</span>
                                            </div>
                                            <div class="score-num">${hotel.rating != null ? hotel.rating : '9.0'}</div>
                                        </div>

                                        <div class="price-stack">
                                            <c:if test="${hotel.oldPrice != null && hotel.oldPrice > hotel.price}">
                                                <div class="discount-row">
                                                    <span class="old-price"><fmt:formatNumber value="${hotel.oldPrice}" type="number"/> đ</span>
                                                    <span class="discount-badge">-${hotel.discountPercent != null ? hotel.discountPercent : 20}%</span>
                                                </div>
                                            </c:if>
                                            <div class="final-price"><fmt:formatNumber value="${hotel.price}" type="number"/> đ <span class="unit">/ đêm</span></div>
                                            <span class="tax-note">${not empty hotel.taxNote ? hotel.taxNote : 'Đã bao gồm thuế & phí'}</span>
                                        </div>

                                        <a href="${pageContext.request.contextPath}/details?id=${hotel.id}" class="btn-book-card">
                                            Xem chỗ trống & Đặt ngay ➔
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state" style="text-align: center; padding: 60px 20px; background: #FFF; border-radius: 12px; border: 1px dashed #cbd5e1;">
                                <span style="font-size: 48px;">🔍</span>
                                <h3 style="margin-top: 15px; font-size: 20px;">Không tìm thấy chỗ nghỉ phù hợp</h3>
                                <p style="color: #64748b; font-size: 15px; margin: 8px 0 20px;">Vui lòng thử từ khóa tìm kiếm khác hoặc nới lỏng các tiêu chí bộ lọc giá.</p>
                                <a href="${pageContext.request.contextPath}/search" class="btn-primary btn-sm">Xem tất cả chỗ nghỉ</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Pagination Footer -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination-bar">
                        <span class="page-info">Trang ${currentPage} / ${totalPages} (Tổng cộng ${totalResults} chỗ nghỉ)</span>
                        <div class="page-numbers">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=${sort}&page=${currentPage - 1}" class="page-btn prev">&lt; Trang trước</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="p">
                                <c:choose>
                                    <c:when test="${p == currentPage}">
                                        <span class="page-num active">${p}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=${sort}&page=${p}" class="page-num">${p}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/search?location=${location}&category=${category}&stars=${stars}&maxPrice=${maxPrice}&sort=${sort}&page=${currentPage + 1}" class="page-btn next">Trang tiếp &gt;</a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </section>

        </div>
    </main>

    <jsp:include page="includes/footer.jsp"/>

</body>
</html>
