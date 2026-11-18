<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán & Xác nhận đặt phòng - Vstay.vn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>

    <jsp:include page="includes/header.jsp"/>

    <!-- Sub-header & Stepper Bar -->
    <div class="stepper-bar">
        <div class="stepper-container">
            <div class="sub-nav-left">
                <a href="${pageContext.request.contextPath}/details?id=${hotel.id}" class="back-link">← Quay lại chi tiết phòng</a>
                <span class="booking-code-tag">Khách sạn: <strong>${hotel.name}</strong></span>
            </div>

            <!-- 3 Steps Bar -->
            <div class="stepper-steps">
                <div class="step-item completed">
                    <span class="step-icon">✓</span>
                    <span class="step-text">BƯỚC 1<br><strong>Chọn phòng & giá</strong></span>
                </div>
                <div class="step-line active"></div>
                <div class="step-item active">
                    <span class="step-icon">2</span>
                    <span class="step-text">BƯỚC 2 · ĐANG TIẾN HÀNH<br><strong>Thông tin & Yêu cầu</strong></span>
                </div>
                <div class="step-line"></div>
                <div class="step-item ${success ? 'completed' : ''}">
                    <span class="step-icon">3</span>
                    <span class="step-text">BƯỚC 3<br><strong>Xác nhận & Thanh toán</strong></span>
                </div>
            </div>

            <!-- Price Timer -->
            <div class="timer-badge">
                ⏱️ Phòng đang được giữ giá tốt trong: <strong id="priceHoldTimer">14:59</strong>
            </div>
        </div>
    </div>

    <!-- Main Content Container -->
    <main class="main-container">
        <div class="checkout-grid">
            
            <!-- Left Main Form Column -->
            <div class="form-column">
                
                <form action="${pageContext.request.contextPath}/checkout" method="POST" id="mainBookingForm">
                    <input type="hidden" name="hotelId" value="${hotel.id}">
                    <input type="hidden" name="roomType" id="hiddenRoomType" value="${not empty roomName ? roomName : 'Phòng Deluxe Tiêu Chuẩn'}">
                    <input type="hidden" name="checkIn" value="${not empty checkIn ? checkIn : '2026-04-15'}">
                    <input type="hidden" name="checkOut" value="${not empty checkOut ? checkOut : '2026-04-18'}">
                    <input type="hidden" name="nights" value="3">
                    <input type="hidden" name="totalPrice" id="hiddenTotalPrice" value="${not empty roomPrice ? roomPrice * 3 : hotel.price * 3}">

                    <!-- Section 1: Guest Information -->
                    <section class="checkout-card">
                        <div class="card-title-row">
                            <h2 class="card-heading">1. Thông tin người đặt phòng</h2>
                            <span class="security-tag">🔒 Bảo mật 100%</span>
                        </div>
                        <p class="card-subtext">Thông tin này được dùng để xuất vé điện tử (E-voucher) nhận phòng.</p>

                        <!-- VIP Member Status Box -->
                        <c:if test="${not empty sessionScope.user}">
                            <div class="member-perk-box">
                                <div class="perk-icon">👑</div>
                                <div class="perk-content">
                                    <strong>Tài khoản thành viên: ${sessionScope.user.fullName}</strong> 
                                    <span class="gold-badge">HẠNG ${sessionScope.user.vipTier.toUpperCase()}</span>
                                    <p>Bạn đang được sử dụng mức tích điểm độc quyền <strong>+<fmt:formatNumber value="${(not empty roomPrice ? roomPrice * 3 : hotel.price * 3) / 10000}" type="number"/> V-Points</strong> vào tài khoản sau khi hoàn tất kỳ nghỉ.</p>
                                </div>
                            </div>
                        </c:if>

                        <div class="inputs-grid">
                            <div class="form-group full-width">
                                <label class="input-label">Họ và tên khách chính* <span class="hint">(Theo đúng CCCD/Hộ chiếu)</span></label>
                                <input type="text" name="guestName" id="guestName" class="form-input" value="${sessionScope.user != null ? sessionScope.user.fullName : 'Nguyễn Thùy Linh'}" required>
                            </div>

                            <div class="form-group">
                                <label class="input-label">Email nhận E-voucher*</label>
                                <input type="email" name="guestEmail" id="guestEmail" class="form-input" value="${sessionScope.user != null ? sessionScope.user.email : 'linh.nguyen@example.com'}" required>
                                <span class="input-hint">V-voucher & hóa đơn sẽ được gửi qua email này</span>
                            </div>

                            <div class="form-group">
                                <label class="input-label">Số điện thoại liên hệ*</label>
                                <input type="tel" name="guestPhone" id="guestPhone" class="form-input" value="${sessionScope.user != null && not empty sessionScope.user.phone ? sessionScope.user.phone : '0908123456'}" required>
                                <span class="input-hint">Nhận tin nhắn SMS & Zalo xác nhận phòng</span>
                            </div>

                            <div class="form-group full-width checkbox-row">
                                <label class="custom-checkbox">
                                    <input type="checkbox" id="isMainGuest" checked>
                                    <span class="checkmark"></span>
                                    Tôi là khách lưu trú chính
                                </label>
                            </div>
                        </div>
                    </section>

                    <!-- Section 2: Special Requests -->
                    <section class="checkout-card">
                        <div class="card-title-row">
                            <h2 class="card-heading">2. Yêu cầu đặc biệt</h2>
                            <span class="free-tag">Hoàn toàn miễn phí</span>
                        </div>
                        <p class="card-subtext">Các yêu cầu đặc biệt tùy thuộc vào tình trạng phòng thực tế của khu nghỉ dưỡng và sẽ được xác nhận khi nhận phòng.</p>

                        <div class="chips-group" id="requestChips">
                            <button type="button" class="chip-btn active" onclick="this.classList.toggle('active')">🌊 Phòng ở tầng cao view biển</button>
                            <button type="button" class="chip-btn active" onclick="this.classList.toggle('active')">🛏️ Giường lớn đôi (King Bed)</button>
                            <button type="button" class="chip-btn" onclick="this.classList.toggle('active')">⏰ Nhận phòng sớm (Tùy phòng trống)</button>
                            <button type="button" class="chip-btn active" onclick="this.classList.toggle('active')">🚭 Phòng không hút thuốc</button>
                        </div>
                        <input type="hidden" name="specialRequests" id="hiddenSpecialRequests" value="Phòng tầng cao, Giường King, Không hút thuốc">

                        <div class="form-group full-width" style="margin-top: 16px;">
                            <label class="input-label">Ghi chú thêm cho ${hotel.name} (Tùy chọn)</label>
                            <textarea name="specialNotes" id="specialNotes" class="form-textarea" rows="3" placeholder="Gia đình có chuyến bay sớm, xin hỗ trợ gửi hành lý trước và nhận phòng sớm nếu có sẵn."></textarea>
                        </div>
                    </section>

                    <!-- Section 3: Payment Methods -->
                    <section class="checkout-card">
                        <div class="card-title-row">
                            <h2 class="card-heading">3. Phương thức thanh toán</h2>
                            <span class="security-tag">Miễn phí giao dịch 0đ</span>
                        </div>
                        <p class="card-subtext">Đa dạng cổng thanh toán an toàn tại Việt Nam, không thu phụ phí chuyển khoản ngoại tệ.</p>

                        <div class="payment-methods-list">
                            <!-- MoMo -->
                            <label class="payment-option selected">
                                <input type="radio" name="paymentMethod" value="Ví điện tử MoMo" checked>
                                <div class="payment-body">
                                    <div class="payment-header">
                                        <span class="pay-logo momo-bg">momo</span>
                                        <strong>Ví điện tử MoMo</strong>
                                        <span class="pay-badge-orange">HOÀN TIỀN 50.000đ</span>
                                    </div>
                                    <p class="payment-desc">Quét mã QR bằng ứng dụng MoMo để thanh toán tức thì.</p>
                                </div>
                            </label>

                            <!-- VNPay -->
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="Cổng VNPAY / QR Ngân hàng">
                                <div class="payment-body">
                                    <div class="payment-header">
                                        <span class="pay-logo vnpay-bg">VNPAY</span>
                                        <strong>Cổng VNPAY / Quét mã VietQR</strong>
                                        <span class="pay-badge-green">TẤT CẢ NGÂN HÀNG VN</span>
                                    </div>
                                    <p class="payment-desc">Quét mã QR từ ứng dụng ngân hàng di động: Vietcombank, BIDV, MB, Techcombank, ACB...</p>
                                </div>
                            </label>

                            <!-- ZaloPay -->
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="Ví điện tử ZaloPay">
                                <div class="payment-body">
                                    <div class="payment-header">
                                        <span class="pay-logo zalopay-bg">ZaloPay</span>
                                        <strong>Ví điện tử ZaloPay</strong>
                                    </div>
                                    <p class="payment-desc">Xác nhận thanh toán trực tiếp qua ứng dụng Zalo hoặc ZaloPay.</p>
                                </div>
                            </label>

                            <!-- Card -->
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="Thẻ tín dụng / Ghi nợ quốc tế">
                                <div class="payment-body">
                                    <div class="payment-header">
                                        <span class="pay-logo card-bg">CARD</span>
                                        <strong>Thẻ Quốc tế (Visa, MasterCard, JCB)</strong>
                                    </div>
                                    <p class="payment-desc">Bảo mật chuẩn quốc tế PCI-DSS. Không tính phí giao dịch ngoại tệ.</p>
                                </div>
                            </label>

                            <!-- Pay at hotel -->
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="Thanh toán tại quầy khi Check-in">
                                <div class="payment-body">
                                    <div class="payment-header">
                                        <span class="pay-logo hotel-bg">HOTEL</span>
                                        <strong>Thanh toán tại khu nghỉ dưỡng khi Check-in</strong>
                                    </div>
                                    <p class="payment-desc">Giữ phòng đảm bảo. Thanh toán tiền mặt/thẻ khi làm thủ tục nhận phòng.</p>
                                </div>
                            </label>
                        </div>
                    </section>

                    <!-- Cancellation Policy Card -->
                    <div class="free-cancel-box">
                        <span class="cancel-icon">✅</span>
                        <div>
                            <strong>Miễn phí hủy phòng đến trước 24 giờ nhận phòng</strong>
                            <p>Bạn có thể hủy đặt phòng hoàn toàn miễn phí mà không bị thu bất kỳ khoản phí nào.</p>
                        </div>
                    </div>
                </form>

            </div>

            <!-- Right Summary Column -->
            <div class="summary-column">
                <div class="sticky-summary-card">
                    
                    <!-- Hotel Thumbnail & Rating -->
                    <div class="resort-summary-header">
                        <img src="${hotel.image}" alt="${hotel.name}" class="summary-thumb" onerror="this.src='https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500'">
                        <div class="resort-meta">
                            <div class="stars">
                                <c:forEach begin="1" end="${hotel.stars != null ? hotel.stars : 5}">★</c:forEach> ${hotel.stars != null ? hotel.stars : 5} sao
                            </div>
                            <h3 class="resort-title">${hotel.name}</h3>
                            <p class="resort-address">📍 ${hotel.address}</p>
                            <div class="score-row">
                                <span class="score-badge">${hotel.rating != null ? hotel.rating : '9.2'}</span>
                                <span class="score-txt"><strong>${not empty hotel.ratingText ? hotel.ratingText : 'Xuất sắc'}</strong> · ${hotel.reviewsCount != null ? hotel.reviewsCount : '120'} đánh giá</span>
                            </div>
                        </div>
                    </div>

                    <!-- Stay Dates Box -->
                    <div class="stay-dates-box">
                        <div class="date-col">
                            <span class="date-label">NHẬN PHÒNG</span>
                            <strong class="date-val">${not empty checkIn ? checkIn : '15/04/2026'}</strong>
                            <span class="time-hint">Từ 14:00</span>
                        </div>
                        <div class="arrow-col">➔</div>
                        <div class="date-col">
                            <span class="date-label">TRẢ PHÒNG</span>
                            <strong class="date-val">${not empty checkOut ? checkOut : '18/04/2026'}</strong>
                            <span class="time-hint">Trước 12:00</span>
                        </div>
                    </div>

                    <!-- Room Specification -->
                    <div class="room-spec-box">
                        <strong>🛏️ ${not empty roomName ? roomName : 'Phòng Deluxe VIP Hướng Biển'}</strong>
                        <p>👥 Thời gian: 3 đêm · 1 phòng cho 2 người lớn</p>
                        <span class="green-spec">🍽️ Bao gồm bữa sáng buffet hàng ngày</span>
                    </div>

                    <!-- Price Breakdown -->
                    <c:set var="finalNightPrice" value="${not empty roomPrice ? roomPrice : hotel.price}"/>
                    <c:set var="finalTotal" value="${finalNightPrice * 3}"/>
                    <c:set var="originalTotal" value="${finalTotal * 1.3}"/>
                    <c:set var="savings" value="${originalTotal - finalTotal}"/>

                    <div class="price-breakdown-list">
                        <div class="price-row">
                            <span>Giá phòng gốc (3 đêm)</span>
                            <span><fmt:formatNumber value="${originalTotal}" type="number"/> đ</span>
                        </div>
                        <div class="price-row discount">
                            <span>Ưu đãi Mùa hè Vstay (-25%)</span>
                            <span>-<fmt:formatNumber value="${savings}" type="number"/> đ</span>
                        </div>
                        <div class="price-row discount">
                            <span>Mã voucher độc quyền: <strong>VSTAY2026</strong></span>
                            <span>Đã áp dụng</span>
                        </div>
                        <span class="tax-included-hint">(Giá đã bao gồm thuế & phí dịch vụ)</span>
                    </div>

                    <!-- Total Price Display -->
                    <div class="total-price-row">
                        <div>
                            <span class="total-label">TỔNG CỘNG THANH TOÁN:</span>
                            <div class="total-price-val"><fmt:formatNumber value="${finalTotal}" type="number"/> đ</div>
                        </div>
                        <div class="vpoint-earn-tag">
                            +<fmt:formatNumber value="${finalTotal / 10000}" type="number"/> V-Points
                        </div>
                    </div>

                    <!-- Main Submit CTA -->
                    <button type="button" class="btn-submit-booking" id="btnCompleteBooking" onclick="submitBookingOrder()">
                        🔒 Hoàn tất đặt phòng & Thanh toán (<fmt:formatNumber value="${finalTotal}" type="number"/> đ)
                    </button>

                    <div class="guarantee-footer-notes">
                        <span>🔒 Giao dịch an toàn & bảo mật</span>
                        <span>✅ Xác nhận phòng tức thì trong 30 giây</span>
                        <span>📞 Cam kết giá tốt nhất · Không phí ẩn</span>
                    </div>

                    <!-- Hotline Support Box -->
                    <div class="hotline-card">
                        <div>
                            <strong>Cần hỗ trợ thanh toán?</strong>
                            <p>Hotline tư vấn đặt phòng 24/7</p>
                        </div>
                        <a href="tel:19006868" class="hotline-btn">1900 6868</a>
                    </div>

                </div>
            </div>

        </div>
    </main>

    <!-- Payment Success / Loading Modal -->
    <div id="paymentModal" class="modal-overlay ${success ? 'active' : ''}">
        <div class="modal-content">
            <div id="modalLoadingState" class="${success ? 'hidden' : ''}">
                <div class="modal-spinner"></div>
                <h3 class="modal-title">Đang xử lý thanh toán...</h3>
                <p class="modal-desc">Hệ thống đang kết nối ngân hàng và lưu đơn đặt phòng vào cơ sở dữ liệu Vstay.</p>
            </div>

            <div id="modalSuccessState" class="${success ? '' : 'hidden'}">
                <div class="success-icon">🎉</div>
                <h3 class="modal-title">Đặt phòng thành công!</h3>
                <p class="modal-desc">Đơn đặt phòng đã được ghi nhận vào hệ thống Vstay thành công.</p>
                
                <div class="success-ticket">
                    <div class="ticket-row">
                        <span>Mã đặt phòng:</span>
                        <strong id="confirmedCode" style="color:#0062E0;">${booking != null ? booking.bookingCode : param.code}</strong>
                    </div>
                    <div class="ticket-row">
                        <span>Khách sạn:</span>
                        <strong>${booking != null ? booking.hotelName : hotel.name}</strong>
                    </div>
                    <div class="ticket-row">
                        <span>Hạng phòng:</span>
                        <strong>${booking != null ? booking.roomType : roomName}</strong>
                    </div>
                    <div class="ticket-row">
                        <span>Số tiền đã thanh toán:</span>
                        <strong class="green-text"><fmt:formatNumber value="${booking != null ? booking.totalPrice : finalTotal}" type="number"/> đ</strong>
                    </div>
                    <div class="ticket-row">
                        <span>Khách nhận phòng:</span>
                        <strong id="confirmedEmail">${booking != null ? booking.guestName : ''} (${booking != null ? booking.guestEmail : ''})</strong>
                    </div>
                </div>

                <div class="modal-actions" style="margin-top: 20px; display:flex; gap:10px; justify-content:center;">
                    <a href="${pageContext.request.contextPath}/" class="btn-primary" style="text-decoration:none; display:inline-block; padding:10px 20px;">Về trang chủ</a>
                    <a href="${pageContext.request.contextPath}/account" class="btn-outline-sm" style="text-decoration:none; display:inline-block; padding:10px 20px;">Xem trong Tài khoản</a>
                    <button class="btn-outline-sm" onclick="window.print()" type="button">In vé E-Voucher</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="includes/footer.jsp"/>

    <script>
        function submitBookingOrder() {
            const form = document.getElementById("mainBookingForm");
            if (!form.checkValidity()) {
                form.reportValidity();
                return;
            }

            // Gather active chips
            const activeChips = Array.from(document.querySelectorAll("#requestChips .chip-btn.active"))
                                     .map(c => c.textContent.trim());
            document.getElementById("hiddenSpecialRequests").value = activeChips.join(", ");

            // Show loading overlay
            const modal = document.getElementById("paymentModal");
            const loadingState = document.getElementById("modalLoadingState");
            const successState = document.getElementById("modalSuccessState");
            
            modal.classList.add("active");
            loadingState.classList.remove("hidden");
            successState.classList.add("hidden");

            // Submit after 1.5s simulated payment animation
            setTimeout(() => {
                form.submit();
            }, 1200);
        }

        // Stepper timer countdown
        let timerSeconds = 14 * 60 + 59;
        const timerEl = document.getElementById("priceHoldTimer");
        if (timerEl) {
            setInterval(() => {
                if (timerSeconds > 0) {
                    timerSeconds--;
                    const m = Math.floor(timerSeconds / 60);
                    const s = timerSeconds % 60;
                    timerEl.textContent = String(m).padStart(2, "0") + ":" + String(s).padStart(2, "0");
                }
            }, 1000);
        }
    </script>
</body>
</html>
