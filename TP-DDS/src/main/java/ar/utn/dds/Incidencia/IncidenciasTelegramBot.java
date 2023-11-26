package ar.utn.dds.Incidencia;

import ar.utn.dds.Incidencia.DTOs.IncidenciaPostDTO;
import ar.utn.dds.Incidencia.DTOs.MedidaDeAccesibilidadDTO;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.util.json.JSONArray;
import org.h2.util.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.ManejoConsultaApi;
import utils.ManejoConsultaApiMDA;

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
        String rta = "";
        String rtaEstInc = """
                El estado ingresado no es correcto.
                Los estados validos son los siguiente:
                REPORTADO, ASIGNADO, CONFIRMADO, EN_PROGRESO, SOLUCIONADO, DESESTIMADO.""";
        switch (comando[0]){
            case "LISTADOULTIMAS" -> {
                System.out.println("comando LISTADOULTIMAS");
                if (Arrays.stream(comando).count()==1){

                    List<IncidenciaPostDTO> incidencias = ManejoConsultaApi.fromJsonArray(ManejoConsultaApi.executeGet(this.apiEndpoint + "/incidencia/listViejas"));
                    int i = 0;
                    for(IncidenciaPostDTO incidencia : incidencias)
                    {
                        rta= rta + "Incidencia: " + i + '\n' +
                                "Codigo de catalogo: " + incidencia.getCodigoCatalogo() + '\n' + "Descripcion: " + incidencia.getDescripcion() + '\n' + "Fecha de reporte: "+ incidencia.getFechaDeReporte() + '\n' + "Estado asignado: "+ incidencia.getEstado() + '\n' + '\n';
                        i++;
                    }
                    break;
                }
                try {
                    String estado = comando[1];

                    List<IncidenciaPostDTO> incidencias = ManejoConsultaApi.fromJsonArray(ManejoConsultaApi.executeGet(this.apiEndpoint + "/incidencia/listViejas?estado="+estado));
                    int i = 0;
                    for(IncidenciaPostDTO incidencia : incidencias)
                    {
                        rta= rta + "Incidencia: " + i + '\n' +
                                "Codigo de catalogo: " + incidencia.getCodigoCatalogo() + '\n' + "Descripcion: " + incidencia.getDescripcion() + '\n' + "Fecha de reporte: "+ incidencia.getFechaDeReporte() + '\n' + "Estado asignado: "+ incidencia.getEstado() + '\n' + '\n';
                        i++;
                    }

                }catch (Exception e) {
                    System.err.println(e);
                    rta = rtaEstInc;
                }
            }
            case "LISTADOPRIMERAS" -> {
                System.out.println("comando LISTADOPRIMERAS");
                if (Arrays.stream(comando).count()==1){

                    List<IncidenciaPostDTO> incidencias = ManejoConsultaApi.fromJsonArray(ManejoConsultaApi.executeGet(this.apiEndpoint + "/incidencia/listMasRecientes?estado="));
                    int i = 0;
                    for(IncidenciaPostDTO incidencia : incidencias)
                    {
                        rta= rta + "Incidencia: " + i + '\n' +
                                "Codigo de catalogo: " + incidencia.getCodigoCatalogo() + '\n' + "Descripcion: " + incidencia.getDescripcion() + '\n' + "Fecha de reporte: "+ incidencia.getFechaDeReporte() + '\n' + "Estado asignado: "+ incidencia.getEstado() + '\n' + '\n';
                        i++;
                    }
                    break;
                }
                try {
                    String estado = comando[1];

                    List<IncidenciaPostDTO> incidencias = ManejoConsultaApi.fromJsonArray(ManejoConsultaApi.executeGet(this.apiEndpoint + "/incidencia/listMasRecientes?estado="+estado));
                    int i = 0;
                    for(IncidenciaPostDTO incidencia : incidencias)
                    {
                        rta= rta + "Incidencia: " + i + '\n' +
                                "Codigo de catalogo: " + incidencia.getCodigoCatalogo() + '\n' + "Descripcion: " + incidencia.getDescripcion() + '\n' + "Fecha de reporte: "+ incidencia.getFechaDeReporte() + '\n' + "Estado asignado: "+ incidencia.getEstado() + '\n' + '\n';
                        i++;
                    }

                } catch (Exception e){
                    System.err.println(e);
                    rta = rtaEstInc;
                }
            }
            case "LISTADOLUGAR" -> {
                System.out.println("comando LISTADOLUGAR");
                if (Arrays.stream(comando).count()==1){
                    rta = """
                              Debe pasar como parametro un codigo de catalogo.
                              Formato XXXX-XX""";
                    break;
                }
                CodigoCatalogo codigoCatalogo = new CodigoCatalogo(comando[1]);

                List<IncidenciaPostDTO> incidencias = ManejoConsultaApi.fromJsonArray(ManejoConsultaApi.executeGet(this.apiEndpoint + "/incidencia/lugar/"+codigoCatalogo.getCodigo()));
                int i = 0;
                for(IncidenciaPostDTO incidencia : incidencias)
                {
                    rta= rta + "Incidencia: " + i + '\n' +
                            "Codigo de catalogo: " + incidencia.getCodigoCatalogo() + '\n' + "Descripcion: " + incidencia.getDescripcion() + '\n' + "Fecha de reporte: "+ incidencia.getFechaDeReporte() + '\n' + "Estado asignado: "+ incidencia.getEstado() + '\n' + '\n';
                    i++;
                }
            }
            case "LISTADOINACCESIBLES" -> {
                System.out.println("comando LISTADOINACCESIBLES");
                ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv().get("MDA_API")); // TODO variable de entorno

                List<MedidaDeAccesibilidadDTO> mdas = consultador.fromJsonArray(consultador.filtrarMDAInaccesibles());
                int i = 0;
                for(MedidaDeAccesibilidadDTO mda : mdas)
                {
                    rta= rta + "Medida de accesibiliad: " + i + '\n' +
                                "Codigo de catalogo: " + mda.getCodigoDeCatalogo() + '\n' + "Linea: " + mda.getLinea() + '\n' + "Estacion: "+ mda.getEstacion() + '\n' + '\n';
                    i++;
                }
            }

            case "LISTADOESTACION" -> {
                System.out.println("comando LISTADOESTACION");
                if (Arrays.stream(comando).count()==1)
                {
                    rta = """
                              Debe pasar como parametro una linea y una estacion.
                              """;
                }
                else
                {
                    ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv().get("MDA_API")); // TODO variable de entorno
                    String linea = comando[1];
                    String estacion = comando[2];

                    List<MedidaDeAccesibilidadDTO> mdas = consultador.fromJsonArray(consultador.filtrarMDA(linea, estacion));
                    int i = 0;
                    for(MedidaDeAccesibilidadDTO mda : mdas)
                    {
                        rta= rta + "Medida de accesibiliad: " + i + '\n' +
                                "Codigo de catalogo: " + mda.getCodigoDeCatalogo() + '\n' + "Linea: " + mda.getLinea() + '\n' + "Estacion: "+ mda.getEstacion() + '\n' + '\n';
                        i++;
                    }
                }
            }

            default -> {
                System.out.println("Comando no reconocido");
                rta = """
                        Comando no renocido.
                        Los comandos aceptados son los siguientes:
                        * LISTADOULTIMAS [ESTADO]: Listado de las incidencias mas viejas creadas.
                        * LISTADOPRIMERAS [ESTADO]: Listado de las incidencias mas nuevas creadas.
                        * LISTADOLUGAR XXXX-XX: Listado de las incidencias en un lugar en particular.
                        * LISTADOINACCESIBLES: Listado de los medios de accesibilidad inaccesibles.
                        * LISTADOESTACION LINEA ESTACION: Listado de los medios de accesibilidad inaccesibles para una estacion de una linea.
                        """;
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

        //ManejoDataset.setearDatos().stream().forEach(unaIncidencia -> repoIncidencias.save(unaIncidencia));

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
