package com.example.event.Controller;

import com.example.event.Service.MailApiService;
import com.resend.core.exception.ResendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TetController {

    @Autowired
    private MailApiService mailApiService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/celebrate")
    public String celebrateFallback() {
        return "redirect:/";
    }

    @PostMapping("/celebrate")
    public String celebrate(@RequestParam String name, @RequestParam String gender, Model model) {
        String wish;

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

    @GetMapping("/game")
    public String gamePage() {
        return "game";
    }
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    @PostMapping("/submit-lixi")
    public String submitLixi(
            @RequestParam String fullName,
            @RequestParam String bankNumber,
            @RequestParam String bankName,
            @RequestParam String socialLink,
            @RequestParam String luckyNote,
            @RequestParam String luckyMoney, // <--- THÊM THAM SỐ NÀY
            Model model
    ) {
        try {
            if (mailApiService == null) {
                System.out.println("LỖI: Chưa cấu hình MailApiService!");
                model.addAttribute("success", false);
                return "game";
            }

            // Tiêu đề email thêm số tiền luôn cho dễ nhìn
            String subject = "[LI XI TET 2026] " + fullName + " - " + luckyMoney;

            String html = String.format("""
                    <div style="font-family: Arial, sans-serif; padding: 20px; border: 2px solid #d4af37; border-radius: 10px;">
                        <h2 style="color: #c90000;">🧧 Co nguoi doi li xi ne ban oi!</h2>
                        <hr/>
                        <p><b>Ten nguoi nhan:</b> %s</p>
                        <p><b>Ngan hang:</b> %s</p>
                        <p><b>So tai khoan:</b> <b style="font-size: 18px;">%s</b></p>
                        <p><b>Facebook/Insta:</b> <a href="%s">Xem trang ca nhan</a></p>
                        <hr/>
                        <h3 style="color: #eab308;">💰 SO TIEN TRUNG: <span style="font-size: 24px; color: red;">%s</span></h3>
                        <p><b>Loi chuc/Que boc duoc:</b> %s</p>
                        <hr/>
                        <p><i>Mau check xem co phai ban minh khong roi chuyen khoan nhe :D</i></p>
                    </div>
                    """,
                    escapeHtml(fullName),
                    escapeHtml(bankName),
                    escapeHtml(bankNumber),
                    escapeHtml(socialLink), // Link để trong thẻ a cho dễ click
                    escapeHtml(luckyMoney), // <--- HIỂN THỊ SỐ TIỀN Ở ĐÂY
                    escapeHtml(luckyNote)
            );

            System.out.println("Dang gui mail cho: " + fullName);

            // Gửi mail (bạn nhớ thay email nhận thành email của bạn)
            mailApiService.sendHtml("daod1068@gmail.com", subject, html);

            System.out.println("Gui thanh cong!");
            model.addAttribute("success", true);

        } catch (ResendException e) {
            System.err.println("Gui mail that bai (Resend): " + e.getMessage());
            model.addAttribute("success", false);
        } catch (Exception e) {
            System.err.println("Gui mail that bai: " + e.getMessage());
            model.addAttribute("success", false);
        }

        return "game";
    }
}
