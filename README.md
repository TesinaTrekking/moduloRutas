# Módulo de Gestión de Rutas

Módulo de gestión de rutas para el sistema de planificación y administración de actividades de trekking y montañismo.

El módulo permite registrar, consultar, modificar y gestionar la información asociada a las rutas mediante operaciones de ABM (Alta, Baja y Modificación).

## 🎯 Objetivo

Proporcionar una interfaz que permita administrar de forma sencilla y validada los datos principales de las rutas utilizadas en las actividades de trekking y montaña.

## 🧩 Funcionalidades

El módulo permite:

- Crear nuevas rutas.
- Visualizar el listado de rutas registradas.
- Editar rutas existentes.
- Eliminar rutas.
- Validar los datos ingresados.
- Evitar el registro de rutas con nombres duplicados.
- Validar coordenadas geográficas.
- Validar la altitud máxima.
- Seleccionar el tipo de terreno.
- Seleccionar el nivel de dificultad técnica.
- Seleccionar el nivel de dificultad física.

### Datos de una ruta

Cada ruta contempla actualmente los siguientes datos:

- **Nombre**
- **Latitud inicial**
- **Longitud inicial**
- **Altitud máxima**
- **Tipo de terreno**
- **Dificultad técnica**
- **Dificultad física**

## 🛠️ Tecnologías

- **Java 17**
- **JavaFX 17**
- **FXML**
- **CSS**
- **SQLite**
- **JDBC**
- **Maven**
- **Git**

## 📁 Estructura

El repositorio presenta la siguiente organización:

```text
.
├── .gitignore
├── README.md
│
└── demo/
    ├── .mvn/
    │   └── wrapper/
    │       └── maven-wrapper.properties
    │
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    │
    └── src/
        └── main/
            ├── java/
            │   ├── module-info.java
            │   └── com/
            │       └── example/
            │           ├── App.java
            │           ├── ConexionDB.java
            │           ├── Ruta.java
            │           ├── RutaController.java
            │           ├── RutaDAO.java
            │           └── RutaFormController.java
            │
            └── resources/
                └── com/
                    └── example/
                        ├── ruta-form.fxml
                        ├── rutas.fxml
                        └── styles.css
```

La carpeta `demo` contiene el proyecto Java y su configuración de Maven. El código fuente se organiza en el paquete `com.example`, mientras que los archivos FXML y CSS se encuentran en los recursos de la aplicación.

> La estructura de paquetes y los nombres de clases podrán ajustarse durante la etapa de integración con el proyecto general.

## 🗄️ Base de datos

El módulo utiliza **SQLite** para almacenar la información de las rutas.

El acceso a la base de datos se realiza mediante **JDBC**, utilizando las clases correspondientes para la conexión y las operaciones de persistencia.

## ✅ Validaciones

El módulo incorpora validaciones para evitar el ingreso de datos incorrectos o inconsistentes.

Entre ellas:

- Nombre obligatorio.
- El nombre solo admite letras, números y espacios.
- Detección de nombres de rutas duplicados.
- Latitud obligatoria y dentro del rango de -90 a 90.
- Longitud obligatoria y dentro del rango de -180 a 180.
- Altitud máxima obligatoria y mayor o igual a cero.
- Validación del formato numérico de coordenadas y altitud.
- Selección obligatoria del tipo de terreno.
- Selección obligatoria de la dificultad técnica y física.

## 🚀 Ejecución

### Requisitos

- JDK 17.
- Git.
- Una conexión a Internet para la descarga inicial de dependencias Maven.

No es necesario instalar JavaFX manualmente, ya que las dependencias se gestionan mediante Maven.

### Windows

Desde la carpeta `demo`:

```powershell
.\mvnw.cmd clean compile
```

Para ejecutar la aplicación:

```powershell
.\mvnw.cmd javafx:run
```

### Linux / macOS

Desde la carpeta `demo`:

```bash
./mvnw clean compile
```

Para ejecutar:

```bash
./mvnw javafx:run
```

## 🧪 Pruebas

El módulo contempla pruebas funcionales para verificar:

- Alta de rutas.
- Consulta y listado.
- Modificación.
- Eliminación.
- Validaciones de campos.
- Casos de entrada inválida.
- Detección de duplicados.
- Persistencia de los datos.
- Navegación entre las distintas pantallas.

## 🔄 Estado del proyecto

**En desarrollo.**

El ABM de Rutas cuenta con su funcionalidad principal implementada. Actualmente se trabaja en el refinamiento de validaciones, experiencia de usuario, estilos y posterior integración con los demás módulos del sistema.

## 👥 Contexto

Este módulo forma parte de un proyecto académico orientado al desarrollo de una aplicación de escritorio para la planificación, seguridad y logística de actividades de trekking y montañismo.
