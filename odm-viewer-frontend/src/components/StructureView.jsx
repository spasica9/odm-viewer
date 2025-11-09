import React from "react";
import TreeView from "./TreeView";

export default function StructureView({ data }) {
    if (!data) return null;

    const studies = data.Study || [];

    const structureTree = {
        FileOID: data.FileOID,
        ODMVersion: data.ODMVersion,
        CreationDateTime: data.CreationDateTime,
        Studies: studies.map(study => ({
            StudyName: study.StudyName,
            ProtocolName: study.ProtocolName,
            MetaDataVersions: (study.MetaDataVersions || []).map(md => ({
                Name: md.Name,
                StudyEventDef: (md.StudyEventDef || []).map(ev => ({
                    Name: ev.Name,
                    Repeating: ev.Repeating,
                    Category: ev.Category,
                    FormRefs: (ev.FormRef || []).map(fr => ({
                        FormOID: fr.FormOID
                    }))
                })),
                FormDef: (md.FormDef || []).map(form => ({
                    Name: form.Name,
                    ItemGroupRefs: (form.ItemGroupRef || []).map(ig => ({
                        ItemGroupOID: ig.ItemGroupOID
                    }))
                })),
                ItemGroupDef: (md.ItemGroupDef || []).map(ig => ({
                    Name: ig.Name,
                    Items: (ig.ItemRef || []).map(ir => ({
                        ItemOID: ir.ItemOID
                    }))
                })),
                ItemDef: (md.ItemDef || []).map(item => ({
                    Name: item.Name,
                    DataType: item.DataType
                }))
            }))
        }))
    };

    return (
        <div className="tree-card">
            <h2>Structure (Metadata)</h2>
            <TreeView data={structureTree} type="structure" />
        </div>
    );
}

