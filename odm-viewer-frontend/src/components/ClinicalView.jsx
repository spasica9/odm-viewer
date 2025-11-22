import React from "react";

export default function ClinicalView({ data }) {
  if (!data || !data.ClinicalData || !data.ClinicalData.length) {
    return <div>No clinical data</div>;
  }

  const pick = (obj, names = []) => {
    if (!obj) return undefined;
    for (const n of names) {
      if (obj[n] !== undefined && obj[n] !== null) return obj[n];
      const low = n.charAt(0).toLowerCase() + n.slice(1);
      const up = n.charAt(0).toUpperCase() + n.slice(1);
      if (obj[low] !== undefined && obj[low] !== null) return obj[low];
      if (obj[up] !== undefined && obj[up] !== null) return obj[up];
    }
    return undefined;
  };

  const arr = (obj, names = []) => {
    if (!obj) return [];
    for (const n of names) {
      if (Array.isArray(obj[n])) return obj[n];
      const low = n.charAt(0).toLowerCase() + n.slice(1);
      const up = n.charAt(0).toUpperCase() + n.slice(1);
      if (Array.isArray(obj[low])) return obj[low];
      if (Array.isArray(obj[up])) return obj[up];
    }
    return [];
  };

  const getVal = (obj, names = []) => {
    if (!obj) return undefined;
    for (const n of names) {
      if (obj[n] !== undefined) return obj[n];
      const low = n.charAt(0).toLowerCase() + n.slice(1);
      const up = n.charAt(0).toUpperCase() + n.slice(1);
      if (obj[low] !== undefined) return obj[low];
      if (obj[up] !== undefined) return obj[up];
    }
    return undefined;
  };

  const study = Array.isArray(data.Study) ? data.Study[0] : data.Study;
  const mdv =
    (pick(study, ["MetaDataVersions", "metaDataVersions", "MetaDataVersion"]) &&
      (pick(study, ["MetaDataVersions"])?.[0] ||
        pick(study, ["MetaDataVersion"]) ||
        pick(study, ["metaDataVersion"]))) ||
    {};
  const mdvObj = mdv || {};

  const itemDefs = pick(mdvObj, ["itemDefs","itemDeves","ItemDef","ItemDefs","ItemDefinition"]) || [];
  const codeLists = pick(mdvObj, ["codeLists","CodeList","CodeLists"]) || [];
  const itemGroupDefs = pick(mdvObj, ["itemGroupDefs","ItemGroupDef","itemGroupDeves"]) || [];
  const studyEventDefs = pick(mdvObj, ["studyEventDefs","StudyEventDef","studyEventDeves"]) || [];
  const formDefs = pick(mdvObj, ["formDefs","FormDef","formDeves"]) || [];

  const itemDefMap = {};
  itemDefs.forEach(d => { const oid = getVal(d, ["OID","oid","ItemOID","itemOID"]); if(oid) itemDefMap[oid] = d; });

  const codeListMap = {};
  codeLists.forEach(c => { const oid = getVal(c, ["OID","oid"]); if(oid) codeListMap[oid] = c; });

  const itemGroupDefMap = {};
  itemGroupDefs.forEach(g => { const oid = getVal(g, ["OID","oid"]); if(oid) itemGroupDefMap[oid] = g; });

  const studyEventDefMap = {};
  studyEventDefs.forEach(s => { const oid = getVal(s, ["OID","oid"]); if(oid) studyEventDefMap[oid] = s; });

  const formDefMap = {};
  formDefs.forEach(f => { const oid = getVal(f, ["OID","oid"]); if(oid) formDefMap[oid] = f; });

  const extractText = node => {
    if (!node) return "";
    if (typeof node === "string") return node.trim();
    if (Array.isArray(node)) return node.map(extractText).filter(Boolean).join(" ");
    if (node.translatedTexts && Array.isArray(node.translatedTexts)) {
      const first = node.translatedTexts[0];
      return extractText(first?.content || first);
    }
    if (node.content) return extractText(node.content);
    if (node.value !== undefined && node.value !== null) return String(node.value).trim();
    if (node._) return String(node._).trim();
    if (node.decode) return extractText(node.decode);
    if (node.description) return extractText(node.description);
    for (const p of ["text","Text","#text"]) if(node[p]) return String(node[p]).trim();
    return "";
  };

  const getDefName = def => {
    if (!def) return undefined;
    return def?.name || def?.Name || def?.label || def?.Label || def?.ItemGroupName || def?.StudyName || def?.title ||
      extractText(def?.description?.translatedTexts?.[0]?.content || def?.description);
  };

  const normalizeProtocol = raw => raw ? String(raw).trim().replace(/^Protocol:\s*/i,"") : "";

  const decodeUsingCodeList = (itemDef, rawValue) => {
    if (!itemDef || rawValue === undefined || rawValue === null) return rawValue;
    const clRef = pick(itemDef, ["codeListRef","CodeListRef"]);
    const codeListOID = clRef?.codeListOID || clRef?.CodeListOID;
    if (!codeListOID) return rawValue;
    const cl = codeListMap[codeListOID];
    if (!cl) return rawValue;
    const items = cl.codeListItems || cl.CodeListItem || [];
    const found = items.find(ci => String(ci?.codedValue) === String(rawValue));
    if (!found) return rawValue;
    return extractText(found?.decode?.translatedTexts?.[0]?.content || found?.decode) || found?.decode?._ || found?.decode || rawValue;
  };

  const cleanOIDForDisplay = oid => oid ? String(oid).replace(/^(IT|IG|SE|FR|CL)\./i,"").replace(/_/g," ") : "";

  const getQuestionTextForItem = itemOID => {
    const def = itemDefMap[itemOID];
    if (!def) return cleanOIDForDisplay(itemOID);
    const t = extractText(def?.description?.translatedTexts?.[0]?.content || def?.description);
    if (t) return t;
    return def?.name || def?.Name || cleanOIDForDisplay(itemOID);
  };

  const getArrayVariants = (obj, variants) => {
    if(!obj) return [];
    for(const v of variants){
      if(Array.isArray(obj[v])) return obj[v];
      const low = v.charAt(0).toLowerCase() + v.slice(1);
      const up = v.charAt(0).toUpperCase() + v.slice(1);
      if(Array.isArray(obj[low])) return obj[low];
      if(Array.isArray(obj[up])) return obj[up];
    }
    return [];
  };

  const getCodeListNameForItem = itemDef => {
    if(!itemDef) return null;
    const clRef = pick(itemDef, ["codeListRef","CodeListRef"]);
    const codeListOID = clRef?.codeListOID || clRef?.CodeListOID;
    if(!codeListOID) return null;
    const cl = codeListMap[codeListOID];
    if(!cl) return null;
    return cl.Name || cl.name || cl.Label || cl.label || cleanOIDForDisplay(codeListOID);
  };

  const renderItemNode = (item, idx) => {
    const itemOID = getVal(item, ["itemOID","ItemOID","ItemOid","itemOid"]);
    let rawValue = Array.isArray(item.values) && item.values.length ? getVal(item.values[0], ["value","Value"])
                 : Array.isArray(item.Value) && item.Value.length ? getVal(item.Value[0], ["value","Value"])
                 : item.value !== undefined ? item.value
                 : item.Value !== undefined && typeof item.Value === "string" ? item.Value
                 : undefined;

    const question = getQuestionTextForItem(itemOID);
    const itemDef = itemDefMap[itemOID];
    const decoded = decodeUsingCodeList(itemDef, rawValue);
    const codeListName = getCodeListNameForItem(itemDef);
    const showRawValue = String(decoded) !== String(rawValue) && rawValue !== undefined && rawValue !== null;

    return (
      <div className="item-row" key={idx}>
        <div><strong>{question}</strong></div>
        {codeListName && <div className="code-list-name">{codeListName}</div>}
        <div className="decoded-value">
          {decoded === undefined || decoded === null ? "N/A" : decoded}
          {showRawValue && <span className="raw-value">({String(rawValue)})</span>}
        </div>
        <div className="item-oid">OID: {itemOID}</div>
      </div>
    );
  };

  const renderGroupRecursive = (node, depth = 0, keyPrefix = "") => {
    if (!node || typeof node !== "object") return null;
    const children = [];

    const itemDatas = getArrayVariants(node, ["itemDatas","ItemData","ItemDatas","itemData"]);
    itemDatas.forEach((item, idx) => children.push(<div key={`${keyPrefix}-item-${idx}`}>{renderItemNode(item, `${keyPrefix}-item-${idx}`)}</div>));

    const wrapperArr = getArrayVariants(node, ["itemGroupDatasAndItemDatas","ItemGroupDatasAndItemDatas"]);
    wrapperArr.forEach((entry, ei) => {
      if (entry.itemOID) children.push(<div key={`${keyPrefix}-leaf-${ei}`}>{renderItemNode(entry, `${keyPrefix}-leaf-${ei}`)}</div>);
      else children.push(<div key={`${keyPrefix}-wrap-${ei}`}>{renderGroupRecursive(entry, depth+1, `${keyPrefix}-wrap-${ei}`)}</div>);
    });

    const igds = getArrayVariants(node, ["itemGroupDatas","ItemGroupData","ItemGroupDatas","itemGroupData"]);
    igds.forEach((g, gi) => {
      const groupName = getDefName(itemGroupDefMap[g.itemGroupOID || g.ItemGroupOID]) || g.name || cleanOIDForDisplay(g.itemGroupOID || g.ItemGroupOID) || "Group";
      children.push(<div key={`${keyPrefix}-ig-${gi}`} style={{marginLeft: depth*14}}>
        <div style={{fontWeight:600, marginTop:6, color:"#f0f0f0"}}>{groupName}</div>
        {renderGroupRecursive(g, depth+1, `${keyPrefix}-ig-${gi}`)}
      </div>);
    });

    const nodeGroupOID = node.itemGroupOID || node.ItemGroupOID || node.ItemGroupId || null;
    let nodeTitle = nodeGroupOID ? getDefName(itemGroupDefMap[nodeGroupOID]) || node.name || cleanOIDForDisplay(nodeGroupOID) : node.name || null;
    const titleColor = nodeGroupOID ? "#a0a0ff" : "#f0f0f0";

    return (
      <div style={{marginLeft: depth*12}} key={keyPrefix || Math.random()}>
        {nodeTitle && <div style={{fontSize:14,fontWeight:700,marginTop:6,color:titleColor}}>{nodeTitle}</div>}
        {children}
      </div>
    );
  };

  const clinical = data.ClinicalData[0];
  const subjects = arr(clinical, ["subjectDatas","SubjectData","subjectData"]) || [];
  const studyName = getDefName(study) || study?.StudyName || (study?.GlobalVariables && study.GlobalVariables.StudyName) || "Study";
  const protocol = normalizeProtocol(study?.ProtocolName || (study?.GlobalVariables && study.GlobalVariables.ProtocolName) || "");

  return (
    <div className="clinical-view">
      <h2>Clinical Data</h2>

      <div className="study-info"><strong>Study:</strong> <span>{studyName}</span></div>
      <div className="study-info"><strong>Protocol:</strong> <span>{protocol || "(unknown)"}</span></div>

      {subjects.length === 0 && <div style={{color:"#aaa"}}>No Subjects</div>}

      {subjects.map((subject, sIdx) => {
        const subjectKey = subject.SubjectKey || subject.subjectKey || subject?.SubjectID || subject?.subjectId || `subject-${sIdx}`;
        const studyEvents = arr(subject, ["studyEventDatas","StudyEventData","studyEventData","StudyEventDatas"]);

        return (
          <div key={sIdx} className="subject-section">
            <h3>Subject: {subjectKey}</h3>

            {studyEvents.length === 0 && getArrayVariants(subject, ["itemGroupDatas","ItemGroupData","itemGroupDatas"]).map((ig,i)=>(
              <div key={i}>{renderGroupRecursive(ig, 1, `subject-${sIdx}-ig-${i}`)}</div>
            ))}

            {studyEvents.map((se,seIdx)=>{
              const seOID = se.studyEventOID || se.StudyEventOID || `se-${seIdx}`;
              const seDef = studyEventDefMap[seOID];
              const seName = getDefName(seDef) || se?.Name || se?.name || cleanOIDForDisplay(seOID);

              const forms = getArrayVariants(se, ["itemGroupDatas","ItemGroupData","ItemGroupDatas","itemGroupData"]);

              return (
                <div key={seIdx} className="study-event-section">
                  <h4>📘 {seName}</h4>

                  {forms.length === 0 && <div style={{marginLeft:8,color:"#ccc"}}>No Forms</div>}

                  {forms.map((form,fIdx)=>{
                    const formOID = form.itemGroupOID || form.ItemGroupOID || form.ItemGroupId || `form-${fIdx}`;
                    const formName = getDefName(formDefMap[formOID]) || form?.name || form?.Name || cleanOIDForDisplay(formOID);

                    return (
                      <div key={fIdx} className="form-section">
                        <h5>📝 {formName}</h5>
                        {renderGroupRecursive(form,1,`se-${seIdx}-form-${fIdx}`)}
                      </div>
                    );
                  })}
                </div>
              );
            })}
          </div>
        );
      })}
    </div>
  );
}