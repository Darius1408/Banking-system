import java.util. *;

class SoldNegativ extends Exception{};
class FonduriInsuficienteException extends Exception{};
class PinInvalidException extends Exception{};
class CardBlocatExceptipon extends Exception{};
class ListaConturiGoalaException extends Exception{};
class FormatNrTelefonInvalid extends Exception{};
class IbanDuplicat extends Exception{};


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

    public Client(String nume, String adresa, String nrTelefon) throws FormatNrTelefonInvalid{
        this.nume = nume;
        this.adresa = adresa;
        if(nrTelefon != null &&(nrTelefon.length() != 10 || nrTelefon.substring(0, 1).equals("0") == false)){
            throw new FormatNrTelefonInvalid();
        }
        this.nrTelefon = nrTelefon;
        this.conturi = new HashMap<>();
    }

    public boolean creeazaContBancar(String iban, double sold, int pin) throws IbanDuplicat{
        if(verificaDuplicatIban(iban) == true){
            throw new IbanDuplicat();
            return false;
        }
        ContBancar contNou = new ContBancar(iban, this.nume, sold, pin);
        conturi.put(contNou.getIban(), contNou);
        return true;
    }

    public boolean stergeContBancar(String iban){
        if(conturi.remove(iban) != null){
            System.out.println("Contul cu ibanul: " + iban + " a fost sters cu succes");
            return true;
        }
        return false;
    }
}
