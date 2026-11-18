<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vstay.vn Admin - Quản Lý Địa Điểm & Khách Sạn</title>
    
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css">
</head>
<body>

    <header>
        <div class="header-container">
            <div class="brand-box">
                <a href="${pageContext.request.contextPath}/" style="text-decoration:none;" class="brand-logo">
                    Vstay<span>.vn</span>
                </a>
                <div class="admin-badge">Admin Portal (Hibernate ORM)</div>
            </div>

            <div class="status-container">
                <div class="connection-status">
                    <div id="statusDot" class="status-dot"></div>
                    <span id="statusText">MS SQL Server Đang Kết Nối</span>
                </div>
                <a href="${pageContext.request.contextPath}/seed?force=true" class="btn btn-seed btn-sm" onclick="return confirm('Bạn có muốn nạp lại dữ liệu 40 khách sạn mẫu?');">
                    ✨ Nạp Lại Dữ Liệu Mẫu
                </a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary btn-sm">
                    🏠 Xem Website
                </a>
            </div>
        </div>
    </header>

    <section class="stats-grid">
        <div class="stat-card">
            <div class="stat-info">
                <h4>Tổng Khách Sạn & Resort</h4>
                <div id="statTotal">${totalHotels}</div>
            </div>
            <div class="stat-icon">🏨</div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <h4>Đơn Đặt Phòng Hệ Thống</h4>
                <div id="statFeatured">${totalBookings}</div>
            </div>
            <div class="stat-icon">📋</div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <h4>Tài Khoản Đang Quản Trị</h4>
                <div id="statCities" style="font-size:16px; font-weight:700;">${sessionScope.user.email}</div>
            </div>
            <div class="stat-icon">👑</div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <h4>Công Nghệ Nền Tảng</h4>
                <div id="statAvgRating" style="font-size:16px; font-weight:700;">Java 17 + Jakarta</div>
            </div>
            <div class="stat-icon">☕</div>
        </div>
    </section>

    <main class="main-container">

        <!-- LEFT PANEL: Add / Edit Form -->
        <aside class="panel-card sticky-panel">
            <div class="panel-title">
                <span id="formTitle">Thêm Khách Sạn Mới</span>
                <button type="button" id="btnResetForm" class="btn btn-secondary btn-sm" style="display: none;" onclick="resetFormToCreate()">Hủy Chỉnh Sửa</button>
            </div>

            <form action="${pageContext.request.contextPath}/admin" method="POST" id="locationForm" class="form-grid">
                <input type="hidden" name="action" id="formAction" value="create">
                <input type="hidden" name="id" id="editHotelId" value="">

                <!-- Property Name -->
                <div class="form-group">
                    <label for="propName">Tên Khách Sạn / Resort / Chỗ Nghỉ *</label>
                    <input type="text" name="name" id="propName" class="form-control" placeholder="Ví dụ: Furama Resort Đà Nẵng" required>
                </div>

                <!-- Category & City -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="propCategory">Loại Hình *</label>
                        <select name="category" id="propCategory" class="form-control" required>
                            <option value="Resort">Resort nghỉ dưỡng</option>
                            <option value="Khách sạn">Khách sạn 4-5 sao</option>
                            <option value="Villa">Biệt thự / Villa</option>
                            <option value="Homestay">Homestay / Căn hộ</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="propCity">Tỉnh / Thành Phố *</label>
                        <select name="city" id="propCity" class="form-control" required>
                            <option value="Đà Nẵng">Đà Nẵng</option>
                            <option value="Phú Quốc">Phú Quốc</option>
                            <option value="Hội An">Hội An</option>
                            <option value="Hồ Chí Minh">TP. Hồ Chí Minh</option>
                            <option value="Hà Nội">Hà Nội</option>
                            <option value="Nha Trang">Nha Trang</option>
                            <option value="Đà Lạt">Đà Lạt</option>
                            <option value="Huế">Huế</option>
                            <option value="Sa Pa">Sa Pa</option>
                            <option value="Vũng Tàu">Vũng Tàu</option>
                        </select>
                    </div>
                </div>

                <!-- Detailed Address -->
                <div class="form-group">
                    <label for="propAddress">Địa Chỉ Chi Tiết *</label>
                    <input type="text" name="address" id="propAddress" class="form-control" placeholder="Ví dụ: 105 Võ Nguyên Giáp, Ngũ Hành Sơn, Đà Nẵng" required>
                </div>

                <!-- Price & Old Price -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="propPrice">Giá Phòng / Đêm (VND) *</label>
                        <input type="number" name="price" id="propPrice" class="form-control" min="100000" step="50000" placeholder="2500000" required oninput="formatPriceDisplay(this.value)">
                        <div id="priceFormatted" class="price-badge">0 ₫ / đêm</div>
                    </div>

                    <div class="form-group">
                        <label for="propOldPrice">Giá Gốc Trước Giảm (VND)</label>
                        <input type="number" name="oldPrice" id="propOldPrice" class="form-control" min="0" step="50000" placeholder="3200000">
                    </div>
                </div>

                <!-- Stars & Rating Score -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="propStars">Số Sao (1 - 5)</label>
                        <select name="stars" id="propStars" class="form-control">
                            <option value="5">5 ⭐⭐⭐⭐⭐</option>
                            <option value="4">4 ⭐⭐⭐⭐</option>
                            <option value="3">3 ⭐⭐⭐</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="propRating">Điểm Đánh Giá (e.g. 9.4)</label>
                        <input type="number" name="rating" id="propRating" class="form-control" min="1" max="10" step="0.1" value="9.0">
                    </div>
                </div>

                <!-- Main Image URL & Preview -->
                <div class="form-group">
                    <label for="propImage">URL Ảnh Đại Diện *</label>
                    <input type="url" name="image" id="propImage" class="form-control" placeholder="https://images.unsplash.com/..." required oninput="document.getElementById('previewImg').src = this.value; document.getElementById('previewImg').style.display = 'block';">
                    <div class="image-preview-wrapper" style="margin-top:8px;">
                        <img id="previewImg" src="" alt="Preview" style="max-height:120px; border-radius:6px; display:none; object-fit:cover; width:100%;">
                    </div>
                </div>

                <!-- Badge & Distance -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="propBadge">Huy Hiệu (Badge)</label>
                        <input type="text" name="badge" id="propBadge" class="form-control" placeholder="Ví dụ: ƯU ĐÃI ĐẶC BIỆT">
                    </div>

                    <div class="form-group">
                        <label for="propDistance">Khoảng Cách</label>
                        <input type="text" name="distance" id="propDistance" class="form-control" placeholder="Cách bãi biển 50m">
                    </div>
                </div>

                <!-- Inclusions & Amenities -->
                <div class="form-group">
                    <label for="propInclusions">Gói Bao Gồm (Ngăn cách bằng dấu gạch đứng |)</label>
                    <input type="text" name="inclusions" id="propInclusions" class="form-control" placeholder="Buffet sáng|Xe đưa đón|Hồ bơi vô cực">
                </div>

                <!-- Description -->
                <div class="form-group">
                    <label for="propDesc">Mô Tả Chỗ Nghỉ</label>
                    <textarea name="description" id="propDesc" class="form-control" rows="3" placeholder="Không gian nghỉ dưỡng sang trọng bên bờ biển..."></textarea>
                </div>

                <!-- Featured Toggle -->
                <div class="form-group" style="display:flex; align-items:center; gap:8px;">
                    <input type="checkbox" name="featured" id="propFeatured" value="true">
                    <label for="propFeatured" style="margin:0; cursor:pointer;">Đánh dấu là Chỗ Nghỉ Nổi Bật (Hiện trang chủ)</label>
                </div>

                <button type="submit" class="btn btn-primary" id="btnSubmitForm" style="width:100%; padding:12px; margin-top:10px;">
                    💾 Lưu Khách Sạn Vào Hệ Thống
                </button>
            </form>
        </aside>

        <!-- RIGHT PANEL: Hotel List & Actions -->
        <section class="panel-card">
            <div class="list-header">
                <h3>Danh Sách Khách Sạn (${totalHotels})</h3>
                <div class="filter-tools" style="display:flex; gap:10px;">
                    <input type="text" id="adminSearchInput" class="form-control" placeholder="Tìm tên khách sạn..." oninput="filterAdminTable(this.value)" style="width:220px;">
                </div>
            </div>

            <div class="table-responsive" style="overflow-x:auto;">
                <table class="data-table" id="adminHotelsTable" style="width:100%; border-collapse:collapse;">
                    <thead>
                        <tr style="background:#f1f5f9; text-align:left; font-size:13px; color:#475569;">
                            <th style="padding:10px;">Ảnh</th>
                            <th style="padding:10px;">Tên & Địa Chỉ</th>
                            <th style="padding:10px;">Loại / Tỉnh</th>
                            <th style="padding:10px;">Giá / Đêm</th>
                            <th style="padding:10px;">Sao / Điểm</th>
                            <th style="padding:10px;">Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="h" items="${hotels}">
                            <tr style="border-bottom:1px solid #e2e8f0; font-size:13px;" data-hotel-row="${h.id}">
                                <td style="padding:10px;">
                                    <img src="${h.image}" alt="${h.name}" style="width:60px; height:45px; border-radius:6px; object-fit:cover;" onerror="this.src='https://images.unsplash.com/photo-1566073771259-6a8506099945?w=200'">
                                </td>
                                <td style="padding:10px;">
                                    <strong>${h.name}</strong>
                                    <c:if test="${h.featured}">
                                        <span style="background:#fef3c7; color:#b45309; padding:2px 6px; border-radius:4px; font-size:10px; font-weight:700;">Nổi Bật</span>
                                    </c:if>
                                    <div style="color:#64748b; font-size:12px;">${h.address}</div>
                                </td>
                                <td style="padding:10px;">
                                    <span style="font-weight:600;">${h.category}</span>
                                    <div style="color:#64748b; font-size:12px;">${h.city}</div>
                                </td>
                                <td style="padding:10px; font-weight:700; color:#0062E0;">
                                    <fmt:formatNumber value="${h.price}" type="number"/> đ
                                </td>
                                <td style="padding:10px;">
                                    <div>${h.stars} ⭐</div>
                                    <span style="color:#16a34a; font-weight:700;">${h.rating}</span>
                                </td>
                                <td style="padding:10px;">
                                    <div style="display:flex; gap:6px;">
                                        <button type="button" class="btn btn-secondary btn-sm" onclick="populateEditForm(${h.id}, '${h.name}', '${h.category}', '${h.city}', '${h.address}', ${h.price}, '${h.oldPrice}', ${h.stars}, ${h.rating}, '${h.image}', '${h.badge}', '${h.distance}', '${h.inclusions}', '${h.featured}')">
                                            ✏️ Sửa
                                        </button>
                                        <form action="${pageContext.request.contextPath}/admin" method="POST" onsubmit="return confirm('Bạn có chắc muốn xóa khách sạn này?');" style="margin:0;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${h.id}">
                                            <button type="submit" class="btn btn-danger btn-sm" style="background:#ef4444; color:white; border:none; padding:4px 8px; border-radius:4px; cursor:pointer;">
                                                🗑️ Xóa
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

    </main>

    <script>
        function formatPriceDisplay(val) {
            const num = parseInt(val, 10);
            if (!isNaN(num)) {
                document.getElementById('priceFormatted').textContent = new Intl.NumberFormat('vi-VN').format(num) + ' ₫ / đêm';
            } else {
                document.getElementById('priceFormatted').textContent = '0 ₫ / đêm';
            }
        }

        function populateEditForm(id, name, category, city, address, price, oldPrice, stars, rating, image, badge, distance, inclusions, featured) {
            document.getElementById("formTitle").textContent = "Chỉnh Sửa Khách Sạn #" + id;
            document.getElementById("formAction").value = "update";
            document.getElementById("editHotelId").value = id;
            document.getElementById("btnResetForm").style.display = "inline-block";

            document.getElementById("propName").value = name || "";
            document.getElementById("propCategory").value = category || "Resort";
            document.getElementById("propCity").value = city || "Đà Nẵng";
            document.getElementById("propAddress").value = address || "";
            document.getElementById("propPrice").value = price || 0;
            formatPriceDisplay(price);

            document.getElementById("propOldPrice").value = oldPrice !== 'null' && oldPrice ? oldPrice : "";
            document.getElementById("propStars").value = stars || 5;
            document.getElementById("propRating").value = rating || 9.0;
            document.getElementById("propImage").value = image || "";
            document.getElementById("propBadge").value = badge !== 'null' && badge ? badge : "";
            document.getElementById("propDistance").value = distance !== 'null' && distance ? distance : "";
            document.getElementById("propInclusions").value = inclusions !== 'null' && inclusions ? inclusions : "";
            document.getElementById("propFeatured").checked = (featured === "true");

            const preview = document.getElementById("previewImg");
            if (image) {
                preview.src = image;
                preview.style.display = "block";
            }

            window.scrollTo({ top: 0, behavior: 'smooth' });
        }

        function resetFormToCreate() {
            document.getElementById("locationForm").reset();
            document.getElementById("formTitle").textContent = "Thêm Khách Sạn Mới";
            document.getElementById("formAction").value = "create";
            document.getElementById("editHotelId").value = "";
            document.getElementById("btnResetForm").style.display = "none";
            document.getElementById("previewImg").style.display = "none";
            document.getElementById("priceFormatted").textContent = "0 ₫ / đêm";
        }

        function filterAdminTable(query) {
            const q = query.toLowerCase().trim();
            const rows = document.querySelectorAll("#adminHotelsTable tbody tr");
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(q) ? "" : "none";
            });
        }
    </script>
</body>
</html>
