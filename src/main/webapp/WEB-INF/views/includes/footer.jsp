<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Footer Section -->
<footer class="footer">
    <div class="footer-container">
        <!-- 4 Column Grid -->
        <div class="footer-grid">
            <!-- Brand & Info -->
            <div class="footer-col">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Vstay.vn Logo" class="logo-img" onerror="this.src='https://placehold.co/120x40/0062E0/FFF?text=Vstay.vn'">
                </a>
                <p class="brand-desc">
                    Vstay.vn là nền tảng đặt phòng khách sạn, resort nghỉ dưỡng và homestay trực tuyến hàng đầu tại Việt Nam.
                </p>
                <div class="contact-item">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
                    <span>Hotline: 1900 6868 (24/7)</span>
                </div>
                <div class="contact-item">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
                    <span>hotro@vstay.vn</span>
                </div>
            </div>

            <!-- Top Destinations -->
            <div class="footer-col">
                <h4 class="footer-heading">Điểm Đến Hàng Đầu</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/search?location=Đà Nẵng">Khách sạn Đà Nẵng</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?location=Hà Nội">Khách sạn Hà Nội</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?location=Hồ Chí Minh">Khách sạn TP. Hồ Chí Minh</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?location=Phú Quốc">Resort Phú Quốc</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?location=Đà Lạt">Homestay Đà Lạt</a></li>
                </ul>
            </div>

            <!-- Property Types -->
            <div class="footer-col">
                <h4 class="footer-heading">Loại Chỗ Nghỉ</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/search?category=Resort">Resort Nghỉ Dưỡng Ven Biển</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?category=Khách sạn">Khách Sạn 5 Sao Cao Cấp</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?category=Villa">Villa & Biệt Thự Gia Đình</a></li>
                    <li><a href="${pageContext.request.contextPath}/search?category=Homestay">Homestay Sống Ảo Độc Đáo</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin">Cổng Quản Trị Hệ Thống</a></li>
                </ul>
            </div>

            <!-- Help & Info -->
            <div class="footer-col">
                <h4 class="footer-heading">Thông Tin & Hỗ Trợ</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/about">Về chúng tôi</a></li>
                    <li><a href="${pageContext.request.contextPath}/about">Liên hệ & Góp ý</a></li>
                    <li><a href="#">Quy chế hoạt động</a></li>
                    <li><a href="#">Chính sách bảo mật</a></li>
                    <li><a href="#">Giải quyết khiếu nại</a></li>
                </ul>
            </div>
        </div>

        <!-- Partner Payment Methods & Security Seals Bar -->
        <div class="payment-bar">
            <div class="payment-left">
                <span class="payment-title">PHƯƠNG THỨC THANH TOÁN ĐỐI TÁC</span>
                <div class="payment-icons">
                    <span class="pay-chip">MoMo</span>
                    <span class="pay-chip">ZaloPay</span>
                    <span class="pay-chip">VNPay</span>
                    <span class="pay-chip">VietQR</span>
                    <span class="pay-chip">VISA</span>
                    <span class="pay-chip text-red">Mastercard</span>
                    <span class="pay-chip text-blue">JCB</span>
                </div>
            </div>
            <div class="payment-right">
                <span class="trust-seal seal-green">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><path d="M9 12l2 2 4-4"/></svg>
                    Bộ Công Thương đã thông báo
                </span>
                <span class="trust-seal seal-blue">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                    Bảo mật PCI-DSS
                </span>
                <span class="trust-seal seal-gold">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
                    Cam kết giá tốt nhất
                </span>
            </div>
        </div>

        <!-- Copyright Line -->
        <div class="copyright-bar">
            <p>Copyright © 2026 Vstay.vn - Nền tảng đặt phòng khách sạn & resort hàng đầu Việt Nam.</p>
            <p class="demo-note">Hệ thống Quản lý Khách sạn Java Servlet & Hibernate ORM</p>
        </div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/vstay.js"></script>
