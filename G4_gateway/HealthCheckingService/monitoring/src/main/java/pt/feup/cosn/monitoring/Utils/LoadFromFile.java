package pt.feup.cosn.monitoring.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoadFromFile {
    private static LoadFromFile LoadFromFileInstance = null;

    public static LoadFromFile getInstance()
    {
        if (LoadFromFileInstance == null)
            LoadFromFileInstance = new LoadFromFile();

        return LoadFromFileInstance;
    }

    public List<JSONObject> convertServiceFromFile(){
        String text = "";
        ArrayList<JSONObject> listOfServices = null;

        try{
            // Gets the file
            InputStream is = getFileAsIOStream("services.json");

            // Gets the file content
            text = getFileContent(is);

            // converts string to list of services
            listOfServices = convertStringToListService(text);


        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfServices;
    }


    private InputStream getFileAsIOStream(final String fileName)
    {
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    private String getFileContent(InputStream is) throws IOException
    {
        String filecontent = "";
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr);)
        {
            String line;
            while ((line = br.readLine()) != null) {
                filecontent = filecontent + line;
            }
            is.close();
        }

        return filecontent;
    }

    private ArrayList<JSONObject> convertStringToListService(String text) throws JSONException {
        JSONArray jsonArray = new JSONArray(text);

        ArrayList<JSONObject> listOfServices = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            listOfServices.add(jsonObject);
        }

        return listOfServices;
    }
}
