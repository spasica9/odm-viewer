import { useState } from "react";
import UploadForm from "./components/UploadForm";
import TreeView from "./components/TreeView";
import "./App.css";

function App() {
  const [odmData, setOdmData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [tab, setTab] = useState("structure");

  const handleUpload = async ({ file, showStructure, showClinical }) => {
    setLoading(true);
    setError(null);
    setOdmData(null);

    const formData = new FormData();
    formData.append("file", file);
    formData.append("showStructure", showStructure);
    formData.append("showClinical", showClinical);

    try {
      const response = await fetch("http://localhost:8080/api/upload", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }

      const data = await response.json();
      setOdmData(data);
      setTab("structure");
    } catch (err) {
      console.error(err);
      setError(err.message || "Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  const splitData = (data) => {
    if (!data) return { structure: null, clinical: null };

    const structure = {};
    const clinical = {};

    Object.entries(data).forEach(([key, value]) => {
      if (key.toLowerCase().includes("clinical") || key.toLowerCase().includes("itemdata")) {
        clinical[key] = value;
      } else {
        structure[key] = value;
      }
    });

    return { structure, clinical };
  };

  const { structure, clinical } = splitData(odmData || {});

  return (
    <div className="app-container">
      <h1>ODM Viewer</h1>
      <UploadForm onSubmit={handleUpload} />

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {odmData && (
        <div style={{ marginTop: 20 }}>
          <div className="tabs">
            <button onClick={() => setTab("structure")} disabled={tab === "structure"}>
              Structure
            </button>
            <button onClick={() => setTab("clinical")} disabled={tab === "clinical"}>
              Clinical Data
            </button>
          </div>

          <div className="tree-container">
            {tab === "structure" && <TreeView data={structure} type="structure" />}
            {tab === "clinical" && <TreeView data={clinical} type="clinical" />}
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
