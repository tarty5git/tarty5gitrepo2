# FUNCTIONAL SPECIFICATION DOCUMENT

## Application Title
**Software Development Document Environment**

## 1. Executive Summary
The Software Development Document Environment (SDM) is an enterprise web platform designed to streamline and automate document lifecycle management across the 7 phases of the Software Development Life Cycle (SDLC). The system provides multi-format intake (Word, Excel, PowerPoint, XML, PDF, TXT), configurable folder watcher handlers, Maker-Checker review and approval workflow, configurable LDAP/local security, toggleable SMS/Email notifications, downloadable approver reports (PDF & Excel), and a local RAG AI recommendation engine powered by Ollama and FAISS.

## 2. SDLC Deliverable Code Mapping
- **Phase 1: Project Initiation & Feasibility**
  - P101: Project Charter (`PC-01`)
  - P102: Feasibility Study (`FS-01`)
- **Phase 2: Requirements Analysis**
  - P201: System Requirement Specification (`SRS-01`)
  - P202: Use Case Document (`UC-01`)
- **Phase 3: Architecture & System Design**
  - P301: High Level Design (`HLD-01`)
  - P302: Database Design (`DB-01`)
- **Phase 4: Interface Design & Development**
  - P401: API Specification (`API-01`)
  - P402: Unit Test Plan (`UTP-01`)
- **Phase 5: Integration & Acceptance Testing**
  - P501: System Integration Test (`SIT-01`)
  - P502: User Acceptance Test (`UAT-01`)
- **Phase 6: Deployment & Operational Readiness**
  - P601: Deployment Instruction Guide (`DIG-01`)
  - P602: Operations and Maintenance Manual (`OMM-01`)
- **Phase 7: Post Implementation & Maintenance**
  - P701: Post Implementation Review (`PIR-01`)

## 3. Technology Stack & Prerequisites
- **Backend:** Java 17 / 21, Spring Boot 3.2.3, Spring Data JPA, Spring Security
- **Database:** Oracle 19c / 21c (Default), PostgreSQL, SQL Server, H2 in-memory
- **Frontend UI:** Thymeleaf, Bootstrap 5.3, Bootstrap Icons
- **Documentation & Report Exporting:** Apache POI 5.2.5 (Excel/Word/PPT), OpenPDF 1.3.30 (PDF)
- **AI RAG Microservice:** Python 3.10+, Flask, FAISS, Ollama (`llama3.2:latest`, `nomic-embed-text`)

## 4. Execution Scripts
- **Windows:**
  - Build: `build.bat`
  - Start: `start.bat`
  - Run Tests: `test.bat`
- **Linux:**
  - Build: `./build.sh`
  - Start: `./start.sh`
  - Run Tests: `./test.sh`

## 5. JUnit Test Cases
- `UserServiceTest.java`: Validates user account creation, locking/unlocking, and password reset functionality.
- `DocumentServiceTest.java`: Validates document upload, sequence ID generation (P101, P201, P202, etc.), 3-char application code tagging, and Maker-Checker approval workflow.
- `ReportServiceTest.java`: Validates PDF and Excel report generation.
