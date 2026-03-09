package com.example.legacy.service;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.legacy.domain.Employee;
import com.example.legacy.util.DateUtil;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportService {

    public File writeReport(File outFile, List<Employee> employees, Map<String, Object> stats) {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("legacyReport");
            root.setAttribute("generatedAt", DateUtil.formatIsoDate(new Date()));
            doc.appendChild(root);

            Element s = doc.createElement("stats");
            root.appendChild(s);
            addText(doc, s, "count", stats.get("count"));
            addText(doc, s, "avgSalary", stats.get("avgSalary"));

            Element emps = doc.createElement("employees");
            root.appendChild(emps);
            employees.stream()
                    .filter(e -> e != null)
                    .forEach(e -> {
                        Element el = doc.createElement("employee");
                        el.setAttribute("id", e.getId());
                        addText(doc, el, "name", e.getFullName());
                        addText(doc, el, "department", e.getDepartment());
                        addText(doc, el, "email", e.getEmail());
                        addText(doc, el, "salary", e.getSalary());
                        emps.appendChild(el);
                    });

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(outFile));

            return outFile;
        } catch (Exception e) {
            throw new RuntimeException("Failed to write report: " + outFile, e);
        }
    }

    private void addText(Document doc, Element parent, String name, Object value) {
        Element e = doc.createElement(name);
        e.appendChild(doc.createTextNode(value == null ? "" : value.toString()));
        parent.appendChild(e);
    }
}