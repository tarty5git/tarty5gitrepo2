package com.cth.sdm.service;

import com.cth.sdm.entity.SystemConfig;
import com.cth.sdm.repository.SystemConfigRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class IntakeFolderWatcherService {

    private static final Logger log = LoggerFactory.getLogger(IntakeFolderWatcherService.class);

    private final SystemConfigRepository systemConfigRepository;
    private final AuditService auditService;

    private ExecutorService threadPool;
    private volatile boolean running = true;

    public IntakeFolderWatcherService(SystemConfigRepository systemConfigRepository,
                                     AuditService auditService) {
        this.systemConfigRepository = systemConfigRepository;
        this.auditService = auditService;
    }

    @PostConstruct
    public void startWatcher() {
        int handlersCount = getIntakeHandlersCount();
        threadPool = Executors.newFixedThreadPool(handlersCount);

        Thread watcherThread = new Thread(this::pollFolder);
        watcherThread.setDaemon(true);
        watcherThread.setName("IntakeFolderWatcher");
        watcherThread.start();
        log.info("Started IntakeFolderWatcher service with {} worker handlers.", handlersCount);
    }

    private int getIntakeHandlersCount() {
        return systemConfigRepository.findById("intake.handlers.count")
                .map(cfg -> Integer.parseInt(cfg.getConfigValue()))
                .orElse(2);
    }

    private String getIntakeFolderPath() {
        return systemConfigRepository.findById("intake.folder.path")
                .map(SystemConfig::getConfigValue)
                .orElse("./intake_folder");
    }

    private void pollFolder() {
        while (running) {
            try {
                String folderPathStr = getIntakeFolderPath();
                File folder = new File(folderPathStr);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                File[] files = folder.listFiles(file -> file.isFile() && !file.getName().startsWith("."));
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        threadPool.submit(() -> processPickedFile(file));
                    }
                }

                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error in IntakeFolderWatcher polling cycle: {}", e.getMessage(), e);
            }
        }
    }

    private void processPickedFile(File file) {
        try {
            log.info("Application Handler picking up document: {}", file.getName());
            auditService.logEvent("FILE_AUTO_PICKUP", "SYSTEM_HANDLER", file.getName(), "Picked up document from intake directory: " + file.getAbsolutePath());

            File destinationDir = new File("./uploaded_documents/intake_processed");
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            File destFile = new File(destinationDir, System.currentTimeMillis() + "_" + file.getName());
            Files.move(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("Successfully processed and stored document: {}", destFile.getAbsolutePath());
            auditService.logEvent("FILE_AUTO_PROCESSED", "SYSTEM_HANDLER", file.getName(), "Auto-processed document stored at: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to process picked document: {}", file.getName(), e);
        }
    }

    @PreDestroy
    public void stopWatcher() {
        this.running = false;
        if (threadPool != null) {
            threadPool.shutdown();
        }
    }
}
