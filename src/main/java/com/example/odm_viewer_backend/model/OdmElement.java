package com.example.odm_viewer_backend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OdmElement {

    private String tagName;
    private Map<String, String> attributes = new HashMap<>();
    private String textContent;
    private List<OdmElement> children = new ArrayList<>();

    public OdmElement() {
    }

    public OdmElement(String tagName) {
        this.tagName = tagName;
    }

    public void addChild(OdmElement child) {
        children.add(child);
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<OdmElement> getChildren() {
        return children;
    }

    public void setChildren(List<OdmElement> children) {
        this.children = children;
    }

}
