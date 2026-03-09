package com.example.legacy.service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.newDocument();

            Element root = doc.createElement("legacyReport");
            root.setAttribute("generatedAt", DateUtil.formatIsoDate(new Date()));
            doc.appendChild(root);

            Element s = doc.createElement("stats");
            root.appendChild(s);

            addText(doc, s, "count", String.valueOf(stats.get("count")));
            addText(doc, s, "avgSalary", String.valueOf(stats.get("avgSalary")));

            Element emps = doc.createElement("employees");
            root.appendChild(emps);

            for (int i = 0; i < employees.size(); i++) {
                Employee e = employees.get(i);
                Element el = doc.createElement("employee");
                el.setAttribute("id", e.getId());
                addText(doc, el, "name", e.getFullName());
                addText(doc, el, "department", e.getDepartment());
                addText(doc, el, "email", e.getEmail());
                addText(doc, el, "salary", String.valueOf(e.getSalary()));
                emps.appendChild(el);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            t.transform(new DOMSource(doc), new StreamResult(outFile));
            return outFile;
        } catch (Exception e) {
            throw new RuntimeException("Failed to write report: " + outFile, e);
        }
    }

    private void addText(Document doc, Element parent, String name, String value) {
        Element e = doc.createElement(name);
        e.appendChild(doc.createTextNode(value == null ? "" : value));
        parent.appendChild(e);
    }
}
