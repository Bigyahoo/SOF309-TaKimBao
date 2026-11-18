package com.vstay.servlet;

import com.vstay.dao.HotelDAO;
import com.vstay.dao.ReviewDAO;
import com.vstay.dao.UserDAO;
import com.vstay.model.Hotel;
import com.vstay.model.Review;
import com.vstay.model.Room;
import com.vstay.model.User;
import com.vstay.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DataSeederServlet", urlPatterns = {"/seed"})
public class DataSeederServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            int seededCount = 0;

            // 1. Seed Admin User
            User admin = userDAO.findByEmail("admin@vstay.vn");
            if (admin == null) {
                String salt = PasswordUtil.generateSalt();
                admin = User.builder()
                        .fullName("Vstay Quản Trị Viên")
                        .email("admin@vstay.vn")
                        .phone("0901234567")
                        .passwordHash(PasswordUtil.hashPassword("admin123", salt))
                        .salt(salt)
                        .vipTier("Diamond")
                        .vPoints(5000)
                        .role("ADMIN")
                        .build();
                userDAO.save(admin);
            }

            // Seed Demo User
            User demo = userDAO.findByEmail("demo@vstay.vn");
            if (demo == null) {
                String salt = PasswordUtil.generateSalt();
                demo = User.builder()
                        .fullName("Nguyễn Văn An")
                        .email("demo@vstay.vn")
                        .phone("0987654321")
                        .passwordHash(PasswordUtil.hashPassword("demo123", salt))
                        .salt(salt)
                        .vipTier("Gold")
                        .vPoints(750)
                        .role("USER")
                        .build();
                userDAO.save(demo);
            }

            // 2. Seed Hotels if table is empty
            List<Hotel> existingHotels = hotelDAO.findAll();
            if (existingHotels.isEmpty() || "true".equalsIgnoreCase(request.getParameter("force"))) {
                List<Hotel> sampleHotels = createSampleHotels();
                for (Hotel h : sampleHotels) {
                    hotelDAO.save(h);
                    seededCount++;
                }

                // Add sample reviews for the first hotel
                if (!sampleHotels.isEmpty()) {
                    Hotel firstHotel = sampleHotels.get(0);
                    Review r1 = Review.builder()
                            .hotel(firstHotel)
                            .authorName("Trần Hoàng Nam")
                            .rating(9.5)
                            .comment("Dịch vụ tuyệt vời, phòng ốc view biển cực đẹp và nhân viên siêu nhiệt tình!")
                            .travelType("Kỳ nghỉ gia đình")
                            .build();
                    Review r2 = Review.builder()
                            .hotel(firstHotel)
                            .authorName("Lê Thị Mai")
                            .rating(9.0)
                            .comment("Hồ bơi vô cực rất chill, đồ ăn sáng buffet đa dạng món Á - Âu. Sẽ quay lại!")
                            .travelType("Cặp đôi")
                            .build();
                    reviewDAO.save(r1);
                    reviewDAO.save(r2);
                }
            } else {
                seededCount = existingHotels.size();
            }

            String html = """
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <title>Khởi tạo dữ liệu Vstay thành công</title>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f7fb; color: #1e293b; display: flex; align-items: center; justify-content: center; min-height: 100vh; margin: 0; }
                        .card { background: white; padding: 40px; border-radius: 16px; box-shadow: 0 10px 25px rgba(0,0,0,0.08); max-width: 500px; width: 90%; text-align: center; }
                        h1 { color: #0062E0; margin-bottom: 8px; font-size: 26px; }
                        p { color: #64748b; font-size: 15px; margin: 12px 0 24px; }
                        .stats { background: #f0f7ff; border: 1px solid #cce3ff; border-radius: 10px; padding: 15px; margin-bottom: 24px; text-align: left; }
                        .stats div { margin: 6px 0; font-size: 14px; }
                        .stats strong { color: #0062E0; }
                        .btn { display: inline-block; background: #0062E0; color: white; padding: 12px 28px; border-radius: 8px; text-decoration: none; font-weight: 600; transition: 0.2s; }
                        .btn:hover { background: #004bb5; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h1>Khởi tạo dữ liệu thành công!</h1>
                        <p>Hệ thống khách sạn Vstay đã được nạp dữ liệu mẫu.</p>
                        <div class="stats">
                            <div>✓ <strong>Hotels:</strong> %d khách sạn trên toàn quốc</div>
                            <div>✓ <strong>Tài khoản Quản trị:</strong> admin@vstay.vn / admin123</div>
                            <div>✓ <strong>Tài khoản Demo:</strong> demo@vstay.vn / demo123</div>
                        </div>
                        <a href="%s/" class="btn">Về Trang Chủ Vstay</a>
                    </div>
                </body>
                </html>
                """.formatted(seededCount, request.getContextPath());

            response.getWriter().write(html);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Lỗi nạp dữ liệu mẫu: " + e.getMessage());
        }
    }

    private List<Hotel> createSampleHotels() {
        List<Hotel> list = new ArrayList<>();

        // Đà Nẵng
        Hotel h1 = Hotel.builder()
                .name("Furama Resort Danang")
                .category("Resort")
                .city("Đà Nẵng")
                .address("105 Võ Nguyên Giáp, Ngũ Hành Sơn, Đà Nẵng")
                .price(3250000L)
                .oldPrice(4500000L)
                .discountPercent(28)
                .stars(5)
                .rating(9.4)
                .ratingText("Xuất sắc")
                .reviewsCount(824)
                .image("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600")
                .badge("ƯU ĐÃI ĐẶC BIỆT")
                .badgeSub("Gần biển Mỹ Khê")
                .roomType("Ocean Suite Hướng Biển")
                .urgency("Chỉ còn 3 phòng giá này!")
                .distance("Cách bãi biển 50m")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(18)
                .featured(true)
                .description("Furama Resort Danang là khu nghỉ dưỡng 5 sao sang trọng toạ lạc trên bờ biển Mỹ Khê tuyệt đẹp. Với kiến trúc pha trộn giữa phong cách Pháp thuộc địa và văn hoá Chăm truyền thống, Furama mang đến không gian nghỉ dưỡng đỉnh cao.")
                .inclusions("Bữa sáng buffet thượng hạng|Miễn phí đưa đón sân bay|Vé sử dụng hồ bơi vô cực")
                .amenities("Hồ bơi vô cực|Spa & Chăm sóc sức khỏe|Phòng Gym hiện đại|Nhà hàng hải sản quốc tế|WiFi tốc độ cao|Bãi đỗ xe riêng")
                .tags("Gần biển|Hồ bơi vô cực|Buffet sáng|View biển")
                .build();
        addRoomsToHotel(h1);
        list.add(h1);

        Hotel h2 = Hotel.builder()
                .name("InterContinental Danang Sun Peninsula Resort")
                .category("Resort")
                .city("Đà Nẵng")
                .address("Bãi Bắc, Bán đảo Sơn Trà, Đà Nẵng")
                .price(8900000L)
                .oldPrice(11500000L)
                .discountPercent(23)
                .stars(5)
                .rating(9.8)
                .ratingText("Tuyệt hảo")
                .reviewsCount(1240)
                .image("https://images.unsplash.com/photo-1582719508461-905c673771fd?w=600")
                .badge("SIÊU SANG TRỌNG")
                .badgeSub("Bán đảo Sơn Trà")
                .roomType("Classic Ocean View King")
                .urgency("Đặt nhiều trong ngày")
                .distance("Bán đảo Sơn Trà riêng tư")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(24)
                .featured(true)
                .description("Kiệt tác kiến trúc của Bill Bensley nép mình bên sườn đồi thoai thoải hướng ra vịnh biển xanh ngắt của bán đảo Sơn Trà.")
                .inclusions("Ăn sáng nhà hàng Citron|Miễn phí dịch vụ cáp treo Nam Tram|Quà chào mừng cao cấp")
                .amenities("Bãi biển riêng tư|Nhà hàng La Maison 1888|Cáp treo nội khu|Spa Harnn Heritage")
                .tags("Độc quyền|Bán đảo Sơn Trà|Michelin Dining|Biển riêng")
                .build();
        addRoomsToHotel(h2);
        list.add(h2);

        Hotel h3 = Hotel.builder()
                .name("Vinpearl Resort & Spa Đà Nẵng")
                .category("Resort")
                .city("Đà Nẵng")
                .address("23 Trường Sa, Hoà Hải, Ngũ Hành Sơn, Đà Nẵng")
                .price(2890000L)
                .oldPrice(3800000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.1)
                .ratingText("Tuyệt vời")
                .reviewsCount(532)
                .image("https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=600")
                .badge("ƯU ĐÃI GIA ĐÌNH")
                .badgeSub("Bãi biển Non Nước")
                .roomType("Deluxe Villa 2 Phòng Ngủ")
                .urgency("Ưu đãi có hạn")
                .distance("Cách Ngũ Hành Sơn 1.2km")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(16)
                .featured(true)
                .description("Ốc đảo thanh bình với 122 căn biệt thự phong cách tân cổ điển, được ôm ấp bởi thảm cỏ xanh mướt và hàng dừa thơ mộng.")
                .inclusions("Buffet sáng phong phú|Miễn phí vé khu vui chơi trẻ em|Trà chiều đón khách")
                .amenities("Hồ bơi chính 540m2|Akoya Spa|Sân tennis|Nhà hàng Gourmet")
                .tags("Biệt thự|Gia đình|Bãi biển Non Nước|Hồ bơi")
                .build();
        addRoomsToHotel(h3);
        list.add(h3);

        Hotel h4 = Hotel.builder()
                .name("Novotel Danang Premier Han River")
                .category("Khách sạn")
                .city("Đà Nẵng")
                .address("36 Bạch Đằng, Hải Châu, Đà Nẵng")
                .price(1850000L)
                .oldPrice(2400000L)
                .discountPercent(23)
                .stars(4)
                .rating(8.9)
                .ratingText("Rất tốt")
                .reviewsCount(680)
                .image("https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=600")
                .badge("TRUNG TÂM THÀNH PHỐ")
                .badgeSub("Bên bờ sông Hàn")
                .roomType("Superior King River View")
                .urgency("Đặt phòng ngay")
                .distance("Ngay cầu quay Sông Hàn")
                .taxNote("Đã bao gồm bữa sáng")
                .galleryCount(14)
                .featured(true)
                .description("Vị trí đắc địa ngay trung tâm thành phố Đà Nẵng với tầm nhìn bao trọn sông Hàn và cầu Rồng lung linh về đêm.")
                .inclusions("Ăn sáng món quốc tế|Sky36 Bar tầng thượng|Miễn phí phòng xông hơi")
                .amenities("Sky Bar ngoài trời tầng 36|Hồ bơi tầng cao|Phòng gym InBalance|Trung tâm hội nghị")
                .tags("View sông Hàn|Sky Bar|Trung tâm|Ngắm Cầu Rồng")
                .build();
        addRoomsToHotel(h4);
        list.add(h4);

        // Hồ Chí Minh
        Hotel h5 = Hotel.builder()
                .name("The Reverie Saigon")
                .category("Khách sạn")
                .city("Hồ Chí Minh")
                .address("22-36 Nguyễn Huệ, Bến Nghé, Quận 1, TP. Hồ Chí Minh")
                .price(5500000L)
                .oldPrice(7200000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.7)
                .ratingText("Xuất sắc")
                .reviewsCount(940)
                .image("https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=600")
                .badge("ĐẲNG CẤP HOÀNG GIA")
                .badgeSub("Phố đi bộ Nguyễn Huệ")
                .roomType("Deluxe Panorama King")
                .urgency("Chỉ còn 2 phòng trống")
                .distance("Mặt tiền phố đi bộ Nguyễn Huệ")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(25)
                .featured(true)
                .description("Khách sạn 6 sao bậc nhất Sài Gòn mang hơi thở kiến trúc quý tộc Ý cổ điển nguy nga tráng lệ.")
                .inclusions("Bữa sáng buffet hoàng gia|Đưa đón xe Rolls-Royce/Bentley (tùy gói)|Sử dụng hồ bơi ozon")
                .amenities("Hồ bơi ngoài trời lọc bằng ozon|Spa 1.200m2|Nhà hàng The Royal Pavilion|Trực thăng riêng")
                .tags("Quận 1|Hoàng gia|Phố đi bộ|Hồ bơi ngoài trời")
                .build();
        addRoomsToHotel(h5);
        list.add(h5);

        Hotel h6 = Hotel.builder()
                .name("Caravelle Saigon Hotel")
                .category("Khách sạn")
                .city("Hồ Chí Minh")
                .address("19-23 Lam Sơn, Bến Nghé, Quận 1, TP. Hồ Chí Minh")
                .price(2900000L)
                .oldPrice(3800000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.0)
                .ratingText("Tuyệt vời")
                .reviewsCount(780)
                .image("https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600")
                .badge("LỊCH SỬ & DI SẢN")
                .badgeSub("Đối diện Nhà hát Thành phố")
                .roomType("Opera Wing Deluxe Room")
                .urgency("Ưu đãi đặt sớm")
                .distance("Cách Nhà hát TP 50m")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(15)
                .featured(true)
                .description("Biểu tượng lịch sử giữa lòng Sài Gòn từ năm 1959, phong cách thanh lịch vượt thời gian kết hợp tiện nghi đương đại.")
                .inclusions("Bữa sáng buffet tại Nineteen|Đồ uống chào mừng tại Saigon Saigon Bar")
                .amenities("Saigon Saigon Rooftop Bar|Kara Spa|Hồ bơi tầng thượng|Phòng họp đa năng")
                .tags("Lịch sử|Quận 1|Nhà hát lớn|Rooftop Bar")
                .build();
        addRoomsToHotel(h6);
        list.add(h6);

        // Hà Nội
        Hotel h7 = Hotel.builder()
                .name("Sofitel Legend Metropole Hanoi")
                .category("Khách sạn")
                .city("Hà Nội")
                .address("15 Ngô Quyền, Hoàn Kiếm, Hà Nội")
                .price(6800000L)
                .oldPrice(8500000L)
                .discountPercent(20)
                .stars(5)
                .rating(9.8)
                .ratingText("Tuyệt hảo")
                .reviewsCount(1520)
                .image("https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=600")
                .badge("HUYỀN THOẠI HÀ NỘI")
                .badgeSub("Cách Hồ Hoàn Kiếm 300m")
                .roomType("Historical Metropole Wing Room")
                .urgency("Đặt nhiều nhất tháng")
                .distance("Ngay trung tâm phố cổ")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(30)
                .featured(true)
                .description("Biểu tượng di sản hàng đầu Đông Dương từ năm 1901, từng đón tiếp các nguyên thủ quốc gia và minh tinh điện ảnh thế giới.")
                .inclusions("Bữa sáng Pháp - Việt|Tour tham quan hầm trú bom lịch sử|Trà chiều Le Club")
                .amenities("Nhà hàng Pháp Le Beaulieu|Hồ bơi sân vườn nhiệt đới|Le Spa du Metropole|Hầm rượu vang quý")
                .tags("Di sản|Phố Cổ|Hoàn Kiếm|Phong cách Pháp")
                .build();
        addRoomsToHotel(h7);
        list.add(h7);

        Hotel h8 = Hotel.builder()
                .name("Lotte Hotel Hanoi")
                .category("Khách sạn")
                .city("Hà Nội")
                .address("54 Liễu Giai, Ba Đình, Hà Nội")
                .price(3100000L)
                .oldPrice(4200000L)
                .discountPercent(26)
                .stars(5)
                .rating(9.2)
                .ratingText("Xuất sắc")
                .reviewsCount(640)
                .image("https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600")
                .badge("TẦM NHÌN TOÀN CẢNH")
                .badgeSub("Đỉnh cao tháp Lotte")
                .roomType("Deluxe King City View")
                .urgency("Giá tốt hôm nay")
                .distance("Cách Hồ Tây 1.5km")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(18)
                .featured(true)
                .description("Tọa lạc trên các tầng cao nhất của toà tháp 65 tầng Lotte Center Hanoi, chiêm ngưỡng toàn cảnh thủ đô ngàn năm văn hiến.")
                .inclusions("Bữa sáng tại Grill63|Miễn phí vé đài quan sát Sky Walk|Sử dụng bể bơi 4 mùa")
                .amenities("Đài quan sát Sky Walk|Hồ bơi trong nhà và ngoài trời|Evian Spa|Nhà hàng Grill63")
                .tags("View cao tầng|Ba Đình|Hồ bơi bốn mùa|Sang trọng")
                .build();
        addRoomsToHotel(h8);
        list.add(h8);

        // Phú Quốc
        Hotel h9 = Hotel.builder()
                .name("JW Marriott Phu Quoc Emerald Bay Resort & Spa")
                .category("Resort")
                .city("Phú Quốc")
                .address("Bãi Khem, An Thới, TP. Phú Quốc, Kiên Giang")
                .price(7500000L)
                .oldPrice(9800000L)
                .discountPercent(23)
                .stars(5)
                .rating(9.6)
                .ratingText("Xuất sắc")
                .reviewsCount(890)
                .image("https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600")
                .badge("TUYỆT TÁC THIẾT KẾ")
                .badgeSub("Bãi Khem cát trắng mịn")
                .roomType("Emerald Bay Front King")
                .urgency("Còn rất ít phòng trống")
                .distance("Ngay bãi biển Bãi Khem")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(28)
                .featured(true)
                .description("Thiết kế theo mô hình trường đại học giả tưởng Lamarck University cuối thế kỷ 19. Mỗi phân khoa là một trải nghiệm nghệ thuật độc nhất vô nhị.")
                .inclusions("Ăn sáng buffet đỉnh cao tại Tempus Fugit|Chèo thuyền kayak miễn phí|Lớp học làm gốm & yoga")
                .amenities("Hồ bơi vỏ sò Seashell Pool|Chanterelle Spa by JW|Pink Pearl Fine Dining|Bãi biển riêng tư")
                .tags("Bãi Khem|Resort thiết kế|Hồ bơi vỏ sò|Check-in sống ảo")
                .build();
        addRoomsToHotel(h9);
        list.add(h9);

        Hotel h10 = Hotel.builder()
                .name("Premier Village Phu Quoc Resort")
                .category("Villa")
                .city("Phú Quốc")
                .address("Mũi Ông Đội, An Thới, TP. Phú Quốc, Kiên Giang")
                .price(6200000L)
                .oldPrice(8000000L)
                .discountPercent(22)
                .stars(5)
                .rating(9.3)
                .ratingText("Xuất sắc")
                .reviewsCount(450)
                .image("https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=600")
                .badge("HAI MẶT BIỂN HIẾM CÓ")
                .badgeSub("Mũi Ông Đội ngắm bình minh & hoàng hôn")
                .roomType("Ocean View Villa có bể bơi riêng")
                .urgency("Bán chạy mùa này")
                .distance("Mũi Ông Đội")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(20)
                .featured(false)
                .description("Duy nhất tại Việt Nam sở hữu 2 mặt biển, nơi bạn có thể ngắm cả bình minh rực rỡ và hoàng hôn lãng mạn tại cùng một vị trí.")
                .inclusions("Buffet sáng nhà hàng The Market|Biệt thự có hồ bơi riêng|Xe buggy đưa đón 24/7")
                .amenities("Hồ bơi vô cực nhiều tầng kết nối 2 bờ biển|Plumeria Spa trên đồi|Nhà hàng hải sản Corallo")
                .tags("Biệt thự biển|Hồ bơi riêng|Mũi Ông Đội|Hai mặt biển")
                .build();
        addRoomsToHotel(h10);
        list.add(h10);

        // Nha Trang
        Hotel h11 = Hotel.builder()
                .name("Amiana Resort Nha Trang")
                .category("Resort")
                .city("Nha Trang")
                .address("Vịnh Rùa, Phạm Văn Đồng, Nha Trang, Khánh Hoà")
                .price(3600000L)
                .oldPrice(4800000L)
                .discountPercent(25)
                .stars(5)
                .rating(9.5)
                .ratingText("Xuất sắc")
                .reviewsCount(730)
                .image("https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=600")
                .badge("VỊNH BIỂN RIÊNG BIỆT")
                .badgeSub("Hồ bơi nước biển 2.500m2")
                .roomType("Deluxe Villa Hướng Vườn")
                .urgency("Khách đặt liên tục")
                .distance("Vịnh Rùa tĩnh lặng")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(22)
                .featured(true)
                .description("Amiana tọa lạc trên vịnh Rùa thơ mộng, nổi bật với hồ bơi nước biển tự nhiên khổng lồ 2.500m2 cùng bãi cát trắng mịn riêng tư.")
                .inclusions("Ăn sáng buffet phong phú|Tắm bùn khoáng nóng riêng tư|Trái cây tươi chào mừng")
                .amenities("Hồ bơi nước mặn 2.500m2|2 hồ bơi nước ngọt vô cực|Tắm bùn khoáng và spa|Lặn ngắm san hô")
                .tags("Nha Trang|Hồ bơi nước mặn|Tắm bùn|Bãi biển riêng")
                .build();
        addRoomsToHotel(h11);
        list.add(h11);

        Hotel h12 = Hotel.builder()
                .name("Vinpearl Resort Nha Trang")
                .category("Resort")
                .city("Nha Trang")
                .address("Đảo Hòn Tre, Vĩnh Nguyên, Nha Trang, Khánh Hoà")
                .price(2700000L)
                .oldPrice(3600000L)
                .discountPercent(25)
                .stars(5)
                .rating(9.1)
                .ratingText("Tuyệt vời")
                .reviewsCount(910)
                .image("https://images.unsplash.com/photo-1544984243-ec57ea16fe25?w=600")
                .badge("ĐẢO THIÊN ĐƯỜNG")
                .badgeSub("Đảo Hòn Tre trọn gói")
                .roomType("Deluxe King Hướng Biển")
                .urgency("Ưu đãi combo")
                .distance("Đảo Hòn Tre")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(17)
                .featured(false)
                .description("Khu nghỉ dưỡng đậm nét kiến trúc Đông Dương thuần khiết trên đảo Hòn Tre, gắn liền công viên giải trí VinWonders kỳ thú.")
                .inclusions("Bữa sáng buffet|Tàu cao tốc đưa đón đảo 24/7|Sử dụng hồ bơi lớn nhất Đông Nam Á")
                .amenities("Hồ bơi ngoài trời 5.000m2|Sân Golf 18 lỗ tiêu chuẩn quốc tế|Vincharm Spa|Bãi biển riêng")
                .tags("Đảo Hòn Tre|VinWonders|Golf|Hồ bơi khổng lồ")
                .build();
        addRoomsToHotel(h12);
        list.add(h12);

        // Đà Lạt
        Hotel h13 = Hotel.builder()
                .name("Dalat Edensee Lake Resort & Spa")
                .category("Resort")
                .city("Đà Lạt")
                .address("Khu chức năng VII.2, Hồ Tuyền Lâm, Đà Lạt, Lâm Đồng")
                .price(2100000L)
                .oldPrice(2800000L)
                .discountPercent(25)
                .stars(5)
                .rating(9.2)
                .ratingText("Xuất sắc")
                .reviewsCount(512)
                .image("https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=600")
                .badge("BÊN BỜ HỒ TUYỀN LÂM")
                .badgeSub("Rừng thông ngút ngàn")
                .roomType("Mimosa Superior Hướng Đồi Thông")
                .urgency("Nhiều người quan tâm")
                .distance("Bên bờ hồ Tuyền Lâm")
                .taxNote("Đã bao gồm bữa sáng")
                .galleryCount(18)
                .featured(true)
                .description("Ngôi làng châu Âu cổ tích nép mình giữa rừng thông bạt ngàn soi bóng xuống mặt hồ Tuyền Lâm bảng lảng sương mờ.")
                .inclusions("Ăn sáng buffet phong cách Âu|Miễn phí bắn cung & chèo kayak hồ|Trà chiều")
                .amenities("Hồ bơi nước ấm trong nhà|Sân tennis & bắn cung|Chèo thuyền kayak|Rừng thông đi dạo")
                .tags("Đà Lạt|Hồ Tuyền Lâm|Rừng thông|Không khí se lạnh")
                .build();
        addRoomsToHotel(h13);
        list.add(h13);

        Hotel h14 = Hotel.builder()
                .name("Ana Mandara Villas Dalat Resort & Spa")
                .category("Villa")
                .city("Đà Lạt")
                .address("Đường Lê Lai, Phường 5, Đà Lạt, Lâm Đồng")
                .price(2950000L)
                .oldPrice(3900000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.3)
                .ratingText("Xuất sắc")
                .reviewsCount(420)
                .image("https://images.unsplash.com/photo-1587061949409-02df41d5e562?w=600")
                .badge("BIỆT THỰ PHÁP CỔ")
                .badgeSub("Đồi thông xanh ngát")
                .roomType("Villa Room Lò Sưởi Cổ Điển")
                .urgency("Còn 1 phòng hôm nay")
                .distance("Cách chợ Đà Lạt 2km")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(21)
                .featured(false)
                .description("Quần thể 17 căn biệt thự Pháp cổ được phục dựng nguyên vẹn từ những năm 1920-1930 với lò sưởi củi ấm cúng.")
                .inclusions("Bữa sáng gọi món hoặc buffet|Lò sưởi củi ban đêm|Trà atiso tươi đón khách")
                .amenities("Hồ bơi nước nóng ngoài trời|La Cochinchine Spa|Nhà hàng Le Petit Dalat|Vườn rau hữu cơ")
                .tags("Biệt thự cổ|Lò sưởi|Hồ bơi nước ấm|Pháp cổ")
                .build();
        addRoomsToHotel(h14);
        list.add(h14);

        // Hội An
        Hotel h15 = Hotel.builder()
                .name("Anantara Hoi An Resort")
                .category("Resort")
                .city("Hội An")
                .address("1 Phạm Hồng Thái, Cẩm Châu, Hội An, Quảng Nam")
                .price(3800000L)
                .oldPrice(4900000L)
                .discountPercent(22)
                .stars(5)
                .rating(9.4)
                .ratingText("Xuất sắc")
                .reviewsCount(610)
                .image("https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=600")
                .badge("BÊN DÒNG THU BỒN")
                .badgeSub("Đi bộ vào phố cổ 5 phút")
                .roomType("Deluxe Garden View Room")
                .urgency("Đặt ngay giá tốt")
                .distance("Cách Chùa Cầu 800m")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(19)
                .featured(true)
                .description("Không gian đậm chất thơ nằm dọc bờ sông Thu Bồn êm đềm, chỉ cách trung tâm phố cổ Hội An vài bước chân tản bộ.")
                .inclusions("Ăn sáng buffet cao cấp bên sông|Tour thuyền trên sông Thu Bồn ngắm hoàng hôn|Xe đạp miễn phí")
                .amenities("Hồ bơi ngoài trời rợp bóng dừa|Anantara Spa thảo mộc|Lớp học nấu ăn Spice Spoons|Bến thuyền riêng")
                .tags("Hội An|Sông Thu Bồn|Đi bộ phố cổ|Thuyền ngắm hoàng hôn")
                .build();
        addRoomsToHotel(h15);
        list.add(h15);

        Hotel h16 = Hotel.builder()
                .name("La Siesta Hoi An Resort & Spa")
                .category("Resort")
                .city("Hội An")
                .address("132 Hùng Vương, Thanh Hà, Hội An, Quảng Nam")
                .price(2200000L)
                .oldPrice(2900000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.6)
                .ratingText("Tuyệt hảo")
                .reviewsCount(890)
                .image("https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600")
                .badge("ĐƯỢC YÊU THÍCH NHẤT")
                .badgeSub("Hồ bơi nước mặn hướng cánh đồng lúa")
                .roomType("Club Wing Grand Suite")
                .urgency("Khách review 5 sao liên tục")
                .distance("Gần làng gốm Thanh Hà")
                .taxNote("Đã bao gồm bữa sáng")
                .galleryCount(20)
                .featured(false)
                .description("Khách sạn nghỉ dưỡng thanh lịch bao quanh bởi cánh đồng lúa xanh mướt của Hội An và hồ bơi vô cực nước mặn độc đáo.")
                .inclusions("Bữa sáng buffet phong phú|Xe bus đưa đón phố cổ và biển An Bàng|Miễn phí mượn xe đạp")
                .amenities("4 hồ bơi ngoài trời (nước ngọt & mặn)|La Siesta Spa|Nhà hàng The Temple|Phòng gym")
                .tags("Hồ bơi nước mặn|Đồng lúa|Gần phố cổ|Dịch vụ 5 sao")
                .build();
        addRoomsToHotel(h16);
        list.add(h16);

        // Huế
        Hotel h17 = Hotel.builder()
                .name("Azerai La Residence Hue")
                .category("Khách sạn")
                .city("Huế")
                .address("5 Lê Lợi, Vĩnh Ninh, TP. Huế, Thừa Thiên Huế")
                .price(4100000L)
                .oldPrice(5200000L)
                .discountPercent(21)
                .stars(5)
                .rating(9.5)
                .ratingText("Xuất sắc")
                .reviewsCount(380)
                .image("https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600")
                .badge("NGẮM SÔNG HƯƠNG")
                .badgeSub("Kiến trúc Art Deco kinh điển")
                .roomType("Superior King River View")
                .urgency("Chỉ còn 3 phòng")
                .distance("Bên bờ sông Hương")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(17)
                .featured(false)
                .description("Tòa dinh thự kiến trúc Art Deco kiêu hãnh trải dài bên dòng sông Hương thơ mộng, hướng thẳng tầm mắt sang Kỳ Đài Đại Nội Huế.")
                .inclusions("Bữa sáng phong cách cung đình & Âu|Du thuyền ngắm hoàng hôn trên sông Hương|Trà chiều")
                .amenities("Hồ bơi nước mặn ngoài trời sát bờ sông|Le Spa|Sân tennis đất nện|Nhà hàng Le Parfum")
                .tags("Sông Hương|Kiến trúc Art Deco|Đại Nội Huế|Sang trọng")
                .build();
        addRoomsToHotel(h17);
        list.add(h17);

        Hotel h18 = Hotel.builder()
                .name("Silk Path Grand Hue Hotel")
                .category("Khách sạn")
                .city("Huế")
                .address("2 Lê Lợi, Vĩnh Ninh, TP. Huế, Thừa Thiên Huế")
                .price(1950000L)
                .oldPrice(2600000L)
                .discountPercent(25)
                .stars(5)
                .rating(9.2)
                .ratingText("Xuất sắc")
                .reviewsCount(410)
                .image("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600")
                .badge("QUÝ TỘC CỐ ĐÔ")
                .badgeSub("Phong cách hoàng gia pha Pháp")
                .roomType("Classic King City View")
                .urgency("Giá ưu đãi hôm nay")
                .distance("Cách ga Huế 300m")
                .taxNote("Đã bao gồm bữa sáng")
                .galleryCount(15)
                .featured(false)
                .description("Tái hiện vẻ đẹp lộng lẫy của cung đình triều Nguyễn kết hợp nét lãng mạn Pháp thế kỷ 19 giữa lòng cố đô trầm mặc.")
                .inclusions("Ăn sáng buffet phong vị Huế|Sử dụng bể bơi bốn mùa|Trà cung đình đón tiếp")
                .amenities("Hồ bơi ngoài trời|Chi Spa|Nhà hàng Nam Phương ẩm thực cung đình|Phòng tập thể hình")
                .tags("Hoàng gia|Huế|Cố đô|Sang trọng")
                .build();
        addRoomsToHotel(h18);
        list.add(h18);

        // Sa Pa
        Hotel h19 = Hotel.builder()
                .name("Hotel de la Coupole - MGallery Sapa")
                .category("Khách sạn")
                .city("Sa Pa")
                .address("1 Hoàng Liên, Sa Pa, Lào Cai")
                .price(3500000L)
                .oldPrice(4600000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.5)
                .ratingText("Xuất sắc")
                .reviewsCount(1100)
                .image("https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=600")
                .badge("VIÊN NGỌC TÂY BẮC")
                .badgeSub("Nhà ga tàu hoả leo núi Fansipan")
                .roomType("Classic King City View Room")
                .urgency("Đặt nhiều dịp cuối tuần")
                .distance("Ngay quảng trường Sa Pa")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(26)
                .featured(true)
                .description("Tác phẩm nghệ thuật của Bill Bensley kết hợp haute couture Pháp và sắc màu văn hóa dân tộc thiểu số Sa Pa rực rỡ.")
                .inclusions("Bữa sáng thịnh soạn tại Chic Restaurant|Trực tiếp kết nối ga tàu hoả leo núi Fansipan|Hồ bơi nước nóng Le Grand Bassin")
                .amenities("Hồ bơi nước ấm nghệ thuật trong nhà|Nuages Spa|Quầy bar Absinthe tầng thượng|Nhà hàng Chic")
                .tags("Sa Pa|Fansipan|Check-in sống ảo|Hồ bơi nước nóng")
                .build();
        addRoomsToHotel(h19);
        list.add(h19);

        // Vũng Tàu
        Hotel h20 = Hotel.builder()
                .name("The Imperial Hotel Vũng Tàu")
                .category("Khách sạn")
                .city("Vũng Tàu")
                .address("159 Thùy Vân, Thắng Tam, Vũng Tàu, Bà Rịa - Vũng Tàu")
                .price(2350000L)
                .oldPrice(3100000L)
                .discountPercent(24)
                .stars(5)
                .rating(9.0)
                .ratingText("Tuyệt vời")
                .reviewsCount(830)
                .image("https://images.unsplash.com/photo-1582719508461-905c673771fd?w=600")
                .badge("PHONG CÁCH HOÀNG GIA ANH")
                .badgeSub("Bãi Sau Vũng Tàu")
                .roomType("Deluxe King Sea View")
                .urgency("Giá tốt cho kỳ nghỉ cuối tuần")
                .distance("Mặt tiền Bãi Sau")
                .taxNote("Đã bao gồm thuế & phí")
                .galleryCount(18)
                .featured(false)
                .description("Khách sạn 5 sao mang phong cách cổ điển Victoria hoàng gia Anh duy nhất tại thành phố biển Vũng Tàu sôi động.")
                .inclusions("Bữa sáng buffet tại Dining Room|Lối đi riêng ra bãi biển Beach Club|Hồ bơi ngoài trời")
                .amenities("Imperial Beach Club bờ biển|3 hồ bơi lớn|Nhà hàng hải sản La Sirena|Sân tennis")
                .tags("Vũng Tàu|Bãi Sau|Hoàng gia Anh|Beach Club")
                .build();
        addRoomsToHotel(h20);
        list.add(h20);

        // Add 20 more hotels to reach 40
        String[] moreCities = {"Đà Nẵng", "Hà Nội", "Hồ Chí Minh", "Nha Trang", "Phú Quốc", "Đà Lạt", "Hội An", "Huế", "Sa Pa", "Vũng Tàu"};
        String[] moreCategories = {"Khách sạn", "Resort", "Homestay", "Villa"};
        for (int i = 21; i <= 40; i++) {
            String city = moreCities[(i - 21) % moreCities.length];
            String category = moreCategories[(i - 21) % moreCategories.length];
            long price = 850000L + (i * 95000L);
            long oldPrice = (long) (price * 1.25);
            int stars = 3 + (i % 3);
            double rating = 8.2 + ((i % 15) * 0.1);
            rating = Math.round(rating * 10.0) / 10.0;

            Hotel extra = Hotel.builder()
                    .name("Vstay " + category + " " + city + " " + (i - 20))
                    .category(category)
                    .city(city)
                    .address((10 + i) + " Đường Du Lịch, Trung Tâm " + city)
                    .price(price)
                    .oldPrice(oldPrice)
                    .discountPercent(20)
                    .stars(stars)
                    .rating(rating)
                    .ratingText(rating >= 9.0 ? "Xuất sắc" : "Rất tốt")
                    .reviewsCount(120 + i * 15)
                    .image("https://images.unsplash.com/photo-" + (1566073771259L + i * 1000L) + "?w=600")
                    .badge("GIÁ TỐT NHẤT")
                    .badgeSub("Trung tâm " + city)
                    .roomType("Phòng Deluxe Tiêu Chuẩn")
                    .urgency("Đặt phòng tiết kiệm")
                    .distance("Cách trung tâm 500m")
                    .taxNote("Đã bao gồm thuế & phí")
                    .galleryCount(12)
                    .featured(false)
                    .description("Nằm ở vị trí thuận tiện tại " + city + ", mang đến không gian nghỉ ngơi thoải mái, tiện nghi và ấm cúng cho du khách với mức giá hợp lý.")
                    .inclusions("Bữa sáng ngon miệng|WiFi miễn phí|Nước suối và trà cafe mỗi ngày")
                    .amenities("Lễ tân 24/7|Thang máy|WiFi miễn phí|Điều hòa nhiệt độ|Dịch vụ dọn phòng hàng ngày")
                    .tags("Tiết kiệm|Trung tâm|Sạch sẽ|Tiện nghi")
                    .build();
            addRoomsToHotel(extra);
            list.add(extra);
        }

        return list;
    }

    private void addRoomsToHotel(Hotel hotel) {
        Room r1 = Room.builder()
                .hotel(hotel)
                .name("Phòng Deluxe Giường Đôi")
                .type("Deluxe Double")
                .price(hotel.getPrice())
                .oldPrice(hotel.getOldPrice())
                .capacity(2)
                .bedType("1 Giường King cỡ lớn")
                .area(38.0)
                .image(hotel.getImage())
                .amenities("Ban công view đẹp|Bồn tắm nằm|WiFi tốc độ cao|Điều hoà 2 chiều")
                .available(true)
                .build();

        Room r2 = Room.builder()
                .hotel(hotel)
                .name("Phòng Suite Hướng Biển / Thành Phố")
                .type("Executive Suite")
                .price((long) (hotel.getPrice() * 1.45))
                .oldPrice((long) ((hotel.getOldPrice() != null ? hotel.getOldPrice() : hotel.getPrice()) * 1.45))
                .capacity(3)
                .bedType("1 Giường King + 1 Giường Đơn")
                .area(55.0)
                .image(hotel.getImage())
                .amenities("Phòng khách riêng|Ban công Panorama|Bồn tắm sục Jacuzzi|Minibar miễn phí")
                .available(true)
                .build();

        hotel.getRooms().add(r1);
        hotel.getRooms().add(r2);
    }
}
