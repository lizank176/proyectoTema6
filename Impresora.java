import java.io.RandomAccessFile;

public class Impresora extends Dispositivo{
    int tipo;
    boolean color;
    boolean scanner;
    int idImpresora;
    public Impresora(String marca, String modelo , boolean estado, int tipo, boolean color, boolean scanner){
        super(marca, modelo, estado);
        this.tipo = tipo;
        this.color = color;
        this.scanner = scanner;
        int ultimoId = -1;
        try{
            RandomAccessFile raf = new RandomAccessFile("Impresoras.dat","rw");
            if(raf.length()>TAM_REG){
                raf.seek(raf.length()-114);
                ultimoId = raf.readInt();
                this.idImpresora = ultimoId + 1; 
            }else{
                this.idImpresora = 1;  
            }
            raf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Impresora(int id){
        super(id);
        this.tipo = 0;
        this.color = false;
        this.scanner = false;
    }
    public int getTipo(){
        return this.tipo;
    }
    public void setTipo( int tipo){
        this.tipo = tipo;
    }
    public boolean getColor(){
        return this.color;
    }
    public void setColor(boolean color){
        this.color = color;
    }
    public boolean getScanner(){
        return this.scanner;
    }
    public void setScanner(boolean scanner){
        this.scanner =  scanner; 
    }
    public String toString(){
        String color ;
        String impresora;
        if(this.color==true){
            color = "sí";
        }else color = "no";
        if(this.scanner == true)  impresora = "tiene escáner";
        else impresora = "no tiene escáner";
        return super.toString() + "\nTipo: " + this.tipo + "\nColor: " + color + "\nScanner: " + impresora;
       }

    public int save(){
        try{
            RandomAccessFile raf = new RandomAccessFile("Impresoras.dat", "rw");
            raf.seek(this.id);
            if(raf.length()<raf.getFilePointer()){
                raf.write(this.id);
            }else{
                raf.seek(raf.length());
                raf.write(this.id);
            }
            raf.seek(raf.length());
            raf.writeInt(this.id);
            raf.seek(raf.getFilePointer()+4);
            raf.writeBoolean(this.color);
            raf.seek(raf.getFilePointer()+4);
            raf.writeBoolean(this.scanner);
            raf.close();
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
    public int load(){
        try{
            RandomAccessFile raf = new RandomAccessFile("Impresoras.dat", "rw");
            raf.seek(this.id);
            this.id = raf.readInt();
            raf.readInt();
            this.color = raf.readBoolean();
            this.scanner = raf.readBoolean();
            raf.close();
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }

}
