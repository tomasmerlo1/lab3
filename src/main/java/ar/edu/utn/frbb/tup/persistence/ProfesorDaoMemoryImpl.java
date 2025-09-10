package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Profesor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public   class ProfesorDaoMemoryImpl implements ProfesorDao{

    private static final String CSV_FILE_PATH = "D:/lab3/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/profesorDATA.csv";

    public void guardarProfesor(Profesor profesor) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            // Abrir el archivo en modo de agregar (append = true)
            fileWriter = new FileWriter(CSV_FILE_PATH, true);
            printWriter = new PrintWriter(fileWriter);

            // Escribir los atributos del alumno en formato CSV
            printWriter.println(
                            profesor.getId()+ "," +
                            profesor.getNombre() + "," +
                            profesor.getApellido() + "," +
                            profesor.getTitulo() + ","

            );

            System.out.println("profesor  guardado correctamente en el archivo CSV.");
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
    public Profesor buscarProfesorporid(long id) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String linea;  // id, nombre, apellido, titulo

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");

                // Verificar que la línea tenga suficientes datos
                if (datos.length < 4) {
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue;
                }

                try {
                    long profesorId = Long.parseLong(datos[0]);
                    String profesornombre = datos[1];
                    String profesorapellido = datos[2];
                    String profesorTitulo = datos[3];

                    if (profesorId == id) {
                        return new Profesor(profesorId, profesornombre, profesorapellido, profesorTitulo);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error de formato en ID en la línea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return null;
    }

    public Profesor borrarProfesorporid(long id) {
        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("tempFile.csv");
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        Profesor profesorEliminado = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            // Leer cada línea del archivo original
            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");

                // Verificar que los datos contengan al menos 4 campos antes de parsear
                if (datos.length < 4) {
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue;
                }

                int profesorId = Integer.parseInt(datos[0]);

                if (profesorId != id) {
                    printWriter.println(linea); // Escribir líneas no coincidentes en el archivo temporal
                } else {
                    // Crear instancia de Profesor eliminado
                    profesorEliminado = new Profesor(
                            profesorId,               // ID
                            datos[1],                 // Nombre
                            datos[2],                 // Apellido
                            datos[3]                  // Título
                    );
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (printWriter != null) printWriter.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar los recursos: " + e.getMessage());
            }
        }

        // Eliminar el archivo original y renombrar el archivo temporal
        if (profesorEliminado != null) {
            if (!inputFile.delete()) {
                System.out.println("No se pudo eliminar el archivo original");
                return null;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("No se pudo renombrar el archivo temporal");
                return null;
            }
            System.out.println("Profesor eliminado exitosamente!");
            return profesorEliminado;
        } else {
            // Si el profesor no fue encontrado
            System.out.println("No existe profesor con ese id");
            return null;
        }
    }


    @Override
    public List<Profesor> buscarProfesores() {
        List<Profesor> profesores = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length < 4) { // Suponiendo que cada línea debe tener al menos 4 campos
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue; // Saltar líneas con formato incorrecto
                }

                try {
                    Long id = Long.parseLong(datos[0].trim());
                    String nombre = String.valueOf(datos[1].trim());
                    String apellido = String.valueOf(datos[2].trim());
                    String titulo = String.valueOf(datos[3].trim());

                    Profesor profesor = new Profesor(id, nombre, apellido, titulo);
                    profesores.add(profesor);
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear números en la línea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }

        return profesores;
    }




    @Override
    public Profesor modificarProfesor(Profesor profesor) {
        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("tempFile.csv");
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        Profesor profesorModificado = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 3) { // Asegúrate de que tienes al menos nombre y apellido
                    printWriter.println(linea); // Copia la línea sin modificar
                    continue;
                }
                long Id = Long.parseLong(datos[0].trim());
                // Verifica si el nombre y apellido coinciden
                if (Id== profesor.getId()) {
                    // Reemplazamos la línea con los datos actualizados del profesor
                    printWriter.println(
                            profesor.getId()+ "," +
                            profesor.getNombre() + "," +
                            profesor.getApellido()+ "," +
                            profesor.getTitulo()
                                     // Solo escribimos los datos disponibles
                    );
                    profesorModificado = profesor; // Almacenamos el profesor modificado
                } else {
                    // Escribimos la línea existente sin modificaciones
                    printWriter.println(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo CSV: " + e.getMessage());
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

        if (profesorModificado != null) {
            System.out.println("Profesor modificado exitosamente: " + profesorModificado);
            return profesorModificado;
        } else {
            System.out.println("No se encontró un profesor con el nombre y apellido proporcionados.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "profesor no encontrado.");
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
}

