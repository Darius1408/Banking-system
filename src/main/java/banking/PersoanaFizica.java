package banking;
import java.util.*;

public class PersoanaFizica extends Client{
    private String cnp;

    public PersoanaFizica(String nume, String adresa, String nrTelefon, Banca banca, String cnp) throws CnpInvalidException, FormatNrTelefonInvalidException{
        super(nume, adresa, nrTelefon, banca);

        if(cnp == null || !cnp.matches("^[0-9]{13}$")){
            throw new CnpInvalidException();
        }
        this.cnp = cnp;
    }

    public String getCnp(){
        return this.cnp;
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(!(obj instanceof PersoanaFizica)){
            return false;
        }
        PersoanaFizica other = (PersoanaFizica) obj;
        return this.getCnp().equals(other.getCnp());
    }
}