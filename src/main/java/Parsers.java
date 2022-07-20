import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parsers {
    protected static List<Employee> parseCSV(String[] columnMapping, String csvFile) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    protected static List<Employee> parseXML(String xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
            NodeList employee = doc.getDocumentElement().getElementsByTagName("employee");
            List<Employee> employees = new ArrayList<>();
            for (int i = 0; i < employee.getLength(); i++) {
                employees.add(new Employee(
                        Long.parseLong(nodeContentByTagName(employee.item(i), "id")),
                        nodeContentByTagName(employee.item(i), "firstName"),
                        nodeContentByTagName(employee.item(i), "lastName"),
                        nodeContentByTagName(employee.item(i), "country"),
                        Integer.parseInt(nodeContentByTagName(employee.item(i), "age"))
                ));
            }
            return employees;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String nodeContentByTagName(Node item, String id) {
        Element element = (Element) item;
        return element.getElementsByTagName(id).item(0).getTextContent();
    }

    protected static List<Employee> jsonToList(String jsonString) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
            JSONArray employee = (JSONArray) jsonObject.get("Employee");
            Gson gson = new GsonBuilder().create();
            for (int i = 0; i < employee.size(); i++) {
                String jsonText = employee.get(i).toString();
                employeeList.add(gson.fromJson(jsonText, Employee.class));
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return employeeList;
    }
}
