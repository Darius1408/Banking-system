package banking;
import java.util.*;

public class ContBancar{
    private String iban;
    private String titular;
    private double sold;
    private boolean stare;
    private int pin;

    public ContBancar(String iban, String titular, double sold, int pin) throws SoldNegativException{
        this.iban = iban;
        this.titular = titular;
        if(sold < 0){
            throw new SoldNegativException();
        }
        this.sold = sold;
        this.stare = true;
        this.pin = pin;
    }

    public  void acceptaTransferBancar(String ibanExpeditor, double suma) throws SumaNegativaException, CardBlocatException{
        if(suma <= 0.0){
            throw new SumaNegativaException();
        }
        if(!this.stare){
            throw new CardBlocatException();
        }
        this.sold += suma;
        System.out.println("Incasat suma de " + suma + " de la iban: " + ibanExpeditor);

    }

    public void retrage(double suma) throws SoldInsuficientException, SumaNegativaException{
        if(this.sold < suma){
            throw new SoldInsuficientException();
        }
        else if(suma <= 0.0){
            throw new SumaNegativaException();
        }
        this.sold -= suma;
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
        else if(!(obj instanceof ContBancar)){
            return false;
        }
        ContBancar other = (ContBancar) obj;
        return this.iban == other.iban;
    }

    public String toString(){
        return "Informatii cont:\n" + "-titular: " + this.getTitular() + ";\n" + "-sold: " + this.getSold() + ";\n" + "-iban: " + this.getIban() + ";\n" + "-stare card: " + this.getStareCard() + ";\n";
    }
}