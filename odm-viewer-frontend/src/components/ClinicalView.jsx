import React from "react";

export default function ClinicalView({ data }) {
  if (!data?.ClinicalData?.length) return <div>No clinical data</div>;

  const study = data.Study?.[0] || data.Study;
  const mdv = study?.MetaDataVersions?.[0] || study?.MetaDataVersion || study?.metaDataVersion;
  
  const itemDefs = mdv?.itemDeves || mdv?.itemDefs || [];
  const codeLists = mdv?.codeLists || mdv?.CodeList || [];
  const valueListDefs = mdv?.valueListDeves || mdv?.valueListDefs || mdv?.ValueListDef || [];

  const codeListMap = {};
  codeLists.forEach(cl => {
    const oid = cl.oid || cl.OID;
    if (oid) codeListMap[oid] = cl;
  });

  const itemDefMap = {};
  itemDefs.forEach(item => {
    const oid = item.oid || item.OID;
    if (oid) itemDefMap[oid] = item;
  });

  const valueListMap = {};
  valueListDefs.forEach(vl => {
    const oid = vl.oid || vl.OID;
    if (oid) {
      const itemRefs = vl.itemRefs || vl.ItemRef || vl.itemReves || [];
      valueListMap[oid] = { ...vl, itemRefs };
    }
  });
 
  const extractTextFromContent = (content) => {
    if (!content || !Array.isArray(content)) return '';
    
    const textParts = [];
    
    content.forEach(item => {
      if (typeof item === 'string') {
        textParts.push(item.trim());
      } else if (item && typeof item === 'object') {
        if (item.div) {
          const divText = extractTextFromContent(item.div.content || [item.div]);
          if (divText) textParts.push(divText);
        } else if (item.content) {
          const nestedText = extractTextFromContent(
            Array.isArray(item.content) ? item.content : [item.content]
          );
          if (nestedText) textParts.push(nestedText);
        } else {
          const objectText = extractTextFromObject(item);
          if (objectText) textParts.push(objectText);
        }
      }
    });
    
    const result = textParts.filter(part => part).join(' ').trim();
    return result;
  };

  const extractTextFromObject = (obj) => {
    if (!obj || typeof obj !== 'object') return '';
    
    const possibleTextProps = ['value', 'text', 'content', '_value', '_content', '#text'];
    
    for (const prop of possibleTextProps) {
      if (obj[prop] !== undefined && obj[prop] !== null) {
        const text = String(obj[prop]).trim();
        if (text) return text;
      }
    }
    
    return '';
  };

  const getQuestionText = (itemOID) => {
    const itemDef = itemDefMap[itemOID];
    
    if (itemDef) {
      if (itemDef.description?.translatedTexts?.[0]) {
        const translatedText = itemDef.description.translatedTexts[0];
        if (translatedText.content && Array.isArray(translatedText.content)) {
          const textContent = extractTextFromContent(translatedText.content);
          if (textContent) return textContent;
        }
      }
      
      if (itemDef.name) return itemDef.name;
    }

    return itemOID
      .replace(/^IT\./, '')
      .replace(/_/g, ' ')
      .replace(/([A-Z])/g, ' $1')
      .trim();
  };

  const getAnswerText = (itemOID, value) => {
    if (!value && value !== 0) return "N/A";

    const itemDef = itemDefMap[itemOID];
    if (!itemDef) return value;
    

    const codeListRef = itemDef.codeListRef;
    if (codeListRef) {
      const codeListOID = codeListRef.codeListOID;
      
      if (codeListOID && codeListMap[codeListOID]) {
        const codeList = codeListMap[codeListOID];
        const codeListItems = codeList.codeListItems || [];
        
        const codeItem = codeListItems.find(ci => 
          ci.codedValue?.toString() === value?.toString()
        );
        
        if (codeItem?.decode?.translatedTexts?.[0]) {
          const translatedText = codeItem.decode.translatedTexts[0];
          if (translatedText.content && Array.isArray(translatedText.content)) {
            return extractTextFromContent(translatedText.content);
          }
        }
      }
    }
    
    const valueListRef = itemDef.valueListRef;
    if (valueListRef) {
      const valueListOID = valueListRef.valueListOID;
      
      if (valueListOID && valueListMap[valueListOID]) {
        const valueList = valueListMap[valueListOID];
        const itemRefs = valueList.itemRefs || [];
        
        for (const itemRef of itemRefs) {
          const refItemOID = itemRef.itemOID;
          const refItemDef = itemDefMap[refItemOID];
          if (refItemDef) {
            const decoded = getAnswerText(refItemOID, value);
            if (decoded !== value) return decoded;
          }
        }
      }
    }

    return value;
  };

  const extractAllItemData = (container, parentGroup = {}) => {
    const items = [];
    
    if (!container || typeof container !== 'object') return items;

    if (container.itemOID && (container.value !== undefined || container.values)) {
      const itemData = {
        itemOID: container.itemOID,
        value: container.value || container.values?.[0]?.value,
        itemGroupOID: parentGroup.itemGroupOID,
        itemGroupRepeatKey: parentGroup.itemGroupRepeatKey
      };
      items.push(itemData);
    }

    for (const key in container) {
      if (container[key] && typeof container[key] === 'object') {
        const newParent = {
          itemGroupOID: container.itemGroupOID || parentGroup.itemGroupOID,
          itemGroupRepeatKey: container.itemGroupRepeatKey || parentGroup.itemGroupRepeatKey
        };
        
        if (Array.isArray(container[key])) {
          container[key].forEach((item) => {
            if (key === 'itemGroupDatas' || !item.studyEventOID) {
              items.push(...extractAllItemData(item, newParent));
            }
          });
        } else {
          if (key !== 'studyEventDatas' && !container[key].studyEventOID) {
            items.push(...extractAllItemData(container[key], newParent));
          }
        }
      }
    }

    return items;
  };

  const groupMatrixEntries = (items) => {
    const groups = {};
    
    items.forEach(item => {
      const key = item.itemGroupRepeatKey ? 
        `${item.itemGroupOID}-${item.itemGroupRepeatKey}` : 
        `${item.itemGroupOID || 'no-group'}-default`;
      
      if (!groups[key]) groups[key] = [];
      
      const exists = groups[key].some(existingItem => 
        existingItem.itemOID === item.itemOID && 
        existingItem.value === item.value &&
        existingItem.itemGroupOID === item.itemGroupOID
      );
      
      if (!exists) {
        groups[key].push(item);
      }
    });
    
    return Object.values(groups);
  };

  const subjects = data.ClinicalData[0].subjectDatas || [];
  
  return (
    <div className="clinical-view">
      <h2>Clinical Data</h2>
      
      <div className="scroll-container">
        <div className="tree-card-wrapper">
          <div className="tree-card">
            {subjects.map((subject, subjectIndex) => (
              <div key={subjectIndex} className="subject-section">
                <h3>Subject: {subject.subjectKey}</h3>
                
                {subject.studyEventDatas?.map((studyEvent, seIndex) => {
                  const allItems = (studyEvent.itemGroupDatas || []).flatMap(formGroup => 
                    extractAllItemData(formGroup, {
                      itemGroupOID: formGroup.itemGroupOID,
                      itemGroupRepeatKey: formGroup.itemGroupRepeatKey
                    })
                  );

                  if (allItems.length === 0) return null;

                  const groupedItems = groupMatrixEntries(allItems);

                  return (
                    <div key={seIndex} className="study-event-section">
                      <h4>📘 {studyEvent.studyEventOID}</h4>
                      {renderItemGroups(groupedItems)}
                    </div>
                  );
                })}
                {(!subject.studyEventDatas || subject.studyEventDatas.length === 0) && 
                  (() => {
                    const subjectItems = extractAllItemData(subject);
                    if (subjectItems.length > 0) {
                      const groupedItems = groupMatrixEntries(subjectItems);
                      return (
                        <div className="subject-data-section">
                          <h4>Subject Data</h4>
                          {renderItemGroups(groupedItems)}
                        </div>
                      );
                    }
                    return null;
                  })()}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );

  function renderItemGroups(itemGroups) {
    return (
      <div>
        {itemGroups.map((group, groupIndex) => (
          <div key={groupIndex} className="item-group">
            {group.length > 1 && (
              <div className="group-header">Entry {group[0]?.itemGroupRepeatKey || groupIndex + 1}</div>
            )}
            {group.map((item, itemIndex) => {
              const question = getQuestionText(item.itemOID);
              const answer = getAnswerText(item.itemOID, item.value);

              return (
                <div key={`${item.itemOID}-${item.value}-${itemIndex}`} className="item-row">
                  <strong>{question}:</strong> 
                  <span>{answer}</span>
                  <div className="item-details">
                    OID: {item.itemOID}, Value: {item.value}
                  </div>
                </div>
              );
            })}
          </div>
        ))}
      </div>
    );
  }
}