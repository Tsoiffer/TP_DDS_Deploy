package ar.utn.dds.Incidencia;

import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.ManejoConsultaApi;
import utils.ManejoDataset;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class IncidenciasTelegramBot extends TelegramLongPollingBot {
    private  String apiEndpoint;
    private RepoIncidencias repoIncidencias;

    public IncidenciasTelegramBot (String telegramToken, String apiEndpoint, RepoIncidencias repoIncidencias) {
        super(telegramToken);
        this.apiEndpoint = apiEndpoint;
        this.repoIncidencias = repoIncidencias;
    }

    private String formatListIncidencias (List<Incidencia> listIncidencias){

        String mensaje = "";
        if (listIncidencias.isEmpty()){
            return "No se encontraron incidencias";
        }
        for(int i = 0; i< listIncidencias.size();i++){
            mensaje = mensaje + "\n" + (i+1) + ": " + listIncidencias.get(i).toString() + "\n";
        }
        return mensaje;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String[] comando = message.getText().toUpperCase().split(" ");
        System.out.println("llego un mensaje");

        String rta = "";
        String rtaEstInc = """
                El estado ingresado no es correcto.
                Los estados validos son los siguiente:
                REPORTADO, ASIGNADO, CONFIRMADO, EN_PROGRESO, SOLUCIONADO, DESESTIMADO.""";
        switch (comando[0]){
            case "LISTADOULTIMAS" -> {
                System.out.println("comando LISTADOULTIMAS");
                if (Arrays.stream(comando).count()==1){
                    rta = ManejoConsultaApi.executeGet(this.apiEndpoint + "incidencia/listViejas?estado=");
                    break;
                }
                try {
                    Estados estado = Estados.valueOf(comando[1]);
                    rta = ManejoConsultaApi.executeGet(this.apiEndpoint + "incidencia/listViejas?estado="+estado);
                }catch (Exception e) {
                    System.err.println(e);
                    rta = rtaEstInc;
                }
            }
            case "LISTADOPRIMERAS" -> {
                System.out.println("comando LISTADOPRIMERAS");
                if (Arrays.stream(comando).count()==1){
                    rta = ManejoConsultaApi.executeGet(this.apiEndpoint + "incidencia/listMasRecientes?estado=");
                    break;
                }
                try {
                    Estados estado = Estados.valueOf(comando[1]);
                    rta = ManejoConsultaApi.executeGet(this.apiEndpoint + "incidencia/listMasRecientes?estado="+estado);
                } catch (Exception e){
                    System.err.println(e);
                    rta = rtaEstInc;
                }
            }
            case "LISTADOLUGAR" -> {
                System.out.println("comando LISTADOLUGAR");
                if (Arrays.stream(comando).count()==1){
                    rta = """
                              Debe pasar como parametro un lugar.
                              Formato XXXX-XX""";
                    break;
                }
                CodigoCatalogo codigoCatalogo = new CodigoCatalogo(comando[1]);
                rta = ManejoConsultaApi.executeGet(this.apiEndpoint + "/incidencia/lugar/"+codigoCatalogo.getCodigo());
            }

            default -> {
                System.out.println("Comando no renocido");
                rta = """
                        Comando no renocido.
                        Los comandos aceptados son los siguientes:
                        LISTADOULTIMAS [ESTADO], LISTADOPRIMERAS [ESTADO], LISTADOLUGAR XXXX-XX""";
            }
        }
        //Enviar mensaje
        SendMessage responseMsg = new SendMessage();
        responseMsg.setChatId(message.getChatId());
        responseMsg.setText(rta);
        try {
            execute(responseMsg);
        } catch (TelegramApiException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        String nombreBot = System.getenv("NOMBRE_BOT");
        return nombreBot;
    }

    public static void main(String[] args) throws TelegramApiException, IncidenciaInvalidaException {

        RepoIncidencias repoIncidencias = new RepoIncidencias();

        ManejoDataset.setearDatos().stream().forEach(unaIncidencia -> repoIncidencias.save(unaIncidencia));

        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        String apiEndpoint = System.getenv("TP_API");
        if (apiEndpoint == null) {
            apiEndpoint = "http://localhost:8080";
        }
        try {
            telegramBotsApi.registerBot(new IncidenciasTelegramBot(System.getenv("TOKEN_BOT"),
                    apiEndpoint,repoIncidencias));
        } catch (TelegramApiException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

}
