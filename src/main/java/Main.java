import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*CSV Parse*/
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> employeeListFromCsv = Parsers.parseCSV(columnMapping, fileName);
        System.out.println(employeeListFromCsv.get(1).toString());
        String json = listToJson(employeeListFromCsv);
        writeString(json, "data.json");
        /*XML Parse*/
        List<Employee> employeeListFromXml = Parsers.parseXML("data.xml");
        String jsonFromXml = listToJson(employeeListFromXml);
        /*JSON Parse*/
        String jsonString = readString("new_data.json");
        List<Employee> employeesFromJson = Parsers.jsonToList(jsonString);
        System.out.println("jsonFromCSV = " + json
                + "\njsonFromXml = " + jsonFromXml
                + "\nemployeesFromJson:\n" + employeesFromJson.get(0) + "\n" + employeesFromJson.get(1));
    }

    private static String readString(String jsonFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return stringBuilder.toString();
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }
}
