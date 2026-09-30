package banking;
import java.util.*;

public class Sistem{
    private static HashMap <String, Banca> coduriBanci = new HashMap<> ();
    private ArrayList <String> banciInrolate;

    public Sistem(){
        this.banciInrolate = new ArrayList<>();
    }

    public void adaugaBanca(Banca bank) throws BancaDuplicatException, BancaInvalidaException{
        if(bank == null) throw new BancaInvalidaException();
        if(this.coduriBanci.containsKey(bank.getCodBanca())){
            throw new BancaDuplicatException();
        }
        this.banciInrolate.add(bank.getNume());
        this.coduriBanci.put(bank.getCodBanca(), bank);
        System.out.println("Banca inrolata cu succes!");
    }

    public static Banca getBanca(String codBanca) throws BancaNegasitaException{
        if(coduriBanci.containsKey(codBanca)){
            return coduriBanci.get(codBanca);
        }
        throw new BancaNegasitaException();
    }

    public void stergeBanca(String codBanca) throws CodBancaInexistentException{
        if(codBanca == null){
            throw new CodBancaInexistentException();
        }
        Banca bancaStearsa = coduriBanci.get(codBanca);
        if(bancaStearsa != null){
            this.coduriBanci.remove(codBanca);
            this.banciInrolate.remove(bancaStearsa.getNume());
        }
    }

    public void printBanci(){
        for(Banca b: this.coduriBanci.values()){
            System.out.println("Banca: " + b.getNume() + " (Cod: " + b.getCodBanca() + ")\n");
        }
    }

    public static boolean existaBanca(String codBanca) {
        if (codBanca == null) {
            return false;
        }
        return coduriBanci.containsKey(codBanca);
    }
}