package org.example;

import org.example.entities.Variavel;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String URL_LISTOFVARIABLES = "http://localhost:8089/v1/petroapi/variaveis/listofvariables";
        final String URL_LOGIN = "http://localhost:8089/v1/petroapi/usuarios/login";
        URL url = new URL(URL_LOGIN);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoOutput(true);
        String jsonInputString = "{\"senha\": \"admin\", \"email\": \"admin@email.com\"}";
        StringBuilder token = new StringBuilder();
        try(OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            int startToken = response.indexOf("Bearer") + "Bearer".length() + 1;
            int endToken = response.substring(startToken).indexOf("\"") + startToken;
            token.append(response.substring(startToken, endToken));
        }
        List<Variavel> variavelList = List.of(Variavel
                .builder()
                .tag("tag1")
                .name("name1")
                .mfgName("mfgName1")
                .plataforma("plataforma1")
                .build(),Variavel
                .builder()
                .tag("tag2")
                .name("name2")
                .mfgName("mfgName2")
                .plataforma("plataforma2")
                .build());
        Map<String, List<Variavel>> mapaDeVariaveisParaJSON = new HashMap<>();
        mapaDeVariaveisParaJSON.put("chave", variavelList);
        JSONObject jsonObject = new JSONObject(mapaDeVariaveisParaJSON);
        String jsonString = jsonObject.toString();
        URL urlEnviarVariaveis = new URL(URL_LISTOFVARIABLES);
        HttpURLConnection conexaoComToken = (HttpURLConnection) urlEnviarVariaveis.openConnection();
        conexaoComToken.setRequestMethod("POST");
        conexaoComToken.setRequestProperty("Content-Type", "application/json");
        conexaoComToken.setRequestProperty("Accept", "application/json");
        conexaoComToken.setRequestProperty("Authorization", "Bearer " + token);
        conexaoComToken.setDoOutput(true);
        try(OutputStream os = conexaoComToken.getOutputStream()) {
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conexaoComToken.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}