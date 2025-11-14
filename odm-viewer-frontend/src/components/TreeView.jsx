import React, { useState } from "react";
import { Folder, FileText, ChevronDown, ChevronRight, Database, Layers, Hash } from 'lucide-react';

const icons = {
    folder: Folder,
    leaf: FileText,
    structure: Layers,
    clinical: Database,
};

const isPrimitiveWrapper = (data) => {
    if (data === null || typeof data !== 'object' || Array.isArray(data)) return false;
    const keys = Object.keys(data);
    return keys.length === 1 && (typeof data[keys[0]] !== 'object' || data[keys[0]] === null || Array.isArray(data[keys[0]]));
};

const shouldSkipKey = (key, value) => {
    const lowerKey = key.toLowerCase();
    if (lowerKey.startsWith("jaxbelement")) return true;
    if (lowerKey.endsWith("property")) return true;
    if (value === null || value === undefined) return true;
    if (Array.isArray(value) && value.length === 0) return true;
    return false;
};

export default function TreeView({ data, name = "root", level = 0, type = "structure" }) {
    const initialOpenState = level < 2;
    const [open, setOpen] = useState(initialOpenState);


    if (data === null || data === undefined) {
        return (
            <div className={`tree-node ${type}`} style={{ paddingLeft: level * 20 }}>
                <Hash size={14} className="icon" />
                <strong className="node-name">{name}:</strong>
                <span className="node-value">null</span>
            </div>
        );
    }

    if (typeof data !== "object") {
        return (
            <div className={`tree-node ${type}`} style={{ paddingLeft: level * 20 }}>
                <Hash size={14} className="icon" />
                <strong className="node-name">{name}:</strong>
                <span className="node-value">{String(data)}</span>
            </div>
        );
    }

    if (isPrimitiveWrapper(data)) {
        const key = Object.keys(data)[0];
        const value = data[key];
        if (typeof value !== 'object' || value === null) {
            return (
                <div className={`tree-node ${type}`} style={{ paddingLeft: level * 20 }}>
                    <Hash size={14} className="icon" />
                    <strong className="node-name">{name} (Attr):</strong>
                    <span className="node-value">{String(value)}</span>
                </div>
            );
        }
    }

    const isArray = Array.isArray(data);
    const Icon = isArray ? icons.leaf : icons.folder;
    const TypeIcon = type === 'structure' ? icons.structure : icons.clinical;

    const childEntries = isArray ? data : Object.entries(data).filter(([key, value]) => !shouldSkipKey(key, value));
    const hasChildren = childEntries.length > 0;

    return (
        <div className="select-none">

            <div
                className={`tree-node ${type}`}
                style={{ paddingLeft: level * 20 }}
                onClick={() => setOpen(!open)}
            >
                {hasChildren ? (
                    open ? <ChevronDown size={14} className="icon" /> : <ChevronRight size={14} className="icon" />
                ) : (
                    <span style={{ width: 14 }} />
                )}

                <TypeIcon size={16} className="icon" />
                <span className="node-name">{name}</span>

                {!isArray && <span className="node-value">({childEntries.length} items)</span>}
                {isArray && <span className="node-value">[{data.length}]</span>}
            </div>

            {open && hasChildren && (
                <div className="tree-children">
                    {isArray
                        ? childEntries.map((item, i) => (
                            <TreeView key={i} data={item} name={`${name}[${i}]`} level={level + 1} type={type} />
                        ))
                        : childEntries.map(([key, value]) => {
                            const childType = key.toLowerCase().includes("clinical") || key.toLowerCase().includes("itemdata") ? "clinical" : type;
                            return <TreeView key={key} data={value} name={key} level={level + 1} type={childType} />;
                        })}
                </div>
            )}

            {!open && hasChildren && (
                <div className="tree-collapsed">
                    {childEntries.length} {isArray ? "elements" : "items"} hidden...
                </div>
            )}
        </div>
    );
}




