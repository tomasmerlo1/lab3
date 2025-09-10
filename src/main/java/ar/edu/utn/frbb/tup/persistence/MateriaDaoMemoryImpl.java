package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Materia;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public  class MateriaDaoMemoryImpl implements MateriaDao {


    private static final String CSV_FILE_PATH = "D:/lab3/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/materiaDATA.csv";

    public void guardarMateria(Materia materia) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            // Abrir el archivo en modo de agregar (append = true)
            fileWriter = new FileWriter(CSV_FILE_PATH, true);
            printWriter = new PrintWriter(fileWriter);

            // Convertir la lista de correlatividades en un string con formato "7-4-5"
            String correlatividadesFormato = String.join("'",
                    materia.getCorrelatividades().stream()
                            .map(String::valueOf)  // Convertir cada correlatividad a String
                            .toArray(String[]::new) // Convertir el stream a un array de Strings
            );

            // Escribir los atributos de la materia en formato CSV
            printWriter.println(
                    materia.getId() + "," +
                            materia.getNombre() + "," +
                            materia.getAnio() + "," +
                            materia.getCuatrimestre() + "," +
                            materia.getIdprofesor() + "," +  // Asegurarse de que este campo sea el correcto
                            correlatividadesFormato // Guardar en formato "7-4-5"
            );
            System.out.println("Materia guardada correctamente en el archivo CSV.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
        } finally {
            // Cerrar los recursos
            try {
                if (printWriter != null) printWriter.close();
                if (fileWriter != null) fileWriter.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Materia> buscarMaterias() {
        List<Materia> materias = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                // Dividir la línea en partes
                String[] datos = linea.split(",");

                // Verificar que la línea tiene al menos 5 elementos
                if (datos.length < 5) {
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue; // Saltar esta línea si no tiene suficientes datos
                }

                try {
                    // Parsear los datos de la línea
                    Long id = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    int anio = Integer.parseInt(datos[2].trim());
                    int cuatrimestre = Integer.parseInt(datos[3].trim());
                    long idProfesor = Long.parseLong(datos[4].trim());

                    // Inicializar la lista de correlatividades
                    List<Long> correlatividades = new ArrayList<>();

                    // Verificar si hay correlatividades (más de 5 elementos en la línea)
                    if (datos.length > 5) {
                        // Verificar si el índice 5 tiene datos antes de procesarlos
                        String correlatividadesString = datos[5].trim();
                        if (!correlatividadesString.isEmpty()) {
                            // Convertir las correlatividades en una lista de Long
                            correlatividades = Arrays.stream(correlatividadesString.split("'")) // Dividir por comillas simples
                                    .map(String::trim) // Eliminar espacios adicionales
                                    .filter(c -> !c.isEmpty()) // Filtrar elementos vacíos
                                    .map(Long::parseLong) // Convertir cada elemento a Long
                                    .collect(Collectors.toList()); // Recoger en una lista de Long
                        }
                    }

                    // Crear el objeto Materia y agregarlo a la lista
                    Materia materia = new Materia(id, nombre, anio, cuatrimestre, idProfesor, correlatividades);
                    materias.add(materia);

                } catch (NumberFormatException e) {
                    // Manejar el error de parseo si alguno de los campos no se puede convertir
                    System.err.println("Error al parsear datos en la línea: " + linea);
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

        return materias;
    }

    @Override
    public List<Materia> buscarMateriasPorProfesorId(long idProfesor)
        {
            List<Materia> materias = new ArrayList<>();
            BufferedReader bufferedReader = null;

            try {
                bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
                String linea;

                while ((linea = bufferedReader.readLine()) != null) {
                    String[] datos = linea.split(",");

                    if (datos.length < 6) {
                        System.err.println("Línea con formato incorrecto: " + linea);
                        continue; // Saltar líneas con formato incorrecto
                    }

                    try {
                        Long id = Long.parseLong(datos[0].trim());
                        String nombre = String.valueOf(datos[1].trim());
                        int anio = Integer.parseInt(datos[2].trim());
                        int cuatrimestre = Integer.parseInt(datos[3].trim());
                        long idProf = Long.parseLong(datos[4].trim());

                        // Convertir las correlatividades en una lista de Long
                        List<Long> correlatividades = Arrays.stream(datos[5].trim().split("'")) // Dividir por el guion
                                .map(Long::parseLong) // Convertir cada elemento a Long
                                .collect(Collectors.toList()); // Recoger en una lista de Long

                        // Crear una nueva instancia de Materia con las correlatividades
                        Materia materia = new Materia(id,nombre, anio, cuatrimestre, idProfesor, correlatividades);
                        if(idProf == idProfesor) {
                            materias.add(materia); // Agregar la materia a la lista
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear números en la línea: " + linea);
                        // Dependiendo de tu lógica, puedes continuar o lanzar una excepción
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

            return materias;
        }
    @Override
    public Materia buscarMateriaId(long id) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 5) {
                    System.err.println("Línea con formato incorrecto: " + linea);
                    continue; // Salta la línea con formato incorrecto
                }
                try {
                    long Id = Long.parseLong(datos[0].trim());
                    String nombre = String.valueOf(datos[1].trim());
                    int anio = Integer.parseInt(datos[2].trim());
                    int cuatrimestre = Integer.parseInt(datos[3].trim());
                    long idprofesor = Long.parseLong(datos[4].trim());

                    // Convertir las correlatividades en una lista de Long
                    List<Long> correlatividades = new ArrayList<>();
                    try {
                        correlatividades = Arrays.stream(datos[5].trim().split("'")) // Dividir por el guion
                                .map(Long::parseLong) // Convertir cada elemento a Long
                                .collect(Collectors.toList()); // Recoger en una lista de Long
                    } catch (Exception e) {
                        System.err.println("Error al procesar correlatividades: " + e.getMessage());
                    }

                    if (Id == id) {
                        return new Materia(Id, nombre, anio, cuatrimestre, idprofesor, correlatividades);
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
    public Materia borrarmateriaporid(long id) {
        File inputFile = new File(CSV_FILE_PATH);
        BufferedReader bufferedReader = null;
        File tempFile = new File("tempFile.csv");
        PrintWriter printWriter = null;
        Materia materiaEliminada = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {

                if (linea.trim().isEmpty()) continue;

                // Manejar posibles problemas con los datos
                String[] datos = linea.split(",");
                if (datos.length < 5) {
                    System.err.println("Línea malformada: " + linea);
                    continue;
                }

                try {
                    long IdMateria = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    int anio = Integer.parseInt(datos[2].trim());
                    int cuatrimestre = Integer.parseInt(datos[3].trim());
                    long idprofesor = Long.parseLong(datos[4].trim());

                    List<Long> correlatividades = new ArrayList<>();
                    if (datos.length > 5) { // Validar que existe la columna de correlatividades
                        correlatividades = Arrays.stream(datos[5].trim().split("'")) // Dividir por comillas simples
                                .map(String::trim) // Eliminar espacios en blanco
                                .filter(s -> !s.isEmpty()) // Filtrar elementos vacíos
                                .map(Long::parseLong) // Convertir a Long
                                .collect(Collectors.toList());
                    }


                    if (IdMateria != id) {
                        printWriter.println(linea);
                    } else {
                        materiaEliminada = new Materia(IdMateria, nombre, anio, cuatrimestre, idprofesor, correlatividades);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear datos numéricos en la línea: " + linea);
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

        if (materiaEliminada != null) {
            if (!inputFile.delete()) {
                System.out.println("No se pudo eliminar el archivo original");
                return null;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("No se pudo renombrar el archivo temporal");
                return null;
            }
            System.out.println("Asignatura eliminada exitosamente!");
            return materiaEliminada;
        } else {
            System.out.println("No existe asignatura con el id proporcionado: " + id);
            return null;
        }
    }


    @Override
    public Materia modificarMateria(Materia materia) {
        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("tempFile.csv");
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        Materia materiaModificada = null;
        System.out.println(materia.getNombre());
        System.out.println(materia.getCorrelatividades().toString());
        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            printWriter = new PrintWriter(new FileWriter(tempFile));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 4) {  // Se espera que haya al menos 4 campos: nombre, anio, cuatrimestre, profesorId
                    printWriter.println(linea);  // Si la línea no tiene el formato correcto, la copiamos tal cual
                    continue;
                }
                long Id = Long.parseLong(datos[0].trim());


                if (Id==materia.getId()) {
                    String correlatividadesFormato = String.join("'",
                            materia.getCorrelatividades().stream()
                                    .map(String::valueOf)  // Convertir cada correlatividad a String
                                    .toArray(String[]::new) // Convertir el stream a un array de Strings
                    );

                    printWriter.println(
                            materia.getId() + "," +
                                    materia.getNombre() + "," +
                                    materia.getAnio() + "," +
                                    materia.getCuatrimestre() + "," +
                                    materia.getIdprofesor() + "," +

                                    correlatividadesFormato
                    );

                    materiaModificada = materia;
                } else {

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
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("No se pudo renombrar el archivo temporal.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al renombrar el archivo temporal.");
            }
        } else {
            System.err.println("No se pudo eliminar el archivo original.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el archivo original.");
        }

        return materiaModificada;
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
