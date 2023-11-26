package ar.utn.dds.mda.DTOs;

public class MDAPutDTO {
    public String inaccesible;

    public MDAPutDTO() {
    }

    public MDAPutDTO(String inaccesible) {
        this.inaccesible = inaccesible;
    }


    public String getInaccesible() {
        return inaccesible;
    }

    public void setInaccesible(String inaccesible) {
        this.inaccesible = inaccesible;
    }
}
