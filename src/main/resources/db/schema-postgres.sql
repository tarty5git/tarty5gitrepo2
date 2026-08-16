DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS maker_checker_requests;
DROP TABLE IF EXISTS document_templates;
DROP TABLE IF EXISTS sdlc_phase_documents;
DROP TABLE IF EXISTS system_config;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    mobile_number VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    locked BOOLEAN DEFAULT FALSE NOT NULL,
    password_updated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE system_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value VARCHAR(500) NOT NULL,
    description VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE sdlc_phase_documents (
    id BIGSERIAL PRIMARY KEY,
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
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    approved_by VARCHAR(50),
    approved_at TIMESTAMP,
    rejection_reason VARCHAR(500)
);

CREATE TABLE document_templates (
    id BIGSERIAL PRIMARY KEY,
    doc_id VARCHAR(50) NOT NULL,
    phase_num INT NOT NULL,
    deliverable_code VARCHAR(20) NOT NULL,
    template_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    uploaded_by VARCHAR(50) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE maker_checker_requests (
    id BIGSERIAL PRIMARY KEY,
    request_type VARCHAR(50) NOT NULL,
    target_doc_id VARCHAR(50) NOT NULL,
    maker_username VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    remarks VARCHAR(500),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    checker_username VARCHAR(50),
    action_at TIMESTAMP
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    file_name VARCHAR(255),
    details VARCHAR(1000),
    event_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
