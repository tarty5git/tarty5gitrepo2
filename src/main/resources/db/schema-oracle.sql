CREATE TABLE users (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    email VARCHAR2(100),
    mobile_number VARCHAR2(20),
    role VARCHAR2(20) NOT NULL,
    active NUMBER(1) DEFAULT 1 NOT NULL,
    locked NUMBER(1) DEFAULT 0 NOT NULL,
    password_updated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE system_config (
    config_key VARCHAR2(100) PRIMARY KEY,
    config_value VARCHAR2(500) NOT NULL,
    description VARCHAR2(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE sdlc_phase_documents (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    doc_id VARCHAR2(50) UNIQUE NOT NULL,
    phase_num NUMBER(3) NOT NULL,
    deliverable_code VARCHAR2(20) NOT NULL,
    doc_title VARCHAR2(255) NOT NULL,
    doc_description VARCHAR2(1000),
    doc_version VARCHAR2(20) NOT NULL,
    app_code VARCHAR2(10) NOT NULL,
    file_path VARCHAR2(500),
    original_filename VARCHAR2(255),
    file_type VARCHAR2(100),
    file_size NUMBER(19),
    status VARCHAR2(30) NOT NULL,
    uploaded_by VARCHAR2(50) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    approved_by VARCHAR2(50),
    approved_at TIMESTAMP,
    rejection_reason VARCHAR2(500)
);

CREATE TABLE document_templates (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    doc_id VARCHAR2(50) NOT NULL,
    phase_num NUMBER(3) NOT NULL,
    deliverable_code VARCHAR2(20) NOT NULL,
    template_name VARCHAR2(255) NOT NULL,
    file_path VARCHAR2(500) NOT NULL,
    original_filename VARCHAR2(255) NOT NULL,
    uploaded_by VARCHAR2(50) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE maker_checker_requests (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    request_type VARCHAR2(50) NOT NULL,
    target_doc_id VARCHAR2(50) NOT NULL,
    maker_username VARCHAR2(50) NOT NULL,
    status VARCHAR2(30) NOT NULL,
    remarks VARCHAR2(500),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    checker_username VARCHAR2(50),
    action_at TIMESTAMP
);

CREATE TABLE audit_logs (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_type VARCHAR2(100) NOT NULL,
    username VARCHAR2(50) NOT NULL,
    file_name VARCHAR2(255),
    details VARCHAR2(1000),
    event_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
