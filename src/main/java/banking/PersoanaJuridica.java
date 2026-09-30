package banking;
import java.util.*;

public class PersoanaJuridica extends Client{
    private String cui;
    private String nrInregistrare;

    public PersoanaJuridica(String nume, String adresa, String nrTelefon, Banca banca, String cui, String nrInregistrare) throws FormatCuiInvalidException, FormatNrInregistrareInvalidException, FormatNrTelefonInvalidException{
        super(nume, adresa, nrTelefon, banca);

        if(cui == null || !cui.matches("^(RO)?[0-9]{2,10}$")){
            throw new FormatCuiInvalidException();
        }
        this.cui = cui;

        if(nrInregistrare == null || !nrInregistrare.matches("^[JFC][0-9]{2}/[0-9]{1,6}/[0-9]{4}$")){
            throw new FormatNrInregistrareInvalidException();
        }
        this.nrInregistrare = nrInregistrare;
    }

    public String getCui(){
        return this.cui;
    }

    public String getNrInregistrare(){
        return this.nrInregistrare;
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(!(obj instanceof PersoanaJuridica)){
            return false;
        }
        PersoanaJuridica other = (PersoanaJuridica) obj;
        return this.getCui().equals(other.getCui());
    }
}