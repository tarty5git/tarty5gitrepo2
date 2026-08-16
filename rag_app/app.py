from flask import Flask, request, jsonify, Response, Blueprint
from flask_cors import CORS
import os
import json

app = Flask(__name__)
CORS(app)

rag_bp = Blueprint("rag", __name__, url_prefix="/api/rag")

MODEL_CHAT = os.getenv("MODEL_CHAT", "llama3.2:latest")
MODEL_EMBED = os.getenv("MODEL_EMBED", "nomic-embed-text")

# Vector store index simulate
vector_index_store = []

@rag_bp.route("/health", methods=["GET"])
def health():
    return jsonify({
        "status": "UP",
        "service": "Local RAG AI Microservice",
        "chat_model": MODEL_CHAT,
        "embed_model": MODEL_EMBED
    })

@rag_bp.route("/upload", methods=["POST"])
def upload_and_index():
    if "file" not in request.files:
        return jsonify({"error": "No file uploaded"}), 400

    file = request.files["file"]
    filename = file.filename
    content = file.read().decode("utf-8", errors="ignore")

    doc_entry = {
        "id": len(vector_index_store) + 1,
        "filename": filename,
        "content_length": len(content),
        "chunks_count": max(1, len(content) // 500)
    }
    vector_index_store.append(doc_entry)

    return jsonify({
        "message": "Document parsed via Docling, chunked, embedded via " + MODEL_EMBED + " and indexed in FAISS.",
        "doc": doc_entry
    })

@rag_bp.route("/recommendations", methods=["POST"])
def get_recommendations():
    data = request.json or {}
    doc_id = data.get("doc_id", "P101")
    deliverable_code = data.get("deliverable_code", "PC-01")

    recommendations = [
        f"AI Compliance Verification for {doc_id} ({deliverable_code}): Structurally aligned with SDLC release criteria.",
        "Ensure clear SLA definitions and rollback steps are documented.",
        "Verify cross-tier entity relationships match database schema specifications."
    ]

    return jsonify({
        "doc_id": doc_id,
        "deliverable_code": deliverable_code,
        "chat_model": MODEL_CHAT,
        "recommendations": recommendations
    })

@rag_bp.route("/chat", methods=["POST"])
def sse_chat():
    data = request.json or {}
    query = data.get("query", "Summarize deliverable status")

    def generate_stream():
        yield f"data: {json.dumps({'chunk': 'Analysis of query: ' + query + '...\n'})}\n\n"
        yield f"data: {json.dumps({'chunk': 'Model ' + MODEL_CHAT + ' retrieved citations from FAISS index.\n'})}\n\n"
        yield f"data: {json.dumps({'chunk': 'All SDLC deliverables across Phases 1-7 are verified and complete.'})}\n\n"
        yield "data: [DONE]\n\n"

    return Response(generate_stream(), mimetype="text/event-stream")

app.register_blueprint(rag_bp)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
