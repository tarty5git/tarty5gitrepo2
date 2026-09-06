package com.cth.sdm.config;

import com.cth.sdm.entity.SystemConfig;
import com.cth.sdm.entity.User;
import com.cth.sdm.repository.SystemConfigRepository;
import com.cth.sdm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SystemConfigRepository configRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           SystemConfigRepository configRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.configRepository = configRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initConfigs();
        initUsers();
    }

    private void initConfigs() {
        saveConfigIfAbsent("app.name", "Software Development Document Environment", "Application Title");
        saveConfigIfAbsent("app.code", "SDM", "Default 3-character Application Code");
        saveConfigIfAbsent("auth.ldap.enabled", "false", "LDAP Active Directory authentication toggle");
        saveConfigIfAbsent("notification.email.enabled", "false", "Email Notification toggle for Maker/Checker");
        saveConfigIfAbsent("notification.sms.enabled", "false", "SMS Notification toggle for Maker/Checker");
        saveConfigIfAbsent("intake.folder.path", "./intake_folder", "Automated document pickup directory");
        saveConfigIfAbsent("intake.handlers.count", "2", "Number of worker handlers for folder intake");
    }

    private void saveConfigIfAbsent(String key, String val, String desc) {
        if (!configRepository.existsById(key)) {
            configRepository.save(new SystemConfig(key, val, desc));
        }
    }

    private void initUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "admin@cth.com", "+10000000000", "ADMIN");
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("maker")) {
            User maker = new User("maker", passwordEncoder.encode("admin123"), "maker@cth.com", "+10000000001", "MAKER");
            userRepository.save(maker);
        }

        if (!userRepository.existsByUsername("checker")) {
            User checker = new User("checker", passwordEncoder.encode("admin123"), "checker@cth.com", "+10000000002", "CHECKER");
            userRepository.save(checker);
        }
    }
}
