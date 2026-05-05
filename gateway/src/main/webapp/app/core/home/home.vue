<template>
  <div class="home-wrapper d-block p-0 m-0" style=" overflow-x:hidden;">
    
    <div class="home-container">
      <div class="home">

        <!-- IMAGEN (Logotipo Caduceo) -->
        <div class="image-container">
          <span class="hipster img-fluid rounded"></span>
        </div>

        <!-- CONTENIDO -->
        <div class="content-box custom-layout">
          <h1 class="titulo-principal">Registro y Consulta de Pacientes</h1>
          <h2 class="bienvenido">¡Bienvenido!</h2>
          
          <div class="auth-section">
            
            <!-- VISTA CUANDO EL USUARIO SÍ HA INICIADO SESIÓN -->
            <div v-if="authenticated" class="logged-in-container">
              <div class="alert alert-success shadow-sm mb-4">
                <span v-if="username" v-text="t$('home.logged.message', { username: username })" class="font-weight-bold"></span>
              </div>
              
              <!-- 🔥 NUEVO BOTÓN DE REDIRECCIÓN AL PANEL 🔥 -->
              <router-link to="/interfaz-pacientes" class="btn-start shadow-lg">
                Ir al Panel de Pacientes <span style="margin-left: 8px;">→</span>
              </router-link>
            </div>

            <!-- VISTA CUANDO EL USUARIO NO HA INICIADO SESIÓN -->
            <div class="alert alert-warning shadow-sm" v-if="!authenticated">
              <span v-text="t$('global.messages.info.register.noaccount')"></span><br>
              <router-link class="alert-link font-weight-bold mt-2 d-inline-block" to="/register" v-text="t$('global.messages.info.register.link')"></router-link>
            </div>
            
          </div>
        </div>

      </div>
    </div>

  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
<style scoped>
  /* Importar Google Fonts */
  @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700;800&display=swap');
  /* Importar CSS local */
  @import "../../../content/css/home.css";
</style>