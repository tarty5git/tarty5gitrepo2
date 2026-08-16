package com.cth.sdm.controller;

import com.cth.sdm.entity.DocumentTemplate;
import com.cth.sdm.entity.SystemConfig;
import com.cth.sdm.repository.DocumentTemplateRepository;
import com.cth.sdm.repository.SystemConfigRepository;
import com.cth.sdm.service.AuditService;
import com.cth.sdm.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final SystemConfigRepository configRepository;
    private final UserService userService;
    private final DocumentTemplateRepository templateRepository;
    private final AuditService auditService;

    public AdminController(SystemConfigRepository configRepository,
                           UserService userService,
                           DocumentTemplateRepository templateRepository,
                           AuditService auditService) {
        this.configRepository = configRepository;
        this.userService = userService;
        this.templateRepository = templateRepository;
        this.auditService = auditService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<SystemConfig> configs = configRepository.findAll();
        model.addAttribute("configs", configs);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("templates", templateRepository.findAll());
        model.addAttribute("auditLogs", auditService.getAllLogs());
        return "admin";
    }

    @PostMapping("/config/update")
    public String updateConfig(@RequestParam("configKey") String key,
                               @RequestParam("configValue") String value,
                               Authentication auth) {
        SystemConfig config = configRepository.findById(key).orElse(new SystemConfig(key, value, ""));
        config.setConfigValue(value);
        config.setUpdatedAt(LocalDateTime.now());
        configRepository.save(config);

        String adminUser = auth != null ? auth.getName() : "admin";
        auditService.logEvent("CONFIG_UPDATE", adminUser, null, "Updated config " + key + " = " + value);
        return "redirect:/admin/dashboard?success=config";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("email") String email,
                             @RequestParam("mobileNumber") String mobileNumber,
                             @RequestParam("role") String role,
                             Authentication auth) {
        String adminUser = auth != null ? auth.getName() : "admin";
        userService.createUser(username, password, email, mobileNumber, role, adminUser);
        return "redirect:/admin/dashboard?success=user_created";
    }

    @PostMapping("/users/toggle-lock")
    public String toggleLock(@RequestParam("username") String username,
                             @RequestParam("locked") boolean locked,
                             Authentication auth) {
        String adminUser = auth != null ? auth.getName() : "admin";
        userService.setUserLockStatus(username, locked, adminUser);
        return "redirect:/admin/dashboard?success=user_lock";
    }

    @PostMapping("/users/reset-password")
    public String resetPassword(@RequestParam("username") String username,
                                @RequestParam("newPassword") String newPassword,
                                Authentication auth) {
        String adminUser = auth != null ? auth.getName() : "admin";
        userService.resetPassword(username, newPassword, adminUser);
        return "redirect:/admin/dashboard?success=password_reset";
    }

    @PostMapping("/templates/upload")
    public String uploadTemplate(@RequestParam("phaseNum") int phaseNum,
                                 @RequestParam("deliverableCode") String deliverableCode,
                                 @RequestParam("templateName") String templateName,
                                 @RequestParam("file") MultipartFile file,
                                 Authentication auth) throws IOException {
        if (file.isEmpty()) {
            return "redirect:/admin/dashboard?error=empty_file";
        }

        File templateDir = new File("./uploaded_documents/templates");
        if (!templateDir.exists()) {
            templateDir.mkdirs();
        }

        File dest = new File(templateDir, "template_p" + phaseNum + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        String adminUser = auth != null ? auth.getName() : "admin";

        DocumentTemplate template = new DocumentTemplate();
        template.setDocId("TPL-P" + phaseNum);
        template.setPhaseNum(phaseNum);
        template.setDeliverableCode(deliverableCode);
        template.setTemplateName(templateName);
        template.setFilePath(dest.getAbsolutePath());
        template.setOriginalFilename(file.getOriginalFilename());
        template.setUploadedBy(adminUser);
        template.setUploadedAt(LocalDateTime.now());

        templateRepository.save(template);
        auditService.logEvent("TEMPLATE_UPLOADED", adminUser, file.getOriginalFilename(), "Uploaded template for Phase " + phaseNum + " (" + deliverableCode + ")");

        return "redirect:/admin/dashboard?success=template_uploaded";
    }
}
