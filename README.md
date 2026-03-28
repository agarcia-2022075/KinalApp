# KinalApp

Sistema de gestión de inventario y ventas desarrollado con Spring Boot. La aplicación expone una API RESTful para la administración eficiente de entidades clave como clientes, productos, usuarios y el proceso transaccional de ventas.

## Tecnologías utilizadas
* **Java 17**
* **SpringBoot 3.2.2**
* **Maven** (Gestor de dependencias)
* **MySQL** (Sistema Gestor de Base de Datos)

## Flujo Principal y Lógica de Negocio

El sistema está diseñado bajo una arquitectura relacional donde cada módulo interactúa para garantizar la integridad de los datos financieros:

1. **Gestión de Usuarios y Clientes:**
   * **Usuarios (`/usuarios`):** Son los empleados o administradores del sistema. Deben ser registrados previamente con correos válidos y roles específicos para poder operar las ventas.
   * **Clientes (`/clientes`):** Se registran utilizando su DPI como llave principal. Un cliente debe existir en el sistema para poder facturarle una venta.

2. **Gestión de Inventario (Productos):**
   * **Productos (`/productos`):** Módulo encargado del inventario. La API restringe mediante validaciones estrictas la creación de productos con inventario negativo o precios nulos, protegiendo así la coherencia de la tienda.

3. **Proceso Transaccional (Ventas y Detalles):**
   * **Creación de la Venta (`/ventas`):** El flujo de compra inicia cuando un **Usuario** (vendedor) abre una **Venta** para un **Cliente**. El sistema de backend está automatizado para asignar la fecha actual (`LocalDate.now()`) y establecer un total inicial de cero.
   * **Asignación de Detalles (`/detalles`):** El núcleo automatizado de la aplicación. Al agregar productos a una venta, el cliente web o Postman solo necesita enviar la cantidad deseada y los códigos del producto y la venta. **El backend se encarga automáticamente de buscar el precio real del producto en la base de datos y calcular el subtotal matemático**, evitando cualquier alteración de precios desde el lado del cliente.

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

---

## Endpoints Disponibles (Para pruebas en Postman)
El servidor se levanta por defecto en el puerto **8082**. A continuación, las rutas exactas y ejemplos JSON listos para copiar y pegar.

### 1. Módulo de Clientes (`/clientes`)
* **GET Todos:** `http://localhost:8082/clientes`
* **GET Activos:** `http://localhost:8082/clientes/activos`
* **GET por ID:** `http://localhost:8082/clientes/{dpi}`
* **DELETE por ID:** `http://localhost:8082/clientes/{dpi}`
* **POST (Crear) / PUT (Actualizar):** `http://localhost:8082/clientes` *(Para PUT agregar el /{dpi} en la URL)*

**Ejemplo de JSON (POST/PUT):**
```json
{
  "dpiCliente": "1000200030004",
  "nombreCliente": "Lionel",
  "apellidoCliente": "Messi",
  "direccion": "Miami, FL",
  "estado": 1
}
```

### 2. Módulo de Usuarios (`/usuarios`)
* **GET Todos:** `http://localhost:8082/usuarios`
* **GET Activos:** `http://localhost:8082/usuarios/activos`
* **GET por ID:** `http://localhost:8082/usuarios/{id}`
* **DELETE por ID:** `http://localhost:8082/usuarios/{id}`
* **POST (Crear) / PUT (Actualizar):** `http://localhost:8082/usuarios` *(Para PUT agregar el /{id} en la URL)*

**Ejemplo de JSON (POST/PUT):**
```json
{
  "username": "admin.01",
  "password": "SecurePassword123!",
  "email": "admin@kinal.edu.gt",
  "rol": "ADMIN",
  "estado": 1
}
```

### 3. Módulo de Productos (`/productos`)
* **GET Todos:** `http://localhost:8082/productos`
* **GET Activos:** `http://localhost:8082/productos/activos`
* **GET por ID:** `http://localhost:8082/productos/{id}`
* **DELETE por ID:** `http://localhost:8082/productos/{id}`
* **POST (Crear) / PUT (Actualizar):** `http://localhost:8082/productos` *(Para PUT agregar el /{id} en la URL)*

**Ejemplo de JSON (POST/PUT):**
```json
{
  "nombreProducto": "Laptop Dell XPS",
  "precio": 1250.50,
  "stock": 15,
  "estado": 1
}
```

### 4. Módulo de Ventas (`/ventas`)
* **GET Todos:** `http://localhost:8082/ventas`
* **GET Activos:** `http://localhost:8082/ventas/activos`
* **GET por ID:** `http://localhost:8082/ventas/{id}`
* **DELETE por ID:** `http://localhost:8082/ventas/{id}`
* **POST (Crear) / PUT (Actualizar):** `http://localhost:8082/ventas` *(Para PUT agregar el /{id} en la URL)*

*(Nota: Asigna automáticamente la fecha del sistema y el total inicial en 0)*

**Ejemplo de JSON (POST/PUT):**
```json
{
  "estado": 1,
  "cliente": {
    "dpiCliente": "1000200030004"
  },
  "usuario": {
    "codigoUsuario": 1
  }
}
```

### 5. Módulo de Detalles de Venta (`/detalles`)
* **GET Todos:** `http://localhost:8082/detalles`
* **GET por ID:** `http://localhost:8082/detalles/{id}`
* **DELETE por ID:** `http://localhost:8082/detalles/{id}`
* **POST (Crear) / PUT (Actualizar):** `http://localhost:8082/detalles` *(Para PUT agregar el /{id} en la URL)*

*(Nota: El sistema busca el precio real del producto y calcula el subtotal automáticamente)*

**Ejemplo de JSON (POST/PUT):**
```json
{
  "cantidad": 2,
  "producto": {
    "codigoProducto": 1
  },
  "venta": {
    "codigoVenta": 1
  }
}
```