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
}
