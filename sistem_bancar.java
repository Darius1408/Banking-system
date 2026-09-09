import java.util. *;

class SoldNegativ extends Exception{};
class FonduriInsuficienteException extends Exception{};
class PinInvalidException extends Exception{};
class CardBlocatException extends Exception{};
class ListaConturiGoalaException extends Exception{};
class FormatNrTelefonInvalid extends Exception{};
class IbanDuplicat extends Exception{};
class IbanInvalid extends Exception{};
class ContInexistent extends Exception{};
class FormatIbanInvalid extends Exception{};
class IbanEmitorInvalid extends Exception{};
class IbanAcceptorInvalid extends Exception{};
class SoldInsuficient extends Exception{};
class SumaNegativaException extends Exception{};
class CnpInvalidException extends Exception{};
class FormatCuiInvalidException extends Exception{};
class FormatNrInregistrareInvalidException extends Exception{};
class FormatEmailInvalidException extends Exception{};


class ContBancar{
    private String iban;
    private String titular;
    private double sold;
    private boolean stare;
    private int pin;

    public ContBancar(String iban, String titular, double sold, int pin) throws SoldNegativ{
        this.iban = iban;
        this.titular = titular;
        if(sold < 0){
            throw new SoldNegativ();
        }
        this.sold = sold;
        this.stare = true;
        this.pin = pin;
    }

    public  double acceptaTransferBancar(String ibanExpeditor, double suma) throws SumaNegativaException, CardBlocatException{
        if(suma <= 0.0){
            throw new SumaNegativaException();
        }
        if(!this.stare){
            throw new CardBlocatException();
        }
        this.sold += suma;
        System.out.println("Incasat suma de " + suma + " de la iban: " + ibanExpeditor);

        return this.sold;
    }

    public String getIban(){
        return this.iban;
    }

    public String getTitular(){
        return this.titular;
    }

    public double getSold(){
        return this.sold;
    }

    public boolean getStareCard(){
        return this.stare;
    }

    public int getPin(){
        return this.pin;
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(!(obj instanceof Client)){
            return false;
        }
        ContBancar other = (ContBancar) obj;
        return this.iban == other.iban;
    }

    public String toString(){
        return "Informatii cont:\n" + "-titular: " + this.getTitular() + ";\n" + "-sold: " + this.getSold() + ";\n" + "-iban: " + this.getIban() + ";\n" + "-stare card: " + this.getStareCard() + ";\n";
    }
}


abstract class Client{
    private String nume;
    private String adresa;
    private String nrTelefon;
    private HashMap <String, ContBancar> conturi;
    private Banca banca;

    public Client(String nume, String adresa, String nrTelefon, Banca banca) throws FormatNrTelefonInvalid{
        this.nume = nume;
        this.adresa = adresa;
        if(nrTelefon != null &&(nrTelefon.length() != 10 || nrTelefon.substring(0, 1).equals("0") == false)){
            throw new FormatNrTelefonInvalid();
        }
        this.nrTelefon = nrTelefon;
        this.conturi = new HashMap<>();
        this.banca = banca;
    }

    public ContBancar creeazaContBancar(String iban, double sold, int pin) throws SoldNegativ, FormatIbanInvalid, IbanDuplicat{
        if(sold < 0.00) throw new SoldNegativ();

        else if(iban == null || iban.length() != 24){
            throw new FormatIbanInvalid();
        }

        else if(iban.substring(0, 2) != "RO" || iban.substring(4,       8).equals(this.banca.getCodBanca())){
            throw new FormatIbanInvalid();
        }

        else if(this.conturi.containsKey(iban)){
            throw new IbanDuplicat();
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

    public void afiseazaInformatiiCont(String iban) throws ContInexistent{
        ContBancar contCautat = this.conturi.get(iban);
        if(contCautat != null){
            System.out.println("Cont gasit:\n" + contCautat.toString());
        }
        else{
            throw new ContInexistent();
        }
    }

    public void initiazaTransferBancar(String ibanEmitor, String ibanAcceptor, double suma) throws IbanEmitorInvalid, IbanAcceptorInvalid, SoldInsuficient, SumaNegativaException{
        if(this.conturi.containsKey(ibanEmitor) == false){
            throw new IbanEmitorInvalid();
        }

        else if(ibanAcceptor == null || ibanAcceptor.startsWith("RO") == false || ibanAcceptor.length() != 24 || this.banca.coduriBanci.containsKey(ibanAcceptor.substring(4, 8)) == false){
            throw new IbanAcceptorInvalid();
        }

        ContBancar contEmitor = this.conturi.get(ibanEmitor);
        if(suma > contEmitor.getSold()){
            throw new SoldInsuficient();
        }

        else if(suma < 0.0){
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

    public String toString(){
        String res = "Informatii client:\n";
        res += "-nume: " + this.getNume() + "\n";
        res += "-adresa: " + this.getAdresa() + "\n";
        res += "-numar telefon: " + this.getNrTelefon() + "\n";
        res += "-nume banca: " + this.banca.getNumeBanca() + "\n";
        res += "-" + this.getConturiBancare() + "\n";
        return res;
    }
}


class PersoanaFizica extends Client{
    private String cnp;

    public PersoanaFizica(String nume, String adresa, String nrTelefon, Banca banca, String cnp) throws CnpInvalidException, FormatNrTelefonInvalid{
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
        else if(!(obj instanceof Client)){
            return false;
        }
        PersoanaFizica other = (PersoanaFizica) obj;
        return this.getCnp().equals(other.getCnp());
    }
}


class PersoanaJuridica extends Client{
    private String cui;
    private String nrInregistrare;

    public PersoanaJuridica(String nume, String adresa, String nrTelefon, Banca banca, String cui, String nrInregistrare) throws FormatCuiInvalidException, FormatNrInregistrareInvalidException, FormatNrTelefonInvalid{
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
        else if(!(obj instanceof Client)){
            return false;
        }
        PersoanaJuridica other = (PersoanaJuridica) obj;
        return this.getCui().equals(other.getCui());
    }
}


class Banca{
    private String nume;
    private String adresa;
    private String nrTelefon;
    private String email;
    private String cui;
    private String nrInregistrare;
    private HashMap <String, Client> clienti;

    public Banca(String nume, String adresa, String nrTelefon, String email, String cui, String nrInregistrare) throws FormatNrTelefonInvalid, FormatEmailInvalidException, FormatCuiInvalidException, FormatNrInregistrareInvalidException{
        this.nume = nume;
        this.adresa = adresa;
        if(!nrTelefon.matches("^0[0-9]{9}") || nrTelefon == null){
            throw new FormatNrTelefonInvalid();
        }
        this.nrTelefon = nrTelefon;
        if(email == null || !email.matches("^[0-9a-zA-Z]{1,50}@[0-9a-zA-Z]{1-50}(.com)$")){
            throw new FormatEmailInvalidException();
        }
        this.email = email;
        if(cui == null || !cui.matches("^(RO)?[0-9]{2,10}$")){
            throw new FormatCuiInvalidException();
        }
        this.cui = cui;
        if(nrInregistrare == null || nrInregistrare.matches("^[JFC][0-9]{2}/[0-9]{1,6}/[0-9]{4}$")){
            throw new FormatNrInregistrareInvalidException();
        }
        this.nrInregistrare = nrInregistrare;

        this.clienti = new HashMap<>();
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(!(obj instanceof Client)){
            return false;
        }
        Banca other = (Banca) obj;
        return this.nume.equals(other.nume) && this.cui.equals(other.cui);
    }

    public double proceseazaTransfer(String ibanEmitor, String ibanAcceptor, double suma){

    }
}
