package com.asr.example.file.deduplicator.view;

import com.asr.example.file.deduplicator.dto.request.DirectoryRequest;
import com.asr.example.file.deduplicator.service.DirectoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/source")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SourceController {

    DirectoryService directoryService;

    @PostMapping
    String saveSource(
            Model model,
            @Valid @ModelAttribute("directoryRequest") DirectoryRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            String collect = errors.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            model.addAttribute("failureMessage", "Errors: " + collect);
        } else if (request == null || !StringUtils.hasText(request.getSourcePath())) {
            model.addAttribute("failureMessage", "Provided path is empty");
        } else {
            directoryService
                    .savePath(request)
                    .ifPresentOrElse(
                            err -> model.addAttribute("failureMessage", err),
                            () -> model.addAttribute("successMessage", String.format("Successfully saved %s directory", request.getSourcePath()))
                    );
        }
        return "index";
    }
}
