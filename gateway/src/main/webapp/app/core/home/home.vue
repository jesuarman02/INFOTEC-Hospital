<template>
  <!-- 🔥 WRAPPER NUEVO (CLAVE PARA EL BUG) -->
  <div class="home-wrapper">
    
    <div class="home-container">
      <div class="home">

        <!-- IMAGEN -->
        <div class="image-container">
          <span class="hipster img-fluid rounded"></span>
        </div>

        <!-- CONTENIDO -->
        <div class="content-box custom-layout">
          <h1 class="titulo-principal">Registro y Consulta de Pacientes</h1>
          <h2 class="bienvenido">Bienvenido!</h2>
          <p class="subtexto">esta es su pagina de inicio</p>

          <div>
            <div class="alert alert-success" v-if="authenticated">
              <span v-if="username" v-text="t$('home.logged.message', { username: username })"></span>
            </div>

            <div class="alert alert-warning" v-if="!authenticated">
              <span v-text="t$('global.messages.info.authenticated.prefix')"></span>
              <a class="alert-link" @click="openLogin()" v-text="t$('global.messages.info.authenticated.link')"></a>
              <span v-html="t$('global.messages.info.authenticated.suffix')"></span>
            </div>

            <div class="alert alert-warning" v-if="!authenticated">
              <span v-text="t$('global.messages.info.register.noaccount')"></span>&nbsp;
              <router-link class="alert-link" to="/register" v-text="t$('global.messages.info.register.link')"></router-link>
            </div>
          </div>

<!--
      <p v-text="t$('home.question')"></p>

      <ul>
        <li><a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer" v-text="t$('home.link.homepage')"></a></li>
        <li>
          <a
            href="https://stackoverflow.com/tags/jhipster/info"
            target="_blank"
            rel="noopener noreferrer"
            v-text="t$('home.link.stackoverflow')"
          ></a>
        </li>
        <li>
          <a
            href="https://github.com/jhipster/generator-jhipster/issues?state=open"
            target="_blank"
            rel="noopener noreferrer"
            v-text="t$('home.link.bugtracker')"
          ></a>
        </li>
        <li>
          <a
            href="https://gitter.im/jhipster/generator-jhipster"
            target="_blank"
            rel="noopener noreferrer"
            v-text="t$('home.link.chat')"
          ></a>
        </li>
        <li>
          <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer" v-text="t$('home.link.follow')"></a>
        </li>
      </ul>

      <p>
        <span v-text="t$('home.like')"></span>
        <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer" v-text="t$('home.github')"></a>!
      </p>
-->
        </div>
      </div>
    </div>

  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
<style scoped>
  /* Importar Google Fonts aquí o en index.html */
  @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700;800&display=swap');
  
  /* Importar tu CSS local */
  @import "../../../content/css/home.css";
</style>