package com.asr.example.file.deduplicator.view;

import com.asr.example.file.deduplicator.dto.request.DirectoryRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "index.html"})
    public String home(Model model) {
        // Provide empty model to enable user to provide information in the form
        model.addAttribute("directoryRequest", new DirectoryRequest());
        model.addAttribute("failureMessage", "");
        model.addAttribute("successMessage", "");
        return "index";
    }
}
