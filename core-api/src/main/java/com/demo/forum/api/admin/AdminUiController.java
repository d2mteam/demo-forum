package com.demo.forum.api.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUiController {
    @GetMapping({"/admin/plugins", "/admin/plugins/"})
    public String pluginsAdmin() {
        return "forward:/admin/plugins/index.html";
    }
}
