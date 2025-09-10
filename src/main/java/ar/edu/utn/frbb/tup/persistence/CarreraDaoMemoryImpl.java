package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Carrera;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class  CarreraDaoMemoryImpl implements  CarreraDao {

    private static final String CSV_FILE_PATH = "D:/lab3/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/carreraDATA.csv";

    @Override
    public void guardarCarrera(Carrera carrera) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            // Abrir el archivo en modo de agregar (append = true)
            fileWriter = new FileWriter(CSV_FILE_PATH, true);
            printWriter = new PrintWriter(fileWriter);

            // Escribir los atributos del alumno en formato CSV
            printWriter.println(

                            carrera.getId() +","+
                            carrera.getNombre()


            );

            System.out.println("Carrera guardado correctamente en el archivo CSV.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
        } finally {
            // Cerrar los recursos
            if (printWriter != null) {
                printWriter.close();
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public List<Carrera> buscarCarrera() {
        List<Carrera> carreras = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");

                // Validación mínima para asegurarse de que haya al menos dos columnas
                if (datos.length < 2) {
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue; // Saltar líneas con formato incorrecto
                }

                try {
                    long id = Long.parseLong(datos[0].trim());  // Parseo del ID
                    String nombre = datos[1].trim();  // Obtención del nombre

                    // Crear una nueva instancia de Carrera y agregarla a la lista
                    Carrera carrera = new Carrera(id,nombre);
                    carreras.add(carrera);
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear números en la línea: " + linea);
                    // Continúa con la siguiente línea
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
            // Podrías lanzar una excepción personalizada aquí si es necesario
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }

        return carreras;
    }

    @Override
    public Carrera buscarCarreraporId(long id) {
        try (

            BufferedReader bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null)
            {
                 //
                try {
                    String[] datos = linea.split(",");
                    long idcarrera = Long.parseLong(datos[0].trim());
                    String nombre = String.valueOf(datos[1].trim());
                    if (idcarrera==id) {
                     Carrera carrera =  new Carrera(id,nombre);
                        return  carrera;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear número: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Carrera borrarCarreraporid(long id) {
        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("tempFile.csv");
        Carrera carreraEliminada = null;

        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
                PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile))
        ) {
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                // Ignorar líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",");
                if (datos.length < 2) { // Verificar que la línea tenga al menos dos campos
                    System.err.println("Formato incorrecto en la línea: " + linea);
                    continue;
                }

                try {
                    long Idcarrera = Long.parseLong(datos[0].trim());

                    if (Idcarrera != id) {
                        printWriter.println(linea);
                    } else {
                        // Guardar la carrera eliminada
                        carreraEliminada = new Carrera(Idcarrera, datos[1].trim());
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear número en la línea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer o escribir el archivo CSV: " + e.getMessage());
        }

        // Operaciones con archivos
        if (carreraEliminada != null) {
            if (!inputFile.delete()) {
                System.out.println("No se pudo eliminar el archivo original");
                return null;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("No se pudo renombrar el archivo temporal");
                return null;
            }
            System.out.println("Carrera eliminada exitosamente!");
            return carreraEliminada;
        } else {
            System.out.println("No existe carrera con el ID proporcionado: " + id);
            return null;
        }
    }

    @Override
    public Carrera modificarCarrera(Carrera carrera) {
        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("tempFile.csv");
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        Carrera carreramodificado = null;
        System.out.println("La carrera es: " + carrera.toString());
        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 2) {
                    // Si la línea no tiene el formato correcto, la copiamos tal cual
                    printWriter.println(linea);
                    continue;
                }

                long id= Long.parseLong(datos[0]);

                String nombre=String.valueOf(datos[1].trim());
                System.out.println("id: " + id + " carr.getId = " + carrera.getId());
                if (id == carrera.getId()) {
                    // Reemplazamos la línea con los datos actualizados del alumno
                    printWriter.println(
                                    carrera.getId() + "," +
                                    carrera.getNombre()
                    );

                    carreramodificado = carrera;
                    System.out.println(carreramodificado.toString());
                } else {
                    // Escribimos la línea existente sin modificaciones
                    printWriter.println(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo CSV: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al modificar la carrera.");
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (printWriter != null) printWriter.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar los recursos: " + e.getMessage());
            }
        }

        // Reemplazamos el archivo original con el temporal
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("No se pudo renombrar el archivo temporal.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al renombrar el archivo temporal.");
            }
        } else {
            System.err.println("No se pudo eliminar el archivo original.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el archivo original.");
        }

        if (carreramodificado != null) {
            System.out.println("carrera modificada exitosamente.");
            return carreramodificado;
        } else {
            System.out.println("No se encontró un carrera con el ID proporcionado.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "carrera no encontrado.");
        }
    }

    @Override
    public int obtenerUltimoId() {
        BufferedReader bufferedReader = null;
        int ultimoId = 0;

        try {
            bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length > 0) {
                    try {
                        int idActual = Integer.parseInt(datos[0].trim());
                        // Guardar el ID más alto encontrado
                        if (idActual > ultimoId) {
                            ultimoId = idActual;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear el ID en la línea: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }

        return ultimoId;
    }

    @Override
    public Carrera buscarCarrerasPorNombre(String nombre) {
        List<Carrera> carreras = buscarCarrera(); // Recupera todas las carreras del DAO

        for (Carrera carrera : carreras) {
            if (carrera.getNombre().equalsIgnoreCase(nombre)) { // Compara ignorando mayúsculas y minúsculas
                return carrera; // Devuelve la primera carrera que coincida
            }
        }
        return null; // Si no se encuentra ninguna carrera con el nombre dado
    }

}








