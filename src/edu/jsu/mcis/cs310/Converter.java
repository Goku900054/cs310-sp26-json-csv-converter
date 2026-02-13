package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
   @SuppressWarnings("unchecked")
public static String csvToJson(String csvString) {
    try {
        CSVReader reader = new CSVReader(new StringReader(csvString));
        List<String[]> rows = reader.readAll();
        JsonObject result = new JsonObject();
        JsonArray prodNums = new JsonArray();
        JsonArray colHeadings = new JsonArray();
        JsonArray data = new JsonArray();

        // Row 0 = headers
        String[] headers = rows.get(0);
        for (String header : headers) {
            colHeadings.add(header);
        }

        // Rows 1+ = data
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            JsonArray innerArray = new JsonArray();
            prodNums.add(row[0]);
            innerArray.add(row[1]);
            innerArray.add(Integer.parseInt(row[2]));
            innerArray.add(Integer.parseInt(row[3]));
            innerArray.add(row[4]);
            innerArray.add(row[5]);
            innerArray.add(row[6]);
            data.add(innerArray);
        }

        result.put("ProdNums", prodNums);
        result.put("ColHeadings", colHeadings);
        result.put("Data", data);
        return result.toJson();
    }     
       catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

@SuppressWarnings("unchecked")
public static String jsonToCsv(String jsonString) {
    String result = "";

    try {
        JsonObject jsonObj = (JsonObject) Jsoner.deserialize(jsonString);
        JsonArray prodNums = (JsonArray) jsonObj.get("ProdNums");
        JsonArray colHeadings = (JsonArray) jsonObj.get("ColHeadings");
        JsonArray data = (JsonArray) jsonObj.get("Data");

        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);

        // Write header row
        String[] headers = new String[colHeadings.size()];
        for (int i = 0; i < colHeadings.size(); i++) {
            headers[i] = colHeadings.get(i).toString();
        }
        writer.writeNext(headers);

        // Write each data row
        for (int i = 0; i < data.size(); i++) {
            JsonArray inner = (JsonArray) data.get(i);
            String[] row = new String[7];
            row[0] = prodNums.get(i).toString();
            row[1] = inner.get(0).toString();
            row[2] = inner.get(1).toString();
            row[3] = String.format("%02d", Integer.parseInt(inner.get(2).toString()));
            row[4] = inner.get(3).toString();
            row[5] = inner.get(4).toString();
            row[6] = inner.get(5).toString();
            writer.writeNext(row);
        }

        writer.close();
        result = sw.toString();

    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return result.trim();
        }
}