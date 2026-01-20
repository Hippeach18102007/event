package com.example.event.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TetController {
    @Autowired
    private JavaMailSender mailSender;
    @GetMapping("/")
    public String index() {
        return "index"; // Trang nhập thông tin
    }
    // --- THÊM ĐOẠN NÀY ĐỂ FIX LỖI 405 ---
    // Nếu ai đó truy cập /celebrate mà không qua form (GET request),
    // hệ thống sẽ tự động đá về trang chủ thay vì báo lỗi.
    @GetMapping("/celebrate")
    public String celebrateFallback() {
        return "redirect:/";
    }
    // ------------------------------------

    @PostMapping("/celebrate")
    public String celebrate(@RequestParam String name, @RequestParam String gender, Model model) {
        String wish = "";

        // Logic chúc Tết năm 2026 (Bính Ngọ - Con Ngựa)
        if ("male".equals(gender)) {
            wish = "Năm mới Bính Ngọ 2026, chúc bạn sức khỏe tựa tuấn mã, sự nghiệp 'Mã đáo thành công'. Mong bạn luôn giữ vững bản lĩnh, phi nước đại về phía trước và gặt hái những thành tựu rực rỡ nhất!";
        } else {
            wish = "Xuân Bính Ngọ 2026, chúc bạn xinh đẹp rạng ngời như hoa mai nở rộ. Mong một năm mới vạn sự như ý, lộc tài đầy nhà, tình duyên phơi phới và luôn được yêu thương trọn vẹn!";
        }

        model.addAttribute("userName", name);
        model.addAttribute("userWish", wish);
        model.addAttribute("year", "2026");
        model.addAttribute("sender", "Đào Anh Đức aka Nopro Peach");

        return "celebrate";
    }

    @GetMapping("/music")
    public String musicPage(Model model) {
        model.addAttribute("sender", "Đào Anh Đức aka Nopro Peach");
        return "music";
    }





        // ... (Các code cũ index, celebrate giữ nguyên) ...


        // --- MỚI: TRANG GAME ---
        @GetMapping("/game")
        public String gamePage() {
            return "game"; // Trả về trang game.html
        }

        // --- MỚI: XỬ LÝ GỬI FORM VỀ EMAIL ---
        @PostMapping("/submit-lixi")
        public String submitLixi(
                @RequestParam String fullName,
                @RequestParam String bankNumber,
                @RequestParam String bankName,
                @RequestParam String socialLink,
                @RequestParam String luckyNote,
                Model model) {

            try {
                if (mailSender == null) {
                    System.out.println("LỖI: Chưa cấu hình mailSender!");
                    model.addAttribute("success", false);
                    return "game";
                }

                SimpleMailMessage message = new SimpleMailMessage();

                // 1. Người nhận (Là chính bạn)
                message.setTo("Daod1068@gmail.com");

                // 2. Tiêu đề
                message.setSubject("[LÌ XÌ TẾT 2026] Yêu cầu từ: " + fullName);

                // 3. NỘI DUNG (ĐÂY LÀ PHẦN BẠN ĐANG THIẾU)
                String content = String.format("""
                🌸 CÓ NGƯỜI ĐÒI LÌ XÌ NÈ BẠN ƠI! 🌸
                
                --------------------------------------
                👤 Tên người nhận: %s
                🏦 Ngân hàng: %s
                💳 Số tài khoản: %s
                🔗 Facebook/Insta: %s
                --------------------------------------
                📜 Quẻ họ bốc được: %s
                
                Mau check xem có phải bạn mình không rồi chuyển khoản nhé! :D
                """, fullName, bankName, bankNumber, socialLink, luckyNote);

                // 4. Gắn nội dung vào mail (QUAN TRỌNG)
                message.setText(content);

                // Log ra kiểm tra
                System.out.println("Đang gửi mail cho: " + fullName);

                // Gửi
                mailSender.send(message);

                System.out.println("Gửi thành công!");
                model.addAttribute("success", true);

            } catch (Exception e) {
                System.err.println("Gửi mail thất bại: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("success", false);
            }

            return "game";
        }
    }
