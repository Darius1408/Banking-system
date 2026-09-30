package banking;
import java.util.*;

public class Banca{
    private String nume;
    private String adresa;
    private String nrTelefon;
    private String email;
    private String codBanca;
    private String cui;
    private String nrInregistrare;
    private HashMap <String, Client> clienti;

    public Banca(String nume, String adresa, String nrTelefon, String email, String cui, String nrInregistrare, String codBanca) throws FormatNrTelefonInvalidException, FormatEmailInvalidException, FormatCuiInvalidException, FormatNrInregistrareInvalidException, FormatCodBancaInvalidException{
        this.nume = nume;
        this.adresa = adresa;
        if(!nrTelefon.matches("^0[0-9]{9}") || nrTelefon == null){
            throw new FormatNrTelefonInvalidException();
        }
        this.nrTelefon = nrTelefon;
        if(email == null || !email.matches("^[0-9a-zA-Z]{1,50}@[0-9a-zA-Z]{1,50}(\\.com)$")){
            throw new FormatEmailInvalidException();
        }
        this.email = email;
        if(cui == null || !cui.matches("^(RO)?[0-9]{2,10}$")){
            throw new FormatCuiInvalidException();
        }
        this.cui = cui;
        if(nrInregistrare == null || !nrInregistrare.matches("^[JFC][0-9]{2}/[0-9]{1,6}/[0-9]{4}$")){
            throw new FormatNrInregistrareInvalidException();
        }
        this.nrInregistrare = nrInregistrare;

        if(!codBanca.matches("^[A-Z]{4}$")){
            throw new FormatCodBancaInvalidException();
        }
        this.codBanca = codBanca;
        this.clienti = new HashMap<>();
    }

    public void adaugaClient(Client client) throws ClientInexistentException{
        if(client == null) throw new ClientInexistentException();
        this.clienti.put(client.getNume(), client);
        System.out.println("Client adaugat cu succes!");
    }

    public void stergeClient(String nume){
        if(nume == null) return;
        this.clienti.remove(nume);
        System.out.println("Client sters cu succes!");
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(!(obj instanceof Banca)){
            return false;
        }
        Banca other = (Banca) obj;
        return this.nume.equals(other.nume) && this.cui.equals(other.cui);
    }

    private ContBancar gasesteCont(String iban) throws ContInexistentException{
        for(Client ct: this.clienti.values()){
            ContBancar cont = ct.getCont(iban);
            if(cont != null){
                return cont;
            }
        }
        throw new ContInexistentException();
    }

    public void proceseazaTransfer(String ibanEmitor, String ibanAcceptor, double suma)
            throws FormatIbanInvalidException, SumaNegativaException, ContInexistentException,
            FonduriInsuficienteException, BancaNegasitaException,
            CardBlocatException, BancaInexistentaException, SoldInsuficientException {

        if (ibanEmitor == null || ibanEmitor.length() != 24 || !ibanEmitor.startsWith("RO") || !ibanEmitor.substring(4, 8).matches("^[A-Z]{4}$")) {
            throw new FormatIbanInvalidException();
        }
        else if (ibanAcceptor == null || ibanAcceptor.length() != 24 || !ibanAcceptor.startsWith("RO") || !ibanAcceptor.substring(4, 8).matches("^[A-Z]{4}$")) {
            throw new FormatIbanInvalidException();
        }
        else if (suma <= 0.0) {
            throw new SumaNegativaException();
        }

        ContBancar contEmitor = gasesteCont(ibanEmitor);
        if (contEmitor == null) {
            throw new ContInexistentException();
        }
        if (contEmitor.getSold() < suma) {
            throw new FonduriInsuficienteException();
        }

        if (ibanEmitor.substring(4, 8).equals(ibanAcceptor.substring(4, 8))) {
            ContBancar contAcceptor = gasesteCont(ibanAcceptor);
            if (contAcceptor == null) {
                throw new ContInexistentException();
            }
            contEmitor.retrage(suma);
            contAcceptor.acceptaTransferBancar(ibanEmitor, suma);
        }
        else {
            String codBancaAcceptoare = ibanAcceptor.substring(4, 8);
            Banca bancaAcceptoare = Sistem.getBanca(codBancaAcceptoare);

            contEmitor.retrage(suma);
            proceseazaTransferBancaAcceptoare(bancaAcceptoare, ibanAcceptor, ibanEmitor, suma);
        }
    }

    public void proceseazaTransferBancaAcceptoare(Banca bancaAcceptoare, String ibanAcceptor, String ibanExpeditor, double suma)
            throws BancaInexistentaException, ContInexistentException, SumaNegativaException, CardBlocatException {

        if (bancaAcceptoare == null) {
            throw new BancaInexistentaException();
        }
        if (ibanAcceptor == null) {
            throw new ContInexistentException();
        }
        if (suma <= 0.0) {
            throw new SumaNegativaException();
        }

        ContBancar contAcceptor = bancaAcceptoare.gasesteCont(ibanAcceptor);
        contAcceptor.acceptaTransferBancar(ibanExpeditor, suma);
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

    public String getEmail(){
        return this.email;
    }

    public String getCodBanca(){
        return this.codBanca;
    }

    public String getCui(){
        return this.cui;
    }

    public String getNrInregistrare(){
        return this.nrInregistrare;
    }
}