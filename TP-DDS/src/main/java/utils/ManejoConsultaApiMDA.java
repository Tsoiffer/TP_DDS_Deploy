package utils;

import ar.utn.dds.Incidencia.DTOs.MedidaDeAccesibilidadDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ManejoConsultaApiMDA{
    public String baseURL;  //TODO setear en el contructor http://localhost:8081/medioDeAccesibilidad
    public ManejoConsultaApiMDA (String URL) {
        this.baseURL = URL;
    }
    public boolean validarCodigoCatalogoConMDA(String codigoDeCatalogo) {
        String rta = executeGet(this.baseURL + "/" + codigoDeCatalogo );
        return !rta.isEmpty();
    }
    public String filtrarMDA(String linea, String estacion){    //Devuelve una lista filtrada por query param por linea y estacion los medidas de accesibilidad

        return executeGet(this.baseURL + "/inaccesibles?estacion="+ estacion + "&linea=" + linea);
    }
    public String filtrarMDAInaccesibles(){     //Devuelve una lista con todas los medidas de accesibilidad inaccesibles
        return executeGet(this.baseURL + "/inaccesibles");
    }
    public String getAllMDA(){  //Devuelve una lista con todas los medidas de accesibilidad
        return executeGet(baseURL);
    }
    public void modificarEstadoMDA(String codigoDeCatalogo,String nuevoEstado) throws IOException {
        executePut(this.baseURL + "/" + codigoDeCatalogo, nuevoEstado);
    }
    public static List<MedidaDeAccesibilidadDTO> fromJsonArray(String jsonArray) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNodes = objectMapper.readTree(jsonArray);

            List<MedidaDeAccesibilidadDTO> medidaDTOList = new ArrayList<>();

            for (JsonNode jsonNode : jsonNodes) {
                MedidaDeAccesibilidadDTO medidaDTO = new MedidaDeAccesibilidadDTO();
                medidaDTO.setCodigoDeCatalogo(jsonNode.path("codigoDeCatalogo").asText());
                medidaDTO.setLinea(jsonNode.path("estacion").path("lineaDeTransporte").path("nombre").asText());
                medidaDTO.setEstacion(jsonNode.path("estacion").path("nombreDeLaEstacion").asText());
                medidaDTOList.add(medidaDTO);
            }

            return medidaDTOList;
        } catch (Exception e) {
            e.printStackTrace(); // Manejo adecuado de la excepción en una aplicación real
            return null;
        }
    }
    public static String executeGet(String url) {
        String rta = "";
        HttpClient httpClient = HttpClients.createDefault();
        System.out.println("----------------------------------------");
        System.out.println("-   GET "+ url);
        final HttpGet httpGet = new HttpGet(url);
        try (
                CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                System.out.println("-   GET StatusCode: " + statusCode);
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("-   GET MDA: " + responseBody);
                System.out.println("----------------------------------------");
            if (statusCode >= 200 && statusCode < 300)
                return responseBody;
            else
                return "";


        } catch (
                ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void executePut(String url, String estadoAccesible) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            System.out.println("----------------------------------------");
            System.out.println("-   PUT "+ url);
            HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            String json = "inaccesible="+estadoAccesible;
            StringEntity stringEntity = new StringEntity(json);
            httpPut.setEntity(stringEntity);
            //System.out.println("Executing request " + httpPut.getRequestLine());
            ResponseHandler < String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            };
            String responseBody = httpclient.execute(httpPut, responseHandler);
            System.out.println("-   PUT MDA body: " + responseBody);
            System.out.println("----------------------------------------");
        }
    }
}
