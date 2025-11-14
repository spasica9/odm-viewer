import React, { useState } from "react";
import { Upload, FileText, X } from "lucide-react";
import "../App.css";

export default function UploadForm({ onSubmit, loading }) {
  const [file, setFile] = useState(null);
  const [showError, setShowError] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!file) {
      setShowError(true);
      return;
    }
    setShowError(false);
    onSubmit({ file, showStructure: true, showClinical: true }); // uvek oba
  };

  return (
    <form className="upload-inline" onSubmit={handleSubmit}>
      <label htmlFor="file-upload" className="upload-label">
        <Upload size={18} /> Choose XML
      </label>
      <input
        id="file-upload"
        type="file"
        accept=".xml"
        onChange={(e) => setFile(e.target.files[0])}
        style={{ display: "none" }}
      />
      {file && (
        <div className="selected-file" title={file.name}>
          <FileText size={14} /> {file.name}
        </div>
      )}
      <button type="submit" disabled={!file || loading}>
        {loading ? "Uploading..." : "Upload"}
      </button>
      {showError && (
        <div className="error-message-inline">
          <X size={14} /> Please select a file.
        </div>
      )}
    </form>
  );
}



