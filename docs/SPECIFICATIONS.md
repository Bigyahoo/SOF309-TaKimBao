## 1. THÔNG TIN DỰ ÁN
* **Tên đề tài:** Hệ thống Quản lý và Đặt phòng Khách sạn "Vstay" (Vstay.vn)
* **Công nghệ:** Java 17, Jakarta Servlet 6.0, Hibernate ORM 6.4.4.Final, Microsoft SQL Server, JSP & JSTL 3.0, Jackson Databind 2.9.1, Lombok 1.18.30, Apache Tomcat 10.1, Apache Maven 3.9+
* **URL chạy local:** `http://localhost:8080/vstay`

## 2. QUY TẮC NGHIỆP VỤ (BUSINESS RULES)

### 2.1. Chức năng Đăng ký (Register)
* **Form gồm:** `fullName` (Họ và tên), `email` (Email), `phone` (Số điện thoại), `password` (Mật khẩu), `confirmPassword` (Xác nhận mật khẩu).
* **Ràng buộc & Validation:**
  * `fullName`, `email`, `password`: Bắt buộc nhập, không được để trống hoặc chỉ chứa khoảng trắng.
  * `email`: Phải có định dạng email hợp lệ; không được trùng lặp với email đã tồn tại trong cơ sở dữ liệu.
  * `password` & `confirmPassword`: Phải hoàn toàn khớp nhau.
* **Xử lý hệ thống:**
  * Mật khẩu được mã hóa an toàn sử dụng thuật toán **SHA-256 kết hợp Salt ngẫu nhiên** (chuỗi UUID Base64), tuyệt đối không lưu mật khẩu dạng bản rõ (plaintext).
  * Mặc định gán vai trò (`role`) là `USER`, cấp bậc ban đầu `Silver` và tặng 100 điểm thưởng `V-Points` chào mừng.
  * Đăng ký thành công $\rightarrow$ tự động tạo session đăng nhập và chuyển hướng về trang chủ `http://localhost:8080/vstay/`.
  * Đăng ký thất bại $\rightarrow$ giữ lại thông tin form (ngoại trừ mật khẩu) và hiển thị thông báo lỗi tương ứng.

### 2.2. Chức năng Đăng nhập (Login)
* **Form gồm:** `email`, `password`, tham số tùy chọn `redirect` (URL muốn truy cập trước đó).
* **Ràng buộc & Validation:**
  * Cả `email` và `password` không được để trống.
* **Xử lý hệ thống:**
  * Kiểm tra tài khoản tồn tại qua email $\rightarrow$ băm mật khẩu nhập vào với `salt` đã lưu $\rightarrow$ so sánh với `passwordHash`.
  * **Đúng thông tin:** 
    * Khởi tạo `HttpSession`, lưu đối tượng `User` vào session attribute `user`.
    * Cập nhật thời điểm đăng nhập gần nhất `lastLoginAt`.
    * Nếu tài khoản có vai trò `ADMIN` $\rightarrow$ chuyển hướng tới trang quản trị `http://localhost:8080/vstay/admin`.
    * Nếu tài khoản là `USER`:
      * Có tham số `redirect` hợp lệ (chặn Open Redirect) $\rightarrow$ chuyển hướng về trang `redirect`.
      * Không có `redirect` $\rightarrow$ chuyển hướng về trang chủ `http://localhost:8080/vstay/`.
  * **Sai thông tin hoặc tài khoản không tồn tại:** Báo lỗi `"Email hoặc mật khẩu không chính xác."`, giữ lại email đã nhập.
  * Nếu người dùng đã đăng nhập từ trước mà truy cập `/login` $\rightarrow$ tự động chuyển hướng về trang chủ.

### 2.3. Chức năng Đăng xuất (Logout)
* Người dùng gửi yêu cầu tới `/logout`.
* Hệ thống thực hiện hủy toàn bộ phiên làm việc (`session.invalidate()`).
* Chuyển hướng người dùng về trang chủ `http://localhost:8080/vstay/`.

### 2.4. Chức năng CRUD kiểm thử (Quản lý Khách sạn - Admin Portal)
Chức năng dành riêng cho quản trị viên (yêu cầu vai trò `ADMIN`, được bảo vệ bởi `AdminFilter`).

