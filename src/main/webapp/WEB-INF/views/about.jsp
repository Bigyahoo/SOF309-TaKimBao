<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Về Chúng Tôi & Liên Hệ | Vstay.vn - Nền tảng đặt phòng hàng đầu Việt Nam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
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
    </style>
</head>
<body>

    <jsp:include page="includes/header.jsp"/>

    <!-- Main Content -->
    <main class="about-main">

        <!-- Section 1: Hero Banner -->
        <section class="about-hero">
            <div class="hero-container">
                <div class="hero-badge-pill">
                    ✨ Nền tảng đặt phòng nghỉ dưỡng hàng đầu Việt Nam
                </div>
                <h1 class="hero-title">
                    Kiến tạo hành trình nghỉ dưỡng <span class="highlight">trọn vẹn</span> tại Việt Nam
                </h1>
                <p class="hero-description">
                    Vstay.vn là hệ thống đặt phòng khách sạn, resort và biệt thự hàng đầu tại Việt Nam, kết nối hàng triệu du khách đến với những không gian lưu trú sang trọng, ấm cúng cùng dịch vụ hoàn hảo.
                </p>
                <div class="hero-actions">
                    <a href="${pageContext.request.contextPath}/search" class="btn-primary btn-lg">Khám phá phòng ngay</a>
                    <a href="#contactSection" class="btn-secondary btn-lg">📞 Liên hệ tư vấn 24/7</a>
                </div>
            </div>
        </section>

        <!-- Section 2: Featured Showcase Grid -->
        <section class="section-container">
            <div class="showcase-grid">
                <!-- Card 1: Large Featured Card -->
                <div class="showcase-card large-card">
                    <img src="https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=900&auto=format&fit=crop" alt="Resort sang trọng & Biệt thự biển">
                    <div class="card-overlay"></div>
                    <div class="card-content">
                        <span class="card-pill">BÁN CHẠY NHẤT</span>
                        <h3>Khu nghỉ dưỡng sang trọng & Biệt thự biển</h3>
                        <p>Hơn 4.500 resort & biệt thự ven biển cao cấp trên toàn quốc</p>
                    </div>
                </div>

                <!-- Card 2: Right Top Card -->
                <div class="showcase-card small-card">
                    <img src="https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600&auto=format&fit=crop" alt="Khách sạn boutique">
                    <div class="card-overlay"></div>
                    <div class="card-content">
                        <span class="card-pill tag-gold">KHÁCH SẠN ĐÔ THỊ</span>
                        <h3>Khách sạn boutique & Lifestyle</h3>
                        <p>Tọa lạc tại trung tâm các thành phố du lịch sầm uất nhất</p>
                    </div>
                </div>

                <!-- Card 3: Right Bottom Card -->
                <div class="showcase-card small-card">
                    <img src="https://images.unsplash.com/photo-1528127269322-539801943592?w=600&auto=format&fit=crop" alt="Bungalow & Homestay độc đáo">
                    <div class="card-overlay"></div>
                    <div class="card-content">
                        <span class="card-pill tag-green">GẦN GŨI THIÊN NHIÊN</span>
                        <h3>Bungalow & Homestay núi rừng</h3>
                        <p>Trải nghiệm mộc mạc, yên bình tại Sa Pa, Đà Lạt, Hà Giang...</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Section 3: Key Metrics Strip -->
        <section class="section-container">
            <div class="stats-card-strip">
                <div class="stat-item">
                    <div class="stat-number">12.000 +</div>
                    <div class="stat-title">Chỗ nghỉ cao cấp</div>
                    <div class="stat-subtitle">Tất cả 63 tỉnh thành</div>
                </div>
                <div class="stat-divider"></div>
                <div class="stat-item">
                    <div class="stat-number">500.000 +</div>
                    <div class="stat-title">Khách hàng tin tưởng</div>
                    <div class="stat-subtitle">Đã phục vụ hơn 1.2M lượt ở</div>
                </div>
                <div class="stat-divider"></div>
                <div class="stat-item">
                    <div class="stat-number">98.6%</div>
                    <div class="stat-title">Hài lòng & Đánh giá cao</div>
                    <div class="stat-subtitle">Dựa trên 120.000+ review</div>
                </div>
                <div class="stat-divider"></div>
                <div class="stat-item">
                    <div class="stat-number">63 / 63</div>
                    <div class="stat-title">Tỉnh thành bao phủ</div>
                    <div class="stat-subtitle">Toàn bộ điểm đến Việt Nam</div>
                </div>
                <div class="stat-divider"></div>
                <div class="stat-item">
                    <div class="stat-number">24/7</div>
                    <div class="stat-title">Hỗ trợ chuyên sâu</div>
                    <div class="stat-subtitle">Đội ngũ tư vấn địa phương</div>
                </div>
            </div>
        </section>

        <!-- Section 4: Brand Story & Values -->
        <section class="section-container">
            <div class="brand-story-grid">
                <div class="story-content">
                    <span class="section-subtitle-pill">CÂU CHUYỆN CỦA VSTAY</span>
                    <h2 class="story-heading">Sứ mệnh mang lại kỳ nghỉ trọn vẹn từng khoảnh khắc</h2>
                    <p class="story-text">
                        Thành lập từ năm 2020 bởi những người trẻ đam mê du lịch và công nghệ, Vstay.vn ra đời với mong muốn giải quyết bài toán: tìm kiếm và đặt phòng nghỉ dưỡng tại Việt Nam một cách minh bạch, an toàn và dễ dàng nhất.
                    </p>
                    <p class="story-text">
                        Từ những resort 5 sao quốc tế hướng biển đến những homestay mộc mạc nép mình giữa sương mù Tây Bắc, chúng tôi tin rằng mỗi chuyến đi đều là một cơ hội để kết nối và tái tạo năng lượng sống.
                    </p>
                    <div class="core-values-list">
                        <div class="value-item">
                            <span class="value-icon">💎</span>
                            <div>
                                <strong>Minh bạch tuyệt đối</strong>
                                <p>Giá hiển thị luôn là giá cuối cùng, không phụ phí ẩn, không phụ thu bất ngờ.</p>
                            </div>
                        </div>
                        <div class="value-item">
                            <span class="value-icon">🤝</span>
                            <div>
                                <strong>Đồng hành bản địa</strong>
                                <p>Hỗ trợ du khách 24/7 bởi đội ngũ am hiểu từng điểm đến tại Việt Nam.</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="story-image-wrapper">
                    <img src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800&auto=format&fit=crop" alt="Vstay Brand Experience" class="story-img">
                </div>
            </div>
        </section>

        <!-- Section 5: Team Members -->
        <section class="section-container">
            <div class="section-title-center">
                <span class="section-subtitle-pill">ĐỘI NGŨ SÁNG LẬP</span>
                <h2 class="section-heading-lg">Những con người xây dựng giấc mơ Vstay</h2>
            </div>

            <div class="team-grid">
                <div class="team-card">
                    <div class="team-avatar-wrapper">
                        <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop" alt="Nguyễn Tuấn Anh">
                        <span class="team-role-pill">CEO & Founder</span>
                    </div>
                    <div class="team-info">
                        <h3 class="member-name">Nguyễn Tuấn Anh</h3>
                        <p class="member-title">Tổng Giám Đốc Điều Hành</p>
                        <p class="member-bio">Hơn 12 năm kinh nghiệm trong ngành du lịch khách sạn và công nghệ số tại Đông Nam Á.</p>
                    </div>
                </div>

                <div class="team-card">
                    <div class="team-avatar-wrapper">
                        <img src="https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&auto=format&fit=crop" alt="Trần Đức Minh">
                        <span class="team-role-pill">CTO</span>
                    </div>
                    <div class="team-info">
                        <h3 class="member-name">Trần Đức Minh</h3>
                        <p class="member-title">Giám Đốc Công Nghệ</p>
                        <p class="member-bio">Chuyên gia kiến trúc hệ thống phân tán, định hình nền tảng đặt phòng hiệu năng cao.</p>
                    </div>
                </div>

                <div class="team-card">
                    <div class="team-avatar-wrapper">
                        <img src="https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400&auto=format&fit=crop" alt="Hoàng Mai Anh">
                        <span class="team-role-pill">CCO</span>
                    </div>
                    <div class="team-info">
                        <h3 class="member-name">Hoàng Mai Anh</h3>
                        <p class="member-title">Giám Đốc Trải Nghiệm Khách Hàng</p>
                        <p class="member-bio">Tâm huyết trong việc xây dựng chuẩn mực dịch vụ chăm sóc khách hàng 24/7 chuẩn 5 sao.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Section 6: Security & Trust Strip -->
        <section class="section-container">
            <div class="section-title-center compact">
                <span class="section-subtitle-pill">ĐỐI TÁC VÀ BẢO MẬT</span>
                <h2 class="section-heading-lg">Bảo chứng an toàn & Năng lực kết nối</h2>
            </div>

            <div class="security-grid">
                <div class="security-card">
                    <div class="sec-icon icon-green"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#20A56A" stroke-width="2.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><path d="M9 12l2 2 4-4"/></svg></div>
                    <div class="sec-info">
                        <strong>Bộ Công Thương</strong>
                        <span>Đã đăng ký & thông báo chính thức</span>
                    </div>
                </div>

                <div class="security-card">
                    <div class="sec-icon icon-blue"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#0057C2" stroke-width="2.5"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg></div>
                    <div class="sec-info">
                        <strong>PCI-DSS Level 1</strong>
                        <span>Bảo mật thanh toán chuẩn quốc tế</span>
                    </div>
                </div>

                <div class="security-card">
                    <div class="sec-icon icon-amber"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#795500" stroke-width="2.5"><path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg></div>
                    <div class="sec-info">
                        <strong>Cam kết Giá Tốt Nhất</strong>
                        <span>Hoàn tiền nếu ở đâu rẻ hơn</span>
                    </div>
                </div>

                <div class="security-card">
                    <div class="sec-icon icon-gold"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#8F5E00" stroke-width="2.5"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg></div>
                    <div class="sec-info">
                        <strong>Xác thực 100% Chỗ ở</strong>
                        <span>Kiểm định thực tế định kỳ</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- Section 7: Contact Form & Support Grid -->
        <section class="section-container" id="contactSection">
            <div class="contact-section-wrapper">
                <div class="contact-header">
                    <span class="section-subtitle-pill">LIÊN HỆ & HỖ TRỢ 24/7</span>
                    <h2 class="section-heading-lg">Liên hệ với Vstay.vn — Luôn sẵn sàng đồng hành</h2>
                    <p class="section-desc">Bất kể bạn có câu hỏi về đơn đặt phòng, cần tư vấn lịch trình hay hỗ trợ doanh nghiệp, đội ngũ Vstay.vn luôn túc trực hỗ trợ.</p>
                </div>

                <div class="contact-grid">
                    <!-- Left: Support Hotline & Offices -->
                    <div class="contact-left">
                        <div class="support-hotline-card">
                            <div class="hotline-badge">TỔNG ĐÀI CHĂM SÓC KHÁCH HÀNG</div>
                            <a href="tel:19006868" class="hotline-main-number">1900 6868</a>
                            <p class="hotline-desc">Phục vụ 24 giờ mỗi ngày, 7 ngày trong tuần (Miễn phí cước cuộc gọi)</p>
                            <div class="hotline-tags">
                                <span>💬 Zalo Official: @VstayVN</span>
                                <span>✉️ Email: cskh@vstay.vn</span>
                            </div>
                        </div>

                        <div class="office-cards-list">
                            <div class="office-card">
                                <div class="office-city-tag tag-blue">TRỤ SỞ CHÍNH HÀ NỘI</div>
                                <h4>Vstay.vn Building Hà Nội</h4>
                                <p>📍 Tầng 12, Tòa nhà Capital Tower, 109 Trần Hưng Đạo, Q. Hoàn Kiếm, Hà Nội</p>
                                <span class="office-phone">📞 Tel: (024) 3988 6868</span>
                            </div>

                            <div class="office-card">
                                <div class="office-city-tag tag-gold">CHI NHÁNH TP. HỒ CHÍ MINH</div>
                                <h4>Vstay.vn Center Sài Gòn</h4>
                                <p>📍 Tầng 8, Tòa nhà Saigon Centre Tower 2, 67 Lê Lợi, Quận 1, TP. Hồ Chí Minh</p>
                                <span class="office-phone">📞 Tel: (028) 3822 6868</span>
                            </div>

                            <div class="office-card">
                                <div class="office-city-tag tag-green">CHI NHÁNH ĐÀ NẴNG</div>
                                <h4>Vstay.vn Office Đà Nẵng</h4>
                                <p>📍 Tầng 5, Tòa nhà Indochina Riverside, 74 Bạch Đằng, Q. Hải Châu, Đà Nẵng</p>
                                <span class="office-phone">📞 Tel: (0236) 389 6868</span>
                            </div>
                        </div>
                    </div>

                    <!-- Right: Contact Form -->
                    <div class="contact-right">
                        <div class="contact-form-card">
                            <h3 class="form-title">Gửi thông điệp tới Vstay.vn</h3>
                            <p class="form-subtitle">Điền thông tin dưới đây, tư vấn viên sẽ liên hệ lại với bạn trong vòng 15 phút.</p>

                            <c:if test="${contactSuccess}">
                                <div class="alert-success">✓ Cảm ơn bạn! Thông điệp đã được gửi thành công đến đội ngũ Vstay.vn. Chúng tôi sẽ phản hồi trong giây lát.</div>
                            </c:if>
                            <c:if test="${not empty contactError}">
                                <div class="alert-error">${contactError}</div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/about" method="POST" class="form-body">
                                <div class="form-group">
                                    <label for="contactName" class="form-label">Họ và tên *</label>
                                    <input type="text" name="name" id="contactName" class="form-input" placeholder="Ví dụ: Nguyễn Văn A" required>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="contactPhone" class="form-label">Số điện thoại *</label>
                                        <input type="tel" name="phone" id="contactPhone" class="form-input" placeholder="Ví dụ: 0912 345 678" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="contactEmail" class="form-label">Email phản hồi *</label>
                                        <input type="email" name="email" id="contactEmail" class="form-input" placeholder="name@domain.com" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="contactTopic" class="form-label">Chủ đề cần hỗ trợ</label>
                                    <select name="topic" id="contactTopic" class="form-select">
                                        <option value="Tư vấn đặt phòng">Tư vấn đặt phòng & Resort</option>
                                        <option value="Hỗ trợ đơn hàng">Hỗ trợ đơn hàng hiện tại</option>
                                        <option value="Hợp tác chỗ nghỉ">Hợp tác đối tác chỗ nghỉ</option>
                                        <option value="Góp ý dịch vụ">Góp ý chất lượng dịch vụ</option>
                                        <option value="Khác">Chủ đề khác</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="contactMessage" class="form-label">Nội dung thông điệp *</label>
                                    <textarea name="message" id="contactMessage" class="form-textarea" rows="4" placeholder="Nhập chi tiết yêu cầu hoặc câu hỏi của bạn..." required></textarea>
                                </div>

                                <button type="submit" class="btn-submit-contact">
                                    <span>Gửi yêu cầu hỗ trợ ngay ➔</span>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    </main>

    <jsp:include page="includes/footer.jsp"/>

</body>
</html>
