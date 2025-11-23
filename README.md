# ODM Viewer: Web Application for Inspecting CDISC ODM XML Files

## Project Description

**ODM File Viewer** is a web application designed to visually inspect and analyze CDISC Operational Data Model XML files.  
It provides two main views:

### **Structure View**
Displays the *entire* study structure defined in the ODM file, including:
- hierarchical study elements  
- branching and nested structures  
- workflows  
- timing elements  
- methods  
- definitions and metadata  
- any other structural components included in the XML  

This view represents the full metadata model as defined in the ODM.

### **Clinical View**
Displays all clinical data contained in the file:
- Subjects  
- Study Events  
- Item Groups  
- Items and their values  
- Repeating groups  
- Nested item structures  

Values are clearly shown along with item names and OIDs.  
This view is designed to make inspection of collected subject data simple and readable.

The project consists of:
- **Backend:** Spring Boot + JAXB for XML parsing  
- **Frontend:** React + Vite for display and user interaction  

---

## Technologies

### **Frontend**
- React  
- JavaScript  
- Vite  
- HTML/CSS  

### **Backend**
- Spring Boot  
- Java  
- JAXB (XML binding)  
- Apache Maven  

### **Communication**
- REST API (JSON)

---

## Installation & Running the Application

### **Requirements**
- Node.js  
- Java 17+  
- Maven  

---

## Backend Setup (Spring Boot)

1. Navigate to the backend folder:
    ```bash
    cd odm-viewer-backend
    ```
2. Install dependencies:

    ```bash
    mvn clean install
    ```

3. Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```

Backend runs on:
http://localhost:8080

---

## Frontend Setup (React + Vite)

1. Navigate to the frontend folder:
    ```bash
    cd odm-viewer-frontend
    ```
2. Install dependencies:
    ```bash
    npm install
    ```
3. Run the application:
    ```bash
    npm run dev
    ```
Frontend typically runs on:
http://localhost:5173

---

## How It Works

**Uploading XML**

Users upload an ODM XML file, which is sent to the backend.
The backend parses the file using JAXB and returns a structured JSON representation.

**Structure View**

Shows the full metadata structure of the study.

**Clinical View**

Shows subjects, events, item groups, items, and values in an organized layout.

---

## Project Structure

### Backend (Spring Boot)

```bash
odm-viewer-backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── odm/                     # JAXB-generated classes
│   │   │   ├── odm_viewer_backend/
│   │   │   │   ├── controller/
│   │   │   │   │   ├── FrontendController.java
│   │   │   │   │   └── UploadController.java
│   │   │   │   ├── service/
│   │   │   │   │   └── OdmParser.java
│   │   │   │   └── OdmViewerBackendApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       ├── xsd/                     # ODM XSD schemas
│   │       └── application.properties
│   └── test/
│
└── pom.xml
```
### Frontend (React + Vite)

```bash
odm-viewer-frontend/
├── src/
│   ├── api/
│   │   └── odmApi.js
│   ├── assets/
│   │   └── react.svg
│   ├── components/
│   │   ├── ClinicalView.jsx
│   │   ├── StructureView.jsx
│   │   ├── TreeView.jsx
│   │   └── UploadForm.jsx
│   ├── pages/
│   ├── App.jsx
│   ├── App.css
│   ├── index.css
│   └── main.jsx
├── public/
├── vite.config.js
└── package.json
```

---

## Deployment

The app can be deployed as:

- Vite frontend (static files) served by any hosting provider

- Spring Boot backend running on a remote server

When deployed, the frontend interacts with the backend via REST API endpoints.

---

## Author
Anastasija Spasić

## Repository
https://github.com/spasica9/odm-viewer