* **Xem danh sách (Read / List):**
  * Truy cập `/admin`: Hiển thị danh sách toàn bộ khách sạn trong hệ thống dưới dạng bảng dữ liệu.
  * Hiển thị đầy đủ: ID, Tên khách sạn, Thành phố, Hạng sao, Giá phòng/đêm, Giá gốc, Giảm giá (%), Đánh giá, Trạng thái Nổi bật (Featured), và các nút thao tác.
  * Hiển thị thống kê tổng quan: Tổng số lượng khách sạn, Tổng số lượt đặt phòng.

* **Thêm mới (Create):**
  * Form gồm: Tên khách sạn (`name`), Danh mục (`category`), Thành phố (`city`), Địa chỉ (`address`), Giá bán (`price`), Giá gốc (`oldPrice`), Số sao (`stars`), Điểm đánh giá (`rating`), Ảnh đại diện (`image`), Mô tả (`description`), Đánh dấu nổi bật (`featured`), Tiện ích (`amenities`).
  * **Validation:**
    * Tên khách sạn (`name`): Bắt buộc, không được để trống.
    * Thành phố (`city`): Bắt buộc.
    * Giá bán (`price`): Bắt buộc, kiểu số nguyên dương > 0.
    * Số sao (`stars`): Trong khoảng từ 1 đến 5 sao.
    * Điểm đánh giá (`rating`): Số thực từ 0.0 đến 10.0.
  * **Tự động tính toán:** Nếu `oldPrice > price`, hệ thống tự động tính phần trăm giảm giá:
    $$\text{discountPercent} = \text{round}\left(\frac{\text{oldPrice} - \text{price}}{\text{oldPrice}} \times 100\right)$$
  * Thêm thành công $\rightarrow$ lưu vào cơ sở dữ liệu, chuyển hướng về bảng danh sách quản trị.

* **Cập nhật / Sửa (Update):**
  * Gửi yêu cầu với `action=update` kèm `id` của khách sạn cần sửa.
  * Kiểm tra khách sạn tồn tại trong DB $\rightarrow$ cập nhật các trường thông tin hợp lệ.
  * Lưu thay đổi vào DB và chuyển hướng về trang danh sách `/admin`.

* **Xóa (Delete):**
  * Gửi yêu cầu với `action=delete` kèm `id` của khách sạn.
  * Hệ thống xóa bản ghi khách sạn khỏi cơ sở dữ liệu (xử lý ràng buộc khóa ngoại với phòng, đánh giá, yêu thích).
  * Cập nhật lại danh sách khách sạn sau khi xóa.

### 2.5. Chức năng Tìm kiếm & Lọc Khách sạn (Search & Filter)
* **Các tiêu chí tìm kiếm/lọc:**
  * `location` / `q`: Từ khóa tìm kiếm theo tên khách sạn, địa chỉ hoặc thành phố (hỗ trợ tiếng Việt có dấu).
  * `city`: Lọc chính xác theo thành phố/tỉnh thành.
  * `minPrice` & `maxPrice`: Khoảng giá mỗi đêm ($\ge 0$).
  * `stars`: Lọc khách sạn có số sao $\ge$ giá trị chọn (1 - 5 sao).
  * `category`: Lọc theo loại hình (`resort`, `hotel`, `boutique`, `apartment`,...).
  * `sort`: Sắp xếp theo `price_asc` (giá tăng dần), `price_desc` (giá giảm dần), `rating` (đánh giá cao nhất), `discount` (giảm giá nhiều nhất), hoặc `recommended` (mặc định).
  * `page`: Phân trang dữ liệu, mặc định 9 khách sạn/trang.
* **Xử lý:**
  * Xây dựng câu truy vấn động HQL (Hibernate) an toàn chống SQL Injection.
  * Trả về danh sách kết quả, tổng số lượng kết quả, số trang và danh sách thành phố để gợi ý.
  * Nếu không có kết quả phù hợp $\rightarrow$ hiển thị thông báo "Không tìm thấy khách sạn phù hợp".

