# SYSTEM WALKTHROUGH & USER INSTRUCTIONS MANUAL

## Application Name
**Software Development Document Environment**

## 1. Quick Start Guide
1. **Build Application:**
   - Windows: `build.bat`
   - Linux: `./build.sh`
2. **Launch Application:**
   - Windows: `start.bat`
   - Linux: `./start.sh`
3. **Access Web Portal:**
   - Navigate to `http://localhost:8080` in any web browser.
4. **Default System Credentials:**
   - **Administrator:** `admin` / `admin123`
   - **Maker User:** `maker` / `admin123`
   - **Checker User:** `checker` / `admin123`

## 2. Main Workbench Features
- **SDLC 7-Phase Deliverables View:**
  - Displays distinct frames for Phases 1 through 7.
  - Allows makers to upload Word, Excel, PowerPoint, XML, PDF, or text files tagged with auto-generated document IDs (P101, P201, P202) and 3-character application codes (`SDM`).
- **Automated Directory Intake:**
  - Drop documents into `./intake_folder`.
  - Multi-threaded worker handlers automatically pick up files and log timestamped audit trails.
- **Maker / Checker Workflow & Pre-Approval Viewing:**
  - Checkers access `/checker/dashboard` to view uploaded files before issuing an Approval or Rejection decision.
- **Approver Reports:**
  - Access `/reports/view` to view live document status reports online or download full summary reports in PDF and Excel formats.
- **Admin Maintenance & Configuration:**
  - Access `/admin/dashboard` to switch LDAP/local authentication, update system parameters, manage users (create, lock, unlock, reset passwords), and upload official deliverable document templates.
- **Swagger OpenAPI Documentation:**
  - View interactive REST API documentation at `http://localhost:8080/swagger-ui.html`.
- **Local RAG AI Recommendations:**
  - Local Python Flask service on port 5000 performs semantic document indexing using Ollama (`llama3.2:latest`, `nomic-embed-text`) and FAISS, providing compliance recommendations for each uploaded deliverable.
