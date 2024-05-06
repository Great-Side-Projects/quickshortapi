package org.dev.quickshortapi.infraestructure.adapter.in.web;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.application.service.UrlService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UrlControllerUI {

    private final IUrlServicePort urlService;

    UrlControllerUI(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public String shorten(@ModelAttribute UrlCommand urlCommand) {
        urlService.shortenUrl(urlCommand);
        return "redirect:/urls";
    }

    @GetMapping("/delete/{shorturl}")
    public String delete(@PathVariable String shorturl) {
        urlService.deleteUrlbyShortUrl(shorturl);
        return "redirect:/urls";
    }

    @GetMapping({"/urls", "/"})
       public String getAllUrls(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<UrlResponse> urls = urlService.getAllUrls(page, 10);
        model.addAttribute("urls", urls.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", urls.getTotalPages());
        model.addAttribute("totalItems", urls.getTotalElements());
        model.addAttribute("urlCommand", new UrlCommand());
        return "urls";
    }
}
