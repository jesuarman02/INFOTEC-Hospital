<template>
  <footer class="footer">
    <div class="footer-container">
      
      <div class="footer-col">
        <img
          :src="'/content/images/logo_gob_mx.png'"
          alt="Logo Gobierno de México"
          class="logo-gob"
        />
      </div>

      <div class="footer-col">
        <h3 class="footer-title">Enlaces</h3>
        <nav class="footer-nav">
          <a href="https://participa.gob.mx/" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Participa</a>
          <a href="http://www.ordenjuridico.gob.mx/" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Marco jurídico</a>
          <a href="https://consultapublicamx.plataformadetransparencia.org.mx/" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Plataforma Nacional de Transparencia</a>
          <a href="https://transparencia.gob.mx/home.html" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Transparencia para el pueblo</a>
          <a href="https://alertadores.buengobierno.gob.mx/" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Alerta</a>
        </nav>
      </div>

      <div class="footer-col">
        <h3 class="footer-title">¿Qué es gob.mx?</h3>
        <p class="footer-text">
          Es el portal único de trámites, información y participación ciudadana.
          <a href="https://www.gob.mx/que-es-gobmx" target="_blank" rel="noopener noreferrer" class="footer-link-inline" title="Abre en una nueva ventana">Leer más</a>
        </p>
        <nav class="footer-nav">
          <a href="https://datos.gob.mx/" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Portal de datos abiertos</a>
          <a href="https://www.gob.mx/accesibilidad" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Declaración de Accesibilidad</a>
          <a href="https://www.gob.mx/terminos" target="_blank" rel="noopener noreferrer" class="footer-link" title="Abre en una nueva ventana">Términos y Condiciones</a>
        </nav>
      </div>

      <div class="footer-col">
        <a href="https://sidec.buengobierno.gob.mx/#!/" target="_blank" rel="noopener noreferrer" class="footer-link footer-link-main" title="Abre en una nueva ventana">Denuncia contra servidores públicos</a>
        
        <h3 class="footer-title mt-spaced">Síguenos en</h3>
        <div class="social-links">
          <a href="https://www.facebook.com/gobmexico" target="_blank" rel="noopener noreferrer" class="social-link">
            <img :src="'/content/images/facebook.png'" alt="Facebook" class="social-icon" />
          </a>
          <a href="https://x.com/GobiernoMX" target="_blank" rel="noopener noreferrer" class="social-link">
            <img :src="'/content/images/twitter.png'" alt="X / Twitter" class="social-icon" />
          </a>
          <a href="https://www.instagram.com/gobmexico/" target="_blank" rel="noopener noreferrer" class="social-link">
            <img :src="'/content/images/instagram.png'" alt="Instagram" class="social-icon" />
          </a>
          <a href="https://www.youtube.com/@gobiernodemexico" target="_blank" rel="noopener noreferrer" class="social-link">
            <img :src="'/content/images/youtube.png'" alt="YouTube" class="social-icon" />
          </a>
        </div>

        <a href="tel:+079" target="_blank" class="contact-banner">
          <img :src="'/content/images/079.png'" alt="Llama al 079" class="contact-img" />
        </a>
      </div>

    </div>
  </footer>
</template>

<script lang="ts" src="./jhi-footer.component.ts"></script>

<style scoped src="../../../content/css/jhi-footer.css"></style>

