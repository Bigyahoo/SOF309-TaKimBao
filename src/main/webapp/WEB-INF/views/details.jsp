<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${hotel.name} - Chi tiết & Đặt phòng | Vstay.vn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/details.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>

    <jsp:include page="includes/header.jsp"/>

    <!-- Sub-header Sticky Bar -->
    <div class="sticky-subnav">
        <div class="subnav-container">
            <div class="subnav-links">
                <a href="#overview" class="subnav-item active">Tổng quan</a>
                <a href="#rooms" class="subnav-item">Chọn loại phòng</a>
                <a href="#amenities" class="subnav-item">Tiện nghi</a>
                <a href="#reviews" class="subnav-item">Đánh giá khách hàng</a>
                <a href="#location" class="subnav-item">Bản đồ & Vị trí</a>
            </div>
            <div class="subnav-price">
                <span class="subnav-price-label">Giá từ</span>
                <strong class="subnav-price-val" id="subnavPriceDisplay"><fmt:formatNumber value="${hotel.price}" type="number"/> đ</strong>
                <a href="#rooms" class="btn-primary btn-sm">Chọn phòng</a>
            </div>
        </div>
    </div>

    <!-- Main Container -->
    <main class="main-container" id="overview">
        <!-- Breadcrumbs -->
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Trang chủ</a> &gt; 
            <a href="${pageContext.request.contextPath}/search">Khách sạn Việt Nam</a> &gt; 
            <a href="${pageContext.request.contextPath}/search?location=${hotel.city}">${hotel.city}</a> &gt; 
            <span id="hotelBreadcrumbName">${hotel.name}</span>
        </div>

        <!-- Title Block -->
        <div class="hotel-header-block">
            <div class="header-main-info">
                <div class="stars-badge">
                    <c:forEach begin="1" end="${hotel.stars != null ? hotel.stars : 5}">★</c:forEach>
                    <span class="stars-text">${hotel.category} ${hotel.stars != null ? hotel.stars : 5} sao đẳng cấp quốc tế</span>
                </div>
                <h1 class="hotel-title" id="hotelName">${hotel.name}</h1>
                <p class="hotel-address" id="hotelAddress">
                    📍 ${hotel.address} · <a href="#location" class="map-link">Xem trên bản đồ</a>
                </p>
            </div>

            <div class="header-actions">
                <div class="rating-box-large">
                    <div class="rating-text-col">
                        <strong class="rating-verdict">${not empty hotel.ratingText ? hotel.ratingText : 'Xuất sắc'}</strong>
                        <span class="rating-count" id="reviewsCount">${hotel.reviewsCount != null ? hotel.reviewsCount : '850'} đánh giá xác thực</span>
                    </div>
                    <div class="rating-score-badge" id="hotelScore">${hotel.rating != null ? hotel.rating : '9.2'}</div>
                </div>
                <div class="action-buttons-row">
                    <button class="btn-outline-icon" onclick="toggleFavorite(${hotel.id}, '${pageContext.request.contextPath}')">
                        ${isFavorite ? '❤️ Đã lưu yêu thích' : '🤍 Lưu chỗ nghỉ'}
                    </button>
                    <button class="btn-outline-icon" onclick="navigator.clipboard.writeText(window.location.href); alert('Đã sao chép liên kết khách sạn!');">🔗 Chia sẻ</button>
                </div>
            </div>
        </div>

        <!-- Photo Gallery Grid -->
        <section class="gallery-section">
            <div class="gallery-grid">
                <div class="gallery-item main-img">
                    <img id="galleryMainImg" src="${hotel.image}" alt="${hotel.name}" onerror="this.src='https://images.unsplash.com/photo-1566073771259-6a8506099945?w=1000'">
                    <span class="gallery-tag">${not empty hotel.badgeSub ? hotel.badgeSub : 'Bãi biển riêng & Hồ bơi vô cực'}</span>
                </div>
                <div class="gallery-item">
                    <img src="https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600&auto=format&fit=crop" alt="Phòng ngủ sang trọng">
                </div>
                <div class="gallery-item">
                    <img src="https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop" alt="Phòng tắm hiện đại">
                </div>
                <div class="gallery-item">
                    <img src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=600&auto=format&fit=crop" alt="Nhà hàng bên biển">
                </div>
                <div class="gallery-item more-overlay">
                    <img src="https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=600&auto=format&fit=crop" alt="Toàn cảnh resort">
                    <div class="overlay-content">
                        <span>🖼️ +12 Ảnh/Video</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- Description Box -->
        <c:if test="${not empty hotel.description}">
            <section style="margin: 24px 0; background: white; padding: 24px; border-radius: 12px; border: 1px solid var(--border-color, #e2e8f0); line-height: 1.7; font-size: 15px; color: #334155;">
                <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 10px; color: #0f172a;">Giới thiệu về ${hotel.name}</h3>
                <p>${hotel.description}</p>
            </section>
        </c:if>

        <!-- Amenity Highlights Chips Bar -->
        <section class="amenities-bar-section" id="amenities">
            <c:forEach var="amenity" items="${hotel.amenitiesList}">
                <div class="amenity-chip">✨ ${amenity}</div>
            </c:forEach>
            <c:if test="${empty hotel.amenitiesList}">
                <div class="amenity-chip">🏊‍♂️ Bãi biển riêng & 3 Hồ bơi</div>
                <div class="amenity-chip">🍽️ 3 Nhà hàng & Bar bãi biển</div>
                <div class="amenity-chip">📶 Wi-Fi miễn phí tốc độ cao</div>
                <div class="amenity-chip">💆‍♀️ Spa & Chăm sóc sức khỏe</div>
                <div class="amenity-chip">🚌 Xe đưa đón sân bay miễn phí</div>
                <div class="amenity-chip">👶 Khu vui chơi trẻ em Kid's Club</div>
            </c:if>
        </section>

        <!-- Availability Search Selector Bar -->
        <section class="dates-selector-bar">
            <div class="selector-field">
                <label>📅 Ngày nhận phòng</label>
                <input type="date" id="dateCheckIn" value="2026-04-15" class="field-input" style="border:none; background:transparent; font-weight:600;">
            </div>
            <div class="selector-field">
                <label>📅 Ngày trả phòng</label>
                <input type="date" id="dateCheckOut" value="2026-04-18" class="field-input" style="border:none; background:transparent; font-weight:600;">
            </div>
            <div class="selector-field">
                <label>👥 Khách & Phòng</label>
                <input type="text" value="2 người lớn · 1 phòng (3 đêm)" readonly>
            </div>
            <button class="btn-primary" id="btnUpdateDates" type="button" onclick="alert('Đã cập nhật tình trạng phòng cho khoảng thời gian bạn chọn!');">Check giá phòng</button>
        </section>

        <!-- Content Split Layout: Room Selection List + Sticky Summary Sidebar -->
        <div class="details-content-grid" id="rooms">
            
            <!-- Room Selection Column -->
            <div class="rooms-column">
                <h2 class="section-heading">Các hạng phòng khả dụng tại ${hotel.name}</h2>

                <c:choose>
                    <c:when test="${not empty rooms}">
                        <c:forEach var="room" items="${rooms}" varStatus="status">
                            <div class="room-card ${status.first ? 'selected' : ''}" data-room-name="${room.name}" data-price="${room.price}">
                                <div class="room-header-row">
                                    <h3 class="room-title">${room.name}</h3>
                                    <span class="room-size-badge ${status.first ? 'badge-gold' : ''}">📐 ${room.area != null ? room.area : '45.0'} m² · ${room.bedType}</span>
                                </div>

                                <div class="room-body-grid">
                                    <div class="room-media">
                                        <img src="${room.image != null ? room.image : hotel.image}" alt="${room.name}" onerror="this.src='https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=500'">
                                        <div class="room-specs-list">
                                            <span>🛏️ ${room.bedType}</span>
                                            <span>☕ Ban công thoáng mát</span>
                                            <span>👥 Tối đa ${room.capacity != null ? room.capacity : 2} người</span>
                                        </div>
                                    </div>

                                    <div class="room-details">
                                        <div class="perks-list">
                                            <span class="perk-green">✓ Bữa sáng buffet quốc tế hàng ngày</span>
                                            <span class="perk-green">✓ Miễn phí hủy phòng trước 24 giờ nhận phòng</span>
                                            <span class="perk-green">✓ Không cần thanh toán trước · Tùy chọn trả tại quầy</span>
                                        </div>

                                        <div class="room-price-box">
                                            <div class="price-stack">
                                                <c:if test="${room.oldPrice != null && room.oldPrice > room.price}">
                                                    <span class="old-price"><fmt:formatNumber value="${room.oldPrice}" type="number"/> đ</span>
                                                    <span class="discount-badge">-25% Ưu Đãi</span>
                                                </c:if>
                                                <div class="room-price"><fmt:formatNumber value="${room.price}" type="number"/> đ <span>/ đêm</span></div>
                                                <span class="tax-hint">Đã bao gồm thuế & phí phục vụ</span>
                                            </div>
                                            <button class="btn-select-room btn-primary" type="button" onclick="selectRoomCard(this, '${room.name}', ${room.price})">
                                                ${status.first ? 'Đã chọn hạng phòng này' : 'Chọn phòng này'}
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Fallback Room Cards generated from hotel base price -->
                        <div class="room-card selected" data-room-name="Phòng Superior Hướng Vườn" data-price="${hotel.price}">
                            <div class="room-header-row">
                                <h3 class="room-title">Phòng Superior Hướng Vườn (Garden View)</h3>
                                <span class="room-size-badge">📐 40.5 m² · View Vườn Nhiệt Đới</span>
                            </div>

                            <div class="room-body-grid">
                                <div class="room-media">
                                    <img src="https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=500&auto=format&fit=crop" alt="Phòng Superior">
                                    <div class="room-specs-list">
                                        <span>🛏️ 1 giường King hoặc 2 giường đơn</span>
                                        <span>☕ Ban công riêng thoáng mát</span>
                                        <span>🛁 Bồn tắm cẩm thạch sang trọng</span>
                                    </div>
                                </div>

                                <div class="room-details">
                                    <div class="perks-list">
                                        <span class="perk-green">✓ Bữa sáng buffet quốc tế hàng ngày cho 2 người</span>
                                        <span class="perk-green">✓ Miễn phí hủy phòng trước ngày nhận</span>
                                        <span class="perk-green">✓ Tùy chọn thanh toán tại khách sạn</span>
                                    </div>

                                    <div class="room-price-box">
                                        <div class="price-stack">
                                            <div class="room-price"><fmt:formatNumber value="${hotel.price}" type="number"/> đ <span>/ đêm</span></div>
                                            <span class="tax-hint">Tổng 3 đêm: <fmt:formatNumber value="${hotel.price * 3}" type="number"/> đ</span>
                                        </div>
                                        <button class="btn-select-room btn-primary" type="button" onclick="selectRoomCard(this, 'Phòng Superior Hướng Vườn', ${hotel.price})">Đã chọn hạng phòng này</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="room-card" data-room-name="Phòng Deluxe Ocean Studio Suite" data-price="${hotel.price + 600000}">
                            <div class="room-header-row">
                                <h3 class="room-title">Phòng Deluxe Ocean Studio Suite (VIP Hướng Biển)</h3>
                                <span class="room-size-badge badge-gold">👑 VIP Ocean View · 55.0 m²</span>
                            </div>

                            <div class="room-body-grid">
                                <div class="room-media">
                                    <img src="${hotel.image}" alt="Phòng Deluxe">
                                    <div class="room-specs-list">
                                        <span>🛏️ 1 Giường Đôi King Size Rộng Rãi</span>
                                        <span>🌊 Ban công Panorama góc nhìn triệu đô</span>
                                        <span>🍷 Tặng 1 chai vang đỏ đón chào</span>
                                    </div>
                                </div>

                                <div class="room-details">
                                    <div class="perks-list">
                                        <span class="perk-gold">🔥 Genius Giảm Thêm 15% Dành Cho Hội Viên</span>
                                        <span class="perk-green">✓ Bữa sáng buffet cao cấp + Miễn phí trà chiều</span>
                                        <span class="perk-green">✓ Nhận phòng sớm miễn phí (theo phòng trống)</span>
                                    </div>

                                    <div class="room-price-box">
                                        <div class="price-stack">
                                            <div class="room-price"><fmt:formatNumber value="${hotel.price + 600000}" type="number"/> đ <span>/ đêm</span></div>
                                            <span class="tax-hint">Tổng 3 đêm: <fmt:formatNumber value="${(hotel.price + 600000) * 3}" type="number"/> đ</span>
                                        </div>
                                        <button class="btn-select-room btn-primary" type="button" onclick="selectRoomCard(this, 'Phòng Deluxe Ocean Studio Suite', ${hotel.price + 600000})">Chọn phòng này</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>

            <!-- Right Sticky Reservation Box -->
            <aside class="sidebar-column">
                <div class="sticky-order-box">
                    <h3 class="order-box-title">Tóm tắt đặt phòng của bạn</h3>
                    
                    <div class="selected-room-info">
                        <strong id="selectedRoomTitle">${not empty rooms ? rooms[0].name : 'Phòng tiêu chuẩn VIP'}</strong>
                        <p id="selectedRoomNights">3 đêm · 15/04/2026 – 18/04/2026</p>
                    </div>

                    <div class="order-price-breakdown">
                        <div class="breakdown-row">
                            <span>Giá gốc (3 đêm):</span>
                            <span id="summaryOldPrice"><fmt:formatNumber value="${hotel.price * 3 * 1.25}" type="number"/> đ</span>
                        </div>
                        <div class="breakdown-row discount">
                            <span>Ưu đãi Vstay & Genius VIP:</span>
                            <span id="summaryDiscount">-<fmt:formatNumber value="${hotel.price * 3 * 0.25}" type="number"/> đ</span>
                        </div>
                        <div class="breakdown-row total-row">
                            <span>Tổng tiền thanh toán:</span>
                            <strong class="total-val" id="summaryTotalPrice"><fmt:formatNumber value="${hotel.price * 3}" type="number"/> đ</strong>
                        </div>
                        <span class="vpoint-earn">🎁 Tích lũy +<fmt:formatNumber value="${hotel.price * 3 / 10000}" type="number"/> V-Points sau chuyến đi</span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout?id=${hotel.id}&room=${not empty rooms ? rooms[0].name : 'Phòng tiêu chuẩn'}&price=${hotel.price}&checkIn=2026-04-15&checkOut=2026-04-18" id="btnProceedCheckout" class="btn-checkout-primary">
                        🔒 Đặt chỗ ngay & Giữ giá tốt ➔
                    </a>

                    <div class="order-trust-notes">
                        <span>✓ Giữ chỗ nhanh trong 60 giây</span>
                        <span>✓ Miễn phí hủy phòng đến trước 24h</span>
                        <span>✓ Không phụ phí thanh toán</span>
                    </div>
                </div>
            </aside>
        </div>

        <!-- Verified Reviews Section -->
        <section class="reviews-section" id="reviews">
            <h2 class="section-heading">Đánh giá từ khách hàng thực tế (${hotel.reviewsCount != null ? hotel.reviewsCount : '120'})</h2>

            <div class="reviews-grid">
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <c:forEach var="rev" items="${reviews}">
                            <div class="review-card">
                                <div class="reviewer-header">
                                    <div class="reviewer-avatar">${rev.authorName.substring(0, 1)}</div>
                                    <div>
                                        <strong>${rev.authorName}</strong>
                                        <span class="review-date">${not empty rev.travelType ? rev.travelType : 'Kỳ nghỉ thư giãn'}</span>
                                    </div>
                                    <span class="review-score">${rev.rating}/10</span>
                                </div>
                                <p class="review-comment">"${rev.comment}"</p>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="review-card">
                            <div class="reviewer-header">
                                <div class="reviewer-avatar">TN</div>
                                <div>
                                    <strong>Trần Hoàng Nam</strong>
                                    <span class="review-date">Kỳ nghỉ gia đình 3 đêm</span>
                                </div>
                                <span class="review-score">9.8/10</span>
                            </div>
                            <p class="review-comment">"Chuyến đi rất đáng nhớ! Khách sạn sạch sẽ, nhân viên phục vụ tận tình và chu đáo. Hồ bơi vô cực ngắm cảnh rất đẹp. Chắc chắn sẽ quay lại!"</p>
                        </div>
                        <div class="review-card">
                            <div class="reviewer-header">
                                <div class="reviewer-avatar">LM</div>
                                <div>
                                    <strong>Lê Thị Mai</strong>
                                    <span class="review-date">Cặp đôi kỳ nghỉ lãng mạn</span>
                                </div>
                                <span class="review-score">9.5/10</span>
                            </div>
                            <p class="review-comment">"Đặt phòng qua Vstay nhanh và giá tốt hơn đặt trực tiếp. Phòng view thoáng, đồ ăn sáng buffet phong phú cả món Âu và món Việt."</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <!-- Location Map Embed Section -->
        <section class="location-section" id="location">
            <h2 class="section-heading">Vị trí & Khoảng cách di chuyển</h2>
            <div class="map-container">
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3834.331498056087!2d108.24357731536248!3d16.04822008889417!2m2!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3142176412f7169f%3A0x6b0ffc61cf4cf423!2sFurama%20Resort%20Danang!5e0!3m2!1svi!2svn!4v1650000000000!5m2!1svi!2svn" width="100%" height="320" style="border:0; border-radius: 12px;" allowfullscreen="" loading="lazy"></iframe>
            </div>
        </section>
    </main>

    <jsp:include page="includes/footer.jsp"/>

    <script>
        function selectRoomCard(btn, roomName, price) {
            document.querySelectorAll(".room-card").forEach(c => {
                c.classList.remove("selected");
                const b = c.querySelector(".btn-select-room");
                if (b) b.textContent = "Chọn phòng này";
            });

            const card = btn.closest(".room-card");
            if (card) {
                card.classList.add("selected");
                btn.textContent = "Đã chọn hạng phòng này";
            }

            document.getElementById("selectedRoomTitle").textContent = roomName;
            const nights = 3;
            const totalPrice = price * nights;
            const oldPrice = Math.round(totalPrice * 1.25);
            const discount = oldPrice - totalPrice;

            document.getElementById("summaryOldPrice").textContent = new Intl.NumberFormat("vi-VN").format(oldPrice) + " đ";
            document.getElementById("summaryDiscount").textContent = "-" + new Intl.NumberFormat("vi-VN").format(discount) + " đ";
            document.getElementById("summaryTotalPrice").textContent = new Intl.NumberFormat("vi-VN").format(totalPrice) + " đ";

            const checkIn = document.getElementById("dateCheckIn").value || "2026-04-15";
            const checkOut = document.getElementById("dateCheckOut").value || "2026-04-18";

            const checkoutBtn = document.getElementById("btnProceedCheckout");
            checkoutBtn.href = "${pageContext.request.contextPath}/checkout?id=${hotel.id}&room=" + encodeURIComponent(roomName) + "&price=" + price + "&checkIn=" + checkIn + "&checkOut=" + checkOut;
        }
    </script>
</body>
</html>
