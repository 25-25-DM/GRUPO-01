# GRUPO-01

# Proyecto de Aplicación Móvil

## Presentación del Grupo

¡Bienvenidos al repositorio de nuestro proyecto! Somos **Grupo Uno** y hemos desarrollado una aplicación móvil que permite gestionar vehículos. El objetivo principal es crear una plataforma que permita a los usuarios autenticarse, registrar nuevos usuarios y gestionar vehículos (insertar, editar y eliminar).

## Miembros del Grupo

Los integrantes del grupo son:

1. **Joel Joshua Luna Grijalva** – Desarrollador Backend  
2. **Jordy Pedro Chamba Carrion** – Diseñador UX/UI  
3. **Freddy Xavier Tapia Rea** – Desarrollador Frontend  
4. **Kevin Fernando Pozo Maldonado** – Líder de Proyecto  
5. **Bryan Eduardo Loya Cadena** – Tester / QA  
6. **Rensso Nicolay Parra Vasquez** – Documentador y Soporte Técnico  

## Nombre de la Aplicación

La aplicación se llama **CarFleet**. 

### Logo de la Aplicación

![Logo de la Aplicación](https://echoes.solutions/wp-content/uploads/2022/08/CarFleet.webp)

## Tecnologías Utilizadas

Este proyecto ha sido desarrollado utilizando las siguientes tecnologías y herramientas:

- **Lenguaje**: Dart (para el desarrollo con Flutter)
- **Framework**: Flutter
- **Base de Datos**: [Firebase, SQLite]
- **Herramientas de Desarrollo**: Android Studio
- **Control de Versiones**: GitHub
- **Diseño**: Figma (para el diseño del logo)

## Instalación

Para instalar y ejecutar este proyecto en tu máquina local, sigue los siguientes pasos:

1. Clona el repositorio:

    ```bash
    git clone https://github.com/[tu-usuario]/[nombre-del-repositorio].git
    ```

2. Entra en el directorio del proyecto:

    ```bash
    cd GRUPO-01
    ```

3. Instala las dependencias de Flutter (asegúrate de tener **Flutter** instalado en tu máquina):

    ```bash
    flutter pub get
    ```

4. Abre el proyecto en **Android Studio**:

    - Si tienes Android Studio configurado con Flutter, solo abre el proyecto desde el IDE.
    - Si no lo tienes configurado, puedes seguir la [guía de instalación de Flutter](https://flutter.dev/docs/get-started/install).

5. Ejecuta la aplicación en tu emulador o dispositivo físico:

    ```bash
    flutter run
    ```

    O, si prefieres usar Android Studio, puedes hacer clic en el botón de "Run" directamente desde el IDE.

## Estructura del Proyecto

## 📁 Estructura del Proyecto

El código está organizado siguiendo el patrón de diseño **MVC (Modelo - Vista - Controlador)**. La estructura de directorios es la siguiente:

- **`/lib/`**
  - **`/models/`**: Clases que representan los datos (Usuario, Vehículo, etc.)
    - `user_model.dart`
    - `vehicle_model.dart`

  - **`/views/`**: Pantallas de la aplicación y widgets de interfaz
    - **`/auth/`**: Pantallas de Login y Registro
      - `login_view.dart`
      - `register_view.dart`
    - **`/home/`**: Pantalla de Inicio con listado de vehículos
      - `home_view.dart`
    - **`/vehicles/`**: Gestión de vehículos (CRUD)
      - `add_vehicle_view.dart`
      - `edit_vehicle_view.dart`
      - `vehicle_details_view.dart`
    - **`/widgets/`**: Componentes reutilizables
      - `vehicle_card.dart`

  - **`/controllers/`**: Lógica para manejar el flujo de datos y navegación
    - `auth_controller.dart`
    - `vehicle_controller.dart`
    - `session_controller.dart`

  - **`/services/`**: Servicios para autenticación y persistencia de datos
    - `auth_service.dart`
    - `vehicle_service.dart`
    - `database.dart`

  - **`/utils/`**: Funciones auxiliares, constantes, validaciones, etc.
    - `validators.dart`
    - `constants.dart`

  - **`main.dart`**: Punto de entrada de la aplicación

- **`/assets/`**: Recursos como imágenes, íconos, etc.
  - `logo.png`
  - **`/vehicles/`**: Imágenes de vehículos
    - `car1.jpg`
    - `car2.jpg`
    - `car3.jpg`

- **`/apk/`**: Archivo APK generado para entrega
  - `app-release.apk`

- **`/documentacion.pdf`**: Documento del modelo de navegación (con capturas y explicación)


# Recursos como imágenes, íconos y logo
/assets                  # Recursos como imágenes, íconos y logo
├── logo.png
└── vehicles/
    ├── car1.jpg
    ├── car2.jpg
    └── car3.jpg
# Archivo compilado para entrega
/apk                    # Archivo compilado para entrega
└── app-release.apk
# Modelo de navegación documentado con capturas y explicación
/documentacion.pdf      # Modelo de navegación documentado con capturas y explicación


