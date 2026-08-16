IF OBJECT_ID('dbo.audit_logs', 'U') IS NOT NULL DROP TABLE dbo.audit_logs;
IF OBJECT_ID('dbo.maker_checker_requests', 'U') IS NOT NULL DROP TABLE dbo.maker_checker_requests;
IF OBJECT_ID('dbo.document_templates', 'U') IS NOT NULL DROP TABLE dbo.document_templates;
IF OBJECT_ID('dbo.sdlc_phase_documents', 'U') IS NOT NULL DROP TABLE dbo.sdlc_phase_documents;
IF OBJECT_ID('dbo.system_config', 'U') IS NOT NULL DROP TABLE dbo.system_config;
IF OBJECT_ID('dbo.users', 'U') IS NOT NULL DROP TABLE dbo.users;

CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    mobile_number VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    active BIT DEFAULT 1 NOT NULL,
    locked BIT DEFAULT 0 NOT NULL,
    password_updated_at DATETIME2,
    created_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE system_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value VARCHAR(500) NOT NULL,
    description VARCHAR(255),
    updated_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE sdlc_phase_documents (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    doc_id VARCHAR(50) UNIQUE NOT NULL,
    phase_num INT NOT NULL,
    deliverable_code VARCHAR(20) NOT NULL,
    doc_title VARCHAR(255) NOT NULL,
    doc_description VARCHAR(1000),
    doc_version VARCHAR(20) NOT NULL,
    app_code VARCHAR(10) NOT NULL,
    file_path VARCHAR(500),
    original_filename VARCHAR(255),
    file_type VARCHAR(100),
    file_size BIGINT,
    status VARCHAR(30) NOT NULL,
    uploaded_by VARCHAR(50) NOT NULL,
    uploaded_at DATETIME2 DEFAULT GETDATE() NOT NULL,
    approved_by VARCHAR(50),
    approved_at DATETIME2,
    rejection_reason VARCHAR(500)
);

CREATE TABLE document_templates (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    doc_id VARCHAR(50) NOT NULL,
    phase_num INT NOT NULL,
    deliverable_code VARCHAR(20) NOT NULL,
    template_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    uploaded_by VARCHAR(50) NOT NULL,
    uploaded_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE maker_checker_requests (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    request_type VARCHAR(50) NOT NULL,
    target_doc_id VARCHAR(50) NOT NULL,
    maker_username VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    remarks VARCHAR(500),
    requested_at DATETIME2 DEFAULT GETDATE() NOT NULL,
    checker_username VARCHAR(50),
    action_at DATETIME2
);

CREATE TABLE audit_logs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    file_name VARCHAR(255),
    details VARCHAR(1000),
    event_timestamp DATETIME2 DEFAULT GETDATE() NOT NULL
);
