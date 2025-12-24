package com.example.event.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChristmasController {

    @GetMapping("/")
    public String index() {
        return "index"; // Trang form nhập tên
    }

    @PostMapping("/wish")
    public String wish(@RequestParam String name, @RequestParam String message, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("message", message);
        return "main"; // Trang Giáng sinh chính
    }

    @PostMapping("/celebrate")
    public String celebrate(@RequestParam String name, @RequestParam String gender, Model model) {
        String wish = "";

        if ("male".equals(gender)) {
            wish = "Chúc bạn luôn giữ vững bản lĩnh, gặt hái nhiều thành công và có một mùa đông ấm áp, tràn đầy năng lượng bên những người thân yêu. Mong mọi dự định của bạn trong năm mới đều rực rỡ!";
        } else {
            wish = "Chúc bạn luôn xinh đẹp rạng rỡ như những ánh đèn Giáng sinh, tâm hồn luôn ngọt ngào và hạnh phúc. Hy vọng phép màu sẽ mang đến cho bạn thật nhiều niềm vui và những món quà tuyệt vời nhất!";
        }

        model.addAttribute("userName", name);
        model.addAttribute("userWish", wish);
        model.addAttribute("sender", "Đào Anh Đức aka Nopro Peach");
        return "magic";
    }
    @GetMapping("/music")
    public String musicPage(Model model) {
        model.addAttribute("sender", "Đào Anh Đức aka Nopro Peach");
        return "music";
    }
}