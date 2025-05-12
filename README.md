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


## 📁 Estructura del Proyecto

El proyecto está organizado en varias carpetas clave que siguen el patrón de diseño MVC (Modelo - Vista - Controlador). La estructura básica es la siguiente:

/lib/: Contiene todo el código fuente de la aplicación.

/models/: Clases que representan los datos de la aplicación, como Usuario y Vehículo.

/views/: Archivos que gestionan la interfaz de usuario (pantallas y widgets), como la pantalla de Login, Registro, Inicio, y las pantallas de Gestión de Vehículos.

/controllers/: Lógica para controlar la interacción entre la vista y los modelos, incluyendo controladores para autenticación y gestión de vehículos.

/services/: Servicios relacionados con la persistencia de datos, como la conexión a la base de datos y autenticación de usuarios.

/utils/: Funciones auxiliares y constantes que se utilizan en todo el proyecto.

main.dart: El archivo principal que inicia la aplicación.

/assets/: Recursos estáticos como imágenes, íconos y el logo de la aplicación.

/apk/: Carpeta que contiene el archivo APK compilado de la aplicación.

/documentacion.pdf: Documento que explica el modelo de navegación de la aplicación, incluyendo capturas de pantalla y diagramas.