### 2.6. Chức năng Chi tiết Khách sạn (Hotel Details)
* Truy cập đường dẫn `/details?id={hotelId}`.
* **Validation:** Tham số `id` bắt buộc phải là số nguyên dương hợp lệ và tồn tại trong hệ thống.
  * Nếu thiếu `id` hoặc sai định dạng $\rightarrow$ chuyển hướng về trang tìm kiếm `/search`.
  * Nếu `id` không tồn tại trong DB $\rightarrow$ trả về mã lỗi HTTP 404 (Trang thông báo không tìm thấy khách sạn).
* **Nội dung hiển thị:**
  * Toàn bộ thông tin khách sạn: Tên, địa chỉ, ảnh thư viện (gallery), tiện nghi (amenities), chính sách giá.
  * Danh sách loại phòng sẵn có (`Room`): Loại phòng, giá/đêm, sức chứa.
  * Danh sách đánh giá & bình luận (`Review`) từ người dùng.
  * Trạng thái đã yêu thích hay chưa (`isFavorite`) nếu người dùng đã đăng nhập.

### 2.7. Chức năng Đặt phòng & Thanh toán (Booking & Checkout)
* **Bảo vệ truy cập:** Yêu cầu người dùng phải đăng nhập (`AuthFilter`). Nếu chưa đăng nhập $\rightarrow$ chuyển hướng sang `/login?redirect=...`.
* **Form đặt phòng gồm:**
  * Thông tin khách hàng: `guestName`, `guestEmail`, `guestPhone`.
  * Thông tin phòng: `hotelId`, `roomType`, `checkIn`, `checkOut`, `nights`.
  * Thanh toán & Yêu cầu: `paymentMethod` (MoMo, VNPay, ZaloPay, Thanh toán tại quầy), `specialRequests`, `specialNotes`.
* **Validation & Tính toán:**
  * `checkIn` $\ge$ ngày hiện tại; `checkOut` > `checkIn`.
  * `nights` $\ge 1$.
  * `totalPrice` = `nights` $\times$ Đơn giá phòng.
  * Tích lũy điểm V-Points: 
    $$\text{vPointsEarned} = \lfloor\text{totalPrice} / 10,000\rfloor \quad (1\text{ điểm cho mỗi }10,000\text{ VNĐ})$$
* **Mã đặt phòng:** Tự động sinh mã duy nhất định dạng `VS<timestamp>` (Ví dụ: `VS1731912345678`).
* Đặt phòng thành công $\rightarrow$ cộng điểm thưởng vào tài khoản người dùng, chuyển hướng sang `/checkout?success=true&code={bookingCode}` hiển thị hóa đơn xác nhận.

### 2.8. Chức năng Khách sạn Yêu thích (Favorites - AJAX API)
* Gửi yêu cầu qua HTTP `POST /favorite` kèm `hotelId`.
* **Phân quyền:**
  * Người dùng chưa đăng nhập $\rightarrow$ trả về HTTP 401 Unauthorized kèm JSON: `{"status":"unauthorized", "message":"Vui lòng đăng nhập..."}`.
* **Cơ chế Toggle:**
  * Nếu khách sạn chưa có trong danh sách yêu thích của User $\rightarrow$ Thêm mới, trả về JSON `{"status":"added"}`.
  * Nếu khách sạn đã có trong danh sách $\rightarrow$ Xóa khỏi danh sách, trả về JSON `{"status":"removed"}`.

### 2.9. Chức năng Quản lý Tài khoản (User Account)
* Đường dẫn `/account`, yêu cầu đăng nhập.
* **Các khu vực chức năng:**
  * **Hồ sơ cá nhân:** Họ tên, email, số điện thoại, số dư điểm thưởng `V-Points`, Huy hiệu phân hạng thành viên (Member: 0-999 pts, Silver: 1000-4999 pts, Gold: 5000-9999 pts, Platinum: $\ge$ 10000 pts).
  * **Lịch sử đặt phòng:** Danh sách các đơn đặt phòng của chính người dùng đó (mã đơn, tên khách sạn, ngày ở, tổng tiền, trạng thái).
  * **Danh sách yêu thích:** Danh sách các khách sạn đã lưu để xem lại hoặc hủy lưu nhanh.

### 2.10. Chức năng Liên hệ (Contact / About)
* Đường dẫn `/about`.
* Form gồm: `name` (bắt buộc), `email` (bắt buộc), `phone`, `topic`, `message`.
* Gửi thành công $\rightarrow$ lưu vào bảng `contacts`, thông báo cảm ơn người dùng.
