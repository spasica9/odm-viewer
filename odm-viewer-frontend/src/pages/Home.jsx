import { useState } from "react";
import UploadForm from "../components/UploadForm";
import TreeView from "../components/TreeView";
import { uploadOdm } from "../api/odmApi";

export default function Home() {
  const [result, setResult] = useState(null);

  const handleUpload = async (data) => {
    const response = await uploadOdm(data);
    setResult(response);
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>ODM Viewer</h1>

      <UploadForm onSubmit={handleUpload} />

      {result && (
        <div style={{ marginTop: 40 }}>
          <TreeView data={result} name="ODM" />
        </div>
      )}
    </div>
  );
}
