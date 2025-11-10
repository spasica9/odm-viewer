import React, { useState } from "react";
import TreeView from "./TreeView";

export default function StructureView({ data }) {
  if (!data) return null;

  const studies = data.Study || [];

  const getClinicalGroups = (study) => {
    const mdv = study.MetaDataVersions?.[0];
    if (!mdv) return [];

    const itemGroups = mdv.itemGroupDeves || [];
    const items = mdv.itemDeves || [];

    return itemGroups.map((ig) => {
      const groupItems = ig.itemGroupRevesAndItemReves
        ?.map((ref) => items.find((item) => item.oid === ref.itemOID))
        .filter(Boolean);
      return {
        GroupName: ig.name,
        Items: groupItems,
      };
    }).filter(g => g.Items?.length > 0);
  };

  const structureTree = {
    FileOID: data.FileOID,
    ODMVersion: data.ODMVersion,
    CreationDateTime: data.CreationDateTime,
    Studies: studies.map((study) => ({
      StudyName: study.StudyName,
      ProtocolName: study.ProtocolName,
      MetaDataVersions: study.MetaDataVersions || [],
    })),
  };

  return (
    <div className="structure-view">
      <h2>Structure (Metadata)</h2>

      {studies.map((study, sIdx) => {
        const clinicalGroups = getClinicalGroups(study);

        return (
          <div key={sIdx} style={{ marginBottom: "2rem" }}>
            <h3>Study: {study.StudyName}</h3>

            {clinicalGroups.length > 0 && (
              <div style={{ marginBottom: "1rem" }}>
                <h4>Clinical Items</h4>
                {clinicalGroups.map((group, gIdx) => (
                  <ClinicalGroup key={gIdx} group={group} />
                ))}
              </div>
            )}

            <div>
              <h4>Full Metadata Tree</h4>
              <TreeView data={structureTree} type="structure" />
            </div>
          </div>
        );
      })}
    </div>
  );
}

function ClinicalGroup({ group }) {
  const [open, setOpen] = useState(false);

  return (
    <div style={{ marginBottom: "0.5rem" }}>
      <div
        onClick={() => setOpen(!open)}
        style={{
          cursor: "pointer",
          fontWeight: "bold",
          display: "flex",
          alignItems: "center",
        }}
      >
        <span style={{ marginRight: "0.5rem" }}>{open ? "▼" : "▶"}</span>
        {group.GroupName}
      </div>
      {open && (
        <ul style={{ paddingLeft: "1.5rem", marginTop: "0.3rem" }}>
          {group.Items.map((item) => (
            <li key={item.oid}>
              {item.name} ({item.dataType})
              {item.prompt && <> - {item.prompt}</>}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}


