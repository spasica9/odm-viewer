export async function uploadOdm({ file, showStructure, showClinical }) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("structure", showStructure);
  formData.append("clinical", showClinical);

  const res = await fetch("/api/upload", {
    method: "POST",
    body: formData
  });

  return res.json();
}