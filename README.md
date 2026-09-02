# Módulo de Gestión de Rutas

Módulo de gestión de rutas para el sistema de planificación y administración de actividades de trekking y montañismo.

El módulo permite registrar, consultar, modificar y gestionar la información asociada a las rutas mediante un ABM (Alta, Baja y Modificación).

## 🎯 Objetivo

Proporcionar una interfaz que permita administrar de forma sencilla y validada los datos principales de las rutas utilizadas en las actividades de trekking y montaña.

## 🧩 Funcionalidades

El módulo permite:

* Crear nuevas rutas.
* Visualizar el listado de rutas registradas.
* Editar rutas existentes.
* Eliminar rutas.
* Validar los datos ingresados.
* Evitar el registro de rutas con nombres duplicados.
* Validar coordenadas geográficas.
* Seleccionar el tipo de terreno.
* Seleccionar el nivel de dificultad técnica.
* Seleccionar el nivel de dificultad física.

### Datos de una ruta

Cada ruta contempla actualmente los siguientes datos:

* **Nombre**
* **Latitud inicial**
* **Longitud inicial**
* **Altitud máxima**
* **Tipo de terreno**
* **Dificultad técnica**
* **Dificultad física**

## 🛠️ Tecnologías

* **Java 17**
* **JavaFX 17**
* **FXML**
* **CSS**
* **SQLite**
* **JDBC**
* **Maven**
* **Git**

## 📁 Estructura

El módulo se encuentra organizado de la siguiente manera:

```text
demo/
├── pom.xml
├── mvnw
├── mvnw.cmd
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
        │           └── RutaFormController.java
        │
        └── resources/
            └── com/
                └── example/
                    ├── rutas.fxml
                    ├── ruta-form.fxml
                    └── styles.css
```

> La estructura de paquetes y nombres de clases podrá ajustarse durante la etapa de integración del proyecto general.

## 🗄️ Base de datos

El módulo utiliza **SQLite** para almacenar las rutas.

El acceso a la base de datos se realiza mediante **JDBC**.

La tabla correspondiente a las rutas se crea automáticamente al iniciar la aplicación si todavía no existe.

## ✅ Validaciones

El módulo incorpora validaciones para evitar datos incorrectos o inconsistentes.

Entre ellas:

* Campos obligatorios.
* Formato válido para el nombre.
* Nombres de rutas duplicados.
* Latitud dentro del rango válido.
* Longitud dentro del rango válido.
* Valores numéricos para coordenadas y altitud.
* Valores válidos en los campos de selección.

Las validaciones y mensajes de error continuarán refinándose durante el desarrollo del proyecto.

## 🚀 Ejecución

### Requisitos

* JDK 17.
* Git.
* Una conexión a Internet para la descarga inicial de dependencias Maven.

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

```bash
./mvnw clean compile
```

Para ejecutar:

```bash
./mvnw javafx:run
```

## 🧪 Pruebas

El módulo será sometido a pruebas funcionales para verificar:

* Alta de rutas.
* Consulta y listado.
* Modificación.
* Eliminación.
* Validaciones de campos.
* Casos de entrada inválida.
* Detección de duplicados.
* Persistencia de los datos.
* Navegación entre las distintas pantallas.

Las pruebas se ampliarán durante la etapa de integración y testing del proyecto general.

## 🔄 Estado del proyecto

**En desarrollo.**

El ABM de Rutas cuenta con su funcionalidad principal implementada. Actualmente se trabaja en el refinamiento de validaciones, experiencia de usuario, estilos y posterior integración con los demás módulos del sistema.

## 👥 Contexto

Este módulo forma parte de un proyecto académico orientado al desarrollo de una aplicación de escritorio para la planificación, seguridad y logística de actividades de trekking y montañismo.
