import "../App.css";

export default function ClinicalView({ data, itemDefs, codeLists }) {
  if (!data) return null;

  const clinicalData = data.ClinicalData || [];

  const getQuestionText = (itemOID) => {
    const item = itemDefs?.find((i) => i.oid === itemOID);
    if (!item) return itemOID;

    if (item.question?.translatedTexts?.length) {
      return item.question.translatedTexts[0].content.join(" ").trim();
    }
    if (item.description?.translatedTexts?.length) {
      return item.description.translatedTexts[0].content.join(" ").trim();
    }
    return item.name || itemOID;
  };

  const getDisplayValue = (itemOID, value) => {
    const item = itemDefs?.find((i) => i.oid === itemOID);
    if (!item) return value;

    if (item.dataType === "BOOLEAN") return value === true || value === "true" ? "Yes" : "No";

    const codeListOID = item.codeListRef?.codeListOID;
    if (codeListOID) {
      const codeList = codeLists?.find((cl) => cl.oid === codeListOID);
      if (codeList) {
        const codeItem = codeList.codeListItems.find(
          (ci) => ci.codedValue.toString() === value.toString()
        );
        if (codeItem)
          return codeItem.decode.translatedTexts[0].content.join(" ");
      }
    }

    return value;
  };

  const renderItem = (obj, level = 0) => {
    if (!obj) return null;

    const hasValues =
      (obj.values && obj.values.length > 0) ||
      (obj.itemGroupDatasAndItemDatas &&
        obj.itemGroupDatasAndItemDatas.some((c) => renderItem(c, level + 1)));

    if (!hasValues) return null;

    return (
      <div className={`level-${level}`} style={{ marginBottom: 4 }}>
        {obj.values &&
          obj.values.map((v, idx) => (
            <div key={idx} className="tree-node clinical">
              <span className="node-name">{getQuestionText(obj.itemOID)}:</span>{" "}
              <span className="node-value">{getDisplayValue(obj.itemOID, v.value)}</span>
            </div>
          ))}

        {obj.itemGroupDatasAndItemDatas &&
          obj.itemGroupDatasAndItemDatas.map((child, idx) => (
            <div key={idx} className="tree-children">
              {renderItem(child, level + 1)}
            </div>
          ))}
      </div>
    );
  };

  return (
    <div className="tree-card">
      <h2 className="tree-header">Clinical Data</h2>
      {clinicalData.length === 0 && <p>No clinical data.</p>}

      {clinicalData.map((study) =>
        (study.subjectDatas || []).map((subject) => (
          <div key={subject.subjectKey} className="tree-node structure" style={{ marginBottom: 10 }}>
            <strong>Subject:</strong> {subject.subjectKey}
            {(subject.studyEventDatas || []).map((se) => (
              <div key={se.studyEventOID} className="tree-children">
                {se.studyEventOID && <div className="tree-node"><span className="icon">📘</span>{se.studyEventOID}</div>}
                {(se.itemGroupDatas || []).map((ig, igIndex) => (
                  <div key={igIndex} className="tree-children" style={{ marginBottom: 8 }}>
                    {renderItem(ig, 1)}
                  </div>
                ))}
              </div>
            ))}
          </div>
        ))
      )}
    </div>
  );
}

