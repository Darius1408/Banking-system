package banking;

import java.util.*;

public class Exceptii{}

class BancaException extends Exception{
    public BancaException() {
        super();
    }
    public BancaException(String message){
        super(message);
    }
    public BancaException(String message, Throwable cause){
        super(message,cause);
    }
}

class SoldNegativException extends BancaException{
    public SoldNegativException(String message){
        super(message);
    }
    public SoldNegativException(){
        super();
    }
}

class FonduriInsuficienteException extends BancaException{
    public FonduriInsuficienteException(String message){
        super(message);
    }
    public FonduriInsuficienteException(){
        super();
    }
}

class PinInvalidException extends BancaException{
    public PinInvalidException(String message){
        super(message);
    }
    public PinInvalidException(){
        super();
    }
}

class CardBlocatException extends BancaException{
    public CardBlocatException(String message){
        super(message);
    }
    public CardBlocatException(){
        super();
    }
}

class ListaConturiGoalaException extends BancaException{
    public ListaConturiGoalaException(String message){
        super(message);
    }
    public ListaConturiGoalaException(){
        super();
    }
}

class FormatNrTelefonInvalidException extends BancaException{
    public FormatNrTelefonInvalidException(String message){
        super(message);
    }
    public FormatNrTelefonInvalidException(){
        super();
    }
}

class IbanDuplicatException extends BancaException{
    public IbanDuplicatException(String message){
        super(message);
    }
    public IbanDuplicatException(){
        super();
    }
}

class IbanInvalidException extends BancaException{
    public IbanInvalidException(String message){
        super(message);
    }
    public IbanInvalidException(){
        super();
    }
}

class ContInexistentException extends BancaException{
    public ContInexistentException(String message){
        super(message);
    }
    public ContInexistentException(){
        super();
    }
}

class FormatIbanInvalidException extends BancaException{
    public FormatIbanInvalidException(String message){
        super(message);
    }
    public FormatIbanInvalidException(){
        super();
    }
}

class IbanEmitorInvalidException extends BancaException{
    public IbanEmitorInvalidException(String message){
        super(message);
    }
    public IbanEmitorInvalidException(){
        super();
    }
}

class IbanAcceptorInvalidException extends BancaException{
    public IbanAcceptorInvalidException(String message){
        super(message);
    }
    public IbanAcceptorInvalidException(){
        super();
    }
}

class SoldInsuficientException extends BancaException{
    public SoldInsuficientException(String message){
        super(message);
    }
    public SoldInsuficientException(){
        super();
    }
}

class SumaNegativaException extends BancaException{
    public SumaNegativaException(String message){
        super(message);
    }
    public SumaNegativaException(){
        super();
    }
}

class CnpInvalidException extends BancaException{
    public CnpInvalidException(String message){
        super(message);
    }
    public CnpInvalidException(){
        super();
    }
}

class FormatCuiInvalidException extends BancaException{
    public FormatCuiInvalidException(String message){
        super(message);
    }
    public FormatCuiInvalidException(){
        super();
    }
}

class FormatNrInregistrareInvalidException extends BancaException{
    public FormatNrInregistrareInvalidException(String message){
        super(message);
    }
    public FormatNrInregistrareInvalidException(){
        super();
    }
}

class FormatEmailInvalidException extends BancaException{
    public FormatEmailInvalidException(String message){
        super(message);
    }
    public FormatEmailInvalidException(){
        super();
    }
}

class BancaInexistentaException extends BancaException{
    public BancaInexistentaException(String message){
        super(message);
    }
    public BancaInexistentaException(){
        super();
    }
}

class FormatCodBancaInvalidException extends BancaException{
    public FormatCodBancaInvalidException(String message){
        super(message);
    }
    public FormatCodBancaInvalidException(){
        super();
    }
}

class BancaDuplicatException extends BancaException{
    public BancaDuplicatException(String message){
        super(message);
    }
    public BancaDuplicatException(){
        super();
    }
}

class BancaInvalidaException extends BancaException{
    public BancaInvalidaException(String message){
        super(message);
    }
    public BancaInvalidaException(){
        super();
    }
}

class BancaNegasitaException extends BancaException{
    public BancaNegasitaException(String message){
        super(message);
    }
    public BancaNegasitaException(){
        super();
    }
}

class CodBancaInexistentException extends BancaException{
    public CodBancaInexistentException(String message){
        super(message);
    }
    public CodBancaInexistentException(){
        super();
    }
}

class ClientInexistentException extends BancaException{
    public ClientInexistentException(String message){
        super(message);
    }
    public ClientInexistentException(){
        super();
    }
}