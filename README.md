# House Reservation API

## Descripción
Esta es una API de reserva de casas que permite crear y listar reservas. La API también valida códigos de descuento a través de un servicio externo.

## Requisitos
- Java 21
- Maven 3.6+
- Docker

## Configuración del Proyecto
1. Clona el repositorio:

    ```sh
    git clone <URL del repositorio>
    cd house-reservation
    ```

2. Configura el archivo `application.properties` en `src/main/resources` con la URL de tu base de datos PostgreSQL:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    discount.api.url=https://sbv2bumkomidlxwffpgbh4k6jm0ydskh.lambda-url.us-east-1.on.aws/
    ```

3. Compila y empaqueta el proyecto sin Unit Test:
    ```sh
    ./mvnw clean package -DskipTests
    ```

## Ejecutar con Docker
1. Construye y ejecuta los contenedores Docker:

    ```sh
    docker-compose up --build
    ```

2. La aplicación estará disponible en `http://localhost:8080`.

## Endpoints

### Crear Reserva

- **URL**: `/bideafactory/book`
- **Método**: `POST`
- **Body**:
    ```json
    {
        "id": "1456088-4",
        "name": "Gonzalo",
        "lastname": "Pérez",
        "age": 33,
        "phoneNumber": "56988123222",
        "startDate": "2022-03-04",
        "endDate": "2022-03-04",
        "houseId": "213132",
        "discountCode": "D054A23"
    }
    ```

### Listar Reservas

- **URL**: `/bideafactory/book`
- **Método**: `GET`

## Pruebas Unitarias

Para ejecutar las pruebas unitarias:

```sh
./mvnw test
```

## Manejo de Errores
  - Si el parametro `houseId` no está presente en la solicitud, se devuelve un error `400 Bad Request`.
  - Si el código de descuento es inválido, se devuelve un error `409 Conflict`.
  - Si la API de descuento no responde en el tiempo esperado, se reintenta hasta 3 veces antes de devolver un error `409 Conflict`.

## Logs
  - La aplicación utiliza SLF4J con Logback para registrar logs de diferentes niveles (info, error, debug).
  - Los logs se configuran en el archivo `application.properties`.

## Autor
- David Arias