import java.util.ArrayList;
import java.util.Scanner;
import java.io.RandomAccessFile;
public class Main {
  static ArrayList<Dispositivo> listaDispositivos = new ArrayList<>();
  static int opcion;
  public static void main(String[] args) {
    Dispositivo d = new Dispositivo("Apple","11",true);
    d.save();
    Ordenador ordenador = new Ordenador("Asus", "Tuf-15",true, 16, "i-7",23044,2);
    ordenador.save();
    Impresora impresora = new Impresora("HP", "23", true, 2, true, true);
    impresora.save();    
    }
  public static void menuPrincipal(){
    System.out.println("1. Agregar dispositivo\n2.Mostrar Dispositivo\n3.Buscar dispositivo\n4.Borrar dispositivo\n5.Cambiar estado dispositivo\n6.Modificar dispositivo\n0.Salir");
    Scanner sc = new Scanner(System.in);
    opcion = sc.nextInt();
  } 
  public static void cargarDatos(){
      try{
        RandomAccessFile raf = new RandomAccessFile("dispositivos.dat", "r");
        listaDispositivos.clear(); // he utilizado el método clear para  que la lista no contenga datos duplicados cada vez que se cargan los dispositivos desde el archivo. 
        while(raf.getFilePointer() < raf.length()) {
          int tipo = raf.readInt();
          String marca = raf.readUTF();
          String modelo = raf.readUTF();
          boolean estado = raf.readBoolean();
          Dispositivo dispositivo =(tipo == 1) ?
          new Ordenador(tipo ) :
          new Impresora(tipo);
          listaDispositivos.add(dispositivo);
        }
        raf.close();
        
      }catch(Exception e){
        System.out.println("Error al cargar datos: " + e.getMessage());
        e.printStackTrace();
      }
  }
  public static void anadirDispositivo(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Ingrese el nombre del fabricante: ");
    String marca = sc.nextLine();
    System.out.println("Ingrese el modelo del dispositivo: ");
    String modelo = sc.nextLine();
    System.out.println("Ingrese el estado del dispositivo: ");
    boolean estado = sc.nextBoolean();
    System.out.println("Ingrese el tipo de dispositivo\n1.Ordenador\n2.Impresora\3.Otro");
    int tipo = sc.nextInt();
    switch(tipo){
      case 1:
        System.out.println("Ingrese el procesador: ");
        String procesador = sc.nextLine();
        System.out.println("Ingrese la ram(Ej.: 16, 32) : ");
        int ram = sc.nextInt();
        System.out.println("Ingrese el tipo del disco(0 = mecánico, 1 = SSD, 2 = NVMe, 3 = otros): ");
        int tipoDisco = sc.nextInt();
        System.out.println("Ingrese el tamaño del disco en GB ");
        int tamanoDisco = sc.nextInt();
        listaDispositivos.add(new Ordenador(marca, modelo, estado, ram, procesador, tamanoDisco, tipoDisco ));
        System.out.println("Dispositivo agregado exitosamente.");
        break;
      case 2:
        System.out.println("Ingrese el tipo de impresora: 0 = laser, 1 = inyección de tinta, 2 = otro");
        int tipoImpresora = sc.nextInt();
        System.out.println("¿Tiene impresión a color? (true/false): ");
        boolean color = sc.nextBoolean();
        System.out.println("¿Tiene Scanner?(true/false):");
        boolean scanner = sc.nextBoolean();
        listaDispositivos.add(new Impresora(marca, modelo, estado, tipoImpresora, color, scanner));
        System.out.println("Dispositivo agregado exitosamente.");
        break;
      case 3:
        listaDispositivos.add(new Dispositivo(marca,modelo, estado));
        System.out.println("Dispositivo agregado exitosamente.");
        break;
      default: 
        System.out.println("Opción no válida");
    } 
  }
  public static void mostrarDispositivos(){
    System.out.printf("%-10s %-15s %-20s\n", "ID","Marca","Modelo","Estado");
    System.out.println("------------------------------------------------------------------");
    for (Dispositivo dispositivo : listaDispositivos) {
      System.out.printf("%-10d %-15s %-20s %-20s\n", 
        dispositivo.getId(),
        dispositivo.getMarca(),
        dispositivo.getModelo(),
        dispositivo.getEstado()? "Funciona": "No funciona");
    }
  }
  public static void buscarDispositivos(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Ingrese el id del dispositivo que quieres localizar: ");
    int id = sc.nextInt();
    boolean encontrado = false;
    for(Dispositivo dispositivo : listaDispositivos){
      if(dispositivo.getId()== id){
        encontrado = true;
      }
    }if(!encontrado)System.out.println("Dispositivo no encontrado");
  }
  public static void borrarDispositivo(){
    try{
      Scanner sc = new Scanner(System.in);
      RandomAccessFile raf = new RandomAccessFile("dispositivo.dat", "rw");
      System.out.println("Ingrese el id del dispositivo que quieres eliminar: ");
      int id = sc.nextInt();
      boolean encontrado = false;
      for(int i = 0; i < listaDispositivos.size(); i++){
        if(listaDispositivos.get(i).getId() == id){
          listaDispositivos.get(i).delete();
          listaDispositivos.remove(i);
          encontrado = true;
          System.out.println("Dispositivo eliminado exitosamente");
        }
      }
      if(!encontrado)System.out.println("Dispositivo no encontrado");
      raf.close();
    }catch(Exception e){
      System.out.println("Error al borrar dispositivo");
    }
    
  }
  public static void cambiarEstadoDispositivo(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Ingrese el id del dispositivo que quieres cambiar el estado: ");
    int id = sc.nextInt();
    try {
      RandomAccessFile raf = new RandomAccessFile("dispositivo.dat", "rw");
        boolean encontrado = false;
      for(Dispositivo dispositivo : listaDispositivos){
        if(dispositivo.getId() == id){
          dispositivo.setEstado(false);
          encontrado = true;
          raf.seek(id*114);
          raf.writeBoolean(dispositivo.getEstado());
          System.out.println("Estado del dispositivo cambiado exitosamente");
        }
        if(encontrado = false){
          System.out.println("Dispositivo no encontrado");
        }
      }raf.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void modificarDispositivo(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Ingrese el id del dispositivo que quieres modificar: ");
    int id = sc.nextInt();
    try {
      RandomAccessFile raf = new RandomAccessFile("dispositivo.dat", "rw");
      boolean encontrado = false;
      for(Dispositivo dispositivo : listaDispositivos){
        if(dispositivo.getId() == id){
          encontrado = true;
          dispositivo.toString();
          System.out.println("Ingrese el nuevo nombre del dispositivo: ");
          String nombre = sc.next();
          System.out.println("Ingrese el nuevo modelo del dispositivo: ");
          String modelo = sc.next();
          System.out.println("Ingrese el nuevo estado del dispositivo (funciona o no funciona): ");
          String estado = sc.nextLine();
          dispositivo.setMarca(nombre);
          dispositivo.setModelo(modelo);
          if (estado == "funciona")dispositivo.setEstado(true);
          else dispositivo.setEstado(false);
          raf.seek(id*114);
          raf.writeUTF(dispositivo.getMarca());
          raf.writeUTF(dispositivo.getModelo());
          raf.writeBoolean(dispositivo.getEstado());
          System.out.println("Dispositivo modificado exitosamente");
        }
      } 
      if(encontrado=false){
        System.out.println("Dispositivo no encontrado");
      }raf.close();
 
    }catch(Exception e){
      System.out.println("Error al modificar dispositivo");
    }
  }
}