import java.io.RandomAccessFile;
public class Dispositivo  {
    String marca, modelo;//50 bytes cada uno 
    int id  ;//4 bytes
    boolean estado;//1 byte
    boolean borrado;//1 byte
    int tipo;//4 bytes
    int idAjeno; //4 bytes
    long TAM_REG = 114;
    public Dispositivo(String marca, String modelo, boolean estado){
        this.marca = marca;
        this.modelo = modelo;
        int ultimoId;
        try{
            RandomAccessFile raf = new RandomAccessFile("dispositivos.dat", "rw");
            if(raf.length()>= TAM_REG){
                raf.seek(raf.length()-TAM_REG);
                ultimoId = raf.readInt();
                this.id =ultimoId + 1;
            }else{
                this.id = 1;
            }
            this.estado = estado;
            this.borrado = false; //no está borrado por defecto
            this.tipo = 0; // Valor predeterminado
            this.idAjeno = -1; // No tiene id ajeno por defecto
            raf.close();
        }catch(Exception e){
            e.printStackTrace();
            this.id = 0;
        } 
    }
    public Dispositivo(int id){
        this.id = id;
        this.marca = "";
        this.modelo = "";
        this.estado = false; // Valor predeterminado
        this.borrado = false; //no está borrado por defecto
        this.tipo = 0;
        this.idAjeno = -1; // No tiene id ajeno por defecto

    }
    
    public String getMarca(){
        return this.marca;
    }
    public void setMarca(String marca){
        this.marca = marca;
    }
    public String getModelo(){
        return this.modelo;
    }
    public void setModelo(String modelo){
        this.modelo = modelo;
    }
    public int getId(){
        return this.id;
    }
    public boolean getEstado(){
        return this.estado;
    }
    public void setEstado(boolean estado){
        this.estado = estado;
    }
    public void setBorrado(boolean borrado){
        this.borrado = borrado;
    }
    public boolean getBorrado(){
        return this.borrado;
    }
    public int getTipo(){
        return this.tipo;
    }
    public void setTipo(int tipo){
        this.tipo = tipo;
    }
    public int getIdAjeno(){
        return this.idAjeno;
    }
    public void setIdAjeno(int idAjeno){
        this.idAjeno = idAjeno;
    }
    public String toString(){
        String estado_cadena;
        String borrar;
        String mensaje;
        if (this.estado == true ) estado_cadena = "funciona";
        else estado_cadena = "no funciona";
        if(this.borrado == true){ 
            borrar = "El dispositivo ha sido borrado "; 
            mensaje ="ID: " + this.id + "\nMarca: " + this.marca + "\nModelo: " + this.modelo + "\nEstado: " + estado_cadena+"\n"+borrar;
        }
        else{
            mensaje = "ID: " + this.id + "\nMarca: " + this.marca + "\nModelo: " + this.modelo + "\nEstado: " + estado_cadena;
        } 
        return  mensaje;
    }
    public int save(){
        try{
            RandomAccessFile raf = new RandomAccessFile("dispositivos.dat", "rw");
                raf.seek(this.id*114);
                System.out.println(raf.getFilePointer());
                //Si existe el dispositivo, lo modificamos
                if(raf.getFilePointer() < raf.length()){
                    raf.writeInt(this.id); // No hacemos ningún salto aquí porque el dispositivo ya existe 
                }//Si no existe, lo creamos
                else{
                    raf.seek(raf.length()); // Hacemos un salto aquí porque necesitamos añadir un  dispositivo nuevo
                    raf.writeInt(this.id);
                }
                longuitudFija(raf, this.marca, 50); // Utilizamos el método longuitudFija para que la cadena ocupe un tamaño fijo
                longuitudFija(raf, this.modelo, 50);
                raf.writeBoolean(this.estado);
                raf.writeBoolean(this.borrado);
                raf.writeInt(this.tipo);
                raf.writeInt(this.idAjeno);
                raf.close(); 
                return 0;
        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
    public  void longuitudFija(RandomAccessFile raf , String nombre, int longitud){
        try{
            long posIni;
            long posFin;
            posIni = raf.getFilePointer();   // Posición ANTES de escribir el nombre en el fichero
            raf.writeUTF(nombre);
            posFin = raf.getFilePointer(); // Posición DESPUÉS de escribir el nombre en el fichero
            long bytesEscritos = posFin - posIni; // La variable bytesEscritos sirve para almacenar la longitud original de la cadena
            for (int j = 0; j < longitud-bytesEscritos; j++) {
                raf.writeByte(0);   // Si queda algo de espacio pues lo rellenamos con ceros                                              
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String leerString (RandomAccessFile raf) throws Exception{
        long pos = raf.getFilePointer();
        String nombre = raf.readUTF();
        raf.seek(pos + 50);
        return nombre;
    }
    public int load(){
        try{
            RandomAccessFile raf = new RandomAccessFile("dispositivos.dat", "r");
            raf.seek(this.id*114);//Nos desplazamos a la posicion del id
            raf.readInt();//Leemos el id
            this.marca=leerString(raf); //Utilizamos el método leerString para que sólo lea la cadena de texto y omita los ceros
            this.modelo=leerString(raf);
            this.estado = raf.readBoolean();//Leemos el estado
            this.borrado = raf.readBoolean();//Leemos el borrado
            this.tipo = raf.readInt();//Leemos el tipo
            this.idAjeno = raf.readInt();//Leemos el idAjeno
            raf.close();
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
    
    public int delete(){
        try{
            RandomAccessFile raf = new RandomAccessFile("dispositivos.dat", "rw");
            raf.seek(this.id*114);
            raf.readInt();
            raf.getFilePointer(); 
            this.marca="Borrado";
            longuitudFija(raf, this.marca, 50);
            this.modelo="Borrado";
            longuitudFija(raf, this.marca, 50);
            raf.seek(raf.getFilePointer()+50);
            raf.writeBoolean(true);
            raf.close();
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("El dispositivo no existe ");
            return 1;
        }
    }
}