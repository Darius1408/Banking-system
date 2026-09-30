package banking;
import java.util.*;
import java.sql.*;

public class Main {
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

        } catch (SoldInsuficientException e) {
            System.out.println("Eroare prinsa cu succes: Sold insuficient pentru tranzactie!");
        } catch (Exception e) {
            System.out.println("A aparut o exceptie neasteptata: " + e.getClass().getSimpleName());
            e.printStackTrace();
        }

        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("Sistemul bancar este legat corect la baza de date!");
        }

        DatabaseConnection.closeConnection();
    }
}
