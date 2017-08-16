package lab7;

import au.com.bytecode.opencsv.CSVReader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Lab7 {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        String fileName = "test_two-anon.csv";
        File file = new File(fileName);

        // this gives you a 2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;
        
        try {
            inputStream = new Scanner(file);
            
            while (inputStream.hasNext()) {
                
                String line = inputStream.nextLine();
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // the following code lets you iterate through the 2-dimensional array
        //int lineNo = 1;

//        for (List<String> line : lines) {
//            System.out.println(line);
//        }

        List <List <String>> parsedData = EscapeQuotes(lines);
       
List<String> stringList = new ArrayList<>();
for (List<String> l : parsedData)
  stringList.addAll(l);
String[] strings = parsedData.toArray(new String[stringList.size()]);
       
       
        String url = "jdbc:mysql://localhost:3306/password";
        String userName = "root";
        String password = "seecs@123";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection(url, userName, password);
        try {
            String insertQuery = "Insert into data (user_id,scheme,time_taken,state,t_c1,state_1,t_c2,state_2,t_c3,state_3,t_c4,state_4t_c5,state_5,t_c6,state_6t_c7,state_7) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            //String[] rowData = null;
            //System.out.println("line43");
            int i = 0;
            //while ((rowData = reader.readNext()) != null) {
                for (String data: strings) {
                    pstmt.setString((i % 18) + 1, data);
                    if (++i % 18 == 0) {
                        pstmt.addBatch();// add batch
                        //System.out.println("line49");
                    }
                    if (i % 180 == 0)// insert when the batch size is 10
                    {
                        //System.out.println("line53");
                        pstmt.executeBatch();
                    }
                }
            //}
            System.out.println("Data Successfully Uploaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public static List <List <String>> EscapeQuotes(List<List<String>> lines){
        List <List <String> >  csvData = new ArrayList(); 
        for (List<String> row : lines) {
            List <String> tempRow = new ArrayList();
            for (String attr_val : row){
                
                String[] values = attr_val.split("\"");
                if (values.length != 0){
                   
                    tempRow.add(new String(values[1]));
                }
            }
            csvData.add(tempRow);
        }
        return csvData;
    }
    public boolean CheckAttrValue (int attr_col_no, List <String> schema){
        
        if(schema.get(attr_col_no).isEmpty()){
           return false; 
        }
        return true;
    }
    
    
    
}
