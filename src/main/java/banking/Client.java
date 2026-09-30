package banking;
import java.util.*;

public abstract class Client{
    private String nume;
    private String adresa;
    private String nrTelefon;
    private HashMap <String, ContBancar> conturi;
    private Banca banca;

    public Client(String nume, String adresa, String nrTelefon, Banca banca) throws FormatNrTelefonInvalidException{
        this.nume = nume;
        this.adresa = adresa;
        if(nrTelefon != null &&(nrTelefon.length() != 10 || nrTelefon.substring(0, 1).equals("0") == false)){
            throw new FormatNrTelefonInvalidException();
        }
        this.nrTelefon = nrTelefon;
        this.conturi = new HashMap<>();
        this.banca = banca;
    }

    public ContBancar creeazaContBancar(String iban, double sold, int pin) throws SoldNegativException, FormatIbanInvalidException, IbanDuplicatException{
        if(sold < 0.00) throw new SoldNegativException();

        else if(iban == null || iban.length() != 24){
            throw new FormatIbanInvalidException();
        }

        else if(!iban.startsWith("RO") || !iban.substring(4, 8).equals(this.banca.getCodBanca())){
            throw new FormatIbanInvalidException();
        }

        else if(this.conturi.containsKey(iban)){
            throw new IbanDuplicatException();
        }

        ContBancar contNou = new ContBancar(iban, this.nume, sold, pin);
        this.conturi.put(iban, contNou);
        return contNou;
    }

    public boolean stergeContBancar(String iban){
        if(conturi.remove(iban) != null){
            System.out.println("Contul cu ibanul: " + iban + " a fost sters cu succes");
            return true;
        }
        return false;
    }

    public void afiseazaInformatiiCont(String iban) throws ContInexistentException{
        ContBancar contCautat = this.conturi.get(iban);
        if(contCautat != null){
            System.out.println("Cont gasit:\n" + contCautat.toString());
        }
        else{
            throw new ContInexistentException();
        }
    }

    public void initiazaTransferBancar(String ibanEmitor, String ibanAcceptor, double suma)
            throws IbanEmitorInvalidException, IbanAcceptorInvalidException, SoldInsuficientException, SumaNegativaException,
            FormatIbanInvalidException, ContInexistentException, FonduriInsuficienteException,
            BancaNegasitaException, CardBlocatException, BancaInexistentaException {

        if (!this.conturi.containsKey(ibanEmitor)) {
            throw new IbanEmitorInvalidException();
        }
        else if (ibanAcceptor == null || !ibanAcceptor.startsWith("RO") || ibanAcceptor.length() != 24
                || !Sistem.existaBanca(ibanAcceptor.substring(4, 8))) {
            throw new IbanAcceptorInvalidException();
        }

        ContBancar contEmitor = this.conturi.get(ibanEmitor);
        if (suma > contEmitor.getSold()) {
            throw new SoldInsuficientException();
        }
        else if (suma <= 0.0) {
            throw new SumaNegativaException();
        }

        this.banca.proceseazaTransfer(ibanEmitor, ibanAcceptor, suma);
    }

    public String getNume(){
        return this.nume;
    }

    public String getAdresa(){
        return this.adresa;
    }

    public String getNrTelefon(){
        return this.nrTelefon;
    }

    public String getConturiBancare(){
        String res = "Conturi:\n";
        Iterator<ContBancar> it = this.conturi.values().iterator();
        while(it.hasNext()){
            res += it.next().toString();
            if(it.hasNext()){
                res += ";\n";
            }
        }
        return res;
    }

    public ContBancar getCont(String iban){
        return this.conturi.get(iban);
    }

    public String toString(){
        String res = "Informatii client:\n";
        res += "-nume: " + this.getNume() + "\n";
        res += "-adresa: " + this.getAdresa() + "\n";
        res += "-numar telefon: " + this.getNrTelefon() + "\n";
        res += "-nume banca: " + this.banca.getNume() + "\n";
        res += "-" + this.getConturiBancare() + "\n";
        return res;
    }
}