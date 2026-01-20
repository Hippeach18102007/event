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

    @PostMapping("/submit-lixi")
    public String submitLixi(
            @RequestParam String fullName,
            @RequestParam String bankNumber,
            @RequestParam String bankName,
            @RequestParam String socialLink,
            @RequestParam String luckyNote,
            Model model
    ) {
        try {
            if (mailApiService == null) {
                System.out.println("LỖI: Chưa cấu hình MailApiService!");
                model.addAttribute("success", false);
                return "game";
            }

            String subject = "[LI XI TET 2026] Yeu cau tu: " + fullName;

            String html = String.format("""
                    <h2>Co nguoi doi li xi ne ban oi!</h2>
                    <hr/>
                    <p><b>Ten nguoi nhan:</b> %s</p>
                    <p><b>Ngan hang:</b> %s</p>
                    <p><b>So tai khoan:</b> %s</p>
                    <p><b>Facebook/Insta:</b> %s</p>
                    <hr/>
                    <p><b>Que ho boc duoc:</b> %s</p>
                    <p>Mau check xem co phai ban minh khong roi chuyen khoan nhe :D</p>
                    """,
                    escapeHtml(fullName),
                    escapeHtml(bankName),
                    escapeHtml(bankNumber),
                    escapeHtml(socialLink),
                    escapeHtml(luckyNote)
            );

            System.out.println("Dang gui mail cho: " + fullName);

            // Gửi về chính bạn
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

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
