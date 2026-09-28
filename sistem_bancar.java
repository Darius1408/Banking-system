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
class BancaInexistentaException extends Exception{};
class FormatCodBancaInvalidException extends Exception{};
class BancaDuplicatException extends Exception{};
class BancaInvalidaException extends Exception{};
class BancaNegasitaException extends Exception{};
class CodBancaInexistentException extends Exception{};
class ClientInexistentException extends Exception{};


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

    public void retrage(double suma) throws SoldInsuficient, SumaNegativaException{
        if(this.sold < suma){
            throw new SoldInsuficient();
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

        else if(!iban.startsWith("RO") || !iban.substring(4,       8).equals(this.banca.getCodBanca())){
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

    public void initiazaTransferBancar(String ibanEmitor, String ibanAcceptor, double suma)
        throws IbanEmitorInvalid, IbanAcceptorInvalid, SoldInsuficient, SumaNegativaException,
        FormatIbanInvalid, ContInexistent, FonduriInsuficienteException,
        BancaNegasitaException, CardBlocatException, BancaInexistentaException {

        if (!this.conturi.containsKey(ibanEmitor)) {
            throw new IbanEmitorInvalid();
        }
        else if (ibanAcceptor == null || !ibanAcceptor.startsWith("RO") || ibanAcceptor.length() != 24
                || !Sistem.existaBanca(ibanAcceptor.substring(4, 8))) {
            throw new IbanAcceptorInvalid();
        }

        ContBancar contEmitor = this.conturi.get(ibanEmitor);
        if (suma > contEmitor.getSold()) {
            throw new SoldInsuficient();
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
        else if(!(obj instanceof PersoanaFizica)){
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
        else if(!(obj instanceof PersoanaJuridica)){
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
    private String codBanca;
    private String cui;
    private String nrInregistrare;
    private HashMap <String, Client> clienti;

    public Banca(String nume, String adresa, String nrTelefon, String email, String cui, String nrInregistrare, String codBanca) throws FormatNrTelefonInvalid, FormatEmailInvalidException, FormatCuiInvalidException, FormatNrInregistrareInvalidException, FormatCodBancaInvalidException{
        this.nume = nume;
        this.adresa = adresa;
        if(!nrTelefon.matches("^0[0-9]{9}") || nrTelefon == null){
            throw new FormatNrTelefonInvalid();
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

    private ContBancar gasesteCont(String iban) throws ContInexistent{
        for(Client ct: this.clienti.values()){
            ContBancar cont = ct.getCont(iban);
            if(cont != null){
                return cont;
            }
        }
        throw new ContInexistent();
    }

    public void proceseazaTransfer(String ibanEmitor, String ibanAcceptor, double suma)
        throws FormatIbanInvalid, SumaNegativaException, ContInexistent,
               FonduriInsuficienteException, BancaNegasitaException,
               CardBlocatException, BancaInexistentaException, SoldInsuficient {

        if (ibanEmitor == null || ibanEmitor.length() != 24 || !ibanEmitor.startsWith("RO") || !ibanEmitor.substring(4, 8).matches("^[A-Z]{4}$")) {
            throw new FormatIbanInvalid();
        }
        else if (ibanAcceptor == null || ibanAcceptor.length() != 24 || !ibanAcceptor.startsWith("RO") || !ibanAcceptor.substring(4, 8).matches("^[A-Z]{4}$")) {
            throw new FormatIbanInvalid();
        }
        else if (suma <= 0.0) {
            throw new SumaNegativaException();
        }

        ContBancar contEmitor = gasesteCont(ibanEmitor);
        if (contEmitor == null) {
            throw new ContInexistent();
        }
        if (contEmitor.getSold() < suma) {
            throw new FonduriInsuficienteException();
        }

        if (ibanEmitor.substring(4, 8).equals(ibanAcceptor.substring(4, 8))) {
            ContBancar contAcceptor = gasesteCont(ibanAcceptor);
            if (contAcceptor == null) {
                throw new ContInexistent();
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
        throws BancaInexistentaException, ContInexistent, SumaNegativaException, CardBlocatException {

        if (bancaAcceptoare == null) {
            throw new BancaInexistentaException();
        }
        if (ibanAcceptor == null) {
            throw new ContInexistent();
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


class Sistem{
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


public class sistem_bancar {
    public static void main(String[] argv) {
        try {
            // 1. Inițializăm sistemul central
            Sistem sistem = new Sistem();

            // 2. Creăm două bănci
            // Regex cerut: tel: 10 cifre plecand cu 0, cui: RO+cifre, nrInreg: J12/123/2020, cod: 4 litere mari
            Banca bt = new Banca("Banca Transilvania", "Cluj-Napoca", "0712345678",
                                 "contact@bancatransilvania.com", "RO123456", "J12/123/2020", "BTRL");
            Banca bcr = new Banca("Banca Comerciala Romana", "Bucuresti", "0787654321",
                                  "contact@bcr.com", "RO654321", "J40/456/2019", "RNCB");

            // 3. Înrolăm băncile în sistem
            sistem.adaugaBanca(bt);
            sistem.adaugaBanca(bcr);

            System.out.println("\n--- Banci inrolate in sistem ---");
            sistem.printBanci();

            // 4. Creăm clienți
            // Persoana Fizica (CNP de 13 cifre)
            PersoanaFizica client1 = new PersoanaFizica("Ion Popescu", "Cluj-Napoca", "0722111222", bt, "1900101123456");
            PersoanaFizica client2 = new PersoanaFizica("Maria Ionescu", "Cluj-Napoca", "0733222333", bt, "2920202123456");

            // Persoana Juridica (CUI si Nr. Inregistrare)
            PersoanaJuridica client3 = new PersoanaJuridica("Tech SRL", "Bucuresti", "0744333444", bcr, "RO987654", "J40/789/2021");

            // 5. Înrolăm clienții în bănci
            bt.adaugaClient(client1);
            bt.adaugaClient(client2);
            bcr.adaugaClient(client3);

            // 6. Deschidem conturi bancare (IBAN-ul trebuie să aibă 24 caractere, să înceapă cu RO și codul băncii la index 4-8)
            String ibanIon   = "RO49BTRL1234567890123456"; // 24 caractere
            String ibanMaria = "RO49BTRL9876543210987654"; // 24 caractere
            String ibanTech  = "RO49RNCB1122334455667788"; // 24 caractere

            ContBancar contIon   = client1.creeazaContBancar(ibanIon, 1500.0, 1234);
            ContBancar contMaria = client2.creeazaContBancar(ibanMaria, 300.0, 5678);
            ContBancar contTech  = client3.creeazaContBancar(ibanTech, 5000.0, 9999);

            System.out.println("--- Solduri initiale ---");
            System.out.println("Ion (BT): " + contIon.getSold() + " RON");
            System.out.println("Maria (BT): " + contMaria.getSold() + " RON");
            System.out.println("Tech SRL (BCR): " + contTech.getSold() + " RON\n");

            // 7. Test transfer intern: Ion trimite 200 RON către Maria (ambii la BT)
            System.out.println(">>> Initiere transfer intern (BT -> BT): Ion trimite 200 RON Mariei");
            client1.initiazaTransferBancar(ibanIon, ibanMaria, 200.0);

            System.out.println("Sold Ion dupa transfer: " + contIon.getSold() + " RON");
            System.out.println("Sold Maria dupa transfer: " + contMaria.getSold() + " RON\n");

            // 8. Test transfer interbancar: Ion (BT) trimite 500 RON către Tech SRL (BCR)
            System.out.println(">>> Initiere transfer interbancar (BT -> BCR): Ion trimite 500 RON catre Tech SRL");
            client1.initiazaTransferBancar(ibanIon, ibanTech, 500.0);

            System.out.println("Sold Ion dupa transfer interbancar: " + contIon.getSold() + " RON");
            System.out.println("Sold Tech SRL dupa transfer interbancar: " + contTech.getSold() + " RON\n");

            // 9. Test cazuri de eroare (Sold insuficient)
            System.out.println(">>> Testare exceptie: Ion incearca sa trimita mai mult decat are (2000 RON)");
            client1.initiazaTransferBancar(ibanIon, ibanMaria, 2000.0);

        } catch (SoldInsuficient e) {
            System.out.println("Eroare prinsa cu succes: Sold insuficient pentru tranzactie!");
        } catch (Exception e) {
            System.out.println("A aparut o exceptie neasteptata: " + e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}
