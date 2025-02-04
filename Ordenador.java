import java.io.RandomAccessFile;
public class Ordenador extends Dispositivo{
   int ram;
   String procesador;
   int tamDisco;
   int tipoDisco;
   int idOrdenador;
    public Ordenador(String marca, String modelo, boolean estado, int ram, String procesador, int tamDisco, int tipoDisco){
        super(marca, modelo, estado);
        this.ram = ram;
        this.procesador = procesador;
        this.tamDisco = tamDisco;
        this.tipoDisco = tipoDisco;
        int ultimoId = -1;
        try{
            RandomAccessFile raf = new RandomAccessFile("ordenadores.dat", "rw");
            if(raf.length()>= TAM_REG){
                raf.seek(raf.length()-TAM_REG);
                ultimoId = raf.readInt();
                this.idOrdenador = ultimoId + 1;
                raf.close();
            }else{
                this.idOrdenador = 1;
                raf.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Ordenador(int id){
        super(id);
        this.ram = 0;
        this.procesador = "";
        this.tamDisco = 0;
        this.tipoDisco = 0;
    }
    public int getRam(){
     return this.ram;
    }
    public void setRam(int ram){
     this.ram = ram;
    }

    public String getProcesador(){
     return this.procesador;
    }
    public void setProcesador(String procesador){
     this.procesador = procesador;
    }

    public int getTamDisco(){
     return this.tamDisco;
    }
    public void setTamDisco(int tamDisco){
     this.tamDisco = tamDisco;
    }

    public String toString(){
     return super.toString() + "\nRAM: " + this.ram + "\nProcesador: " + this.procesador + "\nTamaño del disco: " + this.tamDisco + "\nTipo de disco: " + this.tipoDisco;
    }

    public int save(){
        try{
            RandomAccessFile raf = new RandomAccessFile("ordenadores.dat", "rw");
                raf.seek(this.id*114);
                //Si existe el dispositivo, lo modificamos
                if(raf.getFilePointer() < raf.length()){
                    raf.writeInt(this.id);   
                }//Si no existe, lo creamos
                else{
                    raf.seek(raf.length());
                    raf.writeInt(this.id);
                }
                    longuitudFija(raf, this.procesador, 50);
                    raf.writeInt(this.ram);
                    raf.writeInt(this.tamDisco);
                    raf.writeInt(this.tipoDisco);
                    raf.close();
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
    public int load(){
        try{
            RandomAccessFile raf = new RandomAccessFile("ordenadores.dat", "r");
            raf.seek (this.id*114);//Nos desplazamos a la posicion del id
            raf.readInt();//Leemos el id
            this.procesador=leerString(raf);
            this.tamDisco = raf.readInt();
            this.ram = raf.readInt();
            this.tipoDisco = raf.readInt();
            this.idOrdenador = raf.readInt();
            raf.close();
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
}