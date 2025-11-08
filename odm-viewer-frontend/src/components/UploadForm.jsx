import { useState } from "react";

export default function UploadForm({ onSubmit }) {
  const [file, setFile] = useState(null);
  const [showStructure, setShowStructure] = useState(true);
  const [showClinical, setShowClinical] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!file) return;
    onSubmit({ file, showStructure, showClinical });
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: "flex", gap: 16, flexDirection: "column", maxWidth: 300 }}>
      <input type="file" accept=".xml" onChange={(e) => setFile(e.target.files[0])} />

      <label>
        <input type="checkbox"
               checked={showStructure}
               onChange={() => setShowStructure(!showStructure)} />
        Show Structure
      </label>

      <label>
        <input type="checkbox"
               checked={showClinical}
               onChange={() => setShowClinical(!showClinical)} />
        Show Clinical Data
      </label>

      <button type="submit">Upload</button>
    </form>
  );
}
