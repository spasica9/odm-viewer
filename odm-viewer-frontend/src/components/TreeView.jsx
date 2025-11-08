import { useState } from "react";

const icons = {
  folder: "📁",
  leaf: "📄",
};

export default function TreeView({ data, name = "root", level = 0, type = "structure" }) {
  const [open, setOpen] = useState(true);

  if (data === null || data === undefined) return null;

  const padding = { paddingLeft: `${level * 20}px` };

  if (typeof data !== "object") {
    return (
      <div className={`tree-node ${type}`} style={padding}>
        <span className="icon">{icons.leaf}</span>
        <strong>{name}: </strong> {String(data)}
      </div>
    );
  }

  const isArray = Array.isArray(data);

  return (
    <div style={padding}>
      <div
        onClick={() => setOpen(!open)}
        className={`tree-node ${type}`}
      >
        <span className="icon">{icons.folder}</span>
        {open ? "▼" : "▶"} {name} {isArray ? `[${data.length}]` : ""}
      </div>

      {open && (
        <div>
          {isArray
            ? data.map((item, i) => (
                <TreeView
                  key={i}
                  data={item}
                  name={`${name}[${i}]`}
                  level={level + 1}
                  type={type}
                />
              ))
            : Object.entries(data).map(([key, value]) => {
                const childType = key.toLowerCase().includes("clinical") || key.toLowerCase().includes("itemdata")
                  ? "clinical"
                  : "structure";
                return (
                  <TreeView
                    key={key}
                    data={value}
                    name={key}
                    level={level + 1}
                    type={childType}
                  />
                );
              })}
        </div>
      )}
    </div>
  );
}
