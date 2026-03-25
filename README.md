# KinalApp

Sistema de gestión de inventario y ventas desarrollado con Spring Boot. La aplicación expone una API RESTful para la administración eficiente de entidades clave como clientes y productos.

## Tecnologías utilizadas
* **Java 17**
* **SpringBoot 3.2.2**
* **Maven** (Gestor de dependencias)
* **MySQL** (Sistema Gestor de Base de Datos)

## Requisitos Previos
Antes de ejecutar la aplicación, debe tener instalado:
* JDK 17 o superior.
* Maven instalado (o puede usar el Wrapper incluido en el proyecto).
* Una instancia activa de MySQL ejecutándose en el puerto 3306.

## Instalación y Ejecución

**1. Clonar el repositorio**
Abra su terminal y ejecute el siguiente comando para clonar el proyecto:
`git clone https://github.com/agarcia-2022075/KinalApp.git`

**2. Navegar al directorio del proyecto**
`cd KinalApp`

**3. Configuración de la Base de Datos**
El proyecto está configurado para crear la base de datos automáticamente (`dbClientes_in5am`). Si sus credenciales locales de MySQL son diferentes, por favor actualice las siguientes líneas en el archivo `src/main/resources/application.properties`:
* `spring.datasource.username= SU_USUARIO`
* `spring.datasource.password= SU_CONTRASEÑA`

**4. Ejecutar la aplicación**
Levante el servidor de Spring Boot con el siguiente comando:
`mvn spring-boot:run`

**5. Probar la API**
Una vez que la aplicación esté corriendo, el servidor se levantará en el puerto **8082**. Puede probar los endpoints (ej. `http://localhost:8082/clientes`) utilizando Postman o su navegador.