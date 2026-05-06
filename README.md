# 🏥 INFOTEC-Hospital: Sistema de Gestión Hospitalaria

## 1. Acerca del Proyecto
Este es un Sistema de Gestión Hospitalaria de vanguardia creado para la consulta, registro y control de pacientes en clínicas y hospitales de México. Su propósito principal es facilitar y optimizar la digitalización de expedientes clínicos físicos, trasladándolos a un entorno web seguro, rápido y altamente escalable. 

A través de esta plataforma, el personal médico y administrativo puede concentrar en un solo lugar toda la información crítica del paciente: desde sus datos demográficos y dirección, hasta un registro exhaustivo de su información socioeconómica, signos vitales, alergias, enfermedades crónicas y su historial médico completo.

## 2. Funcionalidades Principales
El sistema está diseñado para cubrir el flujo de atención hospitalaria de principio a fin:
* **Asistente de Registro Inteligente (Wizard):** Un proceso guiado de 5 pasos para dar de alta pacientes sin perder información (Datos Personales, Dirección, Info. Socioeconómica, Historial Médico y Signos Vitales).
* **Gestión y Control de Citas:** Módulo integrado para agregar, visualizar y administrar citas médicas en un calendario.
* **Búsqueda Avanzada de Pacientes:** Búsqueda rápida a través del ECU (Expediente Clínico Único) o herramientas de rescate de información usando el CURP o Nombre/Apellidos para pacientes extranjeros.
* **Autoguardado y Control de Estado:** Sistema que guarda borradores localmente para evitar pérdida de datos, evaluando automáticamente si un expediente está *VACÍO*, *INCOMPLETO* o *COMPLETO*.
* **Dashboard Médico:** Paneles de control interactivos para visualizar el progreso del expediente del paciente al instante.

## 3. Tecnologías Utilizadas
Este proyecto está construido bajo una arquitectura moderna de microservicios, empleando las siguientes herramientas:
* **Arquitectura Base:** JHipster (Gateway API + Microservicios).
* **Frontend:** Vue.js 3, HTML5, CSS3 (con diseño UI/UX personalizado *Dark Frosted Glass*).
* **Backend:** Java 17, Spring Boot, Spring WebFlux.
* **Base de Datos:** PostgreSQL (v16.4) interactuando a través de R2DBC / Hibernate.
* **Mapeo y Migraciones:** MapStruct (para DTOs) y Liquibase (control de versiones de base de datos).
* **Infraestructura y Contenedores:** Docker, Docker Compose.
* **Descubrimiento de Servicios:** Consul.

## 4. Estructura del Proyecto
El repositorio principal está estructurado de la siguiente manera para separar responsabilidades:

```text
INFOTEC-HOSPITAL/
├── gateway/                 # Enrutador principal y Frontend (Vue.js) de la aplicación.
├── pacientesms/             # Microservicio Backend: Lógica de negocio de pacientes y expedientes.
├── *.jdl                    # Archivos de definición de dominio (JHipster Domain Language).
├── *.png                    # Diagramas entidad-relación de la base de datos.
├── package.json             # Dependencias globales del entorno.
└── README.md                # Documentación del proyecto.
```

## 5. Requisitos del Sistema
Para instalar y compilar el proyecto desde cero, el entorno de desarrollo debe cumplir con los siguientes requerimientos mínimos:
* **Sistema Operativo:** Ubuntu 22.04 LTS (o superior). *Compatible con WSL en Windows o distribuciones Linux.*
* **Java:** JDK 17 (Java Development Kit versión 17).
* **Node.js:** Versión 18 LTS (o superior) junto con `npm`.
* **Docker:** Motor de Docker y Docker Compose instalados y en ejecución.
* **Git:** Para la clonación y gestión del repositorio.
* **IDE Recomendado:** Visual Studio Code con el "Extension Pack for Java" y extensiones para Vue.

## 6. Guía de Instalación y Ejecución
Debido a la arquitectura de microservicios, los componentes deben iniciarse en un orden específico. Se recomienda utilizar **tres terminales distintas**.

**Paso A: Clonar el repositorio**
```bash
git clone <URL_DEL_REPOSITORIO>
cd INFOTEC-HOSPITAL
```

**Paso B: Levantar la Infraestructura (Docker)**
*En la Terminal 1 (Consul para el registro de servicios):*
```bash
cd gateway
docker-compose -f src/main/docker/consul.yml up -d
```
*En la Terminal 2 (Base de datos PostgreSQL en el puerto 5433):*
```bash
cd pacientesms
docker-compose -f src/main/docker/postgresql.yml up -d
```

**Paso C: Compilar e Iniciar el Microservicio Backend**
*En la Terminal 2 (Asegúrate de que los contenedores ya estén "Healthy"):*
```bash
# Dentro de la carpeta pacientesms/
./mvnw
```
*(Espera a que la consola muestre el mensaje "Application started").*

**Paso D: Compilar e Iniciar el Gateway (Frontend)**
*En la Terminal 3:*
```bash
cd gateway
./mvnw
```

**Paso E: Acceder al Sistema**
Abre tu navegador web e ingresa a: **http://localhost:9000**
*(Credenciales de administrador por defecto: Usuario: `admin` / Contraseña: `admin`)*.

## 7. Consejos Adicionales y Errores Comunes
Si encuentras problemas durante la compilación o ejecución, revisa estas soluciones frecuentes:

* **Error de "cannot be resolved to a type" en Mappers (VS Code):**
  Es un falso positivo de VS Code al no leer los archivos generados por MapStruct. En la terminal del microservicio ejecuta `./mvnw clean compile test-compile`. Luego, en VS Code presiona `Ctrl+Shift+P`, busca "Java: Clean Java Language Server Workspace" y reinicia.
* **Errores de Liquibase o Base de Datos trabada:**
  Si realizaste cambios en las entidades o Liquibase falla, reinicia la base de datos limpiando los volúmenes en Docker:
  ```bash
  docker-compose -f src/main/docker/postgresql.yml down -v
  docker-compose -f src/main/docker/postgresql.yml up -d
  ```
* **Puertos Ocupados:**
  Verifica que no tengas otras bases de datos corriendo. El Gateway usa el puerto de DB `5432` y el microservicio `pacientesms` usa el `5433`.

## 8. Créditos y Desarrolladores
Este sistema fue diseñado y desarrollado por el siguiente equipo de Ingeniería en Sistemas Computacionales:

* **Jesús Armando Ambrosio García**
* **Alan Sebastián Velazco Hernández**
* **Erik Alvarado Hernandes**