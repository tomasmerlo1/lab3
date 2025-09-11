package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Alumno;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.io.*;
import java.util.*;

@Repository
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static final String CSV_FILE_PATH = "D:/lab3/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/alumnoDATA";

    public void guardarAlumno(Alumno alumno) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            // Abrir el archivo en modo de agregar (append = true)
            fileWriter = new FileWriter(CSV_FILE_PATH, true);
            printWriter = new PrintWriter(fileWriter);

            // Escribir los atributos del alumno en formato CSV
            printWriter.println(
                            alumno.getId() + "," +
                            alumno.getNombre() + "," +
                            alumno.getApellido() + "," +
                            alumno.getDni()


            );

            System.out.println("Alumno guardado correctamente en el archivo CSV.");
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
    public Alumno buscarAlumnopordni(long dni)
    {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
            String linea;  // id, nombre, apellido, dni
            while ((linea = bufferedReader.readLine()) != null)
            {
                String[] datos = linea.split(","); // datos = [id, nombre, apellido, dni]
                int alumnoId = Integer.parseInt(datos[0]);
                int alumnoDni = Integer.parseInt(datos[3]);
                if (alumnoDni == dni) {
                    Alumno alumno = new Alumno(alumnoId, datos[1], datos[2],alumnoDni);
                    return alumno;
                }
            }

        }
        catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
        return null;

    }
@Override
    public Alumno buscarAlumnoporid(long id)
    {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
            String linea;  // id, nombre, apellido, dni
            while ((linea = bufferedReader.readLine()) != null)
            {
                String[] datos = linea.split(","); // datos = [id, nombre, apellido, dni]
                int alumnoId = Integer.parseInt(datos[0]);
                int alumnoDni = Integer.parseInt(datos[3]);
                if (alumnoId == id) {
                    Alumno alumno = new Alumno(alumnoId, datos[1], datos[2],alumnoDni);
                    return alumno;
                }
            }

        }
        catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
        return null;

    }
    @Override
    public Alumno borrarAlumnoporid(long id)
    {
        File inputFile = new File(CSV_FILE_PATH);
        BufferedReader bufferedReader = null;
        File tempFile = new File("tempFile.csv");
        PrintWriter printWriter = null;
        Alumno alumnoEliminado = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                int alumnoId = Integer.parseInt(datos[0]);
                int alumnoDni = Integer.parseInt(datos[3]);
                if (alumnoId != id) {
                    printWriter.println(linea);
                }else
                {
                    alumnoEliminado = new Alumno(
                            alumnoId,                  // ID
                            datos[1],                  // Nombre
                            datos[2],                  // Apellido
                            alumnoDni                   // DNI
                    );

                }
            }

        }catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        finally
        {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (printWriter != null) printWriter.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar los recursos: " + e.getMessage());
            }
        }

        if(alumnoEliminado != null)
        {
            if(!inputFile.delete())
            {
                System.out.println("No se pudo eliminar el archivo original");
                return null;
            }
            if(!tempFile.renameTo(inputFile))
            {
                System.out.println("No se pudo renombrar el archivo temporal");
                return null;
            }
            System.out.println("Alumno eliminado exitosamente! ");
            return alumnoEliminado;
        }
        else
        {
            System.out.println("No existe alumno con ese id");
            return null;
        }

    }

    @Override
    public Alumno modificarAlumno(Alumno alumno) {
        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("tempFile.csv");
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        Alumno alumnoModificado = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 4) {
                    // Si la línea no tiene el formato correcto, la copiamos tal cual
                    printWriter.println(linea);
                    continue;
                }

                long alumnoId = Long.parseLong(datos[0]);

                if (alumnoId == alumno.getId()) {
                    // Reemplazamos la línea con los datos actualizados del alumno
                    printWriter.println(
                            alumno.getId() + "," +
                                    alumno.getNombre() + "," +
                                    alumno.getApellido() + "," +
                                    alumno.getDni()
                    );
                    alumnoModificado = alumno;
                } else {
                    // Escribimos la línea existente sin modificaciones
                    printWriter.println(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo CSV: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al modificar el alumno.");
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

        if (alumnoModificado != null) {
            System.out.println("Alumno modificado exitosamente.");
            return alumnoModificado;
        } else {
            System.out.println("No se encontró un alumno con el ID proporcionado.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado.");
        }
    }
    @Override
    public List<Alumno> buscarAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length < 4) {
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue; // Saltar líneas con formato incorrecto
                }

                try {
                    long id = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    String apellido = datos[2].trim();
                    int dni = Integer.parseInt(datos[3].trim());

                    Alumno alumno = new Alumno(id, nombre, apellido, dni);
                    alumnos.add(alumno);
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear números en la línea: " + linea);
                    // Puedes decidir si continuar o lanzar una excepción
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
            // Dependiendo de tu lógica, podrías lanzar una excepción aquí
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }

        return alumnos;
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

