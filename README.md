# 🏥 INFOTEC-Hospital: Sistema de Gestión Clínica

¡Bienvenido al cierre del proyecto **INFOTEC-Hospital**! Este sistema es una plataforma integral de gestión de pacientes desarrollada con una arquitectura de microservicios moderna, diseñada para ofrecer una experiencia fluida, segura y profesional en el entorno hospitalario.

## 🏗️ Arquitectura del Proyecto

El sistema utiliza **JHipster** como base tecnológica, dividiendo las responsabilidades en dos componentes principales:

1.  **Gateway (`/gateway`):**
    * Contiene el punto de entrada principal del sistema.
    * Aloja la interfaz de usuario (Frontend) desarrollada en **Vue.js**.
    * Gestiona la autenticación y el enrutamiento de peticiones.
    * **Diseño:** Interfaz "Premium" con estilos *Dark Frosted Glass* y componentes reactivos.

2.  **Microservicio de Pacientes (`/pacientesms`):**
    * Gestiona el núcleo de la lógica médica (Backend).
    * Administra las entidades: Pacientes, Direcciones, Información Socioeconómica, Historial Médico y Signos Vitales.
    * Base de datos: **PostgreSQL**.

---

## 🛠️ Requisitos del Sistema

Para ejecutar este proyecto en tu entorno local, necesitas tener instalado:

* **Java 17** (LTS)
* **Node.js** (v18 o superior)
* **Docker & Docker Compose** (Crucial para la base de datos y el registro de servicios)
* **Maven** (Incluido en el proyecto como `./mvnw`)

---

## 🚀 Guía de Inicio Rápido (Setup)

Sigue este orden estricto para levantar el sistema correctamente. Se recomienda usar terminales independientes para cada paso.

### 1. Levantar la Infraestructura (Docker)
Primero, debemos encender el registro de servicios (Consul) y la base de datos.

**Terminal A (Consul):**
```bash
cd gateway
docker-compose -f src/main/docker/consul.yml up -d