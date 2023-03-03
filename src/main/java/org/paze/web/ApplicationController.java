package org.paze.web;

import org.paze.service.api.PazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class ApplicationController {

    private final PazeService pazeService;

    @Autowired
    public ApplicationController(final PazeService pazeService) {
        this.pazeService = pazeService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("number", 100);
        return "index";
    }
    @PostMapping("/send")
    public String submitNumberInputForm(@RequestParam("number") BigDecimal number) {
        String redirectUrl = pazeService.getRedirectUrl(number);
        return "redirect:" + redirectUrl;
    }
}
