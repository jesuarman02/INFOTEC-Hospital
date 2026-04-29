<template>
  <div id="app">
    <ribbon></ribbon>

    <div id="app-header">
      <jhi-navbar></jhi-navbar>
    </div>

    <div class="container-fluid p-0">

     <router-view v-if="['Calendario', 'InterfazPacientes'].includes(String($route.name))"></router-view>

      <div v-else class="card jh-card m-3">
        <router-view></router-view>
      </div>

      <b-modal id="login-page" hide-footer lazy hide-header-close>
        <template #modal-title>
          <span data-cy="loginTitle" id="login-title" v-text="t$('login.title')"></span>
        </template>
        <login-form></login-form>
      </b-modal>

      <jhi-footer></jhi-footer>

    </div>
  </div>
</template>

<script lang="ts" src="./app.component.ts"></script>
<style src="../../webapp/content/css/login.css"></style>  