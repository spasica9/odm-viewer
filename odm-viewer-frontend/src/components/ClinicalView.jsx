import React from "react";

export default function ClinicalView({ data }) {
  if (!data?.ClinicalData?.length) return <div>No clinical data</div>;

  const mdv = data.Study?.[0]?.MetaDataVersions?.[0];
  const codeLists = mdv?.codeLists || [];
  const itemDefs = mdv?.itemDeves || [];
  const itemDefMap = Object.fromEntries(itemDefs.map((i) => [i.oid, i]));

  const studyEventNameMap = Object.fromEntries(
    (mdv?.studyEventDeves || []).map(se => [se.oid, se.name])
  );

  const getAnswerText = (itemOID, value) => {
    const itemDef = itemDefMap[itemOID];
    if (!itemDef) return value;
    const codeListOID = itemDef.codeListRef?.codeListOID;
    if (!codeListOID) return value;
    const codeList = codeLists.find((cl) => cl.oid === codeListOID);
    const codeItem = codeList?.codeListItems.find(
      (ci) => ci.codedValue.toString() === value?.toString()
    );
    return codeItem?.decode?.translatedTexts?.[0]?.content?.join(" ").trim() || value;
  };

  const getQuestionText = (itemOID) => {
    const itemDef = itemDefMap[itemOID];
    if (!itemDef) return itemOID;
    return itemDef.question?.translatedTexts?.[0]?.content?.join(" ").trim() || itemDef.name || itemOID;
  };

  const renderItems = (items) =>
    items.flatMap((ig) => {
      if (ig.itemGroupDatasAndItemDatas) {
        return renderItems(ig.itemGroupDatasAndItemDatas);
      }
      const question = getQuestionText(ig.itemOID);
      const answer = ig.values?.map((v) => getAnswerText(ig.itemOID, v.value)).join(", ");
      return [{ question, answer }];
    });

  const subjects = data.ClinicalData[0].subjectDatas || [];

  return (
    <div className="tree-card">
      {subjects.map((subject) => (
        <div key={subject.subjectKey} style={{ marginBottom: "2rem" }}>
          <h3 style={{ marginBottom: "1rem" }}>Subject: {subject.subjectKey}</h3>

          {subject.studyEventDatas.map((se) => (
            <div key={se.studyEventOID} style={{ marginBottom: "1.5rem" }}>
              <h4 style={{ marginBottom: "0.5rem" }}>
                📘 {studyEventNameMap[se.studyEventOID] || se.studyEventOID}
              </h4>

              {se.itemGroupDatas.map((ig, idx) => {
                const items = renderItems([ig]);
                return (
                  <div key={idx} style={{ marginBottom: "1rem", paddingLeft: "1rem" }}>
                    {items.map((i, index) => (
                      <div key={index} style={{ marginBottom: "0.5rem" }}>
                        <strong>{i.question}:</strong> {i.answer}
                      </div>
                    ))}
                  </div>
                );
              })}
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}


