import React, { useState } from "react";
import UploadForm from "./components/UploadForm";
import StructureView from "./components/StructureView";
import ClinicalView from "./components/ClinicalView";

export default function App() {
  const [odmData, setOdmData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [tab, setTab] = useState("structure");
  const [showStructure, setShowStructure] = useState(true);
  const [showClinical, setShowClinical] = useState(false);

  const handleUpload = async ({ file, showStructure, showClinical }) => {
    setLoading(true);
    setError(null);
    setOdmData(null);

    setShowStructure(showStructure);
    setShowClinical(showClinical);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:8080/api/upload", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error(await response.text());

      const data = await response.json();
      setOdmData(data);

      if (showClinical && !showStructure) setTab("clinical");
      else setTab("structure");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const itemDefs = odmData?.Study?.[0]?.MetaDataVersions?.[0]?.itemDeves || [];
  const codeLists = odmData?.Study?.[0]?.MetaDataVersions?.[0]?.codeLists || [];

  return (
    <div className="container">
      <h1>ODM Viewer</h1>

      <div className="main-layout">
        <div className="sidebar">
          <UploadForm onSubmit={handleUpload} loading={loading} />

          {loading && <div className="status-message loading">Analyzing XML...</div>}
          {error && <div className="status-message error">{error}</div>}

          {odmData && (
            <div className="tabs">
              {showStructure && (
                <button
                  className={`tab-button ${tab === "structure" ? "active" : ""}`}
                  onClick={() => setTab("structure")}
                >
                  Structure
                </button>
              )}
              {showClinical && (
                <button
                  className={`tab-button ${tab === "clinical" ? "active" : ""}`}
                  onClick={() => setTab("clinical")}
                >
                  Clinical
                </button>
              )}
            </div>
          )}
        </div>

        <div className="content">
          {odmData && showStructure && tab === "structure" && (
            <StructureView data={odmData} />
          )}

          {odmData && showClinical && tab === "clinical" && (
            <ClinicalView data={odmData} itemDefs={itemDefs} codeLists={codeLists} />
          )}
        </div>
      </div>
    </div>
  );
}


