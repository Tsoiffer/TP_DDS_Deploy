package utils;

import ar.utn.dds.Incidencia.DTOs.IncidenciaPostDTO;
import ar.utn.dds.Incidencia.DTOs.MedidaDeAccesibilidadDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ManejoConsultaApi {

    public static String executeGet(String apiEndpoint){
        String rta = "";
        HttpClient httpClient = HttpClients.createDefault();
        System.out.println("vamos a mandar GET a "+apiEndpoint);
        final HttpGet httpGet = new HttpGet( apiEndpoint );
        System.out.println("Listo GET ");
        try (
                CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet)) {
            StatusLine statusLine = response.getStatusLine();
            System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("Response body: " + responseBody);
            rta = responseBody;
        } catch (
                ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return rta;
    }

    public static List<IncidenciaPostDTO> fromJsonArray(String jsonArray) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNodes = objectMapper.readTree(jsonArray);

            List<IncidenciaPostDTO> incidenciaDTOList = new ArrayList<>();

            for (JsonNode jsonNode : jsonNodes) {
                IncidenciaPostDTO incidencia = new IncidenciaPostDTO();
                incidencia.setCodigoCatalogo(jsonNode.path("codigoCatalogo").path("codigo").asText());
                incidencia.setDescripcion(jsonNode.path("descripcion").asText());
                incidencia.setFechaDeReporte(jsonNode.path("fechaDeReporte").asText());
                incidencia.setEstado(jsonNode.path("estadoAsignado").path("nombre").asText());
                incidenciaDTOList.add(incidencia);
            }

            return incidenciaDTOList;
        } catch (Exception e) {
            e.printStackTrace(); // Manejo adecuado de la excepción en una aplicación real
            return null;
        }
    }

}
