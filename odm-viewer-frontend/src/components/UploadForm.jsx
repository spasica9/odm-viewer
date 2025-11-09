import React, { useState } from "react";
import { Upload, FileText, X } from 'lucide-react';
import "../App.css";

export default function UploadForm({ onSubmit, loading }) {
  const [file, setFile] = useState(null);
  const [showStructure, setShowStructure] = useState(true);
  const [showClinical, setShowClinical] = useState(false);
  const [showError, setShowError] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!file) {
      setShowError(true);
      return;
    }
    setShowError(false);
    onSubmit({ file, showStructure, showClinical });
  };

  return (
    <form className="upload-card" onSubmit={handleSubmit}>
      <h2><Upload size={24} /> Upload ODM File</h2>

      <input
        type="file"
        accept=".xml"
        onChange={(e) => { setFile(e.target.files[0]); setShowError(false); }}
      />
      {file && (
        <div className="selected-file" title={file.name}>
          <FileText size={14} /> {file.name}
        </div>
      )}

      <label>
        <input type="checkbox" checked={showStructure} onChange={() => setShowStructure(!showStructure)} />
        Show Structure (MetaData)
      </label>
      <label>
        <input type="checkbox" checked={showClinical} onChange={() => setShowClinical(!showClinical)} />
        Show Clinical Data
      </label>

      <button type="submit" disabled={!file || loading}>
        {loading ? "Uploading..." : "Upload File"}
      </button>

      {showError && (
        <div className="error-message">
          <X size={16} /> Please select a file to upload.
        </div>
      )}
    </form>
  );
}


